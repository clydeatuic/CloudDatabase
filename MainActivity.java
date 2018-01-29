package com.uic.clouddatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    /*1. Declare Variables*/
    private EditText editText_name, editText_email;
    private Button button_create, button_update, button_remove;
    private ListView listView_users;

    private List<UserModel> listUsers = new ArrayList<UserModel>();
    private ArrayAdapter<UserModel> arrayAdapterUser;
    private UserModel userSelected;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*2. Instanstiate Variables*/
        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_email = (EditText) findViewById(R.id.editText_email);
        button_create = (Button) findViewById(R.id.button_create);
        button_update = (Button) findViewById(R.id.button_update);
        button_remove = (Button) findViewById(R.id.button_remove);
        listView_users = (ListView) findViewById(R.id.listView_users);

        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

        /*3. Define Actions/Behavior/Methods*/

        //Create User
        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText_name.getText().toString().isEmpty() || editText_email.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Name or Email is empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    UserModel user = new UserModel();
                    user.setId(UUID.randomUUID().toString());
                    user.setName(editText_name.getText().toString().trim());
                    user.setEmail(editText_email.getText().toString().trim());
                    databaseReference.child("users").child(user.getId()).setValue(user);

                    Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                    editText_name.setText("");
                    editText_email.setText("");
                }
            }
        });

        //Read Users
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUsers.clear();
                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    UserModel user = objSnapshot.getValue(UserModel.class);
                    listUsers.add(user);
                }
                arrayAdapterUser = new ArrayAdapter<UserModel>(MainActivity.this,android.R.layout.simple_list_item_1, listUsers);
                listView_users.setAdapter(arrayAdapterUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //Update User
        listView_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userSelected = (UserModel) adapterView.getItemAtPosition(i);
                editText_name.setText(userSelected.getName());
                editText_email.setText(userSelected.getEmail());
            }
        });
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel user = new UserModel();
                user.setId(userSelected.getId());
                user.setName(editText_name.getText().toString().trim());
                user.setEmail(editText_email.getText().toString().trim());
                databaseReference.child("users").child(user.getId()).setValue(user);

                Toast.makeText(getApplicationContext(),"User Updated",Toast.LENGTH_SHORT).show();
                editText_name.setText("");
                editText_email.setText("");
            }
        });

        //Delete user
        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel user = new UserModel();
                user.setId(userSelected.getId());
                databaseReference.child("users").child(user.getId()).removeValue();

                Toast.makeText(getApplicationContext(),"User Removed",Toast.LENGTH_SHORT).show();
                editText_name.setText("");
                editText_email.setText("");
            }
        });
    }
}
