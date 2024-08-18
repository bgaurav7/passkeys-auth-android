package `in`.bgaurav.passkeys.model

data class UserModel (
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val token: String
)
