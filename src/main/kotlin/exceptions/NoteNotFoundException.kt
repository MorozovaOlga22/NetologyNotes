package exceptions

class NoteNotFoundException(noteId: Int) :
    RuntimeException("Note with id $noteId not found")