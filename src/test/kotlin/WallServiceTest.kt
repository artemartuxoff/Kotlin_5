import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class WallServiceTest {

    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun testAdd(){

        //val post = WallService.add(Post(0,0,0,0,0,"text", Comment(1), Like(1)))
        val post = WallService.add(Post(0,0,0,0,0,"text", null, null))

        assertEquals(1, post.id)
    }

    @Test
    fun testUpdateTrue(){
        //добавим записи
        WallService.add(Post(0,0,0,0,0,"text1", Comment(1), Like(1)))
        WallService.add(Post(0,0,0,0,0,"text2", Comment(1), Like(1)))

        //запускаем обновление
        val result = WallService.update(Post(1,0,0,0,0,"text", Comment(1), Like(1)))

        //проверяем результат
        assertEquals(true, result)

    }

    @Test
    fun testUpdateFalse(){

        //добавим записи
        WallService.add(Post(0,0,0,0,0,"text1", Comment(1), Like(1)))
        WallService.add(Post(0,0,0,0,0,"text2", Comment(1), Like(1)))

        //запускаем обновление
        val result = WallService.update(Post(100,0,0,0,0,"text", Comment(1), Like(1)))

        //проверяем результат
        assertEquals(false, result)

    }

    @Test
    fun addComment() {

        //добавим записи
        WallService.add(Post(0,0,0,0,0,"text1", Comment(1), Like(1)))

        //добавим комментарий
        val comment = Comment(1)
        val result  = WallService.createComment(1, comment)

        assertEquals(comment, result)
    }

    @Test(expected = PostNotFoundException::class)
    fun shouldThrow() {
        //добавим записи
        WallService.add(Post(0,0,0,0,0,"text1", Comment(1), Like(1)))

        //добавим комментарий
        val comment = Comment(1)
        WallService.createComment(0, comment)
    }
}