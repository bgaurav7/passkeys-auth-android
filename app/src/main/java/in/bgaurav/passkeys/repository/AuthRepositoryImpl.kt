package `in`.bgaurav.passkeys.repository

import android.util.Log
import `in`.bgaurav.passkeys.model.AuthRequest
import `in`.bgaurav.passkeys.model.AuthResponse
import `in`.bgaurav.passkeys.network.ApiService

class AuthRepositoryImpl(private val apiService: ApiService) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResponse {
        val response = apiService.login(AuthRequest(email = email, password = password))
        Log.e("GB",
            "AuthRepositoryImpl login: " + "isSuccessful=" + response.isSuccessful + " body=" + response.body() + "" + "code=" + response.code() + " message=" + response.message() + " errorBody=" + response.errorBody()
                .toString() + " raw=" + response.raw()
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Login Error: " + response.message())
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): AuthResponse {
        val response = apiService.register(AuthRequest(firstName = firstName, lastName = lastName, email = email, password = password))
        Log.e("GB",
            "AuthRepositoryImpl register: " + "isSuccessful=" + response.isSuccessful + " body=" + response.body() + "" + "code=" + response.code() + " message=" + response.message() + " errorBody=" + response.errorBody()
                .toString() + " raw=" + response.raw()
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Login Error: " + response.message())
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): AuthResponse {
        val response = apiService.verifyOtp(AuthRequest(email = email, otp = otp))
        Log.e("GB",
            "AuthRepositoryImpl verifyOtp: " + "isSuccessful=" + response.isSuccessful + " body=" + response.body() + "" + "code=" + response.code() + " message=" + response.message() + " errorBody=" + response.errorBody()
                .toString() + " raw=" + response.raw()
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Login Error: " + response.message())
        }
    }

}