package de.huerse.jagott.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import de.huerse.jagott.R;

/**
 * RV Adapter for Alarm View
 */
public class JgtAlarmRVAdapter extends RecyclerView.Adapter<JgtAlarmRVAdapter.JgtAlarmViewHolder>{

    public JgtAlarmRVAdapter(){
    }

    public static class JgtAlarmViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        TextView alarmInfoView;
        Button alarmSetButton;
        Button alarmCancleButton;
        TimePicker alarmTimePicker;

        JgtAlarmViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout)itemView.findViewById(R.id.ll);
            //ll.setBackgroundColor(Color.LTGRAY);
            alarmInfoView = (TextView)itemView.findViewById(R.id.alarmInfo);
            alarmSetButton = (Button)itemView.findViewById(R.id.alarmSetButton);
            alarmCancleButton = (Button)itemView.findViewById(R.id.alarmCancleButton);
            alarmTimePicker = (TimePicker)itemView.findViewById(R.id.alarmTimePicker);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public JgtAlarmViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jgt_alarm, viewGroup, false);
        JgtAlarmViewHolder jgtvh = new JgtAlarmViewHolder(v);
        return jgtvh;
    }

    @Override
    public void onBindViewHolder(JgtAlarmViewHolder jgtAlarmViewHolder, int i) {
        jgtAlarmViewHolder.alarmInfoView.setText("Keine Erinnerung eingestellt!");
        set_timepicker_text_color(jgtAlarmViewHolder);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    Resources system;

    private void set_timepicker_text_color(JgtAlarmViewHolder jgtAlarmViewHolder){
        system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        //int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");

        jgtAlarmViewHolder.alarmTimePicker.setIs24HourView(true);
        NumberPicker hour_numberpicker = (NumberPicker) jgtAlarmViewHolder.alarmTimePicker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) jgtAlarmViewHolder.alarmTimePicker.findViewById(minute_numberpicker_id);
        //NumberPicker ampm_numberpicker = (NumberPicker) jgtAlarmViewHolder.alarmTimePicker.findViewById(ampm_numberpicker_id);

        //set_numberpicker_text_color(hour_numberpicker);
        //set_numberpicker_text_color(minute_numberpicker);
        //set_numberpicker_text_color(ampm_numberpicker);
    }
}