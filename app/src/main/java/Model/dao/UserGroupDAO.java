package Model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Model.UserGroup;
import Model.data.TaskTogetherDBHelper;
import Model.data.TaskTogetherContract.UserGroupEntry;

public class UserGroupDAO {
    private SQLiteDatabase bd;

    public UserGroupDAO(Context context) {
        TaskTogetherDBHelper bdHelper = new TaskTogetherDBHelper(context);
        bd = bdHelper.getWritableDatabase();
    }

    public void insert(UserGroup userGroup) {
        ContentValues values = new ContentValues();
        values.put(UserGroupEntry.COLUMN_ID_GROUP, userGroup.getIdGroup());
        values.put(UserGroupEntry.COLUMN_ID_USER, userGroup.getIdUser());
        values.put(UserGroupEntry.COLUMN_ID_WHO_INVITED, userGroup.getIdWhoInvited());
        values.put(UserGroupEntry.COLUMN_ACCESS_LEVEL, userGroup.getAccessLevel());
        values.put(UserGroupEntry.COLUMN_STATUS_PARTICIPATION, userGroup.getStatusParticipation());

        bd.insert(UserGroupEntry.TABLE_NAME,
                null,
                values);
    }

    public void edit(UserGroup userGroup) {
        ContentValues values = new ContentValues();
        values.put(UserGroupEntry.COLUMN_ID_GROUP, userGroup.getIdGroup());
        values.put(UserGroupEntry.COLUMN_ID_USER, userGroup.getIdUser());
        values.put(UserGroupEntry.COLUMN_ID_WHO_INVITED, userGroup.getIdWhoInvited());
        values.put(UserGroupEntry.COLUMN_ACCESS_LEVEL, userGroup.getAccessLevel());
        values.put(UserGroupEntry.COLUMN_STATUS_PARTICIPATION, userGroup.getStatusParticipation());

        bd.update(UserGroupEntry.TABLE_NAME,
                values,
                UserGroupEntry._ID + " = ?",
                new String[]{String.valueOf(userGroup.getIdUserGroup())});
    }

    public void delete (int id) {
        bd.delete(UserGroupEntry.TABLE_NAME,
                UserGroupEntry._ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public ArrayList<UserGroup> searchAll() {
        ArrayList<UserGroup> userGroups = new ArrayList<>();

        String[] columns = {
                UserGroupEntry._ID,
                UserGroupEntry.COLUMN_ID_USER,
                UserGroupEntry.COLUMN_ID_GROUP,
                UserGroupEntry.COLUMN_ID_WHO_INVITED,
                UserGroupEntry.COLUMN_ACCESS_LEVEL,
                UserGroupEntry.COLUMN_STATUS_PARTICIPATION,
        };

        Cursor cursor = bd.query(UserGroupEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                UserGroupEntry.COLUMN_STATUS_PARTICIPATION + " ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                UserGroup userGroup = new UserGroup();
                userGroup.setIdUserGroup(cursor.getInt(0));
                userGroup.setIdUser(cursor.getInt(1));
                userGroup.setIdGroup(cursor.getInt(2));
                userGroup.setAccessLevel(cursor.getString(3));
                userGroup.setStatusParticipation(cursor.getString(4));

                userGroups.add(userGroup);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return userGroups;
    }

    public ArrayList<UserGroup> searchAllInvitations(int idUser) {
        ArrayList<UserGroup> userGroups = new ArrayList<>();

        String[] columns = {
                UserGroupEntry._ID,
                UserGroupEntry.COLUMN_ID_USER,
                UserGroupEntry.COLUMN_ID_GROUP,
                UserGroupEntry.COLUMN_ID_WHO_INVITED,
                UserGroupEntry.COLUMN_ACCESS_LEVEL,
                UserGroupEntry.COLUMN_STATUS_PARTICIPATION,
        };

        Cursor cursor = bd.query(UserGroupEntry.TABLE_NAME,
                columns,
                UserGroupEntry.COLUMN_ID_USER + " = ? AND " +
                        UserGroupEntry.COLUMN_STATUS_PARTICIPATION + " = ?",
                new String[]{String.valueOf(idUser), "Pendente"},
                null,
                null,
                UserGroupEntry.COLUMN_ID_USER + " ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                UserGroup userGroup = new UserGroup();
                userGroup.setIdUserGroup(cursor.getInt(0));
                userGroup.setIdUser(cursor.getInt(1));
                userGroup.setIdGroup(cursor.getInt(2));
                userGroup.setIdWhoInvited(cursor.getInt(3));
                userGroup.setAccessLevel(cursor.getString(4));
                userGroup.setStatusParticipation(cursor.getString(5));

                userGroups.add(userGroup);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return userGroups;
    }

    public ArrayList<UserGroup> searchAllWhereUserIsMember(int idUser) {
        ArrayList<UserGroup> userGroups = new ArrayList<>();

        String[] columns = {
                UserGroupEntry._ID,
                UserGroupEntry.COLUMN_ID_USER,
                UserGroupEntry.COLUMN_ID_GROUP,
                UserGroupEntry.COLUMN_ID_WHO_INVITED,
                UserGroupEntry.COLUMN_ACCESS_LEVEL,
                UserGroupEntry.COLUMN_STATUS_PARTICIPATION,
        };

        Cursor cursor = bd.query(UserGroupEntry.TABLE_NAME,
                columns,
                UserGroupEntry.COLUMN_ID_USER + " = ? AND " +
                        UserGroupEntry.COLUMN_STATUS_PARTICIPATION + " = ?",
                new String[]{String.valueOf(idUser), "Membro"},
                null,
                null,
                UserGroupEntry.COLUMN_ID_GROUP + " ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                UserGroup userGroup = new UserGroup();
                userGroup.setIdUserGroup(cursor.getInt(0));
                userGroup.setIdUser(cursor.getInt(1));
                userGroup.setIdGroup(cursor.getInt(2));
                userGroup.setIdWhoInvited(cursor.getInt(3));
                userGroup.setAccessLevel(cursor.getString(4));
                userGroup.setStatusParticipation(cursor.getString(5));

                userGroups.add(userGroup);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return userGroups;
    }

    public UserGroup searchById(int id) {
        UserGroup userGroup = null;

        String[] columns = {
                UserGroupEntry._ID,
                UserGroupEntry.COLUMN_ID_USER,
                UserGroupEntry.COLUMN_ID_GROUP,
                UserGroupEntry.COLUMN_ID_WHO_INVITED,
                UserGroupEntry.COLUMN_ACCESS_LEVEL,
                UserGroupEntry.COLUMN_STATUS_PARTICIPATION,
        };

        Cursor cursor = bd.query(UserGroupEntry.TABLE_NAME,
                columns,
                UserGroupEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);


        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            userGroup = new UserGroup();
            userGroup.setIdUserGroup(cursor.getInt(0));
            userGroup.setIdUser(cursor.getInt(1));
            userGroup.setIdGroup(cursor.getInt(2));
            userGroup.setIdWhoInvited(cursor.getInt(3));
            userGroup.setAccessLevel(cursor.getString(4));
            userGroup.setStatusParticipation(cursor.getString(5));
        }

        cursor.close();

        return userGroup;
    }
}
