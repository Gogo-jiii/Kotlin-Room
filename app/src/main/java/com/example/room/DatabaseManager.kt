package com.example.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserModel::class], version = 1)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private var instance: DatabaseManager? = null
        @Synchronized
        fun getInstance(context: Context): DatabaseManager? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseManager::class.java, "databasename"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}