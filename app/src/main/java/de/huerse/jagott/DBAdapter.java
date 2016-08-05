package de.huerse.jagott;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    public static final String KEY_ROWID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DATE = "date";
    public static final String KEY_VERSE = "verse";
    public static final String KEY_TEXT = "text";
    public static final String KEY_NOTES = "notes";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MyFavorite";
    private static final String DATABASE_TABLE = "favorite";
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE =
            "create table if not exists favorite (id integer primary key autoincrement, "
            + "name VARCHAR not null, date VARCHAR, verse VARCHAR, text VARCHAR, notes);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS favorite");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a record into the database---
    public long insertRecord(String name, String date, String verse, String text, String notes)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_VERSE, verse);
        initialValues.put(KEY_TEXT, text);
        initialValues.put(KEY_NOTES, notes);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---retrieves all the records---
    public Cursor getAllRecords()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_DATE,
                KEY_VERSE, KEY_TEXT, KEY_NOTES}, null, null, null, null,
                null);
    }

    //---retrieves a particular record---
    public Cursor getRecord(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,KEY_DATE,
                                KEY_VERSE, KEY_TEXT, KEY_NOTES},
                        KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---retrieves a particular record---
    public Cursor getRecord(String name) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_DATE,
                                KEY_VERSE, KEY_TEXT, KEY_NOTES},
                        KEY_NAME + "='" + name + "'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---retrieves a particular record---
    public Cursor getRecord(String date, boolean usedate) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_DATE,
                                KEY_VERSE, KEY_TEXT, KEY_NOTES},
                        KEY_DATE + "='" + date + "'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a record---
    public boolean updateRecord(long rowId, String name, String title, String duedate, String course, String notes)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_DATE, title);
        args.put(KEY_VERSE, duedate);
        args.put(KEY_TEXT, course);
        args.put(KEY_NOTES, notes);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---deletes a particular record---
    public boolean deleteRecord(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteRecord(String name) {

        return db.delete(DATABASE_TABLE,KEY_NAME + "='"+ name +"'",null) > 0;
    }

    //---updates a record---
    public boolean dropTable()
    {
        try {
            db.execSQL("DROP TABLE IF EXISTS favorite");
            db.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
