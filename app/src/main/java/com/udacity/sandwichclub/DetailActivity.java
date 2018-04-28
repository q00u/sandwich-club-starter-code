package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.placeholder_sandwich) //for shawarma
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        //main name
        TextView mName = (TextView) findViewById(R.id.name_tv);
        mName.setText(sandwich.getMainName());

        //description
        TextView mDescription = (TextView) findViewById(R.id.description_tv);
        mDescription.setText(sandwich.getDescription());

        //origin
        TextView mOrigin = (TextView) findViewById(R.id.origin_tv);
        mOrigin.setText(sandwich.getPlaceOfOrigin());

        //aka
        TextView mAlsoKnownAs = (TextView) findViewById(R.id.also_known_tv);
        List<String> alsoKnownAs = sandwich.getAlsoKnownAs();
        if (0<alsoKnownAs.size()) {//skip in case it's empty
            mAlsoKnownAs.setText(sandwich.getAlsoKnownAs().get(0));
            for (int i = 1; i < sandwich.getAlsoKnownAs().size(); ++i) {
                mAlsoKnownAs.append(", " + sandwich.getAlsoKnownAs().get(i));
            }
        }

        //ingredients list with fancy bullets
        //based on https://stackoverflow.com/questions/4992794/how-to-add-bulleted-list-to-android-application
        // and https://developer.android.com/reference/android/text/style/BulletSpan
        TextView mIngredients = (TextView) findViewById(R.id.ingredients_tv);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (String i : sandwich.getIngredients()) {
            SpannableString ss = new SpannableString(i+"\n");
            ss.setSpan(new BulletSpan(8),0,i.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ssb.append(ss);
        }
        mIngredients.setText(ssb);
    }
}
