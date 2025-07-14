package ru.keckinnd.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.keckinnd.data.local.AppDatabase
import ru.keckinnd.data.local.dao.CharacterDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "rick_and_morty.db")
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideCharacterDao(db: AppDatabase): CharacterDao =
        db.characterDao()
}
