package `in`.bgaurav.passkeys.repository

import `in`.bgaurav.passkeys.model.AuthLoginResponse
import `in`.bgaurav.passkeys.model.UserModel
import `in`.bgaurav.passkeys.network.ApiService

class AuthRepositoryImpl(private val apiService: ApiService) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<AuthLoginResponse> {
        val response = apiService.login(email, password)

        if (response.isSuccessful) {
            Result.success(response.body())
        } else {
            Result.failure(Throwable("Login Error: " +  response.message()))
        }
        return Result.failure(Throwable("Login failed"))
    }
}