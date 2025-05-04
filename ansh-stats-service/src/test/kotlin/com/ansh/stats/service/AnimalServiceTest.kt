import com.ansh.entity.animal.Animal
import com.ansh.entity.animal.Vaccination
import com.ansh.event.animal.AddAnimalEvent
import com.ansh.stats.repository.AnimalEventRepository
import com.ansh.stats.service.AnimalService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class AnimalServiceTest {

    private val repository: AnimalEventRepository = mock()
    private val service = AnimalService(repository)

    @Test
    fun `should save animal event and nullify vaccination's animal`() {
        val animal = Animal()
        animal.id = 1
        animal.name = "Max"
        animal.species = "Dog"

        val vaccination = Vaccination()
        vaccination.vaccine = "Rabies"
        vaccination.batch = "RB001"
        vaccination.email = "test@example.com"
        vaccination.vaccinationTime = java.time.LocalDate.now()
        vaccination.comments = "Initial dose"
        vaccination.version = 0
        vaccination.animal = animal

        animal.vaccinations = listOf(vaccination)

        val event = AddAnimalEvent(animal)

        service.saveEvent(event)

        assert(vaccination.animal == null)

        verify(repository).save(check {
            assert(it.animalId == 1L)
            assert(it.payload.animal.name == "Max")
        })
    }
}
