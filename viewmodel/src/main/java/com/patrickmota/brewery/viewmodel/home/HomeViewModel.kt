package com.patrickmota.brewery.viewmodel.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrickmota.brewery.core.data.models.BreweryResponse
import com.patrickmota.brewery.core.domain.repositories.BreweryRepository
import com.patrickmota.brewery.viewmodel.ViewData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(
    private val breweryRepository: BreweryRepository,
    private val defaultDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _breweries = MutableLiveData<ViewData<List<BreweryResponse>>>()
    val breweries = _breweries

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _breweries.postValue(
            ViewData(
                ViewData.Status.ERROR,
                error = exception
            )
        )
    }

    fun loadBreweries(city: String) {
        _breweries.postValue(ViewData(ViewData.Status.LOADING))
        CoroutineScope(defaultDispatcher).launch(coroutineExceptionHandler + defaultDispatcher) {
            val response = breweryRepository.getBreweries(city)
            _breweries.postValue(
                ViewData(
                    ViewData.Status.COMPLETE,
                    response
                )
            )
        }
    }
}
