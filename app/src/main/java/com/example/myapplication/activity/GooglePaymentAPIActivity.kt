package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.shreyaspatil.easyupipayment.EasyUpiPayment
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import com.shreyaspatil.easyupipayment.model.PaymentApp
import com.shreyaspatil.easyupipayment.model.TransactionDetails
import com.shreyaspatil.easyupipayment.model.TransactionStatus
import kotlinx.android.synthetic.main.activity_google_payment_a_p_i.*
import kotlinx.android.synthetic.main.activity_home_page_experiment.*


class GooglePaymentAPIActivity : AppCompatActivity(), PaymentStatusListener {

    private lateinit var easyUpiPayment: EasyUpiPayment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_payment_a_p_i)
        initViews()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
        private fun initViews() {
            val transactionId = "TID" + System.currentTimeMillis()
            field_transaction_id.setText(transactionId)
            field_transaction_ref_id.setText(transactionId)

            // Setup click listener for Pay button
            googlePayButton.setOnClickListener { pay() }
        }
    private fun pay() {
        val payeeVpa = field_vpa.text.toString()
        val payeeName = field_name.text.toString()
        val transactionId = field_transaction_id.text.toString()
        val transactionRefId = field_transaction_ref_id.text.toString()
        val payeeMerchantCode = field_payee_merchant_code.text.toString()
        val description = field_description.text.toString()
        val amount = field_amount.text.toString()
        val paymentAppChoice = radioAppChoice

        val paymentApp = when (paymentAppChoice.checkedRadioButtonId) {
            R.id.app_default -> PaymentApp.ALL
            R.id.app_amazonpay -> PaymentApp.AMAZON_PAY
            R.id.app_bhim_upi -> PaymentApp.BHIM_UPI
            R.id.app_phonepe -> PaymentApp.PHONE_PE
            R.id.app_paytm -> PaymentApp.PAYTM
            else -> throw IllegalStateException("Unexpected value: " + paymentAppChoice.id)
        }
        try {
            // START PAYMENT INITIALIZATION
            easyUpiPayment = EasyUpiPayment(this) {
                this.paymentApp = paymentApp
                this.payeeVpa = payeeVpa
                this.payeeName = payeeName
                this.transactionId = transactionId
                this.transactionRefId = transactionRefId
                this.payeeMerchantCode = payeeMerchantCode
                this.description = description
                this.amount = amount
            }
            // END INITIALIZATION

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this)

            // Start payment / transaction
            easyUpiPayment.startPayment()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onTransactionCancelled() {
        // Payment Cancelled by User
        Toast.makeText(this, "Cancelled by user", Toast.LENGTH_SHORT).show()
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString())
        textView_status.text = transactionDetails.toString()

        when (transactionDetails.transactionStatus) {
            TransactionStatus.SUCCESS -> onTransactionSuccess()
            TransactionStatus.FAILURE -> onTransactionFailed()
            TransactionStatus.SUBMITTED -> onTransactionSubmitted()
        }
    }

    private fun onTransactionSubmitted() {
        Toast.makeText(this, "submitted", Toast.LENGTH_SHORT).show()
    }

    private fun onTransactionFailed() {
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
    }

    private fun onTransactionSuccess() {
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
    }


}