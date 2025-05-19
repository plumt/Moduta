package com.yun.seoul.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yun.seoul.data.local.entity.query.SearchQueryEntity

@Dao
interface SearchQueryDao {
    // 특정 쿼리가 신선한지 확인 (1일 이내 검색했는지)
    @Query("SELECT EXISTS(SELECT 1 FROM search_queries WHERE `query` = :searchQuery AND `type` = :searchType AND (julianday('now') - julianday(lastSearched)) < 1)")
    suspend fun isQueryFresh(searchQuery: String, searchType: String): Boolean

    // 검색 쿼리 저장/업데이트
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuery(searchQueryEntity: SearchQueryEntity)
}