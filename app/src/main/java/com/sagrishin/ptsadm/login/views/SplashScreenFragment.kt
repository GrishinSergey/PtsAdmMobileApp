package com.sagrishin.ptsadm.login.views

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import com.sagrishin.ptsadm.AuthActivity
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.common.animateBounceVertical
import com.sagrishin.ptsadm.common.isOnline
import com.sagrishin.ptsadm.common.livedata.observe
import com.sagrishin.ptsadm.common.uikit.alertdialog.alert
import com.sagrishin.ptsadm.login.viewmodels.AuthViewModel
import com.sagrishin.ptsadm.login.views.SplashScreenFragmentDirections.Companion.actionSplashScreenFragmentToLoginFragment
import com.sagrishin.ptsadm.login.views.SplashScreenFragmentDirections.Companion.actionSplashScreenFragmentToMainActivity
import kotlinx.android.synthetic.main.fragment_splash_screen.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        authViewModel.tokenUpdateLiveData.observe(viewLifecycleOwner) {
            (activity as AuthActivity).close()
            (activity as AuthActivity).navigateTo(actionSplashScreenFragmentToMainActivity())
        }
        authViewModel.errorLiveData.observe(viewLifecycleOwner) {
            (activity as AuthActivity).navigateTo(actionSplashScreenFragmentToLoginFragment())
        }

        callUpdateToken()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageView.animateBounceVertical(1500, 0F, 400F, 100, true, Animation.INFINITE)
    }

    private fun callUpdateToken() {
        if (isOnline()) {
            authViewModel.updateToken()
        } else {
            alert {
                title = getString(R.string.offline_dialog_title)
                message = getString(R.string.check_internet_dialog_message)
                positiveButtonId = R.string.retry to { d ->
                    d.dismiss()
                    callUpdateToken()
                }
            }
        }
    }

}
