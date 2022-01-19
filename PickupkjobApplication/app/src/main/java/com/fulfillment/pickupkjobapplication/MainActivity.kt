package com.fulfillment.pickupkjobapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
import androidx.recyclerview.widget.RecyclerView
import com.fulfillment.pickupkjobapplication.adapters.OnCheckedChangeListener
import com.fulfillment.pickupkjobapplication.repository.PickjobRepository
import com.fulfillment.pickupkjobapplication.utils.Constants
import com.fulfillment.pickupkjobapplication.viewmodel.PickjobViewModel
import com.fulfillment.pickupkjobapplication.viewmodel.PickjobViewModelProviderFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class MainActivity : AppCompatActivity(), OnCheckedChangeListener {

    private lateinit var picjobListAdapter: PickupjobListAdapter
    private lateinit var viewModel: PickjobViewModel
    private var binding: ActivityMainBinding? = null
    private lateinit var repository: PickjobRepository

    private lateinit var pickjobViewModelProviderFactory: PickjobViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        FirebaseApp.initializeApp(this)

        setViewModel()
        signInAndFetchPickjobData()
        setAdapter()
    }

    private fun signInAndFetchPickjobData() {
        CoroutineScope(Dispatchers.IO).launch {
            if (viewModel.getCurrentUser() == null) {
                viewModel.signInwithEmailidAndPassword()
            } else {
                viewModel.getAccessToken()
                Log.d("current user", viewModel.getCurrentUser()?.displayName.toString())
            }
            viewModel.getPickUpJobsList()
        }
    }

    private fun setViewModel() {
        repository = PickjobRepository(Api.retrofitService)
        pickjobViewModelProviderFactory = PickjobViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(
            this,
            pickjobViewModelProviderFactory
        ).get(PickjobViewModel::class.java)
        viewModel.pickupJobList.observe(this, Observer {
            if (it.isNotEmpty()) {
                picjobListAdapter.pickjobDiff.submitList(it)
            }
        })
        viewModel.isLoading.observe(this, Observer {
            if (it)
                showProgressBar()
            else
                hideProgressBar()
        })
    }

    private fun showProgressBar() {
        binding?.pgrBar?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding?.pgrBar?.visibility = View.GONE
    }


    private fun setAdapter() {
        picjobListAdapter = PickupjobListAdapter()
        picjobListAdapter.setOnCheckedChangeListner(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL;
        binding?.rvPickjobs?.layoutManager = layoutManager
        binding?.rvPickjobs?.adapter = picjobListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCheckedChange(pickJob: PickupJobs, isChecked: Boolean) {
        var status: String
        if (isChecked) {
            status = Constants.STATUS_OPEN
        } else {
            status = Constants.STATUS_CLOSED
        }
        if (Constants.isNetworkAvailable(this)) {
            Log.d("auth token", Constants.USER_TOKEN)
            lifecycleScope.launch {
                viewModel.patchPickUpjob(pickJob.id.toString(), status, pickJob.version)
            }
        }
    }
}






