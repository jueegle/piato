package de.neurosys.piato.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import de.neurosys.emendia.HomeEmendiaMSFragment;
import de.neurosys.piato.R;
//import de.neurosys.emendia.db.MySQLiteEventHelper;
//import de.neurosys.emendia.db.MySQLiteFeelingHelper;
//import de.neurosys.emendia.db.MySQLiteFloodlightResultsHelper;
//import de.neurosys.emendia.db.MySQLiteKnowledgeArticleHelper;
//import de.neurosys.emendia.db.MySQLiteMedicationHelper;
//import de.neurosys.emendia.db.MySQLiteMedicationTimestampHelper;
//import de.neurosys.emendia.models.Event;
//import de.neurosys.emendia.models.KnowledgeArticle;
//import de.neurosys.emendia.models.Medication;
//import de.neurosys.emendia.models.MedicationTimestamp;
//import de.neurosys.emendia.module.diary.DiaryEntryFragment;
//import de.neurosys.emendia.module.floodlight.FloodlightListFragment;
//import de.neurosys.emendia.module.floodlight.FloodlightMonthListFragment;
//import de.neurosys.emendia.module.knowledge.KnowledgeNewListFragment;
//import de.neurosys.emendia.module.medicalquery.MedicalQueryMonthListFragment;
//import de.neurosys.emendia.module.medication.MedicationListFragment;
import de.neurosys.piato.notification.NotificationReceiver;

public class NotificationHelper {

    public static final String CHANNEL_NAME_MEDICATION = "Medikation";
    public static final String CHANNEL_ID_MEDICATION = "channel-id-medication";
    public static final String CHANNEL_NAME_KNOWLEDGE = "Wissensartikel";
    public static final String CHANNEL_ID_KNOWLEDGE = "channel-id-knowledge";
    public static final String CHANNEL_NAME_FLOODLIGHT = "Floodlight-Tests";
    public static final String CHANNEL_ID_FLOODLIGHT = "channel-id-floodlight";
    public static final String CHANNEL_NAME_MEDICAL_QUERY = "Medizinische Abfrage";
    public static final String CHANNEL_ID_MEDICAL_QUERY = "channel-id-medical-query";
    public static final String CHANNEL_NAME_DIARY_ENTRY = "Tagebucheintrag";
    public static final String CHANNEL_ID_DIARY_ENTRY = "channel-id-diary-entry";
    public static final String CHANNEL_NAME_WE_MISS_YOU = "Wir vermissen Dich";
    public static final String CHANNEL_ID_WE_MISS_YOU = "channel-id-we-miss-you";
    public static final String CHANNEL_NAME_QUESTIONNAIRE = "Fragebogen";
    public static final String CHANNEL_ID_QUESTIONNAIRE = "channel-id-questionnaire";
    public static final String CHANNEL_NAME_MESSAGE = "Nachricht";
    public static final String CHANNEL_ID_MESSAGE = "channel-id-message";
    public static final int NOTIFICATION_ID_WE_MISS_YOU = 1000000001;
    public static final int NOTIFICATION_ID_DIARY_ENTRY = 1000000002;
    public static final int NOTIFICATION_ID_MEDICAL_QUERY = 1000000003;
    public static final int NOTIFICATION_ID_FLOODLIGHT_TESTS = 1000000004;
    public static final int NOTIFICATION_ID_KNOWLEDGE_ARTICLE = 1000000005;
    public static final int NOTIFICATION_ID_QUESTIONNAIRE = 1000000006;
    public static final int NOTIFICATION_ID_MESSAGE = 1000000007;

    public static void createNotification(Context context, int notificationId, long time, String title, String text, String channelName, String channelId, String fragmentName, int objectId) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(NotificationReceiver.NOTIFICATION_ID, notificationId);
        intent.putExtra(NotificationReceiver.NOTIFICATION_TITLE, title);
        intent.putExtra(NotificationReceiver.NOTIFICATION_TEXT, text);
        intent.putExtra(NotificationReceiver.NOTIFICATION_CHANNEL_NAME, channelName);
        intent.putExtra(NotificationReceiver.NOTIFICATION_CHANNEL_ID, channelId);
        intent.putExtra(NotificationReceiver.NOTIFICATION_FRAGMENT_NAME, fragmentName);
        intent.putExtra(NotificationReceiver.NOTIFICATION_OBJECT_ID, objectId);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
    }

    public static void cancelNotification(Context context, int notificationId) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, new Intent(context, NotificationReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        am.cancel(pendingIntent);
    }

    /*public static void recreateNotifications(Context context) {
        recreateNotificationWeMissYou(context);
        recreateNotificationDiaryEntry(context);
        recreateNotificationMedications(context);
        recreateNotificationMedicalQuery(context);
        recreateNotificationFloodlightTests(context);
        recreateNotificationKnowledgeArticle(context);
    }*/

    /*public static void recreateNotificationWeMissYou(Context context) {
        createNotification(context,
                NOTIFICATION_ID_WE_MISS_YOU,
                new Date().getTime() + 14 * 86400 * 1000,
                context.getString(R.string.notification_we_miss_you_title),
                context.getString(R.string.notification_we_miss_you_text),
                CHANNEL_NAME_WE_MISS_YOU,
                CHANNEL_ID_WE_MISS_YOU,
                HomeEmendiaMSFragment.TAG,
                0);
    }*/

    /*public static void recreateNotificationDiaryEntry(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (sp.getBoolean(StringHelper.NOTIFICATION_DIARY_ON, true)) {
            Calendar c = Calendar.getInstance();
            String time = sp.getString(StringHelper.NOTIFICATION_DIARY_TIME, "18:00");
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), Integer.parseInt(time.split(":")[0]), Integer.parseInt(time.split(":")[1]), 0);
            c.set(Calendar.MILLISECOND, 0);
            if (c.getTimeInMillis() < new Date().getTime() || new MySQLiteFeelingHelper(context).getFeelingForToday() != null) {
                c.add(Calendar.DAY_OF_WEEK, 1);
            }
            createNotification(context,
                    NOTIFICATION_ID_DIARY_ENTRY,
                    c.getTimeInMillis(),
                    context.getString(R.string.notification_diary_entry_title),
                    sp.getString(StringHelper.NOTIFICATION_DIARY_TEXT, context.getString(R.string.notification_diary_entry_text)),
                    CHANNEL_NAME_DIARY_ENTRY,
                    CHANNEL_ID_DIARY_ENTRY,
                    DiaryEntryFragment.TAG,
                    0);
        } else {
            NotificationHelper.cancelNotification(context, NOTIFICATION_ID_DIARY_ENTRY);
        }
    }*/

    /*public static void recreateNotificationMedicalQuery(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Event eventEdss = new MySQLiteEventHelper(context).getNextEvent(StringHelper.EDSS);
        Event eventMfis = new MySQLiteEventHelper(context).getNextEvent(StringHelper.MFIS);
        Event eventLivability = new MySQLiteEventHelper(context).getNextEvent(StringHelper.LIVABILITY);
        if (sp.getBoolean(StringHelper.NOTIFICATION_MEDICAL_QUERY_ON, true) && eventEdss != null && eventMfis != null && eventLivability != null) {
            long millis = Math.min(Math.min(eventEdss.getMillis(), eventMfis.getMillis()), eventLivability.getMillis());
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millis);
            String time = sp.getString(StringHelper.NOTIFICATION_MEDICAL_QUERY_TIME, "18:00");
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), Integer.parseInt(time.split(":")[0]), Integer.parseInt(time.split(":")[1]), 0);
            if (c.getTimeInMillis() < new Date().getTime()) {
                c.add(Calendar.DAY_OF_MONTH, MySQLiteEventHelper.MEDICAL_QUERY_28_DAY_SPAN);
            }
            createNotification(context,
                    NOTIFICATION_ID_MEDICAL_QUERY,
                    c.getTimeInMillis(),
                    context.getString(R.string.notification_medical_query_title),
                    sp.getString(StringHelper.NOTIFICATION_MEDICAL_QUERY_TEXT, context.getString(R.string.notification_medical_query_text)),
                    CHANNEL_NAME_MEDICAL_QUERY,
                    CHANNEL_ID_MEDICAL_QUERY,
                    MedicalQueryMonthListFragment.TAG,
                    0);
        } else {
            NotificationHelper.cancelNotification(context, NOTIFICATION_ID_MEDICAL_QUERY);
        }
    }*/

    /*public static void recreateNotificationFloodlightTests(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (sp.getBoolean(StringHelper.NOTIFICATION_FLOODLIGHT_ON, true)) {
            long resultsDrawShapeMillis = new MySQLiteFloodlightResultsHelper(context).getLastFloodlightResults(FloodlightListFragment.EXERCISE_TYPE_DRAW_SHAPE);
            long resultsPinchingMillis = new MySQLiteFloodlightResultsHelper(context).getLastFloodlightResults(FloodlightListFragment.EXERCISE_TYPE_PINCHING);
            long resultsSymbolsMillis = new MySQLiteFloodlightResultsHelper(context).getLastFloodlightResults(FloodlightListFragment.EXERCISE_TYPE_SYMBOLS);
            long resultsUTurnMillis = new MySQLiteFloodlightResultsHelper(context).getLastFloodlightResults(FloodlightListFragment.EXERCISE_TYPE_U_TURN);
            long oldestResultsMillis = Math.min(Math.min(Math.min(resultsDrawShapeMillis, resultsPinchingMillis), resultsSymbolsMillis), resultsUTurnMillis);

            ArrayList<Integer> allowedDays = new ArrayList<>();
            allowedDays.add(sp.getInt(StringHelper.NOTIFICATION_FLOODLIGHT_DAY_OF_WEEK, Calendar.MONDAY));

            Calendar c = Calendar.getInstance();
            String time = sp.getString(StringHelper.NOTIFICATION_FLOODLIGHT_TIME, "18:00");
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)+7, Integer.parseInt(time.split(":")[0]), Integer.parseInt(time.split(":")[1]), 0);
            c.set(Calendar.MILLISECOND, 0);
            long millis = 5000000000000L;
            for (int i = 0; i < 8; i++) {
                if (allowedDays.contains(c.get(Calendar.DAY_OF_WEEK))
                        && oldestResultsMillis < DateTimeHelper.getTimeInMillisOfThisDay(c)
                        && new Date().getTime() < c.getTimeInMillis()) {
                    millis = c.getTimeInMillis();
                }
                c.add(Calendar.DAY_OF_WEEK, -1);
            }

            createNotification(context,
                    NOTIFICATION_ID_FLOODLIGHT_TESTS,
                    millis,
                    context.getString(R.string.notification_floodlight_title),
                    sp.getString(StringHelper.NOTIFICATION_FLOODLIGHT_TEXT, context.getString(R.string.notification_floodlight_text)),
                    NotificationHelper.CHANNEL_NAME_FLOODLIGHT,
                    NotificationHelper.CHANNEL_ID_FLOODLIGHT,
                    FloodlightMonthListFragment.TAG,
                    0);
        } else {
            NotificationHelper.cancelNotification(context, NOTIFICATION_ID_FLOODLIGHT_TESTS);
        }
    }*/

    /*public static void recreateNotificationMedications(Context context) {
        MySQLiteMedicationTimestampHelper medicationTimestampHelper = new MySQLiteMedicationTimestampHelper(context);
        MySQLiteMedicationHelper medicationHelper = new MySQLiteMedicationHelper(context);
        for (Medication medication : medicationHelper.getMedications()) {
            MedicationTimestamp medicationTimestamp = medicationTimestampHelper.getNextMedicationTimestampByMedicationId(medication.getId());
            createNotification(context,
                    medication.getId(),
                    medicationTimestamp.getMillis(),
                    context.getString(R.string.notification_medication_title),
                    medication.getTitle(),
                    NotificationHelper.CHANNEL_NAME_MEDICATION,
                    NotificationHelper.CHANNEL_ID_MEDICATION,
                    MedicationListFragment.TAG,
                    medication.getId());
        }
    }*/

    /*public static void recreateNotificationMedication(Context context, int medicationId) {
        MySQLiteMedicationTimestampHelper medicationTimestampHelper = new MySQLiteMedicationTimestampHelper(context);
        Medication medication = new MySQLiteMedicationHelper(context).getMedicationById(medicationId);
        MedicationTimestamp medicationTimestamp = medicationTimestampHelper.getNextMedicationTimestampByMedicationId(medicationId);
        createNotification(context,
                medicationId,
                medicationTimestamp.getMillis(),
                context.getString(R.string.notification_medication_title),
                medication.getTitle(),
                NotificationHelper.CHANNEL_NAME_MEDICATION,
                NotificationHelper.CHANNEL_ID_MEDICATION,
                MedicationListFragment.TAG,
                medicationId);
    }*/

    /*public static void recreateNotificationKnowledgeArticle(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (sp.getBoolean(StringHelper.NOTIFICATION_KNOWLEDGE_ON, true)) {
            KnowledgeArticle knowledgeArticle = new MySQLiteKnowledgeArticleHelper(context).getNextKnowledgeArticle();
            if (knowledgeArticle != null) {
                createNotification(context,
                        NOTIFICATION_ID_KNOWLEDGE_ARTICLE,
                        knowledgeArticle.getPublish() + DateTimeHelper.getMillisFromHHMM(sp.getString(StringHelper.NOTIFICATION_KNOWLEDGE_TIME, "18:00")),
                        context.getString(R.string.notification_knowledge_title),
                        sp.getString(StringHelper.NOTIFICATION_KNOWLEDGE_TEXT, context.getString(R.string.notification_knowledge_text)),
                        NotificationHelper.CHANNEL_NAME_KNOWLEDGE,
                        NotificationHelper.CHANNEL_ID_KNOWLEDGE,
                        KnowledgeNewListFragment.TAG,
                        knowledgeArticle.getId());
            }
        } else {
            NotificationHelper.cancelNotification(context, NOTIFICATION_ID_KNOWLEDGE_ARTICLE);
        }
    }*/
}