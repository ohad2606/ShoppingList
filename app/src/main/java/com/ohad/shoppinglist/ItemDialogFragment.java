package com.ohad.shoppinglist;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ItemDialogFragment extends DialogFragment {

    private AutoCompleteTextView txtItemName;
    public TextView lblShowTotal;
    public EditText txtAddPrice;
    public EditText txtAddAmount;
    private ImageButton btnSaveItem, btnCancelItem;
    private boolean isEdit;
    private Item itemToBeEdited;
    private ItemEditListener itemEditListener;
    private View view;
    public String[] itemsNames = AppStrings.items;
    private String totalSum = "0.00";
    private int amount;
    float price, sum;

    public void setListener(ItemEditListener editListener){
        this.itemEditListener = editListener;
    }

    public void setItemToBeEdited(Item itemToBeEdited){
        if(itemToBeEdited != null){
            this.itemToBeEdited = itemToBeEdited;
            isEdit = true;
        }else{
            isEdit = false;
        }
    }

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String itemName = txtItemName.getText().toString().trim();
            if (!itemName.isEmpty()){
                Item item = new Item();
                item.setItemName(itemName);
                item.setItemAmount(Integer.parseInt(txtAddAmount.getText().toString()));
                item.setItemSum(Float.parseFloat(lblShowTotal.getText().toString()));
                itemEditListener.newItem(item);
            }dismiss();
        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_item, container, false);
        findViews();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,itemsNames);
        txtItemName.setThreshold(1);
        txtItemName.setAdapter(adapter);
        txtItemName.addTextChangedListener(txtChangeListener);
        lblShowTotal.setText(totalSum);
        txtAddAmount.setText("1");
        btnSaveItem.setAlpha(0.5f);
        btnSaveItem.setEnabled(false);
        txtAddAmount.addTextChangedListener(textWatcher);
        txtAddPrice.addTextChangedListener(textWatcher);
        btnSaveItem.setOnClickListener(saveListener);
        btnCancelItem.setOnClickListener(cancelListener);
        if(isEdit) {
            txtItemName.setText(itemToBeEdited.getItemName());
            txtAddPrice.setText((int) itemToBeEdited.getItemPrice());
            txtAddAmount.setText(itemToBeEdited.getItemAmount());
            lblShowTotal.setText((int) itemToBeEdited.getItemSum());
        }return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {}
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            String addAmount = txtAddAmount.getText().toString().trim();
            String addPrice = txtAddPrice.getText().toString().trim();
            if (addAmount.isEmpty() || addPrice.isEmpty()) {
                amount = 0;
                price = 0;
                lblShowTotal.setText("0.00");
            }else{
                amount = Integer.parseInt(addAmount);
                price = Float.parseFloat(addPrice);
                calTotalsum(amount, price);
                lblShowTotal.setText(String.valueOf(sum));
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            itemEditListener = (ItemEditListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    private void findViews(){
        txtItemName = view.findViewById(R.id.txtItemName);
        lblShowTotal = view.findViewById(R.id.lblShowTotal);
        txtAddAmount = view.findViewById(R.id.txtAddAmount);
        txtAddPrice = view.findViewById(R.id.txtAddPrice);
        btnSaveItem = view.findViewById(R.id.btnSaveItem);
        btnCancelItem = view.findViewById(R.id.btnCancelItem);
    }

    TextWatcher txtChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0){
                btnSaveItem.setAlpha(1f);
                btnSaveItem.setEnabled(true);
            }else{
                btnSaveItem.setAlpha(0.2f);
                btnSaveItem.setEnabled(false);
            }
        }
    };

    void calTotalsum(int amount, float price){
        sum = amount * price;
    }

    public interface ItemEditListener{
        void newItem(Item item);
    }
}
