package com.example.rhemainventory;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CashierActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private FloatingActionButton fab;
    private String fabAction = "Product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cashier);
        navigationView = findViewById(R.id.navigation);
        fab = findViewById(R.id.fab);
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new ProductsFragment()).commit();


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId==R.id.action_product){
                    fabAction = "Product";
                    fab.setVisibility(View.VISIBLE);
                    navigationView.getMenu().getItem(0).setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProductsFragment()).commit();
                }
                if(itemId==R.id.action_stock){
                    fabAction = "Stock";
                    fab.setVisibility(View.GONE);
                    navigationView.getMenu().getItem(1).setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new StockFragment()).commit();
                }
                if (itemId == R.id.action_logout) {
                    Utils.logout(CashierActivity.this);
                    finish();
                    startActivity(new Intent(CashierActivity.this,Login.class));
                }
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabAction.equals("Product")) {
                    startActivity(new Intent(CashierActivity.this, AddProduct.class));
                }else{
                    startActivity(new Intent(CashierActivity.this,ManageStock.class));
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Utils.logout(CashierActivity.this);
            finish();
            startActivity(new Intent(CashierActivity.this,Login.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}