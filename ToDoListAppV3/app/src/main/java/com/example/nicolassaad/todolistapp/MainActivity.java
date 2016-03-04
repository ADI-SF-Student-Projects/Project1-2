package com.example.nicolassaad.todolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    // My arrayList of arrayLists
    ArrayList<ArrayList<String>> mMasterDataList;

    EditText newListName;
    ListView listOfLists;
    // The listAdapter for the MainActivity
    ArrayAdapter<String> mAdapter;
    //The arrayList that stores list items using the arrayAdapter
    private ArrayList<String> mArrayList;
    // The String key that is used to the pass the list title to the ResultActivity
    String bundleKey = "bundleKey1";
    // The request code that we use to send data on an intent
    private static final int MAIN_REQUEST_CODE = 27;
    // data key to retrieve data from intent. Public so we can retrieve data in DetailActivity
    public static final String DATA_KEY = "myDataKey";
    public static final String DATA_INDEX_KEY = "myDataKey";
    public static final int ERROR_INDEX = -2;

    //My intent that is used to pass array data to the ResultActivity
    Intent intent;
    // The array that catches the data back from the ResultActivity
    private ArrayList<String> mMainList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //index = getIndex();

        // Adding my mArrayList to the ArrayList of ArrayLists
        mMasterDataList = new ArrayList<>();
        mMasterDataList.add(mArrayList);

        // Creating the intent for the MainActivity
        intent = new Intent(MainActivity.this, ResultActivity.class);
        //Setting up our views
        newListName = (EditText) findViewById(R.id.new_list_edit);
        listOfLists = (ListView) findViewById(R.id.list_of_lists_view);
        //The initial arrayList being instantiated
        mArrayList = new ArrayList();
        //The arrayAdapter being instantiated
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArrayList);
        // Setting the ArrayAdapter to the listOfLists listView.
        listOfLists.setAdapter(mAdapter);
        // OnItemClickListener that lets user click on item and takes them to ResultActivity

        listOfLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // THe onClick Listener that takes the user to the ResultActivity when an item is clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Putting the name of the item that the user taps on from the mArrayList into the intent using the putExtra method
                intent.putExtra("newListTitle", mArrayList.get(position));
                // Creating a throw bundle
                Bundle bundle1 = new Bundle();
                // Using the throw bundle to put data from the mMainList ArrayList to the ResultActivity
                bundle1.putStringArrayList(bundleKey, mMainList);
                // Puts the bundle of data in our intent
                intent.putExtras(bundle1);
                // Starts our intent
                //startActivity(intent);

                // NEW CODE
//                intent.putExtra(DATA_INDEX_KEY, 0);
//                intent.putExtra(DATA_KEY, mMasterDataList.get(0));

                // OLD CODE
                intent.putExtra(DATA_KEY, mArrayList);

                // start activity and EXPECT a result back for THIS REQUEST CODE
                startActivityForResult(intent, MAIN_REQUEST_CODE);
            }
        });
        // Setting the fab button up for use
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // The onClick Listener for the fab button that adds new lists to the list of lists
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
    } // End of onCreate method

    // Begin MainActivity methods
    /**
     * Gets the data back from the ResultActivity and assigns it the mMainList
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // make sure returned request code is what we are expecting
        if (requestCode == MAIN_REQUEST_CODE) {
            // make sure results were handled correctly
            if (resultCode == RESULT_OK) {
                // null pointer check
                if (data != null) {
                    // update data list with the new data

                    // OLD CODE
                    mMainList = data.getStringArrayListExtra(DATA_KEY);

                    //NEW CODE
//                    ArrayList<String> tempItemList = data.getStringArrayListExtra(DATA_KEY);
//                    int index = data.getIntExtra(DATA_INDEX_KEY, -1);
//                    if (index != -1) {
//                        mMasterDataList.set(index, tempItemList);
//                    }
                }
            } else if (requestCode == RESULT_CANCELED) {
                Log.w("Main", "Failed to get new list back");
            }
        }
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
