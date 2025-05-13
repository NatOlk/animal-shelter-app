import { useEffect, useState } from "react"
import {
  BarChart, Bar, XAxis, YAxis,
  Tooltip, ResponsiveContainer, CartesianGrid, Legend,
} from "recharts"
import { fetchSubscriptionDecisionByTopic, TopicDecisionStats } from "./statisticsApi"

export default function SubscriptionDecisionsChart() {
  const [data, setData] = useState<TopicDecisionStats[]>([])

  useEffect(() => {
    const fetchData = async () => {
      try {
        const json = await fetchSubscriptionDecisionByTopic()
        setData(json)
      } catch (error) {
        console.error("Error fetching subscription decision data:", error)
      }
    }

    fetchData()
  }, [])

  return (
    <div style={{ width: "100%", height: 400 }}>
      <h2 className="text-xl font-semibold mb-2">Subscription Decisions by Topic</h2>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="topic" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="approvedCount" fill="#82ca9d" name="Approved" />
          <Bar dataKey="rejectedCount" fill="#ff7f7f" name="Rejected" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
