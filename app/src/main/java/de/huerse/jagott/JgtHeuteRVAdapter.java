package de.huerse.jagott;

import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by André on 17.07.2015.
 */
public class JgtHeuteRVAdapter extends RecyclerView.Adapter<JgtHeuteRVAdapter.JgtHeuteViewHolder>{

    List<String> m_JgtHeuteResult;

    JgtHeuteRVAdapter(List<String> jgtHeuteResult){
        this.m_JgtHeuteResult = jgtHeuteResult;
    }

    public static class JgtHeuteViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView dateView;
        TextView verseView;
        TextView messageView;

        JgtHeuteViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
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
        JgtHeuteViewHolder jgtvh = new JgtHeuteViewHolder(v);
        return jgtvh;
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