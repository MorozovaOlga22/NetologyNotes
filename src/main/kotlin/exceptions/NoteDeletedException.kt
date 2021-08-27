package exceptions

class NoteDeletedException(noteId: Int) :
    RuntimeException("Note with id $noteId is deleted")