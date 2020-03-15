


package com.example.dyinglight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*Firebase Database structure
    courses
  - <faculty uids>
  - <course ids>
  - <student ids>
  - <student details>
*/
//UI modification(All)
//Extensify the database(Add more student records for each course, increse no. of courses for each faculty)
//Display only those courses which faculty has enrolled for

public class MainActivity extends AppCompatActivity {

    DatabaseReference mref, fUID;
    ListView lv;
    String selectedCourseId, selectedCourseName;
/*    String courseid[] = {"15CSE311", "15CSE312", "15CSE313", "15MAT315", "15CSE432"};
    String coursename[] = {"Compiler Design", "Computer Networks",
            "Software Engineering", "Calculus", "Machine Learning"};*/
    List<String> courseid = new ArrayList<>();
    List<String> coursename = new ArrayList<>();
    HashMap<String,String> content ;
    List<Map<String, String>> messages;
    ProgressDialog progressDialog;
    HashMap<String, String> coursehm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mref = FirebaseDatabase.getInstance().getReference().child("courses");
/*        fAuth = FirebaseAuth.getInstance();
        fUID = mref.child(fAuth.getCurrentUser().getUid());*/
        fUID = mref.child("id1");
        lv=findViewById(R.id.lv);
/*        messages = new ArrayList<>();*/

        coursehm = new HashMap<>();
        coursehm.put("15CSE311", "Compiler  Design");
        coursehm.put("15CSE312", "Computer Networks");
        coursehm.put("15CSE313", "Software Engineering");
        coursehm.put("15MAT315", "Calculus");
        coursehm.put("15SSK313", "Soft Skills");
        coursehm.put("15ENG320", "Communicative English");
        coursehm.put("15CSE387", "Open Lab");
        coursehm.put("15CSE386", "Computer Networks Lab");
        coursehm.put("15CSE385", "Compiler Design Lab");

/*        for(int i = 0 ; i < courseid.length; i++) {
            content = new HashMap<String, String>();
            content.put("courseid", courseid[i]);
            content.put("coursename", coursename[i]);
            messages.add(content);
        }
        String[] entry = new String[] {"courseid", "coursename"};
        SimpleAdapter adapter = new SimpleAdapter(this, messages,
                android.R.layout.simple_list_item_2,
                entry,
                new int[] {android.R.id.text1,
                        android.R.id.text2,
                });
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                selectedCourseId = courseid[i];
                selectedCourseName = courseid[i];
                checkForCourse();

            }
        });*/

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        createList();

    }



    class MyAdapter extends ArrayAdapter<String>
    {
        Context context;
        List<String> rcourseid;
        List<String> rcoursename;

        MyAdapter(@NonNull Context c, List<String> lcoursename
                , List<String> lcourseid)
        {
            super(c, R.layout.courselistlayout, R.id.textView1, lcoursename);
            this.context = c;
            this.rcoursename = lcoursename;
            this.rcourseid = lcourseid;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.courselistlayout, parent, false);
            TextView sname = row.findViewById(R.id.studentName);
            TextView sid = row.findViewById(R.id.studentID);

            // now set our resources on views
            sname.setText(rcoursename.get(position));
            sid.setText(rcourseid.get(position));
            Log.d("CourseDetails", "row returned");
            return row;

        }

    }







    private void createList()
    {
        fUID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
/*                if(dataSnapshot.hasChild(selectedCourseId))
                {
                    Log.d("MainActivity", ""+selectedCourseId);
                    Intent intent = new Intent(MainActivity.this
                            , CourseDetails.class);
                    intent.putExtra("courseid", selectedCourseId);
                    intent.putExtra("coursename",selectedCourseName );
                    progressDialog.dismiss();
                    startActivity(intent);
                }
                else
                {
                    Log.d("MainActivity", ""+selectedCourseId);
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this
                            , "Select the course which you teach"
                            , Toast.LENGTH_SHORT).show();
                }*/

                for ( DataSnapshot crsid: dataSnapshot.getChildren() )
                {
                    courseid.add(crsid.getKey());
                    coursename.add(coursehm.get(crsid.getKey()));
                }

                MyAdapter adapter = new MyAdapter(MainActivity.this
                        , coursename, courseid);

                lv.setAdapter(adapter);
                progressDialog.dismiss();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        selectedCourseId = courseid.get(position);
                        selectedCourseName = courseid.get(position);
                        Log.d("MainActivity", ""+selectedCourseId);
                        Intent intent = new Intent(MainActivity.this
                                , CourseDetails.class);
                        intent.putExtra("courseid", selectedCourseId);
                        intent.putExtra("coursename",selectedCourseName );
                        progressDialog.dismiss();
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });


    }




}
