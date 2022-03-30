package com.example.danhba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ConTact> list_Contact;
    private AppDatabase1 appDatabase;
    private DanhBaAdapter adapter;
    private ContactDAO contactDAO;
    private FloatingActionButton btnAdd;
    private Fragment_Add fragment_add;

    @Override
    public void onBackPressed() {
        int backStack_Count = getSupportFragmentManager().getBackStackEntryCount();
        if(backStack_Count > 0 ){
            getSupportFragmentManager().popBackStack();
            btnAdd.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rclView);
        btnAdd = findViewById(R.id.btnAdd);
        list_Contact = new ArrayList<>();

        appDatabase = AppDatabase1.getInstance(this);
        contactDAO = appDatabase.ConTactDAO();
        adapter = new DanhBaAdapter(list_Contact, new BackToInf() {
            @Override
            public void Back_Fragment1() {
                getSupportFragmentManager().popBackStack();
                btnAdd.setEnabled(true);
            }

            @Override
            public void BackInf(Fragment fragment) {
                backToFrag(fragment);
                btnAdd.setEnabled(false);
            }
        }, new Case_Interface() {
            @Override
            public void Back_Fragment() {
                getSupportFragmentManager().popBackStack();
                btnAdd.setEnabled(true);
            }

            @Override
            public void Add_User(ConTact conTact, boolean Up_or_Add) {
                if (Up_or_Add)
                    AddContact(conTact);
                else
                    UpdateContact(conTact);
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_add = new Fragment_Add(new Case_Interface() {
                    @Override
                    public void Back_Fragment() {
                        getSupportFragmentManager().popBackStack();
                        btnAdd.setEnabled(true);
                    }

                    @Override
                    public void Add_User(ConTact conTact, boolean Up_or_Add) {
                        if(Up_or_Add)
                            AddContact(conTact);
                        else
                            UpdateContact(conTact);
                    }
                }, null);
                backToFrag(fragment_add);
                btnAdd.setEnabled(false);
            }
        });
        new Load_Data().execute();
    }

    public void backToFrag(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.addToBackStack("Main");
        transaction.commit();
    }

    private void search(String text){
        ArrayList<ConTact> list_search = new ArrayList<>();
        for(ConTact i : list_Contact){
            if(i.getName().toLowerCase().contains(text.toLowerCase()))
                list_search.add(i);
        }
        if(list_search.isEmpty()) {
            if (text.length() > 0)
                Toast.makeText(this, "No Contact Founds", Toast.LENGTH_SHORT).show();
        } else {
            adapter.forTimKiem(list_search);
        }
    }

    public void AddContact(ConTact conTact){
        new Add_Task().execute(conTact);
    }

    public void UpdateContact(ConTact conTact){
        new Update_Task().execute(conTact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Contact Name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                if(newText.length() == 0)
                    adapter.forTimKiem(list_Contact);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    class Add_Task extends AsyncTask<ConTact, Void, Void>{

        @Override
        protected Void doInBackground(ConTact... conTacts) {
            contactDAO.insertAll(conTacts[0]);
            list_Contact.add(conTacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            adapter.notifyDataSetChanged();
            getSupportFragmentManager().popBackStack();
            btnAdd.setEnabled(true);
        }
    }

    class Update_Task extends AsyncTask<ConTact, Void, Void>{

        @Override
        protected Void doInBackground(ConTact... conTacts) {
            contactDAO.updateContac(conTacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            new Load_Data().execute();
            getSupportFragmentManager().popBackStack();
            btnAdd.setEnabled(true);
        }
    }

    class Load_Data extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... tem) {
            try {
                Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
                field.setAccessible(true);
                field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
            } catch (Exception e) {
                e.printStackTrace();
            }
            list_Contact.clear();
            list_Contact.addAll(contactDAO.getAllContact());
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            adapter.notifyDataSetChanged();
        }
    }
}