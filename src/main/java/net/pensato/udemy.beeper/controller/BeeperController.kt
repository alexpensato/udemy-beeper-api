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

import net.pensato.udemy.beeper.domain.Beep
import net.pensato.udemy.beeper.repository.UsuarioRepository
import net.pensato.udemy.beeper.repository.BeepRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
     * @param page [@RequestParam]
     *          - pagination
     * @return list of Beeps
     */
    @GetMapping()
    fun findAllBeeps(page: Int?): Page<Beep> = if (page != null) {
            val pageable: Pageable = PageRequest(page, 5, Sort.Direction.ASC, "text")
            // Sleep only to showcase the spinner icon in the frontend
            Thread.sleep(1000);
            beepRepository.findAll(pageable)
        } else {
            val pageable: Pageable = PageRequest(0, 10)
            beepRepository.findAll(pageable)
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
        val usuario = usuarioRepository.findByUsernameIgnoreCase(username)
        Assert.notNull(usuario, "Authenticated user is not valid.")
        val beep = Beep(text = text, author = usuario!!)
        beepRepository.save(beep)
    }

    /**
     * <p>
     *     Toggles state of a beep.
     *     Endpoint: POST /beeps/{id}/like
     *     PathVariable: id
     *                   - the id of the beep
     * </p>
     */
    @PatchMapping("/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun toggleBeepStatus(@PathVariable id: Long?) {
        Assert.notNull(id, "You must provide a beep ID.")
        val beep = beepRepository.findOne(id!!)
        Assert.notNull(beep, "You must provide a valid beep ID.")
        if(beep.liked) {
            beep.liked = false;
            beep.likes -= 1
        } else {
            beep.liked = true;
            beep.likes += 1
        }
        beepRepository.save(beep)
    }

}


