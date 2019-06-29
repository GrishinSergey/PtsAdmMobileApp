package com.sagrishin.ptsadm

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.sagrishin.ptsadm.common.BaseNavHostActivity

class AuthActivity : BaseNavHostActivity() {

    private val navController: NavController by lazy {
        Navigation.findNavController(this, R.id.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
    }

    override fun getController(): NavController {
        return navController
    }

}
