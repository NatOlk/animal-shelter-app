import { useEffect, useState } from "react"
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid,
} from "recharts"
import { Button } from "@nextui-org/react"
import AnimalLifespan from "../common/types";

export default function AnimalLifespanChart() {
  const [data, setData] = useState<AnimalLifespan[]>([])
  const [showAll, setShowAll] = useState(false)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch("/ansh/stats/stats/animals/lifespans")
        if (!response.ok) throw new Error("Network response was not ok")
        const json = await response.json()
        setData(json)
      } catch (error) {
        console.error("Error fetching lifespan data", error)
      }
    }

    fetchData()
  }, [])

  const getDuration = (animal: AnimalLifespan) =>
    animal.daysInSystem * 1440 + animal.hoursInSystem * 60 + animal.minutesInSystem

  const sortedData = [...data].sort((a, b) => getDuration(b) - getDuration(a))
  const visibleData = showAll ? sortedData : sortedData.slice(0, 10)

  const tooltipFormatter = (_: any, __: string, props: any) => {
    const animal = props.payload
    return `${animal.daysInSystem}d ${animal.hoursInSystem}h ${animal.minutesInSystem}m`
  }

  return (
    <div style={{ width: "100%", height: 450 }}>
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-xl font-bold">Animal Lifespan in System</h2>
        <Button size="sm" onClick={() => setShowAll(!showAll)}>
          {showAll ? "Show Top 10" : "Show All"}
        </Button>
      </div>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
          data={visibleData}
          margin={{ top: 5, right: 20, left: 0, bottom: 50 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis
            dataKey="name"
            tickFormatter={(value, index) =>
              `${value} (${visibleData[index]?.species})`
            }
            interval={0}
            angle={-35}
            textAnchor="end"
            height={60}
            tick={{ fontSize: 12 }}/>
          <YAxis
            label={{ value: "Days", angle: -90, position: "insideLeft" }}
          />
          <Tooltip formatter={tooltipFormatter} />
          <Bar
            dataKey={(animal: AnimalLifespan) =>
              animal.daysInSystem +
              animal.hoursInSystem / 24 +
              animal.minutesInSystem / 1440
            }
            fill="#8884d8"
            name="Lifespan (days)"
          />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
