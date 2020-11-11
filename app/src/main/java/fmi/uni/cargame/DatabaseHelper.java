package fmi.uni.cargame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "accountsManager";
    private static final String TABLE_ACCOUNTS = "accounts";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT," +
                KEY_PASSWORD + " TEXT" + ")";

        db.execSQL(CREATE_ACCOUNTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);

        onCreate(db);
    }

    void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, account.getUsername());
        values.put(KEY_PASSWORD, account.getPassword());

        db.insert(TABLE_ACCOUNTS, null, values);
        db.close();
    }

    Account getAccount(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ACCOUNTS, new String[]{KEY_ID, KEY_USERNAME, KEY_PASSWORD}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Account account = new Account(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getColumnName(2));
        return account;
    }

    public List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<>();

        String selectQuery = "SELECT *FROM " + TABLE_ACCOUNTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account();
                account.setId(Integer.parseInt(cursor.getString(0)));
                account.setUsername(cursor.getString(1));
                account.setPassword(cursor.getString(2));

                accountList.add(account);
            } while (cursor.moveToNext());
        }

        return accountList;
    }

    public int updateAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, account.getUsername());
        values.put(KEY_PASSWORD, account.getPassword());

        return db.update(TABLE_ACCOUNTS, values, KEY_ID + "=?",
                new String[]{String.valueOf(account.getId())});
    }

    public void deleteAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNTS, KEY_ID + "=?",
                new String[]{String.valueOf(account.getId())});
        db.close();
    }

    public int getContactCount() {
        String countQuery = "SELECT *FROM " + TABLE_ACCOUNTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }


}

