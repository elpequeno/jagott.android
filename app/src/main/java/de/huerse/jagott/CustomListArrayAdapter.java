package de.huerse.jagott;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by André on 29.03.2015.
 */
public class CustomListArrayAdapter extends ArrayAdapter<String> {

    Context mContext;
    String[] mTitelArray;
    String[] mDateArray;

    String mSelectedName;

    CustomListArrayAdapter(Context c, String[] titles, String[] dates)
    {
        super(c,R.layout.favorite_single_row,R.id.listItemText, titles);
        this.mContext = c;
        this.mTitelArray = titles;
        this.mDateArray = dates;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.favorite_single_row, parent, false);

        TextView listItemText = (TextView) rowView.findViewById(R.id.listItemText);
        TextView listItemDate = (TextView) rowView.findViewById(R.id.listItemDate);

        listItemDate.setText(mDateArray[position]);

        mSelectedName = mTitelArray[position];
        listItemText.setText(mSelectedName);

        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.GlobalMainActivity.onNavigationDrawerItemSelected(21);

                TextView tv = (TextView) v;
                mSelectedName = tv.getText().toString();

                //Read selected favorite text from databse
                new FavoriteDisplayTask().execute(mSelectedName);
            }
        });

        listItemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.GlobalMainActivity.onNavigationDrawerItemSelected(21);


                TextView tv = (TextView) v;
                String selectedDate = tv.getText().toString();

                DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
                db.open();

                Cursor cur = db.getRecord(selectedDate, true);

                Global.GlobalMainActivity.setTitle(cur.getString(1));

                //Read selected favorite text from databse
                new FavoriteDisplayTask().execute(cur.getString(1));
            }
        });
        return rowView;
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
                TextView titleView = (TextView) Global.GlobalMainActivity.findViewById(R.id.titleView);
                titleView.setText(cur.getString(1));

                TextView dateView = (TextView) Global.GlobalMainActivity.findViewById(R.id.dateView);
                dateView.setText(cur.getString(2));
                TextView verse = (TextView) Global.GlobalMainActivity.findViewById(R.id.verseView);
                verse.setText(cur.getString(3));
                TextView messageView = (TextView) Global.GlobalMainActivity.findViewById(R.id.messageView);
                messageView.setText(cur.getString(4));
                TextView noteView = (TextView) Global.GlobalMainActivity.findViewById(R.id.noteView);
                noteView.setText(cur.getString(5));

                //Global.GlobalMainActivity.setButtonListenerforFavoriteTextView();

                Button editButton = (Button) Global.GlobalMainActivity.findViewById(R.id.edit_btn);
                editButton.setOnClickListener(new EditButtonListener());

                Button deleteButton = (Button) Global.GlobalMainActivity.findViewById(R.id.delete_btn);
                deleteButton.setOnClickListener(new DeleteButtonListener());

                Button backButton = (Button) Global.GlobalMainActivity.findViewById(R.id.btnBack);
                backButton.setOnClickListener(new BackButtonListener());

            }
            catch(Exception e)
            {}

        }
    }// end async task

    private class EditButtonListener implements View.OnClickListener {

        public void onClick(View v) {

            AlertDialog.Builder adb = new AlertDialog.Builder(Global.GlobalMainActivity);
            adb.setTitle("Notiz editieren:");

            DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
            db.open();

            Cursor cur = db.getRecord(mSelectedName);

            final EditText note = new EditText(Global.GlobalMainActivity);
            note.setHint("Deine Notiz.");
            note.setText(cur.getString(5));

            adb.setView(note);
            db.close();

            adb.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    //Action for 'Ok' Button
                    Toast.makeText(Global.GlobalMainActivity, "Notiz geändert!", Toast.LENGTH_SHORT).show();

                    DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
                    db.open();

                    Cursor cur = db.getRecord(mSelectedName);
                    db.updateRecord(cur.getLong(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4), note.getText().toString());
                    db.close();

//                    android.support.v4.app.FragmentManager fragmentManager = Global.GlobalMainActivity.getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.container, PlaceholderFragment.newInstance(5))
//                            .commit();
                }
            });


            adb.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    // Action for 'Cancel' Button

                }
            });
            adb.show();
            return;
        }


    }

    private class DeleteButtonListener implements View.OnClickListener {

        public void onClick(View v) {

            AlertDialog.Builder adb = new AlertDialog.Builder(Global.GlobalMainActivity);
            adb.setTitle("Willst du diesen Text löschen?");

            //Declare Variables as final for use in inner class

            adb.setPositiveButton("Ja", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    //Action for 'Ok' Button
                    Toast.makeText(Global.GlobalMainActivity, "Text gelöscht!", Toast.LENGTH_SHORT).show();

                    DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
                    db.open();
                    db.deleteRecord(mSelectedName);
                    db.close();

//                    android.support.v4.app.FragmentManager fragmentManager = Global.GlobalMainActivity.getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.container, PlaceholderFragment.newInstance(5))
//                            .commit();
                }
            });


            adb.setNegativeButton("Nein", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    // Action for 'Cancel' Button

                }
            });
            adb.show();
            return;
        }
    }

    private class BackButtonListener implements View.OnClickListener {

        public void onClick(View v) {

            Global.GlobalMainActivity.onNavigationDrawerItemSelected(4);
        }
    }
}
