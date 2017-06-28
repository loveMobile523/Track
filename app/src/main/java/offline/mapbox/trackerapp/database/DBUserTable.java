package offline.mapbox.trackerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import offline.mapbox.trackerapp.model.DBUser;

public class DBUserTable extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "TrackerDBUser";

    // Accounts table name
    public static final String TABLE_USERS = "users";

    // Accounts Table Columns names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_TYPE = "user_type";
    public static final String COLUMN_USER_UPDATED = "updated";

    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {
            COLUMN_ID,
            COLUMN_USER_ID,
            COLUMN_USER_NAME,
            COLUMN_USER_TYPE,
            COLUMN_USER_UPDATED
    };


    // ---------------------------------------------------------------------------------------------

    public void close() {
        this.close();
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    // ---------------------------------------------------------------------------------------------
    public DBUserTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "create table "
                + TABLE_USERS + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_USER_ID
                + " text, " + COLUMN_USER_NAME
                + " text, " + COLUMN_USER_TYPE
                + " text, " + COLUMN_USER_UPDATED
                + " text);";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    // Adding new user
    public DBUser addUser(DBUser dbUser) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, dbUser.getUserId());
        values.put(COLUMN_USER_NAME, dbUser.getUsername());
        values.put(COLUMN_USER_TYPE, dbUser.getUser_type());
        values.put(COLUMN_USER_UPDATED, dbUser.getUpdated());

        long insertId = db/*database*/.insert(TABLE_USERS, null,
                values);
        Cursor cursor = db/*database*/.query(TABLE_USERS,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBUser newUser = cursorToUser(cursor);
        cursor.close();

        db.close();
        return newUser;
    }

    // Getting single user
    public DBUser getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, allColumns, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBUser dbUser = new DBUser(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        db.close();
        return dbUser;
    }

    // Getting single user
    public DBUser getUserByUserId(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, allColumns, COLUMN_USER_ID + "=?",
                new String[] { userId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBUser dbUser = new DBUser(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        db.close();
        return dbUser;
    }

    // Getting single user
    public DBUser getUserByUserName(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, allColumns, COLUMN_USER_NAME + "=?",
                new String[] { userName }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBUser dbUser = new DBUser(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        db.close();
        return dbUser;
    }

    // Getting All users
    public List<DBUser> getAllUsers() {
        List<DBUser> accountList = new ArrayList<DBUser>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBUser dbUser = new DBUser();
                dbUser = cursorToUser(cursor);
                // Adding place to list
                accountList.add(dbUser);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting users Count
    public int getCategoriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single user
    public int updateUser(DBUser dbUser) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, dbUser.getId());
        values.put(COLUMN_USER_ID, dbUser.getUserId());
        values.put(COLUMN_USER_NAME, dbUser.getUsername());
        values.put(COLUMN_USER_TYPE, dbUser.getUser_type());
        values.put(COLUMN_USER_UPDATED, dbUser.getUpdated());

        // updating row
        return db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?",
                new String[] { /*String.valueOf(dbPlace.getId())*/dbUser.getUserId() });
    }

    // Deleting single user
    public void deleteUser(DBUser dbUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbUser.getId()) });
        db.close();
    }

    private DBUser cursorToUser(Cursor cursor) {
        DBUser dbUser = new DBUser();
        dbUser.setId(cursor.getLong(0));
        dbUser.setUserId(cursor.getString(1));
        dbUser.setUsername(cursor.getString(2));
        dbUser.setUser_type(cursor.getString(3));
        dbUser.setUpdated(cursor.getString(4));

        return dbUser;
    }
}
