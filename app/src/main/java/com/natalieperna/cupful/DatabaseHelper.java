package com.natalieperna.cupful;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

// Ref: http://www.concretepage.com/android/android-ship-sqlite-database-with-apk-copy-sqlite-database-from-assets-folder-to-data-example

class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DatabaseHelper";
    private final Context context;
    private String filePath;

    public DatabaseHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        this.context = context;
        String filesDir = context.getFilesDir().getAbsolutePath();
        filePath = filesDir + "/" + Config.DATABASE_NAME;
    }

    public void prepareDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if (false) { // TODO Make dbExist work here
            Log.d(TAG, "Database exists.");
            int currentDBVersion = getVersionId();
            if (Config.DATABASE_VERSION > currentDBVersion) {
                Log.d(TAG, "Database version is higher than old.");
                deleteDb();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(filePath);
            checkDB = file.exists();
            // TODO Actually check that expected tables exist
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
        return checkDB;
    }

    private void copyDataBase() throws IOException {
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

    private void deleteDb() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            Log.d(TAG, "Database deleted.");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private int getVersionId() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(filePath, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT version_id FROM dbVersion";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int v = cursor.getInt(0);
        cursor.close();
        db.close();
        return v;
    }

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
