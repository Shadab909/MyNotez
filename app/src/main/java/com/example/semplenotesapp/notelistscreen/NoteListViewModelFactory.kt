package com.example.semplenotesapp.notelistscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semplenotesapp.data.NoteRepository

class NoteListViewModelFactory(private val noteRepository: NoteRepository):ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            return NoteListViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}


