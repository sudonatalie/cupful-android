package com.natalieperna.cupful.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.natalieperna.cupful.core.Config;
import com.natalieperna.cupful.models.Ingredient;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteAssetHelper {

    public DatabaseHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
    }

    // TODO Move to separate class?
    public List<Ingredient> getIngredients() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"id", "name", "gPerCup"};
        String sqlTables = "ingredient";
        String sortOrder = "name";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, sortOrder);

        c.moveToFirst();

        List<Ingredient> list = new ArrayList<>();
        while (c.moveToNext()) {
            Ingredient ingredient = new Ingredient(
                    c.getString(1),
                    c.getInt(2)
            );
            list.add(ingredient);
        }
        c.close();
        db.close();
        return list;

    }
}
