package com.natalieperna.cupful.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.natalieperna.cupful.core.Config;
import com.natalieperna.cupful.models.Ingredient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DatabaseHelper";
    private Context context;
    private String filePath;

    public DatabaseHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String filesDir = context.getFilesDir().getAbsolutePath();
        filePath = filesDir + "/" + Config.DATABASE_NAME;
        prepareDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String filesDir = context.getFilesDir().getAbsolutePath();
        filePath = filesDir + "/" + Config.DATABASE_NAME;
        prepareDatabase();
    }

    private void prepareDatabase() {
        boolean dbExist = checkDatabase();
        if (dbExist) {
            int currentDBVersion = getVersionId();
            if (Config.DATABASE_VERSION > currentDBVersion) {
                deleteDatabase();
                try {
                    copyDatabase();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                this.close();
            }
        }
    }

    private boolean checkDatabase() {
        try {
            boolean checkDB;
            File file = new File(filePath);
            checkDB = file.exists();
            // TODO Actually check that expected tables exist?
            return checkDB;
        } catch (SQLiteException e) {
            // TODO Is it actually possible for a SQLiteException to be thrown here
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    private void copyDatabase() throws IOException {
        OutputStream os = new FileOutputStream(filePath);
        InputStream is = context.getAssets().open(Config.DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.flush();
        os.close();
    }

    private void deleteDatabase() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete(); // TODO Check for failure
        }
    }

    private int getVersionId() {
        // TODO Catch errors!
        SQLiteDatabase db = SQLiteDatabase.openDatabase(filePath, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT version_id FROM dbVersion";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int v = cursor.getInt(0);
        cursor.close();
        db.close();
        return v;
    }

    // TODO Move to separate class?
    public List<Ingredient> getIngredients() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(filePath, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT id, name, gPerCup FROM ingredient ORDER BY name";
        Cursor cursor = db.rawQuery(query, null);
        List<Ingredient> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Ingredient ingredient = new Ingredient(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2)
            );
            list.add(ingredient);
        }
        cursor.close();
        db.close();
        return list;
    }
}
