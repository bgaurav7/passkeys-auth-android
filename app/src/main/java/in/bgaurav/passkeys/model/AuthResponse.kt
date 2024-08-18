package `in`.bgaurav.passkeys.model

import com.google.gson.JsonArray

data class AuthResponse (
    val status: Boolean,
    val message: String,
    val data: UserModel,
    val error: JsonArray
)