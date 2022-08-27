package com.example.semplenotesapp.updateoraddnotescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semplenotesapp.data.Note
import com.example.semplenotesapp.data.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateOrAddNoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    fun saveNote(newNote: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.addNote(newNote)
    }

    fun updateNote(existingNote: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.updateNote(existingNote)
    }
}