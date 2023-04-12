package com.example.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: UserModel?)

    @get:Query("select * from users")
    val allUsers: List<UserModel?>?

    @Query("select * from users where id like :id")
    fun getUser(id: Int): UserModel?

    @Update
    fun updateUser(vararg user: UserModel?)

    @Delete
    fun deleteUser(vararg user: UserModel?)
}