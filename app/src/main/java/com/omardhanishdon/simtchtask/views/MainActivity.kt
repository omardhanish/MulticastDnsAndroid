package com.omardhanishdon.simtchtask.views

import android.Manifest
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.omardhanishdon.simtchtask.adapter.ScanResultAdapter
import com.omardhanishdon.simtchtask.databinding.ActivityMainBinding
import com.omardhanishdon.simtchtask.utils.ProgressDialogUtils
import com.omardhanishdon.simtchtask.viewmodel.MainActivityViewModel
import com.omardhanishdon.smsreadertask.base.BaseActivity
import com.omardhanishdon.smsreadertask.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        checkPermissions(99 , arrayOf(Manifest.permission.ACCESS_WIFI_STATE , Manifest.permission.INTERNET))

        binding.list.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        val adapter = ScanResultAdapter(viewModel.scanResultList , this)
        binding.list.adapter = adapter

        binding.scan.setOnClickListener {
            viewModel.discoverService()
        }

        binding.publish.setOnClickListener {
            viewModel.registerService()
        }

        viewModel.onListUpdate.observe(this , Observer {
            adapter.notifyDataSetChanged()
        })

        viewModel.onremoveUpdate.observe(this , Observer {
            adapter.notifyItemRemoved(it)
        })

        viewModel.loader.observe(this , Observer {
            if(it){
                ProgressDialogUtils.showProgressDialog(this)
            }else{
                ProgressDialogUtils.dismissDialog()
            }
        })

        viewModel.message.observe(this , Observer {
            CommonUtils.showToastShort(it , this)
        })

    }

    override fun onRequestPermissionsResult(
        isAllPermissionAccepted: Boolean,
        notGivenPermission: Array<String?>?,
        requestCode: Int
    ) {

    }

    override fun onDestroy() {
        viewModel.tearDown()
        viewModel.stopDiscovery()
        super.onDestroy()
    }

}
