package com.e.globalaccelerexassessment.views

import CheckNetwork.isConnected
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.braintreepayments.cardform.view.CardForm
import com.e.globalaccelerexassessment.R
import com.e.globalaccelerexassessment.repository.CardRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    //on back press
    private var backPressed: Long? = 0

    //activity
    private var activity = this@MainActivity

    //repository
    private var repo = CardRepository()

    //Views
    private var cardForm: CardForm? = null
    private var cardScheme: TextView? = null
    private var cardType: TextView? = null
    private var bankName: TextView? = null
    private var countryName: TextView? = null
    private var cardLength: TextView? = null
    private var prepaid: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        setContentView(R.layout.activity_main)

        //Initialize views
        cardForm = findViewById(R.id.card_number)
        cardScheme = findViewById(R.id.card_scheme)
        cardType = findViewById(R.id.card_type)
        bankName = findViewById(R.id.bank)
        countryName = findViewById(R.id.country)
        cardLength = findViewById(R.id.card_number_length)
        prepaid = findViewById(R.id.prepaid)
        cardForm!!.cardRequired(true).setup(this)
        back.setOnClickListener(this)
        proceed.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.proceed -> {
                    if (isConnected(this)) {
                        if (cardForm!!.cardNumber.isNotEmpty()) {
                            if (!(cardForm!!.cardNumber.length >= 6 && cardForm!!.cardNumber.length <= 9)) {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Please enter the first 6 or 9 digits of your debit card",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                repo.getInformation(
                                    activity,
                                    cardForm!!.cardNumber
                                ) { CardInfoPage ->
                                    hideKeyboard()
                                    cardScheme!!.setText(CardInfoPage.scheme)
                                    cardType!!.setText(CardInfoPage.type)
                                    bankName!!.setText(CardInfoPage.bank!!.name)
                                    countryName!!.setText(CardInfoPage.country!!.name)
                                    cardLength!!.setText(CardInfoPage.number!!.length.toString())
                                    if (CardInfoPage.prepaid == true) prepaid!!.setText("Yes") else prepaid!!.setText("No")
                                }
                            }
                        } else {
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Please enter your card number!!!!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "No Internet Connection!!!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }


                }

                R.id.back -> finish()


            }
        }
    }

    override fun onBackPressed() {
        if (backPressed!! + 2000 > System.currentTimeMillis()) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
            finishAffinity()
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Touch again to exit",
                Snackbar.LENGTH_SHORT
            ).show()
            backPressed = System.currentTimeMillis()
        }
    }

    private fun hideKeyboard() {
        try {
            val inputManager: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            Log.e("Error hideKeyboard: $e", e.toString())
        }
    }



}