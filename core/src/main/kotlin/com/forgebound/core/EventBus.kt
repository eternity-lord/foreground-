package com.forgebound.core

import kotlin.concurrent.thread

/** A subscriber handler for any published event. */
typealias Handler = (Any) -> Unit

/**
 * Simple in-process event bus (Section 3 architecture).
 * Game logic modules publish events; UI and systems subscribe. Kept dependency-free
 * so core/game stay pure Kotlin and unit-testable on the JVM.
 */
class EventBus {

    private val subscribers = mutableMapOf<Class<*>, MutableList<Handler>>()

    fun <T : Any> subscribe(eventClass: Class<T>, handler: (T) -> Unit) {
        synchronized(subscribers) {
            subscribers.getOrPut(eventClass) { mutableListOf() }.add(handler as Handler)
        }
    }

    fun <T : Any> publish(event: T) {
        val handlers = synchronized(subscribers) {
            subscribers[event::class.java]?.toMutableList() ?: emptyList()
        }
        // Handlers run on the publishing thread; UI threads marshal themselves.
        handlers.forEach { handler ->
            try {
                (handler as Handler)(event)
            } catch (_: Exception) {
                // One subscriber must never break the bus.
            }
        }
    }

    fun clear() {
        synchronized(subscribers) {
            subscribers.clear()
        }
    }
}
