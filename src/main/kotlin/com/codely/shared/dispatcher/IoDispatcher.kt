package com.codely.shared.dispatcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withIOContext(block: suspend () -> T): T =
    withContext(Dispatchers.IO) {
        block()
    }
