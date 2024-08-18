package `in`.bgaurav.passkeys.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}