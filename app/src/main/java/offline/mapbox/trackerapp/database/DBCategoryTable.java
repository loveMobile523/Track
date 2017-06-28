package offline.mapbox.trackerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import offline.mapbox.trackerapp.model.DBCategory;

public class DBCategoryTable extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "TrackerDBCategory";

    // Accounts table name
    public static final String TABLE_CATEGORIES = "categories";

    // Accounts Table Columns names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category";
    public static final String COLUMN_CATEGORY_UPDATED = "updated";


    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {
            COLUMN_ID,
            COLUMN_CATEGORY_ID,
            COLUMN_CATEGORY_NAME,
            COLUMN_CATEGORY_UPDATED
    };


    // ---------------------------------------------------------------------------------------------

    public void close() {
        this.close();
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    // ---------------------------------------------------------------------------------------------
    public DBCategoryTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "create table "
                + TABLE_CATEGORIES + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_CATEGORY_ID
                + " text, " + COLUMN_CATEGORY_NAME
                + " text not null, " + COLUMN_CATEGORY_UPDATED
                + " text);";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

        // Create tables again
        onCreate(db);
    }

    // Adding new category
    public DBCategory addCategory(DBCategory dbCategory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_ID, dbCategory.getCategoryId());
        values.put(COLUMN_CATEGORY_NAME, dbCategory.getCategory());
        values.put(COLUMN_CATEGORY_UPDATED, dbCategory.getUpdated());

        long insertId = db/*database*/.insert(TABLE_CATEGORIES, null,
                values);
        Cursor cursor = db/*database*/.query(TABLE_CATEGORIES,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBCategory newCategory = cursorToCategory(cursor);
        cursor.close();

        db.close();
        return newCategory;
    }

    // Getting single category
    public DBCategory getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIES, allColumns, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBCategory dbCategory= new DBCategory(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        db.close();
        return dbCategory;
    }

    // Getting single category
    public DBCategory getCategoryByCategoryId(String categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIES, allColumns, COLUMN_CATEGORY_ID + "=?",
                new String[] { categoryId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBCategory dbCategory = new DBCategory(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        db.close();
        return dbCategory;
    }

    // Getting single category
    public DBCategory getCategoryByCategoryName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIES, allColumns, COLUMN_CATEGORY_NAME + "=?",
                new String[] { categoryName }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBCategory dbCategory = new DBCategory(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        db.close();
        return dbCategory;
    }

    // Getting All Categories
    public List<DBCategory> getAllCategories() {
        List<DBCategory> accountList = new ArrayList<DBCategory>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBCategory dbCategory = new DBCategory();
                dbCategory = cursorToCategory(cursor);
                // Adding place to list
                accountList.add(dbCategory);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting categories Count
    public int getCategoriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single category
    public int updateCategory(DBCategory dbCategory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, dbCategory.getId());
        values.put(COLUMN_CATEGORY_ID, dbCategory.getCategoryId());
        values.put(COLUMN_CATEGORY_NAME, dbCategory.getCategory());
        values.put(COLUMN_CATEGORY_UPDATED, dbCategory.getUpdated());

        // updating row
        return db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORY_ID + " = ?",
                new String[] { /*String.valueOf(dbPlace.getId())*/dbCategory.getCategoryId() });
    }

    // Deleting single category
    public void deleteCategory(DBCategory dbCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbCategory.getId()) });
        db.close();
    }

    private DBCategory cursorToCategory(Cursor cursor) {
        DBCategory dbCategory = new DBCategory();
        dbCategory.setId(cursor.getLong(0));
        dbCategory.setCategoryId(cursor.getString(1));
        dbCategory.setCategory(cursor.getString(2));
        dbCategory.setUpdated(cursor.getString(3));

        return dbCategory;
    }
}
