package com.khanhlh.basemvvmkt.ui.profile


import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.google.firebase.auth.FirebaseAuth
import com.khanhlh.basemvvmkt.R
import com.khanhlh.basemvvmkt.base.BaseFragment
import com.khanhlh.basemvvmkt.databinding.FragmentProfileBinding
import com.khanhlh.basemvvmkt.helper.dialog.alert
import com.khanhlh.basemvvmkt.helper.extensions.navigateToActivity
import com.khanhlh.basemvvmkt.helper.shared_preference.clear
import com.khanhlh.basemvvmkt.ui.login.LoginActivity
import com.khanhlh.basemvvmkt.utils.USER_PREF
import com.khanhlh.substationmonitor.ui.main.MainActivity
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    lateinit var sharedPref: SharedPreferences

    override fun initView() {
        vm = ProfileViewModel()
        mBinding.vm = vm
        vm.getProfile()
        vm.user.observe(this) { mBinding.user = it }
        includeLayoutListener()
        sharedPref = requireActivity().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
    }

    private fun includeLayoutListener() {
        actionBar.logout.setOnClickListener {
            alert(
                this,
                R.string.log_out,
                R.string.app_name,
                R.string.cancel,
                R.string.ok,
                null,
                View.OnClickListener {
                    sharedPref.clear(requireActivity(), USER_PREF)
                    FirebaseAuth.getInstance().signOut()
                    requireActivity().finish()
                    requireActivity().navigateToActivity(LoginActivity::class.java)
                })!!.show()
        }
        actionBar.back.setOnClickListener { MainActivity.instance.onBackPressed() }
    }

    override fun getLayoutId(): Int = R.layout.fragment_profile

}
