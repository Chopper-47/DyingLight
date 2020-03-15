package com.example.dyinglight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/*import com.google.firebase.auth.FirebaseAuth;*/
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class Mark_Attendance extends AppCompatActivity {

    TextView tvstudentname, tvstudentid;
    RadioGroup rg;
    RadioButton radioButton;
    ImageView ivstudentpic;
    Button update;
    DatabaseReference mRef, fUID, studentID;
/*    FirebaseAuth fAuth;*/
    String courseid, studentid, studentname, attendance
            , totalclasses;
    int studentPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark__attendance);

        Bundle b = getIntent().getExtras();
        studentname = b.getString("studentname");
        studentid = b.getString("studentid");
        courseid = b.getString("courseid");
        attendance = b.getString("attendance");
        totalclasses = b.getString("totalclasses");
        studentPic = Integer.parseInt(b.getString("studentPic") );

        mRef = FirebaseDatabase.getInstance().getReference().child("courses");
/*       fAuth = FirebaseAuth.getInstance();
        fUID = mRef.child(fAuth.getCurrentUser().getUid());*/
        fUID = mRef.child("id1");
        studentID = fUID.child(courseid).child(studentid);

        update = findViewById(R.id.update);
        rg = findViewById(R.id.rg);
        ivstudentpic = findViewById(R.id.studentImage);
        tvstudentname = findViewById(R.id.tvstudentname);
        tvstudentid = findViewById(R.id.tvstudentid);

        ivstudentpic.setImageResource(studentPic);
        tvstudentname.setText(studentname);
        tvstudentid.setText(studentid);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int radioID = rg.getCheckedRadioButtonId();
                radioButton = findViewById(radioID);

                if (radioID == R.id.rbpresent)
                {
                    Log.d("Mark_Attendance", attendance+"   "+totalclasses);
                    attendance = (Integer.parseInt(attendance) + 1) + "";
                    totalclasses = (Integer.parseInt(totalclasses) + 1) + "";
                    studentID.child("attendance").setValue(attendance);
                    studentID.child("totalclasses").setValue(totalclasses);
                    Toast.makeText(Mark_Attendance.this
                            , "Attendance Marked", Toast.LENGTH_SHORT).show();
                }
                else if (radioID == R.id.rbabsent)
                {
                    Log.d("Mark_Attendance", attendance+"   "+totalclasses);
                    totalclasses = (Integer.parseInt(totalclasses) + 1) + "";
                    studentID.child("totalclasses").setValue(totalclasses);
                    Toast.makeText(Mark_Attendance.this
                            , "Attendance Marked", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Mark_Attendance.this,
                            "Select an option first", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
