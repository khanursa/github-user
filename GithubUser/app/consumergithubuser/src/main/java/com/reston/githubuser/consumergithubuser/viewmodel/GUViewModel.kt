package com.reston.githubuser.consumergithubuser.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reston.githubuser.consumergithubuser.model.GithubUserDetail
import com.reston.githubuser.consumergithubuser.service.APIBuilder
import com.reston.githubuser.consumergithubuser.store.DatabaseContract.FavoriteColumns.Companion.ContentUri
import com.reston.githubuser.consumergithubuser.store.MFHelper
import com.reston.githubuser.consumergithubuser.view.`interface`.ProgressInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GUViewModel : ViewModel() {
    private lateinit var disposable: Disposable
    private val apiClient = APIBuilder().apiClient
    private val githubUserList: MutableLiveData<MutableList<GithubUserDetail>> = MutableLiveData()
    private val dataList: MutableList<GithubUserDetail> = ArrayList()
    private var progress: ProgressInterface? = null

    fun firstLaunch(mProgress: ProgressInterface) {
        progress = mProgress
    }

    fun allLocalFavorite(context: Context) {
        dataList.clear()
        val localData = MFHelper(context)
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = context.contentResolver.query(ContentUri, null, null, null, null)
                localData.mapCursorToArrayList(cursor)
            }
            val list = deferredNotes.await()
            if (list.isNotEmpty()) {
                dataList.clear()
                progress?.onSucces()
                dataList.addAll(list)
                githubUserList.postValue(dataList)
            } else {
                progress?.onFailled()
            }
        }
    }

    private fun userDetail(username: String) {
        disposable = apiClient.userDetails(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { userDetails ->
                dataList.add(userDetails)
                githubUserList.postValue(dataList)
            }
    }

    fun searchUser(username: String) {
        dataList.clear()
        disposable = apiClient.searchItems(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ search ->
                search.items?.let {
                    for (x in it.indices)
                        it[x].login?.let { mUserName ->
                            userDetail(mUserName)
                        }
                }
                progress?.onSucces()
            }, {
                progress?.onFailled()
            })
    }

    fun followingUser(username: String) {
        dataList.clear()
        disposable = apiClient.followingElement(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ following ->
                following.let {
                    for (x in it.indices)
                        it[x].login?.let { mUserName ->
                            userDetail(mUserName)
                        }
                }
                progress?.onSucces()
            }, {
                progress?.onFailled()
            })
    }

    fun followerUser(username: String) {
        dataList.clear()
        disposable = apiClient.followerElement(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ follower ->
                follower.let {
                    for (x in it.indices)
                        it[x].login?.let { mUserName ->
                            userDetail(mUserName)
                        }
                }
                progress?.onSucces()
            }, {
                progress?.onFailled()
            })
    }

    fun getUser(): LiveData<MutableList<GithubUserDetail>> = githubUserList
}