package com.omardhanishdon.simtchtask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omardhanishdon.simtchtask.databinding.ItemScanResultBinding
import com.omardhanishdon.simtchtask.model.ScanInfo

class ScanResultAdapter(private val list : MutableList<ScanInfo>,
                        private val context: Context) : RecyclerView.Adapter<ScanResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater  = LayoutInflater.from(context)
        val binding = ItemScanResultBinding.inflate(inflater , parent , false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list.get(holder.adapterPosition).serviceName?.apply {
            holder.view.serviceName.text = "Service Name : " + this
        }
        list.get(holder.adapterPosition).serviceType?.apply {
            holder.view.serviceType.text = "Service Type : " + this
        }
        list.get(holder.adapterPosition).host?.apply {
            holder.view.ipAddress.text = "IpAddress : " + this
        }
        list.get(holder.adapterPosition).port?.apply {
            holder.view.port.text = "Port : " + this.toString()
        }
    }

    inner class ViewHolder(val view : ItemScanResultBinding) : RecyclerView.ViewHolder(view.root)

}