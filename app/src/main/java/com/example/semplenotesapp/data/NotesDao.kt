package com.example.semplenotesapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE

@Dao
interface NotesDao {
    @Insert(onConflict = IGNORE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note : Note)

    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAllNotes() : LiveData<List<Note>>
}