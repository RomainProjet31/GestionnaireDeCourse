package fr.eljugador.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.sql.Date;
import java.util.Calendar;
import java.util.regex.Pattern;

import fr.eljugador.databinding.FragmentHomeBinding;
import fr.eljugador.db.DbHelper;
import fr.eljugador.domain.Course;
import fr.eljugador.domain.Produit;
import fr.eljugador.ui.utils.UiUtils;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LinearLayout linearLayoutProduits;
    private TextView txtViewError;
    private EditText etAddCourse;
    private EditText etSetAmount;
    private Button btnAddCourse;
    private Button btnCloseCourse;
    private DbHelper dbHelper;
    private Course currentCourse;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        etAddCourse = binding.homeEditText;
        txtViewError = binding.homeErrorMsg;
        btnAddCourse = binding.homeBtnAddCourse;
        etSetAmount = binding.homeEditTextSetAmount;
        btnCloseCourse = binding.homeBtnCloseCourse;
        linearLayoutProduits = binding.homeProductLinearLayout;
        dbHelper = DbHelper.getInstance(getContext());

        initListeners();
        initView();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initListeners() {
        btnAddCourse.setOnClickListener((view) -> {
            String productName = etAddCourse.getText().toString();
            if (productName != null && productName.length() > 0) {
                Produit produit = new Produit();
                produit.setCourse(currentCourse);
                produit.setName(productName);
                addProduitToCourse(produit);
                etAddCourse.getText().clear();
            }
        });

        btnCloseCourse.setOnClickListener((View) -> {
            txtViewError.setVisibility(View.INVISIBLE);
            if (checkForm()) {
                String strAmount = etSetAmount.getText().toString();
                currentCourse.setAmount(Float.parseFloat(strAmount));
                Calendar calendar = Calendar.getInstance();
                currentCourse.setClosingDate(new Date(calendar.get(Math.toIntExact(calendar.getTime().getTime()))));
                dbHelper.updateCourse(currentCourse);
                reinitView();
            } else {
                txtViewError.setText("Il doit y avoir au moins un produit et le prix doit être renseigné");
                txtViewError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void reinitView() {
        linearLayoutProduits.removeAllViews();
        etAddCourse.getText().clear();
        etSetAmount.getText().clear();
        initView();
    }

    private void initView() {
        txtViewError.setVisibility(View.INVISIBLE);
        currentCourse = dbHelper.getOpenedCourse();
        if (currentCourse == null) {
            currentCourse = new Course();
            currentCourse.setId(dbHelper.insertCourse(currentCourse));
        }
        currentCourse.setProducts(dbHelper.getProduitsByCourseId(currentCourse.getId()));
        for (Produit produit : currentCourse.getProducts()) {
            addProduitToView(produit);
        }
    }

    private void addProduitToCourse(Produit produit) {
        dbHelper.insertProduit(produit);
        currentCourse.getProducts().add(produit);
        addProduitToView(produit);
    }

    private void addProduitToView(Produit produit) {
        linearLayoutProduits.addView(UiUtils.getCardView(getContext(), produit.getName()));
    }

    private boolean checkForm() {
        boolean produitOK = currentCourse.getProducts().size() > 0;
        boolean amountOK = etSetAmount.getText().toString() != null && etSetAmount.getText().length() > 0;
        return produitOK && amountOK;
    }
}