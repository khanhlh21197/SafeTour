package com.khanhlh.basemvvmkt.di.components

import com.khanhlh.basemvvmkt.di.modules.NetworkModule
import com.khanhlh.basemvvmkt.ui.login.LoginActivityViewModel
import com.khanhlh.basemvvmkt.ui.register.RegisterActivityViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(loginActivity: LoginActivityViewModel)
    fun inject(registerActivityViewModel: RegisterActivityViewModel)


    @Component.Builder
    interface Builder {

        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
    }

}