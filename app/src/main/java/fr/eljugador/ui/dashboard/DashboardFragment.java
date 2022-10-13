package fr.eljugador.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import fr.eljugador.ReportingActivity;
import fr.eljugador.databinding.FragmentDashboardBinding;
import fr.eljugador.db.DbHelper;
import fr.eljugador.domain.CourseSum;
import fr.eljugador.ui.utils.UiUtils;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private LinearLayout sumsLinearLayout;
    private TextView textViewCurrentSum;
    private DbHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dbHelper = DbHelper.getInstance(getContext());
        textViewCurrentSum = binding.dashboardCurrentAmount;
        sumsLinearLayout = binding.dashboardLinearLayoutSums;

        initCourseSums();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initCourseSums() {
        for (CourseSum courseSum : dbHelper.getCourseSums()) {
            addCourseSum(courseSum);
            addToCurrentAmountIfCurrentMonth(courseSum);
        }
    }

    private void addCourseSum(CourseSum courseSum) {
        CardView cardView = UiUtils.getCardView(getContext(), courseSum.toString());
        cardView.setOnClickListener((View) -> {
            Intent intent = new Intent(getActivity(), ReportingActivity.class);
            intent.putExtra("SHOPPING_MONTH", courseSum.getMonth());
            intent.putExtra("SHOPPING_YEAR", courseSum.getYear());
            startActivity(intent);
        });
        sumsLinearLayout.addView(cardView);
    }

    private void addToCurrentAmountIfCurrentMonth(CourseSum courseSum) {
        Calendar calendar = Calendar.getInstance();
        boolean sameMonth = calendar.get(Calendar.MONTH) + 1 == courseSum.getMonth();
        boolean sameYear = calendar.get(Calendar.YEAR) == courseSum.getYear();
        if (sameMonth && sameYear) {
            textViewCurrentSum.setText(String.format("%.2f €", courseSum.getAmount()));
        }
    }
}