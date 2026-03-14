import NoteService.comments
import NoteService.idNote
import NoteService.idNoteComment
import NoteService.notes
import WallService.idPost
import WallService.posts
import kotlin.collections.arrayListOf

fun main() {

    ChatService.addMessage(1, "text")
    ChatService.addMessage(1, "text")
    val result = ChatService.getLatestMessages().size

}

data class Post(
    val id: Int,
    val ownerId: Int,
    val fromId: Int,
    val createdBy: Int,
    val date: Int,
    val text: String,
    val comments: Comment?,
    val likes: Like?,
    val attachments: Array<Attachment> = emptyArray()
)

class Comment(
    val count: Int = 0,
    val canPost: Boolean = true,
    val groupCanPost: Boolean = true,
    val canClose: Boolean = false,
    val canOpen: Boolean = false
)

class Like(
    val count: Int = 0,
    val userLikes: Boolean = false,
    val canLikes: Boolean = true,
    val canPublish: Boolean = true
)

class PostNotFoundException() : RuntimeException()

object WallService {

    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()
    private var idPost: Int = 0

    fun add(post: Post): Post {
        idPost += 1
        val newPost = post.copy(id = idPost)
        posts += newPost
        return posts.last()
    }

    fun update(post: Post): Boolean {
        for ((index, postIn) in posts.withIndex()) {
            if (post.id == postIn.id) {
                posts.set(index, post)
                return true
            }
        }
        return false
    }

    fun clear() {
        posts = emptyArray()
        comments = emptyArray()
        idPost = 0

    }

    fun createComment(postId: Int, comment: Comment): Comment {

        for (post in posts) {
            if (post.id == postId) {
                comments += comment
                return comment
            }
        }
        throw PostNotFoundException()
    }
}

data class Note(
    val id: Int,
    var title: String,
    var text: String
)

class NoteComment(
    val id: Int,
    val noteId: Int,
    var message: String,
    var marker: Boolean
)

class NoteNotFoundException() : RuntimeException()

object NoteService {

    private var notes = arrayListOf<Note>()
    private var comments = arrayListOf<NoteComment>()

    private var idNote: Int = 0
    private var idNoteComment: Int = 0

    fun clear() {
        notes.clear()
        comments.clear()
        idNote = 0
        idNoteComment = 0
    }

    fun add(title: String, text: String): Int {
        notes.add(Note(++idNote, title, text))
        return notes.last().id
    }

    fun delete(noteId: Int): Boolean {
        for ((index, noteIndex) in notes.withIndex()) {
            if (noteIndex.id == noteId) {
                deleteCommentByNoteId(noteId)
                notes.removeAt(index)
                return true
            }
        }
        return false
    }

    fun edit(noteId: Int, title: String, text: String): Boolean {
        for ((index, noteIndex) in notes.withIndex()) {
            if (noteIndex.id == noteId) {
                val note = Note(noteId, title, text)
                notes.set(index, note)
                return true
            }
        }
        return false
    }

    fun get(): ArrayList<Note> {
        return notes
    }

    fun getById(noteId: Int): Note {
        for ((index, noteIndex) in notes.withIndex()) {
            if (noteIndex.id == noteId) {
                return noteIndex
            }
        }

        throw NoteNotFoundException()
    }

    fun createComment(noteId: Int, message: String): Int {
        for ((index, noteIndex) in notes.withIndex()) {
            if (noteIndex.id == noteId) {
                comments.add(NoteComment(++idNoteComment, noteId, message, false))
                return comments.last().id
            }
        }
        throw NoteNotFoundException()
    }

    fun deleteComment(commentId: Int): Boolean {
        for ((index, commentIndex) in comments.withIndex()) {
            if (commentIndex.id == commentId) {
                commentIndex.marker = true
                return true
            }
        }
        return false
    }

    fun deleteCommentByNoteId(noteId: Int): Boolean {
        for ((index, commentIndex) in comments.withIndex()) {
            if (commentIndex.noteId == noteId) {
                comments.remove(commentIndex)
            }
        }
        return true
    }

    fun editComment(commentId: Int, message: String): Boolean {
        for ((index, commentIndex) in comments.withIndex()) {
            if (commentIndex.id == commentId) {
                commentIndex.message = message
                return true
            }
        }
        return false
    }

    fun getComments(noteId: Int): Array<NoteComment> {
        var list = emptyArray<NoteComment>()
        for (comment in comments) {
            if (comment.noteId == noteId) {
                if (!comment.marker) {
                    list += comment
                }
            }
        }
        return list
    }

    fun restoreComment(commentId: Int): Boolean {
        for ((index, commentIndex) in comments.withIndex()) {
            if (commentIndex.id == commentId) {
                commentIndex.marker = false
                return true
            }
        }
        return false
    }
}

data class Chat(
    val id: Int,
    var name: String = "",
    val idUser: Int = 0
)

class Message(
    val id: Int,
    val idChat: Int,
    var text: String,
    var marker: Boolean = false
)

object ChatService {

    private var chats = arrayListOf<Chat>()
    private var messages = arrayListOf<Message>()

    private var idChats: Int = 0
    private var idMessages: Int = 0

    fun clear() {
        chats.clear()
        messages.clear()
        idChats = 0
        idMessages = 0
    }

    fun addChats(idChat: Int): Int {

       for(chat in chats) {
            if (chat.id == idChat) {
               return chat.id
            }
        }
        val chat = Chat(++idChats)
        chats.add(chat)
        return chat.id
    }

    fun addMessage(idChat: Int, text: String): Int {
        val idParent = addChats(idChat)
        val message = Message(++idMessages,idParent, text)
        messages.add(message)
        return message.id
    }

    fun getChats(): ArrayList<Chat> {
        return chats
    }

    fun getMessages(idChat: Int): List<Message> {

        val chatList = messages.filter(fun(message: Message) = message.idChat == idChat)

        for (message in chatList) {
            editMessage(message.id, message.text, true)
        }
        return chatList
    }

    fun editMessage(id: Int, text: String, marker: Boolean): Boolean {
        for (message in messages) {
            if (message.id == id) {
                message.text = text
                message.marker = marker
                return true
            }
        }
        return false
    }

    fun getUnreadChatsCount(): Int {

        var count = messages.filter { it.marker == false }
        .groupBy { it.idChat }
        .size

        //for (chat in chats) {
        //    val chatList =
        //        messages.filter(fun(message: Message) = (message.idChat == chat.id) and (message.marker == false))
        //    if (chatList.size > 0) {
        //        count++
        //    }
        //}
        return count
    }

    fun getLatestMessages(): ArrayList<String> {

        var latest = messages.filter { it.marker }
            .map { it.text }
            .toCollection(arrayListOf())

        //var latest = ArrayList<String>()
        //val chatList = messages.filter(fun(message: Message) = message.marker == false)
        //
        //for (message in chatList) {
        //    latest.add(message.text)
        //}
        return latest
    }

    fun deleteMessage(id: Int): Boolean {
        for (message in messages) {
            if (message.id == id) {
                messages.remove(message)
                return true
            }
        }
        return false
    }

    fun deleteChat(idChat: Int): Boolean {
        for (chat in chats) {
            if (chat.id == idChat) {

                val delMessages: List<Message> = messages.filter { message -> message.idChat == idChat }

                for (delMessage in delMessages) {
                    deleteMessage(delMessage.id)
                }
                chats.remove(chat)
                return true
            }
        }
        return false
    }
}


interface Attachment {
    val type: String
}

class PhotoAttachment(val photo: Photo, override val type: String) : Attachment {}

class Photo(
    val id: Integer,
    val albumId: Integer,
    val ownerId: Integer,
    val userId: Integer,
    val text: String

)

class VideoAttachment(val video: Video, override val type: String) : Attachment

class Video(

    val id: Integer,
    val ownerId: Integer,
    val title: String,
    val description: String,
    val duration: Integer

)

class AudioAttachment(val audio: Audio, override val type: String) : Attachment

class Audio(

    val id: Integer,
    val ownerId: Integer,
    val artist: String,
    val title: String,
    val duration: Integer

)

class FileAttachment(val file: File, override val type: String) : Attachment

class File(

    val id: Integer,
    val ownerId: Integer,
    val title: String,
    val size: Integer,
    val ext: String

)

class StickerAttachment(val sticker: Sticker, override val type: String) : Attachment

class Sticker(

    val productId: Integer,
    val stickerId: Integer,
    val isAllowed: Boolean

)