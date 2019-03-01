package com.carbonapps.afrocharger.prefrence;

import android.content.Context;
import android.graphics.Color;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.carbonapps.afrocharger.R;

public class mPreference extends Preference {
    public mPreference(Context context) {
        super(context);
    }

    public mPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public mPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    protected void onBindView(View view) {
        super.onBindView(view);
        TextView textView = (TextView) view.findViewById(android.R.id.title);
        ((TextView) view.findViewById(android.R.id.summary)).setTextColor(Color.WHITE);
        textView.setTextColor(Color.WHITE);
    }
}