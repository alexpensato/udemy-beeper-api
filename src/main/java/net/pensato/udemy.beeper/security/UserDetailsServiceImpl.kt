package net.pensato.udemy.beeper.security

import net.pensato.udemy.beeper.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl @Autowired constructor(
        val usuarioRepository: UsuarioRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val applicationUser = usuarioRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        return User(applicationUser.username, applicationUser.password, listOf())
    }
}