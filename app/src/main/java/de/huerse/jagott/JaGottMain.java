package de.huerse.jagott;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.huerse.jagott.Adapters.JgtAlarmRVAdapter;
import de.huerse.jagott.Adapters.JgtFavoritesRVAdapter;
import de.huerse.jagott.Adapters.JgtHeuteRVAdapter;
import de.huerse.jagott.Adapters.JgtKontaktRVAdapter;
import de.huerse.jagott.alarm.AlarmHandler;

public class JaGottMain extends AppCompatActivity {

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
    Integer mTextSize;

    //AlarmHandler für die Erinnerungsfunktion
    AlarmHandler mAlarmHandler;

    int mSection;

    TextView date;
    TextView verse;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

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
        //hide Floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveText();
            }
        });
        // fab.hide();

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

        date = (TextView) findViewById(R.id.dateView);
        verse = (TextView) findViewById(R.id.verseView);
        message = (TextView) findViewById(R.id.messageView);

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
        initializeGUI();

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
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
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

                //hide Floating action button
                fab = (FloatingActionButton) findViewById(R.id.fab);
                p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                p.setAnchorId(R.id.collapsing_toolbar);
                fab.setLayoutParams(p);
                fab.setVisibility(View.GONE);

                switch (menuItem.getItemId()) {
                    case R.id.title_section1:
                        fab.setVisibility(View.VISIBLE);
                        initializeGUI();
                        //this lines are needed to prevent title from vanishing when toolbar collapses
                        setTitle(R.string.title_section1);

                        mImageHeaderView.setImageResource(R.drawable.heute);
                        new JaGottHeute().execute();
                        mSection = 0;
                        mSwipeRefreshLayout.setEnabled(true);
                        //mCurrentSelectedPosition = 0;
                        break;
                    case R.id.title_section2:
                        //this lines are needed to prevent title from vanishing when toolbar collapses
                        initializeGUI(); //um "Laden"-Text anzuzeigen
                        setTitle(R.string.title_section2);

                        mImageHeaderView.setImageResource(R.drawable.archiv);
                        new JaGottArchiv().execute();
                        mSection = 1;
                        mSwipeRefreshLayout.setEnabled(true);
                        //mCurrentSelectedPosition = 1;
                        break;
                    case R.id.title_section3:
                        setTitle(R.string.title_section3);
                        mImageHeaderView.setImageResource(R.drawable.team);
                        initializeGUI(); //um "Laden"-Text anzuzeigen

                        new JaGottTeam().execute();
                        break;
                    case R.id.title_section4:
                        //this lines are needed to prevent title from vanishing when toolbar collapses
                        setTitle(R.string.title_section4);
                        mImageHeaderView.setImageResource(R.drawable.clock);

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
                        //fab = (FloatingActionButton) findViewById(R.id.fab);
                        p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                        p.setAnchorId(R.id.container);
                        mImageHeaderView.setImageResource(R.drawable.favoriten);
                        //fab.setLayoutParams(p);
                        //fab.setVisibility(View.VISIBLE);
                        ////fab.show();

                        //this lines are needed to prevent title from vanishing when toolbar collapses
                        setTitle(R.string.title_section5);
                        DBAdapter db = new DBAdapter(Global.GlobalMainActivity);
                        db.open();

                        Cursor cursor = db.getAllRecords();
                        cursor.moveToFirst();

                        ArrayList<String> archivList = new ArrayList<>();

                        try {
                            do {
                                String name = cursor.getString(1);
                                archivList.add(name);
                            } while (cursor.moveToNext());
                        } catch (Exception e) {
                            archivList.add("Du hast bisher keine Liebligstexte gespeichert.");
                        }
                        //Set Adapter for Favorties
                        rv = (RecyclerView) findViewById(R.id.container);

                        JgtFavoritesRVAdapter favoritesAdapter = new JgtFavoritesRVAdapter(archivList);
                        rv.setAdapter(favoritesAdapter);

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
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "JA-GOTT - Deine Andachtsapp\n");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
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


    public void saveText(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Zu Favoriten hinzufügen");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        //final EditText name = new EditText(this);
        //name.setHint("Gebe einen Titel ein.");
        //layout.addView(name);

        final EditText note = new EditText(this);
        note.setHint("Füge eine Notiz hinzu");
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

                String save_name = Global.GlobalJaGottCurrentDate;
                long record_id = -1;
                Cursor cur = db.getRecord(save_name);
                if (cur.getCount() == 0) {
                    record_id = db.insertRecord(save_name,
                            Global.GlobalJaGottCurrentDate,
                            Global.GlobalJaGottCurrentVerse,
                            Global.GlobalJaGottCurrentMessage,
                            mynote);
                }
                //db.close();

                //db.open();
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
                dialog.cancel();
            }
        });
        adb.show();

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
    private class JaGottHeute extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            new JaGottParser().refreshJaGottHeute();
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

    /**
     * AsyncClass to load GUI for JaGottTeam
     */
    private class JaGottTeam extends AsyncTask<String, Void, String> {

        JgtKontaktRVAdapter kontaktAdapter;

        @Override
        protected String doInBackground(String... strings) {

            ArrayList<KontaktData> kontakte = new ArrayList<>();
            //kontakte.add(new KontaktData("Habt ihr Fragen oder Anregungen?", "\nDann schreibt uns doch eine Mail!", R.drawable.ic_launcher,"", "")); //ic_empty
            kontakte.add(new KontaktData("Michael Bayer", "michael@ja-gott.de", R.mipmap.michael, "Author", KontaktData.aboutMichael));
            kontakte.add(new KontaktData("Lena Vach", "lena@ja-gott.de", R.mipmap.lena, "Authorin", KontaktData.aboutLena));
            kontakte.add(new KontaktData("Carina Pfeiffer", "carina@ja-gott.de", R.mipmap.carina, "Authorin", KontaktData.aboutCarina));
            kontakte.add(new KontaktData("Kerstin Penner", "kerstin@ja-gott.de", R.mipmap.kerstin, "Authorin", KontaktData.aboutKerstin));
            kontakte.add(new KontaktData("Eva Dorothée Kurrer", "eva@ja-gott.de", R.mipmap.eva, "Authorin", KontaktData.aboutEva));
            kontakte.add(new KontaktData("Marcel Meaubert", "marcel@ja-gott.de", R.mipmap.marcel, "Author", KontaktData.aboutMarcel));
            kontakte.add(new KontaktData("Alexander Blümel", "alexander@ja-gott.de", R.mipmap.alexander, "Author", KontaktData.aboutAlexander));
            kontakte.add(new KontaktData("André Klein", "andre@ja-gott.de", R.mipmap.andre, "Technik", KontaktData.aboutAndre));
            kontakte.add(new KontaktData("Martin Forell", "martin@ja-gott.de", R.mipmap.martin, "Technik", KontaktData.aboutMartin));

            kontaktAdapter = new JgtKontaktRVAdapter(kontakte);
            return "";//JaGottParser.parseJaGottOnline();
        }

        @Override
        protected void onPostExecute(String result) {
            RecyclerView rv = (RecyclerView) findViewById(R.id.container);

            rv.setAdapter(kontaktAdapter);
            //mCurrentSelectedPosition = 0;
            mSection = 2;
            mSwipeRefreshLayout.setEnabled(false);
        }
    }
}
