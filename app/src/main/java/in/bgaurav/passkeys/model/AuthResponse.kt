package `in`.bgaurav.passkeys.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class AuthResponse (
    val status: Boolean,
    val message: String,
    val data: UserModel,
    val error: JsonArray,
    val request: JsonObject,
)