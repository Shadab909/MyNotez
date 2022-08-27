package com.example.semplenotesapp.data

class NoteRepository(private val database: NotesDatabase) {

    fun getAllNotes() = database.notesDao.getAllNotes()

    suspend fun addNote(note: Note) = database.notesDao.addNote(note)

    suspend fun updateNote(note: Note) = database.notesDao.updateNote(note)

    suspend fun deleteNote(note: Note) = database.notesDao.deleteNote(note)
}