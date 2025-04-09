package com.ansh.stats.repository

import com.ansh.stats.entity.AnimalEventDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface AnimalEventRepository : MongoRepository<AnimalEventDocument, String>
