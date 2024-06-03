package Model.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import Model.data.TaskTogetherContract.*;
public class TaskTogetherDBHelper extends SQLiteOpenHelper {
    private static final String BD_NAME = "tasktogether.db";
    private static final int BD_VERSION = 1;

    public TaskTogetherDBHelper(Context context) {
        super(context, BD_NAME, null, BD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        String SQL_CREATE_USER = "CREATE TABLE " + UserEntry.TABLE_NAME + "("
                + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, "
                + UserEntry.COLUMN_PHONE + " TEXT UNIQUE NOT NULL, "
                + UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL"
                + ")";
        bd.execSQL(SQL_CREATE_USER);

        String SQL_CREATE_GROUP = "CREATE TABLE " + GroupEntry.TABLE_NAME + "("
                + GroupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GroupEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + GroupEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL,"
                + GroupEntry.COLUMN_TYPE + " TEXT NOT NULL"
                + ")";
        bd.execSQL(SQL_CREATE_GROUP);

        String SQL_CREATE_USER_GROUP = "CREATE TABLE " + UserGroupEntry.TABLE_NAME + "("
                + UserGroupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserGroupEntry.COLUMN_ID_USER + " INTEGER NOT NULL, "
                + UserGroupEntry.COLUMN_ID_GROUP + " INTEGER NOT NULL,"
                + UserGroupEntry.COLUMN_ID_WHO_INVITED + " INTEGER,"
                + UserGroupEntry.COLUMN_ACCESS_LEVEL + " TEXT NOT NULL,"
                + UserGroupEntry.COLUMN_STATUS_PARTICIPATION + " TEXT NOT NULL,"
                + "FOREIGN KEY(" + UserGroupEntry.COLUMN_ID_USER + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + "),"
                + "FOREIGN KEY(" + UserGroupEntry.COLUMN_ID_GROUP + ") REFERENCES " + GroupEntry.TABLE_NAME + "(" + GroupEntry._ID + ")"
                + ")";
        bd.execSQL(SQL_CREATE_USER_GROUP);

        String SQL_CREATE_TASK = "CREATE TABLE " + TaskEntry.TABLE_NAME + "("
                + TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskEntry.COLUMN_ID_GROUP + " INTEGER NOT NULL, "
                + TaskEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + TaskEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL,"
                + TaskEntry.COLUMN_STATUS + " TEXT NOT NULL,"
                + TaskEntry.COLUMN_LIMIT_DATE + " TEXT NOT NULL,"
                + "FOREIGN KEY(" + TaskEntry.COLUMN_ID_GROUP + ") REFERENCES " + GroupEntry.TABLE_NAME + "(" + GroupEntry._ID + ")"
                + ")";
        bd.execSQL(SQL_CREATE_TASK);

        String SQL_CREATE_USER_TASK = "CREATE TABLE " + UserTaskEntry.TABLE_NAME + "("
                + UserTaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserTaskEntry.COLUMN_ID_TASK + " INTEGER NOT NULL, "
                + UserTaskEntry.COLUMN_ID_USER + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + UserTaskEntry.COLUMN_ID_TASK + ") REFERENCES " + TaskEntry.TABLE_NAME + "(" + TaskEntry._ID + "),"
                + "FOREIGN KEY(" + UserTaskEntry.COLUMN_ID_USER + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")"
                + ")";
        bd.execSQL(SQL_CREATE_USER_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
        String SQL_UPDATE_USER = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
        bd.execSQL(SQL_UPDATE_USER);

        String SQL_UPDATE_GROUP = "DROP TABLE IF EXISTS " + GroupEntry.TABLE_NAME;
        bd.execSQL(SQL_UPDATE_GROUP);

        String SQL_UPDATE_USER_GROUP = "DROP TABLE IF EXISTS " + UserGroupEntry.TABLE_NAME;
        bd.execSQL(SQL_UPDATE_USER_GROUP);

        String SQL_UPDATE_TASK = "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;
        bd.execSQL(SQL_UPDATE_TASK);

        String SQL_UPDATE_USER_TASK = "DROP TABLE IF EXISTS " + UserTaskEntry.TABLE_NAME;
        bd.execSQL(SQL_UPDATE_USER_TASK);

        onCreate(bd);
    }
}

