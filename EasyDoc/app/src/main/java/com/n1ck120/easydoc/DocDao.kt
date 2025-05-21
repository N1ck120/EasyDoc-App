package com.n1ck120.easydoc

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DocDao {
    @Query("SELECT * FROM doc")
    fun getAll(): Flow<List<Doc>>

    @Query("SELECT * FROM doc WHERE uid IN (:docIds)")
    suspend fun loadAllByIds(docIds: IntArray): List<Doc>

    @Query("SELECT * FROM doc WHERE doc_name LIKE :name OR " +
            "content LIKE :content LIMIT 1")
    suspend fun findByName(name: String, content: String): Doc?

    @Insert
    suspend fun insertAll(vararg doc: Doc)

    @Query("UPDATE doc SET title = :title , content = :content, doc_name = :docname, date = :date WHERE uid = :uid")
    suspend fun update(uid: Long, title: String, content: String, docname: String, date: String)

    @Delete
    suspend fun delete(doc: Doc)
}