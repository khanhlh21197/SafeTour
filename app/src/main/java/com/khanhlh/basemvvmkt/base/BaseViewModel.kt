package com.khanhlh.basemvvmkt.base


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khanhlh.basemvvmkt.di.components.DaggerViewModelInjector
import com.khanhlh.basemvvmkt.di.components.ViewModelInjector
import com.khanhlh.basemvvmkt.di.modules.NetworkModule
import com.khanhlh.basemvvmkt.ui.login.LoginActivityViewModel
import com.khanhlh.basemvvmkt.ui.register.RegisterActivityViewModel


abstract class BaseViewModel<T> : ViewModel() {

    var loadingVisibility = MutableLiveData<Int>()

    var errorMessage: MutableLiveData<String> = MutableLiveData()

    var view: T? = null

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    fun attachView(view: T) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is LoginActivityViewModel -> injector.inject(this)
            is RegisterActivityViewModel -> injector.inject(this)
        }
    }

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    open fun updateActionBarTitle(title: String) = _title.postValue(title)
}

