package com.example.possumunnki.locaaliapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductInformationActivity extends AppCompatActivity {
    private TextView title;
    private TextView description;
    private TextView postedDate;
    private TextView maxAmount;
    private TextView price;
    private SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);
        simpleDateFormat = new SimpleDateFormat("MMM/dd/yyyy HH:mm");

        title = (TextView) findViewById(R.id.product_title);
        description = (TextView) findViewById(R.id.product_description);
        postedDate = (TextView) findViewById(R.id.product_postedDate);
        maxAmount = (TextView) findViewById(R.id.product_amount);
        price = (TextView) findViewById(R.id.product_price);


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String priceStr = "Price: " + extras.getDouble("price") + "€";
            String amountStr = "Amount: " + extras.getInt("amount");
            Long postTime = extras.getLong("postedDate");
            Date date = new Date(postTime);
            String postedDateStr = "Posted: " + simpleDateFormat.format(date);

            title.setText(extras.getString("productTitle"));
            description.setText(extras.getString("productDescription"));
            postedDate.setText(postedDateStr);
            maxAmount.setText(amountStr);
            price.setText(priceStr);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
