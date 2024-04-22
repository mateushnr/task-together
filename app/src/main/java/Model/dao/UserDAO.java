package Model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Model.User;
import Model.data.TaskTogetherDBHelper;
import Model.data.TaskTogetherContract.UserEntry;

public class UserDAO {
    private SQLiteDatabase bd;

    public UserDAO(Context context) {
        TaskTogetherDBHelper bdHelper = new TaskTogetherDBHelper(context);
        bd = bdHelper.getWritableDatabase();
    }

    public void insert(User user) {
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME, user.getName());
        values.put(UserEntry.COLUMN_EMAIL, user.getEmail());
        values.put(UserEntry.COLUMN_PHONE, user.getPhone());
        values.put(UserEntry.COLUMN_PASSWORD, user.getPassword());

        bd.insert(UserEntry.TABLE_NAME,
                null,
                values);
    }

    public void edit(User user) {
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME, user.getName());
        values.put(UserEntry.COLUMN_EMAIL, user.getEmail());
        values.put(UserEntry.COLUMN_PHONE, user.getPhone());
        values.put(UserEntry.COLUMN_PASSWORD, user.getPassword());

        bd.update(UserEntry.TABLE_NAME,
                values,
                UserEntry._ID + " = ?",
                new String[]{String.valueOf(user.getIdUser())});
    }

    public void delete (int id) {
        bd.delete(UserEntry.TABLE_NAME,
                UserEntry._ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public ArrayList<User> searchAll() {
        ArrayList<User> users = new ArrayList<>();

        String[] columns = {
                UserEntry._ID,
                UserEntry.COLUMN_NAME,
                UserEntry.COLUMN_EMAIL,
                UserEntry.COLUMN_PHONE
        };

        Cursor cursor = bd.query(UserEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                UserEntry.COLUMN_NAME + " ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                User user = new User();
                user.setIdUser(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setPhone(cursor.getString(3));

                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return users;
    }

    public User searchById(int id) {
        User user = null;

        String[] columns = {
                UserEntry._ID,
                UserEntry.COLUMN_NAME,
                UserEntry.COLUMN_EMAIL,
                UserEntry.COLUMN_PHONE,
                UserEntry.COLUMN_PASSWORD
        };

        Cursor cursor = bd.query(UserEntry.TABLE_NAME,
                columns,
                UserEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);


        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User();
            user.setIdUser(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPhone(cursor.getString(3));
            user.setPassword(cursor.getString(4));
        }

        cursor.close();

        return user;
    }

    public User searchByPhone(String phone) {
        User user = null;

        String[] columns = {
                UserEntry._ID,
                UserEntry.COLUMN_NAME,
                UserEntry.COLUMN_EMAIL,
                UserEntry.COLUMN_PHONE,
                UserEntry.COLUMN_PASSWORD
        };

        Cursor cursor = bd.query(UserEntry.TABLE_NAME,
                columns,
                UserEntry.COLUMN_PHONE + " = ?",
                new String[]{String.valueOf(phone)},
                null,
                null,
                null);


        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User();
            user.setIdUser(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPhone(cursor.getString(3));
            user.setPassword(cursor.getString(4));
        }

        cursor.close();

        return user;
    }

    public User searchByEmail(String email) {
        User user = null;

        String[] columns = {
                UserEntry._ID,
                UserEntry.COLUMN_NAME,
                UserEntry.COLUMN_EMAIL,
                UserEntry.COLUMN_PHONE,
                UserEntry.COLUMN_PASSWORD
        };

        Cursor cursor = bd.query(UserEntry.TABLE_NAME,
                columns,
                UserEntry.COLUMN_EMAIL + " = ?",
                new String[]{String.valueOf(email)},
                null,
                null,
                null);


        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User();
            user.setIdUser(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPhone(cursor.getString(3));
            user.setPassword(cursor.getString(4));
        }

        cursor.close();

        return user;
    }
}
