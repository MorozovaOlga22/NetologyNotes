interface NoteService {
    /**
     * Функция создает новую заметку
     */
    fun add(title: String, text: String, privacyView: MutableList<String>, privacyComment: MutableList<String>): Int

    /**
     * Функция добавляет новый комментарий к заметке
     * Если не удалось найти указанную заметку, заметка удалена
     * или комментарий с таким GUID уже добавлен, выбрасывается исключение
     */
    fun createComment(noteId: Int, replyTo: Int?, message: String, guid: String): Int

    /**
     * Функция удаляет заметку
     * Если не удалось найти указанную заметку или заметка удалена, выбрасывается исключение
     */
    fun delete(noteId: Int): Int

    /**
     * Функция удаляет комментарий к заметке
     * Если комментария с указанным commentId не существует, комментарий уже удален,
     * не удалось найти заметку, к которой относится комментарий, или заметка удалена, выбрасывается исключение
     */
    fun deleteComment(commentId: Int): Int

    /**
     * Функция редактирует заметку
     * Если заметка удалена, она восстанавливается
     * Если не удалось найти указанную заметку, выбрасывается исключение
     */
    fun edit(
        noteId: Int,
        title: String,
        text: String,
        privacyView: MutableList<String>,
        privacyComment: MutableList<String>
    ): Int

    /**
     * Функция редактирует указанный комментарий у заметки
     * Если комментарий удален, он восстанавливается
     * Если комментария с указанным commentId не существует,
     * не удалось найти заметку, к которой относится комментарий, или заметка удалена, выбрасывается исключение
     */
    fun editComment(commentId: Int, message: String): Int

    /**
     * Функция возвращает список заметок
     * Если не удалось найти указанную заметку, выбрасывается исключение
     */
    fun get(noteIds: List<Int>, sort: NotesSortEnum): MutableList<Note>

    /**
     * Функция возвращает заметку по её id
     * Возвращаемое значение функции сильно упрощено...
     * Если не удалось найти указанную заметку, выбрасывается исключение
     */
    fun getById(noteId: Int): Note

    /**
     * Функция возвращает список комментариев к заметке (включая удаленные комментарии)
     */
    fun getComments(noteId: Int, sort: NotesSortEnum): MutableList<Comment>

    /**
     * Функция восстанавливает удалённый комментарий
     * Если комментария с указанным commentId не существует, комментарий не удален,
     * не удалось найти заметку, к которой относится комментарий, или заметка удалена, выбрасывается исключение
     */
    fun restoreComment(commentId: Int): Int
}

enum class NotesSortEnum {
    DateDescending, DateAscending
}