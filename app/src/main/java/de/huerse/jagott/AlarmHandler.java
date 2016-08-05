package de.huerse.jagott;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by andre on 28.10.2014.
 */
public class AlarmHandler extends Activity implements View.OnClickListener {

    //Variables for Alarm
    protected static int HELLO_ID =5555;
    private Intent mAlarmIntent;
    Integer mAlarmHour;
    Integer mAlarmMinute;
    AlarmHandler mAlarmHandler;
    Context mApplicationContext;

    static TimePicker mSelectedTime;
    Button mAlarmSet;
    Button mAlarmCancel;
    TextView mAlarmInfo;

    AlarmHandler( Context context)
    {
        mApplicationContext = context;

        mAlarmHandler = this;
        mAlarmHour  = null;
        mAlarmMinute = null;

        mAlarmIntent = new Intent(mApplicationContext, AlarmReceiver.class);
        mAlarmIntent.putExtra("title","Heute schon gelesen?");
        mAlarmIntent.putExtra("note", " Viel Spaß dabei!");

        if( this.readAlarmFromFile())
        {
            this.setAlarm(mAlarmMinute, mAlarmHour);
        }
    }

    public void onClick(View v)
    {
        if( v == mAlarmSet)
        {
            mAlarmHour = mSelectedTime.getCurrentHour();
            mAlarmMinute = mSelectedTime.getCurrentMinute();
            cancelAlarm();
            setAlarm(mAlarmMinute, mAlarmHour);
            writeAlarmToFile(mAlarmMinute, mAlarmHour);
            mAlarmSet.setText("Neue Zeit speichern");
            mAlarmCancel.setEnabled(true);
            if( mAlarmMinute < 10)
            {
                mAlarmInfo.setText("Tägliche Erinnerung um: " + mAlarmHour + ":0" + mAlarmMinute + " Uhr");
            }
            else
            {
                mAlarmInfo.setText("Tägliche Erinnerung um: " + mAlarmHour + ":" + mAlarmMinute + " Uhr");
            }
        }

        else if( v == mAlarmCancel)
        {
            cancelAlarm();
            mAlarmCancel.setEnabled(false);
            mAlarmInfo.setText("Keine Erinnerung eingestellt!");
        }
    }

    //Alarm
    public boolean IsAlarmSet() {
        return PendingIntent.getBroadcast(mApplicationContext, HELLO_ID, mAlarmIntent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    public void setAlarm(int alarmMinute, int alarmHour) {

        Calendar cal = Calendar.getInstance();       //for using this you need to import java.util.Calendar;

//			int day = cal.get(Calendar.DAY_OF_MONTH);
//			int month = cal.get(Calendar.MONTH);
//			int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        //cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        //cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        //cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, alarmHour);
        cal.set(Calendar.MINUTE, alarmMinute);
        cal.set(Calendar.SECOND, 0);

        int day = cal.get(Calendar.DAY_OF_MONTH);

        if( hour > alarmHour || ( hour == alarmHour && minute >= alarmMinute) )
        {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        day = cal.get(Calendar.DAY_OF_MONTH);
        PendingIntent sender = PendingIntent.getBroadcast(mApplicationContext, HELLO_ID, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);//)|  Intent.FILL_IN_DATA);

        AlarmManager am = (AlarmManager) mApplicationContext.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY , sender);
    }

    void cancelAlarm()
    {
        PendingIntent sender = PendingIntent.getBroadcast(mApplicationContext, HELLO_ID, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);//|  Intent.FILL_IN_DATA);
        AlarmManager am = (AlarmManager) mApplicationContext.getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        sender.cancel();

        mAlarmSet.setText("Erinnere mich!");
        mAlarmCancel.setEnabled(false);
        mAlarmInfo.setText("Keine Erinnerung eingestellt!");

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "jaGott" + File.separator + "alarm.jaGott");
        if(file.exists())
        {
            file.delete();
        }

    }

    void writeAlarmToFile(int alarmMinute, int alarmHour)
    {

        try {
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "jaGott");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            if (success) {
                // Do something on success
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "jaGott" + File.separator + "alarm.jaGott");
                file.createNewFile();
                byte[] data1={(byte) alarmMinute, (byte) alarmHour};
                //write the bytes in file
                if(file.exists())
                {
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(data1);
                    fo.close();
                }
            } else {
                // Do something else on failure
            }
        } catch (IOException ioe)
        {ioe.printStackTrace();}
    }

    boolean readAlarmFromFile()
    {
        byte[] data1 = new byte[2];

        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "jaGott" + File.separator + "alarm.jaGott");
            if(file.exists())
            {
                FileInputStream fi = new FileInputStream(file);
                fi.read(data1);
                fi.close();

                mAlarmMinute = (int) data1[0];
                mAlarmHour = (int) data1[1];

                return true;
            }

        } catch (IOException ioe)
        {
            return false;
        }

        return false;
    }


    public void executeJaGottAlarm()
    {
        new JaGottAlarm().execute();
    }

    private class JaGottAlarm extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                mSelectedTime = (TimePicker) Global.GlobalMainActivity.findViewById(R.id.alarmTimePicker);
                mSelectedTime.setIs24HourView(true);

                mAlarmInfo = (TextView) Global.GlobalMainActivity.findViewById(R.id.alarmInfo);
                mAlarmSet = (Button) Global.GlobalMainActivity.findViewById(R.id.alarmSetButton);
                mAlarmCancel = (Button) Global.GlobalMainActivity.findViewById(R.id.alarmCancleButton);
                mAlarmSet.setOnClickListener(mAlarmHandler);
                mAlarmCancel.setOnClickListener(mAlarmHandler);
                if (mAlarmHandler.IsAlarmSet()) {
                    mAlarmSet.setText("Neue Zeit speichern");
                    mAlarmCancel.setEnabled(true);
                    mSelectedTime.setCurrentHour(mAlarmHour);
                    mSelectedTime.setCurrentMinute(mAlarmMinute);
                    if (mAlarmHandler.mAlarmMinute < 10) {
                        mAlarmInfo.setText("Tägliche Erinnerung um: " +
                                mAlarmHandler.mAlarmHour + ":0" + mAlarmHandler.mAlarmMinute + " Uhr");
                    } else {
                        mAlarmInfo.setText("Tägliche Erinnerung um: " +
                                mAlarmHandler.mAlarmHour + ":" + mAlarmHandler.mAlarmMinute + " Uhr");
                    }
                } else {
                    mAlarmSet.setText("Erinnere mich!");
                    mAlarmCancel.setEnabled(false);
                    mAlarmInfo.setText("Keine Erinnerung eingestellt!");
                }
            }catch(Exception e)
            {
                //Do nothing!
            }
        }
    }

}