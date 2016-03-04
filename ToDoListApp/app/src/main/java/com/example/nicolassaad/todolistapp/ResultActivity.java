package com.example.nicolassaad.todolistapp;

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
import android.widget.Toast;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    //The editText box used for adding items
    EditText addItem;
    // The listView in this activity
    ListView currentList;
    //The textView that holds the name of the list
    TextView titleListText;
    // ArrayList for a specific list that happens to be groceries right now
    ArrayList<String> mListArray;

    //The arrayAdapter that populates our listview with an ArrayList
    ArrayAdapter<String> mAdapter2;
    // My original ArrayList for this activity
    ArrayList<String> mArrayList2;

    int[] colors = {
            R.color.colorPrimaryDark,
            R.color.colorAccent,
            android.R.color.transparent};
    int colorIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting up our views
        titleListText = (TextView) findViewById(R.id.current_list_text_view);
        addItem = (EditText) findViewById(R.id.current_list_edit_text);
        currentList = (ListView) findViewById(R.id.current_list_view);

        //The catch bundle being defined and named extras
        Bundle extras = getIntent().getExtras();
        //Taking the extras bundle and assigning the ArrayList values to my new mListArray
        mListArray = extras.getStringArrayList("bundleKey1");

        //mArrayList2 = new ArrayList();
        //mAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArrayList2);
        mAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mListArray);

        //Setting the adapter to the listView in this activity
        currentList.setAdapter(mAdapter2);

        //Grabbing the name of the list from the MainActivity and assigning it to a String called listTitle
        String listTitle = (String) getIntent().getExtras().get("newListTitle");
        //Assigning listTitle to the textView title of the Result Activity
        titleListText.setText(listTitle);

        String Initials = "NAS";
        if (mListArray == null) {
            Toast.makeText(this, "mListArray is empty", Toast.LENGTH_SHORT).show();
            Log.i(Initials, "mListArray is empty");
        }

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

        currentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                /**
                 * WOULD LIKE TO USE A CUSTOM ADAPTER TO USE IMAGES OF A CHECKBOX THAT IS
                 * UNCHECKED ON EACH ITEM OF THE LIST AND CLICKING THE ITEM CHANGES THE IMAGE TO A
                 * CHECKED BOX. USING COLORS FOR NOW
                 */
                if (colorIndex < colors.length -1) {
                    colorIndex++;
                } else {
                    colorIndex = 0;
                }
                textView.setBackgroundResource(colors[colorIndex]);

            }
        });

        // The floating action button listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

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
    }

}
