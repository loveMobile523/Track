package offline.mapbox.trackerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import offline.mapbox.trackerapp.model.DBMarker;

public class DBMarkerTable extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "TrackerDBMarker";

    // Accounts table name
    public static final String TABLE_MARKERS = "markers";

    // Accounts Table Columns names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MARKER_ID = "marker_id";
    public static final String COLUMN_MARKER_NAME = "name";
    public static final String COLUMN_MARKER_ICON_NAME = "icon_name";
    public static final String COLUMN_MARKER_ICON_PATH = "icon_path";

    public static final String COLUMN_MARKER_ADDRESS = "address";
    public static final String COLUMN_MARKER_LOCATION_LATLNG = "location_latlng";
    public static final String COLUMN_MARKER_CATEGORY = "category";
    public static final String COLUMN_MARKER_SUB_CATEGORY = "sub_category";
    public static final String COLUMN_MARKER_DESCRIPTION = "description";

    public static final String COLUMN_MARKER_CUSTOM_FIELDS = "custom_fields";
    public static final String COLUMN_MARKER_DRAWING_TYPE = "drawing_type";
    public static final String COLUMN_MARKER_FILL_COLOR = "fill_color";
    public static final String COLUMN_MARKER_BORDER_COLOR = "border_color";
    public static final String COLUMN_MARKER_TRANSPARENCY = "transparency";

    public static final String COLUMN_MARKER_BORDER_WEIGHT = "border_weight";
    public static final String COLUMN_MARKER_BOUNDS = "bounds";
    public static final String COLUMN_MARKER_BUDGET = "budget";
    public static final String COLUMN_MARKER_EXPENCE = "expence";
    public static final String COLUMN_MARKER_DATE_DECIDE_BUDGET = "date_decide_budget";

    public static final String COLUMN_MARKER_IMAGE = "image";
    public static final String COLUMN_MARKER_STATUS = "status";
    public static final String COLUMN_MARKER_DAMAGE_ROUTE = "damage_route";
    public static final String COLUMN_MARKER_UPDATED = "updated";


    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {
            COLUMN_ID,
            COLUMN_MARKER_ID,
            COLUMN_MARKER_NAME,
            COLUMN_MARKER_ICON_NAME,
            COLUMN_MARKER_ICON_PATH,

            COLUMN_MARKER_ADDRESS,
            COLUMN_MARKER_LOCATION_LATLNG,
            COLUMN_MARKER_CATEGORY,
            COLUMN_MARKER_SUB_CATEGORY,
            COLUMN_MARKER_DESCRIPTION,

            COLUMN_MARKER_CUSTOM_FIELDS,
            COLUMN_MARKER_DRAWING_TYPE,
            COLUMN_MARKER_FILL_COLOR,
            COLUMN_MARKER_BORDER_COLOR,
            COLUMN_MARKER_TRANSPARENCY,

            COLUMN_MARKER_BORDER_WEIGHT,
            COLUMN_MARKER_BOUNDS,
            COLUMN_MARKER_BUDGET,
            COLUMN_MARKER_EXPENCE,
            COLUMN_MARKER_DATE_DECIDE_BUDGET,

            COLUMN_MARKER_IMAGE,
            COLUMN_MARKER_STATUS,
            COLUMN_MARKER_DAMAGE_ROUTE,
            COLUMN_MARKER_UPDATED
    };


    // ---------------------------------------------------------------------------------------------

    public void close() {
        this.close();
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    // ---------------------------------------------------------------------------------------------
    public DBMarkerTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "create table "
                + TABLE_MARKERS + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_MARKER_ID
                + " text, " + COLUMN_MARKER_NAME
                + " text, " + COLUMN_MARKER_ICON_NAME
                + " text, " + COLUMN_MARKER_ICON_PATH

                + " text, " + COLUMN_MARKER_ADDRESS
                + " text, " + COLUMN_MARKER_LOCATION_LATLNG
                + " text, " + COLUMN_MARKER_CATEGORY
                + " text, " + COLUMN_MARKER_SUB_CATEGORY
                + " text, " + COLUMN_MARKER_DESCRIPTION

                + " text, " + COLUMN_MARKER_CUSTOM_FIELDS
                + " text, " + COLUMN_MARKER_DRAWING_TYPE
                + " text, " + COLUMN_MARKER_FILL_COLOR
                + " text, " + COLUMN_MARKER_BORDER_COLOR
                + " text, " + COLUMN_MARKER_TRANSPARENCY

                + " text, " + COLUMN_MARKER_BORDER_WEIGHT
                + " text, " + COLUMN_MARKER_BOUNDS
                + " text, " + COLUMN_MARKER_BUDGET
                + " text, " + COLUMN_MARKER_EXPENCE
                + " text, " + COLUMN_MARKER_DATE_DECIDE_BUDGET

                + " text, " + COLUMN_MARKER_IMAGE
                + " text, " + COLUMN_MARKER_STATUS
                + " text, " + COLUMN_MARKER_DAMAGE_ROUTE
                + " text, " + COLUMN_MARKER_UPDATED
                + " text);";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKERS);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKERS);

        // Create tables again
        onCreate(db);
    }

    // Adding new marker
    public DBMarker addMarker(DBMarker dbMarker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MARKER_ID, dbMarker.getMarkerId());
        values.put(COLUMN_MARKER_NAME, dbMarker.getName());
        values.put(COLUMN_MARKER_ICON_NAME, dbMarker.getIcon_name());
        values.put(COLUMN_MARKER_ICON_PATH, dbMarker.getIcon_path());

        values.put(COLUMN_MARKER_ADDRESS, dbMarker.getAddress());
        values.put(COLUMN_MARKER_LOCATION_LATLNG, dbMarker.getLocation_latlng());
        values.put(COLUMN_MARKER_CATEGORY, dbMarker.getCategory());
        values.put(COLUMN_MARKER_SUB_CATEGORY, dbMarker.getSub_category());
        values.put(COLUMN_MARKER_DESCRIPTION, dbMarker.getDescription());

        values.put(COLUMN_MARKER_CUSTOM_FIELDS, dbMarker.getCustom_fields());
        values.put(COLUMN_MARKER_DRAWING_TYPE, dbMarker.getDrawing_type());
        values.put(COLUMN_MARKER_FILL_COLOR, dbMarker.getFill_color());
        values.put(COLUMN_MARKER_BORDER_COLOR, dbMarker.getBorder_color());
        values.put(COLUMN_MARKER_TRANSPARENCY, dbMarker.getTransparency());

        values.put(COLUMN_MARKER_BORDER_WEIGHT, dbMarker.getBorder_weight());
        values.put(COLUMN_MARKER_BOUNDS, dbMarker.getBounds());
        values.put(COLUMN_MARKER_BUDGET, dbMarker.getBudget());
        values.put(COLUMN_MARKER_EXPENCE, dbMarker.getExpence());
        values.put(COLUMN_MARKER_DATE_DECIDE_BUDGET, dbMarker.getDate_decide_budget());

        values.put(COLUMN_MARKER_IMAGE, dbMarker.getImage());
        values.put(COLUMN_MARKER_STATUS, dbMarker.getStatus());
        values.put(COLUMN_MARKER_DAMAGE_ROUTE, dbMarker.getDamage_route());
        values.put(COLUMN_MARKER_UPDATED, dbMarker.getUpdated());

        long insertId = db/*database*/.insert(TABLE_MARKERS, null,
                values);
        Cursor cursor = db/*database*/.query(TABLE_MARKERS,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBMarker newMarker = cursorToMarker(cursor);
        cursor.close();

        db.close();
        return newMarker;
    }

    // Getting single marker
    public DBMarker getMarker(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MARKERS, allColumns, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBMarker dbMarker = new DBMarker(cursor.getLong(0), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(23));
        db.close();
        return dbMarker;
    }

    // Getting single marker
    public DBMarker getMarkerByMarkerId(String markerId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MARKERS, allColumns, COLUMN_MARKER_ID + "=?",
                new String[] { markerId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBMarker dbMarker = new DBMarker(cursor.getLong(0), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(23));
        db.close();
        return dbMarker;
    }

    // Getting single marker
    public DBMarker geMarkerByMarkerName(String markerName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MARKERS, allColumns, COLUMN_MARKER_NAME + "=?",
                new String[] { markerName }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBMarker dbMarker = new DBMarker(cursor.getLong(0), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(23));
        db.close();
        return dbMarker;
    }

    // Getting All markers
    public List<DBMarker> getAllMarkers() {
        List<DBMarker> accountList = new ArrayList<DBMarker>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MARKERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBMarker dbMarker = new DBMarker();
                dbMarker = cursorToMarker(cursor);
                // Adding place to list
                accountList.add(dbMarker);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting markers Count
    public int getMarkersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MARKERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single marker
    public int updateMarker(DBMarker dbMarker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, dbMarker.getId());
        values.put(COLUMN_MARKER_ID, dbMarker.getMarkerId());
        values.put(COLUMN_MARKER_NAME, dbMarker.getName());
        values.put(COLUMN_MARKER_ICON_NAME, dbMarker.getIcon_name());
        values.put(COLUMN_MARKER_ICON_PATH, dbMarker.getIcon_path());

        values.put(COLUMN_MARKER_ADDRESS, dbMarker.getAddress());
        values.put(COLUMN_MARKER_LOCATION_LATLNG, dbMarker.getLocation_latlng());
        values.put(COLUMN_MARKER_CATEGORY, dbMarker.getCategory());
        values.put(COLUMN_MARKER_SUB_CATEGORY, dbMarker.getSub_category());
        values.put(COLUMN_MARKER_DESCRIPTION, dbMarker.getDescription());

        values.put(COLUMN_MARKER_CUSTOM_FIELDS, dbMarker.getCustom_fields());
        values.put(COLUMN_MARKER_DRAWING_TYPE, dbMarker.getDrawing_type());
        values.put(COLUMN_MARKER_FILL_COLOR, dbMarker.getFill_color());
        values.put(COLUMN_MARKER_BORDER_COLOR, dbMarker.getBorder_color());
        values.put(COLUMN_MARKER_TRANSPARENCY, dbMarker.getTransparency());

        values.put(COLUMN_MARKER_BORDER_WEIGHT, dbMarker.getBorder_weight());
        values.put(COLUMN_MARKER_BOUNDS, dbMarker.getBounds());
        values.put(COLUMN_MARKER_BUDGET, dbMarker.getBudget());
        values.put(COLUMN_MARKER_EXPENCE, dbMarker.getExpence());
        values.put(COLUMN_MARKER_DATE_DECIDE_BUDGET, dbMarker.getDate_decide_budget());

        values.put(COLUMN_MARKER_IMAGE, dbMarker.getImage());
        values.put(COLUMN_MARKER_STATUS, dbMarker.getStatus());
        values.put(COLUMN_MARKER_DAMAGE_ROUTE, dbMarker.getDamage_route());
        values.put(COLUMN_MARKER_UPDATED, dbMarker.getUpdated());


        // updating row
        return db.update(TABLE_MARKERS, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbMarker.getId()) });
    }

    // Deleting single marker
    public void deleteMarker(DBMarker dbMarker) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MARKERS, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbMarker.getId()) });
        db.close();
    }

    private DBMarker cursorToMarker(Cursor cursor) {
        DBMarker dbMarker = new DBMarker();
        dbMarker.setId(cursor.getLong(0));
        dbMarker.setMarkerId(cursor.getString(1));
        dbMarker.setName(cursor.getString(2));
        dbMarker.setIcon_name(cursor.getString(3));
        dbMarker.setIcon_path(cursor.getString(4));

        dbMarker.setAddress(cursor.getString(5));
        dbMarker.setLocation_latlng(cursor.getString(6));
        dbMarker.setCategory(cursor.getString(7));
        dbMarker.setSub_category(cursor.getString(8));
        dbMarker.setDescription(cursor.getString(9));

        dbMarker.setCustom_fields(cursor.getString(10));
        dbMarker.setDrawing_type(cursor.getString(11));
        dbMarker.setFill_color(cursor.getString(12));
        dbMarker.setBorder_color(cursor.getString(13));
        dbMarker.setTransparency(cursor.getString(14));

        dbMarker.setBorder_weight(cursor.getString(15));
        dbMarker.setBounds(cursor.getString(16));
        dbMarker.setBudget(cursor.getString(17));
        dbMarker.setExpence(cursor.getString(18));
        dbMarker.setDate_decide_budget(cursor.getString(19));

        dbMarker.setImage(cursor.getString(20));
        dbMarker.setStatus(cursor.getString(21));
        dbMarker.setDamage_route(cursor.getString(22));
        dbMarker.setUpdated(cursor.getString(23));

        return dbMarker;
    }

    // todo firebase table
    // User id,Latitude,Longitude,time & date,

}
