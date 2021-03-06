package mouse.com.cloudnote_01.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import mouse.com.cloudnote_01.beans.Note;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final static String CREATE_NOTES = "create table notes (_id integer primary key autoincrement, title text, content text, time text, note_id integer, bmob_id text,need_update_to_bmob integer)";
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
     */
    public void insert(Note note) {
        //插入一条数据
        db.insert("notes", null, getContentValues(note));
    }


    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put("note_id", note.getNote_id());
        values.put("content", note.getNote_content());
        values.put("time", note.getNote_time());
        values.put("bmob_id", note.getBmob_id());
        values.put("need_update_to_bmob", note.getNeed_update_to_bmob());
        return values;
    }


    public void insert(List<Note> newNotes) {
        for (int i = 0; i < newNotes.size(); i++) {
            insert(newNotes.get(i));
        }
    }

    /**
     * 根据id删除数据库notes表中某一行
     *
     * @param note_id note_id
     */
    public void delete(long note_id) {
        //删除一条数据
        int i = db.delete("notes", "note_id like ?", new String[]{Long.toString(note_id)});
        Toast.makeText(mContext, "delete " + i + " from db.", Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据id更新数据库notes表中的某一行
     *
     * @param title   title
     * @param content content
     * @param time    time
     * @param note_id      note_id
     * @param bmob_id bmob_id
     */
    public int update(String title, String content, String time, long note_id, String bmob_id, int need_update_to_bmob) {
        //修改一条数据
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("time", time);
        values.put("bmob_id", bmob_id);
        values.put("need_update_to_bmob", need_update_to_bmob);
        int i = db.update("notes", values, "note_id = ?", new String[]{String.valueOf(note_id)});
        Toast.makeText(mContext, "update " + i + " from db.", Toast.LENGTH_SHORT).show();
        return i;
    }

    /**
     * 查询数据库notes表中所有note行
     *
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
                long note_id = cursor.getLong(cursor.getColumnIndex("note_id"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String bmob_id = cursor.getString(cursor.getColumnIndex("bmob_id"));
                int need_update_to_bmob = cursor.getInt(cursor.getColumnIndex("need_update_to_bmob"));
                Note note = new Note(title, content, time, note_id, bmob_id, need_update_to_bmob);
                data.add(note);
                Log.d("NotesActivity", note_id + ": title:" + title + ", content:" + content + ", time:" + time + ", bmob_id:" + bmob_id + ", need_update_to_bmob:" + need_update_to_bmob);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    /**
     * 查询数据库notes表中所有未同步到Bmob后端的note行
     *
     * @return 将所有查询到的note封装在LinkedList中
     */
    public LinkedList<Note> query(String bmob_id) {
        //查询全部数据，返回一个cursor，从cursor取出数据
        Cursor cursor = db.rawQuery("select * from notes where bmob_id like ?", new String[]{bmob_id});
        LinkedList<Note> data = new LinkedList<>();
        if (cursor.moveToNext()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                long note_id = cursor.getLong(cursor.getColumnIndex("note_id"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int need_update_to_bmob = cursor.getInt(cursor.getColumnIndex("need_update_to_bmob"));
                Note note = new Note(title, content, time, note_id, bmob_id, need_update_to_bmob);
                data.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    /**
     * 查询数据库notes表中所有未同步到Bmob后端的note行
     *
     * @return 将所有查询到的note封装在LinkedList中
     */
    public LinkedList<Note> query(int need_update_to_bmob) {
        //查询全部数据，返回一个cursor，从cursor取出数据
        Cursor cursor = db.rawQuery("select * from notes where need_update_to_bmob like ?", new String[]{String.valueOf(need_update_to_bmob)});
        LinkedList<Note> data = new LinkedList<>();
        if (cursor.moveToNext()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                long note_id = cursor.getLong(cursor.getColumnIndex("note_id"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String bmob_id = cursor.getString(cursor.getColumnIndex("bmob_id"));
                Note note = new Note(title, content, time, note_id, bmob_id, need_update_to_bmob);
                data.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    /**
     * 根据id从数据库中查询相应的note行
     *
     * @param note_id note的note_id
     * @return 查询到的Note，查询失败返回null
     */
    public Note query(long note_id) {
        Note note = null;
        //利用SQL语句生查操作(超好用)
        Cursor cursor = db.rawQuery("select * from notes where note_id like ?", new String[]{String.valueOf(note_id)});
        if (cursor.moveToNext()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String bmob_id = cursor.getString(cursor.getColumnIndex("bmob_id"));
                int need_update_to_bmob = cursor.getInt(cursor.getColumnIndex("need_update_to_bmob"));
                note = new Note(title, content, time, note_id, bmob_id, need_update_to_bmob);
            } while (cursor.moveToNext());

        } else {
            Log.d("NotesActivity", "query: no found!");
        }
        cursor.close();
        return note;
    }
}