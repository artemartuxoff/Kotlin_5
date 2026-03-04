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