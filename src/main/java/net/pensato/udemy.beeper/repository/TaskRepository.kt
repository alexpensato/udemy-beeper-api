package net.pensato.udemy.beeper.repository

import net.pensato.udemy.beeper.domain.Task
import org.springframework.data.repository.PagingAndSortingRepository

interface TaskRepository : PagingAndSortingRepository<Task, Long> {}
