package com.nabi.nabi.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nabi.data.utils.LoggerUtils
import com.nabi.nabi.R
import com.nabi.nabi.views.splash.StartActivity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // 메시지를 수신할 때 호출 된다.
    // 수신된 RemoteMessage 객체를 기준으로 작업을 수행하고 메시지 데이터를 가져올 수 있다.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 메시지에 데이터 페이로드가 포함 되어 있는지 확인한다.
        // 페이로드란 전송된 데이터를 의미한다.
        if (remoteMessage.data.isNotEmpty()) {
            LoggerUtils.d("Message data payload: ${remoteMessage.data}")
            sendNotification(
                remoteMessage.data["title"].toString(),
                remoteMessage.data["body"].toString()
            )
        } else {
            // 메시지에 알림 페이로드가 포함되어 있는지 확인한다.
            remoteMessage.notification?.let {
                sendNotification(
                    remoteMessage.notification!!.title.toString(),
                    remoteMessage.notification!!.body.toString()
                )
            }
        }
    }

    // 새 토큰이 생성될 때마다 onNewToken 콜백이 호출된다.
    // 등록 토큰이 처음 생성되므로 여기서 토큰을 검색할 수 있다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToServer(token)
    }

    // 메시지에 데이터 페이로드가 포함 되어 있을 때 실행되는 메서드
    // 장시간 실행 (10초 이상) 작업의 경우 WorkManager를 사용하여 비동기 작업을 예약한다.
    private fun scheduleJob() {
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance(this)
            .beginWith(work)
            .enqueue()
    }

    // 메시지에 데이터 페이로드가 포함 되어 있을 때 실행되는 메서드
    // 10초 이내로 걸릴 때 메시지를 처리한다.
    private fun handleNow() {
        LoggerUtils.d("Short lived task is done.")
    }

    // 타사 서버에 토큰을 유지해주는 메서드이다.
    private fun sendRegistrationToServer(token: String?) {
        LoggerUtils.d("sendRegistrationTokenToServer($token)")
    }

    // 수신 된 FCM 메시지를 포함하는 간단한 알림을 만들고 표시한다.
    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 이상에서 알림을 제공하려면 앱의 알림 채널을 시스템에 등록해야 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {
        override fun doWork(): Result {
            return Result.success()
        }
    }

    fun getRegistrationToken(): String? = runBlocking {
        return@runBlocking try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            LoggerUtils.w("Fetching FCM Registration Token failed")
            null
        }
    }


}