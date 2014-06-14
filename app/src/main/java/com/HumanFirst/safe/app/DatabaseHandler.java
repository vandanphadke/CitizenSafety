package com.HumanFirst.safe.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vandan on 06-06-2014.
 * Store selected contacts in database
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1 ;

    // Database Name
    private static final String DATABASE_NAME = "contactdb" ;

    // Table name
    private static final String TABLE_NAME = "contactnumbers" ;

    //Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "";
    private static final String KEY_PHONE = "";
    private static final String KEY_LABEL = "";



    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FINAL_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_LABEL + " TEXT" + ")";

        db.execSQL(CREATE_FINAL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        db.close();

    }

    //Adding new items.
    void addContacts(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, contact.getId());
        values.put(KEY_NAME , contact.getName());
        values.put(KEY_PHONE, contact.getPhone());
        values.put(KEY_LABEL, contact.getLabel());


        // Inserting Row
        db.insertOrThrow(TABLE_NAME, null, values);
        db.close();
    }

    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_NAME, KEY_PHONE , KEY_LABEL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2) , cursor.getString(3));
        return contact;

    }
    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contact.setLabel(cursor.getString(3));
                // Adding contact to list
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return contacts;

    }

    // Updating single item
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhone());
        values.put(KEY_LABEL, contact.getLabel());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(countQuery, null);
        int temp=cursor.getCount();
        cursor.close();
        db1.close();

        // return count
        return temp;
    }

}
