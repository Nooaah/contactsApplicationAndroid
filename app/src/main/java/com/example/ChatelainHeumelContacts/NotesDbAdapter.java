package com.example.ChatelainHeumelContacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbAdapter {

    /**
     * Mes attributs de la base de données
     */
    public static final String KEY_LIGNEID = "_id";
    public static final String KEY_NOM = "nom";
    public static final String KEY_PRENOM ="prenom";
    public static final String KEY_TELEPHONE = "telephone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESSE = "addresse";
    public static final String KEY_FAVORIS = "favoris";

    /**
     * La base de données...
     */
    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Création d'une base de données adaptée à une liste de contacts
     */
    private static final String DATABASE_CREATE =
            "create table contacts(_id integer primary key autoincrement,"
                    + "nom text not null, prenom text not null, telephone text not null, "
                    + "email text, addresse text not null, favoris text not null);";


    private static final String DATABASE_NAME = "toutlescontacts";
    private static final String DATABASE_TABLE = "contacts";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    /**
     * Methode de manipulation de la base de données
     *
     */
    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * Création d'un contact
     * @return -1 if echec
     */
    public long createContact(String nom, String prenom, String telephone, String email,
                           String addresse, boolean favoris ) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOM, nom);
        initialValues.put(KEY_PRENOM, prenom);
        initialValues.put(KEY_TELEPHONE, telephone);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_ADDRESSE, addresse);
        initialValues.put(KEY_FAVORIS, favoris);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Supression de contact....
     *
     */
    public boolean deleteContact(long ligneId) {

        return mDb.delete(DATABASE_TABLE, KEY_LIGNEID + "=" + ligneId, null) > 0;
    }

    public boolean deleteAll(){

        return mDb.delete(DATABASE_TABLE, KEY_LIGNEID + ">" + 0, null) > 0;
    }

    /**
     * Recuperer la liste de mes contacts
     * ORDER BY KEY_NOM
     */
    public Cursor fetchAllContacts() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_LIGNEID, KEY_NOM, KEY_PRENOM,
                        KEY_TELEPHONE, KEY_EMAIL, KEY_ADDRESSE, KEY_FAVORIS},
                null, null, null, null, KEY_NOM);
    }

    /**
     * Recuperer la liste de mes favoris
     * @return
     */
    public Cursor fetchAllContactsFav() {

        String selection = KEY_FAVORIS + "=?";
        String[] selectionArg = new String[] { "1" };

        return mDb.query(DATABASE_TABLE, new String[] {KEY_LIGNEID, KEY_NOM, KEY_PRENOM,
                        KEY_TELEPHONE, KEY_EMAIL, KEY_ADDRESSE, KEY_FAVORIS},
                selection,selectionArg, null, null, KEY_NOM);
    }



    /**
     * Recuperer un seul contact
     *
     */
    public Cursor fetchContacts(long ligneId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[]{KEY_LIGNEID, KEY_NOM,
                                KEY_PRENOM, KEY_TELEPHONE, KEY_EMAIL, KEY_ADDRESSE,
                                KEY_FAVORIS}, KEY_LIGNEID + "=" + ligneId,
                        null, null, null, KEY_NOM, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Modifier un contact sur la base de données
     *
     */
    public boolean updateContact(long ligneId, String nom, String prenom, String telephone, String email,
                              String addresse, boolean favoris) {
        ContentValues args = new ContentValues();
        args.put(KEY_NOM, nom);
        args.put(KEY_PRENOM, prenom);
        args.put(KEY_TELEPHONE, telephone);
        args.put(KEY_EMAIL, email);
        args.put(KEY_ADDRESSE, addresse);
        args.put(KEY_FAVORIS, favoris);

        return mDb.update(DATABASE_TABLE, args, KEY_LIGNEID + "=" + ligneId, null) > 0;
    }
}
