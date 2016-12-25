package com.technovanzahackathon.tripistant.TripChecklists;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import com.technovanzahackathon.tripistant.DatabaseHelper;
import com.technovanzahackathon.tripistant.R;
import it.feio.android.checklistview.ChecklistManager;
import it.feio.android.checklistview.exceptions.ViewNotSupportedException;
import it.feio.android.checklistview.interfaces.CheckListChangedListener;
import it.feio.android.checklistview.interfaces.Constants;

public class EditTripChecklistsActivity extends AppCompatActivity implements CheckListChangedListener {

    private static final String EXTRA_NOTE = "EXTRA_CHECK";
    ChecklistManager checklistManager;
    private TextInputLayout inputlayoutTitlec;
    private EditText editTitle;
    View editContent;


    private TripChecklists note;
    View view;
    Toolbar toolbar;
    int i=1;
    NotificationManager mNotificationManager;
    int mNotificationId = 001;
    LinearLayout ll;
    LinearLayout ll1;
    private List<TripChecklists> notesData;
    private DatabaseHelper databaseHelper;
    ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
    int selectedColor;
    String primaryThemeName;
    int preselect = colorGenerator.getRandomColor();
    Boolean save;
    TextView checkUpdated;
    ScrollView scrollView;

    int fav;
    int lock_status;
    String folderLocation;

    boolean amoled;
    boolean navpreff;
    boolean movepref;
    Trips note1;
    private FloatingActionButton saveButton;
    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_checklist);

        SharedPreferences pref6 = PreferenceManager.getDefaultSharedPreferences(this);
        save = pref6.getBoolean("backsave", false);
        SharedPreferences amoledpref = PreferenceManager.getDefaultSharedPreferences(this);
        amoled = amoledpref.getBoolean("amoled_theme", false);
        SharedPreferences navpref = PreferenceManager.getDefaultSharedPreferences(this);
        navpreff = amoledpref.getBoolean("colored_nav_bar", false);
        SharedPreferences move = PreferenceManager.getDefaultSharedPreferences(this);
        movepref = move.getBoolean("move", true);

        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_check);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        inputlayoutTitlec = (TextInputLayout) findViewById(R.id.inputlayoutTitlec);
        editTitle = (EditText) findViewById(R.id.checklist_title);
        editContent = findViewById(R.id.edit_text_description);
        editContent.requestFocus();

        view = findViewById(R.id.scroll_view);

        note = (TripChecklists) getIntent().getSerializableExtra(EXTRA_NOTE);

        note1 = (Trips) getIntent().getSerializableExtra("NEW_TRIP1");

        if (note != null) {
            editTitle.setText(note.getTitle());
            ((EditText)editContent).setText(note.getContent());
            note.getColor();
            note.getLock_status();
        } else {
            note = new TripChecklists();
            note.setUpdated_at(DATETIME_FORMAT.format(new Date()));
        }

        lock_status = note.getLock_status();

        saveButton = (FloatingActionButton) findViewById(R.id.edit_fab);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNoteFormOk()) {
                    setNoteResult();
                    finish();
                } else
                    validateNoteForm();
            }
        });

        toggleTripChecklists();

        ll = (LinearLayout) findViewById(R.id.llmain);
        ll1 = (LinearLayout) findViewById(R.id.ll1);

            selectedColor = preselect;


        ll.setBackgroundColor(note.getColor());
        ll1.setBackgroundColor(note.getColor());
        toolbar.setBackgroundColor(note.getColor());

    }

    private void toggleTripChecklists() {
        try {
            checklistManager = ChecklistManager.getInstance(getApplicationContext());
            checklistManager.setNewEntryHint("New Item");
            checklistManager.setMoveCheckedOnBottom(1);
            checklistManager.setCheckListChangedListener(this);
            checklistManager.setLinesSeparator(Constants.LINES_SEPARATOR);
            checklistManager.setDragEnabled(false);
            checklistManager.setKeepChecked(true);
            checklistManager.setShowChecks(true);
            view = checklistManager.convert(editContent);
            checklistManager.replaceViews(editContent,view);
            editContent = view;
        } catch (ViewNotSupportedException v) {
            v.printStackTrace();
        }
    }

    public String getNoteDescription(){
        String text;
        try{
            text = ((EditText)checklistManager.convert(editContent)).getText().toString();
            return text;
        }catch (ViewNotSupportedException v){
            v.printStackTrace();
            return  "";
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_checklist, menu);

        if(note.getLock_status() == 1){
            menu.findItem(R.id.lock).setIcon(getResources().getDrawable(R.drawable.lock));
            return true;
        }
        menu.findItem(R.id.lock).setIcon(getResources().getDrawable(R.drawable.lock_open_outline));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBack();
                return true;

            case R.id.lock:
                if(databaseHelper.isPasscodeSet()) {
                    if (note.getLock_status() == 0) {
                        item.setIcon(getResources().getDrawable(R.drawable.lock));
                        lock_status = 1;
                        note.setLock_status(lock_status);
                        Toast.makeText(getApplicationContext(),"Locked", Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        item.setIcon(getResources().getDrawable(R.drawable.lock_open_outline));
                        lock_status = 0;
                        note.setLock_status(lock_status);
                        Toast.makeText(getApplicationContext(),"Unlocked", Toast.LENGTH_SHORT).show();
                    }
                }else if(!databaseHelper.isPasscodeSet()){
                    Toast.makeText(getApplicationContext(),"Pass", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.share_note:
                shareTripChecklists();
                return true;

            case R.id.delete_note:
                deleteNote();
                return true;

            case R.id.notify:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Title: "+note.getTitle()+"\nContent: "+ note.getContent());
                sendIntent.setType("text/plain");

                PendingIntent pIntent = PendingIntent.getActivity(EditTripChecklistsActivity.this, 0, sendIntent, 0);

                NotificationCompat.WearableExtender wearableExtender =
                        new NotificationCompat.WearableExtender()
                                .setHintHideIcon(true)
                                .setContentIcon(R.drawable.home);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(EditTripChecklistsActivity.this)
                                .setSmallIcon(R.drawable.home)
                                .setContentTitle(note.getTitle())
                                .setContentText(note.getContent())
                                .setColor(note.getColor())
                                .extend(wearableExtender)
                                .addAction(0,"SHARE NOTE",pIntent)
                                .setVisibility(1);

                NotificationCompat.InboxStyle bigNote = new NotificationCompat.InboxStyle();
                bigNote.setBigContentTitle(note.getTitle());
                String[] nc1 = note.getContent().split("\\r?\\n");
                for (String aNc1 : nc1) {
                    bigNote.addLine(aNc1);
                }
                mBuilder.setStyle(bigNote);

                Intent intent = new Intent(getApplicationContext(), EditTripChecklistsActivity.class);
                intent.putExtra(EXTRA_NOTE, note);
                setResult(RequestResultCode.REQUEST_CODE_VIEW_CHECK,intent);

                android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(EditTripChecklistsActivity.this);
                stackBuilder.addParentStack(EditTripChecklistsActivity.class);
                stackBuilder.addNextIntent(intent);

                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);

                mNotificationManager =
                        (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

                mNotificationManager.notify(mNotificationId, mBuilder.build());
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    public void deleteNote() {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do You Want to Delete the TripChecklist?")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intentHome = new Intent(EditTripChecklistsActivity.this,ChecklistFragment.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentHome.putExtra(EXTRA_NOTE, note);
                        setResult(RequestResultCode.RESULT_CODE_DELETE_CHECK, intentHome);
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private boolean isNoteFormOk() {
        String title=editTitle.getText().toString();
        return !(title==null || title.trim().length()==0);
    }

    private void validateNoteForm() {
        String msg=null;
        if (isNullOrBlank(editTitle.getText().toString())){
            msg="Title Required";
            inputlayoutTitlec.setError("Title is Missing");
        }
        if (msg!=null){
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    private void setNoteResult() {
        note.setTitle(editTitle.getText().toString().trim());
        String str = getNoteDescription();
        note.setContent(str);
        note.setUpdated_at(DATETIME_FORMAT.format(new Date()));
        note.setColor(selectedColor);
        note.setLock_status(lock_status);
        note.setTripNameC("");
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intent);
        Toast.makeText(EditTripChecklistsActivity.this, "TripChecklists Saved.", Toast.LENGTH_LONG).show();
    }

    private void onBack() {
            if (isNoteFormOk()) {
                if ((editTitle.getText().toString().equals(note.getTitle())) && (getNoteDescription().equals(note.getContent())) && lock_status == note.getLock_status()) {
                    setResult(RESULT_CANCELED, new Intent());
                    finish();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Delete")
                            .setMessage("Do You Want to Save the TripChecklist?")
                            .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setNoteResult();
                                    finish();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setResult(RESULT_CANCELED, new Intent());
                                    finish();
                                }
                            })
                            .show();
                }
            }else {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
    }

    private void addNote(Intent data) {
        TripChecklists note = (TripChecklists) data.getSerializableExtra(EXTRA_NOTE);
        long noteId = databaseHelper.createTrip(note);
        note.setId(noteId);
    }

    @Override
    public void onBackPressed() {
        onBack();
        Intent intentHome = new Intent(EditTripChecklistsActivity.this, ChecklistFragment.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentHome.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intentHome);
    }

    private void shareTripChecklists(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Title: "+note.getTitle()+"\nContent: "+note.getContent());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    @Override
    public void onCheckListChanged() {
        Log.v(Constants.TAG,"Text is Changed");
    }

}
