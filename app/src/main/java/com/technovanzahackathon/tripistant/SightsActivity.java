package com.technovanzahackathon.tripistant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class SightsActivity extends AppCompatActivity implements SightsRecyclerAdapter.RecyclerItemClickListener {
	ArrayList<SightsData> sightData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sights);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sights_recycler);
		recyclerView.setHasFixedSize(true);

		sightData = new ArrayList<SightsData>();
		sightData.add(new SightsData("Museum of Himachal Culture", "https://media-cdn.tripadvisor.com/media/photo-s/0b/75/1c/ff/outside-the-museum.jpg", "32.2462111", "77.1805751"));
		sightData.add(new SightsData("Hot Spring Water", "http://journeymart.com/de/AttrationImages/vashisht-hot-water-springs-manali.jpg", "32.2668416", "77.1853445"));
		sightData.add(new SightsData("Museum of Himachal Culture", "https://media-cdn.tripadvisor.com/media/photo-s/0b/75/1c/ff/outside-the-museum.jpg", "32.2462111", "77.1805751"));
		sightData.add(new SightsData("Hot Spring Water", "http://journeymart.com/de/AttrationImages/vashisht-hot-water-springs-manali.jpg", "32.2668416", "77.1853445"));
		sightData.add(new SightsData("Museum of Himachal Culture", "https://media-cdn.tripadvisor.com/media/photo-s/0b/75/1c/ff/outside-the-museum.jpg", "32.2462111", "77.1805751"));
		sightData.add(new SightsData("Hot Spring Water", "http://journeymart.com/de/AttrationImages/vashisht-hot-water-springs-manali.jpg", "32.2668416", "77.1853445"));
		SightsRecyclerAdapter sightsRecyclerAdapter = new SightsRecyclerAdapter(this, this, sightData);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(sightsRecyclerAdapter);


	}

	@Override
	public void onClick(View v, int i) {
		String s = "geo:0,0?q=" + sightData.get(i).latitude+ "," +sightData.get(i).longitude + " (" + sightData.get(i).name + ")" ;

		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse(s));
		startActivity(intent);

	}
}
