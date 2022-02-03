package de.neurosys.piato;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobParameters;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.neurosys.piato.notification.FCMService;
import de.neurosys.piato.sync.JobManager;
import de.neurosys.piato.sync.UploadJob;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.buttonPushNotification);
        button.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v)
            {
                //Zeitstempel Z1
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssSS");
                Date d = new Date();
                System.out.println(s.format(d));

                /*UploadJob uploadJob = new UploadJob();
                JobParameters jopParameters;
                uploadJob.onStartJob(jopParameters);*/
            }
        });
    }
}