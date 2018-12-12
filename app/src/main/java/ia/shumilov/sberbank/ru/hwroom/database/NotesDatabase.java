package ia.shumilov.sberbank.ru.hwroom.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Notes.class, SettingNote.class}, version = 2)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NoteDAO getNoteDAO();
    public abstract SettingNoteDAO getSettingNoteDAO();
}
