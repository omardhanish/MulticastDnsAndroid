package com.omardhanishdon.simtchtask.viewmodel

import android.app.Application
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.core.util.Predicate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.omardhanishdon.simtchtask.model.ScanInfo
import com.omardhanishdon.smsreadertask.utils.CommonUtils


class MainActivityViewModel(val app : Application) : AndroidViewModel(app) {

    val SERVICE_TYPE = "_http._tcp"

    var serviceName = ""
    var nsdManager : NsdManager? = null

    val scanResultList = mutableListOf<ScanInfo>()

    private var registerListenerAdapter : NsdManager.RegistrationListener? = null
    private var discoveryListener : NsdManager.DiscoveryListener? = null

    //Events
    val onListUpdate = MutableLiveData<Unit>()
    val onremoveUpdate = MutableLiveData<Int>()
    val loader = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    init {
        nsdManager = app.getSystemService(Context.NSD_SERVICE) as NsdManager
    }

    fun addScanResult(serviceInfo: NsdServiceInfo) {
        val data = ScanInfo(serviceName = serviceInfo.serviceName , serviceType = serviceInfo.serviceType ,
        host = serviceInfo.host.hostAddress , port = serviceInfo.port)
        scanResultList.add(data)
        onListUpdate.postValue(Unit)
    }

    fun removeResult(serviceInfo: NsdServiceInfo) {
        var p : Int = -1
        CommonUtils.removeFilter(scanResultList , Predicate {
            p++
            it.serviceName.equals(serviceInfo.serviceName)
        })
        if(p != -1 && p < scanResultList.size){
            onremoveUpdate.postValue(p)
        }
    }

    fun registerService(port : Int = 80 , sName : String = "MyService001") {
        try {
            loader.postValue(true)
            tearDown()
            initRegisterListener()
            val serviceInfo = NsdServiceInfo()
            serviceName = sName
            serviceInfo.serviceName = sName
            serviceInfo.serviceType = SERVICE_TYPE
            serviceInfo.port = port

            app.apply {
                nsdManager?.registerService(
                    serviceInfo, NsdManager.PROTOCOL_DNS_SD,
                    registerListenerAdapter)
            }
        }catch (e : Exception) {
            loader.postValue(false)
        }
    }

    fun discoverService() {
        loader.postValue(true)
        stopDiscovery()
        initDiscovery()
        scanResultList.clear()
        nsdManager?.apply {
            discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        }
    }

    fun tearDown() {
        if (registerListenerAdapter != null) {
            try {
                nsdManager?.unregisterService(registerListenerAdapter)
            }catch (e:java.lang.Exception)
            {} finally {
            }
            registerListenerAdapter = null
        }
    }

    fun stopDiscovery() {
        if (discoveryListener != null) {
            try {
                nsdManager?.stopServiceDiscovery(discoveryListener)
            }catch (e : java.lang.Exception){}
            finally {
            }
            discoveryListener = null
        }
    }

    private fun initRegisterListener() {
        registerListenerAdapter  =  object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
                loader.postValue(false)
                message.postValue("Successfully registered")
                serviceName = NsdServiceInfo.serviceName
                Log.i("onServiceRegistered" , serviceName)
            }

            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                loader.postValue(false)
            }

            override fun onServiceUnregistered(arg0: NsdServiceInfo) {
                loader.postValue(false)
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                loader.postValue(false)
                Log.i("onUnregistrationFailed" , serviceInfo.toString())
            }
        }
    }

    private fun initDiscovery() {
        discoveryListener = object : NsdManager.DiscoveryListener {

            override fun onDiscoveryStarted(regType: String) {
                loader.postValue(false)
            }

            override fun onServiceFound(service: NsdServiceInfo) {
                loader.postValue(false)
                if (service.serviceName.contains(serviceName)) {
                    nsdManager?.resolveService(service, object : NsdManager.ResolveListener {

                        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                        }

                        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                            addScanResult(serviceInfo)
                        }
                    })
                }
            }

            override fun onServiceLost(service: NsdServiceInfo) {
                removeResult(service)
            }

            override fun onDiscoveryStopped(serviceType: String) {
                loader.postValue(false)
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                loader.postValue(false)
                try {
                    nsdManager?.stopServiceDiscovery(this)
                }catch (e : Exception){}

            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                loader.postValue(false)
                try {
                    nsdManager?.stopServiceDiscovery(this)
                }catch (e : Exception){}
            }
        }
    }

}