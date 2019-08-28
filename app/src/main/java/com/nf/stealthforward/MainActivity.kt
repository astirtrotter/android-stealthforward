package com.nf.stealthforward

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.nf.stealthforward.api.NetworkClient
import com.nf.stealthforward.listener.SmsListener
import com.nf.stealthforward.receiver.SmsBroadcastReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity(), SmsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        SmsBroadcastReceiver.smsListener = this

        checkForSmsPermission()
    }

    override fun onSmsReceived(sender: String, body: String) {
        tvSmsLog.append("\n$sender : $body")

        tvSmsLog.append("\nCalling registering api")
        val apiCall = NetworkClient.retrofitClient.saveOTP(sender, body)
        apiCall.enqueue(object : retrofit2.Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                tvSmsLog.append("\nFailed to register. error: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                tvSmsLog.append("\nSuccessfully registered: ${response.body()}")
            }
        })
    }

    private fun checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECEIVE_SMS),
                101
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            101 -> {
                if (!permissions[0].equals(android.Manifest.permission.RECEIVE_SMS, true) ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "SMS permission denied!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
