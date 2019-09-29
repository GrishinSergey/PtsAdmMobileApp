package com.sagrishin.ptsadm

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.sagrishin.ptsadm.common.BaseNavHostActivity
import com.sagrishin.ptsadm.common.SHOW_LOGIN_PARAM_KEY
import com.sagrishin.ptsadm.common.uikit.snackbar.snackbar
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthActivity : BaseNavHostActivity(R.layout.activity_authentication) {

    private val navController: NavController by lazy {
        Navigation.findNavController(this, R.id.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.getBooleanExtra(SHOW_LOGIN_PARAM_KEY, false)) {
            snackbar {
                view = root
                messageId = R.string.error_token_expired
                duration = Snackbar.LENGTH_LONG
            }
            navigateTo(R.id.loginFragment)
        }
    }

    override fun getController(): NavController {
        return navController
    }

}
