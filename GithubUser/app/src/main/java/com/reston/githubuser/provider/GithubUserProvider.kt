package com.reston.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.reston.githubuser.store.DatabaseContract
import com.reston.githubuser.store.DatabaseContract.AUTHORITY
import com.reston.githubuser.store.DatabaseContract.FavoriteColumns.Companion.ContentUri
import com.reston.githubuser.store.FavoriteDataBase

class GithubUserProvider : ContentProvider() {

    companion object {
        private const val GUSER = 1
        private const val GUSER_NAME = 2
        private lateinit var dbFavorite: FavoriteDataBase

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, DatabaseContract.FAVORITE_TABLE, GUSER)
            sUriMatcher.addURI(AUTHORITY, "${DatabaseContract.FAVORITE_TABLE}/*", GUSER_NAME)
        }
    }

    override fun onCreate(): Boolean {
        dbFavorite = FavoriteDataBase.getInstance(context as Context)
        dbFavorite.openDb()
        return true
    }

    override fun query(
        uri: Uri,
        strings: Array<String>?,
        s: String?,
        strings1: Array<String>?,
        s1: String?
    ): Cursor? {

        Log.d("URI", "onProvider = $uri\n${sUriMatcher.match(uri)}")
        return when (sUriMatcher.match(uri)) {
            GUSER -> dbFavorite.queryAll()
            GUSER_NAME -> dbFavorite.queryByUserName(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (GUSER) {
            sUriMatcher.match(uri) -> dbFavorite.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(ContentUri, null)

        return Uri.parse("$ContentUri/$added")
    }


    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (GUSER_NAME) {
            sUriMatcher.match(uri) -> dbFavorite.delete(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(ContentUri, null)

        return deleted
    }

}
