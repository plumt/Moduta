package com.yun.seoul.data.local.entity.query

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "search_queries")
data class SearchQueryEntity(
    @PrimaryKey
    val query: String,
    val type: String,
    val lastSearched: LocalDateTime = LocalDateTime.now()
)