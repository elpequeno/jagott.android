package de.huerse.jagott.Adapters;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.huerse.jagott.Global;
import de.huerse.jagott.KontaktData;
import de.huerse.jagott.R;

/**
 * RV Adapter for "Kontakte"
 */
public class JgtKontaktRVAdapter extends RecyclerView.Adapter<JgtKontaktRVAdapter.JgtKontaktViewHolder>{

    List<KontaktData> m_JgtKontaktResult;

    public JgtKontaktRVAdapter(List<KontaktData> jgtKontaktResult){
        this.m_JgtKontaktResult = jgtKontaktResult;
    }

    public static class JgtKontaktViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView mail;
        ImageView image;
        TextView about;


        JgtKontaktViewHolder(View itemView) {
            super(itemView);

            cv = (CardView)itemView.findViewById(R.id.cv);
            cv.setCardBackgroundColor(Color.LTGRAY);
            image = (ImageView)itemView.findViewById(R.id.image);
            name= (TextView)itemView.findViewById(R.id.name);
            mail= (TextView)itemView.findViewById(R.id.mail);
            about= (TextView)itemView.findViewById(R.id.about);
        }
    }

    @Override
    public int getItemCount() {
        return m_JgtKontaktResult.size();
    }

    @Override
    public JgtKontaktViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jgt_kontakt_card, viewGroup, false);
        return new JgtKontaktViewHolder(v);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(JgtKontaktViewHolder jgtKontaktViewHolder, int i) {

        final String authorName = m_JgtKontaktResult.get(i).name;
        final String authorMail = m_JgtKontaktResult.get(i).mail;
        final String authorJob = m_JgtKontaktResult.get(i).job;
        final String authorAbout = m_JgtKontaktResult.get(i).about;
        final int photoId = m_JgtKontaktResult.get(i).photoId;

        jgtKontaktViewHolder.name.setText(m_JgtKontaktResult.get(i).name);
        jgtKontaktViewHolder.mail.setText(m_JgtKontaktResult.get(i).mail);


//        if ( m_JgtKontaktResult.get(i).name == "Habt ihr Fragen oder Anregungen?")
//        {
//            jgtKontaktViewHolder.image.getLayoutParams().height = 0;
//            jgtKontaktViewHolder.image.getLayoutParams().width = 0;
//            jgtKontaktViewHolder.image.requestLayout();
//            jgtKontaktViewHolder.about.setText("");
//        }
//        else
//        {
            jgtKontaktViewHolder.image.requestLayout();
            String replaceMailHtml = new String("<a href=\"mailto:" + m_JgtKontaktResult.get(i).mail + "\"> " + m_JgtKontaktResult.get(i).mail + " </a>");
            Spanned replaceMail;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                replaceMail = Html.fromHtml(replaceMailHtml,Html.FROM_HTML_MODE_LEGACY);
            } else {
                replaceMail = Html.fromHtml(replaceMailHtml);
            }
            jgtKontaktViewHolder.mail.setText(replaceMail);
            jgtKontaktViewHolder.mail.setMovementMethod(LinkMovementMethod.getInstance());

            jgtKontaktViewHolder.about.setText("\nmehr...");
            jgtKontaktViewHolder.about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // custom dialog
                    final Dialog dialog = new Dialog(Global.GlobalMainActivity);
                    dialog.setContentView(R.layout.jgt_kontakt_card_detail);
                    dialog.setTitle("Title...");

                    // set the custom dialog components - text, image and button
                    TextView name = (TextView) dialog.findViewById(R.id.name);
                    TextView mail = (TextView) dialog.findViewById(R.id.mail);
                    TextView about = (TextView) dialog.findViewById(R.id.about);
                    ImageView image = (ImageView) dialog.findViewById(R.id.image);


                    name.setText(authorName);
                    mail.setText(authorJob);
                    about.setText(authorAbout);
                    image.setImageResource(photoId);

                    ImageView back = (ImageView) dialog.findViewById(R.id.backButton);
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            });
//        }

        jgtKontaktViewHolder.image.setImageResource(m_JgtKontaktResult.get(i).photoId);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}