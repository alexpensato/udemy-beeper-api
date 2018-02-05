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
import net.pensato.udemy.beeper.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
        val usuarioRepository: UsuarioRepository,
        val bCryptPasswordEncoder: BCryptPasswordEncoder) {

    /**
     * <p>
     *     Returns JWT token
     *     Endpoint: POST /auth
     *     RequestBody parameters: username, password
     * </p>
     */
    @PostMapping("/login")
    fun authResource(@RequestBody username: String, @RequestBody password: String): String {
        return "ACCESS GRANTED"
    }

    /**
     * <p>
     *     Registers a new user. It is similar to create new user, but this URI doesn't require previous authentication.
     *     Endpoint: POST /auth/register
     *     RequestBody parameters: usuario
     * </p>
     */
    @PostMapping("/register")
    fun register(@RequestBody usuario: Usuario) {
        usuario.password = bCryptPasswordEncoder.encode(usuario.password)
        usuarioRepository.save(usuario)
    }

}

