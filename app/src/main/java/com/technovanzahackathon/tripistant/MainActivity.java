package com.technovanzahackathon.tripistant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import com.technovanzahackathon.tripistant.TripChecklists.RequestResultCode;
import com.technovanzahackathon.tripistant.TripChecklists.TripChecklists;
import com.technovanzahackathon.tripistant.TripChecklists.Trips;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	private static final int REQUEST_STORAGE_PERMISSION = 0;
	private TextView textEmpty;
	private TextView textEmpty1;
	private List<Trips> notesData;
	private TripsAdapter checkAdapter;
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

	private static final String EXTRA_NOTE = "EXTRA_CHECK";
	private static final String EXTRA_NOTE1 = "EXTRA_CHECK1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation_drawer);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		databaseHelper = new DatabaseHelper(getApplicationContext());

		textEmpty = (TextView) findViewById(R.id.textEmpty);
		textEmpty1 = (TextView) findViewById(R.id.textEmpty1);

		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clayout);

		fab = (FloatingActionButton) findViewById(R.id.fab);

		recyclerView = (RecyclerView) findViewById(R.id.listTrips);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		setupTripChecklistsAdapter();
		updateView();
		sortList1(TripsAdapter.newestFirstComparator);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
				startActivityForResult(intent, RequestResultCode.REQUEST_CODE_ADD_TRIP);
			}
		});
		setListeners();

	}

	public void sortList1(Comparator<Trips> noteComparator) {
		Collections.sort(notesData, noteComparator);
		checkAdapter.notifyDataSetChanged();
	}

	private void setListeners() {
		TripsAdapter.setOnItemClickListener(new TripsAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, View v) {
				final Trips note = notesData.get(position);
				Intent intent = new Intent(MainActivity.this, TripActivity.class);
				intent.putExtra(EXTRA_NOTE, note);
				startActivityForResult(intent, RequestResultCode.REQUEST_CODE_EDIT_CHECK);
			}
		});

	}

	@Override
	protected void onResume() {
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
		notesData = databaseHelper.getAllTripsO();
		checkAdapter = new TripsAdapter(notesData, context, MainActivity.this);
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
		if (requestCode == RequestResultCode.REQUEST_CODE_EDIT_TRIP) {
			if (resultCode == RESULT_OK) {
				Log.d("TripChecklistsActivity", "RESULT_OK");
				TripChecklists note1 = (TripChecklists) data.getSerializableExtra(EXTRA_NOTE);
				note1.getTitle();
				note1.getContent();
			}
		}
		if (requestCode == RequestResultCode.REQUEST_CODE_EDIT_TRIP) {
			if (resultCode == RESULT_OK) {
				updateNote(data);
				checkAdapter.notifyDataSetChanged();
			} else if (resultCode == RequestResultCode.RESULT_CODE_DELETE_TRIP) {
				deleteNote(data);
			}
		}
		if (requestCode == RequestResultCode.REQUEST_CODE_ADD_TRIP) {
			if (resultCode == RESULT_OK) {
				Trips note = (Trips) data.getSerializableExtra(EXTRA_NOTE);
				long noteId = databaseHelper.createTripO(note);
				note.setTripId(noteId);
				notesData.add(0, note);
				updateView();
				checkAdapter.notifyDataSetChanged();
			}
		} else if (resultCode == RESULT_FIRST_USER) {
			addNote(data);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void addNote(Intent data) {
		Trips note = (Trips) data.getSerializableExtra(EXTRA_NOTE);
		long noteId = databaseHelper.createTripO(note);
		note.setTripId(noteId);
		notesData.add(note);
		updateView();
		checkAdapter.notifyDataSetChanged();
	}

	private void updateNote(Intent data) {
		Trips updatedNote = (Trips) data.getSerializableExtra(EXTRA_NOTE);
		databaseHelper.updateTripO(updatedNote);
		for (Trips note : notesData) {
			if (note.getTripId().equals(updatedNote.getTripId())) {
				note.setTripname(updatedNote.getTripname());
				note.setTripsrc(updatedNote.getTripsrc());
				note.setTripdest(updatedNote.getTripdest());
				note.setTripdate(updatedNote.getTripdate());
				note.setTriptime(updatedNote.getTriptime());
				note.setTripdatetimestatus(updatedNote.getTripdatetimestatus());
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
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}


	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

}