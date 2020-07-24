package com.omardhanishdon.simtchtask.model

data class ScanInfo(var serviceName : String? = null,
                    var serviceType : String? = null,
                    var port : Int? = null,
                    var host : String? = null)