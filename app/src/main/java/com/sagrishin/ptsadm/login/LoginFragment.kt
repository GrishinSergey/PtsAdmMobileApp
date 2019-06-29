package com.sagrishin.ptsadm.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sagrishin.ptsadm.AuthActivity
import com.sagrishin.ptsadm.R
import com.sagrishin.ptsadm.common.addOnBackPressedCallback
import com.sagrishin.ptsadm.common.livedata.observe
import com.sagrishin.ptsadm.login.LoginFragmentDirections.Companion.actionLoginFragmentToMainActivity
import com.sagrishin.ptsadm.login.viewmodels.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.addOnBackPressedCallback(this) {
            (activity as AuthActivity).close()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authViewModel.tokenUpdateLiveData.observe(this) { newToken ->
            if (newToken != null) {
                (activity as AuthActivity).close()
                (activity as AuthActivity).navigateTo(actionLoginFragmentToMainActivity())
            } else {
                /// notify about error
            }
        }
        onSubmit.setOnClickListener {
            authViewModel.loginUser(UiUser(login = login.text.toString(), password = password.text.toString()))
        }
    }

}
