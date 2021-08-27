data class Note(
    val id: Int,
    val ownerId: Int,
    var title: String,
    var text: String,
    val date: Long,
    var comments: Int,
    val readComments: Int?,
    val viewUrl: String,
    var privacyView: MutableList<String>,
    var privacyComment: MutableList<String>,
    val canComment: Boolean,
    val textWiki: String,
    var isDeleted: Boolean = false
)