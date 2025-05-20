import { useEffect, useState } from "react"
import {
  BarChart, Bar, XAxis, YAxis,
  Tooltip, ResponsiveContainer, CartesianGrid, Legend,
} from "recharts"
import { fetchSubscriptionRequestByTopic, TopicRequestStats } from "./statisticsApi"

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

  return (
    <div style={{ width: "100%", height: 400 }}>
      <h2 className="text-xl font-semibold mb-2">Subscription Requests by Topic</h2>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="topic" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="count" fill="#8884d8" name="Requests" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
