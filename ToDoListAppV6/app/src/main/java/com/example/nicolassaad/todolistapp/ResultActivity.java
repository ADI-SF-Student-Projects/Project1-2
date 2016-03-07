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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private int index;

    // A string tag for debugging (the fab button onClick listener)
    private static final String TAG = "ResultActivity: ";
    //The editText box used for adding items
    EditText addItem;
    // The listView in this activity
    ListView currentList;
    //The textView that holds the name of the list
    TextView titleListText;
    Button mEditButton2;
    Button mDeleteButton2;
    Button mDoneButton2;
    // ArrayList for a specific list that happens to be groceries right now
    private ArrayList<String> mListArray;
    //ArrayList<String> mArrayList;

    //The arrayAdapter that populates our listView with an ArrayList
    private ArrayAdapter<String> mAdapter2;

    // My array of colors that are used by the user to toggle the urgency of a list item
    int[] colors = {
            android.R.color.darker_gray,
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

        //Setting up our views and buttons for this Activity
        titleListText = (TextView) findViewById(R.id.current_list_text_view);
        addItem = (EditText) findViewById(R.id.current_list_edit_text);
        currentList = (ListView) findViewById(R.id.current_list_view);
        mEditButton2 = (Button) findViewById(R.id.edit_button2);
        mDeleteButton2 = (Button) findViewById(R.id.delete_button2);
        mDoneButton2 = (Button) findViewById(R.id.done_button2);

        currentList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        /**
         * My method invocations-------------------------------------
         */
        mListArray = getDataList();
        index = getDataIndex();
        // Sets up the arrayAdapter for the ResultActivity
        settingUpAdapter();
        // Method that checks to see if the word "list" has been added by user when naming their list
        // makes sure that their list title won't read "..list list"
        addOrRemoveListToTitle();
        /**
         * End of method invocations---------------------------------
         */

        /**
         * The floating action button on click listener
         */
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

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


        currentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                Snackbar.make(view, "Editing list item", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mEditButton2.setVisibility(View.VISIBLE);
                mDeleteButton2.setVisibility(View.VISIBLE);
                mDoneButton2.setVisibility(View.VISIBLE);
                addItem.setHint("Currently Selected: " + mListArray.get(position));
                mAdapter2.notifyDataSetChanged();
                fab.setVisibility(View.GONE);
                // Sets the color of the Delete button back to red if it was greyed out before
                mDeleteButton2.setBackgroundResource(android.R.color.holo_red_light);
                mDeleteButton2.setTypeface(null, Typeface.NORMAL);
                mEditButton2.setBackgroundResource(android.R.color.white);
                mEditButton2.setTypeface(null, Typeface.NORMAL);


                mDeleteButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addItem.setHint("Deleted: " + mListArray.get(position));
                        mListArray.remove(position);
                        mAdapter2.notifyDataSetChanged();
                        mEditButton2.setClickable(false);
                        Snackbar.make(view, "Item Deleted", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        // Runs the code in the Done button if there are no lists to edit or delete anymore
                        if (mListArray.isEmpty()) {
                            mEditButton2.setVisibility(View.GONE);
                            mDeleteButton2.setVisibility(View.GONE);
                            mDoneButton2.setVisibility(View.GONE);
                            mAdapter2.notifyDataSetChanged();
                            addItem.getText().clear();
                            addItem.setHint("Enter New Item");
                            fab.setVisibility(View.VISIBLE);
                            Snackbar.make(v, "Item deleted, Exited Edit Mode", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        mDeleteButton2.setClickable(false);
                        mDeleteButton2.setBackgroundResource(android.R.color.darker_gray);
                        mDeleteButton2.setTypeface(null, Typeface.ITALIC);
                        mEditButton2.setClickable(false);
                        mEditButton2.setBackgroundResource(android.R.color.darker_gray);
                        mEditButton2.setTypeface(null, Typeface.ITALIC);
                    }
                });

                mEditButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!addItem.getText().toString().isEmpty()) {
                            mListArray.set(position, addItem.getText().toString());
                            addItem.setHint("Currently Selected: " + mListArray.get(position));
                            mAdapter2.notifyDataSetChanged();
                            addItem.getText().clear();
                        } else {
                            Snackbar.make(view, "Please enter an item", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
                return false;
            }
        });

        mDoneButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditButton2.setVisibility(View.GONE);
                mDeleteButton2.setVisibility(View.GONE);
                mDoneButton2.setVisibility(View.GONE);
                mAdapter2.notifyDataSetChanged();
                addItem.getText().clear();
                addItem.setHint("Enter items here");
                fab.setVisibility(View.VISIBLE);
                Snackbar.make(v, "Exited Edit Mode", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

                /**
                 *  Allows list items to be assigned different colors
                 */
                currentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
//                if (colorIndex < colors.length - 1) {
//                    colorIndex++;
//                } else {
//                    colorIndex = 0;
//                }
            }
        });


    } // --------End of onCreate method --------------------------------


    // ----------Start of my ResultActivity Methods---------------------
    private void addOrRemoveListToTitle() {
        if (!getListTitle().contains("list")) {
            titleListText.setText(getListTitle() + " list");
        } else {
            titleListText.setText(getListTitle());
        }
    }

    private ArrayList<String> getDataList() {
        Intent returnIntent = getIntent();
        if (returnIntent == null) {
            return new ArrayList<>();
        }
        return returnIntent.getStringArrayListExtra(MainActivity.DATA_KEY);
    }

    /**
     * Sets up our arrayAdapter for the ResultActivity
     */
    private void settingUpAdapter() {
        mAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mListArray);
        //Setting the adapter to the listView in this activity
        currentList.setAdapter(mAdapter2);
        mAdapter2.notifyDataSetChanged();
    }

    /**
     * The method that gets the title of the list from the bundle in the MainActivity
     */
    private String getListTitle() {
        Intent returnIntent = getIntent();
        if (returnIntent == null) {
            return "";
        }
        return returnIntent.getStringExtra(MainActivity.DATA_LIST_TITLE);

    }

    private int getDataIndex() {
        Intent returnIntent = getIntent();
        if (returnIntent == null) {
            return MainActivity.ERROR_INDEX;
        }
        return returnIntent.getIntExtra(MainActivity.DATA_INDEX_KEY, MainActivity.ERROR_INDEX);
    }

    /**
     * Adding code to the back button
     */
    @Override
    public void onBackPressed() {
        sendDataBack();
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
        // Putting the actual arrayList data into the intent
        returnIntent.putExtra(MainActivity.DATA_KEY, mListArray);
        //NEW CODE
        returnIntent.putExtra(MainActivity.DATA_INDEX_KEY, index);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
