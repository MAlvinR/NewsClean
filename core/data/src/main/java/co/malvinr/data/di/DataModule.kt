package co.malvinr.data.di

import co.malvinr.core.domain.repository.ArticleRepository
import co.malvinr.core.domain.repository.CategoryRepository
import co.malvinr.core.domain.repository.SourceRepository
import co.malvinr.data.repository.ArticleRepositoryImpl
import co.malvinr.data.repository.CategoryRepositoryImpl
import co.malvinr.data.repository.SourceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindArticleRepository(
        articleRepositoryImpl: ArticleRepositoryImpl
    ) : ArticleRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ) : CategoryRepository

    @Binds
    @Singleton
    abstract fun bindSourceRepository(
        sourceRepositoryImpl: SourceRepositoryImpl
    ) : SourceRepository
}