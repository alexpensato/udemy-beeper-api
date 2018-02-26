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
package net.pensato.udemy.beeper

import net.pensato.udemy.beeper.domain.Usuario
import net.pensato.udemy.beeper.domain.Beep
import net.pensato.udemy.beeper.repository.UsuarioRepository
import net.pensato.udemy.beeper.repository.BeepRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder



@SpringBootApplication
@EnableJpaRepositories(basePackages = arrayOf("net.pensato.udemy.beeper.repository"))
open class App : SpringBootServletInitializer() {

	override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
		return application.sources(App::class.java)
	}

	@Bean
	open fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
		return BCryptPasswordEncoder()
	}

	@Bean
	open fun init(
			personRepository: UsuarioRepository,
			beepRepository: BeepRepository) = CommandLineRunner {

		val result = personRepository.findAll()
		if (result == null || result.toList().isEmpty()) {

			val john = personRepository.save(Usuario(username = "John", email = "john@example.com"))
			beepRepository.save(Beep(text = "My first beep", author = john))
			beepRepository.save(Beep(text = "Another beep", author = john))
			beepRepository.save(Beep(text = "Last beep", author = john))

			val mary = personRepository.save(Usuario(username = "Mary", email = "mary@example.com"))
			beepRepository.save(Beep(text = "Hello everyone", author = mary))
			beepRepository.save(Beep(text = "How are you doing?", author = mary))
			beepRepository.save(Beep(text = "Good bye everyone", author = mary))
		}
	}
}

fun main(args: Array<String>) {
	SpringApplication.run(App::class.java, *args)
}
