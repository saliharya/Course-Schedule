package com.dicoding.courseschedule.notification

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.util.NOTIFICATION_CHANNEL_ID
import com.dicoding.courseschedule.util.NOTIFICATION_CHANNEL_NAME
import com.dicoding.courseschedule.util.NOTIFICATION_ID
import com.dicoding.courseschedule.util.executeThread
import java.util.Calendar

class DailyReminder : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        executeThread {
            val repository = DataRepository.getInstance(context)
            repository?.getTodaySchedule()?.takeIf { it.isNotEmpty() }?.let {
                showNotification(context, it)
            }
        }
    }

    // TODO 12: Implement daily reminder for every 06.00 a.m using AlarmManager
    fun setDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val alarmIntent = Intent(context, DailyReminder::class.java)
        val pendingIntent = getPendingIntent(context, alarmIntent)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val alarmIntent = Intent(context, DailyReminder::class.java)
        val pendingIntent = getPendingIntent(context, alarmIntent)

        alarmManager?.cancel(pendingIntent)
    }

    private fun getPendingIntent(context: Context, intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(
            context, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showNotification(context: Context, content: List<Course>) {
        // TODO 13: Show today schedules in inbox style notification & open HomeActivity when notification tapped
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationIntent = Intent(context, HomeActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val timeString = context.resources.getString(R.string.notification_message_format)
        val inboxStyle = createInboxStyle(content, timeString)

        val notification = buildNotification(context, inboxStyle, pendingIntent)

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun createInboxStyle(
        content: List<Course>, timeString: String
    ): NotificationCompat.InboxStyle {
        val inboxStyle = NotificationCompat.InboxStyle()
        content.forEach {
            val courseData = String.format(timeString, it.startTime, it.endTime, it.courseName)
            inboxStyle.addLine(courseData)
        }
        return inboxStyle
    }

    private fun buildNotification(
        context: Context, inboxStyle: NotificationCompat.InboxStyle, pendingIntent: PendingIntent
    ): Notification {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(context.getString(R.string.today_schedule)).setStyle(inboxStyle)
            .setContentIntent(pendingIntent).setAutoCancel(true).build()
    }

    companion object {
        private const val DAILY_REMINDER_REQUEST_CODE = 1001
    }
}
