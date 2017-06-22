package de.huerse.jagott.Adapters;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.huerse.jagott.R;

/**
 * CRV Adapter for JaGottHeute
 */
public class JgtHeuteRVAdapter extends RecyclerView.Adapter<JgtHeuteRVAdapter.JgtHeuteViewHolder>{

    private List<String> m_JgtHeuteResult;

    public JgtHeuteRVAdapter(List<String> jgtHeuteResult){
        this.m_JgtHeuteResult = jgtHeuteResult;
    }

    static class JgtHeuteViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView dateView;
        TextView verseView;
        TextView messageView;

        JgtHeuteViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cv.setCardBackgroundColor(Color.LTGRAY);
            dateView = (TextView)itemView.findViewById(R.id.dateView);
            verseView = (TextView)itemView.findViewById(R.id.verseView);
            messageView = (TextView)itemView.findViewById(R.id.messageView);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public JgtHeuteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jgt_heute, viewGroup, false);
        return new JgtHeuteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(JgtHeuteViewHolder jgtHeuteViewHolder, int i) {
        jgtHeuteViewHolder.dateView.setText(m_JgtHeuteResult.get(0));
        jgtHeuteViewHolder.verseView.setText(m_JgtHeuteResult.get(1));
        jgtHeuteViewHolder.messageView.setText(m_JgtHeuteResult.get(2));

        if(Build.VERSION.SDK_INT >= 11) {
            jgtHeuteViewHolder.dateView.setTextIsSelectable(true);
            jgtHeuteViewHolder.verseView.setTextIsSelectable(true);
            jgtHeuteViewHolder.messageView.setTextIsSelectable(true);
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}