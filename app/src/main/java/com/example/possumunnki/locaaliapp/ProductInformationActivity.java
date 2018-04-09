package com.example.possumunnki.locaaliapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProductInformationActivity extends AppCompatActivity {
    private TextView title;
    private TextView description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);
        title = (TextView) findViewById(R.id.product_title);
        description = (TextView) findViewById(R.id.product_description);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            title.setText(extras.getString("productTitle"));
            description.setText(extras.getString("productDescription"));
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
