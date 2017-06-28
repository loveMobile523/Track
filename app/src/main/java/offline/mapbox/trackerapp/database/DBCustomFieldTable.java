package offline.mapbox.trackerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import offline.mapbox.trackerapp.model.DBCustomField;

public class DBCustomFieldTable extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "TrackerDBCustomField";

    // Accounts table name
    public static final String TABLE_CUSTOMFIELDS = "custom_fields";

    // Accounts Table Columns names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CUSTOMFIELD_ID = "custom_field_id";
    public static final String COLUMN_CUSTOMFIELD_SUB_CATE_ID = "sub_category_id";
    public static final String COLUMN_CUSTOMFIELD_FIELD = "field";
    public static final String COLUMN_CUSTOMFIELD_TYPE = "type";
    public static final String COLUMN_CUSTOMFIELD_UPDATED = "updated";


    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {
            COLUMN_ID,
            COLUMN_CUSTOMFIELD_ID,
            COLUMN_CUSTOMFIELD_SUB_CATE_ID,
            COLUMN_CUSTOMFIELD_FIELD,
            COLUMN_CUSTOMFIELD_TYPE,
            COLUMN_CUSTOMFIELD_UPDATED
    };


    // ---------------------------------------------------------------------------------------------

    public void close() {
        this.close();
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    // ---------------------------------------------------------------------------------------------
    public DBCustomFieldTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "create table "
                + TABLE_CUSTOMFIELDS + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_CUSTOMFIELD_ID
                + " text, " + COLUMN_CUSTOMFIELD_SUB_CATE_ID
                + " text, " + COLUMN_CUSTOMFIELD_FIELD
                + " text, " + COLUMN_CUSTOMFIELD_TYPE
                + " text, " + COLUMN_CUSTOMFIELD_UPDATED
                + " text);";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMFIELDS);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMFIELDS);

        // Create tables again
        onCreate(db);
    }

    // Adding new custom field
    public DBCustomField addCustomField(DBCustomField dbCustomField) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMFIELD_ID, dbCustomField.getCustomFieldId());
        values.put(COLUMN_CUSTOMFIELD_SUB_CATE_ID, dbCustomField.getSub_category_id());
        values.put(COLUMN_CUSTOMFIELD_FIELD, dbCustomField.getField());
        values.put(COLUMN_CUSTOMFIELD_TYPE, dbCustomField.getType());
        values.put(COLUMN_CUSTOMFIELD_UPDATED, dbCustomField.getUpdated());

        long insertId = db/*database*/.insert(TABLE_CUSTOMFIELDS, null,
                values);
        Cursor cursor = db/*database*/.query(TABLE_CUSTOMFIELDS,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBCustomField newCustomField = cursorToCustomField(cursor);
        cursor.close();

        db.close();
        return newCustomField;
    }

    // Getting single custom field
    public DBCustomField getCustomField(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CUSTOMFIELDS, allColumns, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBCustomField dbCustomField= new DBCustomField(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
        db.close();
        return dbCustomField;
    }

    // Getting single custom field
    public DBCustomField getCustomFieldByCustomFieldId(String customFieldId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CUSTOMFIELDS, allColumns, COLUMN_CUSTOMFIELD_ID + "=?",
                new String[] { customFieldId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBCustomField dbCustomField = new DBCustomField(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
        db.close();
        return dbCustomField;
    }

    // Getting single custom field
    public List<DBCustomField> getCustomFieldByCustomFieldSubCateId(String subCategoryId) {

        List<DBCustomField> accountList = new ArrayList<DBCustomField>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CUSTOMFIELDS, allColumns, COLUMN_CUSTOMFIELD_SUB_CATE_ID + "=?",
                new String[] { subCategoryId }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBCustomField dbCustomField = new DBCustomField();
                dbCustomField = cursorToCustomField(cursor);
                // Adding place to list
                accountList.add(dbCustomField);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting single custom field
    public DBCustomField getCustomFieldByCustomFieldName(String customFieldName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CUSTOMFIELDS, allColumns, COLUMN_CUSTOMFIELD_FIELD + "=?",
                new String[] { customFieldName }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBCustomField dbCustomField = new DBCustomField(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
        db.close();
        return dbCustomField;
    }

    // Getting All custom fields
    public List<DBCustomField> getAllCustomFields() {
        List<DBCustomField> accountList = new ArrayList<DBCustomField>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMFIELDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBCustomField dbCustomField = new DBCustomField();
                dbCustomField = cursorToCustomField(cursor);
                // Adding place to list
                accountList.add(dbCustomField);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting custom fields Count
    public int getCustomFieldsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CUSTOMFIELDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single custom field
    public int updateCustomField(DBCustomField dbCustomField) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, dbCustomField.getId());
        values.put(COLUMN_CUSTOMFIELD_ID, dbCustomField.getCustomFieldId());
        values.put(COLUMN_CUSTOMFIELD_SUB_CATE_ID, dbCustomField.getSub_category_id());
        values.put(COLUMN_CUSTOMFIELD_FIELD, dbCustomField.getField());
        values.put(COLUMN_CUSTOMFIELD_TYPE, dbCustomField.getType());
        values.put(COLUMN_CUSTOMFIELD_UPDATED, dbCustomField.getUpdated());

        // updating row
        return db.update(TABLE_CUSTOMFIELDS, values, COLUMN_CUSTOMFIELD_ID + " = ?",
                new String[] { /*String.valueOf(dbPlace.getId())*/dbCustomField.getCustomFieldId() });
    }

    // Deleting single custom field
    public void deleteCustomField(DBCustomField dbCustomField) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMFIELDS, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbCustomField.getId()) });
        db.close();
    }

    private DBCustomField cursorToCustomField(Cursor cursor) {
        DBCustomField dbCustomField = new DBCustomField();
        dbCustomField.setId(cursor.getLong(0));
        dbCustomField.setCustomFieldId(cursor.getString(1));
        dbCustomField.setSub_category_id(cursor.getString(2));
        dbCustomField.setField(cursor.getString(3));
        dbCustomField.setType(cursor.getString(4));
        dbCustomField.setUpdated(cursor.getString(5));

        return dbCustomField;
    }
}
