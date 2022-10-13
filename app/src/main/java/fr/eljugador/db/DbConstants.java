package fr.eljugador.db;

public class DbConstants {
    public static final String COURSE_TABLE = "COURSE";
    public static final String PRODUIT_TABLE = "PRODUIT";
    public static final String SQL_SEPARATOR = "; ";

    public static String getCreationScript() {
        final String createCourseTable = "CREATE TABLE %s (" +
                "ID INTEGER PRIMARY KEY," +
                "AMOUNT REAL," +
                "CLOSING_DATE DATE" +
                ")";

        final String createProduitTable = "CREATE TABLE %s (" +
                "ID INTEGER PRIMARY KEY," +
                "NAME TEXT," +
                "ID_COURSE INTEGER," +
                "FOREIGN KEY(ID_COURSE) REFERENCES %s(ID)" +
                ")";

        final String script = createCourseTable + SQL_SEPARATOR + createProduitTable;
        return String.format(script, COURSE_TABLE, PRODUIT_TABLE, COURSE_TABLE);
    }

    public static String getDropScript() {
        final String dropCourseTable = "DROP TABLE IF EXISTS " + COURSE_TABLE;
        final String dropProduitTable = "DROP TABLE IF EXISTS " + PRODUIT_TABLE;
        return dropCourseTable + SQL_SEPARATOR + dropProduitTable;
    }
}
