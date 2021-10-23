package com.reston.githubuser.store

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.reston.githubuser"
    const val SCHEME = "content"

    /** --------------------------------- FAVORITE USER ------------------------------------- */
    const val FAVORITE_TABLE = "TABLE_FAVORITE"
    const val NAME = "NAME"
    const val LOGIN = "USER_NAME"
    const val AVATAR = "AVATAR"
    const val COMPANY = "COMPANY"
    const val LOCATION = "LOCATION"
    const val FOLLOWER = "FOLLOWER"
    const val FOLLOWING = "FOLLOWING"
    const val REPOSITORY = "REPOSITORY"

    /** ------------------------------------------------------------------------------------- */

    class FavoriteColumns : BaseColumns {

        companion object {
            val ContentUri: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(FAVORITE_TABLE)
                .build()
        }

    }
}