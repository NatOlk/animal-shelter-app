package com.ansh.stats.repository

import com.ansh.stats.dto.AnimalLifespanStats
import com.ansh.stats.dto.EventCountByDate
import com.ansh.stats.entity.AnimalEventDocument
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AnimalEventRepository : MongoRepository<AnimalEventDocument, String> {
    fun countByEventType(eventType: String): Long
    fun existsByEventId(eventId: String): Boolean
    @Aggregation(
        pipeline = [
            "{ '\$match': { 'eventType': { '\$in': [ " +
                    "'com.ansh.event.animal.AddAnimalEvent', " +
                    "'com.ansh.event.animal.RemoveAnimalEvent' ] } } }",
            "{ '\$group': { " +
                    "'_id': '\$animalId', " +
                    "'minDate': { '\$min': { '\$cond': [ { '\$eq': [ '\$eventType', 'com.ansh.event.animal.AddAnimalEvent' ] }, '\$payload.created', null ] } }, " +
                    "'maxDate': { '\$max': { '\$cond': [ { '\$eq': [ '\$eventType', 'com.ansh.event.animal.RemoveAnimalEvent' ] }, '\$payload.created', null ] } }, " +
                    "'animal': { '\$first': '\$payload.animal' }" +
                    "} }",
            "{ '\$match': { 'minDate': { '\$ne': null }, 'maxDate': { '\$ne': null } } }",
            "{ '\$project': { " +
                    "'animalId': '\$_id', " +
                    "'name': '\$animal.name', " +
                    "'species': '\$animal.species', " +
                    "'daysInSystem': { '\$dateDiff': { 'startDate': '\$minDate', 'endDate': '\$maxDate', 'unit': 'day' } }, " +
                    "'_id': 0" +
                    "} }"
        ]
    )
    fun aggregateAnimalLifespans(): List<AnimalLifespanStats>

    @Aggregation(
        pipeline = [
            "{ '\$match': { 'eventType': ?0 } }",
            "{ '\$group': { '_id': '\$payload.created', 'count': { '\$sum': 1 } } }",
            "{ '\$project': { 'date': '\$_id', 'count': 1, '_id': 0 } }",
            "{ '\$sort': { 'date': 1 } }"
        ]
    )
    fun aggregateEventCountByDate(eventType: String): List<EventCountByDate>
}