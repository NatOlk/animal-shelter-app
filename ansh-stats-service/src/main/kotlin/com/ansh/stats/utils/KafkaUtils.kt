package com.ansh.stats.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord

inline fun <reified T : Any> processAndLogKafkaMessage(
    message: ConsumerRecord<String, String>,
    objectMapper: ObjectMapper,
    crossinline onSuccess: (T) -> Unit,
    crossinline logBefore: (T) -> Unit = {},
    crossinline logError: (Exception) -> Unit = {},
    crossinline logAfter: (T) -> Unit = {}
) {
    try {
        val event = objectMapper.readValue(message.value(), T::class.java)
        logBefore(event)
        onSuccess(event)
        logAfter(event)
    } catch (e: Exception) {
        logError(e);
    }
}
