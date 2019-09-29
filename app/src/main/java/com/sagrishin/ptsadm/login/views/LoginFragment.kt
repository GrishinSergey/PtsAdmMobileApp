package com.sagrishin.ptsadm.login.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sagrishin.ptsadm.AuthActivity
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.common.addOnBackPressedCallback
import com.sagrishin.ptsadm.common.livedata.observe
import com.sagrishin.ptsadm.common.uikit.snackbar.snackbar
import com.sagrishin.ptsadm.login.UiUser
import com.sagrishin.ptsadm.login.viewmodels.AuthViewModel
import com.sagrishin.ptsadm.login.views.LoginFragmentDirections.Companion.actionLoginFragmentToMainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.addOnBackPressedCallback(this) {
            (activity as AuthActivity).close()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        authViewModel.tokenUpdateLiveData.observe(viewLifecycleOwner) {
            (activity as AuthActivity).close()
            (activity as AuthActivity).navigateTo(actionLoginFragmentToMainActivity())
        }

        authViewModel.errorLiveData.observe(viewLifecycleOwner) {
            showError(it.message!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onSubmit.setOnClickListener {
            if (login.text?.isBlank() == true) {
                showError(getString(R.string.error_login_required))
            } else if (password.text?.isBlank() == true) {
                showError(getString(R.string.error_password_required))
            } else {
                authViewModel.loginUser(UiUser(login.text.toString(), password.text.toString()))
            }
        }
    }


    private fun showError(errorMessage: String) {
        snackbar {
            view = this@LoginFragment.view!!
            message = errorMessage
            duration = Snackbar.LENGTH_LONG
        }
    }

}
