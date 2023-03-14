package com.example.semplenotesapp.updateoraddnotescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semplenotesapp.data.NoteRepository
import com.example.semplenotesapp.notelistscreen.NoteListViewModel

class UpdateOrAddNoteViewModelFactory (private val noteRepository: NoteRepository): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateOrAddNoteViewModel::class.java)) {
            return UpdateOrAddNoteViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}



