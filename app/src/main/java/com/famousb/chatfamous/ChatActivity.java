package com.famousb.chatfamous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private String mDisplayName;
    private ListView mChatListView;
    private EditText mMessageInputText;
    private ImageButton mSendBtn;

    //database
    private DatabaseReference mDatabaseReference;

    private ChatListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //localy save username
        setDisplayName();

        //database instance
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();


        mChatListView = findViewById(R.id.chat_list_view);
        mMessageInputText = findViewById(R.id.msg_type);
        mSendBtn = findViewById(R.id.btn_chat_send);

        mMessageInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                sendMessage();
                return true;
            }
        });


    }


    //shared preferences
    private void setDisplayName(){
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
            mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY,null);
            if(mDisplayName == null){
                mDisplayName = "Anonymous";
            }
    }


    public void sendChatBtn(View view) {
        sendMessage();
    }

    private void sendMessage(){

        Log.d("ChatFamous", "i press send");

        String input = mMessageInputText.getText().toString();

        if(!input.equals("")){

            InstantMessage chat = new InstantMessage(input, mDisplayName);
            mDatabaseReference.child("messages").push().setValue(chat);
            mMessageInputText.setText("");
            Toast.makeText(this,"sent", Toast.LENGTH_SHORT).show();

        }
    }

    //adapter setup
    @Override
    public void onStart()
    {
        super.onStart();
        mAdapter = new ChatListAdapter(this, mDatabaseReference, mDisplayName);
        mChatListView.setAdapter(mAdapter);
    }

    //remove adapter listener
    @Override
    public void onStop(){
        super.onStop();

        mAdapter.cleanUp();
    }
}
