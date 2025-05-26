package com.ansh.stats.utils

inline fun <T> saveAndLog(
    item: T,
    save: (T) -> Unit,
    logBefore: (T) -> Unit = {},
    logAfter: (T) -> Unit = {}
) {
    logBefore(item)
    save(item)
    logAfter(item)
}