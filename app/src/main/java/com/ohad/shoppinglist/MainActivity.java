package com.ohad.shoppinglist;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemDialogFragment.ItemEditListener, DeleteDialogFragment.YesNoListener {

    private AutoCompleteTextView lblStoreName;
    private List<Item> items;
    private ListView lstItems;
    private static TextView lblTotalStore;
    private ItemAdapter itemAdapter;
    public static ItemDao itemDao;
    private AppDatabase db;
    private String txt;
    static float price = 0.0f;
    final String PREFS_NAME = "firstRun";
    String[] stores = AppStrings.stores;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        lstSet();
        priceTotal();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stores);
        lblStoreName.setThreshold(1);
        lblStoreName.setAdapter(adapter);
        lblStoreName.clearFocus();
        itemAdapter = new ItemAdapter(MainActivity.this, R.layout.row_item, items);
        lstItems.setAdapter(itemAdapter);
        SharedPreferences firstRun = getSharedPreferences(PREFS_NAME, 0);
        if (firstRun.getBoolean("my_first_time", true)) {
            LoginFragment guide = new LoginFragment();
            guide.setCancelable(false);
            guide.show(getFragmentManager(), "");
            firstRun.edit().putBoolean("my_first_time", false).commit();
        }
    }

    public void btnAddItem(View view) {
        ItemDialogFragment dialogFragment = new ItemDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.setListener(this);
        dialogFragment.setItemToBeEdited(null);
        dialogFragment.show(getFragmentManager(),"");
    }
    public void btnSendTo(View view) {
        txt = "משתף איתך רשימת קניות:";
        listToSend();
    }

    public void btnWaze(View view) {
        String strName = lblStoreName.getText().toString();
        if(!strName.isEmpty()) {
            String uri = "waze://?q="+strName;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }else{
            Toast.makeText(this, "אנא הכנס שם חנות", Toast.LENGTH_SHORT).show();
        }
    }
    public void btnDeleteStore(View view) {
        DeleteDialogFragment fragment = new DeleteDialogFragment();
        fragment.setCancelable(false);
        fragment.setYesNoListener(this);
        fragment.show(getFragmentManager(),"yes no");
    }

    private void findViews(){
        lblStoreName = findViewById(R.id.lblStoreName);
        lblTotalStore = findViewById(R.id.lblTotalStore);
        lstItems = findViewById(R.id.lstItems);
    }

    public void lstSet(){
        items = new ArrayList<>();
        if (db == null) {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name").fallbackToDestructiveMigration().build();
        }
        itemDao = db.itemDao();
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                items.clear();
                items.addAll(itemDao.getAll());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                itemAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public static void priceTotal(){
        price = 0.0f;
        final ArrayList<Item> itemsToTotal = new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                itemsToTotal.addAll(itemDao.getAll());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                for (int i = 0; i < itemsToTotal.size(); i++) {
                    price += itemsToTotal.get(i).getItemSum();
                }
                lblTotalStore.setText(String.valueOf((float)price));
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void newItem(final Item item) {
        new AsyncTask<Item,Void,Void>(){
            @Override
            protected Void doInBackground(Item... items) {
                Item tempItem = items[0];
                int maxId = itemDao.getMaxId();
                tempItem.setUid(maxId+1);
                itemDao.insert(tempItem);
                MainActivity.this.items.add(tempItem);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                itemAdapter.notifyDataSetChanged();
            }
        }.execute(item);
        priceTotal();
    }

    @Override
    public void onFinishYesNo(boolean yes) {
        if (yes) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    itemDao.deleteAll();
                    MainActivity.this.items.removeAll(items);
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    itemAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "הרשימה נמחקה בהצלחה", Toast.LENGTH_SHORT).show();
                }
            }.execute();
            lblTotalStore.setText("0.00");
        }
    }

    public void listToSend(){
        final ArrayList<Item> itemsToSend = new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                itemsToSend.addAll(itemDao.getAll());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                if(itemsToSend.size() > 0) {
                    for (int i = 0; i < itemsToSend.size(); i++) {
                        txt += "\n"+itemsToSend.get(i).getItemName()+" -"+itemsToSend.get(i).getItemAmount()+" יח'. ";
                    }
                    txt += "\n" + "סכום הקנייה המשוער הוא: " + "₪"+ price;
                    Intent sendList = new Intent(Intent.ACTION_SEND);
                    sendList.setType("text/plain");
                    sendList.putExtra(Intent.EXTRA_TEXT, txt);
                    startActivity(Intent.createChooser(sendList, "בחר אפליקציה:"));
                }else{
                    Toast.makeText(MainActivity.this, "הכנס פריטים חדשים", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        priceTotal();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db != null)
            db.close();
    }
}