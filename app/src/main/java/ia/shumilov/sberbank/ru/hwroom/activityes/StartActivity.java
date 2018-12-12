package ia.shumilov.sberbank.ru.hwroom.activityes;

import android.app.ListActivity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ia.shumilov.sberbank.ru.hwroom.R;
import ia.shumilov.sberbank.ru.hwroom.database.NoteDAO;
import ia.shumilov.sberbank.ru.hwroom.database.Notes;
import ia.shumilov.sberbank.ru.hwroom.database.NotesDatabase;
import ia.shumilov.sberbank.ru.hwroom.database.SettingNote;

public class StartActivity extends ListActivity {

    private NotesDatabase mDataBase;
    private NoteDAO mNoteDAO;
    public static final String ID_NOTE = "id_note_in_bd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create BD
        mDataBase = Room.databaseBuilder(getApplicationContext(),
                NotesDatabase.class,
                "notes_database")
                .allowMainThreadQueries()
                .build();
        mNoteDAO = mDataBase.getNoteDAO();
//        add item in mDataBase
        if (mNoteDAO.getNotes().size() < 1) {
            for (int i = 1; i < 10; i++) {
                mNoteDAO.insert(new Notes(i, "First Note " + i, "mText number one" + i));
            }
        }
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, ShowNote.class);
        if (((TextView) v).getText().toString().equalsIgnoreCase(getResources().getString(R.string.btnAddNote))) {
            intent.putExtra(ID_NOTE, -1);
        } else {
            intent.putExtra(ID_NOTE, getIdNote(((TextView) v).getText().toString()));
        }
        startActivity(intent);
    }

    private int getIdNote(String title) {
        for (Notes notes : mNoteDAO.getNotes()) {
            if (title.equalsIgnoreCase(notes.mTitle)) {
                return notes.id;
            }
        }
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        create adapter
        List<String> listTitle = new ArrayList<>();
        listTitle.add(getResources().getString(R.string.btnAddNote) );
        for (Notes notes : mNoteDAO.getNotes()) {
            listTitle.add(notes.mTitle);
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listTitle);

//        show adapter
        setListAdapter(mAdapter);

    }
}
