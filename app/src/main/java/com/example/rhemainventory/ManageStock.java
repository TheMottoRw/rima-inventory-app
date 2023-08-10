package com.example.rhemainventory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ManageStock extends AppCompatActivity {
    public MaterialToolbar toolbar;
    private ProgressDialog pgdialog;
    private EditText quantity, pu,expiration,product;
    private String productId,productName,stockAction;
    private ToggleButton toggleButton;
    private Button btnSave;
    private Calendar calendarPicker,newCalendar;
    private DatePickerDialog expirationCalendarPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_stock);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        productName = intent.getStringExtra("productName");
        Toast.makeText(ManageStock.this,productName,Toast.LENGTH_SHORT).show();
        product = findViewById(R.id.product);
        quantity = findViewById(R.id.quantity);
        pu = findViewById(R.id.pu);
        expiration = findViewById(R.id.expiration_date);
        toggleButton = findViewById(R.id.toggleButton);
        product.setText(productName);
        btnSave = findViewById(R.id.btnSave);
        pgdialog = new ProgressDialog(this);
        pgdialog.setMessage("Loading data...");
        pgdialog.setCancelable(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        expiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setExpiratonDateTimeField();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductStock();
            }
        });
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButton.isChecked())
                    expiration.setVisibility(View.GONE);
                else expiration.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void setExpiratonDateTimeField() {
        calendarPicker = Calendar.getInstance();
        newCalendar = calendarPicker;
        expirationCalendarPicker = new DatePickerDialog(ManageStock.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarPicker.set(year, monthOfYear, dayOfMonth, 0, 0);
                expiration.setText(Utils.dateFormat(calendarPicker.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        expirationCalendarPicker.show();
    }


    private void addProductStock() {
        final String url = Utils.host + "/stock";
        JSONObject body = new JSONObject();
        pgdialog.show();
        String toggleAction = toggleButton.getText().toString();
        Toast.makeText(ManageStock.this,toggleAction,Toast.LENGTH_SHORT).show();
        if(toggleAction.equals("Stock in"))
            stockAction = "add";
        else stockAction = "sold";
        Log.d("URL", url);
        try{
            body.put("product",productId);
            body.put("user",2);
            body.put("quantity",quantity.getText().toString().trim());
            body.put("pu",pu.getText().toString());
            body.put("expiration_date",expiration.getText().toString().trim());
            body.put("action",stockAction);
        }catch (JSONException ex){
            Log.d("JSONErr",ex.getMessage());
        }
//        tvLoggingIn.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
// prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        pgdialog.dismiss();
                        Log.d("Logresp", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(ManageStock.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            quantity.setText("");
                            pu.setText("");
                            expiration.setText("");
                        } catch (JSONException ex) {
                            Log.d("Json error", ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pgdialog.dismiss();
                        Toast.makeText(ManageStock.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.e("jsonerr", "JSON Error " + (error != null ? error.getMessage() : ""));
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                return headers;
            }
            @Override
            public byte[] getBody() {
                return body.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        ;

// add it to the RequestQueue
        queue.add(getRequest);
    }

}