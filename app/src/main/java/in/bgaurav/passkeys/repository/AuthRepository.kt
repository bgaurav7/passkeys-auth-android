package `in`.bgaurav.passkeys.repository

import `in`.bgaurav.passkeys.model.AuthResponse

interface AuthRepository {

    suspend fun loginPassword(email: String, password: String): AuthResponse

    suspend fun registerPassword(firstName: String, lastName: String, email: String, password: String): AuthResponse

    suspend fun registerVerifyPassword(email: String, otp: String): AuthResponse

    suspend fun loginPasskeys(email: String): AuthResponse

    suspend fun loginVerifyPasskeys(email: String, responseJson: String): AuthResponse

    suspend fun registerPasskeys(firstName: String, lastName: String, email: String): AuthResponse

    suspend fun registerVerifyPasskeys(email: String, otp: String, responseJson: String): AuthResponse

}