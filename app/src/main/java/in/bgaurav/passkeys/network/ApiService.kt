package `in`.bgaurav.passkeys.network

import `in`.bgaurav.passkeys.model.AuthRequest
import `in`.bgaurav.passkeys.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
//    @POST("register/start")
//    fun registerStart(@Body authModel: AuthModel): Call<JsonObject>
//
//    @POST("register/finish")
//    fun registerFinish(@Body authModel: AuthModel): Call<JsonObject>
//
//    @POST("login/start")
//    fun loginStart(@Body authModel: AuthModel): Call<JsonObject>
//
//    @POST("login/finish")
//    fun loginFinish(@Body authModel: AuthModel): Call<JsonObject>

    @POST("api/auth/login-password")
    suspend fun loginPassword(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/auth/register-password")
    suspend fun registerPassword(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body authRequest: AuthRequest): Response<AuthResponse>
    
}