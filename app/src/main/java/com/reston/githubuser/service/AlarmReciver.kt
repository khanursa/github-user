package com.reston.githubuser.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.reston.githubuser.R
import com.reston.githubuser.view.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class AlarmReciver : BroadcastReceiver() {
    companion object {
        const val EXTRA_MESSAGE = "message"
        private const val ID_REPEATING = 101
        private const val TIME_FORMAT = "HH:mm"
        private const val TIME = "09:00"
        private const val MESSAGE = "Pengingat alarm untuk kembali lagi ke aplikasi"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)!!
        val intentUser = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntentWithParentStack(intentUser)
        val pendingIntent =
            stackBuilder.getPendingIntent(101, PendingIntent.FLAG_UPDATE_CURRENT)
        showAlarmNotification(context, message, pendingIntent)
    }

    private fun showAlarmNotification(
        context: Context,
        message: String,
        pendingIntent: PendingIntent
    ) {
        val chanelId = "Channel"
        val chanelName = "Github user channel"
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, chanelId)
            .setSmallIcon(R.drawable.ic_baseline_add_alarm_24)
            .setContentTitle("Alarm Github-User")
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(5000, 4000, 3000, 2000, 1000))
            .setSound(alarmSound)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setColor(context.resources.getColor(R.color.colorPrimaryDark))
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                chanelId,
                chanelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(5000, 4000, 3000, 2000, 1000)
            builder.setChannelId(chanelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(101, notification)
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReciver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Alarm telah di batalkan", Toast.LENGTH_SHORT).show()
    }

    fun setRepeatingAlarm(context: Context) {
        if (isDateInvalid()) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReciver::class.java)
        intent.putExtra(EXTRA_MESSAGE, MESSAGE)
        val timeArray = TIME.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Alarm berhasil di setel ke jam $TIME", Toast.LENGTH_SHORT).show()
    }

    fun isAlarmSet(context: Context): Boolean {
        val intent = Intent(context, AlarmReciver::class.java)
        val requestCode = ID_REPEATING
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    private fun isDateInvalid(): Boolean {
        return try {
            val df = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
            df.isLenient = false
            df.parse(TIME)
            false
        } catch (e: ParseException) {
            true
        }
    }

}