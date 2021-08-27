package exceptions

class CommentDeletedException(commentId: Int) :
    RuntimeException("Comment with id $commentId is deleted")