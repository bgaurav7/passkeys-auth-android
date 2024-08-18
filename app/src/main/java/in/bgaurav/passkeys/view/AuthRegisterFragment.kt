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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import `in`.bgaurav.passkeys.R
import `in`.bgaurav.passkeys.databinding.FragmentAuthRegisterBinding
import `in`.bgaurav.passkeys.viewmodel.AuthViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AuthRegisterFragment : Fragment() {

    private var _binding: FragmentAuthRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            Log.d("GB", it.toString())
            when (it) {
                is AuthViewModel.RegisterState.Idle -> {

                }
                is AuthViewModel.RegisterState.Loading -> {

                }
                is AuthViewModel.RegisterState.Error -> {
                    showErrorSnackbar(requireView(), it.message)
                }
                is AuthViewModel.RegisterState.RegisterSuccess -> {
                    showEditTextDialog(requireContext(), onTextEntered = {
                        viewModel.verifyOtp(binding.emailEditText.text.toString(), it)
                    })
                }
                is AuthViewModel.RegisterState.VerificationSuccess -> {
                    findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                    viewModel.resetRegisterState()
                }
            }
        }
    }

    fun showEditTextDialog(context: Context, onTextEntered: (String) -> Unit) {
        val builder = AlertDialog.Builder(context)
            .setTitle("OTP Verification")
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