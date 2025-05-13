import { Spacer, Tabs, Tab, Card, CardBody } from "@nextui-org/react";
import AnimalAddedChart from "./animalAddedChart"
import VaccinationAddedChart from "./vaccinationAddedChart"
import AnimalLifespanChart from "./animalLifespanChart"
import SubscriptionDecisionsChart from "./subscriptionDecisionsChart"
import { useEffect, useState } from "react"
import { MdOutlinePets } from "react-icons/md";
import { BsPersonBoundingBox } from "react-icons/bs";
import { fetchAnimalCount, fetchVaccinationCount } from "./statisticsApi"

export default function StatisticsPage() {
  const [animalCount, setAnimalCount] = useState<number>(0)
  const [vaccinationCount, setVaccinationCount] = useState<number>(0)

  useEffect(() => {
    const fetchCounts = async () => {
      try {
        const [animalRes, vaccinationRes] = await Promise.all([
          fetchAnimalCount(),
          fetchVaccinationCount(),
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
    <div className="flex w-full flex-col">
      <Tabs aria-label="Stats Options" size="lg" variant="bordered">
        <Tab key="animalStats" title={
          <div className="flex items-center space-x-2">
            <MdOutlinePets /><p>Animals</p>
          </div>}>
          <Card>
            <CardBody>
              <h2>Total Animals: <strong>{animalCount}</strong></h2>
              <h2>Total Vaccinations: <strong>{vaccinationCount}</strong></h2>
              <Spacer y={5} />
              <AnimalAddedChart />
              <Spacer y={5} />
              <VaccinationAddedChart />
              <Spacer y={5} />
              <AnimalLifespanChart />
            </CardBody>
          </Card>
        </Tab>
        <Tab key="subscriptionStats" title={
          <div className="flex items-center space-x-2">
            <BsPersonBoundingBox /><p>Subscription Decisions</p>
          </div>}>
          <Card>
            <CardBody>
              <Spacer y={5} />
              <SubscriptionDecisionsChart />
            </CardBody>
          </Card>
        </Tab>
      </Tabs>
    </div>
  )
}
