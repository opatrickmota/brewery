package com.patrickmota.brewery.viewmodel

class ViewData<D>(val status: Status, val data: D? = null, val error: Throwable? = null) {
    enum class Status {
        LOADING, COMPLETE, ERROR
    }
}