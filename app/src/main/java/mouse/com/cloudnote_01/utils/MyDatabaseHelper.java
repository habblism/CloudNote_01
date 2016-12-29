package mouse.com.cloudnote_01.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;

import mouse.com.cloudnote_01.beans.Note;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final static String CREATE_NOTES = "create table notes (_id integer primary key autoincrement, title text, content text, time text, id integer)";
    private Context mContext;
    private SQLiteDatabase db;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTES);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 插入数据库notes表中一行数据
     * @param title title
     * @param content content
     * @param time time
     * @param id id
     */
    public void insert(String title, String content, String time, long id) {
        //插入一条数据
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("id", id);
        values.put("time",time);
        db.insert("notes", null, values);
    }

    /**
     * 根据id删除数据库notes表中某一行
      * @param id id
     */
    public void delete(long id) {
        //删除一条数据
        int i = db.delete("notes", "id like ?", new String[]{Long.toString(id)});
        Toast.makeText(mContext, "delete " + i + " from db.", Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据id更新数据库notes表中的某一行
     * @param title title
     * @param content content
     * @param time time
     * @param id id
     */
    public void update(String title, String content, String time, long id) {
        //修改一条数据
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("time", time);
        int i = db.update("notes", values, "id = ?", new String[]{String.valueOf(id)});
        Toast.makeText(mContext, "update " + i + " from db.", Toast.LENGTH_SHORT).show();
    }

    /**
     * 查询数据库notes表中所有note行
     * @return 将所有查询到的note封装在LinkedList中
     */
    public LinkedList<Note> query() {
        //查询全部数据，返回一个cursor，从cursor取出数据
        Cursor cursor = db.query("notes", null, null, null, null, null, null);
        LinkedList<Note> data = new LinkedList<>();
        if (cursor.moveToNext()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                Note note = new Note(title, content, time, id);
                data.add(note);
                Log.d("MainActivity", id + ": " + title + ", " + content + ", " + time);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    /**
     * 根据id从数据库中查询相应的note行
     * @param id note的id
     * @return  查询到的Note，查询失败返回null
     */
    public Note query(long id) {
        Note note = null;
        //利用SQL语句生查操作(超好用)
        Cursor cursor = db.rawQuery("select * from notes where id like ?", new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                note = new Note(title, content, time, id);
                Log.d("query", id + ": " + title + " " + content + " " + time);
            } while (cursor.moveToNext());

        } else {
            Log.d("MainActivity", "query: no found!");
        }
        cursor.close();
        return note;
    }
}