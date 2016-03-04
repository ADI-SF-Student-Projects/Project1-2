package com.example.nicolassaad.todolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    EditText newListName;
    ListView listOfLists;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> mArrayList;
    String bundleKey = "bundleKey1";

    ArrayList<String> mMainList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // An example of item that will populate the ResultActivity ListView (will be deleted eventually)
//        mMainList.add("lactose milk");
//        mMainList.add("eggs");
//        mMainList.add("lettuce");

        //Setting up our views
        newListName = (EditText) findViewById(R.id.new_list_edit);
        listOfLists = (ListView) findViewById(R.id.list_of_lists_view);
        //my
        mArrayList = new ArrayList();

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArrayList);
        // Setting the ArrayAdapter to the listOfLists listview.
        listOfLists.setAdapter(mAdapter);

        // OnItemClickListener that lets user click on item and takes them to ResultActivity
        listOfLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Creating a new intent
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                // Putting the name of the item that the user taps on from the mArrayList into the intent using the putExtra method
                intent.putExtra("newListTitle", mArrayList.get(position));
                // Creating a throw bundle
                Bundle bundle1 = new Bundle();
                // Using the throw bundle to put data from the mMainList ArrayList to the ResultActivity
                bundle1.putStringArrayList(bundleKey, mMainList);
                // Puts the bundle of data in our intent
                intent.putExtras(bundle1);
                // Starts our intent
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInputsLists = newListName.getText().toString();
                //Checks if the editText is empty and doesn't let the user enter an empty list name
                if (!userInputsLists.isEmpty()) {
                    mArrayList.add(userInputsLists);
                    mAdapter.notifyDataSetChanged();
                    newListName.getText().clear();
                } else {
                    Snackbar.make(view, "Please enter a name for your list", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });

      /*  listOfLists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mMainList.remove(position);
                Snackbar.make(view, "List Item Deleted", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mAdapter.notifyDataSetChanged();

                return false;
            }
        });*/

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
