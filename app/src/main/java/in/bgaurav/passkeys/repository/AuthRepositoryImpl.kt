package `in`.bgaurav.passkeys.repository

import android.util.Log
import `in`.bgaurav.passkeys.model.AuthRequest
import `in`.bgaurav.passkeys.model.AuthResponse
import `in`.bgaurav.passkeys.network.ApiService

class AuthRepositoryImpl(private val apiService: ApiService) : AuthRepository {

    private val TAG = AuthRepositoryImpl::class.java.simpleName

    override suspend fun loginPassword(
        email: String,
        password: String
    ): AuthResponse {
        val response = apiService.loginPassword(AuthRequest(email = email, password = password))
        Log.d(
            TAG,
            "AuthRepositoryImpl loginPassword: " + "isSuccessful=" + response.isSuccessful + " body=" + response.body() + "" + "code=" + response.code() + " message=" + response.message() + " errorBody=" + response.errorBody()
                .toString() + " raw=" + response.raw()
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Login Error: " + response.message())
        }
    }

    override suspend fun registerPassword(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): AuthResponse {
        val response = apiService.registerPassword(
            AuthRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password
            )
        )
        Log.d(
            TAG,
            "AuthRepositoryImpl registerPassword: " + "isSuccessful=" + response.isSuccessful + " body=" + response.body() + "" + "code=" + response.code() + " message=" + response.message() + " errorBody=" + response.errorBody()
                .toString() + " raw=" + response.raw()
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Login Error: " + response.message())
        }
    }

    override suspend fun registerVerifyPassword(
        email: String,
        otp: String
    ): AuthResponse {
        val response = apiService.registerVerifyPassword(AuthRequest(email = email, otp = otp))
        Log.d(
            TAG,
            "AuthRepositoryImpl registerVerifyPassword: " + "isSuccessful=" + response.isSuccessful + " body=" + response.body() + "" + "code=" + response.code() + " message=" + response.message() + " errorBody=" + response.errorBody()
                .toString() + " raw=" + response.raw()
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Login Error: " + response.message())
        }
    }

    override suspend fun registerPasskeys(
        firstName: String,
        lastName: String,
        email: String
    ): AuthResponse {
        val response = apiService.registerPasskeys(
            AuthRequest(
                firstName = firstName,
                lastName = lastName,
                email = email
            )
        )
        Log.d(
            TAG,
            "AuthRepositoryImpl registerPasskeys: " + "isSuccessful=" + response.isSuccessful + " body=" + response.body() + "" + "code=" + response.code() + " message=" + response.message() + " errorBody=" + response.errorBody()
                .toString() + " raw=" + response.raw()
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Login Error: " + response.message())
        }
    }

    override suspend fun registerVerifyPasskeys(
        email: String,
        otp: String,
        responseJson: String
    ): AuthResponse {
        val response = apiService.registerVerifyPasskeys(AuthRequest(email = email, otp = otp, response = responseJson))
        Log.d(
            TAG,
            "AuthRepositoryImpl registerVerifyPasskeys: " + "isSuccessful=" + response.isSuccessful + " body=" + response.body() + "" + "code=" + response.code() + " message=" + response.message() + " errorBody=" + response.errorBody()
                .toString() + " raw=" + response.raw()
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Login Error: " + response.message())
        }
    }

    override suspend fun loginPasskeys(
        email: String
    ): AuthResponse {
        val response = apiService.loginPasskeys(AuthRequest(email = email))
        Log.d(
            TAG,
            "AuthRepositoryImpl loginPasskeys: " + "isSuccessful=" + response.isSuccessful + " body=" + response.body() + "" + "code=" + response.code() + " message=" + response.message() + " errorBody=" + response.errorBody()
                .toString() + " raw=" + response.raw()
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Login Error: " + response.message())
        }
    }

    override suspend fun loginVerifyPasskeys(
        email: String,
        responseJson: String
    ): AuthResponse {
        val response = apiService.loginVerifyPasskeys(AuthRequest(email = email, response = responseJson))
        Log.d(
            TAG,
            "AuthRepositoryImpl loginVerifyPasskeys: " + "isSuccessful=" + response.isSuccessful + " body=" + response.body() + "" + "code=" + response.code() + " message=" + response.message() + " errorBody=" + response.errorBody()
                .toString() + " raw=" + response.raw()
        )
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Login Error: " + response.message())
        }
    }
}