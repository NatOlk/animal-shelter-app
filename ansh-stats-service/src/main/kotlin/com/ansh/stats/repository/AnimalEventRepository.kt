package com.ansh.stats.repository

import com.ansh.stats.entity.AnimalEventDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface AnimalEventRepository : MongoRepository<AnimalEventDocument, String> {
    fun findByEventType(eventType: String): List<AnimalEventDocument>
    fun countByEventType(eventType: String): Long
    fun findByEventTypeIn(eventTypes: List<String>): List<AnimalEventDocument>
}