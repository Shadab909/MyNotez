package com.example.semplenotesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class] , version = 2 , exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract val notesDao : NotesDao

    companion object{

        @Volatile
        private var INSTANCE : NotesDatabase? = null

        fun getInstance(context: Context) : NotesDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}