package com.natalieperna.cupful.data;

import android.content.Context;

import com.natalieperna.cupful.models.Ingredient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IngredientReader {
    private static final String FILE_NAME = "data/ingredients.json";
    private final JSONObject ingredients;

    public IngredientReader(Context context) {
        try {
            InputStream stream = context.getAssets().open(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            ingredients = new JSONObject(builder.toString());
        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        } catch (JSONException e) {
            throw new RuntimeException("Could not parse JSON", e);
        }
    }

    public List<Ingredient> getAll() {
        List<Ingredient> list = new ArrayList<>();
        for (Iterator<String> it = ingredients.keys(); it.hasNext(); ) {
            String name = it.next();
            try {
                list.add(new Ingredient(name, ingredients.getDouble(name)));
            } catch (JSONException e) {
                throw new RuntimeException(String.format("Could not get ingredient %s", name), e);
            }
        }
        return list;
    }
}
