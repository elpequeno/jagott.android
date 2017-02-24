package de.huerse.jagott;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class JaGottMain extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    CollapsingToolbarLayout mCollapsingToolbar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.anim_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.navigation_view)
    NavigationView mNavigationView;
    @InjectView(R.id.header)
    ImageView mImageHeaderView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    //@InjectView(R.id.drawer_recyclerView) RecyclerView drawerRecyclerView;

    private CharSequence mTitle;

    //Buttons
    ButtonHandler mButtonHandler;
    Button mBtnBigger;
    Button mBtnSmaller;
    Button mBtnBack;
    Integer mTextSize;

    //AlarmHandler für die Erinnerungsfunktion
    AlarmHandler mAlarmHandler;

    //Archiv Liste
    ListView mArchivListView;
    SwipeRefreshLayout swipeLayout;

    int mSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.JaGottLight);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.jgt_main_activity);

        ButterKnife.inject(this);

        Global.GlobalMainActivity = this;   //MainActivity in globaler Variable merken, um inn gesamter App darauf zugreifen zu können... schlechter Stil... naja

        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        //hide Floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        //Initialisierung der Variablen
        mAlarmHandler = new AlarmHandler(getApplicationContext());
        mTextSize = 14;
        mButtonHandler = new ButtonHandler();

        mTitle = getTitle();

        initializeGUI();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        mSection = 0;

        //this lines are needed to prevent title from vanishing when toolbar collapses
        setTitle(R.string.title_section1);
        new JaGottHeute().execute();

        // Set up the drawer.
        setUpNavDrawer();
    }

    private void initializeGUI() {
        ArrayList<String> jgtHeuteInit = new ArrayList<>();
        jgtHeuteInit.add(0, "-");
        jgtHeuteInit.add(1, "Lade...");
        jgtHeuteInit.add(2, "-");

        RecyclerView rv = (RecyclerView) findViewById(R.id.container);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        JgtHeuteRVAdapter adapter = new JgtHeuteRVAdapter(jgtHeuteInit);
        rv.setAdapter(adapter);
    }

    void refreshItems() {
        // Load items
        // ...

        switch(mSection)
        {
            case 0: //Ja-Gott-Heute
                new JaGottHeute().execute();
                break;
            case 1:
                new JaGottArchiv().execute();
                break;
            default:
                //doNothing
        }

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer_light);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                RecyclerView rv;
                FloatingActionButton fab;
                CoordinatorLayout.LayoutParams p;

                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.title_section1:
                        initializeGUI();
                        //this lines are needed to prevent title from vanishing when toolbar collapses
                        setTitle(R.string.title_section1);

                        //hide Floating action button
                        fab = (FloatingActionButton) findViewById(R.id.fab);
                        p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                        p.setAnchorId(R.id.container);
                        fab.setLayoutParams(p);
                        fab.setVisibility(View.GONE);

                        mImageHeaderView.setImageResource(R.drawable.image_heute2);
                        new JaGottHeute().execute();
                        mSection = 0;
                        mSwipeRefreshLayout.setEnabled(true);
                        //mCurrentSelectedPosition = 0;
                        break;
                    case R.id.title_section2:
                        //this lines are needed to prevent title from vanishing when toolbar collapses
                        setTitle(R.string.title_section2);
                        mImageHeaderView.setImageResource(R.drawable.header_image_1);
                        new JaGottArchiv().execute();
                        mSection = 1;
                        mSwipeRefreshLayout.setEnabled(true);
                        //mCurrentSelectedPosition = 1;
                        break;
                    case R.id.title_section3:
                        //this lines are needed to prevent title from vanishing when toolbar collapses
                        setTitle(R.string.title_section3);
                        mImageHeaderView.setImageResource(R.drawable.header_image_2);
                        ArrayList<KontaktData> kontakte = new ArrayList<KontaktData>();
                        kontakte.add(new KontaktData("Habt ihr Fragen oder Anregungen?", "Dann schreibt uns doch eine Mail!", R.drawable.ic_launcher)); //ic_empty
                        kontakte.add(new KontaktData("Michael Bayer", "michael@ja-gott.de", R.drawable.ic_michael));
                        kontakte.add(new KontaktData("Lena Vach", "lena@ja-gott.de", R.drawable.ic_lena));
                        kontakte.add(new KontaktData("Carina Pfeiffer", "carina@ja-gott.de", R.drawable.ic_carina));
                        kontakte.add(new KontaktData("Kerstin Penner", "kerstin@ja-gott.de", R.drawable.ic_kerstin));
                        kontakte.add(new KontaktData("André Klein", "andre@ja-gott.de", R.drawable.andre));
                        kontakte.add(new KontaktData("Martin Forell", "matin@ja-gott.de", R.drawable.ic_launcher));

                        rv = (RecyclerView) findViewById(R.id.container);

                        JgtKontaktRVAdapter kontaktAdapter = new JgtKontaktRVAdapter(kontakte);
                        rv.setAdapter(kontaktAdapter);
                        //mCurrentSelectedPosition = 0;
                        mSection = 2;
                        mSwipeRefreshLayout.setEnabled(false);
                        break;
                    case R.id.title_section4:
                        //this lines are needed to prevent title from vanishing when toolbar collapses
                        setTitle(R.string.title_section4);

                        //Set Adapter for Alarm
                        rv = (RecyclerView) findViewById(R.id.container);

                        JgtAlarmRVAdapter alarmAdapter = new JgtAlarmRVAdapter();
                        rv.setAdapter(alarmAdapter);


                        //Execute Alarm Handler
                        mAlarmHandler.executeJaGottAlarm();
                        //mCurrentSelectedPosition = 1;
                        mSection = 3;
                        mSwipeRefreshLayout.setEnabled(false);
                        break;
                    case R.id.title_section5:
                        fab = (FloatingActionButton) findViewById(R.id.fab);
                        p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                        p.setAnchorId(R.id.container);
                        fab.setLayoutParams(p);
                        fab.setVisibility(View.VISIBLE);
                        //fab.show();

                        //this lines are needed to prevent title from vanishing when toolbar collapses
                        setTitle(R.string.title_section5);
                        DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
                        db.open();

                        Cursor cursor = db.getAllRecords();
                        cursor.moveToFirst();

                        ArrayList<String> archivList = new ArrayList<>();
                        ArrayList<String> archivDateList = new ArrayList<>();

                        try {
                            do {
                                String name = cursor.getString(1);
                                archivList.add(name);
                                archivDateList.add(cursor.getString(2));
                            } while (cursor.moveToNext());
                        } catch (Exception e) {
                            archivList.add("Du hast bisher keine Liebligstexte gespeichert.");
                        }
                        //Set Adapter for Favorties
                        rv = (RecyclerView) findViewById(R.id.container);

                        JgtFavoritesRVAdapter favoritesAdapter = new JgtFavoritesRVAdapter(archivList);
                        rv.setAdapter(favoritesAdapter);
                        //mCurrentSelectedPosition = 0;
                        mSection = 4;
                        mSwipeRefreshLayout.setEnabled(false);
                        break;

                    default:
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        mCollapsingToolbar.setTitle(mTitle);
        mCollapsingToolbar.requestLayout();
        //getSupportActionBar().setTitle(mTitle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ja_gott_main, menu);
        //restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        if (item.getItemId() == R.id.action_share) {
            Toast.makeText(this, "Teilen", Toast.LENGTH_SHORT).show();

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = Global.GlobalJaGottCurrentText;
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "JA-GOTT - Deine Seite zum Auftanken\n");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            return true;
        }

        if (item.getItemId() == R.id.action_save) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Speichern");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText name = new EditText(this);
            name.setHint("Gebe einen Titel ein.");
            layout.addView(name);

            final EditText note = new EditText(this);
            note.setHint("Füge eine Notiz hinzu.");
            layout.addView(note);

            adb.setView(layout);

            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Action for 'Ok' Button
                    DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
                    db.open();

                    String save_name = name.getText().toString();
                    if (save_name.isEmpty()) {
                        save_name = Global.GlobalJaGottCurrentDate;
                    }

                    long record_id = -1;
                    Cursor cur = db.getRecord(save_name);
                    if (cur.getCount() == 0) {
                        record_id = db.insertRecord(save_name,
                                Global.GlobalJaGottCurrentDate,
                                Global.GlobalJaGottCurrentVerse,
                                Global.GlobalJaGottCurrentMessage,
                                note.getText().toString());
                    }
                    db.close();

                    db.open();
                    Cursor c = db.getRecord(record_id);
                    if (c.moveToFirst()) {
                        Toast.makeText(Global.GlobalMainActivity,
                                "Text gespeichert als: " + c.getString(1), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(Global.GlobalMainActivity, "Diesen Text hast du schon als Lieblingstext gespeichert.", Toast.LENGTH_LONG).show();
                    db.close();
                }
            });


            adb.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Action for 'Cancel' Button
                    Toast.makeText(Global.GlobalMainActivity, "Speichern abgebrochen.", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });
            adb.setIcon(R.drawable.ic_launcher);
            adb.show();

            return true;
        }

//        if (item.getItemId() == R.id.action_settings) {
//            Toast.makeText(getActivity(), "Einstellungen", Toast.LENGTH_SHORT).show();
//
//            DBAdapter db = new DBAdapter(getActivity());
//            db.open();
//            db.dropTable();
//            db.close();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        JaGottParser parser = new JaGottParser();
        parser.refreshJaGottHeute();
        parser.refreshJaGottArchiv();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 5000);
    }

    private class ButtonHandler implements View.OnClickListener {

        public void onClick(View v) {
            if (v == mBtnBigger) {
                if (mTextSize < 40) {
                    mTextSize += 2;
                }
                setTextViewSize();
            } else if (v == mBtnSmaller) {
                if (mTextSize > 8) {
                    mTextSize -= 2;
                }
                setTextViewSize();
            } else if (v == mBtnBack) {
                //Button_Back brings user back to fav list
            }
        }

        private void setTextViewSize() {
            TextView date = (TextView) findViewById(R.id.dateView);
            date.setTextSize(mTextSize);


            TextView verse = (TextView) findViewById(R.id.verseView);
            verse.setTextSize(mTextSize);

            TextView message = (TextView) findViewById(R.id.messageView);
            message.setTextSize(mTextSize);

            try {
                //TextView noteView = (TextView) findViewById(R.id.noteView);
                //noteView.setTextSize(mTextSize);
            } catch (Exception e) {
                //do nothing, because TextView does not exist...
            }
        }
    }

    /**
     * AsyncClass to load GUI for JaGottHeute
     */
    public class JaGottHeute extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                //mBtnBigger = (Button) findViewById(R.id.btnBigger);
                //mBtnBigger.setOnClickListener(mButtonHandler);

                //mBtnSmaller = (Button) findViewById(R.id.btnSmaller);
                //mBtnSmaller.setOnClickListener(mButtonHandler);

                new JaGottParser().refreshJaGottHeute();
                //mButtonHandler.setTextViewSize();

                //swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
                //swipeLayout.setOnRefreshListener(Global.GlobalMainActivity);

                //mImage = (ImageView) findViewById(R.id.image_heute_view);
                //ScrollView sv = (ScrollView) findViewById(R.id.ScrollView01);

                //sv.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {

                //    @Override
                //    public void onScrollChanged() {

                //       parallax(mImage);
                //DO SOMETHING WITH THE SCROLL COORDINATES

                //   }
                //});
            } catch (Exception e) {
            }

        }
    }

    /**
     * AsyncClass to load GUI for JaGottArchiv
     */
    private class JaGottArchiv extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {


            return "";//JaGottParser.parseJaGottOnline();
        }

        @Override
        protected void onPostExecute(String result) {
            new JaGottParser().refreshJaGottArchiv();


//            TextView archivLink = (TextView) findViewById(R.id.linkView);
//            if ( archivLink != null)
//            {
//                archivLink.setText(Html.fromHtml("<a href=\"http://ja-gott.de/index.php/ja-gott-heute/ja-gott-archiv\">Hier geht's zum Archiv!  </a>"));
//                archivLink.setMovementMethod(LinkMovementMethod.getInstance());
//            }
        }
    }


    private class JaGottFavorite extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            return "";//JaGottParser.parseJaGottOnline();
        }

        @Override
        protected void onPostExecute(String result) {
            DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
            db.open();

            Cursor cursor = db.getAllRecords();
            cursor.moveToFirst();

            ArrayList<String> archivList = new ArrayList<>();
            ArrayList<String> archivDateList = new ArrayList<>();

            try {
                do {
                    String name = cursor.getString(1);
                    archivList.add(name);
                    archivDateList.add(cursor.getString(2));
                } while (cursor.moveToNext());

                //convert ArrayLit to String[] for CustomListArrayAdapter
                String[] archiveStringlist = new String[archivList.size()];
                archiveStringlist = archivList.toArray(archiveStringlist);

                String[] archiveDateStringlist = new String[archivDateList.size()];
                archiveDateStringlist = archivDateList.toArray(archiveDateStringlist);

                mArchivListView = (ListView) findViewById(R.id.favorite_list);
                final CustomListArrayAdapter arrayAdapter = new CustomListArrayAdapter(Global.GlobalMainActivity,
                        archiveStringlist, archiveDateStringlist);
                mArchivListView.setAdapter(arrayAdapter);

                mArchivListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            TextView listItemText = (TextView) view.findViewById(R.id.listItemText);
                            listItemText.performClick();
                        } catch (Exception e) {
                            //do nothing
                        }
                    }
                });
            } catch (Exception e) {
                archivList.add("Du hast bisher keine Liebligstexte gespeichert.");
                mArchivListView = (ListView) findViewById(R.id.favorite_list);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        Global.GlobalMainActivity,
                        android.R.layout.simple_list_item_1, archivList);
                mArchivListView.setAdapter(arrayAdapter);
            }
        }
    }
}
