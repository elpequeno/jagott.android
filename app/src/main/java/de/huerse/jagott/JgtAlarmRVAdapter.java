package de.huerse.jagott;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by André on 17.07.2015.
 */
public class JgtAlarmRVAdapter extends RecyclerView.Adapter<JgtAlarmRVAdapter.JgtAlarmViewHolder>{

    List<String> m_JgtAlarmResult;

    JgtAlarmRVAdapter(){
    }

    public static class JgtAlarmViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView infoView;
        TextView alarmInfoView;
        Button alarmSetButton;
        Button alarmCancleButton;
        TimePicker alarmTimePicker;

        JgtAlarmViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cv.setCardBackgroundColor(Color.LTGRAY);
            infoView = (TextView)itemView.findViewById(R.id.infoView);
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
        jgtAlarmViewHolder.infoView.setText("Möchtest du erinnert werden Ja-Gott zu lesen?");
        jgtAlarmViewHolder.infoView.setTextColor(Global.GlobalMainActivity.getResources().getColor(R.color.black));
        jgtAlarmViewHolder.alarmInfoView.setText("Keine Erinnerung eingestellt!");
        jgtAlarmViewHolder.infoView.setTextColor(Global.GlobalMainActivity.getResources().getColor(R.color.black));
        set_timepicker_text_color(jgtAlarmViewHolder);
        jgtAlarmViewHolder.alarmSetButton.setTextColor(Global.GlobalMainActivity.getResources().getColor(R.color.white));
        jgtAlarmViewHolder.alarmCancleButton.setTextColor(Global.GlobalMainActivity.getResources().getColor(R.color.white));
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

        NumberPicker hour_numberpicker = (NumberPicker) jgtAlarmViewHolder.alarmTimePicker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) jgtAlarmViewHolder.alarmTimePicker.findViewById(minute_numberpicker_id);
        //NumberPicker ampm_numberpicker = (NumberPicker) jgtAlarmViewHolder.alarmTimePicker.findViewById(ampm_numberpicker_id);

        set_numberpicker_text_color(hour_numberpicker);
        set_numberpicker_text_color(minute_numberpicker);
        //set_numberpicker_text_color(ampm_numberpicker);
    }

    private void set_numberpicker_text_color(NumberPicker number_picker){
        final int count = number_picker.getChildCount();
        final int color = Global.GlobalMainActivity.getResources().getColor(R.color.black);

        for(int i = 0; i < count; i++){
            View child = number_picker.getChildAt(i);

            try{
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);

                ((Paint)wheelpaint_field.get(number_picker)).setColor(color);
                ((EditText)child).setTextColor(color);
                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
            }
            catch(IllegalAccessException e){
            }
            catch(IllegalArgumentException e){
            }
        }
    }
}