package com.example.dilk.notemanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends Activity {

    private ListView listView;
    private List<Note> noteList = new ArrayList<>();
    private int positionInListView;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.notes_list_view) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            positionInListView = info.position;

            String[] menuItems = getResources().getStringArray(R.array.notes_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int menuItemIndex = item.getItemId();

        //delete note
        if (menuItemIndex == 0) {
            FirebaseDatabase.getInstance().getReference().child("notes").child(noteList.get(positionInListView).getId()).removeValue();
            Toast.makeText(getApplicationContext(), "Note deleted", Toast.LENGTH_SHORT).show();
            loadNotes();
        }


        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();

        loadNotes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton createNote;

        listView = findViewById(R.id.notes_list_view);
        createNote = findViewById(R.id.createNote);

        registerForContextMenu(listView);

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNote.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ShowNote.class);
                intent.putExtra("id", noteList.get(position).getId());
                startActivity(intent);
            }
        });

    }

    private void loadNotes(){
        noteList.clear();

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseDatabase.getInstance().getReference().child("notes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Note note = new Note(snapshot.getKey(), Objects.requireNonNull(snapshot.child("title").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("content").getValue()).toString());
                        noteList.add(note);
                    }
                    listView.setAdapter(new NoteListAdapter(getApplicationContext(), 0, noteList));
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
