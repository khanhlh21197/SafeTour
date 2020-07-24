package com.khanhlh.basemvvmkt.ui.profile

import androidx.lifecycle.MutableLiveData
import com.khanhlh.basemvvmkt.api.FirebaseCommon
import com.khanhlh.basemvvmkt.base.BaseViewModel
import com.khanhlh.basemvvmkt.helper.extensions.set
import com.khanhlh.basemvvmkt.model.User
import com.khanhlh.basemvvmkt.utils.*
import io.reactivex.disposables.Disposable

class ProfileViewModel : BaseViewModel<Any>() {
    var user = MutableLiveData<User>()

    fun getProfile(): Disposable? = FirebaseCommon.getProfile()
        .subscribe {
            var id: String = ""
            var email: String = ""
            var name: String = ""
            var phoneNumber: String = ""
            var devices: String = ""
            if (it[ID] != null) id = it[ID].toString()
            if (it[EMAIL] != null) email = it[EMAIL].toString()
            if (it[NAME] != null) name = it[NAME].toString()
            if (it[PHONE_NUMBER] != null) phoneNumber = it[PHONE_NUMBER].toString()
            if (it[DEVICES] != null) devices = it[DEVICES].toString()
            user.set(
                User(id, email, name, phoneNumber, devices)
            )
        }

    fun logOut() {}
}