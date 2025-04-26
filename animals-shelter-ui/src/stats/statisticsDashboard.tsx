import { Spacer } from "@nextui-org/react"
import AnimalAddedChart from "./animalAddedChart"
import VaccinationAddedChart from "./vaccinationAddedChart"
import AnimalLifespanChart from "./animalLifespanChart"
import { useEffect, useState } from "react"

export default function StatisticsPage() {
  const [animalCount, setAnimalCount] = useState<number>(0)
  const [vaccinationCount, setVaccinationCount] = useState<number>(0)

  useEffect(() => {
    const fetchCounts = async () => {
      try {
        const [animalRes, vaccinationRes] = await Promise.all([
          fetch("/ansh/stats/stats/animals/count"),
          fetch("/ansh/stats/stats/vaccinations/count"),
        ])

        setAnimalCount(await animalRes.json())
        setVaccinationCount(await vaccinationRes.json())
      } catch (error) {
        console.error("Failed to fetch counts:", error)
      }
    }

    fetchCounts()
  }, [])

  return (
    <div className="p-6 space-y-12">
      <h1 className="text-2xl font-bold">Statistics Dashboard</h1>

      <div className="space-y-2">
        <p>Total Animals: <strong>{animalCount}</strong></p>
        <p>Total Vaccinations: <strong>{vaccinationCount}</strong></p>
      </div>

      <Spacer y={10} />
      <AnimalAddedChart />

      <Spacer y={10} />
      <VaccinationAddedChart />

      <Spacer y={10} />
      <AnimalLifespanChart />
    </div>
  )
}
