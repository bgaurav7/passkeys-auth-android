package `in`.bgaurav.passkeys.repository

import `in`.bgaurav.passkeys.model.AuthLoginResponse
import `in`.bgaurav.passkeys.model.UserModel

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthLoginResponse>
}