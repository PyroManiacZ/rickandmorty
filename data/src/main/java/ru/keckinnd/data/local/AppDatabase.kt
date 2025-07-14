package ru.keckinnd.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.keckinnd.data.local.dao.CharacterDao
import ru.keckinnd.data.local.entity.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}
