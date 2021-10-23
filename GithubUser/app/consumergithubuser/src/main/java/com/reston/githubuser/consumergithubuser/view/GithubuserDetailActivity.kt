package com.reston.githubuser.consumergithubuser.view

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.reston.githubuser.consumergithubuser.R
import com.reston.githubuser.consumergithubuser.model.GithubUserDetail
import com.reston.githubuser.consumergithubuser.store.DatabaseContract
import com.reston.githubuser.consumergithubuser.store.MFHelper
import kotlinx.android.synthetic.main.activity_githubuser_detail.*

@Suppress("DEPRECATION")
class GithubuserDetailActivity : AppCompatActivity() {

    private var username: String = ""
    val values = ContentValues()
    private val localData by lazy { MFHelper(this@GithubuserDetailActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_githubuser_detail)

        intent?.extras?.getParcelable<GithubUserDetail>("EXTRA_USER")?.let { userDetail ->
            Glide.with(this@GithubuserDetailActivity)
                .load(Uri.parse(userDetail.avatarURL))
                .into(img_photo)
            txt_description.text = resources.getString(
                R.string.description_usergithub,
                userDetail.name,
                userDetail.login,
                userDetail.company,
                userDetail.location,
                userDetail.publicRepos.toString()
            )

            values.put(DatabaseContract.NAME, userDetail.name)
            values.put(DatabaseContract.COMPANY, userDetail.company)
            values.put(DatabaseContract.LOCATION, userDetail.location)
            values.put(DatabaseContract.REPOSITORY, userDetail.publicRepos)
            values.put(DatabaseContract.FOLLOWING, userDetail.following)
            values.put(DatabaseContract.FOLLOWER, userDetail.followers)
            values.put(DatabaseContract.LOGIN, userDetail.login)
            values.put(DatabaseContract.AVATAR, userDetail.avatarURL)

            userDetail.login?.let {
                username = it
                if (localData.findByUserName(username)) {
                    ib_favorite.background =
                        resources.getDrawable(R.drawable.ic_outline_favorite_24)
                } else {
                    ib_favorite.background =
                        resources.getDrawable(R.drawable.ic_baseline_favorite_border_24)
                }
            }

            btn_follower.text =
                resources.getString(R.string.button_follower, userDetail.followers.toString())
            btn_follower.setOnClickListener {
                startActivity(
                    Intent(this@GithubuserDetailActivity, GithubUserFollowActivity::class.java)
                        .putExtra("EXTRA", userDetail)
                        .putExtra("TABS", GithubUserFollowActivity.FOLLOWERS)
                )
            }
            btn_following.text =
                resources.getString(R.string.button_following, userDetail.following.toString())
            btn_following.setOnClickListener {
                startActivity(
                    Intent(this@GithubuserDetailActivity, GithubUserFollowActivity::class.java)
                        .putExtra("EXTRA", userDetail)
                        .putExtra("TABS", GithubUserFollowActivity.FOLLOWING)
                )
            }

            ib_favorite.setOnClickListener {
                if (localData.findByUserName(username)) {
                    localData.deleteByUserName(username)
                    ib_favorite.background =
                        resources.getDrawable(R.drawable.ic_baseline_favorite_border_24)
                } else {
                    localData.saveUser(values)
                    ib_favorite.background =
                        resources.getDrawable(R.drawable.ic_outline_favorite_24)
                }
            }
        }
    }
}
