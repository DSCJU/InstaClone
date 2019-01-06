package com.sushilmaurya.instaclone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvFeeds;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvFeeds = findViewById(R.id.rv_feeds);
        rvFeeds.setHasFixedSize(true);
        rvFeeds.setLayoutManager(new LinearLayoutManager(this));


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Feeds");


        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(registerIntent);
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Post, PostHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostHolder>(
                Post.class,
                R.layout.post_card,
                PostHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(PostHolder viewHolder, Post model, int position) {
                Log.d("IMAGE", model.toString());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };

        rvFeeds.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_signout){
            firebaseAuth.signOut();
        }
        else if (id == R.id.add_icon) {
            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public static class PostHolder extends RecyclerView.ViewHolder{

        View view;
        public PostHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTitle(String title){
            TextView postTitle = view.findViewById(R.id.postTitle);
            postTitle.setText(title);
        }

        public void setDescription(String description){
            TextView postTitle = view.findViewById(R.id.postDescription);
            postTitle.setText(description);
        }

        public void setImage(Context context, String image){
            ImageView postImage = view.findViewById(R.id.postImage);
            Log.i("IMAGE", image);
            Glide.with(context).load(image).into(postImage);
        }
    }
}
