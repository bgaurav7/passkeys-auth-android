package `in`.bgaurav.passkeys.model

data class AuthRequest (
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String,
    val password: String? = null,
    val otp: String? = null,
    val response: String? = null,
)
