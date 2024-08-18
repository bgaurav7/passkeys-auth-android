package `in`.bgaurav.passkeys.network

import `in`.bgaurav.passkeys.model.AuthLoginResponse
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

    @POST("api/auth/login")
    fun login(@Body email: String, @Body password: String): Response<AuthLoginResponse>
}