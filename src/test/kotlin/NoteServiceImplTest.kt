import exceptions.*
import org.junit.Test

import org.junit.Assert.*
import kotlin.collections.ArrayList

class NoteServiceImplTest {

    @Test
    fun add() {
        val noteServiceImpl = NoteServiceImpl()
        val addedNodeId = noteServiceImpl.add(
            title = "anyTitle",
            text = "anyText",
            privacyView = ArrayList(),
            privacyComment = ArrayList()
        )

        assertEquals(1, noteServiceImpl.notes.size)
        val actualNote = noteServiceImpl.notes[0]
        val expectedNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = actualNote.date
        )
        assertEquals(expectedNote, actualNote)
        assertEquals(0, addedNodeId)
        assertEquals(1, noteServiceImpl.nextNoteId)
    }

    @Test
    fun createComment() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val createdCommentId = noteServiceImpl.createComment(
            noteId = 0,
            replyTo = null,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde"
        )

        assertEquals(1, noteServiceImpl.comments.size)
        val actualComment = noteServiceImpl.comments[0]
        val expectedComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = actualComment.date
        )
        assertEquals(expectedComment, actualComment)
        assertEquals(0, createdCommentId)
        assertEquals(1, noteServiceImpl.nextCommentId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun createComment_badNoteId() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        noteServiceImpl.createComment(
            noteId = 5,
            replyTo = null,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde"
        )
    }

    @Test(expected = NoteDeletedException::class)
    fun createComment_deletedNote() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        anyNote.isDeleted = true
        noteServiceImpl.notes.add(anyNote)

        noteServiceImpl.createComment(
            noteId = 0,
            replyTo = null,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde"
        )
    }

    @Test(expected = SameGuidException::class)
    fun createComment_sameGuid() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        noteServiceImpl.createComment(
            noteId = 0,
            replyTo = null,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde"
        )

        noteServiceImpl.createComment(
            noteId = 0,
            replyTo = null,
            message = "anyMessage2",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde"
        )
    }

    @Test
    fun delete() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val deleteResult = noteServiceImpl.delete(noteId = 0)
        assertTrue(anyNote.isDeleted)
        assertEquals(1, deleteResult)
    }

    @Test(expected = NoteNotFoundException::class)
    fun delete_badNoteId() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)
        noteServiceImpl.delete(noteId = 5)
    }

    @Test(expected = NoteDeletedException::class)
    fun delete_deletedNote() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        anyNote.isDeleted = true
        noteServiceImpl.notes.add(anyNote)
        noteServiceImpl.delete(noteId = 0)
    }

    @Test
    fun deleteComment() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        noteServiceImpl.comments.add(anyComment)

        val deletedCommentId = noteServiceImpl.deleteComment(commentId = 0)
        assertTrue(anyComment.isDeleted)
        assertEquals(1, deletedCommentId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun deleteComment_badNoteId() {
        val noteServiceImpl = NoteServiceImpl()
        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.deleteComment(commentId = 0)
    }

    @Test(expected = NoteDeletedException::class)
    fun deleteComment_deletedNote() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        anyNote.isDeleted = true
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.deleteComment(commentId = 0)
    }

    @Test(expected = CommentNotFoundException::class)
    fun deleteComment_badCommentId() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.deleteComment(commentId = 1)
    }

    @Test(expected = CommentDeletedException::class)
    fun deleteComment_deletedComment() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        anyComment.isDeleted = true
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.deleteComment(commentId = 0)
    }

    @Test
    fun edit() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val editResult = noteServiceImpl.edit(
            noteId = 0,
            title = "anotherTitle",
            text = "anotherText",
            privacyView = ArrayList(),
            privacyComment = ArrayList()
        )
        assertEquals("anotherTitle", anyNote.title)
        assertEquals("anotherText", anyNote.text)
        assertEquals(1, editResult)
    }

    @Test
    fun edit_deletedNote() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        anyNote.isDeleted = true
        noteServiceImpl.notes.add(anyNote)

        val editResult = noteServiceImpl.edit(
            noteId = 0,
            title = "anotherTitle",
            text = "anotherText",
            privacyView = ArrayList(),
            privacyComment = ArrayList()
        )
        assertEquals("anotherTitle", anyNote.title)
        assertEquals("anotherText", anyNote.text)
        assertFalse(anyNote.isDeleted)
        assertEquals(1, editResult)
    }

    @Test(expected = NoteNotFoundException::class)
    fun edit_withBadId() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        noteServiceImpl.edit(
            noteId = 5,
            title = "anotherTitle",
            text = "anotherText",
            privacyView = ArrayList(),
            privacyComment = ArrayList()
        )
    }

    @Test
    fun editComment() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        noteServiceImpl.comments.add(anyComment)

        val editedCommentId = noteServiceImpl.editComment(
            commentId = 0,
            message = "anotherMessage"
        )
        assertEquals("anotherMessage", anyComment.message)
        assertEquals(1, editedCommentId)
    }

    @Test
    fun editComment_deleteComment() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        anyComment.isDeleted = true
        noteServiceImpl.comments.add(anyComment)

        val editedCommentId = noteServiceImpl.editComment(
            commentId = 0,
            message = "anotherMessage"
        )
        assertEquals("anotherMessage", anyComment.message)
        assertFalse(anyComment.isDeleted)
        assertEquals(1, editedCommentId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun editComment_badNoteId() {
        val noteServiceImpl = NoteServiceImpl()
        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.editComment(
            commentId = 0,
            message = "anotherMessage"
        )
    }

    @Test(expected = NoteDeletedException::class)
    fun editComment_deletedNote() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        anyNote.isDeleted = true
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.editComment(
            commentId = 0,
            message = "anotherMessage"
        )
    }

    @Test(expected = CommentNotFoundException::class)
    fun editComment_badCommentId() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.editComment(
            commentId = 1,
            message = "anotherMessage"
        )
    }

    @Test
    fun get_dateAscendingSorting() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        val anyNote1 = createTestNote(
            noteId = 1,
            title = "anyTitle1",
            text = "anyText1",
            date = 1
        )
        val anyNote2 = createTestNote(
            noteId = 2,
            title = "anyTitle2",
            text = "anyText2",
            date = 2
        )

        noteServiceImpl.notes.add(anyNote)
        noteServiceImpl.notes.add(anyNote1)
        noteServiceImpl.notes.add(anyNote2)

        val resultNotes = noteServiceImpl.get(
            noteIds = listOf(0, 2),
            sort = NotesSortEnum.DateAscending
        )

        assertEquals(2, resultNotes.size)
        assertEquals(anyNote, resultNotes[0])
        assertEquals(anyNote2, resultNotes[1])
    }

    @Test
    fun get_dateDescendingSorting() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        val anyNote1 = createTestNote(
            noteId = 1,
            title = "anyTitle1",
            text = "anyText1",
            date = 1
        )
        val anyNote2 = createTestNote(
            noteId = 2,
            title = "anyTitle2",
            text = "anyText2",
            date = 2
        )

        noteServiceImpl.notes.add(anyNote)
        noteServiceImpl.notes.add(anyNote1)
        noteServiceImpl.notes.add(anyNote2)

        val resultNotes = noteServiceImpl.get(
            noteIds = listOf(0, 2),
            sort = NotesSortEnum.DateDescending
        )

        assertEquals(2, resultNotes.size)
        assertEquals(anyNote2, resultNotes[0])
        assertEquals(anyNote, resultNotes[1])
    }

    @Test(expected = NoteNotFoundException::class)
    fun get_badIds() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        val anyNote1 = createTestNote(
            noteId = 1,
            title = "anyTitle1",
            text = "anyText1",
            date = 1
        )
        val anyNote2 = createTestNote(
            noteId = 2,
            title = "anyTitle2",
            text = "anyText2",
            date = 2
        )

        noteServiceImpl.notes.add(anyNote)
        noteServiceImpl.notes.add(anyNote1)
        noteServiceImpl.notes.add(anyNote2)

        noteServiceImpl.get(
            noteIds = listOf(0, 5),
            sort = NotesSortEnum.DateDescending
        )
    }

    @Test
    fun getById() {
        val noteServiceImpl = NoteServiceImpl()
        val expectedNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(expectedNote)

        val actualNote = noteServiceImpl.getById(noteId = 0)
        assertEquals(expectedNote, actualNote)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getById_badId() {
        val noteServiceImpl = NoteServiceImpl()
        val expectedNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(expectedNote)

        noteServiceImpl.getById(noteId = 5)
    }

    @Test
    fun getComments_dateAscendingSorting() {
        val noteServiceImpl = NoteServiceImpl()
        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        val anyComment1 = createTestComment(
            commentId = 1,
            noteId = 0,
            message = "anyMessage1",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 1
        )
        val anyComment2 = createTestComment(
            commentId = 2,
            noteId = 0,
            message = "anyMessage2",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 2
        )

        noteServiceImpl.comments.add(anyComment)
        noteServiceImpl.comments.add(anyComment1)
        noteServiceImpl.comments.add(anyComment2)

        val comments = noteServiceImpl.getComments(noteId = 0, sort = NotesSortEnum.DateAscending)

        assertEquals(3, comments.size)
        assertEquals(anyComment, comments[0])
        assertEquals(anyComment1, comments[1])
        assertEquals(anyComment2, comments[2])
    }

    @Test
    fun getComments_dateDescendingSorting() {
        val noteServiceImpl = NoteServiceImpl()
        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        val anyComment1 = createTestComment(
            commentId = 1,
            noteId = 0,
            message = "anyMessage1",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 1
        )
        val anyComment2 = createTestComment(
            commentId = 2,
            noteId = 0,
            message = "anyMessage2",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 2
        )

        noteServiceImpl.comments.add(anyComment)
        noteServiceImpl.comments.add(anyComment1)
        noteServiceImpl.comments.add(anyComment2)

        val comments = noteServiceImpl.getComments(noteId = 0, sort = NotesSortEnum.DateDescending)

        assertEquals(3, comments.size)
        assertEquals(anyComment2, comments[0])
        assertEquals(anyComment1, comments[1])
        assertEquals(anyComment, comments[2])
    }

    @Test
    fun getComments_noComments() {
        val noteServiceImpl = NoteServiceImpl()
        val comments = noteServiceImpl.getComments(noteId = 0, sort = NotesSortEnum.DateDescending)

        assertEquals(0, comments.size)
    }

    @Test
    fun restoreComment() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        anyComment.isDeleted = true
        noteServiceImpl.comments.add(anyComment)

        val restoredCommentId = noteServiceImpl.restoreComment(
            commentId = 0
        )
        assertFalse(anyComment.isDeleted)
        assertEquals(1, restoredCommentId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun restoreComment_badNoteId() {
        val noteServiceImpl = NoteServiceImpl()
        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        anyComment.isDeleted = true
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.restoreComment(
            commentId = 0
        )
    }

    @Test(expected = NoteDeletedException::class)
    fun restoreComment_deletedNote() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        anyNote.isDeleted = true
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        anyComment.isDeleted = true
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.restoreComment(
            commentId = 0
        )
    }

    @Test(expected = CommentNotFoundException::class)
    fun restoreComment_badCommentId() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        anyComment.isDeleted = true
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.restoreComment(
            commentId = 1
        )
    }

    @Test(expected = CommentNotDeletedException::class)
    fun restoreComment_notDeletedComment() {
        val noteServiceImpl = NoteServiceImpl()
        val anyNote = createTestNote(
            noteId = 0,
            title = "anyTitle",
            text = "anyText",
            date = 0
        )
        noteServiceImpl.notes.add(anyNote)

        val anyComment = createTestComment(
            commentId = 0,
            noteId = 0,
            message = "anyMessage",
            guid = "eaa509f0-44af-465b-a0b1-0bc3c8104bde",
            date = 0
        )
        noteServiceImpl.comments.add(anyComment)

        noteServiceImpl.restoreComment(
            commentId = 0
        )
    }

    private fun createTestNote(noteId: Int, title: String, text: String, date: Long) = Note(
        id = noteId,
        ownerId = 0,
        title = title,
        text = text,
        date = date,
        comments = 0,
        readComments = null,
        viewUrl = "",
        privacyView = ArrayList(),
        privacyComment = ArrayList(),
        canComment = true,
        textWiki = ""
    )

    @Suppress("SameParameterValue")
    private fun createTestComment(commentId: Int, noteId: Int, message: String, guid: String, date: Long) = Comment(
        id = commentId,
        uid = 0,
        nid = noteId,
        oid = 0,
        date = date,
        message = message,
        replyTo = null,
        guid = guid,
        isDeleted = false
    )
}