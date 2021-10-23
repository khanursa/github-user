package com.reston.githubuser.consumergithubuser.view

//import com.reston.githubuser.consumergithubuser.viewmodel.SearchViewModel
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reston.githubuser.consumergithubuser.R
import com.reston.githubuser.consumergithubuser.store.DatabaseContract.FavoriteColumns.Companion.ContentUri
import com.reston.githubuser.consumergithubuser.view.`interface`.ProgressInterface
import com.reston.githubuser.consumergithubuser.view.adapter.GithubUserAdapter
import com.reston.githubuser.consumergithubuser.viewmodel.GUViewModel
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, ProgressInterface {

    companion object {
        const val TAG = "MAINACTIVITY"
    }

    private lateinit var vModelGU: GUViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = GithubUserAdapter()
        rv_list.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rv_list.adapter = adapter
        val key: String = resources.getString(R.string.key_open_all_favorite)
        text_note.text = resources.getString(R.string.note, key)

        sv_users.setOnQueryTextListener(this)

        vModelGU = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GUViewModel::class.java)
        vModelGU.firstLaunch(this)

        vModelGU.getUser().observe(this, Observer { itemList ->
            Log.d(TAG, "itemlist : $itemList")
            adapter.addItem(itemList as ArrayList)
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                text_identity.visibility = View.VISIBLE
                vModelGU.allLocalFavorite(this@MainActivity)
            }
        }

        contentResolver.registerContentObserver(ContentUri, true, myObserver)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            rv_list.visibility = View.VISIBLE
            text_nolist.visibility = View.GONE
            loading.visibility = View.VISIBLE
            if (it.equals(resources.getString(R.string.key_open_all_favorite), true)) {
                text_identity.visibility = View.VISIBLE
                vModelGU.allLocalFavorite(this@MainActivity)
            } else {
                text_identity.visibility = View.INVISIBLE
                vModelGU.searchUser(it)
            }
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onSucces() {
        loading.visibility = View.GONE
        text_nolist.visibility = View.GONE
        rv_list.visibility = View.VISIBLE
    }

    override fun onFailled() {
        loading.visibility = View.GONE
        text_nolist.visibility = View.VISIBLE
        rv_list.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
