package com.tokopedia.workshopnovember.ui.main

import androidx.lifecycle.*
import com.tokopedia.workshopnovember.data.BookRepository
import com.tokopedia.workshopnovember.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(repo: BookRepository): ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _message: SingleLiveEvent<String> = SingleLiveEvent()
    val message: LiveData<String> get() = _message

    private val _query: MutableLiveData<String> = MutableLiveData()
    val result = _query.switchMap {
        _loading.value = true

        liveData {
            try {
                val books = repo.searchWithQuery(it).map {
                    it.toBookEntity()
                }
                emit(books)
            } catch (e: Exception) {
                _message.value = "something went wrong"
            }
            _loading.value = false
        }
    }

    fun search(q: String) {
        _query.value = q
    }

}