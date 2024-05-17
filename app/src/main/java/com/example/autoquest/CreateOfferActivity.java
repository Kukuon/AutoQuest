package com.example.autoquest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autoquest.databinding.CreateOfferActivityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateOfferActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 22;
    private static final String TAG = "CreateOfferActivity";

    private CreateOfferActivityBinding binding;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private StorageReference storageReference;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference usersRef;
    private DatabaseReference offersRef;

    private String offerId;

    private List<Uri> filePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateOfferActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        usersRef = firebaseDatabase.getReference("users");
        offersRef = firebaseDatabase.getReference("offers");
        offerId = offersRef.push().getKey();

        binding.selectButton.setOnClickListener(v -> selectImage());

        binding.uploadButton.setOnClickListener(v -> {
            uploadImage();
            uploadToDatabase();
            startActivity(new Intent(CreateOfferActivity.this, MainActivity.class));
        });

        binding.backButton.setOnClickListener(v -> startActivity(new Intent(CreateOfferActivity.this, MainActivity.class)));
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            List<Bitmap> bitmaps = new ArrayList<>();

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    filePaths.add(imageUri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        bitmaps.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                filePaths.add(imageUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    bitmaps.add(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            displaySelectedImages(bitmaps);
        }
    }

    private void displaySelectedImages(List<Bitmap> bitmaps) {
        AvatarAdapter adapter = new AvatarAdapter(this, bitmaps);
        binding.imageGridView.setAdapter(adapter);
    }

    private void uploadImage() {
        if (filePaths != null && !filePaths.isEmpty()) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            for (Uri uri : filePaths) {
                // Defining the child of storageReference
                StorageReference ref = storageReference.child("offer_images/" + offerId + "/" + UUID.randomUUID().toString());
                // adding listeners on upload or failure of image
                ref.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Toast.makeText(CreateOfferActivity.this, "Изображения загружены", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(CreateOfferActivity.this, "Не удалось загрузить изображения " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnProgressListener(taskSnapshot -> {
                    // Progress Listener for loading percentage on the dialog box
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                });
            }
        }
    }

    private void uploadToDatabase() {
        String brand = ((EditText) findViewById(R.id.brandET)).getText().toString();
        String model = ((EditText) findViewById(R.id.modelET)).getText().toString();
        String generation = ((EditText) findViewById(R.id.generationET)).getText().toString();
        String price = ((EditText) findViewById(R.id.priceET)).getText().toString();
        String year = ((EditText) findViewById(R.id.yearET)).getText().toString();
        String enginePower = ((EditText) findViewById(R.id.enginePowerET)).getText().toString();
        String fuelConsumption = ((EditText) findViewById(R.id.fuelConsumptionET)).getText().toString();
        String description = ((EditText) findViewById(R.id.descriptionET)).getText().toString();

        String ownerId = firebaseUser.getUid();

        usersRef.child(ownerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ownerPhoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                Offer item = new Offer(offerId, brand, model, generation, price, year, description, enginePower, fuelConsumption, ownerId, ownerPhoneNumber);

                if (offerId != null) {
                    // Сохранение offer в базу данных Realtime Database
                    offersRef.child(offerId).setValue(item).addOnSuccessListener(aVoid -> {
                        Toast.makeText(CreateOfferActivity.this, "Объявление добавлено", Toast.LENGTH_SHORT).show();
                        clearEditTextFields();
                        startActivity(new Intent(CreateOfferActivity.this, MainActivity.class));
                    }).addOnFailureListener(e -> {
                        Toast.makeText(CreateOfferActivity.this, "Не удалось добавить данные", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void clearEditTextFields() {
        ((EditText) findViewById(R.id.brandET)).setText("");
        ((EditText) findViewById(R.id.modelET)).setText("");
        ((EditText) findViewById(R.id.generationET)).setText("");
        ((EditText) findViewById(R.id.priceET)).setText("");
        ((EditText) findViewById(R.id.yearET)).setText("");
        ((EditText) findViewById(R.id.enginePowerET)).setText("");
        ((EditText) findViewById(R.id.fuelConsumptionET)).setText("");
    }
}
