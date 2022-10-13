package fr.eljugador.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import fr.eljugador.databinding.FragmentNotificationsBinding;
import fr.eljugador.db.DbHelper;
import fr.eljugador.domain.Produit;
import fr.eljugador.ui.utils.UiUtils;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private LinearLayout linearLayoutProduits;
    private DbHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        linearLayoutProduits = binding.notificationLinearProducts;
        dbHelper = DbHelper.getInstance(getContext());
        initView();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initView() {
        for (Produit produit : dbHelper.getTopProducts(10)) {
            CardView cardView = UiUtils.getCardView(getContext(), produit.getName());
            linearLayoutProduits.addView(cardView);
        }
    }
}