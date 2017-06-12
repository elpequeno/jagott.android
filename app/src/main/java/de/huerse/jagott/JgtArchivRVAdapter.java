package de.huerse.jagott;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by André on 17.07.2015.
 */
public class JgtArchivRVAdapter extends RecyclerView.Adapter<JgtArchivRVAdapter.JgtArchivViewHolder>{

    List<String> m_JgtArchivResult;

    JgtArchivRVAdapter(List<String> jgtArchivResult){
        this.m_JgtArchivResult = jgtArchivResult;
    }

    public static class JgtArchivViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl;
        TextView titleView;


        JgtArchivViewHolder(View itemView) {
            super(itemView);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            //rl.setBackgroundColor(Color.LTGRAY);
            titleView = (TextView)itemView.findViewById(R.id.titleView);
        }
    }

    @Override
    public int getItemCount() {
        return m_JgtArchivResult.size();
    }

    @Override
    public JgtArchivViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jgt_archiv_card, viewGroup, false);
        return new JgtArchivViewHolder(v);
    }

    @Override
    public void onBindViewHolder(JgtArchivViewHolder jgtArchivViewHolder, int i) {
        jgtArchivViewHolder.titleView.setText(m_JgtArchivResult.get(i));
        jgtArchivViewHolder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tv = (TextView) v;
                String selectedDate = tv.getText().toString();

                RecyclerView rv = (RecyclerView) Global.GlobalMainActivity.findViewById(R.id.container);
                JgtArchivTextRVAdapter archivTextAdapter = new JgtArchivTextRVAdapter(selectedDate);
                rv.setAdapter(archivTextAdapter);
            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}