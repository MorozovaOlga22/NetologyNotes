В методах не используются следующие поля:

createComment, deleteComment, editComment и restoreComment:
1. owner_id (в текущей реализации для идентификации достаточно note_id или comment_id)

get и getComments:
1. user_id/owner_id (в текущей реализации для идентификации достаточно note_id)
2. offset и count (не представляю, что делать с этими параметрами...)

getById:
1. owner_id (в текущей реализации для идентификации достаточно note_id)
2. need_wiki (в текущей реализации нет способа определить id текущего пользователя)