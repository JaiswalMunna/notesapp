package com.example.notesapp.di
import android.app.Application
import androidx.room.Room
import com.example.notesapp.data_layer.NoteDB
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {

    fun provideDatabase(application :Application):NoteDB{
        return Room.databaseBuilder(
            context = application,
            NoteDB::class.java,
            "note_database.sql"
        ).build()
    }
}
