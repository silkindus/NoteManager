package com.example.dilk.notemanager;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class CreateNote extends Activity {

    private EditText titleEdit;
    private EditText contentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Button createNote;

        createNote = findViewById(R.id.create);
        titleEdit = findViewById(R.id.titleEdit);
        contentEdit = findViewById(R.id.contentEdit);

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToCloud();
            }
        });
    }

    private void writeToCloud(){
        String title = titleEdit.getText().toString();
        String content = contentEdit.getText().toString();

        if (title.isEmpty() || content.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill whole note", Toast.LENGTH_SHORT).show();
        }else {
            String pushKey = FirebaseDatabase.getInstance().getReference().child("notes").push().getKey();
            assert pushKey != null;
            FirebaseDatabase.getInstance().getReference().child("notes").child(pushKey).child("title").setValue(title);
            FirebaseDatabase.getInstance().getReference().child("notes").child(pushKey).child("content").setValue(content);

            Toast.makeText(getApplicationContext(), "Note created", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

