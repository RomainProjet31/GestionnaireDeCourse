package fr.eljugador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

import fr.eljugador.db.DbHelper;
import fr.eljugador.domain.Course;

public class ReportingActivity extends AppCompatActivity {

    private DbHelper dbHelper;
    private TextView reportingMonthTxt;
    private ArrayList<BarEntry> barEntriesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);
        BarChart barChart = findViewById(R.id.reporting_barchart);
        reportingMonthTxt = findViewById(R.id.reporting_monthLabel);
        dbHelper = DbHelper.getInstance(getBaseContext());

        barEntriesArrayList = new ArrayList<>();
        initCourseByMonth();
        BarDataSet barDataSet = new BarDataSet(barEntriesArrayList, "Montant des dépenses par jour");

        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(20f);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setGranularity(1f);
    }

    private void initCourseByMonth() {
        Intent intent = getIntent();
        int month = intent.getIntExtra("SHOPPING_MONTH", 1);
        int year = intent.getIntExtra("SHOPPING_YEAR", 1);
        reportingMonthTxt.setText(month + "/" + year);

        for (Course course : dbHelper.getCourseByMonth(month, year)) {
            int day = course.getClosingDate().getDate();
            barEntriesArrayList.add(new BarEntry(day, course.getAmount()));
        }
    }
}