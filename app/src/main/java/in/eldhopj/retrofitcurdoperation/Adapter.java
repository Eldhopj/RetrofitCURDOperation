package in.eldhopj.retrofitcurdoperation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.eldhopj.retrofitcurdoperation.ModelClass.User;

public class Adapter  extends RecyclerView.Adapter<Adapter.ViewHolder> { //Adapter


    List<User> mListItems; // List
    Context mContext;
    private OnItemClickListener mListener; // Listener for the OnItemClickListener interface

    //constructor
    public Adapter(List<User> listItems, Context context) { // constructor
        this.mListItems = listItems;
        this.mContext = context;
    }

    /**
     * interface will forward our click from adapter to our main activity
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {// this method calls when ever our view method is created , ie; the instance of ViewHolder class is created
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false); /**list_item-> is the Card view which holds the data in the recycler view*/
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//populate the data into the list_item (View Holder), as we scroll
        /**Binding data to the list_item*/
        User listitem = mListItems.get(position);
        holder.nameTV.setText(listitem.getName());
        holder.emailTV.setText(listitem.getEmail());
        holder.schoolTV.setText(listitem.getSchool());
    }

    @Override
    public int getItemCount() { // return the size of the list view , NOTE : this must be a fast process
        if (mListItems == null) {
            return 0;
        }
        return mListItems.size();
    }

    //View Holder class caches these references that gonna modify in the adapter
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Define viewHolder views (list_item) here
         */
        public TextView nameTV;
        public TextView emailTV;
        public TextView schoolTV;


        //create a constructor with itemView as a params
        public ViewHolder(View itemView) { // with the help of "itemView" we ge the views from xml
            super(itemView);
            /**bind views*/
            nameTV = itemView.findViewById(R.id.nameTV);
            emailTV = itemView.findViewById(R.id.emailTV);
            schoolTV = itemView.findViewById(R.id.schoolTV);

            /**Assigning on click listener on the item*/
            itemView.setOnClickListener(new View.OnClickListener() { // we can handle the click as like we do in normal
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition(); // Get the index of the view holder
                        if (position != RecyclerView.NO_POSITION) { // Makes sure this position is still valid
                            mListener.onItemClick(v,position); // we catch the click on the item view then pass it over the interface and then to our activity
                        }
                    }

                }
            });
        }
    }
}
