package com.patrickmota.brewery.viewmodel.rate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.patrickmota.brewery.data.local.model.RateModel
import com.patrickmota.brewery.data.local.repository.RateRepository
import com.patrickmota.brewery.viewmodel.ViewData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RateViewModel(
    private val repository: RateRepository,
    private val defaultDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _rating = MutableLiveData<ViewData<RateModel>>()
    val rating = _rating

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _rating.postValue(
            ViewData(
                ViewData.Status.ERROR,
                error = exception
            )
        )
    }

    fun getRateById(id: String) {
        _rating.postValue(ViewData(ViewData.Status.LOADING))
        CoroutineScope(defaultDispatcher).launch(coroutineExceptionHandler + defaultDispatcher) {
            val response = repository.getRateById(id)
            _rating.postValue(
                ViewData(
                    ViewData.Status.COMPLETE,
                    response
                )
            )
        }
    }

    fun addRating(rateModel: RateModel) {
        CoroutineScope(defaultDispatcher).launch(coroutineExceptionHandler + defaultDispatcher) {
            repository.addRate(rateModel)
        }
    }
}