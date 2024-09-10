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

    @GET("schedule")
    fun getTeamSchedule(@Query("date") date: String): Call<List<MetaverseMatch>>

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

    // 검색 API 엔드포인트 추가
    @GET("api/boards/search")
    fun searchBoards(
        @Query("keyword") keyword: String,
        @Query("type") type: String,
        @Query("page") page: Int
    ): Call<PagedBoardResponse>

    // 특정 경기의 문자 중계 데이터 가져오기
    @GET("live-stream/{teamName}/{matchDate}")
    fun getLiveStream(
        @Path("teamName") teamName: String,
        @Path("matchDate") matchDate: String
    ): Call<List<String>>

    @GET("match/get")
    fun getMatchDetails(
        @Query("teamName") teamName: String,
        @Query("matchDate") matchDate: String
    ): Call<MatchResponse>
}
