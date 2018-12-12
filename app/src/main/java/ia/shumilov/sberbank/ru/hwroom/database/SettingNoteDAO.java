package ia.shumilov.sberbank.ru.hwroom.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SettingNoteDAO {
    @Query("select * from setting where id = :id")
    SettingNote getSettingNoteById(long id);

    @Query("select * from setting")
    List<SettingNote> getSettingNotes();

    @Update
    void update(SettingNote settingNote);

    @Query("select * from setting where note_id = :id")
    SettingNote getSettingNoteByNoteId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SettingNote settingNote);

    @Delete
    void delete(SettingNote settingNote);
}
