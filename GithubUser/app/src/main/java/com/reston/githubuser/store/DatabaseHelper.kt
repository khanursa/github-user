package com.reston.githubuser.store

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.reston.githubuser.store.DatabaseContract.AVATAR
import com.reston.githubuser.store.DatabaseContract.COMPANY
import com.reston.githubuser.store.DatabaseContract.FAVORITE_TABLE
import com.reston.githubuser.store.DatabaseContract.FOLLOWER
import com.reston.githubuser.store.DatabaseContract.FOLLOWING
import com.reston.githubuser.store.DatabaseContract.LOCATION
import com.reston.githubuser.store.DatabaseContract.LOGIN
import com.reston.githubuser.store.DatabaseContract.NAME
import com.reston.githubuser.store.DatabaseContract.REPOSITORY

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "GithubUser.db"

        private const val TABLE_FAVORITE =
            "CREATE TABLE $FAVORITE_TABLE" +
                    " ($NAME TEXT NOT NULL," +
                    " $LOGIN TEXT NOT NULL," +
                    " $AVATAR TEXT," +
                    " $COMPANY TEXT," +
                    " $LOCATION TEXT," +
                    " $FOLLOWER TEXT," +
                    " $FOLLOWING TEXT," +
                    " $REPOSITORY TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $FAVORITE_TABLE")
        onCreate(db)
    }
}