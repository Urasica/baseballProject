package com.example.baseballapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val title: String,
    val content: String,
    val authorId: String,
    val createdAt: String,
    val updatedAt: String,
    val type: String
) : Parcelable

@Parcelize
data class CommentData(
    val id: Int,
    val content: String,
    val authorId: String,
    val createdAt: String,
    val board: String
) : Parcelable

@Parcelize
data class BoardData(
    val id: Int,
    val title: String,
    val content: String,
    val authorId: String,
    val createdAt: String,
    val updatedAt: String,
    val upVote: Int,
    val type: String,
    val commentCount: Int,
    var comments: List<CommentData>? = emptyList() // 빈 리스트로 초기화, nullable로 변경
) : Parcelable

data class BoardPageResponse(
    val totalPages: Int,
    val totalElements: Int,
    val content: List<BoardData>,
    val number: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int
)

data class PagedBoardResponse(
    val totalPages: Int,
    val totalElements: Int,
    val content: List<BoardData>
)
