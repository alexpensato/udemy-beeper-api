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
package net.pensato.udemy.beeper.repository

import net.pensato.udemy.beeper.domain.Usuario
import net.pensato.udemy.beeper.domain.Beep
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface BeepRepository : PagingAndSortingRepository<Beep, Long> {

    fun findAllByAuthor(author: Usuario): List<Beep>

    fun findAllByAuthor(author: Usuario, pageable: Pageable): Page<Beep>

}
