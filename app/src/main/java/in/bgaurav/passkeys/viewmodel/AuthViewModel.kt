package `in`.bgaurav.passkeys.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.bgaurav.passkeys.model.UserModel
import `in`.bgaurav.passkeys.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {

    sealed class AuthTypeState {
        object Passkey : AuthTypeState()
        object Password : AuthTypeState()
    }

    private val _authTypeState = MutableLiveData<AuthTypeState>()
    val authTypeState: LiveData<AuthTypeState> = _authTypeState

    init {
        _authTypeState.value = AuthTypeState.Passkey
    }

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
}