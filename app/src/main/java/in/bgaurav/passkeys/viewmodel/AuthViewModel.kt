package `in`.bgaurav.passkeys.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.bgaurav.passkeys.model.UserModel
import `in`.bgaurav.passkeys.network.ApiClient
import `in`.bgaurav.passkeys.repository.AuthRepository
import `in`.bgaurav.passkeys.repository.AuthRepositoryImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {

    private val TAG = AuthViewModel::class.java.simpleName

    private val authRepository: AuthRepository = AuthRepositoryImpl(ApiClient.apiService)

    sealed class AuthTypeState {
        object Passkey : AuthTypeState()
        object Password : AuthTypeState()
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
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val user: UserModel) : LoginState()
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
        viewModelScope.launch(loginExceptionHandler) {
            _loginState.value = LoginState.Loading

            val result = authRepository.loginPassword(email, password)

            Log.d(TAG, "AuthViewModel loginResult: $result")
            if(result.status) {
                _loginState.value = LoginState.Success(result.data)
            } else {
                _loginState.value = LoginState.Error(result.message)
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        data class RegisterSuccess(val user: UserModel) : RegisterState()
        object VerificationSuccess : RegisterState()
        data class Error(val message: String) : RegisterState()
    }

    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> = _registerState

    private val registerExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.message?.let {
            Log.d(TAG, "AuthViewModel registerExceptionHandler: $it")
            _registerState.value = RegisterState.Error(it)
        }
    }

    fun register(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch(registerExceptionHandler) {
            _registerState.value = RegisterState.Loading

            val result = authRepository.registerPassword(firstName, lastName, email, password)

            Log.d(TAG, "AuthViewModel registerResult: $result")
            if(result.status) {
                _registerState.value = RegisterState.RegisterSuccess(result.data)
            } else {
                _registerState.value = RegisterState.Error(result.message)
            }
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch(registerExceptionHandler) {
            _registerState.value = RegisterState.Loading

            val result = authRepository.verifyOtp(email, otp)

            Log.d(TAG, "AuthViewModel verifyOtp: $result")
            if(result.status) {
                _registerState.value = RegisterState.VerificationSuccess
            } else {
                _registerState.value = RegisterState.Error(result.message)
            }
        }
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