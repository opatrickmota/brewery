package com.patrickmota.brewery.di

import android.app.Application
import androidx.room.Room
import com.patrickmota.brewery.data.local.repository.FavoritesRepository
import com.patrickmota.brewery.data.local.repository.FavoritesRepositoryImpl
import com.patrickmota.brewery.data.local.room.BreweryDatabase
import com.patrickmota.brewery.data.local.room.FavoritesDao
import com.patrickmota.brewery.data.remote.api.BreweryService
import com.patrickmota.brewery.data.remote.repository.BreweryRepository
import com.patrickmota.brewery.data.remote.repository.BreweryRepositoryImpl
import com.patrickmota.brewery.viewmodel.detail.DetailViewModel
import com.patrickmota.brewery.viewmodel.favorite.FavoriteViewModel
import com.patrickmota.brewery.viewmodel.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KoinModules {

    val networkModule = module {
        single(named("BASE_URL")) {
            "https://api.openbrewerydb.org/v1/"
        }

        single {
            val loggingInterceptor: HttpLoggingInterceptor = get()
            OkHttpClient.Builder().apply {
                addInterceptor(loggingInterceptor)
            }.build()
        }

        single {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        single {
            Retrofit.Builder()
                .baseUrl(get<String>(named("BASE_URL")))
                .addConverterFactory(GsonConverterFactory.create())
                .client(get())
                .build()
        }

        single {
            get<Retrofit>().create(BreweryService::class.java)
        }
    }

    val repositoryModule = module {
        single<BreweryRepository> {
            BreweryRepositoryImpl(get())
        }

        single<FavoritesRepository> {
            FavoritesRepositoryImpl(get())
        }
    }

    val viewModelModule = module {
        single { Dispatchers.IO }

        single {
            HomeViewModel(get(), get())
        }

        single {
            DetailViewModel(get(), get())
        }

        single {
            FavoriteViewModel(get(), get())
        }
    }

    val databaseModule = module {
        fun provideDatabase(application: Application): BreweryDatabase {
            return Room.databaseBuilder(application, BreweryDatabase::class.java, "BREWERY_DB")
                .build()
        }

        fun provideFavoriteDao(database: BreweryDatabase): FavoritesDao {
            return database.favoritesDao()
        }

        single { provideDatabase(androidApplication()) }
        single { provideFavoriteDao(get()) }
    }
}