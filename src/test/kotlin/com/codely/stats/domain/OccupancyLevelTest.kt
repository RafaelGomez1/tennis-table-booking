package com.codely.stats.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class OccupancyLevelTest : FunSpec({

    test("should classify 100% occupancy as ALWAYS_FULL") {
        OccupancyLevel.from(1.0) shouldBe OccupancyLevel.ALWAYS_FULL
    }

    test("should classify 0% occupancy as ALWAYS_EMPTY") {
        OccupancyLevel.from(0.0) shouldBe OccupancyLevel.ALWAYS_EMPTY
    }

    test("should classify 75% occupancy as USUALLY_FULL") {
        OccupancyLevel.from(0.75) shouldBe OccupancyLevel.USUALLY_FULL
    }

    test("should classify 90% occupancy as USUALLY_FULL") {
        OccupancyLevel.from(0.90) shouldBe OccupancyLevel.USUALLY_FULL
    }

    test("should classify 50% occupancy as MODERATE") {
        OccupancyLevel.from(0.50) shouldBe OccupancyLevel.MODERATE
    }

    test("should classify 25% occupancy as MODERATE") {
        OccupancyLevel.from(0.25) shouldBe OccupancyLevel.MODERATE
    }

    test("should classify 10% occupancy as USUALLY_EMPTY") {
        OccupancyLevel.from(0.10) shouldBe OccupancyLevel.USUALLY_EMPTY
    }

    test("should classify values just above 0 as USUALLY_EMPTY") {
        OccupancyLevel.from(0.01) shouldBe OccupancyLevel.USUALLY_EMPTY
    }
})
