import { useEffect, useState } from "react"
import {
  LineChart, Line, XAxis, YAxis, Tooltip,
  ResponsiveContainer, CartesianGrid,
} from "recharts"
import CountByDate from "../common/types"
import { fetchAnimalsByDate } from "./statisticsApi"

export default function AnimalAddedLineChart() {
  const [data, setData] = useState<CountByDate[]>([])

  useEffect(() => {
    const fetchData = async () => {
      try {
        const json = await fetchAnimalsByDate()
        const transformed = Object.entries(json).map(([date, count]) => ({
          date,
          count: Number(count),
        }))
        setData(transformed)
      } catch (error) {
        console.error("Error fetching animal added data")
      }
    }

    fetchData()
  }, [])

  return (
    <div style={{ width: "100%", height: 400 }}>
      <h2 className="text-xl font-semibold mb-2">Animals Added by Date</h2>
      <ResponsiveContainer width="100%" height="100%">
        <LineChart
          data={data}
          margin={{ top: 20, right: 20, left: 0, bottom: 20 }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="date" />
          <YAxis domain={[0, 'dataMax + 5']} />
          <Tooltip />
          <Line
            type="monotone"
            dataKey="count"
            stroke="#82ca9d"
            strokeWidth={2}
            dot={{ r: 4 }}
            activeDot={{ r: 6 }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  )
}
