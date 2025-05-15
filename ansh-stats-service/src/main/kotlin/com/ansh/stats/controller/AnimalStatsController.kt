package com.ansh.stats.controller

import com.ansh.stats.dto.AnimalLifespanStats
import com.ansh.stats.service.stats.AnimalStatsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/stats")
@Tag(name = "Animal Statistics", description = "Endpoints for animal and vaccination statistics")
class AnimalStatsController(
    private val service: AnimalStatsService
) {

    @GetMapping("/animals/count")
    @Operation(
        summary = "Get total number of animals",
        description = "Returns the total number of animals added to the system.",
        responses = [ApiResponse(responseCode = "200", description = "Total animal count")]
    )
    fun getAnimalCount(): Long = service.getAnimalCount()

    @GetMapping("/animals/added-by-date")
    @Operation(
        summary = "Get animal additions by date",
        description = "Returns the number of animals added, grouped by date.",
        responses = [ApiResponse(responseCode = "200", description = "Animals added grouped by date")]
    )
    fun getAddedByDate(): Map<LocalDate, Long> = service.getAddedAnimalsGroupedByDate()

    @GetMapping("/vaccinations/count")
    @Operation(
        summary = "Get total number of vaccinations",
        description = "Returns the total number of vaccinations added to the system.",
        responses = [ApiResponse(responseCode = "200", description = "Total vaccination count")]
    )
    fun getVaccinationCount(): Long = service.getVaccinationCount()

    @GetMapping("/vaccinations/added-by-date")
    @Operation(
        summary = "Get vaccinations by date",
        description = "Returns the number of vaccinations performed, grouped by date.",
        responses = [ApiResponse(responseCode = "200", description = "Vaccinations added grouped by date")]
    )
    fun getAddedVaccinationsGroupedByDate(): Map<LocalDate, Long> =
        service.getAddedVaccinationsGroupedByDate()

    @GetMapping("/animals/lifespans")
    @Operation(
        summary = "Get animal lifespans",
        description = "Returns the lifespan (in days) of each animal that has been added and later removed.",
        responses = [ApiResponse(responseCode = "200", description = "List of animal lifespans")]
    )
    fun getAnimalLifespans(): List<AnimalLifespanStats> = service.getAnimalLifespans()
}