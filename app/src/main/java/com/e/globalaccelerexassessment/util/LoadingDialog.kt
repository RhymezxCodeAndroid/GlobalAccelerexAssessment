package com.e.globalaccelerexassessment.util

import android.app.Activity
import android.app.AlertDialog
import com.e.globalaccelerexassessment.R

data class LoadingDialog(var myActivity: Activity) {

    private var alertDialog: AlertDialog ? = null
    private var activity = myActivity

    fun loadingAlertDialog(){
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_loader, null))
        builder.setCancelable(true)

        alertDialog = builder.create()
        alertDialog!!.show()

    }

    fun dismissAlertDialog(){
        alertDialog!!.dismiss()
    }


}