package `in`.bgaurav.passkeys.model

data class AuthRequest (
    val firstName: String = "",
    val lastName: String = "",
    val email: String,
    val password: String = "",
    val otp: String = "",
)
