import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class NoteServiceTest {

    @Before
    fun clearBeforeTest() {
        NoteService.clear()
    }

    @Test
    fun add() {
        val result = NoteService.add("title", "text")
        assertEquals(1, result)
    }

    @Test
    fun delete() {
        NoteService.add("title", "text")
        val result = NoteService.delete(1)
        assertEquals(true, result)
    }

    @Test
    fun edit() {
        NoteService.add("title", "text")
        val result = NoteService.edit(1, "title1", "text1")
        assertEquals(true, result)
    }

    @Test
    fun createComment() {

        NoteService.add("title", "text")
        val result = NoteService.createComment(1, "message")
        assertEquals(1, result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun shouldCreateCommentThrow() {

        NoteService.add("title", "text")
        NoteService.createComment(100, "message")

    }

    @Test
    fun deleteComment() {

        NoteService.add("title", "text")
        NoteService.createComment(1, "message")
        val result = NoteService.deleteComment(1)
        assertEquals(true, result)

    }

    @Test
    fun restoreComment() {

        NoteService.add("title", "text")
        NoteService.createComment(1, "message")
        NoteService.deleteComment(1)
        val result = NoteService.restoreComment(1)
        assertEquals(true, result)

    }
}