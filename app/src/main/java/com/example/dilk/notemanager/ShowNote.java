package com.example.dilk.notemanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ShowNote extends Activity {

    private String noteId;
    private TextView titleText;
    private TextView contentText;
    private EditText titleEdit;
    private EditText contentEdit;
    private Button edit;
    private Button update;
    private boolean isEdit = false;
    private ConstraintLayout questionConstraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        Button deleteNote;
        Button yes;
        Button no;

        titleText = findViewById(R.id.title);
        contentText = findViewById(R.id.content);
        titleEdit = findViewById(R.id.titleEdit);
        contentEdit = findViewById(R.id.contentEdit);
        edit = findViewById(R.id.edit);
        update = findViewById(R.id.update);
        deleteNote = findViewById(R.id.delete);
        questionConstraint = findViewById(R.id.question_constraint);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);

        noteId = Objects.requireNonNull(getIntent().getExtras()).getString("id");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;

                loadData();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = titleEdit.getText().toString();
                String newContent = contentEdit.getText().toString();

                FirebaseDatabase.getInstance().getReference().child("notes").child(noteId).child("title").setValue(newTitle);
                FirebaseDatabase.getInstance().getReference().child("notes").child(noteId).child("content").setValue(newContent);

                isEdit = false;

                loadData();
            }
        });

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionConstraint.setVisibility(View.VISIBLE);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("notes").child(noteId).removeValue();
                Toast.makeText(getApplicationContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionConstraint.setVisibility(View.INVISIBLE);
            }
        });

        loadData();
    }

    private void loadData() {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ShowNote.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseDatabase.getInstance().getReference().child("notes").child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString();
                    String content = Objects.requireNonNull(dataSnapshot.child("content").getValue()).toString();
                    titleText.setText(title);
                    contentText.setText(content);
                    titleEdit.setText(title);
                    contentEdit.setText(content);

                    if (isEdit) {
                        titleText.setVisibility(View.GONE);
                        contentText.setVisibility(View.GONE);
                        titleEdit.setVisibility(View.VISIBLE);
                        contentEdit.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.GONE);
                        update.setVisibility(View.VISIBLE);
                    } else {
                        titleText.setVisibility(View.VISIBLE);
                        contentText.setVisibility(View.VISIBLE);
                        titleEdit.setVisibility(View.GONE);
                        contentEdit.setVisibility(View.GONE);
                        edit.setVisibility(View.VISIBLE);
                        update.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Note does not longer exists", Toast.LENGTH_SHORT).show();
                    finish();
                }

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

