package ia.shumilov.sberbank.ru.hwroom.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity (tableName = "setting", primaryKeys = "id")
public class SettingNote {

    public int id;
    public int mColorBack;
    public int mColorTitleBack;
    public int mTextSize;
    @ColumnInfo(name = "note_id")
    public int mNoteID;

    public SettingNote(int id, int mColorBack, int mColorTitleBack, int mTextSize, int mNoteID) {
        this.id = id;
        this.mColorBack = mColorBack;
        this.mColorTitleBack = mColorTitleBack;
        this.mTextSize = mTextSize;
        this.mNoteID = mNoteID;
    }
}
