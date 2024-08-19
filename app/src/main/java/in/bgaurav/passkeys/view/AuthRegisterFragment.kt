package `in`.bgaurav.passkeys.view

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialException
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import `in`.bgaurav.passkeys.R
import `in`.bgaurav.passkeys.databinding.FragmentAuthRegisterBinding
import `in`.bgaurav.passkeys.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AuthRegisterFragment : Fragment() {

    private val TAG = AuthRegisterFragment::class.java.simpleName

    private var _binding: FragmentAuthRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels()

    private lateinit var credentialManager: CredentialManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        credentialManager = CredentialManager.create(requireActivity())

        binding.alternateLogin.setOnClickListener {
            viewModel.toggleAuthType()
        }

        viewModel.authTypeState.observe(viewLifecycleOwner) {
            when (it) {
                is AuthViewModel.AuthTypeState.Passkey -> {
                    binding.passwordLayout.visibility = View.GONE
                    binding.alternateLogin.text = getString(R.string.register_with_password)
                }
                is AuthViewModel.AuthTypeState.Password -> {
                    binding.passwordLayout.visibility = View.VISIBLE
                    binding.alternateLogin.text = getString(R.string.register_with_passkeys)
                }
            }
        }

        binding.submitButton.setOnClickListener {
            viewModel.resetRegisterState()
            viewModel.register(binding.firstNameEditText.text.toString(), binding.lastNameEditText.text.toString(), binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
        }

        viewModel.registerState.observe(viewLifecycleOwner) {
            Log.d(TAG, "View registerState $it")
            when (it) {
                is AuthViewModel.RegisterState.Idle -> {

                }
                is AuthViewModel.RegisterState.Loading -> {

                }
                is AuthViewModel.RegisterState.Error -> {
                    showErrorUi(requireView(), it.message)
                }
                is AuthViewModel.RegisterState.PasswordSuccess -> {
                    showOtpDialog(requireContext(), onTextEntered = { otp ->
                        viewModel.registerVerifyPassword(binding.emailEditText.text.toString(), otp)
                    })
                }
                is AuthViewModel.RegisterState.PasskeysSuccess -> {
                    val email = it.user.email
                    val request = it.request.toString()
                    showOtpDialog(requireContext(), onTextEntered = { otp ->
                        lifecycleScope.launch {
                            val response = createPasskey(request)
                            Log.e(TAG, "GB: " + response?.registrationResponseJson)
                            if(response != null)
                                viewModel.registerVerifyPasskeys(email, otp, response.registrationResponseJson)
                        }
                    })
                }
                is AuthViewModel.RegisterState.VerificationSuccess -> {
                    findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                    viewModel.resetRegisterState()
                }
            }
        }
    }

    private suspend fun createPasskey(registerReq: String): CreatePublicKeyCredentialResponse? {
        val request = CreatePublicKeyCredentialRequest(registerReq)
        var response: CreatePublicKeyCredentialResponse? = null
        try {
            response = credentialManager.createCredential(
                requireActivity(), request
            ) as CreatePublicKeyCredentialResponse
        } catch (e: CreateCredentialException) {
            viewModel.handlePasskeyFailure(e)
        }
        return response
    }

    private fun showOtpDialog(context: Context, onTextEntered: (String) -> Unit) {
        val builder = AlertDialog.Builder(context)
            .setTitle("Email Verification")
            .setMessage("Please enter one time password received on your email.")

        val editText = EditText(context)
        editText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setView(editText)
            .setPositiveButton("OK") { _, _ ->
                onTextEntered(editText.text.toString())
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                viewModel.resetRegisterState()
            }
            .show()
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