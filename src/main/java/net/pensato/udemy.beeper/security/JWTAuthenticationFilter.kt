package net.pensato.udemy.beeper.security

import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import net.pensato.udemy.beeper.domain.Usuario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.http.HttpServletRequest

open class JWTAuthenticationFilter @Autowired constructor(
        val authentication: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
            req: HttpServletRequest, res: HttpServletResponse): Authentication {
        try {
            val creds = ObjectMapper()
                    .readValue(req.getInputStream(), Usuario::class.java)

            return authentication.authenticate(
                    UsernamePasswordAuthenticationToken(
                            creds.username,
                            creds.password,
                            ArrayList<GrantedAuthority>())
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(req: HttpServletRequest,
                                           res: HttpServletResponse,
                                           chain: FilterChain,
                                           auth: Authentication) {

        val token = Jwts.builder()
                .setSubject((auth.getPrincipal() as User).getUsername())
                .setExpiration(Date(System.currentTimeMillis() + SecurityCenter.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityCenter.SECRET.toByteArray())
                .compact()

        res.addHeader(SecurityCenter.HEADER_STRING, SecurityCenter.TOKEN_PREFIX + token)
    }
}