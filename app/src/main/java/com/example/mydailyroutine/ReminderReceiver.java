package com.example.mydailyroutine;
import android.app.*;import android.content.*;import androidx.core.app.NotificationCompat;
public class ReminderReceiver extends BroadcastReceiver {
 @Override public void onReceive(Context c,Intent i){String title=i.getStringExtra("title");if(title==null)title="Routine reminder";Notification n=new NotificationCompat.Builder(c,"routine").setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("My Daily Routine").setContentText(title).setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true).build();((NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE)).notify((int)System.currentTimeMillis(),n);ReminderScheduler.schedule(c,title,i.getIntExtra("hour",8),i.getIntExtra("minute",0),i.getIntExtra("id",0));}
}
