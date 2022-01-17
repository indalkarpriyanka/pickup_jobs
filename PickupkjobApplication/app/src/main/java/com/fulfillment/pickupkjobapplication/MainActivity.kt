package com.fulfillment.pickupkjobapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class MainActivity : AppCompatActivity(), OnCheckedChangeListener {

    private lateinit var picjobListAdapter: PickupjobListAdapter
    private lateinit var viewModel: PickjobViewModel
    private var binding: ActivityMainBinding? = null
    private lateinit var repository: PickjobRepository
    private lateinit var finalList:ArrayList<PickupJobs>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        FirebaseApp.initializeApp(this)
        repository = PickjobRepository(Api.retrofitService)

        var pickjobViewModelProviderFactory = PickjobViewModelProviderFactory(repository)

        viewModel = ViewModelProvider(
            this,
            pickjobViewModelProviderFactory
        ).get(PickjobViewModel::class.java)

        finalList= ArrayList<PickupJobs>()
        setAdapter()

        viewModel.pickupJobList.observe(this, Observer {
            if (it.isNotEmpty()) {

                picjobListAdapter.submitList(it)
                picjobListAdapter.notifyDataSetChanged()
                //binding?.rvPickjobs?.adapter = picjobListAdapter
                Log.d("inside oberserver","======================${it.toString()}")

            }
        })
        CoroutineScope(Dispatchers.IO).launch {

           // if(viewModel.getCurrentUser()==null)
           /* {
                viewModel.signInwithEmailidAndPassword()
            }else
            {
                Log.d("current user",viewModel.getCurrentUser()?.displayName.toString())
            }*/

           // viewModel.getAccessToken()
            viewModel.getPickUpJobsList()

        }
    }

    private fun setAdapter() {

        picjobListAdapter = PickupjobListAdapter()
        picjobListAdapter.setOnCheckedChangeListner(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL;
        binding?.rvPickjobs?.layoutManager =layoutManager
        binding?.rvPickjobs?.adapter = picjobListAdapter

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCheckedChange(pickJob: PickupJobs) {
        var pickjobUiModel: PickjobUiModel = if (pickJob.status.equals(Constants.STATUS_CLOSED)) {
            PickjobUiModel(pickJob.id.toString(), Constants.STATUS_OPEN, pickJob.version)
        } else {
            PickjobUiModel(pickJob.id.toString(), Constants.STATUS_CLOSED, pickJob.version)
        }
        if (Constants.isNetworkAvailable(this)) {
            lifecycleScope.launch {
                viewModel.patchPickUpjob(pickjobUiModel)
            }

        }
    }

    override fun onStart() {
        super.onStart()


    }
}






