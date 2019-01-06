package com.sushilmaurya.instaclone;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private EditText etTitle, etDescription;
    Uri imageUri;

    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    public void postButtonClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            imageUri = data.getData();
            ImageButton imageSelect = findViewById(R.id.imageButtonImageSelect);
            imageSelect.setImageURI(imageUri);
        }
    }

    public void submitButtonClicked(View view) {
        final String title = etTitle.getText().toString().trim();
        final String description = etDescription.getText().toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)){
            StorageReference filePath = storageReference.child("PostImages").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        databaseReference = firebaseDatabase.getReference().child("Feeds");
                        DatabaseReference currentNode = databaseReference.push();
                        currentNode.child("title").setValue(title);
                        currentNode.child("description").setValue(description);
                        currentNode.child("image").setValue(downloadUrl.toString());
                        Toast.makeText(getApplicationContext(), "ImagePosted", Toast.LENGTH_LONG).show();
                    }
                }
            );
        }
    }
}
