package com.ansh.stats.controller

import com.ansh.stats.dto.AnimalLifespanStats
import com.ansh.stats.service.stats.AnimalStatsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/stats")
class AnimalStatsController(
    private val service: AnimalStatsService
) {

    @GetMapping("/animals/count")
    fun getAnimalCount(): Long = service.getAnimalCount()

    @GetMapping("/animals/added-by-date")
    fun getAddedByDate(): Map<LocalDate, Long> = service.getAddedAnimalsGroupedByDate()

    @GetMapping("/vaccinations/count")
    fun getVaccinationCount(): Long = service.getVaccinationCount()

    @GetMapping("/vaccinations/added-by-date")
    fun getAddedVaccinationsGroupedByDate(): Map<LocalDate, Long> =
        service.getAddedVaccinationsGroupedByDate()

    @GetMapping("/animals/lifespans")
    fun getAnimalLifespans(): List<AnimalLifespanStats> = service.getAnimalLifespans()
}