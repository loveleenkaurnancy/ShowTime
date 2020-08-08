package com.kitkat.showtime.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.kitkat.showtime.model.ShowModel

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $SHOW_ID INTEGER, $POSTER_PATH TEXT, $BACKDROP_PATH TEXT, $TYPE TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addTask(showModel: ShowModel.Result, type : String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(SHOW_ID, showModel.id)
        values.put(POSTER_PATH, showModel.poster_path)
        values.put(BACKDROP_PATH, showModel.backdrop_path)
        values.put(TYPE, type)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedId", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

//    fun getTask(_id: Int): Tasks {
//        val tasks = Tasks()
//        val db = writableDatabase
//        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
//        val cursor = db.rawQuery(selectQuery, null)
//
//        cursor?.moveToFirst()
//        tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
//        tasks.name = cursor.getString(cursor.getColumnIndex(NAME))
//        tasks.desc = cursor.getString(cursor.getColumnIndex(DESC))
//        tasks.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
//        cursor.close()
//        return tasks
//    }

    fun show(type : String): ArrayList<ShowModel.Result> {
        val showList = ArrayList<ShowModel.Result>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $TYPE = '$type'"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val showModel = ShowModel.Result()
                    showModel.id = cursor.getInt(cursor.getColumnIndex(SHOW_ID))
                    showModel.poster_path = cursor.getString(cursor.getColumnIndex(POSTER_PATH))
                    showModel.backdrop_path = cursor.getString(cursor.getColumnIndex(BACKDROP_PATH))
                    showList.add(showModel)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return showList
    }

//    fun updateTask(tasks: Tasks): Boolean {
//        val db = this.writableDatabase
//        val values = ContentValues()
//        values.put(NAME, tasks.name)
//        values.put(DESC, tasks.desc)
//        values.put(COMPLETED, tasks.completed)
//        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(tasks.id.toString())).toLong()
//        db.close()
//        return Integer.parseInt("$_success") != -1
//    }

    fun deleteShows(type: String): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, TYPE + "=?", arrayOf(type))
        db.close()
        return Integer.parseInt("$_success") != -1
    }

//    fun deleteAllTasks(): Boolean {
//        val db = this.writableDatabase
//        val _success = db.delete(TABLE_NAME, null, null).toLong()
//        db.close()
//        return Integer.parseInt("$_success") != -1
//    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "showTime"
        private val TABLE_NAME = "showTime"
        private val ID = "id"
        private val SHOW_ID = "show_id"
        private val POSTER_PATH = "poster_path"
        private val BACKDROP_PATH = "backdrop_path"
        private val TYPE = "type"
    }
}
