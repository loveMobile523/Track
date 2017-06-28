package offline.mapbox.trackerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import offline.mapbox.trackerapp.model.DBIcon;

public class DBIconTable extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "TrackerDBIcon";

    // Accounts table name
    public static final String TABLE_ICONS= "icons";

    // Accounts Table Columns names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ICON_ID = "icon_id";
    public static final String COLUMN_ICON_NAME = "name";
    public static final String COLUMN_ICON_PATH = "path";
    public static final String COLUMN_ICON_DESCRIPTION = "description";
    public static final String COLUMN_ICON_UPDATED = "updated";


    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {
            COLUMN_ID,
            COLUMN_ICON_ID,
            COLUMN_ICON_NAME,
            COLUMN_ICON_PATH,
            COLUMN_ICON_DESCRIPTION,
            COLUMN_ICON_UPDATED
    };


    // ---------------------------------------------------------------------------------------------

    public void close() {
        this.close();
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    // ---------------------------------------------------------------------------------------------
    public DBIconTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "create table "
                + TABLE_ICONS + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_ICON_ID
                + " text, " + COLUMN_ICON_NAME
                + " text, " + COLUMN_ICON_PATH
                + " text, " + COLUMN_ICON_DESCRIPTION
                + " text, " + COLUMN_ICON_UPDATED
                + " text);";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ICONS);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ICONS);

        // Create tables again
        onCreate(db);
    }

    // Adding new icon
    public DBIcon addIcon(DBIcon dbIcon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ICON_ID, dbIcon.getIconId());
        values.put(COLUMN_ICON_NAME, dbIcon.getName());
        values.put(COLUMN_ICON_PATH, dbIcon.getPath());
        values.put(COLUMN_ICON_DESCRIPTION, dbIcon.getDescription());
        values.put(COLUMN_ICON_UPDATED, dbIcon.getUpdated());

        long insertId = db/*database*/.insert(TABLE_ICONS, null,
                values);
        Cursor cursor = db/*database*/.query(TABLE_ICONS,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBIcon newIcon = cursorToIcon(cursor);
        cursor.close();

        db.close();
        return newIcon;
    }

    // Getting single icon
    public DBIcon getIcon(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ICONS, allColumns, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBIcon dbIcon = new DBIcon(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
        db.close();
        return dbIcon;
    }

    // Getting single icon
    public DBIcon getIconByIconId(String iconId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ICONS, allColumns, COLUMN_ICON_ID + "=?",
                new String[] { iconId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBIcon dbIcon = new DBIcon(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
        db.close();
        return dbIcon;
    }

    // Getting single icon
    public DBIcon getIconByIconName(String iconName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ICONS, allColumns, COLUMN_ICON_NAME + "=?",
                new String[] { iconName }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBIcon dbIcon = new DBIcon(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
        db.close();
        return dbIcon;
    }

    // Getting All icons
    public List<DBIcon> getAllIcons() {
        List<DBIcon> accountList = new ArrayList<DBIcon>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ICONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBIcon dbIcon = new DBIcon();
                dbIcon = cursorToIcon(cursor);
                // Adding place to list
                accountList.add(dbIcon);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting icons Count
    public int getIconsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ICONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single icon
    public int updateIcon(DBIcon dbIcon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, dbIcon.getId());
        values.put(COLUMN_ICON_ID, dbIcon.getIconId());
        values.put(COLUMN_ICON_NAME, dbIcon.getName());
        values.put(COLUMN_ICON_PATH, dbIcon.getPath());
        values.put(COLUMN_ICON_DESCRIPTION, dbIcon.getDescription());
        values.put(COLUMN_ICON_UPDATED, dbIcon.getUpdated());

        // updating row
        return db.update(TABLE_ICONS, values, COLUMN_ICON_ID + " = ?",
                new String[] { /*String.valueOf(dbPlace.getId())*/dbIcon.getIconId() });
    }

    // Deleting single icon
    public void deleteIcon(DBIcon dbIcon) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ICONS, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbIcon.getId()) });
        db.close();
    }

    private DBIcon cursorToIcon(Cursor cursor) {
        DBIcon dbIcon = new DBIcon();
        dbIcon.setId(cursor.getLong(0));
        dbIcon.setIconId(cursor.getString(1));
        dbIcon.setName(cursor.getString(2));
        dbIcon.setPath(cursor.getString(3));
        dbIcon.setDescription(cursor.getString(4));
        dbIcon.setUpdated(cursor.getString(5));

        return dbIcon;
    }
}
