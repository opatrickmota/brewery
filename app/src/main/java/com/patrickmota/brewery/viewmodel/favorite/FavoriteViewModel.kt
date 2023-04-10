package com.patrickmota.brewery.viewmodel.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.patrickmota.brewery.data.local.model.BreweryModel
import com.patrickmota.brewery.data.local.repository.FavoritesRepository
import com.patrickmota.brewery.viewmodel.ViewData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: FavoritesRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _brewery = MutableLiveData<ViewData<BreweryModel>>()
    val brewery = _brewery

    private val _breweries = MutableLiveData<ViewData<List<BreweryModel>>>()
    val breweries = _breweries

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _brewery.postValue(
            ViewData(
                ViewData.Status.ERROR,
                error = exception
            )
        )
    }

    fun addFavorite(breweryModel: BreweryModel) =
        CoroutineScope(defaultDispatcher).launch(coroutineExceptionHandler + defaultDispatcher) {
            repository.addFavorite(breweryModel)
        }

    fun deleteFavorite(breweryModel: BreweryModel) =
        CoroutineScope(defaultDispatcher).launch(coroutineExceptionHandler + defaultDispatcher) {
            repository.deleteBrewery(breweryModel)
        }

    fun loadFavoriteById(id: String) {
        _brewery.postValue(ViewData(ViewData.Status.LOADING))

        CoroutineScope(defaultDispatcher).launch(defaultDispatcher + coroutineExceptionHandler) {
            val response = repository.getBreweryById(id)
            _brewery.postValue(
                ViewData(
                    ViewData.Status.COMPLETE,
                    response
                )
            )
        }
    }

    fun loadFavoriteBreweries() {
        _breweries.postValue(ViewData(ViewData.Status.LOADING))

        CoroutineScope(defaultDispatcher).launch(defaultDispatcher + coroutineExceptionHandler) {
            val response = repository.getBreweries()
            _breweries.postValue(
                ViewData(
                    ViewData.Status.COMPLETE,
                    response
                )
            )
        }
    }
}