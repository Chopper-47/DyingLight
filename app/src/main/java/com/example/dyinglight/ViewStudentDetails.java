package com.example.dyinglight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/*import com.google.firebase.auth.FirebaseAuth;*/
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;

public class ViewStudentDetails extends AppCompatActivity {

    TextView tvname, tvid, tvmarks, tvattendance
            ,tvattendancepercent ;
    String studentname, studentid, courseid
            , marks, attendance, totalclasses, email,stats;
    int studentPic;
    double attendancePercent;
    DatabaseReference mRef, fUID, studentID;
/*    FirebaseAuth fAuth;*/
    DecimalFormat df2 = new DecimalFormat("#.##");
    ProgressDialog progressDialog;
//arrow is the image which has the intent for forwarding student details
    ImageView arrow, ivstudentpic;
    TextView notifyStudent;

    HashMap<String, String> coursehm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_details);

        Bundle b = getIntent().getExtras();
        studentname = b.getString("studentname");
        studentid = b.getString("studentid");
        courseid = b.getString("courseid");
        studentPic = Integer.parseInt(b.getString("studentPic") );

        notifyStudent = findViewById(R.id.notifyStudent);
        ivstudentpic = findViewById(R.id.studentImage);
        arrow = findViewById(R.id.arrow);
        tvname = findViewById(R.id.tvname);
        tvid = findViewById(R.id.tvid);
        tvmarks = findViewById(R.id.tvmarks);
        tvattendance = findViewById(R.id.tvattendance);
        tvattendancepercent = findViewById(R.id.tvattendancepercent);

//course hashmap initialization(more to be added)
        coursehm = new HashMap<>();
        coursehm.put("15CSE311", "Compiler  Design");
        coursehm.put("15CSE312", "Computer Networks");
        coursehm.put("15CSE313", "Software Engineering");
        coursehm.put("15MAT315", "Calculus");
        coursehm.put("15CSE432", "Machine Learning");

        mRef = FirebaseDatabase.getInstance().getReference().child("courses");
/*        fAuth = FirebaseAuth.getInstance();
        fUID = mRef.child(fAuth.getCurrentUser().getUid());*/
        fUID = mRef.child("id1");
        studentID = fUID.child(courseid).child(studentid);
        //sets a student's details to their corresponding views

        progressDialog = new ProgressDialog(ViewStudentDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        ivstudentpic.setImageResource(studentPic);
        displayStudentDetails();

        notifyStudent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(ViewStudentDetails.this, "Click on the bell icon", Toast.LENGTH_SHORT).show();
            }
        });

        arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_EMAIL
                        , new String[]{email});
                i.putExtra(Intent.EXTRA_SUBJECT, "Your "+ coursehm.get(courseid) + " Stats/Remarks for the current semester");
                stats = "Your Marks -> " + marks + "\nYour Attendance -> " + df2.format(attendancePercent) + "%\n" + "Remarks ->";
                i.putExtra(Intent.EXTRA_TEXT   , stats);
                i.setType("message/rfc822");

                startActivity(Intent.createChooser(i, "Choose an message client :"));

            }
        });



    }

    private void displayStudentDetails()
    {
        studentID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                marks = dataSnapshot.child("marks").getValue(String.class);
                attendance = dataSnapshot.child("attendance").getValue(String.class);
                totalclasses = dataSnapshot.child("totalclasses").getValue(String.class);
                email = dataSnapshot.child("email").getValue(String.class);
                tvname.setText(studentname);
                tvid.setText(studentid);
                tvmarks.setText(marks);
                tvattendance.setText(attendance+"/"+totalclasses);
                attendancePercent = ( Integer.parseInt(attendance)*1.0/
                        Integer.parseInt(totalclasses) )*100;
                tvattendancepercent.setText(""+df2.format(attendancePercent));
                progressDialog.dismiss();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                progressDialog.dismiss();
            }
        });
    }

}

