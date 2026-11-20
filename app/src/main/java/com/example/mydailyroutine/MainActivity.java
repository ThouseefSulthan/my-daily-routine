package com.example.mydailyroutine;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.*;

public class MainActivity extends Activity {
    static final String PREF="routine_data";
    LinearLayout root, content;
    int green=Color.rgb(37,112,82);
    String[] defaultNames={"Wake up","Drink water","Exercise","Take tablet","Drink water","Drink water","Evening walk","Sleep"};
    String[] defaultTimes={"06:00","07:00","07:30","09:00","11:00","14:00","18:00","22:30"};
    String[] exercises={"Wall push-ups","Chair sit-to-stand","Seated knee extensions","Seated arm raises","Shoulder rolls","Side leg raises"};
    @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(green); getWindow().setNavigationBarColor(Color.BLACK); createChannel(); if(Build.VERSION.SDK_INT>=33 && ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.POST_NOTIFICATIONS},5); show("Today");}
    void createChannel(){if(Build.VERSION.SDK_INT>=26){NotificationChannel c=new NotificationChannel("routine","Routine reminders",NotificationManager.IMPORTANCE_HIGH); c.setDescription("Daily routine alerts"); ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(c);}}
    TextView text(String s,int size){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(Color.rgb(35,48,42));t.setPadding(0,10,0,10);return t;}
    void show(String tab){root=new LinearLayout(this);root.setOrientation(1);root.setBackgroundColor(Color.rgb(246,249,246));root.setPadding(22,18,22,12);setContentView(root); TextView title=text("My Daily Routine",25);title.setTextColor(green);title.setTypeface(null,1);root.addView(title); TextView subtitle=text("Small steps. Better days.",14);root.addView(subtitle); content=new LinearLayout(this);content.setOrientation(1); ScrollView sc=new ScrollView(this);sc.addView(content);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
        if(tab.equals("Today")) today(); else if(tab.equals("Reminders")) reminders(); else if(tab.equals("Exercise")) exercise(); else videos();
        LinearLayout nav=new LinearLayout(this);String[] tabs={"Today","Reminders","Exercise","Videos"};for(String s:tabs){Button b=new Button(this);b.setText(s);b.setTextSize(11);b.setAllCaps(false);b.setOnClickListener(v->show(s));nav.addView(b,new LinearLayout.LayoutParams(0,-2,1));}root.addView(nav);
    }
    void today(){content.addView(text("Your day",21));String[] names=names();String[] times=times();int done=0;for(int i=0;i<names.length;i++)if(getPreferences(0).getBoolean("done_"+i,false))done++;content.addView(text(done+" of "+names.length+" tasks completed",15));ProgressBar p=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);p.setMax(Math.max(1,names.length));p.setProgress(done);content.addView(p);for(int i=0;i<names.length;i++){final int n=i;CheckBox cb=new CheckBox(this);cb.setText(times[i]+"   "+names[i]);cb.setTextSize(16);cb.setChecked(getPreferences(0).getBoolean("done_"+i,false));cb.setOnCheckedChangeListener((v,c)->getPreferences(0).edit().putBoolean("done_"+n,c).apply());content.addView(cb);} }
    String[] names(){return getPreferences(0).getString("names",String.join("|",defaultNames)).split("\\|",-1);} String[] times(){return getPreferences(0).getString("times",String.join("|",defaultTimes)).split("\\|",-1);}
    void reminders(){content.addView(text("Daily reminders",21));content.addView(text("Set a time and receive a notification with sound.",14));String[] ns=names(),ts=times();for(int i=0;i<ns.length;i++){final int n=i;LinearLayout row=new LinearLayout(this);row.setOrientation(0);TextView label=text(ts[i]+"  "+ns[i],16);row.addView(label,new LinearLayout.LayoutParams(0,-2,1));Button del=new Button(this);del.setText("Delete");del.setOnClickListener(v->{ArrayList<String>a=new ArrayList<>(Arrays.asList(names())),b=new ArrayList<>(Arrays.asList(times()));a.remove(n);b.remove(n);save(a,b);show("Reminders");});row.addView(del);content.addView(row);}
        Button add=new Button(this);add.setText("+ Add reminder");add.setOnClickListener(v->addReminder());content.addView(add);}
    void save(List<String>a,List<String>b){getPreferences(0).edit().putString("names",String.join("|",a)).putString("times",String.join("|",b)).apply();ReminderScheduler.scheduleAll(this);}
    void addReminder(){EditText name=new EditText(this);name.setHint("Reminder name");EditText time=new EditText(this);time.setHint("Time (HH:MM)");new AlertDialog.Builder(this).setTitle("New reminder").setView(name).setPositiveButton("Next",(d,w)->{String nm=name.getText().toString().trim();if(nm.isEmpty())return;new AlertDialog.Builder(this).setTitle("Reminder time").setView(time).setPositiveButton("Save",(x,y)->{String tm=time.getText().toString().trim();if(!tm.matches("([01]\\d|2[0-3]):[0-5]\\d")){Toast.makeText(this,"Use HH:MM, e.g. 07:30",Toast.LENGTH_LONG).show();return;}ArrayList<String>a=new ArrayList<>(Arrays.asList(names())),b=new ArrayList<>(Arrays.asList(times()));a.add(nm);b.add(tm);save(a,b);show("Reminders");}).setNegativeButton("Cancel",null).show();}).setNegativeButton("Cancel",null).show();}
    void exercise(){content.addView(text("10–15 minute movement",21));content.addView(text("Try these gentle movements at a comfortable pace. Stop if you feel pain, dizziness or unusual discomfort.",14));for(String s:exercises){CheckBox c=new CheckBox(this);c.setText(s);content.addView(c);} }
    void videos(){content.addView(text("Exercise videos",21));content.addView(text("Open guided beginner-friendly exercise searches. Choose a pace that feels comfortable.",14));link("NHS strength and flexibility exercises","https://www.nhs.uk/live-well/exercise/strength-and-flexibility-exercises/");link("Beginner chair exercises on YouTube","https://www.youtube.com/results?search_query=beginner+chair+exercises");}
    void link(String label,String url){Button b=new Button(this);b.setText(label);b.setAllCaps(false);b.setOnClickListener(v->startActivity(new Intent(Intent.ACTION_VIEW,android.net.Uri.parse(url))));content.addView(b);}
}
