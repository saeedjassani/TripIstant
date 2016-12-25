package com.technovanzahackathon.tripistant.TripChecklists;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.technovanzahackathon.tripistant.DatabaseHelper;
import com.technovanzahackathon.tripistant.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Pooja S on 12/24/2016.
 */

public class ChecklistFragment extends AppCompatActivity {
    private static final int REQUEST_STORAGE_PERMISSION = 0;
    private TextView textEmpty;
    private TextView textEmpty1;
    private List<TripChecklists> notesData;
    private TripChecklistsAdapter checkAdapter;
    private static Context context;
    private DatabaseHelper databaseHelper;
    String ttsContent;

    private SearchView searchView;
    private MenuItem searchMenuItem;

    int mNotificationId = 001;

    public Toolbar toolbar;

    private final static String MENU_SELECTED = "selected";
    private int selected = -1;

    private RecyclerView recyclerView;

    private Boolean fabdrwble;
    private Boolean grid1;
    private Boolean bottombar;
    private Boolean scroll;
    private Boolean swipe;
    private Boolean quickl;
    String sortl;
    boolean amoled;

    FloatingActionButton fab;
    CoordinatorLayout coordinatorLayout;
    LinearLayout ll1;

    String check = "checklist";
    String check1 = "checklist1";

    String folderLocation;
    int exportTextPos;

    String STAGGER_CONTENT = "EXTRA CONTENT";
    String primaryThemeName;
    Trips note1;
    private static final String EXTRA_NOTE = "EXTRA_CHECK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist_fragment);

        context = getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar_checklist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        note1 = (Trips) getIntent().getSerializableExtra("NEW_TRIP");

        databaseHelper = new DatabaseHelper(getApplicationContext());

        textEmpty = (TextView) findViewById(R.id.textEmpty);
        textEmpty1 = (TextView) findViewById(R.id.textEmpty1);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clayout);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        recyclerView = (RecyclerView) findViewById(R.id.listNotes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupTripChecklistsAdapter();
        updateView();
        sortList1(TripChecklistsAdapter.newestFirstComparator);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChecklistFragment.this, EditTripChecklistsActivity.class);
                intent.putExtra("NEW_TRIP1",note1);
                startActivityForResult(intent, RequestResultCode.REQUEST_CODE_ADD_CHECK);
            }
        });


        setListeners();

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void sortList1(Comparator<TripChecklists> noteComparator) {
        Collections.sort(notesData, noteComparator);
        checkAdapter.notifyDataSetChanged();
    }

    private void setListeners() {
        TripChecklistsAdapter.setOnItemClickListener(new TripChecklistsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                    final TripChecklists note = notesData.get(position);
                    Intent intent=new Intent(ChecklistFragment.this, EditTripChecklistsActivity.class);
                    intent.putExtra(EXTRA_NOTE, note);
                    startActivityForResult(intent, RequestResultCode.REQUEST_CODE_EDIT_CHECK);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        checkAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(MENU_SELECTED, selected);
        super.onSaveInstanceState(savedInstanceState);
    }

    public static Context getContext() {
        return context;
    }

    private void setupTripChecklistsAdapter() {
        notesData = databaseHelper.getAllTrips(note1.getTripname());
        checkAdapter = new TripChecklistsAdapter(notesData,context,ChecklistFragment.this);
        recyclerView.setAdapter(checkAdapter);
    }

    private void updateView() {
        if (notesData.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textEmpty.setVisibility(View.VISIBLE);
            textEmpty1.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textEmpty.setVisibility(View.GONE);
            textEmpty1.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== RequestResultCode.REQUEST_CODE_EDIT_CHECK){
            if (resultCode==RESULT_OK){
                Log.d("TripChecklistsActivity", "RESULT_OK");
                TripChecklists note1 = (TripChecklists) data.getSerializableExtra(EXTRA_NOTE);
                note1.getTitle();
                note1.getContent();
            }
        }
        if (requestCode == RequestResultCode.REQUEST_CODE_EDIT_CHECK) {
            if (resultCode == RESULT_OK) {
                updateNote(data);
                checkAdapter.notifyDataSetChanged();
            } else if (resultCode == RequestResultCode.RESULT_CODE_DELETE_CHECK) {
                deleteNote(data);
            }
        }
        if (requestCode == RequestResultCode.REQUEST_CODE_ADD_CHECK) {
            if (resultCode == RESULT_OK) {
                TripChecklists note = (TripChecklists) data.getSerializableExtra(EXTRA_NOTE);
                long noteId = databaseHelper.createTrip(note);
                note.setId(noteId);
                notesData.add(0,note);
                updateView();
                checkAdapter.notifyDataSetChanged();
            }
        }else if(resultCode == RESULT_FIRST_USER){
            addNote(data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addNote(Intent data) {
        TripChecklists note = (TripChecklists) data.getSerializableExtra(EXTRA_NOTE);
        /*long noteId = databaseHelper.createNote(note);
        note.setId(noteId);*/
        if(sortl.equals("Newest First") || sortl.equals("Sort By Title(Aescending)") || sortl.equals("Sort By Title(Descending)")){
            notesData.add(0,note);
        }else {
            notesData.add(note);
        }
        updateView();
        checkAdapter.notifyDataSetChanged();
    }

    private void updateNote(Intent data) {
        TripChecklists updatedNote = (TripChecklists) data.getSerializableExtra(EXTRA_NOTE);
        databaseHelper.updateTrip(updatedNote);
        for (TripChecklists note : notesData) {
            if (note.getId().equals(updatedNote.getId())) {
                note.setTitle(updatedNote.getTitle());
                note.setContent(updatedNote.getContent());
                note.setUpdated_at(updatedNote.getUpdated_at());
                note.setColor(updatedNote.getColor());
                note.setLock_status(updatedNote.getLock_status());
            }
        }
        checkAdapter.notifyDataSetChanged();
    }

    private void deleteNote(Intent data) {
        TripChecklists deletedNote = (TripChecklists) data.getSerializableExtra(EXTRA_NOTE);
        databaseHelper.deleteTrip(deletedNote);
        notesData.remove(deletedNote);
        updateView();
        checkAdapter.notifyDataSetChanged();
        Toast.makeText(ChecklistFragment.this, "TripChecklists Deleted.", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }

}