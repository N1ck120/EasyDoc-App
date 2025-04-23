package com.n1ck120.easydoc

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DocDao {
    @Query("SELECT * FROM doc")
    suspend fun getAll(): List<Doc>

    @Query("SELECT * FROM doc WHERE uid IN (:docIds)")
    suspend fun loadAllByIds(docIds: IntArray): List<Doc>

    @Query("SELECT * FROM doc WHERE doc_name LIKE :name OR " +
            "content LIKE :content LIMIT 1")
    suspend fun findByName(name: String, content: String): Doc

    @Insert
    suspend fun insertAll(vararg doc: Doc)

    @Delete
    suspend fun delete(doc: Doc)
}