package com.reston.githubuser.service

import com.reston.githubuser.BuildConfig
import com.reston.githubuser.model.FollowersElement
import com.reston.githubuser.model.FollowingElement
import com.reston.githubuser.model.GithubUserDetail
import com.reston.githubuser.model.SearchBase
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface APIClient {
    companion object {
        const val USERNAME = "username"
        const val SEARCH = "search/users"
        const val USER_DETAIL = "users/{username}"
        const val FOLLOWER = "users/{username}/followers"
        const val FOLLOWING = "users/{username}/following"
    }

    @GET(SEARCH)
    @Headers(BuildConfig.BASE_TOKEN)
    fun searchItems(
        @Query("q") username: String
    ): Observable<SearchBase>

    @GET(USER_DETAIL)
    @Headers(BuildConfig.BASE_TOKEN)
    fun userDetails(
        @Path(USERNAME) username: String
    ): Observable<GithubUserDetail>

    @GET(FOLLOWER)
    @Headers(BuildConfig.BASE_TOKEN)
    fun followerElement(
        @Path(USERNAME) username: String
    ): Observable<ArrayList<FollowersElement>>

    @GET(FOLLOWING)
    @Headers(BuildConfig.BASE_TOKEN)
    fun followingElement(
        @Path(USERNAME) username: String
    ): Observable<ArrayList<FollowingElement>>
}