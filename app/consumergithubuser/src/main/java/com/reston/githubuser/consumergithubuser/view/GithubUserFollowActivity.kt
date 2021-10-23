@file:Suppress("DEPRECATION")

package com.reston.githubuser.consumergithubuser.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.reston.githubuser.consumergithubuser.R
import com.reston.githubuser.consumergithubuser.model.GithubUserDetail
import kotlinx.android.synthetic.main.activity_githubuser_follow.*

class GithubUserFollowActivity : AppCompatActivity() {
    companion object {
        const val FOLLOWERS = 0
        const val FOLLOWING = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_githubuser_follow)
        intent.extras?.let { bundle ->
            bundle.getParcelable<GithubUserDetail>("EXTRA")?.login?.let {
                view_pager.adapter = PagerFollowGithubUser(supportFragmentManager, it)
                tab_layout.setupWithViewPager(view_pager)
                bundle.getInt("TABS").let { type ->
                    if (type == 0) tab_layout.getTabAt(0)?.select()
                    else tab_layout.getTabAt(1)?.select()
                }
            }
        }
    }

    inner class PagerFollowGithubUser(fm: FragmentManager, private val userName: String) :
        FragmentPagerAdapter(fm) {
        private val pageTitle = arrayOf("List Follower", "List Following")
        override fun getItem(position: Int): Fragment {
            Log.d("GITAS", "posisi : $position")
            return when (position) {
                0 -> GithubUserFollower().bridge(userName)
                else -> GithubUserFollowing().bridge(userName)
            }
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            return pageTitle[position]
        }
    }
}