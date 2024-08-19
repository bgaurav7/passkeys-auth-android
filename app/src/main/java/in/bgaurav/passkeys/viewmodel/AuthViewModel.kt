package `in`.bgaurav.passkeys.viewmodel

import android.util.Log
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialCustomException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.CreateCredentialInterruptedException
import androidx.credentials.exceptions.CreateCredentialProviderConfigurationException
import androidx.credentials.exceptions.CreateCredentialUnknownException
import androidx.credentials.exceptions.publickeycredential.CreatePublicKeyCredentialDomException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import `in`.bgaurav.passkeys.model.UserModel
import `in`.bgaurav.passkeys.network.ApiClient
import `in`.bgaurav.passkeys.repository.AuthRepository
import `in`.bgaurav.passkeys.repository.AuthRepositoryImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val TAG = AuthViewModel::class.java.simpleName

    private val authRepository: AuthRepository = AuthRepositoryImpl(ApiClient.apiService)

    sealed class AuthTypeState {
        data object Passkey : AuthTypeState()
        data object Password : AuthTypeState()
    }

    private val _authTypeState = MutableLiveData<AuthTypeState>()
    val authTypeState: LiveData<AuthTypeState> = _authTypeState

    fun toggleAuthType() {
        _authTypeState.value = when (_authTypeState.value) {
            AuthTypeState.Password -> AuthTypeState.Passkey
            AuthTypeState.Passkey -> AuthTypeState.Password
            else -> AuthTypeState.Passkey
        }
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class PasswordSuccess(val user: UserModel) : LoginState()
        data class PasskeysInit(val user: UserModel, val request: JsonObject) : LoginState()
        data class PasskeysSuccess(val user: UserModel) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    private val loginExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.message?.let {
            Log.d(TAG, "AuthViewModel loginExceptionHandler: $it")
            _loginState.value = LoginState.Error(it)
        }
    }

    fun login(email: String, password: String) {
        when(authTypeState.value) {
            is AuthTypeState.Passkey -> {
                viewModelScope.launch(loginExceptionHandler) {
                    _loginState.value = LoginState.Loading

                    val result = authRepository.loginPasskeys(email)

                    Log.d(TAG, "AuthViewModel Passkeys loginResult: $result")
                    if (result.status) {
                        _loginState.value = LoginState.PasskeysInit(result.data, result.request)
                    } else {
                        _loginState.value = LoginState.Error(result.message)
                    }
                }
            }
            is AuthTypeState.Password -> {
                viewModelScope.launch(loginExceptionHandler) {
                    _loginState.value = LoginState.Loading

                    val result = authRepository.loginPassword(email, password)

                    Log.d(TAG, "AuthViewModel Password loginResult: $result")
                    if (result.status) {
                        _loginState.value = LoginState.PasswordSuccess(result.data)
                    } else {
                        _loginState.value = LoginState.Error(result.message)
                    }
                }
            }
            null -> Log.e(TAG, "AuthViewModel login: AuthTypeState is null")
        }
    }

    fun loginVerifyPasskeys(email: String, responseJson: String) {
        viewModelScope.launch(loginExceptionHandler) {
            _loginState.value = LoginState.Loading

            val result = authRepository.loginVerifyPasskeys(email, responseJson)

            Log.d(TAG, "AuthViewModel Passkeys loginVerifyPasskeys: $result")
            if (result.status) {
                _loginState.value = LoginState.PasskeysSuccess(result.data)
            } else {
                _loginState.value = LoginState.Error(result.message)
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    sealed class RegisterState {
        data object Idle : RegisterState()
        data object Loading : RegisterState()
        data class PasswordSuccess(val user: UserModel) : RegisterState()
        data class PasskeysSuccess(val user: UserModel, val request: JsonObject) : RegisterState()
        data object VerificationSuccess : RegisterState()
        data class Error(val message: String) : RegisterState()
    }

    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> = _registerState

    private val registerExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.message?.let {
                Log.d(TAG, "AuthViewModel registerExceptionHandler: $it")
                _registerState.value = RegisterState.Error(it)
            }
        }

    fun register(firstName: String, lastName: String, email: String, password: String) {
        when(authTypeState.value) {
            is AuthTypeState.Passkey -> {
                viewModelScope.launch(registerExceptionHandler) {
                    _registerState.value = RegisterState.Loading

                    val result = authRepository.registerPasskeys(firstName, lastName, email)

                    Log.i(TAG, "AuthViewModel Passkeys registerResult: $result")
                    if (result.status) {
                        _registerState.value = RegisterState.PasskeysSuccess(result.data, result.request)
                    } else {
                        _registerState.value = RegisterState.Error(result.message)
                    }
                }
            }
            is AuthTypeState.Password -> {
                viewModelScope.launch(registerExceptionHandler) {
                    _registerState.value = RegisterState.Loading

                    val result = authRepository.registerPassword(firstName, lastName, email, password)

                    Log.i(TAG, "AuthViewModel Password registerResult: $result")
                    if (result.status) {
                        _registerState.value = RegisterState.PasswordSuccess(result.data)
                    } else {
                        _registerState.value = RegisterState.Error(result.message)
                    }
                }
            }
            null -> Log.e(TAG, "AuthViewModel register: AuthTypeState is null")
        }
    }

    fun registerVerifyPassword(email: String, otp: String) {
        viewModelScope.launch(registerExceptionHandler) {
            _registerState.value = RegisterState.Loading

            val result = authRepository.registerVerifyPassword(email, otp)

            Log.d(TAG, "AuthViewModel verifyOtp: $result")
            if (result.status) {
                _registerState.value = RegisterState.VerificationSuccess
            } else {
                _registerState.value = RegisterState.Error(result.message)
            }
        }
    }

    fun registerVerifyPasskeys(email: String, otp: String, responseJson: String) {
        viewModelScope.launch(registerExceptionHandler) {
            _registerState.value = RegisterState.Loading

            Log.d(TAG, "AuthViewModel registerVerifyPasskeys: $email $otp $responseJson")

            val result = authRepository.registerVerifyPasskeys(email, otp, responseJson)

            Log.d(TAG, "AuthViewModel registerVerifyPasskeys: $result")
            if (result.status) {
                _registerState.value = RegisterState.VerificationSuccess
            } else {
                _registerState.value = RegisterState.Error(result.message)
            }
        }
    }

    // These are types of errors that can occur during passkey creation.
    fun handlePasskeyFailure(e: CreateCredentialException) {
        val msg = when (e) {
            is CreatePublicKeyCredentialDomException -> {
                // Handle the passkey DOM errors thrown according to the
                // WebAuthn spec using e.domError
                "An error occurred while creating a passkey, please check logs for additional details."
            }

            is CreateCredentialCancellationException -> {
                // The user intentionally canceled the operation and chose not
                // to register the credential.
                "The user intentionally canceled the operation and chose not to register the credential. Check logs for additional details."
            }

            is CreateCredentialInterruptedException -> {
                // Retry-able error. Consider retrying the call.
                "The operation was interrupted, please retry the call. Check logs for additional details."
            }

            is CreateCredentialProviderConfigurationException -> {
                // Your app is missing the provider configuration dependency.
                // Most likely, you're missing "credentials-play-services-auth".
                "Your app is missing the provider configuration dependency. Check logs for additional details."
            }

            is CreateCredentialUnknownException -> {
                "An unknown error occurred while creating passkey. Check logs for additional details."
            }

            is CreateCredentialCustomException -> {
                // You have encountered an error from a 3rd-party SDK. If you
                // make the API call with a request object that's a subclass of
                // CreateCustomCredentialRequest using a 3rd-party SDK, then you
                // should check for any custom exception type constants within
                // that SDK to match with e.type. Otherwise, drop or log the
                // exception.
                "An unknown error occurred from a 3rd party SDK. Check logs for additional details."
            }

            else -> {
                Log.w(TAG, "Unexpected exception type ${e::class.java.name}")
                "An unknown error occurred."
            }
        }
        Log.e(
            TAG, "createPasskey failed with exception: " + e.message.toString() + " Type: " + e.type
        )
        if (e is CreatePublicKeyCredentialDomException) Log.e(TAG, "Dom Error: " + e.domError)
        _registerState.value = RegisterState.Error(msg)
    }

    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }

    init {
        _authTypeState.value = AuthTypeState.Passkey
        _loginState.value = LoginState.Idle
        _registerState.value = RegisterState.Idle
    }
}