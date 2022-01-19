package com.fulfillment.pickupkjobapplication.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fulfillment.pickupkjobapplication.databinding.ItemListLayoutBinding
import android.view.LayoutInflater
import android.widget.CompoundButton
import androidx.recyclerview.widget.AsyncListDiffer
import com.fulfillment.pickupkjobapplication.utils.Constants
import com.fulfillment.pickupkjobapplication.MainActivity
import com.fulfillment.pickupkjobapplication.models.PickupJobs

class PickupjobListAdapter : RecyclerView.Adapter<PickupjobListAdapter.PickjobViewModel>() {

    private lateinit var onCheckedChangeListener: OnCheckedChangeListener
    fun setOnCheckedChangeListner(onCheckedChangeListener: MainActivity) {
        this.onCheckedChangeListener = onCheckedChangeListener
    }

    private val pickjobsDiffCallback = object : DiffUtil.ItemCallback<PickupJobs>() {
        override fun areItemsTheSame(oldItem: PickupJobs, newItem: PickupJobs): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PickupJobs, newItem: PickupJobs): Boolean {
            return oldItem == newItem
        }
    }

     val pickjobDiff = AsyncListDiffer(this, pickjobsDiffCallback)

    inner class PickjobViewModel(itemBinding: ItemListLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val txtPcijobId = itemBinding.txtPickjobId
        val swhStatus = itemBinding.swhStatus

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickjobViewModel {
        val itemBinding: ItemListLayoutBinding = ItemListLayoutBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        )
        return PickjobViewModel(itemBinding)
    }

    override fun onBindViewHolder(holder: PickjobViewModel, position: Int) {
        var pickjob = pickjobDiff.currentList[position]
        holder.txtPcijobId.text = pickjob.id
        if (pickjob.status.equals(Constants.STATUS_OPEN)) {
            holder.swhStatus.isChecked = true
        } else if (pickjob.status.equals(Constants.STATUS_CLOSED)) {
            holder.swhStatus.isChecked = false
        }
        holder.swhStatus.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                onCheckedChangeListener.onCheckedChange(pickjob, isChecked)
            }
        })
    }

    override fun getItemCount(): Int {
        return pickjobDiff.currentList.size
    }
}

interface OnCheckedChangeListener {
    fun onCheckedChange(pickJob: PickupJobs, isChecked: Boolean)
}
