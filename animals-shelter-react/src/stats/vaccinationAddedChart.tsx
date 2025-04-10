import { useEffect, useState } from "react"
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid,
} from "recharts"
import CountByDate from "../common/types";

export default function VaccinationAddedChart() {
  const [data, setData] = useState<CountByDate[]>([])

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch("/ansh/stats/stats/vaccinations/added-by-date")
        if (!response.ok) {
          throw new Error("Failed to fetch vaccination data")
        }
        const json = await response.json()
        const transformed = Object.entries(json).map(([date, count]) => ({
          date,
          count: Number(count),
        }))
        setData(transformed)
      } catch (error) {
        console.error("Error fetching vaccination chart data:", error)
      }
    }

    fetchData()
  }, [])

  return (
    <div style={{ width: "100%", height: 400 }}>
      <h2 className="text-xl font-semibold mb-2">Vaccinations Added by Date</h2>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="date" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="count" fill="#8884d8" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
