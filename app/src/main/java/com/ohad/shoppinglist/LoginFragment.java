package com.ohad.shoppinglist;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginFragment extends DialogFragment {

    private View view;
    int curenntIcon = 2;
    TextView lblGuide;
    Button btnNext, btnSkip;
    ImageView icon01, icon02, icon03, icon04;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        setView();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconAlpha(curenntIcon);
                curenntIcon++;
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }


    private void setView() {
        lblGuide = view.findViewById(R.id.lblGuide);
        btnNext = view.findViewById(R.id.btnNext);
        btnSkip = view.findViewById(R.id.btnSkip);
        icon01 = view.findViewById(R.id.icon01);
        icon02 = view.findViewById(R.id.icon02);
        icon02.setAlpha(0.2f);
        icon03 = view.findViewById(R.id.icon03);
        icon03.setAlpha(0.2f);
        icon04 = view.findViewById(R.id.icon04);
        icon04.setAlpha(0.2f);
    }

    private void iconAlpha(int i) {
        if (i == 2) {
            lblGuide.setText("שיתוף הרשימה באמצעות הודעת sms או whatsapp");
            icon01.setAlpha(0.1f);
            icon02.setAlpha(1f);
            icon03.setAlpha(0.1f);
            icon04.setAlpha(0.1f);
        }
        if (i == 3) {
            lblGuide.setText("ניווט לחנות באמצעות הקלדת שם החנות");
            icon01.setAlpha(0.1f);
            icon02.setAlpha(0.1f);
            icon03.setAlpha(1f);
            icon04.setAlpha(0.1f);
        }
        if (i == 4) {
            lblGuide.setText("מחיקת הרשימה");
            icon01.setAlpha(0.1f);
            icon02.setAlpha(0.1f);
            icon03.setAlpha(0.1f);
            icon04.setAlpha(1f);
        }
        if(i == 5){
            dismiss();
        }
    }
}