package com.codely.members.domain

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class MembershipTest {

    @Test
    fun `coach membership should have zero price`() {
        assertEquals(0, Membership.Coach().membership().value)
    }
}
