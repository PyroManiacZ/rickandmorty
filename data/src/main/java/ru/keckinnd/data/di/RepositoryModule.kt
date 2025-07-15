package ru.keckinnd.data.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.keckinnd.domain.repository.CharactersRepository
import ru.keckinnd.data.repository.CharactersRepositoryImpl
import javax.inject.Singleton

@Suppress("unused")

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindCharactersRepository(
        impl: CharactersRepositoryImpl
    ): CharactersRepository
}
