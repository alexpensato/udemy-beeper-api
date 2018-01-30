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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/beeps")
class BeeperController @Autowired constructor(
        val usuarioRepository: UsuarioRepository,
        val beepRepository: BeepRepository) {

    /**
     * <p>
     *     Return all existing beeps
     *     Endpoint: GET /beeps{?page}
     * </p>
     * @param page
     *          - pagination
     * @return list of Beeps
     */
    @GetMapping()
    fun findAllBeeps(@RequestParam page: Int?): List<Beep> = if (page != null) {
            val pageable: Pageable = PageRequest(page, 10)
            beepRepository.findAll(pageable).toList()
        } else {
            beepRepository.findAll().toList()
        }

    /**
     * <p>
     *     Create a new beep to authenticated user
     *     Endpoint: POST /beeps
     *     RequestBody: text as String
     *                  - the content of the beep
     * </p>
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody text: String, request: HttpServletRequest) {
        val username = request.userPrincipal.getName()
        val usuario = usuarioRepository.findByUsername(username)
        Assert.notNull(usuario, "Authenticated user is not valid.")
        val beep = Beep(text = text, usuario = usuario!!)
        beepRepository.save(beep)
    }

    /**
     * <p>
     *     Increase counter for a beep likes
     *     Originally was: Toggles state of a beep
     *     Endpoint: POST /beeps/{id}/like
     *     PathVariable: id
     *                   - the id of the beep
     * </p>
     */
    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun toggleBeepStatus(@PathVariable id: Long?) {
        Assert.notNull(id, "You must provide a beep ID.")
        val beep = beepRepository.findOne(id!!)
        Assert.notNull(beep, "You must provide a valid beep ID.")
        beep.likes = beep.likes + 1
        beepRepository.save(beep)
    }

}


