package de.huerse.jagott;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import de.huerse.jagott.Adapters.JgtHeuteRVAdapter;

//import android.support.annotation.NonNull;

//import android.support.annotation.NonNull;

public class JgtActivityFavoriteText extends AppCompatActivity {

    public ArrayList<String> mJgtHeuteResult;
    RecyclerView rv;
    CollapsingToolbarLayout mCollapsingToolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Toolbar mToolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            ArrayList<String> result = new ArrayList<>();
            result.add(0, mJgtHeuteResult.get(0));
            result.add(1, mJgtHeuteResult.get(1));
            result.add(2, mJgtHeuteResult.get(2));
            JgtHeuteRVAdapter adapter;
            switch (item.getItemId()) {
                case R.id.text_tab:
                    adapter = new JgtHeuteRVAdapter(result);
                    rv.setAdapter(adapter);
                    return true;
                case R.id.note_tab:
                    result.set(0, "Notiz");
                    result.set(1, mJgtHeuteResult.get(3));
                    result.set(2, "\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    adapter = new JgtHeuteRVAdapter(result);
                    rv.setAdapter(adapter);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jgt_favorite_text_activity);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle b = getIntent().getExtras();
        if (b != null)
            mJgtHeuteResult = b.getStringArrayList("jgtHeuteResult");

        rv = (RecyclerView) findViewById(R.id.container);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        JgtHeuteRVAdapter adapter = new JgtHeuteRVAdapter(mJgtHeuteResult);
        rv.setAdapter(adapter);


        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBar= (AppBarLayout)  findViewById(R.id.appbar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if ((mCollapsingToolbar.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(mCollapsingToolbar))) {
                    mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                } else {
                    mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                //refreshItems();
                // Load complete
                // Stop refresh animation
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editNote();
            }
        });
    }

    public void editNote()
    {
        final String selectedDate = mJgtHeuteResult.get(0);

        DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
        db.open();

        Cursor cur = db.getRecord(selectedDate, true);
        cur.getString(5); //5 is the column "notes"

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Zu Favoriten hinzufügen");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        //final EditText name = new EditText(this);
        //name.setHint("Gebe einen Titel ein.");
        //layout.addView(name);

        final EditText note = new EditText(this);
        note.setText(mJgtHeuteResult.get(3));
        layout.addView(note);

        adb.setView(layout);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Action for 'Ok' Button
                DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
                db.open();

                //String save_name = name.getText().toString();
                //if (save_name.isEmpty()) {
                //save_name = Global.GlobalJaGottCurrentDate;
                //}

                String mynote = note.getText().toString();
                if (mynote.isEmpty()) {
                    mynote = "-";
                }

                String save_name = selectedDate;
                long record_id = -1;
                Cursor cur = db.getRecord(save_name);
                record_id = db.updateRecord(cur.getLong(0),
                            cur.getString(2),
                            cur.getString(3),
                            cur.getString(4),
                            mynote);
                //db.close();

                //db.open();
                Cursor c = db.getRecord(record_id);
                if (c.moveToFirst()) {
                    mJgtHeuteResult.set(3, mynote);
                    Toast.makeText(Global.GlobalMainActivity,
                            "Notiz aktualisiert!", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(Global.GlobalMainActivity, "Fehler beim Aktualisieren der Notiz.", Toast.LENGTH_LONG).show();
                db.close();
            }
        });

        adb.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        adb.show();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

}
