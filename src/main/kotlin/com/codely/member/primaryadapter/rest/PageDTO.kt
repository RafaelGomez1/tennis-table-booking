package com.codely.member.primaryadapter.rest

data class PageDTO<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
