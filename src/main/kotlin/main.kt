import WallService.idPost
import WallService.posts

fun main() {


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

    fun getById(noteId: Int): ArrayList<Note> {
        var list = ArrayList<Note>()
        for ((index, noteIndex) in notes.withIndex()) {
            if (noteIndex.id == noteId) {
                list.add(noteIndex)
            }
        }
        return list
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