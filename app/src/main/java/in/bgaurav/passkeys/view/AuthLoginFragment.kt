package `in`.bgaurav.passkeys.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import `in`.bgaurav.passkeys.R
import `in`.bgaurav.passkeys.databinding.FragmentAuthLoginBinding
import `in`.bgaurav.passkeys.utility.DataProvider
import `in`.bgaurav.passkeys.viewmodel.AuthViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AuthLoginFragment : Fragment() {

    private var _binding: FragmentAuthLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.alternateLogin.setOnClickListener {
            viewModel.toggleAuthType()
        }

        viewModel.authTypeState.observe(viewLifecycleOwner) {
            when (it) {
                is AuthViewModel.AuthTypeState.Passkey -> {
                    binding.passwordLayout.visibility = View.GONE
                    binding.alternateLogin.text = getString(R.string.login_with_password)
                }
                is AuthViewModel.AuthTypeState.Password -> {
                    binding.passwordLayout.visibility = View.VISIBLE
                    binding.alternateLogin.text = getString(R.string.login_with_passkeys)
                }
            }
        }

        binding.submitButton.setOnClickListener {
            viewModel.login(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
        }

        viewModel.loginState.observe(viewLifecycleOwner) {
            Log.d("GB", it.toString())
            when (it) {
                is AuthViewModel.LoginState.Idle -> {

                }
                is AuthViewModel.LoginState.Loading -> {

                }
                is AuthViewModel.LoginState.Error -> {
                    showErrorSnackbar(requireView(), it.message)
                }
                is AuthViewModel.LoginState.Success -> {
                    DataProvider.setSignedInThroughPassword()
                    startActivity(Intent(requireContext(), BookActivity::class.java))
                    viewModel.resetLoginState()
                }
            }
        }
    }

    private fun showErrorSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.gray))
        snackbar.setTextColor(ContextCompat.getColor(view.context, R.color.white))
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}