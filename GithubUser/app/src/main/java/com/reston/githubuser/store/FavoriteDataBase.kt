package com.reston.githubuser.store

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.reston.githubuser.store.DatabaseContract.FAVORITE_TABLE
import com.reston.githubuser.store.DatabaseContract.LOGIN
import java.sql.SQLException

class FavoriteDataBase(context: Context) {
    companion object {
        private var INSTANCE: FavoriteDataBase? = null

        fun getInstance(context: Context): FavoriteDataBase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteDataBase(context)
        }
    }

    private var table: String = FAVORITE_TABLE
    private var userName: String = LOGIN
    private var dbHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    @Throws(SQLException::class)
    fun openDb() {
        database = dbHelper.writableDatabase
    }

    fun closeDb() {
        dbHelper.close()
        if (database.isOpen) database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            table,
            null,
            null,
            null,
            null,
            null,
            "$userName ASC",
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(table, null, values)
    }

    fun delete(mUserName: String): Int {
        return database.delete(table, "$userName = '$mUserName'", null)
    }

    fun queryByUserName(mUserName: String): Cursor {
        return database.query(
            table,
            null,
            "$userName = ?",
            arrayOf(mUserName),
            null,
            null,
            null,
            null
        )
    }
}