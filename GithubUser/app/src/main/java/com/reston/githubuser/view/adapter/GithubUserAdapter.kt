package com.reston.githubuser.view.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reston.githubuser.R
import com.reston.githubuser.model.GithubUserDetail
import com.reston.githubuser.view.GithubuserDetailActivity
import kotlinx.android.synthetic.main.item_githubuser.view.*

class GithubUserAdapter : RecyclerView.Adapter<GithubUserAdapter.ChildView>() {
    inner class ChildView(view: View) : RecyclerView.ViewHolder(view) {
        fun bindSearchItems(users: GithubUserDetail) {

            itemView.txt_name.text = users.name
            itemView.txt_username.text =
                itemView.context.resources.getString(R.string.item_username, users.login)
            itemView.txt_company.text =
                itemView.context.resources.getString(R.string.item_company, users.company)
            itemView.txt_location.text =
                itemView.context.resources.getString(R.string.item_location, users.location)

            Glide.with(itemView.context)
                .load(Uri.parse(users.avatarURL))
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(itemView.img_photo)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, GithubuserDetailActivity::class.java)
                    .putExtra("EXTRA_USER", users)
                itemView.context.startActivity(intent)
            }
        }
    }

    private var listItems: MutableList<GithubUserDetail> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildView {
        return ChildView(
            LayoutInflater.from(parent.context).inflate(R.layout.item_githubuser, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return if (listItems.isNullOrEmpty()) 0
        else listItems.size
    }

    override fun onBindViewHolder(holder: ChildView, position: Int) {
        holder.bindSearchItems(listItems[position])
    }

    fun addItem(itemList: ArrayList<GithubUserDetail>) {
        listItems.clear()
        listItems.addAll(itemList)
        notifyDataSetChanged()
    }
}