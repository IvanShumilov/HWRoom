package ia.shumilov.sberbank.ru.hwroom.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface NoteDAO {
    @Query("select * from notes where id = :id")
    Notes getNoteById(long id);

    @Query("select * from notes")
    List<Notes> getNotes();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Notes notes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Notes notes);

    @Delete
    void delete(Notes notes);

    @Query("select * from notes")
    Cursor getNotesCursor();

}
