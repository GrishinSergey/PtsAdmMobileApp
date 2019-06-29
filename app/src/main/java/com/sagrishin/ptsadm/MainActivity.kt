package com.sagrishin.ptsadm

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.sagrishin.ptsadm.common.BaseNavHostActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseNavHostActivity() {

    private val navController: NavController by lazy {
        Navigation.findNavController(this, R.id.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setupWithNavController(navController)

        askForPermissions(Permission.READ_CONTACTS) {
            if (it.isAllDenied(Permission.READ_CONTACTS)) {
                /// denied, so need to ask again or close
            } else {
                /// all granted
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun getController(): NavController {
        return navController
    }

}
