package com.example.nicolassaad.todolistapp;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * TO DO LIST APP DOCUMENTATION:
 * ----------------------
 * What to work on for Version 7:
 * -When a list gets deleted the items come back when a new list gets created in its place, make sure data gets deleted
 * -Use the title of the app to display the name of the list, and get rid of the TextView title in the MainActivity
 * -Prevent the user from hitting enter on the both EditText boxes or make hitting enter mimic the fab button
 * -Using longClicks to enter in Edit Mode, then use short clicks to select different items to edit.
 * -Find a way to add a "How to use this app" maybe figure out how to use the settings menu, explain that the grumpy cat
 * fab button is how you add a list or item. Or make a toast or snackbar that loads when the app launches
 *-----------------------
 * Current Working Version: 6
 *-----------------------
 * Features added in Version 6:
 * -Added new UI design and implemented better UX. Now the user can utilize an edit mode where they can
 * edit and delete entire list and also items.
 * -If there is only one list or item in the ListView, deleting it will automatically exit edit mode.
 * -The fab button is now hidden while edit mode is active to prevent confusion for the user.
 * -The user can edit a list or item all they want but if they delete an item or list they need to
 * long click on a new item or list to edit or delete it.
 * -The hint in the editText now displays the currently selected item because I couldn't figure out how to
 * highlight an item in ListView. This helps the user remember what item or list they are currently editing
 * while in edit mode.
 * -Added a method that checks to see if the user entered the word "list" in their list and doesn't add
 * the word "list" to the title if they did so. Otherwise the word "list" is added to the title.
 *-----------------------
 */

public class MainActivity extends AppCompatActivity {

    // My arrayList of arrayLists
    ArrayList<ArrayList<String>> mMasterDataList;

    EditText newListName;
    ListView listOfLists;
    Button mEditButton;
    Button mDeleteButton;
    Button mDoneButton;
    // The listAdapter for the MainActivity
    ArrayAdapter<String> mAdapter;
    //The arrayList that stores list items using the arrayAdapter
    private ArrayList<String> mArrayList;
    // The request code that we use to send data on an intent
    private static final int MAIN_REQUEST_CODE = 27;
    // data key to retrieve data from intent. Public so we can retrieve data in DetailActivity
    public static final String DATA_KEY = "myDataKey";
    public static final String DATA_INDEX_KEY = "myIndexKey";
    public static final int ERROR_INDEX = -2;
    public static final String DATA_LIST_TITLE = "newListTitle";

    //My intent that is used to pass array data to the ResultActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //The initial arrayList being instantiated
        mArrayList = new ArrayList();

        // Adding my mArrayList to the ArrayList of ArrayLists
        mMasterDataList = new ArrayList<>();

        // Creating the intent for the MainActivity
        //Setting up our views and buttons
        newListName = (EditText) findViewById(R.id.new_list_edit);
        listOfLists = (ListView) findViewById(R.id.list_of_lists_view);
        mEditButton = (Button) findViewById(R.id.edit_button);
        mDeleteButton = (Button) findViewById(R.id.delete_button);
        mDoneButton = (Button) findViewById(R.id.done_button);
        //The arrayAdapter being instantiated
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArrayList);
        // Setting the ArrayAdapter to the listOfLists listView.
        listOfLists.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        // OnItemClickListener that lets user click on item and takes them to ResultActivity


        // Setting the fab button up for use
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // The onClick Listener for the fab button that adds new lists to the list of lists
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInputsLists = newListName.getText().toString();
                //Checks if the editText is empty and doesn't let the user enter an empty list name
                if (!userInputsLists.isEmpty()) {
                    mArrayList.add(userInputsLists);
                    ArrayList<String> placeHolderList = new ArrayList<>();
                    mMasterDataList.add(placeHolderList);
                    mAdapter.notifyDataSetChanged();
                    newListName.getText().clear();
                } else {
                    Snackbar.make(view, "Please enter a name for your list", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        listOfLists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                Snackbar.make(view, "Entered edit mode", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mEditButton.setVisibility(View.VISIBLE);
                mDeleteButton.setVisibility(View.VISIBLE);
                mDoneButton.setVisibility(View.VISIBLE);
                newListName.setHint("Currently Selected: " + mArrayList.get(position));
                mAdapter.notifyDataSetChanged();
                fab.setVisibility(View.GONE);
                // Sets the color of the Delete button back to red if it was greyed out before
                mDeleteButton.setBackgroundResource(android.R.color.holo_red_light);
                mDeleteButton.setTypeface(null, Typeface.NORMAL);
                mEditButton.setBackgroundResource(android.R.color.white);
                mEditButton.setTypeface(null, Typeface.NORMAL);

                mDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newListName.setHint("Deleted: " + mArrayList.get(position));
                        mArrayList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        mEditButton.setClickable(false);
                        Snackbar.make(view, "List Deleted", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        // Runs the code in the Done button if there are no lists to edit or delete anymore
                        if (mArrayList.isEmpty()) {
                            mEditButton.setVisibility(View.GONE);
                            mDeleteButton.setVisibility(View.GONE);
                            mDoneButton.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                            newListName.getText().clear();
                            newListName.setHint("Enter New List Name");
                            fab.setVisibility(View.VISIBLE);
                            Snackbar.make(v, "List deleted, Exited Edit Mode", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        mDeleteButton.setClickable(false);
                        mDeleteButton.setBackgroundResource(android.R.color.darker_gray);
                        mDeleteButton.setTypeface(null, Typeface.ITALIC);
                        mEditButton.setClickable(false);
                        mEditButton.setBackgroundResource(android.R.color.darker_gray);
                        mEditButton.setTypeface(null, Typeface.ITALIC);
                        mAdapter.notifyDataSetChanged();
                    }
                });

                mEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!newListName.getText().toString().isEmpty()) {
                            mArrayList.set(position, newListName.getText().toString());
                            newListName.setHint("Currently Editing: " + mArrayList.get(position));
                            mAdapter.notifyDataSetChanged();
                            newListName.getText().clear();
                        } else {
                            Snackbar.make(view, "Please enter a new name for your list", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
                return true;
            }
        });

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditButton.setVisibility(View.GONE);
                mDeleteButton.setVisibility(View.GONE);
                mDoneButton.setVisibility(View.GONE);
                newListName.getText().clear();
                newListName.setHint("Enter New List Name");
                mAdapter.notifyDataSetChanged();
                fab.setVisibility(View.VISIBLE);
                Snackbar.make(v, "Exited Edit Mode", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listOfLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // THe onClick Listener that takes the user to the ResultActivity when an item is clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Putting the name of the item that the user taps on from the mArrayList into the intent using the putExtra method
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra(DATA_LIST_TITLE, mArrayList.get(position));

                // NEW CODE
                intent.putExtra(DATA_INDEX_KEY, position);
                intent.putExtra(DATA_KEY, mMasterDataList.get(position));

                // start activity and EXPECT a result back for THIS REQUEST CODE
                startActivityForResult(intent, MAIN_REQUEST_CODE);
            }
        });

    } // End of onCreate method

    // Begin MainActivity methods

    /**
     * Gets the data back from the ResultActivity and assigns it to the master list
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
                    //NEW CODE
                    ArrayList<String> tempItemList = data.getStringArrayListExtra(DATA_KEY);
                    int index = data.getIntExtra(DATA_INDEX_KEY, ERROR_INDEX);
                    if (index != ERROR_INDEX) {
                        mMasterDataList.set(index, tempItemList);
                    }
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
