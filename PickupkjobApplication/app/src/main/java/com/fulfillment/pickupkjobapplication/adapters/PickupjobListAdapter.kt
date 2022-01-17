package com.fulfillment.pickupkjobapplication.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fulfillment.pickupkjobapplication.databinding.ItemListLayoutBinding
import android.view.LayoutInflater
import android.widget.CompoundButton
import com.fulfillment.pickupkjobapplication.Constants
import com.fulfillment.pickupkjobapplication.MainActivity
import com.fulfillment.pickupkjobapplication.models.PickupJobs

class PickupjobListAdapter : RecyclerView.Adapter<PickupjobListAdapter.PickjobViewModel>() {

    private var pickupJobs = ArrayList<PickupJobs>()
    lateinit var onCheckedChangeListener:OnCheckedChangeListener

    fun setOnCheckedChangeListner(onCheckedChangeListener: MainActivity){
        this.onCheckedChangeListener=onCheckedChangeListener

    }

    class PickjobsDiffCallback(
        var oldItemList: ArrayList<PickupJobs>,
        var newItemList: ArrayList<PickupJobs>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldItemList.size
        }

        override fun getNewListSize(): Int {
            return newItemList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItemList[oldItemPosition].id == newItemList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItemList[oldItemPosition] == newItemList[newItemPosition]
        }

    }

    fun submitList(pickJoblist: ArrayList<PickupJobs>) {
        //val oldList = pickupJobs
        //  val diffResult: DiffUtil.DiffResult =
        //      DiffUtil.calculateDiff(PickjobsDiffCallback(oldList, pickJoblist))
        //    diffResult.dispatchUpdatesTo(this)'

        pickupJobs = pickJoblist
    }

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
        var pickjob = pickupJobs[position]
        holder.txtPcijobId.text = pickjob.id
        if (pickjob.status.equals(Constants.STATUS_OPEN)) {
            holder.swhStatus.isChecked = true
        } else if (pickjob.status.equals(Constants.STATUS_CLOSED)) {
            holder.swhStatus.isChecked = false
        }
        holder.swhStatus.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            onCheckedChangeListener.onCheckedChange(pickjob)
        })
    }

    override fun getItemCount(): Int {
        return pickupJobs.size

    }
}

interface OnCheckedChangeListener{
    fun onCheckedChange(pickJob:PickupJobs)
}
