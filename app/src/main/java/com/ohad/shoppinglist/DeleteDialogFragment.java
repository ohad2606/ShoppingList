package com.ohad.shoppinglist;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DeleteDialogFragment extends DialogFragment {

    TextView lblTitle;
    Button btnYes, btnNo;
    YesNoListener yesNoListener;

    public void setYesNoListener(YesNoListener yesNoListener){
        this.yesNoListener = yesNoListener;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean yes = v.getId() == R.id.btnYes;
            if(yesNoListener != null)
                yesNoListener.onFinishYesNo(yes);
            dismiss();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_delete, container, false);
        lblTitle = view.findViewById(R.id.lblTitle);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(listener);
        btnNo.setOnClickListener(listener);
        return view;
    }

    public interface YesNoListener{
        void onFinishYesNo(boolean yes);
    }
}
