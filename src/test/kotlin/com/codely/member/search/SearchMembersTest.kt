package com.codely.member.search

import com.codely.member.MemberMother
import com.codely.member.domain.Page
import com.codely.member.fakes.FakeMemberRepository
import com.codely.member.primaryadapter.rest.error.MemberServerErrors.INVALID_TYPE
import com.codely.member.primaryadapter.rest.search.SearchMembersController
import com.codely.shared.error.ServerError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMembersTest {

    private val repository = FakeMemberRepository()
    private val controller = SearchMembersController(repository)

    @BeforeEach
    fun setUp() {
        repository.resetFake()
    }

    @Test
    fun `should return all members paginated`() = runTest {
        val casual = MemberMother.casual()
        val competition = MemberMother.competition()
        repository.save(casual)
        repository.save(competition)

        val result = controller.search(type = null, page = 0, size = 20)

        assertEquals(OK, result.statusCode)
        val page = result.body as Page<*>
        assertEquals(2, page.totalElements)
        assertEquals(2, page.content.size)
    }

    @Test
    fun `should filter by type`() = runTest {
        val casual = MemberMother.casual()
        val competition = MemberMother.competition()
        repository.save(casual)
        repository.save(competition)

        val result = controller.search(type = "CASUAL", page = 0, size = 20)

        assertEquals(OK, result.statusCode)
        val page = result.body as Page<*>
        assertEquals(1, page.totalElements)
        assertEquals(casual, page.content.first())
    }

    @Test
    fun `should paginate results`() = runTest {
        val member1 = MemberMother.casual()
        val member2 = MemberMother.casual()
        val member3 = MemberMother.casual()
        repository.save(member1)
        repository.save(member2)
        repository.save(member3)

        val result = controller.search(type = null, page = 0, size = 2)

        assertEquals(OK, result.statusCode)
        val page = result.body as Page<*>
        assertEquals(3, page.totalElements)
        assertEquals(2, page.content.size)
        assertEquals(2, page.totalPages)
    }

    @Test
    fun `should return second page`() = runTest {
        val member1 = MemberMother.casual()
        val member2 = MemberMother.casual()
        val member3 = MemberMother.casual()
        repository.save(member1)
        repository.save(member2)
        repository.save(member3)

        val result = controller.search(type = null, page = 1, size = 2)

        assertEquals(OK, result.statusCode)
        val page = result.body as Page<*>
        assertEquals(3, page.totalElements)
        assertEquals(1, page.content.size)
    }

    @Test
    fun `should fail with invalid type`() = runTest {
        val result = controller.search(type = "UNKNOWN", page = 0, size = 20)

        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(ServerError.of(INVALID_TYPE), result.body)
    }
}
