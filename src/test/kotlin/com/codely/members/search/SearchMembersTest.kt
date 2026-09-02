package com.codely.members.search

import com.codely.members.MemberMother
import com.codely.members.fakes.FakeMemberRepository
import com.codely.members.primaryadapter.rest.MemberResponseDTO
import com.codely.members.primaryadapter.rest.PageDTO
import com.codely.members.primaryadapter.rest.search.SearchMembersController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
        val page = result.body as PageDTO<*>
        assertEquals(2L, page.totalElements)
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
        val page = result.body as PageDTO<*>
        assertEquals(1L, page.totalElements)
        assertEquals(MemberResponseDTO.fromDomain(casual), page.content.first())
    }

    @Test
    fun `should filter coach members by type`() = runTest {
        val coach = MemberMother.coach()
        val casual = MemberMother.casual()
        repository.save(coach)
        repository.save(casual)

        val result = controller.search(type = "COACH", page = 0, size = 20)

        assertEquals(OK, result.statusCode)
        val page = result.body as PageDTO<*>
        assertEquals(1L, page.totalElements)
        assertEquals(MemberResponseDTO.fromDomain(coach), page.content.first())
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
        val page = result.body as PageDTO<*>
        assertEquals(3L, page.totalElements)
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
        val page = result.body as PageDTO<*>
        assertEquals(3L, page.totalElements)
        assertEquals(1, page.content.size)
    }
}
