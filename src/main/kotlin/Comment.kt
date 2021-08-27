data class Comment(
    val id: Int,
    val uid: Int,
    val nid: Int,
    val oid: Int,
    val date: Long,
    var message: String,
    val replyTo: Int?,
    val guid: String,
    var isDeleted: Boolean = false
)
