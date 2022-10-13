package fr.eljugador.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import fr.eljugador.domain.Course;
import fr.eljugador.domain.CourseSum;
import fr.eljugador.domain.Produit;

public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper instance;
    private static final String DB_NAME = "COURSE.sqlite";
    private static final int DB_VERSION = 5;

    public static DbHelper getInstance(Context context) {
        if (instance == null) instance = new DbHelper(context);
        return instance;
    }

    private DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String sql = DbConstants.getCreationScript();
        for (String singleDDL : sql.split(DbConstants.SQL_SEPARATOR)) {
            db.execSQL(singleDDL);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = DbConstants.getDropScript();
        for (String singleDDL : sql.split(DbConstants.SQL_SEPARATOR)) {
            db.execSQL(singleDDL);
        }
        onCreate(db);
    }

    public Course getOpenedCourse() {
        Course course = null;
        String sql = "SELECT ID FROM COURSE WHERE CLOSING_DATE IS NULL";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            course = new Course();
            course.setId(cursor.getLong(0));
        }
        return course;
    }

    public long insertCourse(Course course) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("AMOUNT", course.getAmount());
        contentValues.put("CLOSING_DATE", course.getClosingDateTime());

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(DbConstants.COURSE_TABLE, null, contentValues);
        return id;
    }

    public void updateCourse(Course course) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("AMOUNT", course.getAmount());
        contentValues.put("CLOSING_DATE", course.getClosingDateTime());

        String where = "ID=?";
        String[] whereArgs = {String.valueOf(course.getId())};

        SQLiteDatabase db = getWritableDatabase();
        db.update(DbConstants.COURSE_TABLE, contentValues, where, whereArgs);
    }

    public List<Produit> getProduitsByCourseId(long courseId) {
        List<Produit> produits = new ArrayList<>();

        String sql = "SELECT ID, NAME FROM " + DbConstants.PRODUIT_TABLE + " WHERE ID_COURSE=?";
        String[] whereArgs = {String.valueOf(courseId)};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, whereArgs);
        if (cursor.moveToFirst()) {
            do {
                Produit produit = new Produit();
                produit.setId(cursor.getLong(0));
                produit.setName(cursor.getString(1));
                produits.add(produit);
            } while (cursor.moveToNext());
        }
        return produits;
    }

    public List<Produit> getTopProducts(int limitProduits) {
        List<Produit> produits = new ArrayList<>();

        String sql = "SELECT ID, NAME " +
                "FROM PRODUIT " +
                "GROUP BY NAME " +
                "ORDER BY COUNT(NAME) DESC " +
                "LIMIT ?";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(limitProduits)});

        if (cursor.moveToFirst()) {
            do {
                Produit produit = new Produit();
                produit.setId(cursor.getLong(0));
                produit.setName(cursor.getString(1));
                produits.add(produit);
            } while (cursor.moveToNext());
        }
        return produits;
    }

    public long insertProduit(Produit produit) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", produit.getName());
        contentValues.put("ID_COURSE", produit.getCourse().getId());

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(DbConstants.PRODUIT_TABLE, null, contentValues);
        return id;
    }

    public List<CourseSum> getCourseSums() {
        List<CourseSum> courseSumList = new ArrayList<>();
        String sql = "SELECT SUM(AMOUNT) as AMOUNT, strftime(\"%m\",datetime(CLOSING_DATE/1000, 'unixepoch')) as MONTH, strftime(\"%Y\",datetime(CLOSING_DATE/1000, 'unixepoch')) as YEAR " +
                "FROM COURSE " +
                "WHERE CLOSING_DATE IS NOT NULL " +
                "GROUP BY " +
                "strftime(\"%m\",datetime(CLOSING_DATE/1000, 'unixepoch')), strftime(\"%Y\",datetime(CLOSING_DATE/1000, 'unixepoch'))";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                CourseSum courseSum = new CourseSum();
                courseSum.setAmount(cursor.getFloat(0));
                courseSum.setMonth(cursor.getInt(1));
                courseSum.setYear(cursor.getInt(2));
                courseSumList.add(courseSum);
            } while (cursor.moveToNext());
        }
        return courseSumList;
    }

    public List<Course> getCourseByMonth(int month, int year) {
        List<Course> courseList = new ArrayList<>();
        String sql = "SELECT ID, AMOUNT, CLOSING_DATE " +
                "FROM COURSE " +
                "WHERE CLOSING_DATE IS NOT NULL " +
                "AND strftime(\"%m\",datetime(CLOSING_DATE/1000, 'unixepoch'))=? " +
                "AND strftime(\"%Y\",datetime(CLOSING_DATE/1000, 'unixepoch'))=?";

        String[] whereArgs = {String.valueOf(month), String.valueOf(year)};

        Cursor cursor = getReadableDatabase().rawQuery(sql, whereArgs);
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setId(cursor.getLong(0));
                course.setAmount(cursor.getFloat(1));
                course.setClosingDate(new Date(cursor.getLong(2)));
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        return courseList;
    }
}
