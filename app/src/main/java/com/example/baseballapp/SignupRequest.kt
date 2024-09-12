data class SignupRequest(
    val username: String,
    val password: String,
    val name: String,
    val phoneNumber: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)
