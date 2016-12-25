package com.technovanzahackathon.tripistant.Expense;

import android.content.*;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;
import com.technovanzahackathon.tripistant.DatabaseHelper;
import com.technovanzahackathon.tripistant.R;
import com.technovanzahackathon.tripistant.TripChecklists.ColorGenerator;
import com.technovanzahackathon.tripistant.TripChecklists.RequestResultCode;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Pooja S on 10/1/2016.
 */

public class NewExpenseActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE = "EXTRA_NOTE";
    private ActionMode mActionMode;
    public TextView editTitle;
    public TextInputLayout inputlayoutContent, inputLayoutAmount;
    public EditText editContent, amount;
    private Expense note;
    private TextView editNote;

    Toolbar toolbar;
    private Boolean fabdrwble;
    private List<Expense> notesData;
    private DatabaseHelper databaseHelper;

    private Boolean save;
    private FloatingActionButton saveButton;
    private static final int SPEECH_REQUEST_CODE = 0;
    private ColorStateList tint;

    ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
    int selectedColor;
    int preselect = colorGenerator.getRandomColor();

    LinearLayout ll;
    LinearLayout ll1;

    Long mRowId;

    Spinner spinner;
    String spinnertext;
    boolean amoled;

    int fav;
    TextView cat;

    boolean mSharedFromIntentFilter = false;
    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        inputlayoutContent = (TextInputLayout) findViewById(R.id.inputlayoutContent);
        inputLayoutAmount = (TextInputLayout) findViewById(R.id.inputLayoutAmount);
        editTitle = (TextView) findViewById(R.id.note_title);
        editContent = (EditText) findViewById(R.id.note_content);
        amount = (EditText) findViewById(R.id.amount);


        cat = (TextView) findViewById(R.id.buttoncate);
        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {
                        "Expense", "Saving"
                };

                final CharSequence[] items_expense = {
                    "Clothing","E-Commerce","Extra","Food", "Movies", "Stocks"
                };

                final CharSequence[] items_savings = {
                        "Bank","Deposit","Extras","Gift"
                };

                new AlertDialog.Builder(NewExpenseActivity.this)
                        .setTitle("Make your selection")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item){
                                    case 0:
                                        new AlertDialog.Builder(NewExpenseActivity.this)
                                                .setTitle("Make your selection")
                                                .setItems(items_expense, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int item) {
                                                        switch (item){
                                                            case 0:
                                                                editTitle.setText("Clothing");
                                                                break;

                                                            case 1:
                                                                editTitle.setText("E-Commerce");
                                                                break;

                                                            case 2:
                                                                editTitle.setText("Extra");
                                                                break;

                                                            case 3:
                                                                editTitle.setText("Food");
                                                                break;

                                                            case 4:
                                                                editTitle.setText("Movies");
                                                                break;

                                                            case 5:
                                                                editTitle.setText("Stocks");
                                                                break;
                                                        }
                                                    }
                                                }).show();
                                        break;

                                    case 1:
                                        new AlertDialog.Builder(NewExpenseActivity.this)
                                                .setTitle("Make your selection")
                                                .setItems(items_savings, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int item) {
                                                        switch (item){
                                                            case 0:
                                                                editTitle.setText("Bank");
                                                                break;

                                                            case 1:
                                                                editTitle.setText("Deposit");
                                                                break;

                                                            case 2:
                                                                editTitle.setText("Extras (Savings)");
                                                                break;

                                                            case 3:
                                                                editTitle.setText("Gift");
                                                                break;

                                                        }
                                                    }
                                                }).show();
                                        break;

                                }
                            }
                        }).show();
            }
        });

        note = (Expense) getIntent().getSerializableExtra(EXTRA_NOTE);
        if (note != null) {
            editTitle.setText(note.getTitle());
            editContent.setText(note.getNote());
        } else {
            note = new Expense();
            note.setDate(DATETIME_FORMAT.format(new Date()));
        }

        saveButton = (FloatingActionButton) findViewById(R.id.add_edit_button);
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

        ll = (LinearLayout) findViewById(R.id.llmain);
        ll1 = (LinearLayout) findViewById(R.id.ll1);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBack();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestResultCode.REQUEST_CODE_ADD_TRIP) {
            if (resultCode == RESULT_OK) {
                addNote(data);
            }
        }
    }

    private boolean isNoteFormOk() {
        String title=editTitle.getText().toString();
        return !(title==null || title.trim().length()==0);
    }

    private void validateNoteForm() {
        String msg=null;
        if (isNullOrBlank(editTitle.getText().toString())){
            msg="Title";
            //inputlayoutTitle.setError("Title is Missing");
        }
        if (msg!=null){
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    private void setNoteResult() {
        note.setTitle(editTitle.getText().toString().trim());
        note.setNote(editContent.getText().toString().trim());
        note.setDate(DATETIME_FORMAT.format(new Date()));
        note.setAmount(Float.parseFloat(amount.getText().toString()));

        String check = editTitle.getText().toString();
        if(check.equalsIgnoreCase("Clothing") || check.equalsIgnoreCase("E-Commerce") || check.equalsIgnoreCase("Extras") || check.equalsIgnoreCase("Food") || check.equalsIgnoreCase("Movies") || check.equalsIgnoreCase("Stocks")){
            //note.setBudget(databaseHelper.getBudget() - Float.parseFloat(amount.getText().toString()));
            databaseHelper.updateBudget(databaseHelper.getBudget() - Float.parseFloat(amount.getText().toString()));
        }else if(check.equalsIgnoreCase("Bank") || check.equalsIgnoreCase("Deposit") || check.equalsIgnoreCase("Extras (Savings)") || check.equalsIgnoreCase("Gift")){
            //note.setBudget(databaseHelper.getBudget() + Float.parseFloat(amount.getText().toString()));
            databaseHelper.updateBudget(databaseHelper.getBudget() + Float.parseFloat(amount.getText().toString()));
        }

        Intent intent=new Intent();
        intent.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intent);

        Toast.makeText(NewExpenseActivity.this, "Expense Saved.", Toast.LENGTH_LONG).show();
    }

    private void onBack(){
        if (isNoteFormOk()) {
            if ((editTitle.getText().toString().equals(note.getTitle())) && (editContent.getText().toString().equals(note.getNote()))) {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }else {
                setNoteResult();
                finish();
            }
        } else {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        }
    }

    private void addNote(Intent data) {
        Expense note = (Expense) data.getSerializableExtra(EXTRA_NOTE);
        long noteId = databaseHelper.createExpenseNote(note);
        note.setId(noteId);
    }

    @Override
    public void onBackPressed() {
        onBack();
        Intent intentHome = new Intent(NewExpenseActivity.this, ExpenseActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentHome.putExtra(EXTRA_NOTE, note);
        setResult(RESULT_OK, intentHome);
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

}
