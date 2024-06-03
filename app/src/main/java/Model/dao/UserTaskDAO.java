package Model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import Model.UserTask;
import Model.data.TaskTogetherDBHelper;
import Model.data.TaskTogetherContract.UserTaskEntry;

public class UserTaskDAO {
    private SQLiteDatabase bd;

    public UserTaskDAO(Context context) {
        TaskTogetherDBHelper bdHelper = new TaskTogetherDBHelper(context);
        bd = bdHelper.getWritableDatabase();
    }

    public void insert(UserTask userTask) {
        ContentValues values = new ContentValues();
        values.put(UserTaskEntry.COLUMN_ID_TASK, userTask.getIdTask());
        values.put(UserTaskEntry.COLUMN_ID_USER, userTask.getIdUser());

        bd.insert(UserTaskEntry.TABLE_NAME,
                null,
                values);
    }

    public void delete (int id) {
        bd.delete(UserTaskEntry.TABLE_NAME,
                UserTaskEntry._ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public ArrayList<UserTask> searchAllByTask(int idTask) {
        ArrayList<UserTask> userTasks = new ArrayList<>();

        String[] columns = {
                UserTaskEntry._ID,
                UserTaskEntry.COLUMN_ID_TASK,
                UserTaskEntry.COLUMN_ID_USER,
        };

        Cursor cursor = bd.query(UserTaskEntry.TABLE_NAME,
                columns,
                UserTaskEntry.COLUMN_ID_TASK + " = ?",
                new String[]{String.valueOf(idTask)},
                null,
                null,
                UserTaskEntry.COLUMN_ID_TASK+ " ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                UserTask userTask = new UserTask();

                userTask.setIdUserTask(cursor.getInt(0));
                userTask.setIdTask(cursor.getInt(1));
                userTask.setIdUser(cursor.getInt(2));

                userTasks.add(userTask);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return userTasks;
    }

    public ArrayList<UserTask> searchAllByUser(int idUser) {
        ArrayList<UserTask> userTasks = new ArrayList<>();

        String[] columns = {
                UserTaskEntry._ID,
                UserTaskEntry.COLUMN_ID_TASK,
                UserTaskEntry.COLUMN_ID_USER,
        };

        Cursor cursor = bd.query(UserTaskEntry.TABLE_NAME,
                columns,
                UserTaskEntry.COLUMN_ID_TASK + " = ?",
                new String[]{String.valueOf(idUser)},
                null,
                null,
                UserTaskEntry.COLUMN_ID_TASK+ " ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                UserTask userTask = new UserTask();

                userTask.setIdUserTask(cursor.getInt(0));
                userTask.setIdTask(cursor.getInt(1));
                userTask.setIdUser(cursor.getInt(2));

                userTasks.add(userTask);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return userTasks;
    }

    public UserTask searchByTaskIdAndUserId(int idTask, int idUser) {
        UserTask userTask = null;

        String[] columns = {
                UserTaskEntry._ID,
                UserTaskEntry.COLUMN_ID_TASK,
                UserTaskEntry.COLUMN_ID_USER,
        };

        Cursor cursor = bd.query(UserTaskEntry.TABLE_NAME,
                columns,
                UserTaskEntry.COLUMN_ID_TASK + " = ? AND " + UserTaskEntry.COLUMN_ID_USER + " = ?",
                new String[]{String.valueOf(idTask), String.valueOf(idUser)},
                null,
                null,
                null);


        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            userTask = new UserTask();

            userTask.setIdUserTask(cursor.getInt(0));
            userTask.setIdTask(cursor.getInt(1));
            userTask.setIdUser(cursor.getInt(2));
        }

        cursor.close();

        return userTask;
    }

    public UserTask searchById(int id) {
        UserTask userTask = null;

        String[] columns = {
                UserTaskEntry._ID,
                UserTaskEntry.COLUMN_ID_TASK,
                UserTaskEntry.COLUMN_ID_USER,
        };

        Cursor cursor = bd.query(UserTaskEntry.TABLE_NAME,
                columns,
                UserTaskEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            userTask = new UserTask();

            userTask.setIdUserTask(cursor.getInt(0));
            userTask.setIdTask(cursor.getInt(1));
            userTask.setIdUser(cursor.getInt(2));
        }

        cursor.close();

        return userTask;
    }
}
