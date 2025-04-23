package com.n1ck120.easydoc

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Doc(
    @PrimaryKey var uid: Int,
    @ColumnInfo(name = "doc_name") var doc_name: String?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "content") var content: String?
)
