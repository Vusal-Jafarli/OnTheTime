package com.example.onthetime.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onthetime.model.News
import com.example.onthetime.model.Position
import com.example.onthetime.repository.EmployerRepository
import com.google.firebase.auth.FirebaseAuth

class NewsViewModel :ViewModel(){


    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>> get() = _news


    private val _count_of_news = MutableLiveData<Int>().apply { value = 0 }
    val count_of_news: MutableLiveData<Int> get() = _count_of_news


    private val _addPNewsResult = MutableLiveData<Boolean>()
    val addNewsResult: LiveData<Boolean> get() = _addPNewsResult

    val repository = EmployerRepository()
    val employerId = FirebaseAuth.getInstance().currentUser!!.uid




    init {
        fetchNews(employerId)

        _count_of_news.value = _news.value?.size
    }


    fun fetchNews(employerId: String) {
        repository.getNewsWithSnapshot(employerId) { newsList ->
            _news.value = newsList
        }
    }



    fun addNewsToEmployer(employerId: String, news: News) {
        repository.addNewsToEmployer(employerId, news) { success ->
            _addPNewsResult.value = success
        }
    }
}