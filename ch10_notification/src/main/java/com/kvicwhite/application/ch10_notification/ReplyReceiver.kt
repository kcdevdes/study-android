package com.kvicwhite.application.ch10_notification

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class ReplyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        // receive the message to display on the notification
        val replyTxt = RemoteInput.getResultsFromIntent(intent)
            ?.getCharSequence("key_text_reply")
        Log.d("TAG", "replyText : $replyTxt")

        // cancel notification
        val manager = context.getSystemService(
            AppCompatActivity.NOTIFICATION_SERVICE
        ) as NotificationManager
        manager.cancel(11)
    }
}