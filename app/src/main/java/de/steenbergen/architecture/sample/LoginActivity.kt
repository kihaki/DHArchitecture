package de.steenbergen.architecture.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.steenbergen.architecture.R
import de.steenbergen.architecture.sample.ui.login.LoginFragment
import de.steenbergen.architecture.sample.ui.login.SessionFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        if (savedInstanceState == null) {
            goToLogin()
        }
    }

    fun goToLogin() {
        if (
            supportFragmentManager.backStackEntryCount > 0 &&
            supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name == "SessionFragment"
        ) {
            supportFragmentManager.popBackStack()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }

    fun goToSession() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SessionFragment.newInstance())
            .addToBackStack("SessionFragment")
            .commit()
    }

}
