package com.google.android.exoplayer.demo.materialdesignone;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.exoplayer.demo.Model.Channel;
import com.google.android.exoplayer.demo.PlayerActivity;
import com.google.android.exoplayer.demo.PrefActivity;
import com.google.android.exoplayer.demo.R;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    Toolbar toolbar;
    private static FirstTabFrag firstTabFrag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFrag drawerFrag = (NavigationDrawerFrag) getSupportFragmentManager().findFragmentById(R.id.fragmentDrawer);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerFrag.setUp(drawerLayout,toolbar);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        firstTabFrag = new FirstTabFrag();
        fragmentTransaction.add(R.id.fragment_place, firstTabFrag, "firstTabFrag");
        fragmentTransaction.commit();

    }

    public static void populateNewData(List<Channel> channels){
        Log.d("channels.size()", "==========================" + channels.size());
        firstTabFrag.getData().clear();
        for (Channel ch : channels) {
            firstTabFrag.getData().add(ch);
        }
        firstTabFrag.getRcAdapter().notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id ==R.id.action_settings){
            Intent intent = new Intent(this, PrefActivity.class);
            startActivity(intent);
        }

//        if (id==R.id.navigate){
//            Toast.makeText(getApplicationContext(), "Navigate", Toast.LENGTH_SHORT).show();
//        }
        return super.onOptionsItemSelected(item);
    }

}
