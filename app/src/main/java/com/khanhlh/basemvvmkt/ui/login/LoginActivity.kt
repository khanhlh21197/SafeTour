package com.khanhlh.basemvvmkt.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.khanhlh.basemvvmkt.MainActivity
import com.khanhlh.basemvvmkt.R
import com.khanhlh.basemvvmkt.base.BaseActivity
import com.khanhlh.basemvvmkt.databinding.ActivityLoginBinding
import com.khanhlh.basemvvmkt.helper.extensions.get
import com.khanhlh.basemvvmkt.helper.extensions.navigateToActivity
import com.khanhlh.basemvvmkt.helper.extensions.set
import com.khanhlh.basemvvmkt.helper.shared_preference.clear
import com.khanhlh.basemvvmkt.helper.shared_preference.get
import com.khanhlh.basemvvmkt.helper.shared_preference.put
import com.khanhlh.basemvvmkt.ui.register.RegisterActivity
import com.khanhlh.basemvvmkt.utils.USER_PREF
import com.khanhlh.basemvvmkt.di.ViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginActivityViewModel>() {
    private lateinit var vm: LoginActivityViewModel
    lateinit var sharedPref: SharedPreferences

    override fun initVariables() {
        baseViewModel = LoginActivityViewModel()
        baseViewModel.apply { }
        baseViewModel.attachView(this)
        baseViewModel = ViewModelProvider(this, ViewModelFactory(this))
            .get(LoginActivityViewModel::class.java)
        vm = baseViewModel
        bindView(R.layout.activity_login)
        binding.viewModel = vm
        sharedPref = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        onSwitchListener()
        getUser()
    }

    private fun onSwitchListener() {
        switchSaveUser.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.put(SWITCH_STATE, isChecked)
            if (isChecked) {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )
                }
            } else {
                sharedPref.clear(this, USER_PREF)
            }
        }
    }

    private fun getUser() {
        vm.mail.set(sharedPref.get(USER_EMAIL, DEFAULT_EMAIL))
        vm.password.set(sharedPref.get(USER_PASSWORD, DEFAULT_PASSWORD))
        switchSaveUser.isChecked = sharedPref.get(SWITCH_STATE, false)
    }

    private fun saveUser() {
        sharedPref.put(USER_EMAIL, vm.mail.get())
        sharedPref.put(USER_PASSWORD, vm.password.get())
    }

    override fun observeViewModel() {
        super.observeViewModel()
        vm.registerButtonClicked.observe(this, Observer {
            navigateToActivity(RegisterActivity::class.java)
        })

        vm.isLoginSuccess.observe(this, Observer {
            if (it) {
                navigateToActivity(MainActivity::class.java)
                if (switchSaveUser.isChecked) saveUser()
            }
        })
    }

    companion object {
        private const val USER_EMAIL = "USER_EMAIL";
        private const val USER_PASSWORD = "USER_PASSWORD";
        private const val SWITCH_STATE = "SWITCH_STATE";
        private const val DEFAULT_EMAIL = "";
        private const val DEFAULT_PASSWORD = "";
    }

}




