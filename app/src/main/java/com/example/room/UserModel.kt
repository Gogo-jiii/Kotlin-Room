package com.example.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserModel(var name: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    override fun toString(): String {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}'
    }
}