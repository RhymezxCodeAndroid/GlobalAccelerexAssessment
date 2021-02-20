package com.e.globalaccelerexassessment.repository

import android.app.Activity
import android.util.Log
import com.e.globalaccelerexassessment.models.CardInfoPage
import com.e.globalaccelerexassessment.util.LoadingDialog
import com.e.globalaccelerexassessment.util.Urls
import com.github.kittinunf.fuel.core.isClientError
import com.github.kittinunf.fuel.core.isServerError
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import com.google.android.material.snackbar.Snackbar


class CardRepository {

    fun getInformation(
        activity: Activity,
        card_number: String,
        responseHandler: (result: CardInfoPage) -> Unit
    ) {
        val dialog = LoadingDialog(activity)
        dialog.loadingAlertDialog()

        Urls.getInformationUrl(card_number)
            .httpGet()
            .responseObject(CardInfoPage.Deserializer())
            { _, response, result ->

                Log.v("response: ", response.toString())

                try {
                    when {
                        response.isSuccessful -> {
                            val (data, _) = result
                            responseHandler.invoke(data as CardInfoPage)

                            dialog.dismissAlertDialog()
                        }
                        response.isServerError -> {
                            dialog.dismissAlertDialog()
                            Snackbar.make(
                                activity.findViewById(android.R.id.content),
                                "Server error!!!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        response.isClientError -> {
                            dialog.dismissAlertDialog()
                            when (response.statusCode) {
                                404 -> {
                                    Snackbar.make(
                                        activity.findViewById(android.R.id.content),
                                        "Card information not found!",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                                429 -> {
                                    Snackbar.make(
                                        activity.findViewById(android.R.id.content),
                                        "Too many request!",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                                400 -> {
                                    Snackbar.make(
                                        activity.findViewById(android.R.id.content),
                                        "Bad request!",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    Snackbar.make(
                                        activity.findViewById(android.R.id.content),
                                        "Client error!!!",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        }
                        else -> {
                            dialog.dismissAlertDialog()
                            Snackbar.make(
                                activity.findViewById(android.R.id.content),
                                "Something went wrong!!!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }


                } catch (error: Exception) {
                    Log.v("error: ", error.toString())
                }

            }


    }

}