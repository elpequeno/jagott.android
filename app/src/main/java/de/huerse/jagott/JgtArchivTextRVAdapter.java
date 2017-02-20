package de.huerse.jagott;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by André on 17.07.2015.
 */
public class JgtArchivTextRVAdapter extends RecyclerView.Adapter<JgtArchivTextRVAdapter.JgtArchivTextViewHolder>{

    String m_JgtArchivTextResult;

    JgtArchivTextRVAdapter(String jgtArchivTextResult){
        this.m_JgtArchivTextResult = jgtArchivTextResult;
    }

    public static class JgtArchivTextViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView titleView;


        JgtArchivTextViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cv.setCardBackgroundColor(Color.LTGRAY);
            titleView = (TextView)itemView.findViewById(R.id.titleView);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public JgtArchivTextViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jgt_heute, viewGroup, false);
        JgtArchivTextViewHolder jgtvh = new JgtArchivTextViewHolder(v);
        return jgtvh;
    }

    @Override
    public void onBindViewHolder(JgtArchivTextViewHolder jgtArchivViewHolder, int i) {
        new JaGottParser().clickOnArchiveText(m_JgtArchivTextResult);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}