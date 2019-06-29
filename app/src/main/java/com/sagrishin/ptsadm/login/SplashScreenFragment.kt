package com.sagrishin.ptsadm.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import com.sagrishin.ptsadm.AuthActivity
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.common.isOnline
import com.sagrishin.ptsadm.common.livedata.observe
import com.sagrishin.ptsadm.common.uikit.alertdialog.alert
import com.sagrishin.ptsadm.login.SplashScreenFragmentDirections.Companion.actionSplashScreenFragmentToLoginFragment
import com.sagrishin.ptsadm.login.SplashScreenFragmentDirections.Companion.actionSplashScreenFragmentToMainActivity
import com.sagrishin.ptsadm.login.viewmodels.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        authViewModel.tokenUpdateLiveData.observe(this) { token ->
            if (token == null) {
                (activity as AuthActivity).navigateTo(actionSplashScreenFragmentToLoginFragment())
            } else {
                (activity as AuthActivity).close()
                (activity as AuthActivity).navigateTo(actionSplashScreenFragmentToMainActivity())
            }
        }

        callUpdateToken()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TranslateAnimation(0F, 0F, 0F, 400F).apply {
            startOffset = 100
            duration = 1500
            fillAfter = true
            interpolator = BounceInterpolator()
            repeatCount = Animation.INFINITE

            view.imageView.startAnimation(this)
        }
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
