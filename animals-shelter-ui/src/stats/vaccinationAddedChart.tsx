import { useEffect, useState } from "react"
import {
  BarChart, Bar, XAxis, YAxis,
  Tooltip, ResponsiveContainer, CartesianGrid,
} from "recharts"
import CountByDate from "../common/types";
import { fetchVaccinationsByDate } from "./statisticsApi"

export default function VaccinationAddedChart() {
  const [data, setData] = useState<CountByDate[]>([])

  useEffect(() => {
    const fetchData = async () => {
      try {
        const json = await fetchVaccinationsByDate()
        const transformed = Object.entries(json).map(([date, count]) => ({
          date,
          count: Number(count),
        }))
        setData(transformed)
      } catch (error) {
        console.error("Error fetching vaccination chart data:")
      }
    }

    fetchData()
  }, [])

  return (
    <div style={{ width: "100%", height: 400 }}>
      <h2 className="text-xl font-semibold mb-2">Vaccinations Added by Date</h2>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
          data={data}
          margin={{ top: 20, right: 20, left: 0, bottom: 20 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="date" />
          <YAxis domain={[0, 'dataMax + 5']} />
          <Tooltip />
          <Bar
            dataKey="count"
            fill="#8884d8"
            radius={[4, 4, 0, 0]}
            isAnimationActive
            barSize={30}
          />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
