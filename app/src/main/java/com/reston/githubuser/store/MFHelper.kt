package com.reston.githubuser.store

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.reston.githubuser.model.GithubUserDetail
import com.reston.githubuser.store.DatabaseContract.FavoriteColumns.Companion.ContentUri

class MFHelper(val context: Context) {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<GithubUserDetail> {
        val githubUserFavorite = ArrayList<GithubUserDetail>()
        cursor?.apply {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(DatabaseContract.NAME))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.LOCATION))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.LOGIN))
                val follower = getLong(getColumnIndexOrThrow(DatabaseContract.FOLLOWER))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.AVATAR))
                val following = getLong(getColumnIndexOrThrow(DatabaseContract.FOLLOWING))
                val repository = getLong(getColumnIndexOrThrow(DatabaseContract.REPOSITORY))

                githubUserFavorite.add(
                    GithubUserDetail(
                        name = name,
                        login = login,
                        avatarURL = avatarUrl,
                        followers = follower,
                        following = following,
                        publicRepos = repository,
                        company = company,
                        location = location
                    )
                )
            }
        }
        return githubUserFavorite
    }

    fun mapCursorToObject(cursor: Cursor?): GithubUserDetail {
        var user = GithubUserDetail()
        cursor?.apply {
            moveToFirst()
            val name = getString(getColumnIndexOrThrow(DatabaseContract.NAME))
            val company = getString(getColumnIndexOrThrow(DatabaseContract.COMPANY))
            val location = getString(getColumnIndexOrThrow(DatabaseContract.LOCATION))
            val login = getString(getColumnIndexOrThrow(DatabaseContract.LOGIN))
            val follower = getLong(getColumnIndexOrThrow(DatabaseContract.FOLLOWER))
            val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.AVATAR))
            val following = getLong(getColumnIndexOrThrow(DatabaseContract.FOLLOWING))
            val repository = getLong(getColumnIndexOrThrow(DatabaseContract.REPOSITORY))
            user = GithubUserDetail(
                name = name,
                login = login,
                avatarURL = avatarUrl,
                followers = follower,
                following = following,
                publicRepos = repository,
                company = company,
                location = location
            )
        }
        return user
    }

    fun saveUser(value: ContentValues) {
        try {
        } catch (err: java.lang.Exception) {
            context.contentResolver.insert(ContentUri, value)

        }
    }

    fun findByUserName(username: String): Boolean {
        return try {
            val uriByUsername = Uri.parse("${ContentUri}/$username")
            val cursor = context.contentResolver.query(uriByUsername, null, null, null, null)
            if (cursor != null) {
                if (mapCursorToObject(cursor).login.equals(username, true)) {
                    cursor.close()
                    true
                } else {
                    cursor.close()
                    false
                }
            } else {
                false
            }
        } catch (er: Exception) {
            false
        }
    }

    fun deleteByUserName(username: String) {
        try {
            val uriByUsername = Uri.parse("${ContentUri}/$username")
            context.contentResolver.delete(uriByUsername, null, null)
        } catch (er: Exception) {
        }
    }
}