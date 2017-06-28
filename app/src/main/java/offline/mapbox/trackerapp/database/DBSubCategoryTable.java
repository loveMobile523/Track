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
import offline.mapbox.trackerapp.model.DBSubCategory;

public class DBSubCategoryTable extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "TrackerDBSubCategory";

    // Accounts table name
    public static final String TABLE_SUBCATEGORIES = "sub_categories";

    // Accounts Table Columns names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SUBCATEGORY_ID = "sub_category_id";
    public static final String COLUMN_SUBCATEGORY_CATE_ID = "category_id";
    public static final String COLUMN_SUBCATEGORY_CATE_NAME = "category_name";
    public static final String COLUMN_SUBCATEGORY_SUB_CATE = "sub_category";
    public static final String COLUMN_SUBCATEGORY_UPDATED = "updated";


    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {
            COLUMN_ID,
            COLUMN_SUBCATEGORY_ID,
            COLUMN_SUBCATEGORY_CATE_ID,
            COLUMN_SUBCATEGORY_CATE_NAME,
            COLUMN_SUBCATEGORY_SUB_CATE,
            COLUMN_SUBCATEGORY_UPDATED
    };


    // ---------------------------------------------------------------------------------------------

    public void close() {
        this.close();
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    // ---------------------------------------------------------------------------------------------
    public DBSubCategoryTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "create table "
                + TABLE_SUBCATEGORIES + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_SUBCATEGORY_ID
                + " text, " + COLUMN_SUBCATEGORY_CATE_ID
                + " text, " + COLUMN_SUBCATEGORY_CATE_NAME
                + " text not null, " + COLUMN_SUBCATEGORY_SUB_CATE
                + " text not null, " + COLUMN_SUBCATEGORY_UPDATED
                + " text);";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBCATEGORIES);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBCATEGORIES);

        // Create tables again
        onCreate(db);
    }

    // Adding new sub category
    public DBSubCategory addSubCategory(DBSubCategory dbSubCategory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBCATEGORY_ID, dbSubCategory.getSubCategoryID());
        values.put(COLUMN_SUBCATEGORY_CATE_ID, dbSubCategory.getCategory_id());
        values.put(COLUMN_SUBCATEGORY_CATE_NAME, dbSubCategory.getCategory_name());
        values.put(COLUMN_SUBCATEGORY_SUB_CATE, dbSubCategory.getSub_category());
        values.put(COLUMN_SUBCATEGORY_UPDATED, dbSubCategory.getUpdated());

        long insertId = db/*database*/.insert(TABLE_SUBCATEGORIES, null,
                values);
        Cursor cursor = db/*database*/.query(TABLE_SUBCATEGORIES,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBSubCategory newSubCategory = cursorToSubCategory(cursor);
        cursor.close();

        db.close();
        return newSubCategory;
    }

    // Getting single sub category
    public DBSubCategory getSubCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SUBCATEGORIES, allColumns, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBSubCategory dbSubCategory= new DBSubCategory(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
        db.close();
        return dbSubCategory;
    }

    // Getting single sub category
    public DBSubCategory getSubCategoryBySubCategoryId(String subCategoryId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SUBCATEGORIES, allColumns, COLUMN_SUBCATEGORY_ID + "=?",
                new String[] { subCategoryId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBSubCategory dbSubCategory = new DBSubCategory(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
        db.close();
        return dbSubCategory;
    }

    // Getting single sub category
    public DBSubCategory getSubCategoryBySubCategoryName(String subCategoryName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SUBCATEGORIES, allColumns, COLUMN_SUBCATEGORY_SUB_CATE + "=?",
                new String[] { subCategoryName }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBSubCategory dbSubCategory = new DBSubCategory(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
        db.close();
        return dbSubCategory;
    }

    // Getting All sub Categories
    public List<DBSubCategory> getAllSubCategories() {
        List<DBSubCategory> accountList = new ArrayList<DBSubCategory>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUBCATEGORIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBSubCategory dbSubCategory = new DBSubCategory();
                dbSubCategory = cursorToSubCategory(cursor);
                // Adding place to list
                accountList.add(dbSubCategory);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting sub categories Count
    public int getSubCategoriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SUBCATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single sub category
    public int updateCategory(DBSubCategory dbSubCategory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, dbSubCategory.getId());
        values.put(COLUMN_SUBCATEGORY_ID, dbSubCategory.getSubCategoryID());
        values.put(COLUMN_SUBCATEGORY_CATE_ID, dbSubCategory.getCategory_id());
        values.put(COLUMN_SUBCATEGORY_CATE_NAME, dbSubCategory.getCategory_name());
        values.put(COLUMN_SUBCATEGORY_SUB_CATE, dbSubCategory.getSub_category());
        values.put(COLUMN_SUBCATEGORY_UPDATED, dbSubCategory.getUpdated());

        // updating row
        return db.update(TABLE_SUBCATEGORIES, values, COLUMN_SUBCATEGORY_ID+ " = ?",
                new String[] { /*String.valueOf(dbPlace.getId())*/dbSubCategory.getSubCategoryID() });
    }

    // Deleting single sub category
    public void deleteSubCategory(DBCategory dbCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBCATEGORIES, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbCategory.getId()) });
        db.close();
    }

    private DBSubCategory cursorToSubCategory(Cursor cursor) {
        DBSubCategory dbSubCategory = new DBSubCategory();
        dbSubCategory.setId(cursor.getLong(0));
        dbSubCategory.setSubCategoryID(cursor.getString(1));
        dbSubCategory.setCategory_id(cursor.getString(2));
        dbSubCategory.setCategory_name(cursor.getString(3));
        dbSubCategory.setSub_category(cursor.getString(4));
        dbSubCategory.setUpdated(cursor.getString(5));

        return dbSubCategory;
    }
}
