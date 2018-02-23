/*
 * Copyright 2017 twitter.com/PensatoAlex
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.pensato.udemy.beeper.controller

import net.pensato.udemy.beeper.domain.Usuario
import net.pensato.udemy.beeper.domain.Beep
import net.pensato.udemy.beeper.repository.UsuarioRepository
import net.pensato.udemy.beeper.repository.BeepRepository
import net.pensato.udemy.beeper.security.SecurityCenter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/users")
class UsuarioController @Autowired constructor(
        val usuarioRepository: UsuarioRepository,
        val beepRepository: BeepRepository,
        val bCryptPasswordEncoder: BCryptPasswordEncoder) {

    /**
     * <p>
     *     Create a new user
     *     Endpoint: POST /users
     *     RequestBody parameters: username, email, password
     * </p>
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody usuario: Usuario) {
        usuario.password = bCryptPasswordEncoder.encode(usuario.password)
        usuarioRepository.save(usuario)
    }

    /**
     * <p>
     *     Returns user information
     *     Originally was: Returns JWT token
     *     Endpoint: GET /users/{username}
     *     PathVariable: username
     * </p>
     */
    @GetMapping("/{username}")
    @ResponseBody
    fun getUserInfo(@PathVariable username: String, request: HttpServletRequest): Usuario? {
        val usuario: Usuario?
        if (username == "me") {
            usuario = usuarioRepository.findByUsername(SecurityCenter.getUsernameFromToken(request))
        } else {
            usuario = usuarioRepository.findByUsername(username)
        }
        Assert.notNull(usuario, "Username provided does not correspond to a valid user.")
        return usuario
    }

    /**
     * <p>
     *     Return paginated beeps of a user
     *     Endpoint: GET /users/{username}/beeps{?page}
     *     PathVariable: username
     * </p>
     * @param page
     *          - pagination
     * @return list of Beeps
     */
    @GetMapping("/{username}/beeps")
    @ResponseBody
    fun getUserBeeps(@PathVariable username: String, @RequestParam page: Int?): List<Beep> {
        val usuario = usuarioRepository.findByUsername(username)
        Assert.notNull(usuario, "Username provided does not correspond to a valid user.")
        if (page != null) {
            val pageable: Pageable = PageRequest(page, 10)
            return beepRepository.findAllByUsuario(usuario!!, pageable).toList()
        } else {
            return beepRepository.findAllByUsuario(usuario!!)
        }
    }

    /**
     * <p>
     *     Update user information
     *     Endpoint: PUT /users/me
     *     RequestBody parameters: username, about
     * </p>
     */
    @PutMapping("/me")
    fun updateUser(@RequestBody about: String, request: HttpServletRequest) {
        val username = request.userPrincipal.getName()
        val usuario = usuarioRepository.findByUsername(username)
        Assert.notNull(usuario, "Authenticated user is not valid.")
        usuario!!.about = about
        usuarioRepository.save(usuario)
    }

    /**
     * <p>
     *     Update user avatar.
     *     Originally should return its location
     *     Endpoint: PUT /users/me/avatar
     *     RequestBody parameters: avatar
     * </p>
     */
    @PutMapping("/me/avatar")
    fun updateAvatar(@RequestBody avatar: String, request: HttpServletRequest) {
        val username = request.userPrincipal.getName()
        val usuario = usuarioRepository.findByUsername(username)
        Assert.notNull(usuario, "Authenticated user is not valid.")
        usuario!!.avatar = avatar
        usuarioRepository.save(usuario)
    }
}

