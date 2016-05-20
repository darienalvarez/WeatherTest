package com.flomio.test.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flomio.test.R;
import com.flomio.test.networking.dto.Forecast;

import java.util.List;

/**
 * Created by darien
 * on 5/19/16.
 * <p>
 * Draw a forecast in a recycler View
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<Forecast> mForecasts;

    public ForecastAdapter(List<Forecast> forecasts) {
        super();
        this.mForecasts = forecasts;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);

        return new ForecastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        Forecast forecast = mForecasts.get(position);
        holder.mDayTextView.setText(forecast.getDay());
        holder.mForecastTextView.setText(forecast.getForecast());
    }

    @Override
    public int getItemCount() {
        return mForecasts.size();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        public TextView mDayTextView, mForecastTextView;

        public ForecastViewHolder(View itemView) {
            super(itemView);

            mDayTextView = (TextView) itemView.findViewById(R.id.dayTextView);
            mForecastTextView = (TextView) itemView.findViewById(R.id.forecastTextView);
        }
    }

}
