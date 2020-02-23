package com.famousb.chatfamous;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private String mDisplayName;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mSnapshotsList;




    //listener from database firebase
    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            mSnapshotsList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public ChatListAdapter(Activity activity,
                           DatabaseReference databaseReference,String displayName) {
        mActivity = activity;
        mDisplayName = displayName;
        mDatabaseReference = databaseReference.child("messages");
        mDatabaseReference.addChildEventListener(mListener);
        mSnapshotsList = new ArrayList<>();
    }

    static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams mParams;
    }

    @Override
    public int getCount() {
        return mSnapshotsList.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot = mSnapshotsList.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater;
        final  InstantMessage message;


        int layoutResources = 0;
        //our chat view type
        message = getItem(position);
        boolean isItMe = message.getAuthor().equals(mDisplayName);

        if(isItMe){

            layoutResources = R.layout.right_chat_bubbble;
            isItMe = true;
        }else{
            layoutResources = R.layout.left_chat_bubble;
            isItMe = false;
        }


        if(convertView == null){

            inflater = (LayoutInflater)
                    mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResources,parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.authorName = convertView.findViewById(R.id.txt_author);
            holder.body = convertView.findViewById(R.id.txt_msg);
            holder.mParams = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();

        String author = message.getAuthor();
        holder.authorName.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);

        return convertView;
    }

    //show display chat view
//    private void setChatRowAppearance(boolean isMe){
//
//        if(isMe){
//            //holder.mParams.gravity = Gravity.END;
//            layoutResources = R.layout.left_chat_bubble;
//        }else{
//            //holder.mParams.gravity = Gravity.START;
//            layoutResources = R.layout.right_chat_bubbble;
//        }
//
//
//        //holder.authorName.setLayoutParams(holder.mParams);
//        //holder.body.setLayoutParams(holder.mParams);
//
//    }

    public  void cleanUp(){
        mDatabaseReference.removeEventListener(mListener);
    }


}
