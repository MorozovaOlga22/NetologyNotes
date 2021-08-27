package exceptions

class CommentNotDeletedException(commentId: Int) :
    RuntimeException("Comment with id $commentId is not deleted")