package studios.slick.toolbar;

import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;


public class DrawerActivity extends ActionBarActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    Toolbar t;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private RelativeLayout mRelativeLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle,mTitle;

    private String[] navMenuItems;
    private SpannableString[] navmenuItems;
    private ArrayAdapter<SpannableString> adapter;

        //Logcat TAG
    private static final String TAG="DrawerActivity";
    private static final int PROFILE_PIC_SIZE=200, RC_SIGN_IN=0;
        //Google API client
    private GoogleApiClient mGoogleApiClient;
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */

    private boolean mIntentInProgress, mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private RoundedImageView mProfilePic;
    private TextView tvName,tvEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        t=(Toolbar)findViewById(R.id.mytoolbardrawer);
        setSupportActionBar(t);
        initStuffs();
        initgplus();


        if (savedInstanceState == null) {
           new SliderMenuClickListener().displayView(0);
        }
    }

    private void initgplus() {

        //Google+ Init stuff
        btnSignIn=(SignInButton)findViewById(R.id.btn_sign_in);
        btnSignOut=(Button)findViewById(R.id.btn_sign_out);
        btnRevokeAccess=(Button)findViewById(R.id.btn_revoke_access);
        mProfilePic=(RoundedImageView)findViewById(R.id.img_dp);
        tvName=(TextView)findViewById(R.id.tVName);
        tvEmail=(TextView)findViewById(R.id.tVEmail);

        //Google+ Button Click handlers
        btnSignOut.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        //Google+ API Client init stuff
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
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

    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop(){
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_sign_in:
                signInWithGplus();
                break;
            case R.id.btn_sign_out:
                signOutFromGplus();
                break;
            case R.id.btn_revoke_access:
                revokeGplusAccess();
        }
    }

    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }

                    });
        }
    }

    private void signInWithGplus() {
        if(!mGoogleApiClient.isConnecting()){
            mSignInClicked=true;
            resolveSignInError();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked=false;
        Toast.makeText(this,"Connected",Toast.LENGTH_SHORT).show();
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null);
        getProfileInfo();
        updateUI(true);
    }

    private void getProfileInfo() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String photoURL = currentPerson.getImage().getUrl();
                String profileURL = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                tvEmail.setText(email);
                tvName.setText(personName);
                Log.i(TAG, "Name: " + personName + ", plusProfile: "
                        + profileURL + ", email: " + email
                        + ", Image: " + photoURL);

                /*
                By default the profile url gives 50x50 px image only
                we can replace the value with whatever dimension we want by
                replacing sz=X.
                */

                photoURL = photoURL.substring(0, photoURL.length() - 2) + PROFILE_PIC_SIZE;
                new LoadProfileImage(mProfilePic).execute(photoURL+"");
            } else {
                Toast.makeText(this, "Person info is null", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    private void updateUI(boolean isSignedIn) {
        if(isSignedIn){
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
        }else{
            Drawable d;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                d=getDrawable(R.drawable.photo_4);
            }else{
                d=getResources().getDrawable(R.drawable.photo_4);
            }
            mProfilePic.setImageDrawable(d);
            tvEmail.setText("");
            tvName.setText("");
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
        }
    }
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if(!result.hasResolution()){
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),this,0).show();
            return;
        }
        if(!mIntentInProgress){
            mConnectionResult=result;
            if(mSignInClicked){

               /* The user has already clicked 'sign-in' so we attempt to
                resolve all
                errors until the user is signed in, or they cancel.*/

                resolveSignInError();
            }
        }
/*
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(result.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }*/

    }

    private void resolveSignInError() {
        if(mConnectionResult.hasResolution()){
            try{
                mIntentInProgress=true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress=false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent){
        if(requestCode==RC_SIGN_IN){
            if(responseCode!=RESULT_OK){
                mSignInClicked=false;
            }

            mIntentInProgress=false;

            if(!mGoogleApiClient.isConnecting()){
                mGoogleApiClient.connect();
            }
        }
    }

    private class LoadProfileImage extends AsyncTask<String,Void,Bitmap>{
        ImageView bw;

        public LoadProfileImage(ImageView b){
            bw=b;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url=urls[0];
            Bitmap icon=null;
            try{
                InputStream in=new java.net.URL(url).openStream();
                icon= BitmapFactory.decodeStream(in);
            }catch (Exception e){
                e.printStackTrace();
                Log.e("Error",e.getMessage());
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result){
            bw.setImageBitmap(result);
        }
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
