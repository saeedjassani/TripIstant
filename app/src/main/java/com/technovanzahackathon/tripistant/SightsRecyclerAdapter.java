package com.technovanzahackathon.tripistant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Saeed Jassani on 25-12-2016.
 */

public class SightsRecyclerAdapter extends RecyclerView.Adapter<SightsRecyclerAdapter.ViewHolder> {

	RecyclerItemClickListener clickListener;
	Context context;
	ArrayList<SightsData> sightsData;

	public SightsRecyclerAdapter(RecyclerItemClickListener clickListener, Context context, ArrayList<SightsData> sightsData) {
		this.clickListener = clickListener;
		this.context = context;
		this.sightsData = sightsData;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sights, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.textView.setText(sightsData.get(position).name);
		Glide.with(context)
				.load(sightsData.get(position).image)
				.placeholder(R.color.accent_color)
				.centerCrop()
				.into(holder.image);
	}

	@Override
	public int getItemCount() {
		return sightsData.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		ImageView image;
		TextView textView;

		public ViewHolder(View itemView) {
			super(itemView);
			image = (ImageView) itemView.findViewById(R.id.row_sight_image);
			textView = (TextView) itemView.findViewById(R.id.row_sight_text);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					clickListener.onClick(view, getAdapterPosition());
				}
			});
		}
	}

	public interface RecyclerItemClickListener {
		void onClick(View v, int position);
	}

}
