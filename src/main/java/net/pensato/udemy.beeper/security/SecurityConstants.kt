package net.pensato.udemy.beeper.security

object SecurityConstants {
    val SECRET = "SecretKeyToGenJWTs"
    val EXPIRATION_TIME: Long = 864000000 // 10 days
    val TOKEN_PREFIX = "Bearer "
    val HEADER_STRING = "Authorization"
    val REGISTER_URL = "/auth/register"
}