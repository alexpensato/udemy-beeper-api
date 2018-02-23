package net.pensato.udemy.beeper.security

import io.jsonwebtoken.Jwts
import javax.servlet.http.HttpServletRequest

object SecurityCenter {
    val SECRET = "SecretKeyToGenJWTs"
    val EXPIRATION_TIME: Long = 864000000 // 10 days
    val TOKEN_PREFIX = "Bearer "
    val HEADER_STRING = "Authorization"
    val REGISTER_URL = "/auth/register"
    val LOGIN_URL = "/auth/login"

    fun getUsernameFromToken(request: HttpServletRequest): String {
        val token = request.getHeader(HEADER_STRING)
        if (token != null) {
            // parse the token.
            val username: String? = Jwts.parser()
                    .setSigningKey(SECRET.toByteArray())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject()

            return username ?: ""
        }
        return ""
    }
}