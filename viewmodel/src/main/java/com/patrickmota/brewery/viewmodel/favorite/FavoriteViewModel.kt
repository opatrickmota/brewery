package com.patrickmota.brewery.viewmodel.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrickmota.brewery.core.data.models.BreweryModel
import com.patrickmota.brewery.core.domain.repositories.FavoritesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: FavoritesRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _brewery = MutableLiveData<com.patrickmota.brewery.viewmodel.ViewData<BreweryModel>>()
    val brewery = _brewery

    private val _breweries = MutableLiveData<com.patrickmota.brewery.viewmodel.ViewData<List<BreweryModel>>>()
    val breweries = _breweries

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _brewery.postValue(
            com.patrickmota.brewery.viewmodel.ViewData(
                com.patrickmota.brewery.viewmodel.ViewData.Status.ERROR,
                error = exception
            )
        )
    }

    fun addFavorite(breweryModel: BreweryModel) =
        viewModelScope.launch(coroutineExceptionHandler + defaultDispatcher) {
            repository.addFavorite(breweryModel)
        }

    fun deleteFavorite(breweryModel: BreweryModel) =
        viewModelScope.launch(coroutineExceptionHandler + defaultDispatcher) {
            repository.deleteBrewery(breweryModel)
        }

    fun loadFavoriteById(id: String) {
        _brewery.postValue(com.patrickmota.brewery.viewmodel.ViewData(com.patrickmota.brewery.viewmodel.ViewData.Status.LOADING))

        viewModelScope.launch(defaultDispatcher + coroutineExceptionHandler) {
            val response = repository.getBreweryById(id)
            _brewery.postValue(
                com.patrickmota.brewery.viewmodel.ViewData(
                    com.patrickmota.brewery.viewmodel.ViewData.Status.COMPLETE,
                    response
                )
            )
        }
    }

    fun loadFavoriteBreweries() {
        _breweries.postValue(com.patrickmota.brewery.viewmodel.ViewData(com.patrickmota.brewery.viewmodel.ViewData.Status.LOADING))

        viewModelScope.launch(defaultDispatcher + coroutineExceptionHandler) {
            val response = repository.getBreweries()
            _breweries.postValue(
                com.patrickmota.brewery.viewmodel.ViewData(
                    com.patrickmota.brewery.viewmodel.ViewData.Status.COMPLETE,
                    response
                )
            )
        }
    }
}