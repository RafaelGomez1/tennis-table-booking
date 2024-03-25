package com.codely.shared.fakes

import org.assertj.core.api.Assertions

interface FakeRepository<ID, T> {
    val elements: MutableMap<ID, T>
    val errors: MutableList<Throwable>

    fun shouldFailWith(error: Throwable) = errors.add(error)

    fun resetFake() {
        elements.clear()
        errors.clear()
    }

    fun assertDoesNotContain(vararg resource: T) = Assertions.assertThat(elements.values).doesNotContain(*resource)
    fun assertContains(vararg resource: T) = Assertions.assertThat(elements.values).contains(*resource)
    fun assertContains(resource: T) = Assertions.assertThat(elements.values).contains(resource)

    fun resourceExistsById(id: ID): Boolean = id in elements.keys

    fun <Response> failIfConfiguredOrElse(block: () -> Response): Response =
        if (errors.isNotEmpty()) throw errors.removeFirst()
        else block()

    fun MutableMap<ID, T>.saveOrUpdate(value: T, id: ID) =
        if (id in elements.keys) elements.replace(id, value)
        else elements.put(id, value)
}
