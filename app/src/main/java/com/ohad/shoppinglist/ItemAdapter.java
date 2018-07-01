package com.ohad.shoppinglist;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private List<Item> items;
    private ItemDao itemDao = MainActivity.itemDao;
    Context context;
    ImageButton btnDeleteItem;
    CheckBox chkDone;


    private View.OnClickListener deleteItem = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    int pos = (int) v.getTag();
                    itemDao.delete(items.get(pos));
                    items.remove(items.get(pos));
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    notifyDataSetChanged();
                    MainActivity.priceTotal();
                    Toast.makeText(context, "המוצר נמחק", Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }
    };

    public ItemAdapter(Context context, int resource, List<Item> items){
        super(context, resource, items);
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
        }
        final TextView lblRowItemName = convertView.findViewById(R.id.lblRowItemName);
        final TextView lblRowAmountItem = convertView.findViewById(R.id.lblRowAmountItem);
        final TextView lblRowSum = convertView.findViewById(R.id.lblRowSum);
        lblRowItemName.setText(String.valueOf(getItem(position).getItemName()));
        lblRowAmountItem.setText(String.valueOf(getItem(position).getItemAmount()));
        lblRowSum.setText(String.valueOf(getItem(position).getItemSum()));
        btnDeleteItem = convertView.findViewById(R.id.btnDeleteItem);
        btnDeleteItem.setOnClickListener(deleteItem);
        chkDone = convertView.findViewById(R.id.chkDone);
        chkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lblRowItemName.setPaintFlags(lblRowItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    lblRowAmountItem.setPaintFlags(lblRowAmountItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    lblRowSum.setPaintFlags(lblRowSum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    lblRowItemName.setPaintFlags(0);
                    lblRowAmountItem.setPaintFlags(0);
                    lblRowSum.setPaintFlags(0);
                }
            }
        });
        btnDeleteItem.setTag(position);
        return convertView;
    }

    void itemCheckUpdate(){

    }

}

