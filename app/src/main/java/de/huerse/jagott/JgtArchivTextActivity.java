package de.huerse.jagott;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.widget.Toast;

import java.util.ArrayList;

import de.huerse.jagott.Adapters.JgtHeuteRVAdapter;

public class JgtArchivTextActivity extends AppCompatActivity {

    public ArrayList<String> mJgtHeuteResult;
    RecyclerView rv;
    CollapsingToolbarLayout mCollapsingToolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archiv_text);

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
                share();
            }
        });
    }

    public void share()
    {
        Toast.makeText(this, "Teilen", Toast.LENGTH_SHORT).show();

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = Global.GlobalJaGottCurrentText;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "JA-GOTT - Deine Andachtsapp\n");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
