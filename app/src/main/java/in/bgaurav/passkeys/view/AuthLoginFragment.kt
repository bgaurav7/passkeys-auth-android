package `in`.bgaurav.passkeys.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import `in`.bgaurav.passkeys.R
import `in`.bgaurav.passkeys.databinding.FragmentAuthLoginBinding
import `in`.bgaurav.passkeys.utility.DataProvider
import `in`.bgaurav.passkeys.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AuthLoginFragment : Fragment() {

    private val TAG = AuthLoginFragment::class.java.simpleName

    private var _binding: FragmentAuthLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels()

    private lateinit var credentialManager: CredentialManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        credentialManager = CredentialManager.create(requireActivity())

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
            Log.d(TAG, "View loginState $it")
            when (it) {
                is AuthViewModel.LoginState.Idle -> {

                }
                is AuthViewModel.LoginState.Loading -> {

                }
                is AuthViewModel.LoginState.Error -> {
                    showErrorUi(requireView(), it.message)
                }
                is AuthViewModel.LoginState.PasswordSuccess -> {
                    DataProvider.setSignedInThroughPassword()
                    startActivity(Intent(requireContext(), BookActivity::class.java))
                    viewModel.resetLoginState()
                }
                is AuthViewModel.LoginState.PasskeysInit -> {
                    val getCredentialRequest = configureGetCredentialRequest(it.request.toString())
                    val email = it.user.email
                    lifecycleScope.launch {
                        val data = getSavedCredentials(getCredentialRequest)

                        data?.let {
                            if (data.credential is PublicKeyCredential) {
                                val cred = data.credential as PublicKeyCredential
                                Log.d(TAG, "Passkey: ${cred.authenticationResponseJson}")

                                viewModel.loginVerifyPasskeys(email, cred.authenticationResponseJson.toString())
                            }
                            if (data.credential is PasswordCredential) {
                                val cred = data.credential as PasswordCredential
                                Log.d(TAG, "Got Password - User:${cred.id} Password: ${cred.password}")

                                viewModel.toggleAuthType()
                                viewModel.login(cred.id, cred.password)
                            }
                            if (data.credential is CustomCredential) {
                                //If you are also using any external sign-in libraries, parse them here with the
                                // utility functions provided.
                            }
                        }
                    }
                }
                is AuthViewModel.LoginState.PasskeysSuccess -> {
                    DataProvider.setSignedInThroughPasskeys()
                    startActivity(Intent(requireContext(), BookActivity::class.java))
                    viewModel.resetLoginState()
                }
            }
        }
    }

    private fun configureGetCredentialRequest(responseJson: String): GetCredentialRequest {
        val getPublicKeyCredentialOption = GetPublicKeyCredentialOption(responseJson, null)
        val getPasswordOption = GetPasswordOption()
        val getCredentialRequest = GetCredentialRequest(
            listOf(
                getPublicKeyCredentialOption, getPasswordOption
            )
        )
        return getCredentialRequest
    }

    private suspend fun getSavedCredentials(getCredentialRequest: GetCredentialRequest): GetCredentialResponse? {
        val result = try {
            credentialManager.getCredential(
                requireActivity(),
                getCredentialRequest,
            )
        } catch (e: Exception) {
            Log.e(TAG, "getCredential failed with exception: " + e.message.toString())
//            activity?.showErrorAlert(
//                "An error occurred while authenticating through saved credentials. Check logs for additional details"
//            )
            return null
        }

        return result
    }

    private fun showErrorUi(view: View, message: String) {
        val sBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        sBar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.gray))
        sBar.setTextColor(ContextCompat.getColor(view.context, R.color.white))
        sBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}