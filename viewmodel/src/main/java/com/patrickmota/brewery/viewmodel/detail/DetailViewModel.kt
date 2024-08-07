package com.patrickmota.brewery.viewmodel.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.patrickmota.brewery.core.data.models.BreweryResponse
import com.patrickmota.brewery.core.domain.repositories.BreweryRepository
import com.patrickmota.brewery.viewmodel.ViewData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DetailViewModel(
    private val breweryRepository: BreweryRepository,
    private val defaultDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _brewery = MutableLiveData<ViewData<BreweryResponse>>()
    val brewery = _brewery

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _brewery.postValue(
            ViewData(
                ViewData.Status.ERROR,
                error = exception
            )
        )
    }

    fun loadBrewery(id: String) {
        _brewery.postValue(ViewData(ViewData.Status.LOADING))

        CoroutineScope(defaultDispatcher).launch(coroutineExceptionHandler + defaultDispatcher) {
            val response = breweryRepository.getBreweryById(id)

            _brewery.postValue(
                ViewData(
                    ViewData.Status.COMPLETE,
                    response
                )
            )
        }
    }
}
