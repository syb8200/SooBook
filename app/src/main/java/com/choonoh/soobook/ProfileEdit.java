package com.choonoh.soobook;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

            profile_img.setImageURI(ImageUri); // 이미지 띄움
        }






        profile_edit_btn.setOnClickListener(v->{


            FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
            DatabaseReference databaseReference = database.getReference("User/" + currentUserId + "/nick"); // DB 테이블 연결FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            DatabaseReference databaseReference2 = database.getReference("User/" + currentUserId + "/state"); // DB 테이블 연결FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            databaseReference.setValue(nickname_et.getText().toString());
            databaseReference2.setValue(status_et.getText().toString());


            if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    StorageReference filePath = Profile_Image_Ref.child(currentUserId + ".jpg");

                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                       // Glide.with(ProfileEdit.this).load(uri).into(profile_img);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("profile pic: ", String.valueOf(e));
                                    }
                                });
                            final String downloadUrl = task.getResult().getUploadSessionUri().toString();
                            Log.e("사진 주소: ", downloadUrl);
                            Profile_Edit_Ref.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if(task.isSuccessful()){


                                                Toast.makeText(ProfileEdit.this, "프로필 변경이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                String message = task.getException().getMessage();
                                                Toast.makeText(ProfileEdit.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }


                    });



                    Intent intent = new Intent(ProfileEdit.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }});}

}
