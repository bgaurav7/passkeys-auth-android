package `in`.bgaurav.passkeys.network

import `in`.bgaurav.passkeys.model.AuthRequest
import `in`.bgaurav.passkeys.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/login-password")
    suspend fun loginPassword(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/auth/register-password")
    suspend fun registerPassword(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/auth/register-verify-password")
    suspend fun registerVerifyPassword(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/auth/login-passkeys")
    suspend fun loginPasskeys(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/auth/login-verify-passkeys")
    suspend fun loginVerifyPasskeys(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/auth/register-passkeys")
    suspend fun registerPasskeys(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/auth/register-verify-passkeys")
    suspend fun registerVerifyPasskeys(@Body authRequest: AuthRequest): Response<AuthResponse>

}