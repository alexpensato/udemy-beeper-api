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
package net.pensato.udemy.beeper.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
data class Usuario(
        @Id
        @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
        var id: Long = 0,
        @Column(length=50)
        @get:NotNull
        var username: String = "",
        @Column(length=50)
        @get:NotNull
        var email: String = "",
        @Column(length=100)
        @get:NotNull
        var password: String = "",
        var about: String = "I like beeping",
        @Column(length=100)
        var avatar: String = "novatar.jpg"
)