package studios.slick.toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class LaunchedActivity extends ActionBarActivity {

    ImageView iv;
    TextView tv_title,tv_link;
    Toolbar t;
    ImageButton fab;
    CountDownTimer ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launched);
        t=(Toolbar)findViewById(R.id.mytoolbarlaunched);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        t.setBackground(getResources().getDrawable(R.drawable.grad));
        setTitle(" ");
        iv=(ImageView)findViewById(R.id.imageView);
        fab=(ImageButton)findViewById(R.id.fab);
        tv_link=(TextView)findViewById(R.id.myLink);
        tv_title=(TextView)findViewById(R.id.myTitle);
        ct=new CountDownTimer(2000,2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                rollback();
            }
        };
        Intent i=getIntent();
        Bundle b=i.getExtras();
        tv_title.setText(b.getString("title"));
        tv_link.setText(b.getString("link"));
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.photo_2);
        Palette.generateAsync(bitmap,new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //Palette.Swatch vibrant=palette.getVibrantSwatch();
                //t.setBackgroundColor(vibrant.getRgb());
                //You can use these palette swatches and RippleDrawable class to dynamically change the colour of the FAB
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.getLayoutParams().height=ViewGroup.LayoutParams.MATCH_PARENT;
                fab.setVisibility(View.GONE);
                ct.start();
            }
        });
    }

    void rollback(){
        iv.getLayoutParams().height=(int)((getResources().getDisplayMetrics().density*300)+0.5f);
        fab.setVisibility(View.VISIBLE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launched, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
