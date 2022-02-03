package de.neurosys.piato.notification;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

//import de.neurosys.emendia.HomeEmendiaMSFragment;
import de.neurosys.piato.R;
//import de.neurosys.emendia.emendia.messages.MessageListFragment;
//import de.neurosys.emendia.emendia.questionnaires.QuestionnaireListFragment;
import de.neurosys.piato.helpers.NotificationHelper;
import de.neurosys.piato.sync.DownloadJob;
import de.neurosys.piato.sync.JobManager;
import de.neurosys.piato.sync.UploadJob;

public class FCMService extends FirebaseMessagingService {
    private static final String FCMTYPE = "type";
    private static final String FCMMODULEID = "module_id";
    private static final String FCMOFFICEID = "office_id";
    public static final String FCM_TOKEN = "fcm-token";
    public static final String FCM_TOKEN_UPLOAD = "fcm-token-upload";
    public static final String EMENDIA_MESSAGES = "emendia-messages";
    public static final String EMENDIA_SURVEYS = "emendia-surveys";


    @Override
    public void handleIntent(@NonNull Intent intent) {
        super.handleIntent(intent);
        /*if (intent.getExtras().get("google.c.a.m_l").equals(EMENDIA_MESSAGES)) {
            (new JobManager(getApplicationContext())).startOneTimeDownloadJob(DownloadJob.DOWNLOAD_MESSAGES, JobManager.DOWNLOAD_JOB_ID_MESSAGES);
            NotificationHelper.cancelNotification(getApplicationContext(), NotificationHelper.NOTIFICATION_ID_MESSAGE);
            NotificationHelper.createNotification(getApplicationContext(),
                    NotificationHelper.NOTIFICATION_ID_MESSAGE,
                    new Date().getTime(),
                    getApplicationContext().getString(R.string.notification_message_title),
                    getApplicationContext().getString(R.string.notification_message_text),
                    NotificationHelper.CHANNEL_NAME_MESSAGE,
                    NotificationHelper.CHANNEL_ID_MESSAGE,
                    MessageListFragment.TAG,
                    0);
        } else if (intent.getExtras().get("google.c.a.m_l").equals(EMENDIA_SURVEYS)) {
            (new JobManager(getApplicationContext())).startOneTimeDownloadJob(DownloadJob.DOWNLOAD_QUESTIONNAIRES, JobManager.DOWNLOAD_JOB_ID_QUESTIONNAIRES);
            NotificationHelper.cancelNotification(getApplicationContext(), NotificationHelper.NOTIFICATION_ID_QUESTIONNAIRE);
            NotificationHelper.createNotification(getApplicationContext(),
                    NotificationHelper.NOTIFICATION_ID_QUESTIONNAIRE,
                    new Date().getTime(),
                    getApplicationContext().getString(R.string.notification_questionnaire_title),
                    getApplicationContext().getString(R.string.notification_questionnaire_text),
                    NotificationHelper.CHANNEL_NAME_QUESTIONNAIRE,
                    NotificationHelper.CHANNEL_ID_QUESTIONNAIRE,
                    QuestionnaireListFragment.TAG,
                    0);
        }*/
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //Zeitstempel Z5
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssSS");
        Date d = new Date();
        System.out.println(s.format(d));

        if (remoteMessage.getNotification() != null && remoteMessage.getNotification().getTitle() != null) {
            if (remoteMessage.getNotification().getTitle().equals("Neue Nachricht")) {
                (new JobManager(getApplicationContext())).startOneTimeDownloadJob(DownloadJob.DOWNLOAD_MESSAGES, JobManager.DOWNLOAD_JOB_ID_MESSAGES);
                NotificationHelper.cancelNotification(getApplicationContext(), NotificationHelper.NOTIFICATION_ID_MESSAGE);
                NotificationHelper.createNotification(getApplicationContext(),
                        NotificationHelper.NOTIFICATION_ID_MESSAGE,
                        new Date().getTime(),
                        getApplicationContext().getString(R.string.notification_message_title),
                        getApplicationContext().getString(R.string.notification_message_text),
                        NotificationHelper.CHANNEL_NAME_MESSAGE,
                        NotificationHelper.CHANNEL_ID_MESSAGE,
                        //MessageListFragment.TAG,
                        "",
                        0);
            } else if (remoteMessage.getNotification().getTitle().equals("Neuer Fragebogen")) {
                (new JobManager(getApplicationContext())).startOneTimeDownloadJob(DownloadJob.DOWNLOAD_QUESTIONNAIRES, JobManager.DOWNLOAD_JOB_ID_QUESTIONNAIRES);
                NotificationHelper.cancelNotification(getApplicationContext(), NotificationHelper.NOTIFICATION_ID_QUESTIONNAIRE);
                NotificationHelper.createNotification(getApplicationContext(),
                        NotificationHelper.NOTIFICATION_ID_QUESTIONNAIRE,
                        new Date().getTime(),
                        getApplicationContext().getString(R.string.notification_questionnaire_title),
                        getApplicationContext().getString(R.string.notification_questionnaire_text),
                        NotificationHelper.CHANNEL_NAME_QUESTIONNAIRE,
                        NotificationHelper.CHANNEL_ID_QUESTIONNAIRE,
                        //QuestionnaireListFragment.TAG,
                        "",
                        0);
            }
        }

        /*Map<String, String> data = remoteMessage.getData();

        if (remoteMessage.getNotification() != null) { // new fcm
            Log.w("FCM notification: ", remoteMessage.getNotification().toString());
            (new JobManager(getApplicationContext())).syncAll();
            new GeneralNotification().createGeneralNotification(getApplicationContext(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

        } else if (data.get(FCMMODULEID) != null && data.get(FCMTYPE) != null) {
            Log.w("FCM data: ", data.toString());
            int moduleId = Integer.parseInt(Objects.requireNonNull(data.get(FCMMODULEID)));
            String fcmType = Objects.requireNonNull(data.get(FCMTYPE));

            if (moduleId == FCM.ALL_ID) { // regular fcm every day at 6am
                (new JobManager(getApplicationContext())).syncAll();

            } else if (fcmType.equals("0")) { // fcm targeting a module in Doctor's office
                FCM fcmHandlerPraxis = new FCM(getApplicationContext(), moduleId, Integer.parseInt(Objects.requireNonNull(data.get(FCMOFFICEID))));
                fcmHandlerPraxis.syncPraxisModule();

            } else if (fcmType.equals("1")) { // fcm for a module in Personal space
                FCM fcmHandlerPersoenlich = new FCM(getApplicationContext(), moduleId);
                if (moduleId == FCM.STEPS_ID) {
                    (new JobManager(getApplicationContext())).startOneTimeUploadJob(UploadJob.UPLOAD_STEPS, UploadJob.BACKGROUND);
                } else {
                    fcmHandlerPersoenlich.syncPersonalModul();
                }
            }
        }*/
    }

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull String fcmToken) {
        super.onNewToken(fcmToken);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(FCM_TOKEN, fcmToken);
        editor.putBoolean(FCM_TOKEN_UPLOAD, true);
        editor.apply();
        if (!sp.getString("patient-code", "").equals("")) {
            (new JobManager(getApplicationContext())).startOneTimeUploadJob(UploadJob.UPLOAD_FCM_TOKEN, JobManager.UPLOAD_JOB_ID_FCM_TOKEN);
        }
    }
}