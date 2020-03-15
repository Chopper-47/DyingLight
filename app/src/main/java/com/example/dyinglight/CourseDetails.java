package com.example.dyinglight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*import com.google.firebase.auth.FirebaseAuth;*/
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDetails extends AppCompatActivity {

    DatabaseReference mRef, fUID, courseID;
/*    FirebaseAuth fAuth;*/
    String courseid, coursename;
    ListView lv;
    List<String> studentid = new ArrayList<>();
    List<String> studentname = new ArrayList<>();
    List<String> attendance = new ArrayList<>();
    List<String> totalclasses = new ArrayList<>();
    List<String> marks = new ArrayList<>();
    HashMap<String,String> content ;
    List<Map<String, String>> messages;
    ProgressDialog progressDialog;
    Map<String, Integer> studentPic = new HashMap<>();
    String strmarks, strattendance, strtotalclasses, stremail, strstudentid, strstudentname, stats;
    TextView alert_tvname, alert_tvid, alert_tvmarks, alert_tvattendance, alert_tvattendancepercent;
    ImageView alert_studentImage;
    DecimalFormat df2 = new DecimalFormat("#");
    double attendancePercent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

    }


    class MyAdapter extends ArrayAdapter<String>
    {
        Context context;
        List<String> rstudentid;
        List<String> rstudentname;
        Map<String, Integer> rstudentPic;

        MyAdapter(@NonNull Context c, List<String> lstudentname
                , List<String> lstudentid, Map<String, Integer> lstudentPic)
        {
            super(c, R.layout.customlayout, R.id.textView1, lstudentname);
            this.context = c;
            this.rstudentname = lstudentname;
            this.rstudentid = lstudentid;
            this.rstudentPic = lstudentPic;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.customlayout, parent, false);
            ImageView simage = row.findViewById(R.id.studentImage);
            TextView sname = row.findViewById(R.id.studentName);
            TextView sid = row.findViewById(R.id.studentID);

            // now set our resources on views
            simage.setImageResource(rstudentPic.get(rstudentid.get(position)));
            sname.setText(rstudentname.get(position));
            sid.setText(rstudentid.get(position));
            Log.d("CourseDetails", "row returned");
            return row;

        }

    }



    @Override
    protected void onResume()
    {
        super.onResume();
        studentid.clear();
        studentname.clear();
        attendance.clear();
        totalclasses.clear();
        marks.clear();
        studentPic.clear();

        lv =findViewById(R.id.list);
        Bundle b = getIntent().getExtras();
        courseid = b.getString("courseid");
        coursename = b.getString("coursename");
        messages = new ArrayList<>();

        mRef = FirebaseDatabase.getInstance().getReference().child("courses");
/*        fAuth = FirebaseAuth.getInstance();
        fUID = mRef.child(fAuth.getCurrentUser().getUid());*/
        fUID = mRef.child("id1");
        courseID = fUID.child(courseid);

        studentPic.put("stid1", R.drawable.student1);
        studentPic.put("stid2", R.drawable.student2);
        studentPic.put("stid3", R.drawable.student3);
        studentPic.put("stid4", R.drawable.student4);
        studentPic.put("stid5", R.drawable.student5);
        studentPic.put("stid6", R.drawable.student6);
        studentPic.put("stid7", R.drawable.student7);
        studentPic.put("stid8", R.drawable.student8);
        studentPic.put("stid9", R.drawable.student9);
        studentPic.put("stid10", R.drawable.student9);
        studentPic.put("stid11", R.drawable.student9);
        studentPic.put("stid12", R.drawable.student9);
        studentPic.put("stid13", R.drawable.student9);
        studentPic.put("stid14", R.drawable.student9);
        studentPic.put("stid15", R.drawable.student9);
        studentPic.put("stid16", R.drawable.student9);
        studentPic.put("stid17", R.drawable.student9);
        studentPic.put("stid18", R.drawable.student9);
        studentPic.put("stid19", R.drawable.student9);
        studentPic.put("stid20", R.drawable.student9);

        progressDialog = new ProgressDialog(CourseDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.d("CourseDetails", "started creating list");
        createListOfStudentsInSelectedCourse();


    }

    private void createListOfStudentsInSelectedCourse()
    {
        courseID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot studentDetails: dataSnapshot.getChildren())
                {
                    studentid.add( studentDetails.child("studentid")
                            .getValue(String.class) );
                    studentname.add( studentDetails.child("studentname")
                            .getValue(String.class) );
                    attendance.add( studentDetails.child("attendance")
                            .getValue(String.class));
                    totalclasses.add( studentDetails.child("totalclasses")
                            .getValue(String.class));
                    marks.add(studentDetails.child("marks")
                            .getValue(String.class));

                }

                Log.d("CourseDetails", "got list data");
                MyAdapter adapter = new MyAdapter(CourseDetails.this
                        , studentname, studentid, studentPic);

                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Toast.makeText(CourseDetails.this
                                , "Long Press for options", Toast.LENGTH_SHORT).show();
                    }
                });
                progressDialog.dismiss();
                Log.d("CourseDetails", "list created");
                registerForContextMenu(lv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("CourseDetails", "fucked up");
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu
            , View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


        switch(item.getItemId())
        {
            case R.id.view:
/*                Intent intent1 = new Intent(CourseDetails.this
                        , ViewStudentDetails.class);
                intent1.putExtra("studentid", studentid.get(info.position));
                intent1.putExtra("studentname", studentname.get(info.position));
                intent1.putExtra("courseid", courseid);
                intent1.putExtra("studentPic", ""+studentPic.get(studentid.get(info.position)));
                startActivity(intent1);*/
                TextView notifyStudent;
                ImageView notify;
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetails.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert2,null);
                builder.setView(dialogView);
                notify = dialogView.findViewById(R.id.notify);
                notifyStudent = dialogView.findViewById(R.id.notifyStudent);
                displayAlertDialog(info, dialogView, builder);

                notify.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.putExtra(Intent.EXTRA_EMAIL
                                , new String[]{stremail});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Your "+ coursename + " Stats/Remarks for the current semester");
                        stats = "Marks -> " + strmarks + "\nAttendance -> " + df2.format(attendancePercent) + "%\n" + "Remarks ->";
                        i.putExtra(Intent.EXTRA_TEXT   , stats);
                        i.setType("message/rfc822");

                        startActivity(Intent.createChooser(i, "Choose an message client :"));
                    }
                });

                notifyStudent.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(CourseDetails.this, "Click on the bell icon", Toast.LENGTH_SHORT).show();
                    }
                });


                return super.onContextItemSelected(item);

            case R.id.edit:
                Intent intent2 = new Intent(CourseDetails.this
                        , EditStudentDetails.class);
                intent2.putExtra("studentid", studentid.get(info.position));
                intent2.putExtra("studentname", studentname.get(info.position));
                intent2.putExtra("courseid", courseid);
                intent2.putExtra("totalclasses", totalclasses.get(info.position));
                intent2.putExtra("studentPic", ""+studentPic.get(studentid.get(info.position)));

                startActivity(intent2);
                return super.onContextItemSelected(item);

            case R.id.markAttendance:
                Intent intent3 = new Intent(CourseDetails.this,
                        Mark_Attendance.class);
                intent3.putExtra("studentid", studentid.get(info.position));
                intent3.putExtra("studentname", studentname.get(info.position));
                intent3.putExtra("courseid", courseid);
                intent3.putExtra("attendance", attendance.get(info.position));
                intent3.putExtra("totalclasses", totalclasses.get(info.position));
                intent3.putExtra("marks", marks.get(info.position));
                intent3.putExtra("studentPic", ""+studentPic.get(studentid.get(info.position)));

                startActivity(intent3);
                return super.onContextItemSelected(item);

            default:
                return super.onContextItemSelected(item);
        }
    }


    public void displayAlertDialog(AdapterView.AdapterContextMenuInfo info, View dialogView,AlertDialog.Builder builder )
    {

        ProgressDialog progressDialog1;
        progressDialog1 = new ProgressDialog(CourseDetails.this);
        progressDialog1.setCancelable(false);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();

        alert_tvid = dialogView.findViewById(R.id.alert_studentID);
        alert_tvname = dialogView.findViewById(R.id.alert_studentName);
        alert_tvmarks = dialogView.findViewById(R.id.alert_tvmarks);
        alert_tvattendance = dialogView.findViewById(R.id.alert_tvattendance);
        alert_tvattendancepercent = dialogView.findViewById(R.id.alert_tvattendancepercent);
        alert_studentImage = dialogView.findViewById(R.id.alert_studentImage);

        final AlertDialog dialog = builder.create();

        strstudentid = studentid.get(info.position);
        strstudentname = studentname.get(info.position);
        courseID.child(strstudentid).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                strmarks = dataSnapshot.child("marks").getValue(String.class);
                strattendance = dataSnapshot.child("attendance").getValue(String.class);
                strtotalclasses = dataSnapshot.child("totalclasses").getValue(String.class);
                stremail = dataSnapshot.child("email").getValue(String.class);
                alert_studentImage.setImageResource(studentPic.get(strstudentid));
                alert_tvname.setText(strstudentname);
                alert_tvid.setText(strstudentid);
                alert_tvmarks.setText(strmarks);
                alert_tvattendance.setText("Attended "+strattendance+" out of "+strtotalclasses+ " classes" );
                attendancePercent = ( Integer.parseInt(strattendance)*1.0/
                        Integer.parseInt(strtotalclasses) )*100;
                alert_tvattendancepercent.setText(df2.format(attendancePercent)+"%");
                Log.d("CourseDetails", "");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });

        dialog.show();
        progressDialog1.dismiss();
    }







}
