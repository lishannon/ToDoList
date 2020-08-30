package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// adapter:
// taking the data at a position to a view holder
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public interface OnClickListener {
        void onItemClicked(int position);
    }
    public interface OnlongClickListener {
        void onItemLongClicked(int position);
    }
    List<String> listOfTask;
    OnlongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemsAdapter(List<String> listOfTask, OnlongClickListener longClickListener,OnClickListener clickListener) {
        this.listOfTask= listOfTask;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    //creating each view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wrap it inside of the viewholder
        // use layout inflator to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        return new ViewHolder(todoView);
    }

    @Override
    //taking data positon to view holder
    //binding data to a particular viewholder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     // grab the item at the position
        String listOfTasks =  listOfTask.get(position);
        holder.bind(listOfTasks);
    }

    @Override
    //tell the recview  how many item there is
    public int getItemCount() {
        return listOfTask.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }
        // updated the view inside the view holder with this data
        public void bind(String listOfTask) {
            tvItem.setText(listOfTask);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //notify adapter that an item is inserted
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
