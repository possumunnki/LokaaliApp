package com.example.possumunnki.locaaliapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is activity.
 * In this activity user can see the proper detail of the product.
 *
 * @author Akio Ide
 * @version 1.0
 * @since 2018-23-04
 */
public class ProductInformationActivity extends AppCompatActivity {
    /**title of the product post*/
    private TextView title;
    /**description of the product post*/
    private TextView description;
    /**date when the product post has been posted*/
    private TextView postedDate;
    /**amount of the product*/
    private TextView maxAmount;
    /**price of the product*/
    private TextView price;
    /**date format*/
    private SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);
        //format of the time
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
