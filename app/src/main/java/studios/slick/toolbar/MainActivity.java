package studios.slick.toolbar;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity{

    Toolbar t;
    RecyclerView recyclerView;
    private String[] titles,links;
    private TypedArray images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t=(Toolbar)findViewById(R.id.mytoolbarmain);
        setSupportActionBar(t);
        recyclerView=(RecyclerView)findViewById(R.id.card_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        titles=getResources().getStringArray(R.array.Titles);
        links=getResources().getStringArray(R.array.Links);
        images=getResources().obtainTypedArray(R.array.Images);


        CardAdapter ca=new CardAdapter(getList());
        recyclerView.setAdapter(ca);

        ca.SetOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                final View img=v.findViewById(R.id.myCardImage),title=v.findViewById(R.id.myCardTitle),link=v.findViewById(R.id.myCardLink);
                Bundle b=new Bundle();
                b.putString("title", ((TextView) title).getText().toString());
                b.putString("link", ((TextView) link).getText().toString());
                Intent intent=new Intent(MainActivity.this,LaunchedActivity.class);
                intent.putExtras(b);
                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, Pair.create(img,"movingImage"),Pair.create(title,"movingTitle"),Pair.create(link,"movingLink"));
                ActivityCompat.startActivity(MainActivity.this,intent,options.toBundle());
            }
        });
    }

    private List<CardListView> getList() {
        List<CardListView> clv=new ArrayList<CardListView>();
        for(int i=0;i<6;i++){
            CardListView ci=new CardListView();
            ci.title=titles[i];
            ci.link=links[i];
            ci.image=images.getResourceId(i,-1);
            clv.add(ci);
        }
        images.recycle();
        return clv;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent i=new Intent(this,DrawerActivity.class);
            ActivityCompat.startActivity(this,i,null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
