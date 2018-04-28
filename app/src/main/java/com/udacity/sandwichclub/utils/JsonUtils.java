package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        try {
            //JSON root
            JSONObject sandwichJson = new JSONObject(json);
            //name object
            JSONObject name = sandwichJson.getJSONObject("name");
            //name sub-objects
            String mainName = name.getString("mainName");
            JSONArray alsoKnownAsJson = name.getJSONArray("alsoKnownAs");
            List<String> alsoKnownAs = new ArrayList<>();
            for (int i = 0; i<alsoKnownAsJson.length(); ++i) {
                alsoKnownAs.add(alsoKnownAsJson.optString(i));
            }
            //origin
            String placeOfOrigin = sandwichJson.getString("placeOfOrigin");
            //description
            String description = sandwichJson.getString("description");
            //image URI
            String image = sandwichJson.getString("image");
            //list of ingredients
            JSONArray ingredientsJson = sandwichJson.getJSONArray("ingredients");
            List<String> ingredients = new ArrayList<>();
            for (int i = 0; i<ingredientsJson.length(); ++i) {
                ingredients.add(ingredientsJson.optString(i));
            }
            //if no errors by this point, build sandwich and return it
            return new Sandwich(mainName,alsoKnownAs,placeOfOrigin,description,image,ingredients);
        } catch (JSONException err) {
            //something went wrong, bail out!
            Log.e("JsonUtils", "Error in JsonUtils ", err);
            return null;
        }
    }
}
