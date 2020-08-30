package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
//import android.os.FileUtils;
import org.apache.commons.io.FileUtils;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List; // option +enter to import library

public class MainActivity extends AppCompatActivity {

   public static final String KEY_ITEM_TEXT = "item_text";
   public static final String KEY_ITEM_POSITION = "item_position";
   public static final int EDIT_TEXT_CODE = 29;

    List<String> listOfTask;

    Button btnAdd;
    EditText etItem;
    RecyclerView recItem;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // allow us to see the ui

        btnAdd = findViewById(R.id.btnAdd); // reference
        etItem = findViewById(R.id.etItem);
        recItem = findViewById(R.id.recItem);

        //etItem.setText( "Hello, task");// automatically type on the box

        loadItems();

        ItemsAdapter.OnlongClickListener onLongClickListener=  new ItemsAdapter.OnlongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                // delete the idea item
                listOfTask.remove(position);
                // notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener(){
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position" + position);
                //create the new activity- intents to create a flow
                Intent i = new Intent(MainActivity.this, EditPage.class); // (the current instance of the activity, where we want to go)
                //pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, listOfTask.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //display the activity
                startActivityForResult(i,EDIT_TEXT_CODE);// expect the result
            }
        };
        itemsAdapter = new ItemsAdapter(listOfTask, onLongClickListener,onClickListener);
        recItem.setAdapter(itemsAdapter);
        recItem.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                String toDoItem = etItem.getText().toString();
                // add item to the model
                listOfTask.add(toDoItem);
                // notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(listOfTask.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();;
            }
        });

    }
    // handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //retrieve the edit text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //get the modified text position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            // update the model with the new modified text
            listOfTask.set(position,itemText);
            // notify the adapter for the new change
            itemsAdapter.notifyItemChanged(position);
            // persist the change
            Toast.makeText(getApplicationContext(), "Item was modified", Toast.LENGTH_SHORT).show();
            saveItems();
        } else {
            Log.w("MainActivity", "Unknown call to the MainActivity");
        }
    }

    private File getDataFile(){
        return new File (getFilesDir(), "data.txt");
    }

    // fuction: load function by reading every line at the data file
    private void loadItems() {
        try {
            listOfTask = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e){
            Log.e("MainActivity", "Error reading item",e );
            listOfTask = new ArrayList<>();
        }
    }

    // function saves items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),listOfTask);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing item",e );
        }
    }
}