package com.example.notesapp.di
import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {
    fun provideDatabase(application :Application):NoteDatabase{
        return Room.databaseBuilder(
            context = application,
            NoteDatabase::class.java,
            "note_database.sql"
        ).build()
    }
}
