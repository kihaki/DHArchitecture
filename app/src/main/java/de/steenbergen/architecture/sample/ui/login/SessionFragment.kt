package de.steenbergen.architecture.sample.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import de.steenbergen.architecture.R
import de.steenbergen.architecture.sample.LoginActivity
import de.steenbergen.architecture.sample.ui.login.SessionViewState.LoadingSession
import de.steenbergen.architecture.sample.ui.login.SessionViewState.Session
import kotlinx.android.synthetic.main.session_fragment.*

class SessionFragment : Fragment(R.layout.session_fragment) {

    companion object {
        fun newInstance() = SessionFragment()
    }

    private val viewModel: SessionViewModel by lazy {
        ViewModelProvider(this).get(SessionViewModel::class.java).apply {
            closeView = { (activity as? LoginActivity)?.goToLogin() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is LoadingSession -> {
                    sessionView_headLine.visibility = View.GONE
                    sessionView_tokenText.visibility = View.GONE
                    sessionView_logoutButton.visibility = View.GONE
                    sessionView_loadingProgress.visibility = View.VISIBLE
                }
                is Session -> {
                    sessionView_headLine.visibility = View.VISIBLE
                    sessionView_tokenText.visibility = View.VISIBLE
                    sessionView_logoutButton.visibility = View.VISIBLE
                    sessionView_loadingProgress.visibility = View.GONE

                    sessionView_headLine.text = "Welcome!"
                    sessionView_tokenText.text = "Your token is ${state.token}"
                }
            }
        })

        sessionView_logoutButton.setOnClickListener { viewModel.logout() }
    }

}
