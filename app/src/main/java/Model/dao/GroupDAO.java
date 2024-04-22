package Model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Model.Group;
import Model.data.TaskTogetherDBHelper;
import Model.data.TaskTogetherContract.GroupEntry;

public class GroupDAO {
    private SQLiteDatabase bd;

    public GroupDAO(Context context) {
        TaskTogetherDBHelper bdHelper = new TaskTogetherDBHelper(context);
        bd = bdHelper.getWritableDatabase();
    }

    public void insert(Group group) {
        ContentValues values = new ContentValues();
        values.put(GroupEntry.COLUMN_NAME, group.getName());
        values.put(GroupEntry.COLUMN_DESCRIPTION, group.getDescription());
        values.put(GroupEntry.COLUMN_TYPE, group.getType());

        bd.insert(GroupEntry.TABLE_NAME,
                null,
                values);
    }

    public void edit(Group group) {
        ContentValues values = new ContentValues();
        values.put(GroupEntry.COLUMN_NAME, group.getName());
        values.put(GroupEntry.COLUMN_DESCRIPTION, group.getDescription());
        values.put(GroupEntry.COLUMN_TYPE, group.getType());

        bd.update(GroupEntry.TABLE_NAME,
                values,
                GroupEntry._ID + " = ?",
                new String[]{String.valueOf(group.getIdGroup())});
    }

    public void delete (int id) {
        bd.delete(GroupEntry.TABLE_NAME,
                GroupEntry._ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public ArrayList<Group> searchAll() {
        ArrayList<Group> groups = new ArrayList<>();

        String[] columns = {
                GroupEntry._ID,
                GroupEntry.COLUMN_NAME,
                GroupEntry.COLUMN_DESCRIPTION,
                GroupEntry.COLUMN_TYPE,
        };

        Cursor cursor = bd.query(GroupEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                GroupEntry.COLUMN_NAME + " ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Group group = new Group();
                group.setIdGroup(cursor.getInt(0));
                group.setName(cursor.getString(1));
                group.setDescription(cursor.getString(2));
                group.setType(cursor.getString(3));

                groups.add(group);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return groups;
    }

    public ArrayList<Group> searchAllOffline() {
        ArrayList<Group> groups = new ArrayList<>();

        String[] columns = {
                GroupEntry._ID,
                GroupEntry.COLUMN_NAME,
                GroupEntry.COLUMN_DESCRIPTION,
                GroupEntry.COLUMN_TYPE,
        };

        Cursor cursor = bd.query(GroupEntry.TABLE_NAME,
                columns,
                GroupEntry.COLUMN_TYPE + " = ?",
                new String[]{"offline"},
                null,
                null,
                GroupEntry.COLUMN_NAME + " ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Group group = new Group();
                group.setIdGroup(cursor.getInt(0));
                group.setName(cursor.getString(1));
                group.setDescription(cursor.getString(2));
                group.setType(cursor.getString(3));

                groups.add(group);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return groups;
    }

    public Group searchById(int id) {
        Group group = null;

        String[] columns = {
                GroupEntry._ID,
                GroupEntry.COLUMN_NAME,
                GroupEntry.COLUMN_DESCRIPTION,
                GroupEntry.COLUMN_TYPE,
        };

        Cursor cursor = bd.query(GroupEntry.TABLE_NAME,
                columns,
                GroupEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);


        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            group = new Group();
            group.setIdGroup(cursor.getInt(0));
            group.setName(cursor.getString(1));
            group.setDescription(cursor.getString(2));
            group.setType(cursor.getString(3));
        }

        cursor.close();

        return group;
    }

    public Group searchLatest() {
        Group group = null;

        String[] columns = {
                GroupEntry._ID,
                GroupEntry.COLUMN_NAME,
                GroupEntry.COLUMN_DESCRIPTION,
                GroupEntry.COLUMN_TYPE,
        };

        Cursor cursor = bd.query(GroupEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                GroupEntry._ID + " DESC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            group = new Group();
            group.setIdGroup(cursor.getInt(0));
            group.setName(cursor.getString(1));
            group.setDescription(cursor.getString(2));
            group.setType(cursor.getString(3));
        }

        cursor.close();

        return group;
    }
}
