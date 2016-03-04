package com.example.nicolassaad.todolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private int index = 0;

    // A string tag for debugging (the fab button onClick listener)
    private static final String TAG = "ResultActivity: ";
    //The editText box used for adding items
    EditText addItem;
    // The listView in this activity
    ListView currentList;
    //The textView that holds the name of the list
    TextView titleListText;
    // ArrayList for a specific list that happens to be groceries right now
    private ArrayList<String> mListArray;
    //ArrayList<String> mArrayList;

    //The arrayAdapter that populates our listView with an ArrayList
    private ArrayAdapter<String> mAdapter2;

    // My array of colors that are used by the user to toggle the urgency of a list item
    int[] colors = {
            R.color.colorPrimaryDark,
            R.color.colorAccent,
            android.R.color.transparent};
    int colorIndex = 0;
    // ResultActivity intent used to send data back
    Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting up our views for this Activity
        titleListText = (TextView) findViewById(R.id.current_list_text_view);
        addItem = (EditText) findViewById(R.id.current_list_edit_text);
        currentList = (ListView) findViewById(R.id.current_list_view);

        /**
         * My method invocations-------------------------------------
         */
        // get the original list of data from main activity
        mListArray = getData();
        // Invokes the method that grabs the name of the list and makes it the title for the ResultActivity
        setListTitle();
        // Method that grabs the title of the list from the MainActivity using a bundle called extras
        getListTitle();
        // Sets up the arrayAdapter for the ResultActivity
        settingUpAdapter();
        /**
         * End of method invocations---------------------------------
         */

        /**
         * The floating action button on click listener
         */
        FloatingActionButton fab;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Hit the click listener");
                String userInputsLists = addItem.getText().toString();
                if (!userInputsLists.isEmpty()) {
                    //Lets the user add items to the mListArray populating the list
                    mListArray.add(userInputsLists);
                    // Notifying the adapter that there has been a change
                    mAdapter2.notifyDataSetChanged();
                    // Clears the editText box for new entries
                    addItem.getText().clear();
                } else {
                    // If the editText box is empty and the user clicks the fab button display an error
                    Snackbar.make(view, "Please enter a list item", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        /**
         * OnItemLongClick Listener deletes items from list
         */
        currentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mListArray.remove(position);
                mAdapter2.notifyDataSetChanged();
                Snackbar.make(view, "List Item Deleted", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });

        /**
         *  Allows list items to be assigned different colors
        */
        currentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                if (colorIndex < colors.length - 1) {
                    colorIndex++;
                } else {
                    colorIndex = 0;
                }
                textView.setBackgroundResource(colors[colorIndex]);
            }
        });

        // A null check for ResultActivity's listArray
        String Initials = "NAS";
        if (mListArray == null) {
            Log.i(Initials, "mListArray is empty");
        }
    } // --------End of onCreate method --------------------------------


    // ----------Start of my ResultActivity Methods---------------------
    /**
     * Sets up our arrayAdapter for the ResultActivity
     */
    private void settingUpAdapter() {
        //mAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArrayList2);
        mAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mListArray);
        //Setting the adapter to the listView in this activity
        currentList.setAdapter(mAdapter2);
        // A null check for ResultActivity's listArray
    }

    /**
     * The method that gets the title of the list from the bundle in the MainActivity
     */
    private void getListTitle() {
        //The catch bundle being defined and named extras
        Bundle extras = getIntent().getExtras();
        //Taking the extras bundle and assigning the ArrayList values to my new mListArray
        mListArray = extras.getStringArrayList("bundleKey1");
    }

    /**
     * Method that takes the list that the user clicked on and sets it to be the title in
     * ResultActivity
     */
    private void setListTitle() {
        //Grabbing the name of the list from the MainActivity and assigning it to a String called listTitle
        String listTitle = (String) getIntent().getExtras().get("newListTitle");
        //Assigning listTitle to the textView title of the Result Activity AND adds the word "list" to the title
        titleListText.setText(listTitle + " list");
    }

    /**
     * Gets the data from the MainActivity's intent
     *
     * @return
     */
    private ArrayList<String> getData() {
        returnIntent = getIntent();
        if (returnIntent == null) {
            return null;
        }
        return returnIntent.getStringArrayListExtra(MainActivity.DATA_KEY);
    }

    /**
     * Adding code to the back button
     */
    @Override
    public void onBackPressed() {
        sendDataBack();
        // NEED TO FIGURE OUT WHY PRESSING BACK DOESN'T GO BACK UNLESS HIT TWICE
        //When I hit back once, adding or removing items doesn't save
        //it only saves the items I manipulate in the first ResultActivity screen
    }

    /**
     * Handle what needs to happen to send data back
     */
    private void sendDataBack() {
        // Runs the method that sends the data back to the MainActivity
        sendNewListBack();
    }

    /**
     * Creates a new Intent to send the data back
     */
    private void sendNewListBack() {
        // Creates an intent that is used to send data back to the MainActivity
        Intent returnIntent = getIntent();
        // A null check for the intent
        if (returnIntent == null) {
            return;
        }
        // Putting the actual arrayList daya into the intent
        returnIntent.putExtra(MainActivity.DATA_KEY, mListArray);
        //NEW CODE
        //returnIntent.putExtra(MainActivity.DATA_INDEX_KEY, index);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
