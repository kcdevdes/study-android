package com.kvicwhite.application.ch10_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.kvicwhite.application.ch10_notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        // binds view elements in activity_main
        setContentView(binding.root)
        // sets content view as the root of binding variable

        binding.notificationButton.setOnClickListener {
            // sets a clickListener of notificationButton

            val manager = getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager
            val builder: NotificationCompat.Builder


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // only works when the version is OREO or higher (26)
                val channelId = "one-channel" // channel id to show various notification types
                val channelName = "My Channel One" // sets the channel name
                val channel = NotificationChannel( // views in Setting panel
                    channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    // configures various properties the notification will have

                    description = "My Channel One Description"
                    // explains what this channel actually does
                    setShowBadge(true)
                    // I WANNA SEE A FANCY BADGE
                    val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    // gets a sound of notification that I've chosen in Setting
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            // SONIFICATION : the use of non-speech audio to convey information or perceptualize data.
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                    setSound(uri, audioAttributes)
                    enableLights(true)
                    enableVibration(true)
                }
                manager.createNotificationChannel(channel)
                // shows notification channel in Setting
                builder = NotificationCompat.Builder(this, channelId)
                // builds with notification channel
            } else {
                // works when the version is lower than OREO
                builder = NotificationCompat.Builder(this)
            }

            builder.run {
                setSmallIcon(R.drawable.small)
                setWhen(System.currentTimeMillis())
                setContentTitle("홍길동") // title
                setContentText("안녕하세요") // content
                setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.big))
                // when the notification gets bigger
            }

            val KEY_TEXT_REPLY = "key_text_reply" // identifier
            var replyLabel = "답장" // hint
            var remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
                setLabel(replyLabel)
                build()
            }
            // RemoteInput provides a user to type text in the notification.

            val replyIntent = Intent(this, ReplyReceiver::class.java)
            // sends data to ReplyReceiver
            val replyPendingIntent = PendingIntent.getBroadcast(
                    this, 30, replyIntent, PendingIntent.FLAG_MUTABLE
            )
            // when the same notification happens
            builder.addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.send,
                    "답장",
                    replyPendingIntent
                ).addRemoteInput(remoteInput).build()
            )
            // Action provides a user to interact with the notification
            manager.notify(11, builder.build())
            // MUST notify that the application can receive the action from RemoteInput
        }
    }
}