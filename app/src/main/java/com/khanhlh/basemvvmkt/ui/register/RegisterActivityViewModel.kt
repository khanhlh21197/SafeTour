package com.khanhlh.basemvvmkt.ui.register

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.khanhlh.basemvvmkt.MyApp
import com.khanhlh.basemvvmkt.R
import com.khanhlh.basemvvmkt.helper.extensions.get
import com.khanhlh.basemvvmkt.helper.extensions.init
import com.khanhlh.basemvvmkt.helper.extensions.set
import com.khanhlh.basemvvmkt.utils.EMAIL
import com.khanhlh.basemvvmkt.utils.ID
import com.khanhlh.basemvvmkt.utils.USER_COLLECTION
import com.khanhlh.basemvvmkt.api.FirebaseCommon
import com.khanhlh.basemvvmkt.base.BaseViewModel
import io.reactivex.Observable

class RegisterActivityViewModel : BaseViewModel<Any?>() {
    var mail = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var rePassword = MutableLiveData<String>()
    val onBackPress = MutableLiveData<Boolean>().init(false)
    val isRegisterSuccess = MutableLiveData<Boolean>().init(false)

    val registerClickListener = View.OnClickListener {
        tryRegister()
    }

    @SuppressLint("CheckResult")
    fun tryRegister() {
        if (password.get()!!.length > 6 && mail.get()!!.length > 4) {
            if (password.get() == rePassword.get()) {
                showLoading()
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(mail.get()!!, password.get()!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            insertUserToFireStore().subscribe {
                                isRegisterSuccess.set(true)
                                errorMessage.value = MyApp.context.getString(R.string.login_success)
                            }
                        } else {
                            isRegisterSuccess.set(false)
                            errorMessage.value = task.exception?.localizedMessage
                        }
                        hideLoading()
                    }
            } else {
                errorMessage.value = MyApp.context.getString(R.string.require_length)
            }
        } else {
            errorMessage.value = MyApp.context.getString(R.string.require_length)
        }
    }

    private fun showLoading() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun hideLoading() {
        loadingVisibility.value = View.INVISIBLE
    }

    @SuppressLint("CheckResult")
    fun insertUserToFireStore(): Observable<DocumentReference> {
        val currentUser = FirebaseCommon.currentUser!!
        val email = currentUser.email
        val id = currentUser.uid
        val user: HashMap<String, String> = HashMap()
        user[ID] = id
        user[EMAIL] = email!!
        return FirebaseCommon.push(USER_COLLECTION, id, user)
    }
}

