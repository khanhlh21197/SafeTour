package com.khanhlh.basemvvmkt.ui.register

import androidx.lifecycle.ViewModelProvider
import com.khanhlh.basemvvmkt.R
import com.khanhlh.basemvvmkt.base.BaseActivity
import com.khanhlh.basemvvmkt.databinding.ActivityRegisterBinding
import com.khanhlh.basemvvmkt.di.ViewModelFactory

class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterActivityViewModel>() {
    override fun initVariables() {
        bindView(R.layout.activity_register)
        baseViewModel = RegisterActivityViewModel()
        baseViewModel.attachView(this)
        baseViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this)
        ).get(RegisterActivityViewModel::class.java)
        binding.viewModel = baseViewModel
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }

}