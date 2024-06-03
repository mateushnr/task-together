package Model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Model.Group;
import Model.Task;
import Model.data.TaskTogetherContract;
import Model.data.TaskTogetherDBHelper;
import Model.data.TaskTogetherContract.TaskEntry;

public class TaskDAO {
    private SQLiteDatabase bd;

    public TaskDAO(Context context) {
        TaskTogetherDBHelper bdHelper = new TaskTogetherDBHelper(context);
        bd = bdHelper.getWritableDatabase();
    }

    public Task insert(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_ID_GROUP, task.getIdGroup());
        values.put(TaskEntry.COLUMN_NAME, task.getName());
        values.put(TaskEntry.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_STATUS, task.getStatus());
        values.put(TaskEntry.COLUMN_LIMIT_DATE, task.getLimitDate());

        long newRowId = bd.insert(TaskEntry.TABLE_NAME,
                null,
                values);

        return searchById((int) newRowId);
    }

    public void edit(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_ID_GROUP, task.getIdGroup());
        values.put(TaskEntry.COLUMN_NAME, task.getName());
        values.put(TaskEntry.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_STATUS, task.getStatus());
        values.put(TaskEntry.COLUMN_LIMIT_DATE, task.getLimitDate());

        bd.update(TaskEntry.TABLE_NAME,
                values,
                TaskEntry._ID + " = ?",
                new String[]{String.valueOf(task.getIdTask())});
    }

    public void delete (int id) {
        bd.delete(TaskEntry.TABLE_NAME,
                TaskEntry._ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public ArrayList<Task> searchAllByGroup(int idGroup) {
        ArrayList<Task> tasks = new ArrayList<>();

        String[] columns = {
                TaskEntry._ID,
                TaskEntry.COLUMN_ID_GROUP,
                TaskEntry.COLUMN_NAME,
                TaskEntry.COLUMN_DESCRIPTION,
                TaskEntry.COLUMN_STATUS,
                TaskEntry.COLUMN_LIMIT_DATE,
        };

        Cursor cursor = bd.query(TaskEntry.TABLE_NAME,
                columns,
                TaskEntry.COLUMN_ID_GROUP + " = ?",
                new String[]{String.valueOf(idGroup)},
                null,
                null,
                TaskEntry.COLUMN_NAME + " ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Task task = new Task();
                task.setIdTask(cursor.getInt(0));
                task.setIdGroup(cursor.getInt(1));
                task.setName(cursor.getString(2));
                task.setDescription(cursor.getString(3));
                task.setStatus(cursor.getString(4));
                task.setLimitDate(cursor.getString(5));

                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return tasks;
    }

    public ArrayList<Task> searchByGroupAndStatus(int idGroup, String status) {
        ArrayList<Task> tasks = new ArrayList<>();

        String[] columns = {
                TaskEntry._ID,
                TaskEntry.COLUMN_ID_GROUP,
                TaskEntry.COLUMN_NAME,
                TaskEntry.COLUMN_DESCRIPTION,
                TaskEntry.COLUMN_STATUS,
                TaskEntry.COLUMN_LIMIT_DATE,
        };

        Cursor cursor = bd.query(TaskEntry.TABLE_NAME,
                columns,
                TaskEntry.COLUMN_ID_GROUP + " = ? AND " + TaskEntry.COLUMN_STATUS + " = ?",
                new String[]{String.valueOf(idGroup), status},
                null,
                null,
                TaskEntry.COLUMN_NAME + " ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Task task = new Task();
                task.setIdTask(cursor.getInt(0));
                task.setIdGroup(cursor.getInt(1));
                task.setName(cursor.getString(2));
                task.setDescription(cursor.getString(3));
                task.setStatus(cursor.getString(4));
                task.setLimitDate(cursor.getString(5));

                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return tasks;
    }

    public Task searchById(int id) {
        Task task = null;

        String[] columns = {
                TaskEntry._ID,
                TaskEntry.COLUMN_ID_GROUP,
                TaskEntry.COLUMN_NAME,
                TaskEntry.COLUMN_DESCRIPTION,
                TaskEntry.COLUMN_STATUS,
                TaskEntry.COLUMN_LIMIT_DATE,
        };

        Cursor cursor = bd.query(TaskEntry.TABLE_NAME,
                columns,
                TaskEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);


        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            task = new Task();

            task.setIdTask(cursor.getInt(0));
            task.setIdGroup(cursor.getInt(1));
            task.setName(cursor.getString(2));
            task.setDescription(cursor.getString(3));
            task.setStatus(cursor.getString(4));
            task.setLimitDate(cursor.getString(5));
        }

        cursor.close();

        return task;
    }
}
