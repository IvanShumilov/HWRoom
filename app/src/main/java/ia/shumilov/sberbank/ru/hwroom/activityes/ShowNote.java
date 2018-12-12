package ia.shumilov.sberbank.ru.hwroom.activityes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ia.shumilov.sberbank.ru.hwroom.R;
import ia.shumilov.sberbank.ru.hwroom.database.NoteDAO;
import ia.shumilov.sberbank.ru.hwroom.database.Notes;
import ia.shumilov.sberbank.ru.hwroom.database.NotesDatabase;
import ia.shumilov.sberbank.ru.hwroom.database.SettingNote;
import ia.shumilov.sberbank.ru.hwroom.database.SettingNoteDAO;

import static ia.shumilov.sberbank.ru.hwroom.activityes.StartActivity.ID_NOTE;

public class ShowNote extends AppCompatActivity {

    private int mIDNote;
    private EditText mTextView;
    private EditText mTextViewTitle;
    private Button mChangeBtn;
    private Button mDeleteBtn;
    private Button mSettingBtn;
    private boolean mChange = false;
    private NoteDAO mNoteDAO;
    private SettingNoteDAO mSettingDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);
        mTextView = findViewById(R.id.textViewShowNote);
        mTextViewTitle = findViewById(R.id.textViewShowNoteTitle);
        mChangeBtn = findViewById(R.id.Save_ChangeShowActBtn);
        mDeleteBtn = findViewById(R.id.Delete_ChangeShowActBtn);
        mSettingBtn = findViewById(R.id.Setting_ChangeShowActBtn);

        mIDNote = getIntent().getIntExtra(ID_NOTE, -1);

        NotesDatabase db = Room.databaseBuilder(getApplicationContext(),
                NotesDatabase.class,
                "notes_database")
                .allowMainThreadQueries()
                .build();
        mNoteDAO = db.getNoteDAO();
        mSettingDAO = db.getSettingNoteDAO();


        prepareTextView();

        mChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBtnName();
                setTextViewChange(mChange);
            }
        });

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoteDAO.delete(mNoteDAO.getNoteById(mIDNote));
                mSettingDAO.delete(mSettingDAO.getSettingNoteByNoteId(mIDNote));
                back(StartActivity.class);
            }
        });

        mSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowNote.this, ShowSettingNote.class);
                intent.putExtra(ID_NOTE, mIDNote);
                startActivity(intent);
            }
        });

    }

    private void back(Class cl) {
        startActivity(new Intent(ShowNote.this, cl));
    }

    private void prepareTextView() {
        if (mIDNote < 0) {
            mDeleteBtn.setEnabled(false);
            mSettingBtn.setEnabled(false);
            mChange = true;
            changeBtnName();
        } else {
            mDeleteBtn.setEnabled(true);
            mSettingBtn.setEnabled(true);
            setTextViewChange(mChange);
            mTextView.setText(mNoteDAO.getNoteById(mIDNote).mText);
            mTextViewTitle.setText(mNoteDAO.getNoteById(mIDNote).mTitle.toUpperCase());
        }
    }

    //    lock / unlock mChange mTextView
    private void setTextViewChange(boolean b) {
        mTextView.setFocusable(b);
        mTextView.setFocusableInTouchMode(b);
        mTextViewTitle.setFocusableInTouchMode(b);
        mTextViewTitle.setFocusable(b);
    }

    private void changeBtnName() {
        if (mChangeBtn.getText().equals(getResources().getString(R.string.change))) {
            mChangeBtn.setText(R.string.save);
            mChange = true;
        } else {
            if (mIDNote > 0) {
                mChangeBtn.setText(R.string.change);
                mChange = false;
            }
            saveData();
        }
    }

    private void saveData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mIDNote < 0) {
                    int idLast = 0;
                    for (Notes n : mNoteDAO.getNotes()) {
                        idLast = Math.max(idLast, n.id);
                    }
                    mNoteDAO.insert(new Notes(idLast + 1, mTextViewTitle.getText().toString(), mTextView.getText().toString()));
                    back(StartActivity.class);
                } else {
                    Notes notes = mNoteDAO.getNoteById(mIDNote);
                    notes.mTitle = mTextViewTitle.getText().toString();
                    notes.mText = mTextView.getText().toString();
                    mNoteDAO.update(notes);
                }
            }
        }).start();
    }

    private void checkSetting() {
        SettingNote settingNote = mSettingDAO.getSettingNoteByNoteId(mIDNote);
        if (settingNote != null) {
            mTextView.setBackgroundColor(settingNote.mColorBack);
            mTextViewTitle.setBackgroundColor(settingNote.mColorTitleBack);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingNote.mTextSize);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIDNote > 0)
            checkSetting();
    }
}