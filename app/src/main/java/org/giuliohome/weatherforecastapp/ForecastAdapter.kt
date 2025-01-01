package org.giuliohome.weatherforecastapp

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import org.giuliohome.weatherforecastapp.ui.ForecastItem



class ForecastAdapter(private val forecastList: List<Forecast>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(val composableView: ComposeView) : RecyclerView.ViewHolder(composableView)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ForecastViewHolder {
        return ForecastViewHolder(ComposeView(parent.context).apply {
            setContent {
                ForecastItem(forecastList[position]) // Assuming you have access to the forecastList here
            }
        })
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.composableView.setContent {
            ForecastItem(forecastList[position])
        }
    }

    override fun getItemCount() = forecastList.size
}