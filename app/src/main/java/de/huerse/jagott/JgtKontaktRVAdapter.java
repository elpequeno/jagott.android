package de.huerse.jagott;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by André on 17.07.2015.
 */
public class JgtKontaktRVAdapter extends RecyclerView.Adapter<JgtKontaktRVAdapter.JgtKontaktViewHolder>{

    List<KontaktData> m_JgtKontaktResult;

    JgtKontaktRVAdapter(List<KontaktData> jgtKontaktResult){
        this.m_JgtKontaktResult = jgtKontaktResult;
    }

    public static class JgtKontaktViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView mail;
        ImageView image;

        JgtKontaktViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            image = (ImageView)itemView.findViewById(R.id.image);
            name= (TextView)itemView.findViewById(R.id.name);
            mail= (TextView)itemView.findViewById(R.id.mail);
        }
    }

    @Override
    public int getItemCount() {
        return m_JgtKontaktResult.size();
    }

    @Override
    public JgtKontaktViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jgt_kontakt_card, viewGroup, false);
        JgtKontaktViewHolder jgtvh = new JgtKontaktViewHolder(v);
        return jgtvh;
    }

    @Override
    public void onBindViewHolder(JgtKontaktViewHolder jgtKontaktViewHolder, int i) {

        jgtKontaktViewHolder.name.setText(m_JgtKontaktResult.get(i).name);
        jgtKontaktViewHolder.mail.setText(m_JgtKontaktResult.get(i).mail);

        if (i== 0)
        {
            jgtKontaktViewHolder.image.getLayoutParams().height = 0;
            jgtKontaktViewHolder.image.requestLayout();
        }
        else
        {
            jgtKontaktViewHolder.image.getLayoutParams().height = 400;
            jgtKontaktViewHolder.image.requestLayout();
            String replaceMail = new String("<a href=\"mailto:" + m_JgtKontaktResult.get(i).mail + "\"> " + m_JgtKontaktResult.get(i).mail + " </a>");
            jgtKontaktViewHolder.mail.setText(Html.fromHtml(replaceMail));
            jgtKontaktViewHolder.mail.setMovementMethod(LinkMovementMethod.getInstance());
        }

        jgtKontaktViewHolder.image.setImageResource(m_JgtKontaktResult.get(i).photoId);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}