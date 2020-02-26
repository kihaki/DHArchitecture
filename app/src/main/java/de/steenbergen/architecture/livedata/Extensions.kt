package de.steenbergen.architecture.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

val ViewModel.scope
    get() = this.viewModelScope


inline fun <X, Y> LiveData<X>.switchMap(
    crossinline transform: (X) -> LiveData<Y>
): LiveData<Y> = Transformations.switchMap(this) { transform(it) }
