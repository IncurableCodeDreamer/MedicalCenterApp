package com.example.klaudia.medicalcenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.klaudia.medicalcenter.DatabaseModel.Examination;
import com.example.klaudia.medicalcenter.Helper.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNoteActivity extends AppCompatActivity {

    @BindView(R.id.datePicker)
    CalendarView datePicker;
    @BindView(R.id.addNoteButton)
    Button button;
    @BindView(R.id.noteEditText)
    EditText noteEditText;
    Examination examination;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);

        setIconInCalendar();

        Bundle exstras = this.getIntent().getExtras();
        Calendar date = (Calendar) exstras.getSerializable("date");

        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        String strdate = formatter.format(date.getTime());

        try {
            datePicker.setDate(date);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        final DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        if(dbHelper.examinationCheck(strdate)) {
            examination = dbHelper.getExamination(strdate);

            if(examination.getNote() != null && !(examination.getNote().isEmpty())) {
                noteEditText.setText(examination.getNote());
            }
        }

        datePicker.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar cal = datePicker.getFirstSelectedDate();// getSelectedDate();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                String strdate = formatter.format(cal.getTime());
                Examination exam;

                if(dbHelper.examinationCheck(strdate)) {
                    exam = dbHelper.getExamination(strdate);
                    noteEditText.setText(exam.getNote());
                } else {
                    noteEditText.setText(null);
                }
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = datePicker.getSelectedDate();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                String strdate = formatter.format(cal.getTime());
                Examination exam;

                if(dbHelper.examinationCheck(strdate)) {
                    exam = dbHelper.getExamination(strdate);
                    noteEditText.setText(exam.getNote());
                } else {
                    noteEditText.setText(null);
                }

            }
        });

        datePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Calendar cal = datePicker.getFirstSelectedDate();// getSelectedDate();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                String strdate = formatter.format(cal.getTime());
                Examination exam;

                if(dbHelper.examinationCheck(strdate)) {
                    exam = dbHelper.getExamination(strdate);
                    noteEditText.setText(exam.getNote());
                } else {
                    noteEditText.setText(null);
                }
            }
        });

        datePicker.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                Calendar cal = datePicker.getFirstSelectedDate();// getSelectedDate();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                String strdate = formatter.format(cal.getTime());
                Examination exam;

                if(dbHelper.examinationCheck(strdate)) {
                    exam = dbHelper.getExamination(strdate);
                    noteEditText.setText(exam.getNote());
                } else {
                    noteEditText.setText(null);
                }
                return false;
            }
        });

        datePicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar cal = datePicker.getFirstSelectedDate();// getSelectedDate();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                String strdate = formatter.format(cal.getTime());
                Examination exam;

                if(dbHelper.examinationCheck(strdate)) {
                    exam = dbHelper.getExamination(strdate);
                    noteEditText.setText(exam.getNote());
                } else {
                    noteEditText.setText(null);
                }

                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = datePicker.getSelectedDate();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                String strdate = formatter.format(cal.getTime());
                Examination ex = new Examination();

                if(dbHelper.examinationCheck(strdate)) {
                    ex = dbHelper.getExamination(strdate);
                    ex.setNote(noteEditText.getText().toString());
                    ex.setDate(strdate);
                    dbHelper.updateExamination(examination);
                } else {
                    ex.setDate(strdate);
                    ex.setNote(noteEditText.getText().toString());
                    dbHelper.insertExamination(ex);
                }

                Intent intent = new Intent(AddNoteActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setIconInCalendar() {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        List<Examination> examinationList = dbHelper.getAllExamination();
        List<Examination> notes = dbHelper.getAllNotes();
        List <EventDay> events = new ArrayList<>();

        for (Examination e :examinationList) {
            String dateFromExamination = e.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            try {
                Date date = formatter.parse(dateFromExamination);
                Calendar cal=Calendar.getInstance();
                cal.setTime(date);
                events.add(new EventDay(cal, R.drawable.ic_favorite_black_24dp));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }

        for (Examination e :notes) {
            String dateFromExamination = e.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            try {
                Date date = formatter.parse(dateFromExamination);
                Calendar cal=Calendar.getInstance();
                cal.setTime(date);
                events.add(new EventDay(cal, R.drawable.ic_message_black_24dp));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }

        datePicker.setEvents(events);
    }
}