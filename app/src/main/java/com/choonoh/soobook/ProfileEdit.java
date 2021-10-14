package com.choonoh.soobook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEdit extends AppCompatActivity {

    private EditText nickname_et, status_et;
    private ImageButton back_btn;
    private TextView change_img;
    private Button profile_edit_btn;
    private CircleImageView profile_img;

    private DatabaseReference Profile_Edit_Ref;
    private StorageReference Profile_Image_Ref;
    private FirebaseAuth mAuth;

    private String currentUserId;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        Profile_Edit_Ref = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        Profile_Image_Ref = FirebaseStorage.getInstance().getReference().child("Profile Images");

        back_btn = (ImageButton)findViewById(R.id.back_btn);
        profile_img = (CircleImageView)findViewById(R.id.profile_img);
        change_img = (TextView)findViewById(R.id.change_img);
        nickname_et = (EditText)findViewById(R.id.nickname_et);
        status_et = (EditText)findViewById(R.id.status_et);
        profile_edit_btn = findViewById(R.id.profile_edit_btn);


        change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        Profile_Edit_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String image = dataSnapshot.child("profileimage").getValue().toString();

                    //하아 왜 안될까....
                    Picasso.get().load(image).placeholder(R.drawable.profile_first).into(profile_img);
                    //Picasso.get().load(Uri.parse(image)).placeholder(R.drawable.profile_first).into(profile_img);
                    //Picasso.with(ProfileEdit.this).load(image).placeholder(R.drawable.profile_first).into(profile_img);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null){
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                StorageReference filePath = Profile_Image_Ref.child(currentUserId + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                            Toast.makeText(ProfileEdit.this, "사진 변경에 성공하였습니다.",Toast.LENGTH_SHORT).show();

                        //왜 안될까...흠
                        final String downloadUrl = task.getResult().getUploadSessionUri().toString();

                        Profile_Edit_Ref.child("profileimage").setValue(downloadUrl)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent profileIntent = new Intent(ProfileEdit.this, ProfileEdit.class);
                                            startActivity(profileIntent);

                                            Toast.makeText(ProfileEdit.this, "사진 변경에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            String message = task.getException().getMessage();
                                            Toast.makeText(ProfileEdit.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }


                });

            }
            else{
                Toast.makeText(this, "Error Occured: Image can not be cropped Try Again." , Toast.LENGTH_SHORT).show();
            }
        }
    }
}

