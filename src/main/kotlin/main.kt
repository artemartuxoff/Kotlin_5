fun main() {


}

data class Post(
    val id: Int,
    val ownerId: Int,
    val fromId: Int,
    val createdBy: Int,
    val date: Int,
    val text: String,
    val comments: Comment,
    val likes: Like
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

object WallService {

    private var posts = emptyArray<Post>()
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
        idPost = 0
    }
}