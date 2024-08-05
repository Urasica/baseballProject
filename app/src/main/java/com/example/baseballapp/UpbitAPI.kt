package com.example.baseballapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UpbitAPI {
    @GET("player")
    fun getAllPlayers(): Call<List<PlayerData>>

    @GET("schedule")
    fun getAllSchedule(): Call<List<GameListData>>

    @GET("team")
    fun getAllTeams(): Call<List<TeamRankData>>

    @GET("hitter")
    fun getAllHitters(): Call<List<HitterRankData>>

    @GET("pitcher")
    fun getAllPitchers(): Call<List<PitcherRankData>>

    @GET("schedule")
    fun getSchedule(@Query("date") date: String): Call<List<ScheduleData>>

    @POST("boards/create")
    fun submitPost(@Body post: Post): Call<Void>

    @GET("boards/getBoardsByPage")
    fun getBoardsByPage(@Query("type") type: String, @Query("page") page: Int, @Query("size") size: Int): Call<PagedBoardResponse>

    @DELETE("boards/delete/{id}")
    fun deletePost(@Path("id") id: Long): Call<Void>

    @POST("boards/upvote/{id}")
    fun upvotePost(@Path("id") id: Long): Call<Void>

    @POST("comments/create")
        fun submitComment(@Query("boardId") boardId: Long, @Body comment: CommentData): Call<Void>

    @GET("comments/board/{boardId}")
        fun getComments(@Path("boardId") boardId: Long): Call<List<CommentData>>


}

