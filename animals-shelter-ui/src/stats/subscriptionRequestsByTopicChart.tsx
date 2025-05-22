import { useEffect, useState } from "react"
import {
  BarChart, Bar, XAxis, YAxis,
  Tooltip, ResponsiveContainer, CartesianGrid, Legend,
} from "recharts"
import { fetchSubscriptionRequestByTopic, TopicRequestStats } from "./statisticsApi"
import { topics } from "../common/types"

export default function SubscriptionRequestsByTopicChart() {
  const [data, setData] = useState<TopicRequestStats[]>([])

  useEffect(() => {
    const fetchData = async () => {
      try {
        const json = await fetchSubscriptionRequestByTopic()
        setData(json)
      } catch (error) {
        console.error("Error fetching subscription request data")
      }
    }

    fetchData()
  }, [])

  const topicMap = Object.fromEntries(
    topics.map(({ key, label }) => [key, label])
  )

  return (
    <div style={{ width: "100%", height: 400 }}>
      <h2 className="text-xl font-semibold mb-2">Subscription Requests by Topic</h2>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
          data={data}
          margin={{ top: 20, right: 20, left: 0, bottom: 20 }}
          barCategoryGap="20%">
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis
            dataKey="topic"
            tickFormatter={(value: string) => topicMap[value] || value}/>
          <YAxis domain={[0, 'dataMax + 2']} />
          <Tooltip
            formatter={(value: any, name: string, props: any) => [value, 'Requests']}
            labelFormatter={(label: string) => topicMap[label] || label}/>
          <Legend />
          <Bar
            dataKey="count"
            fill="#8884d8"
            name="Requests"
            barSize={30}
            radius={[4, 4, 0, 0]}
            isAnimationActive/>
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}