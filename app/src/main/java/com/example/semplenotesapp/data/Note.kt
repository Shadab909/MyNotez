package com.example.semplenotesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    var title : String,
    var content : String ,
//    var time : String ,
   var time : Long
) : Serializable
