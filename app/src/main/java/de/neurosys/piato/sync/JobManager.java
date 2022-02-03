package de.neurosys.piato.sync;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import java.util.Timer;
import java.util.TimerTask;


public class JobManager {
    public static final String UPLOAD_WHAT = "upload_what";
    public static final String DOWNLOAD_WHAT = "download_what";
    public static final int UPLOAD_JOB_ID_ALL = 101;
    public static final int UPLOAD_JOB_ID_FEELING = 103;
    public static final int UPLOAD_JOB_ID_SYMPTOM = 104;
    public static final int UPLOAD_JOB_ID_LIVABILITY = 105;
    public static final int UPLOAD_JOB_ID_MFIS = 106;
    public static final int UPLOAD_JOB_ID_EDSS = 107;
    public static final int UPLOAD_JOB_ID_RELAPSE = 108;
    public static final int UPLOAD_JOB_ID_FLOODLIGHT = 109;
    public static final int UPLOAD_JOB_ID_QUESTIONNAIRE_ANSWERS = 110;
    public static final int UPLOAD_JOB_ID_MESSAGE_FEEDBACK = 111;
    public static final int UPLOAD_JOB_ID_FCM_TOKEN = 112;
    public static final int DOWNLOAD_JOB_ID_ALL = 1001;
    public static final int DOWNLOAD_JOB_ID_QUESTIONNAIRES = 1002;
    public static final int DOWNLOAD_JOB_ID_MESSAGES = 1003;
    private static final int TIME_OUT = 15000;
    private final Context context;

    public JobManager(Context context) {
        this.context = context;
    }

    public void startOneTimeUploadJob(int uploadWhat, int uploadId) {
        Timer stopTimerJob = new Timer();
        stopTimerJob.schedule(new TimerTask() {
            public void run() {
                cancelJob(uploadId);
            }
        }, TIME_OUT);

        ComponentName componentName = new ComponentName(context, UploadJob.class);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putInt(UPLOAD_WHAT, uploadWhat);
        JobInfo uploadInfoOneTime = new JobInfo.Builder(uploadId, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(1)
                .setOverrideDeadline(1)
                .setExtras(bundle)
                .build();
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(uploadInfoOneTime);
    }

    public void startOneTimeDownloadJob(int downloadWhat, int downloadId) {
        Timer stopTimerJob = new Timer();
        stopTimerJob.schedule(new TimerTask() {
            public void run() {
                cancelJob(downloadId);
            }
        }, TIME_OUT);

        ComponentName componentName = new ComponentName(context, DownloadJob.class);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putInt(DOWNLOAD_WHAT, downloadWhat);
        JobInfo downloadInfoOneTime = new JobInfo.Builder(downloadId, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(1)
                .setOverrideDeadline(1)
                .setExtras(bundle)
                .build();
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(downloadInfoOneTime);
    }

    public void cancelJob(int id) {
        if (context != null) {
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            scheduler.cancel(id);
        }
    }
}