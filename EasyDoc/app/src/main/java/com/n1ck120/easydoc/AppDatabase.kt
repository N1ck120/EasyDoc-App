package com.n1ck120.easydoc

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Doc::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): DocDao
}