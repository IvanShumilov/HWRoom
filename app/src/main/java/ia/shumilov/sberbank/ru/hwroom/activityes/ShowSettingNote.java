package ia.shumilov.sberbank.ru.hwroom.activityes;

import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ia.shumilov.sberbank.ru.hwroom.R;
import ia.shumilov.sberbank.ru.hwroom.database.NotesDatabase;
import ia.shumilov.sberbank.ru.hwroom.database.SettingNote;
import ia.shumilov.sberbank.ru.hwroom.database.SettingNoteDAO;

import static ia.shumilov.sberbank.ru.hwroom.activityes.StartActivity.ID_NOTE;

public class ShowSettingNote extends AppCompatActivity {

    private static final String COLORS_ARRAY = "colors";
    private static final String TEXT_SIZE_ARRAY = "textSize";
    private Spinner mSpinner_text_color;
    private Spinner mSpinner_title_color;
    private Spinner mSpinner_text_size;
    private SettingNoteDAO mSettingNoteDAO;
    private int mColorTextBack;
    private int mColorTitleBack;
    private int mSizeText;
    private int mNote_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_note);

        // получаем id записи у которой вызвана форма
        mNote_id = getIntent().getIntExtra(ID_NOTE, 0);

        // инициализируем базу
        initDB();
        // проверяем есть ли запись к note
        checkRowInBD();
        // экзепляр настроек по ноте
        SettingNote settingNote = mSettingNoteDAO.getSettingNoteByNoteId(mNote_id);
        // Получаем экземпляр элемента Spinner
        mSpinner_text_color = findViewById(R.id.setting_note_spinner_color);
        mSpinner_title_color = findViewById(R.id.setting_note_spinner_color_title);
        mSpinner_text_size = findViewById(R.id.setting_note_spinner_textSize);

        // Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Вызываем адаптер
        mSpinner_text_color.setAdapter(adapter);
        Integer position = getSelectedSpin(settingNote.mColorBack, COLORS_ARRAY);
        if (position != null) {
            mSpinner_text_color.setSelection(position);
        }
        mSpinner_title_color.setAdapter(adapter);
        position = getSelectedSpin(settingNote.mColorTitleBack, COLORS_ARRAY);
        if (position != null) {
            mSpinner_title_color.setSelection(position);
        }

        // фдаптер для размера
        adapter = ArrayAdapter.createFromResource(this, R.array.textSize, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // вызываем для текста адаптер
        mSpinner_text_size.setAdapter(adapter);
        position = getSelectedSpin(settingNote.mTextSize, TEXT_SIZE_ARRAY);
        if (position != null) {
            mSpinner_text_size.setSelection(position);
        }

        // обработка нового выбора адаптеров
        mSpinner_text_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mColorTextBack = Color.parseColor(((TextView) view).getText().toString());
                mSpinner_text_color.setBackgroundColor(mColorTextBack);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mColorTextBack = Color.parseColor("#FFFFFF");
            }
        });

        mSpinner_title_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mColorTitleBack = Color.parseColor(((TextView) view).getText().toString());
                mSpinner_title_color.setBackgroundColor(mColorTitleBack);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mColorTitleBack = Color.parseColor("#000000");
            }
        });
        mSpinner_text_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSizeText = Integer.parseInt(((TextView) view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSizeText = 8;
            }
        });


    }

    private void checkRowInBD() {
        int idLast = 1;
        for (SettingNote settingNote : mSettingNoteDAO.getSettingNotes()) {
            idLast = Math.max(idLast, settingNote.id);
            if (settingNote.mNoteID == mNote_id) {
                return;
            }
        }
        mSettingNoteDAO.insert(new SettingNote(idLast+1, mColorTextBack, mColorTitleBack, mSizeText, mNote_id));
    }

    private void initDB() {
        NotesDatabase db = Room.databaseBuilder(getApplicationContext(),
                NotesDatabase.class,
                "notes_database")
                .allowMainThreadQueries()
                .build();
        mSettingNoteDAO = db.getSettingNoteDAO();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SettingNote settingNote = mSettingNoteDAO.getSettingNoteByNoteId(mNote_id);
        if (settingNote != null) {
            settingNote.mColorBack = mColorTextBack;
            settingNote.mColorTitleBack = mColorTitleBack;
            settingNote.mTextSize = mSizeText;
            mSettingNoteDAO.update(settingNote);
        } else {
            int idLast = 1;
            for (SettingNote n : mSettingNoteDAO.getSettingNotes()) {
                idLast = Math.max(idLast, n.id);
            }
            mSettingNoteDAO.insert(new SettingNote(idLast+1, mColorTextBack, mColorTitleBack, mSizeText, mNote_id));
        }
    }

    private Integer getSelectedSpin(int value, String name_array) {
        List<String> array;
        switch (name_array) {
            case "colors": {
                array = Arrays.asList(getResources().getStringArray(R.array.colors));
                for (int i = 0; i < array.size(); i++) {
                    if (value == Color.parseColor(array.get(i))) {
                        return i;
                    }
                }
                break;
            }
            case "textSize": {
                array = Arrays.asList(getResources().getStringArray(R.array.textSize));
                for (int i = 0; i < array.size(); i++) {
                    if (value == Integer.parseInt(array.get(i))) {
                        return i;
                    }
                }
                break;
            }
        }
        return null;
    }
}
