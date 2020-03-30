package com.s.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private CollectionReference objectCollectionReference;
    private FirebaseFirestore objectFirebaseFirestore;
    private static final String COLLECTION_NAME = "BSCSClassInformation";

    private Dialog objectDialog;
    private EditText collectionIDET,documentIDET,studentNameET,studentCityET;

    private DocumentReference objectDocumentReference;
    private TextView downloadedDataTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        objectDialog = new Dialog(this);
        objectDialog.setContentView(R.layout.please_wait);
        objectDialog.setCancelable(false);

        collectionIDET = findViewById(R.id.collectionIDET);
        documentIDET = findViewById(R.id.documentIDET);
        studentNameET = findViewById(R.id.studentNameET);
        studentCityET = findViewById(R.id.studentCityET);
        downloadedDataTV = findViewById(R.id.downloadedDataTV);
    }

    public void addValuesToFirebaseFirestore(View view)
    {
        try
        {
            if(!documentIDET.getText().toString().isEmpty() && !studentNameET.getText().toString().isEmpty()
            && !studentCityET.getText().toString().isEmpty())
            {
                if (documentIDET.getText().toString().equals(objectDocumentReference))
                {
                    Toast.makeText(this, "All ready exist", Toast.LENGTH_SHORT).show();
                }
                else {
                    objectDialog.show();
                    Map<String, Object> objectMap = new HashMap();
                    objectMap.put("stuName", studentNameET.getText().toString());
                    objectMap.put("StuCity", studentCityET.getText().toString());

                    objectFirebaseFirestore.collection(COLLECTION_NAME)
                    .document(documentIDET.getText().toString())
                            .set(objectMap)
                            .addOnSuccessListener(
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            documentIDET.setText("");
                                            studentNameET.setText("");
                                            studentCityET.setText("");
                                            documentIDET.requestFocus();
                                            objectDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Data added to collection", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            )
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            objectDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Fails to add data to collection" +
                                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                }
            }

            else if (documentIDET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter document id", Toast.LENGTH_SHORT).show();
            }
            else if (studentNameET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter student name", Toast.LENGTH_SHORT).show();
            }
            else if (studentCityET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter student city", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this,"addValuesToFirebaseFirestore:"+
                    e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCollectionFromFirebaseFirestore(View view)
    {
        try
        {
            if (documentIDET.getText().toString().isEmpty())
            {
                objectDialog.dismiss();
                Toast.makeText(this, "Please provide the name of Collection to delete", Toast.LENGTH_SHORT).show();
            }
            else
            {

                objectDialog.show();
                objectDocumentReference = objectFirebaseFirestore.collection(COLLECTION_NAME).document(
                        collectionIDET.getText().toString()
                );
                objectDocumentReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        objectDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Collection is deleted", Toast.LENGTH_SHORT).show();
                                        collectionIDET.setText("");
                                        collectionIDET.requestFocus();
                                    }
                                }
                        ).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to delete the document", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }

        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "deleteCollectionFromFirebaseFirestore:"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    public void deleteDocumentFromFirebaseFirestore(View view)
    {
        try
        {
            if (documentIDET.getText().toString().isEmpty())
            {
                objectDialog.dismiss();
                Toast.makeText(this, "Please provide the document ID to delete", Toast.LENGTH_SHORT).show();
            }
            else
            {

                objectDialog.show();
                objectDocumentReference = objectFirebaseFirestore.collection(COLLECTION_NAME).document(
                        documentIDET.getText().toString()
                );
                objectDocumentReference.delete()
                        .addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        objectDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Document is deleted", Toast.LENGTH_SHORT).show();
                                        documentIDET.setText("");
                                        documentIDET.requestFocus();
                                    }
                                }
                        ).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to delete the document", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    String allData="";
    public void getCollectionFromFirebaseFireStore(View view)
    {
        try
        {
            objectCollectionReference = objectFirebaseFirestore.collection(COLLECTION_NAME);
            objectDialog.show();
            objectCollectionReference.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot objectDocumentSnapshot:queryDocumentSnapshots)
                            {
                                objectDialog.dismiss();
                                String studentID = objectDocumentSnapshot.getId();
                                String stuName = objectDocumentSnapshot.getString("stuName");
                                String stuCity = objectDocumentSnapshot.getString("StuCity");

                                allData ="Document ID:"
                                        +studentID
                                        +"\n"+ "Student Name:"
                                        +stuName
                                        + "\n"+"Student City:"
                                        +stuCity+"\n";

                            }
                            downloadedDataTV.setText(allData);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Fails to retrieve collection", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "addValuesToFirebaseFirestore:"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}
