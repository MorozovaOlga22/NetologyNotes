import exceptions.*
import java.util.*
import kotlin.collections.ArrayList

class NoteServiceImpl : NoteService {
    internal val notes: MutableList<Note> = ArrayList()
    internal val comments: MutableList<Comment> = ArrayList()

    internal var nextNoteId = 0
    internal var nextCommentId = 0

    override fun add(
        title: String,
        text: String,
        privacyView: MutableList<String>,
        privacyComment: MutableList<String>
    ): Int {
        //часть полей заполняем заглушками
        val newNote = Note(
            id = nextNoteId++,
            ownerId = 0,
            title = title,
            text = text,
            date = Calendar.getInstance().time.time,
            comments = 0,
            readComments = null,
            viewUrl = "",
            privacyView = privacyView,
            privacyComment = privacyComment,
            canComment = true,
            textWiki = ""
        )
        notes.add(newNote)

        return newNote.id
    }

    override fun createComment(noteId: Int, replyTo: Int?, message: String, guid: String): Int {
        checkGuid(guid)
        val note = getById(noteId)
        if (note.isDeleted) {
            throw NoteDeletedException(noteId)
        }

        val newComment = Comment(
            id = nextCommentId++,
            uid = 0,
            nid = noteId,
            oid = 0,
            date = Calendar.getInstance().time.time,
            message = message,
            replyTo = replyTo,
            guid = guid
        )
        comments.add(newComment)
        note.comments++

        return newComment.id
    }

    private fun checkGuid(guid: String) {
        for (comment in comments) {
            if (comment.guid == guid) {
                throw SameGuidException(guid)
            }
        }
    }

    private fun checkNoteNotDeleted(noteId: Int) {
        val note = getById(noteId)
        if (note.isDeleted) {
            throw NoteDeletedException(noteId)
        }
    }

    override fun delete(noteId: Int): Int {
        val note = getById(noteId)
        if (note.isDeleted) {
            throw NoteDeletedException(noteId)
        }
        note.isDeleted = true
        return 1
    }

    override fun deleteComment(commentId: Int): Int {
        val comment = getCommentById(commentId)
        if (comment.isDeleted) {
            throw CommentDeletedException(commentId)
        }
        checkNoteNotDeleted(comment.nid)
        comment.isDeleted = true
        return 1
    }

    private fun getCommentById(commentId: Int): Comment {
        for (comment in comments) {
            if (comment.id == commentId) {
                return comment
            }
        }
        throw CommentNotFoundException(commentId)
    }

    override fun edit(
        noteId: Int,
        title: String,
        text: String,
        privacyView: MutableList<String>,
        privacyComment: MutableList<String>
    ): Int {
        val note = getById(noteId)
        note.title = title
        note.text = text
        note.privacyView = privacyView
        note.privacyComment = privacyComment
        note.isDeleted = false
        return 1
    }

    override fun editComment(commentId: Int, message: String): Int {
        val comment = getCommentById(commentId)
        checkNoteNotDeleted(comment.nid)
        comment.message = message
        comment.isDeleted = false
        return 1
    }

    override fun get(noteIds: List<Int>, sort: NotesSortEnum): MutableList<Note> {
        val resultNotesList: MutableList<Note> = ArrayList()
        noteIds.forEach { noteId -> resultNotesList.add(getById(noteId)) }
        when (sort) {
            NotesSortEnum.DateAscending -> resultNotesList.sortBy { it.date }
            NotesSortEnum.DateDescending -> resultNotesList.sortByDescending { it.date }
        }
        return resultNotesList
    }

    override fun getById(noteId: Int): Note {
        for (note in notes) {
            if (note.id == noteId) {
                return note
            }
        }
        throw NoteNotFoundException(noteId)
    }

    override fun getComments(noteId: Int, sort: NotesSortEnum): MutableList<Comment> {
        val resultCommentsList: MutableList<Comment> = ArrayList()
        comments.filter { comment -> comment.nid == noteId }.toCollection(resultCommentsList)
        when (sort) {
            NotesSortEnum.DateAscending -> resultCommentsList.sortBy { it.date }
            NotesSortEnum.DateDescending -> resultCommentsList.sortByDescending { it.date }
        }
        return resultCommentsList

    }

    override fun restoreComment(commentId: Int): Int {
        val comment = getCommentById(commentId)
        if (!comment.isDeleted) {
            throw CommentNotDeletedException(commentId)
        }
        checkNoteNotDeleted(comment.nid)
        comment.isDeleted = false
        return 1
    }
}