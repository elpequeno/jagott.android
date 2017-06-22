package de.huerse.jagott.Adapters;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.huerse.jagott.DBAdapter;
import de.huerse.jagott.Global;
import de.huerse.jagott.R;

/**
 * Created by André on 17.07.2015.
 *
 * JgtFavoritesRVAdapter Class
 */
public class JgtFavoritesRVAdapter extends RecyclerView.Adapter<JgtFavoritesRVAdapter.JgtFavoritesViewHolder>{

    private List<String> m_JgtFavoritesResult;

    public JgtFavoritesRVAdapter(List<String> jgtFavoritesResult){
        this.m_JgtFavoritesResult = jgtFavoritesResult;
    }

    static class JgtFavoritesViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;

        JgtFavoritesViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView)itemView.findViewById(R.id.listItemText);
        }
    }

    @Override
    public int getItemCount() {
        return m_JgtFavoritesResult.size();
    }

    @Override
    public JgtFavoritesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favorite_single_row, viewGroup, false);
        return new JgtFavoritesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(JgtFavoritesViewHolder jgtFavoritesViewHolder, int i) {
        jgtFavoritesViewHolder.titleView.setText(m_JgtFavoritesResult.get(i));
        jgtFavoritesViewHolder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tv = (TextView) v;
                String selectedDate = tv.getText().toString();

                DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
                db.open();

                Cursor cur = db.getRecord(selectedDate, true);

                try{
                    Global.GlobalMainActivity.setTitle(cur.getString(1));

                    //Read selected favorite text from databse
                    new FavoriteDisplayTask().execute(cur.getString(1));
                }catch(Exception e)
                {}

            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Async Task to access the web
    private class FavoriteDisplayTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
            db.open();

            Cursor cur = db.getRecord(result);

            try{
                //TextView titleView = (TextView) Global.GlobalMainActivity.findViewById(R.id.titleView);
                //titleView.setText(cur.getString(1));

                Global.GlobalJaGottCurrentDate = cur.getString(2);
                Global.GlobalJaGottCurrentVerse = cur.getString(3);
                Global.GlobalJaGottCurrentMessage = cur.getString(4);

                ArrayList<String> jgtHeuteResult = new ArrayList<>();
                jgtHeuteResult.add(0,Global.GlobalJaGottCurrentDate );
                jgtHeuteResult.add(1, Global.GlobalJaGottCurrentVerse);
                jgtHeuteResult.add(2,Global.GlobalJaGottCurrentMessage + "\n\nNotiz:\n" + cur.getString(5) + "\n\n\n\n\n\n\n\n\n\n\n\n\n" );

                RecyclerView rv = (RecyclerView)Global.GlobalMainActivity.findViewById(R.id.container);

                JgtHeuteRVAdapter adapter = new JgtHeuteRVAdapter(jgtHeuteResult);
                rv.setAdapter(adapter);

            }
            catch(Exception e)
            {}

        }
    }// end async task
}