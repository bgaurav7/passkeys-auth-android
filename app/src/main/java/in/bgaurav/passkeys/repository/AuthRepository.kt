package `in`.bgaurav.passkeys.repository

import `in`.bgaurav.passkeys.model.AuthResponse

interface AuthRepository {

    suspend fun loginPassword(email: String, password: String): AuthResponse

    suspend fun registerPassword(firstName: String, lastName: String, email: String, password: String): AuthResponse

    suspend fun verifyOtp(email: String, otp: String): AuthResponse

}