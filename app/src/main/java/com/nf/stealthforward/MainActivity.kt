package com.nf.stealthforward

import android.Manifest.permission.SEND_SMS
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), SmsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        SmsBroadcastReceiver.smsListener = this

        checkForSmsPermission()
    }

    override fun onSmsReceived(sender: String, body: String) {
        tvSmsLog.append("$sender : $body\n")
        Toast.makeText(this, "$sender : $body", Toast.LENGTH_LONG).show()
    }

    private fun checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(SEND_SMS),
                101
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            101 -> {
                if (!permissions[0].equals(SEND_SMS, true) ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "SMS permission denied!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
