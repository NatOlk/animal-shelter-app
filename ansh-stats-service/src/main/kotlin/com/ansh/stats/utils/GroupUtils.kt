package com.ansh.stats.utils

fun <T, K, R> groupAndMap(
    items: List<T>,
    keySelector: (T) -> K,
    valueMapper: (K, List<T>) -> R
): List<R> {
    return items.groupBy(keySelector).map { (key, group) ->
        valueMapper(key, group)
    }
}