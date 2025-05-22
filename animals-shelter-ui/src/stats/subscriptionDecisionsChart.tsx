import { useEffect, useState } from "react"
import {
  PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer,
} from "recharts"
import { fetchSubscriptionDecisionByTopic, TopicDecisionStats } from "./statisticsApi"
import { topics } from "../common/types"

const COLORS = {
  approved: "#82ca9d",
  rejected: "#ff7f7f"
}

export default function SubscriptionDecisionsChart() {
  const [data, setData] = useState<TopicDecisionStats[]>([])

  useEffect(() => {
    const fetchData = async () => {
      try {
        const json = await fetchSubscriptionDecisionByTopic()
        setData(json)
      } catch (error) {
        console.error("Error fetching subscription decision data")
      }
    }

    fetchData()
  }, [])

  const topicMap = Object.fromEntries(
    topics.map(({ key, label, description }) => [key, { label, description }])
  )

  const renderPieData = (topicData: TopicDecisionStats) => [
    { name: "Approved", value: topicData.approvedCount },
    { name: "Rejected", value: topicData.rejectedCount },
  ]

  return (
    <div>
      <h2 className="text-xl font-semibold mb-4">Subscription Decisions by Topic</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
       {data.map((topicData, index) => {
         const topicInfo = topicMap[topicData.topic] ?? {
           label: topicData.topic,
           description: "",
         }
         return (
           <div
             key={index}
             className="bg-white rounded p-6"
             style={{
               minWidth: "350px",
               height: 340,
               marginBottom: "20px",
             }}>
             <h3 className="text-md font-medium text-center mb-1">{topicInfo.label}</h3>
             <p className="text-sm text-center text-gray-500 mb-3">{topicInfo.description}</p>
             <p className="text-sm text-center text-gray-700 mb-3 font-semibold">
                   Total requests: {topicData.count}
             </p>
             <ResponsiveContainer width="100%" height="100%">
               <PieChart>
                 <Pie
                   data={renderPieData(topicData)}
                   dataKey="value"
                   nameKey="name"
                   cx="50%"
                   cy="50%"
                   outerRadius={80}
                   label>
                   <Cell fill={COLORS.approved} />
                   <Cell fill={COLORS.rejected} />
                 </Pie>
                 <Tooltip />
                 <Legend />
               </PieChart>
             </ResponsiveContainer>
           </div>
         )
       })}
      </div>
    </div>
  )
}
