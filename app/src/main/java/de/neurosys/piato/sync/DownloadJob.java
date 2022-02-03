package de.neurosys.piato.sync;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;

import androidx.work.Configuration;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
//import de.neurosys.emendia.db.MySQLiteMessageHelper;
//import de.neurosys.emendia.db.MySQLiteQuestionnaireHelper;

public class DownloadJob extends JobService {

    public DownloadJob() {
        Configuration.Builder builder = new Configuration.Builder();
        builder.setJobSchedulerJobIdRange(1000, 2000);
    }

    public static final String TAG = DownloadJob.class.getSimpleName();
    public static final Integer TIME_OUT = 15000;
    public static final String QUESTIONNAIRES_URL = "/patients/surveys";
    public static final String MESSAGES_URL = "/patients/messages";
    public static final int DOWNLOAD_ALL = 1;
    public static final int DOWNLOAD_QUESTIONNAIRES = 2;
    public static final int DOWNLOAD_MESSAGES = 3;

    private Context context;

    private int job_running = 0;
    private JobParameters params;
    private Timer stopServiceTimer;
    private int downloadWhat = DOWNLOAD_ALL;

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.i(TAG, "DownloadJob started");
        this.params = params;
        context = getApplicationContext();
        stopServiceTimer = new Timer();
        stopServiceTimer.schedule(new TimerTask() {
            public void run() {
                cancel();
                jobFinished(params, false);
            }
        }, TIME_OUT);

        initDownload();

        download();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    @Override
    public void onDestroy() {
        if (stopServiceTimer != null) {
            stopServiceTimer.cancel();
        }
        Log.w(TAG, "DownloadJob finished");
        super.onDestroy();
    }

    private void initDownload() {
        downloadWhat = params.getExtras().getInt(JobManager.DOWNLOAD_WHAT);
    }

    private void download() {
        switch (downloadWhat) {
            case DOWNLOAD_QUESTIONNAIRES:
                getQuestionnaires();
                break;
            case DOWNLOAD_MESSAGES:
                getMessages();
                break;
            case DOWNLOAD_ALL: default:
                getQuestionnaires();
                getMessages();
                break;
        }
    }

    private void getQuestionnaires() {
        job_running++;
        Log.w("Job Running: ", "QUESTIONNAIRES++ " + job_running);
        SyncClient.get(context, SyncClient.URL_V1, QUESTIONNAIRES_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /*new MySQLiteQuestionnaireHelper(context).saveQuestionnaires(response);
                Log.w("Job Running: ", "QUESTIONNAIRES-- " + job_running--);
                if (job_running <= 0) {
                    jobFinished(params, false);
                }*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w("Job Running: ", "QUESTIONNAIRES-- " + job_running--);
                Log.w("ERROR: ", throwable.toString());
                Log.w("ERROR: ", responseString);
                if (job_running <= 0) {
                    jobFinished(params, false);
                }
            }
        });
    }

    private void getMessages() {
        job_running++;
        Log.w("Job Running: ", "MESSAGES++ " + job_running);
        SyncClient.get(context, SyncClient.URL_V1, MESSAGES_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /*new MySQLiteMessageHelper(context).saveMessages(response);
                Log.w("Job Running: ", "MESSAGES-- " + job_running--);
                if (job_running <= 0) {
                    jobFinished(params, false);
                }*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w("Job Running: ", "MESSAGES-- " + job_running--);
                Log.w("ERROR: ", throwable.toString());
                Log.w("ERROR: ", responseString);
                if (job_running <= 0) {
                    jobFinished(params, false);
                }
            }
        });
    }
}