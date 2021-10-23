package com.reston.githubuser.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reston.githubuser.R
import com.reston.githubuser.view.`interface`.ProgressInterface
import com.reston.githubuser.view.adapter.GithubUserAdapter
import com.reston.githubuser.viewmodel.GUViewModel
import kotlinx.android.synthetic.main.fragment_githubuser_following_follower.view.*

class GithubUserFollowing : Fragment(), ProgressInterface {
    private lateinit var vModelGU: GUViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_githubuser_following_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vModelGU = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GUViewModel::class.java)
        vModelGU.firstLaunch(this)

        arguments?.getString("EXTRA_STRING")?.let {
            view.loading.visibility = View.VISIBLE
            val adapter = GithubUserAdapter()
            view.rv_follows.layoutManager =
                LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            view.rv_follows.adapter = adapter
            vModelGU.followingUser(it)

            vModelGU.getUser().observe(this, Observer { itemList ->
                adapter.addItem(itemList as ArrayList)
            })
        }
    }

    fun bridge(username: String): GithubUserFollowing {
        val dataBundle = Bundle()
        dataBundle.putString("EXTRA_STRING", username)
        val argument = GithubUserFollowing()
        argument.arguments = dataBundle
        return argument
    }

    override fun onSucces() {
        view?.loading?.visibility = View.GONE
    }

    override fun onFailled() {
        view?.loading?.visibility = View.GONE
        Toast.makeText(requireContext(), "Gak bisa ambil data following", Toast.LENGTH_SHORT).show()
    }
}
