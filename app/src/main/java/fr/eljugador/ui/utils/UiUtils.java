package fr.eljugador.ui.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class UiUtils {

    public static CardView getCardView(Context context, Object content) {
        CardView cardView = new CardView(context);
        cardView.setCardBackgroundColor(Color.rgb(102, 0, 204));

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 0);
        cardView.setLayoutParams(layoutParams);

        TextView textView = new TextView(context);
        textView.setText(content.toString());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(Color.WHITE);
        cardView.addView(textView);
        return cardView;
    }
}
