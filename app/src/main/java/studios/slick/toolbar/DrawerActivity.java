package studios.slick.toolbar;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class DrawerActivity extends ActionBarActivity {

    Toolbar t;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private RelativeLayout mRelativeLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle,mTitle;

    private String[] navMenuItems;
    private SpannableString[] navmenuItems;
    private ArrayAdapter<SpannableString> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        t=(Toolbar)findViewById(R.id.mytoolbardrawer);
        setSupportActionBar(t);

        initStuffs();

        if (savedInstanceState == null) {
           new SliderMenuClickListener().displayView(0);
        }
    }

    private void initStuffs() {
        mTitle=mDrawerTitle=getTitle();
        navMenuItems=getResources().getStringArray(R.array.Titles);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList=(ListView)findViewById(R.id.list_slidermenu);
        mRelativeLayout=(RelativeLayout)findViewById(R.id.rel_drawer);

        navmenuItems=new SpannableString[navMenuItems.length];
        for(int i=0;i<navMenuItems.length;i++) {
            navmenuItems[i] = new SpannableString(Html.fromHtml("<font color='#000000'>" + navMenuItems[i] + "</font>"));
        }
        adapter=new ArrayAdapter<SpannableString>(DrawerActivity.this,android.R.layout.simple_list_item_1,navmenuItems);
        mDrawerList.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle=new ActionBarDrawerToggle(DrawerActivity.this,mDrawerLayout,t,R.string.app_name,R.string.app_name){
            public void onDrawerClosed(View view){
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View view){
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new SliderMenuClickListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen=mDrawerLayout.isDrawerOpen(mRelativeLayout);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitle=title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SliderMenuClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            displayView(position);
        }

        private void displayView(int position) {
            Fragment fragment=null;
            switch (position){
                case 0:
                        fragment=new HomeFragment();
                        break;
                case 1:
                    fragment=new HomeFragment();
                    break;
                case 2:
                    fragment=new HomeFragment();
                    break;
                case 3:
                    fragment=new HomeFragment();
                    break;
                default:break;
            }
            if(fragment!=null){
                FragmentManager fm=getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.frame_container,fragment).commit();
                mDrawerList.setItemChecked(position,true);
                mDrawerList.setSelection(position);
                setTitle(navMenuItems[position]);
                mDrawerLayout.closeDrawer(mRelativeLayout);
            }else{
                Log.e("DrawerActivity","Error creating fragment");
            }
        }

    }
}
