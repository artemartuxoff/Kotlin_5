import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @Test
    fun addMessage(){
        val result = ChatService.addMessage(0, "text")
        assertEquals(1, result)
    }

    @Test
    fun getChats(){
        ChatService.addChats(0)
        val result = ChatService.getChats().size
        assertEquals(1, result)
    }

    @Test
    fun getMessages(){
        ChatService.addMessage(1, "text")
        ChatService.addMessage(1, "text")
        val result = ChatService.getMessages(1).size
        assertEquals(2, result)
    }

    @Test
    fun getUnreadChatsCount(){
        ChatService.addMessage(1, "text")
        ChatService.addMessage(1, "text")
        val result = ChatService.getUnreadChatsCount()
        assertEquals(1, result)
    }

    @Test
    fun getUnreadChatsCount0(){

        ChatService.addMessage(1, "text")
        ChatService.addMessage(1, "text")
        ChatService.getMessages(1)
        val result = ChatService.getUnreadChatsCount()
        assertEquals(0, result)
    }

    @Test
    fun getLatestMessages(){
        ChatService.addMessage(1, "text")
        ChatService.addMessage(1, "text")
        ChatService.addMessage(2, "text")
        val result = ChatService.getLatestMessages().size
        assertEquals(2, result)
 }


    @Test (expected = ChatNotFoundException::class)
    fun deleteChatThrow() {

        ChatService.addMessage(1, "text")
        ChatService.deleteChat(2)

    }

}