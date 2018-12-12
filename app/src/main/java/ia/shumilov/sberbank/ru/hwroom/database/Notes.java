package ia.shumilov.sberbank.ru.hwroom.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import java.util.UUID;

@Entity(tableName = "notes", primaryKeys = "id")
public class Notes {
    public int id;
    public String mTitle;
    public String mText;

    public Notes(int id, String mTitle, String mText) {
        this.id = id;
        this.mTitle = mTitle;
        this.mText = mText;
    }

    @Ignore
    public Notes(String mTitle, String mText) {
        id =  Integer.parseInt(UUID.randomUUID().toString());
        this.mTitle = mTitle;
        this.mText = mText;
    }
}
