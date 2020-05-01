package com.hku.cs_cinema;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectMovie extends ListActivity{

    ArrayList<Map<String, Object>> list = new ArrayList< Map<String, Object> >();
    ArrayList<Map<String, Object>> list_internal = new ArrayList< Map<String, Object> >();
    public static int m_id = 0;
    public static String m_name;
    public static String m_date;
    public static String m_time;
    public static String m_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = this.getIntent();
        ArrayList<String> Name = intent.getStringArrayListExtra("Name");
        ArrayList<String> Category = intent.getStringArrayListExtra("Category");
        ArrayList<String> Duration = intent.getStringArrayListExtra("Duration");
        ArrayList<String> Date = intent.getStringArrayListExtra("Date");
        ArrayList<String> Time = intent.getStringArrayListExtra("Time");
        ArrayList<String> Price = intent.getStringArrayListExtra("Price");

        for( int i = 0; i < Name.size(); i++ ){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "Name", Name.get(i) );
            map.put( "Category", "Category: "+Category.get(i) );
            map.put( "Duration", "Duration: "+Duration.get(i) );
            map.put( "Date", "Date: "+Date.get(i) );
            map.put( "Time", "Time: "+Time.get(i) );
            map.put( "Price", "Price: HKD"+Price.get(i) );
            list.add(map);
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put( "Name", Name.get(i) );
            map2.put( "Category", Category.get(i) );
            map2.put( "Duration", Duration.get(i) );
            map2.put( "Date", Date.get(i) );
            map2.put( "Time", Time.get(i) );
            map2.put( "Price", Price.get(i) );
            list_internal.add(map2);
        }

        SimpleAdapter adapter = new SimpleAdapter(	this, list, R.layout.row_item,
                new String[]{"Name","Category","Duration","Date","Time","Price"},
                new int[]{R.id.Name, R.id.Category,R.id.Duration,R.id.Date,R.id.Time,R.id.Price}	);
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                m_id = position;
                m_name = list_internal.get(position).get("Name").toString();
                m_date = list_internal.get(position).get("Date").toString();
                m_time = list_internal.get(position).get("Time").toString();
                m_price = list_internal.get(position).get("Price").toString();
                Intent intent = new Intent(getBaseContext(), SelectSeat.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
