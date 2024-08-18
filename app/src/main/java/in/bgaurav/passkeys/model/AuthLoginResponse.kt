package `in`.bgaurav.passkeys.model

data class AuthLoginResponse (
    val status: Long,
    val message: String,
    val data: UserModel
)
