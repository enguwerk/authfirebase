package com.example.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class upload extends AppCompatActivity implements View.OnClickListener {
EditText bname;
EditText bauthor ;
EditText shelfno;
EditText bamount;
Spinner libraryn;
Spinner floornum;
Button uploadbtn;
EditText bdescription;
Dataupload dataupload;
FirebaseAuth firebaseAuth;
FirebaseDatabase database;
DatabaseReference reference;
    ProgressDialog progressDialog;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///
        getSupportActionBar().setTitle("UPLOAD DATA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db=FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        bname=findViewById(R.id.bname);
        bauthor=findViewById(R.id.author);
        shelfno=findViewById(R.id.shelf_no);
        bamount=findViewById(R.id.bamount);
        libraryn=findViewById(R.id.library);
        floornum=findViewById(R.id.floor);
        bdescription=findViewById(R.id.description);
        uploadbtn=findViewById(R.id.upload);
        progressDialog=new ProgressDialog(this);
        uploadbtn.setOnClickListener(this);
        dataupload=new Dataupload();

    }

    private boolean validateInputs(String book, String author, int amount, String description, int shelf) {
        if (book==null) {

            bname.requestFocus();
            return true;
        }
        if (author.isEmpty()) {
            bauthor.setError("Name required");
            bauthor.requestFocus();
            return true;
        }
        if (String.valueOf(amount).isEmpty()) {
            bamount.setError("Name required");
            bamount.requestFocus();
            return true;
        }
        if (description.isEmpty()) {
            bdescription.setError("Name required");
            bdescription.requestFocus();
            return true;
        }
        if (String.valueOf(shelf).isEmpty()) {
            shelfno.setError("Name required");
            shelfno.requestFocus();
            return true;
        }

        return  false;
    }
    //clear all text field after uploading
    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }

    @Override
    public void onClick(View v) {
        //get data from view
        String book = bname.getText().toString().trim();
        String author = bauthor.getText().toString().trim();
        int amount=Integer.valueOf(bamount.getText().toString().trim());
        String description=bdescription.getText().toString().trim();
        String floor=floornum.getSelectedItem().toString().trim();
        int shelf=Integer.valueOf(shelfno.getText().toString().trim());
        String library=libraryn.getSelectedItem().toString().trim();
        //getting 1st character
        String searchKey=Character.valueOf(book.charAt(0)).toString().toUpperCase();
        //clear text field


        if ( !validateInputs( book,author,amount, description,shelf)){
            progressDialog.setMessage("UPLOADING DATA TO DATABASE ");
            progressDialog.show();
            CollectionReference dbbooks=db.collection("books");
            Dataupload dataupload=new Dataupload(book,searchKey,
             author,
             library,
             description,
             floor,
             amount,
             shelf);
            dbbooks.add(dataupload).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                @Override
                public void onSuccess(DocumentReference documentReference) {
                    progressDialog.dismiss();
                    Toast.makeText(upload.this, "book Added", Toast.LENGTH_LONG).show();
                    clearForm((ViewGroup) findViewById(R.id.constr));

                }
            });
    }
}

    public void create_user(View view) {
        firebaseAuth.signOut();

    startActivity( new Intent(this,Login_activity.class));
    }
}
