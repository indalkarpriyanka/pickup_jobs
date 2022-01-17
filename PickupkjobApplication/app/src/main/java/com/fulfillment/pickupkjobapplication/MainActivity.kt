package com.fulfillment.pickupkjobapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fulfillment.pickupkjobapplication.adapters.PickupjobListAdapter
import com.fulfillment.pickupkjobapplication.databinding.ActivityMainBinding
import com.fulfillment.pickupkjobapplication.models.*
import com.fulfillment.pickupkjobapplication.network.Api
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.fulfillment.pickupkjobapplication.adapters.OnCheckedChangeListener


class MainActivity : AppCompatActivity(), OnCheckedChangeListener {

    private lateinit var picjobListAdapter: PickupjobListAdapter
    private lateinit var viewModel: PickjobViewModel
    private var binding: ActivityMainBinding? = null
    private lateinit var repository: PickjobRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        FirebaseApp.initializeApp(this)

        setAdapter()

        repository = PickjobRepository(Api.retrofitService)

        var pickjobViewModelProviderFactory = PickjobViewModelProviderFactory(repository)

        viewModel = ViewModelProvider(
            this,
            pickjobViewModelProviderFactory
        ).get(PickjobViewModel::class.java)

        viewModel.pickupJobList.observe(this, Observer {
            if (it.isNotEmpty()) {
                picjobListAdapter.submitList(it)
                picjobListAdapter.notifyDataSetChanged()

            }
        })
        lifecycleScope.launch {

            viewModel.signInwithEmailidAndPassword()
            viewModel.getAccessToken()
            viewModel.getPickUpJobsList()

        }
    }

    private fun setAdapter() {

        picjobListAdapter = PickupjobListAdapter()
        picjobListAdapter.setOnCheckedChangeListner(this)

        binding?.rvPickjobs?.adapter = picjobListAdapter

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        binding?.rvPickjobs?.layoutManager =layoutManager
        binding?.rvPickjobs?.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCheckedChange(pickJob: PickupJobs) {
        lateinit var pickjobUiModel: PickjobUiModel
        if (pickJob.status.equals(Constants.STATUS_CLOSED)) {
            pickjobUiModel =
                PickjobUiModel(pickJob.id.toString(), Constants.STATUS_OPEN, pickJob.version)
        } else {
            pickjobUiModel =
                PickjobUiModel(pickJob.id.toString(), Constants.STATUS_CLOSED, pickJob.version)
        }
        if (Constants.isNetworkAvailable(this)) {
            lifecycleScope.launch {
                viewModel.patchPickUpjob(pickjobUiModel)
            }

        }
    }
}






