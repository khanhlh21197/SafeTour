package com.khanhlh.basemvvmkt.ui.login


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.khanhlh.basemvvmkt.MyApp
import com.khanhlh.basemvvmkt.R
import com.khanhlh.basemvvmkt.helper.extensions.get
import com.khanhlh.basemvvmkt.helper.extensions.init
import com.khanhlh.basemvvmkt.helper.extensions.set
import com.khanhlh.basemvvmkt.utils.USER_COLLECTION
import com.khanhlh.basemvvmkt.api.FirebaseCommon
import com.khanhlh.basemvvmkt.base.BaseViewModel
import io.reactivex.Single


class LoginActivityViewModel : BaseViewModel<Any?>() {

    val registerButtonClicked = MutableLiveData<Boolean>()
    val isLoginSuccess = MutableLiveData<Boolean>().init(false)
    val mail = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val loginClickListener = View.OnClickListener {
        tryLogin()
    }

    val registerClickListener = View.OnClickListener {
        registerButtonClicked.value = true
    }

    private fun showLoading() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun hideLoading() {
        loadingVisibility.value = View.INVISIBLE
    }

    fun getAllUsers(): Single<QuerySnapshot> =
        FirebaseCommon.getListDocument(USER_COLLECTION)

    @SuppressLint("CheckResult")
    fun tryLogin() {
        showLoading()
        if (password.get()!!.length > 5 && mail.get()!!.length > 5) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                mail.get()!!,
                password.get()!!
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoginSuccess.set(true)
                    Log.d(TAG, "signInWithEmail:success")
                } else {
                    errorMessage.value = task.exception?.localizedMessage
                    isLoginSuccess.set(false)
                }
                hideLoading()
            }
        } else {
            errorMessage.value = MyApp.context.getString(R.string.require_length)
        }
    }
}

