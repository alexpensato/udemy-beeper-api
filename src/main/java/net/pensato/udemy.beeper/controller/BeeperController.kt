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

import net.pensato.udemy.beeper.domain.AppUser
import net.pensato.udemy.beeper.domain.Beep
import net.pensato.udemy.beeper.repository.AppUserRepository
import net.pensato.udemy.beeper.repository.BeepRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/beeper")
class BeeperController @Autowired constructor(val appUserRepository: AppUserRepository, val beepRepository: BeepRepository) {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun findAll(pageable: Pageable): Page<AppUser> {
        return appUserRepository.findAll(pageable)
    }

    @RequestMapping(value = "/{id}", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun findById(@PathVariable id: Long?): AppUser {
        Assert.notNull(id, "You must provide an ID to locate an item in the repository.")
        return appUserRepository.findOne(id!!)
    }

    @RequestMapping(value = "/{appUserId}/beeps", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun findAllBeepsByAppUser(@PathVariable appUserId: Long?): List<Beep> {
        Assert.notNull(appUserId, "You must provide an ID to locate nested itens in an auxiliary repository.")
        val appUser = appUserRepository.findOne(appUserId!!)
        Assert.notNull(appUser, "ID provided does not correspond to a valid AppUser Id.")
        return beepRepository.findAllByAppUser(appUser)
    }

    @RequestMapping(value = "/{id}", method = arrayOf(RequestMethod.DELETE))
    @ResponseBody
    fun delete(@PathVariable id: Long?): String {
        Assert.notNull(id, "You must provide an ID to delete an item from the repository.")
        appUserRepository.delete(id!!)
        return "Item corresponding to ID $id has been deleted."
    }

    @RequestMapping(value = "/{id}", method = arrayOf(RequestMethod.PUT))
    @ResponseBody
    fun update(@PathVariable id: Long?, @RequestBody t: AppUser): String {
        Assert.notNull(id, "You must provide an ID to update an item in the repository.")
        Assert.state(t.id == id, "The item you are trying to update does not exist in the repository.")
        t.id = id!!
        return "Item updated: ${appUserRepository.save(t)}."
    }

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody t: AppUser): AppUser {
        val c = appUserRepository.save(t)
        return c
    }

}

