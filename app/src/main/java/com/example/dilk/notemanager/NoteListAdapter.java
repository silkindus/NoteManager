package com.example.dilk.notemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NoteListAdapter extends ArrayAdapter<Note> {

    private List<Note> noteData;

    NoteListAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);

        noteData = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        Note currentNote = noteData.get(position);

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_note_list_adapter, parent, false);
        }

        TextView title = listItemView.findViewById(R.id.title);
        TextView content = listItemView.findViewById(R.id.content);

        title.setText(currentNote.getTitle());
        content.setText(currentNote.getContent());

        return listItemView;
    }
}


