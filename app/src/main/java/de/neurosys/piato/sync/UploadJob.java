package de.neurosys.piato.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;
import androidx.work.Configuration;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
//import de.neurosys.emendia.HomeEmendiaMSFragment;
//import de.neurosys.emendia.db.MySQLiteAnswerHelper;
//import de.neurosys.emendia.db.MySQLiteEDSSHelper;
//import de.neurosys.emendia.db.MySQLiteFeelingHelper;
//import de.neurosys.emendia.db.MySQLiteFloodlightResultsHelper;
//import de.neurosys.emendia.db.MySQLiteLivabilityHelper;
//import de.neurosys.emendia.db.MySQLiteMFISHelper;
//import de.neurosys.emendia.db.MySQLiteMessageHelper;
//import de.neurosys.emendia.db.MySQLiteQuestionnaireHelper;
//import de.neurosys.emendia.db.MySQLiteRelapseSymptomValueHelper;
//import de.neurosys.emendia.db.MySQLiteScoreHelper;
//import de.neurosys.emendia.db.MySQLiteSymptomHelper;
//import de.neurosys.emendia.db.MySQLiteSymptomValueHelper;
//import de.neurosys.emendia.emendia.questionnaires.QuestionFragment;
//import de.neurosys.emendia.helpers.DateTimeHelper;
//import de.neurosys.emendia.helpers.StringHelper;
//import de.neurosys.emendia.models.Answer;
//import de.neurosys.emendia.models.EDSS;
//import de.neurosys.emendia.models.Feeling;
//import de.neurosys.emendia.models.FloodlightResult;
//import de.neurosys.emendia.models.Livability;
//import de.neurosys.emendia.models.MFIS;
//import de.neurosys.emendia.models.Message;
//import de.neurosys.emendia.models.Questionnaire;
//import de.neurosys.emendia.models.RelapseSymptomValue;
//import de.neurosys.emendia.models.Score;
//import de.neurosys.emendia.models.SymptomValue;
import de.neurosys.piato.notification.FCMService;


public class UploadJob extends JobService {

    public UploadJob() {
        Configuration.Builder builder = new Configuration.Builder();
        builder.setJobSchedulerJobIdRange(0, 1000);
    }

    public static final int UPLOAD_ALL = 1;
    public static final int GET_ACCESS_CODES = 2;
    public static final int UPLOAD_FEELINGS = 3;
    public static final int UPLOAD_SYMPTOM_VALUES = 4;
    public static final int UPLOAD_LIVABILLITY = 5;
    public static final int UPLOAD_MFIS = 6;
    public static final int UPLOAD_EDSS = 7;
    public static final int UPLOAD_RELAPSE_SYMPTOM_VALUES = 8;
    public static final int UPLOAD_FLOODLIGHT_RESULTS = 9;
    public static final int UPLOAD_QUESTIONNAIRE_ANSWERS = 10;
    public static final int UPLOAD_MESSAGE_FEEDBACK = 11;
    public static final int UPLOAD_FCM_TOKEN = 12;
    public static final String FEELING_URL = "/patients/entries/feeling";
    public static final String SYMPTOMS_URL = "/patients/entries/symptoms";
    public static final String LIVABILITY_URL = "/patients/entries/livability";
    public static final String MFIS_URL = "/patients/entries/mfis";
    public static final String EDSS_URL = "/patients/entries/edss";
    public static final String RELAPSE_URL = "/patients/entries/episode";
    public static final String FLOODLIGHT_URL = "/patients/entries/floodlight";
    public static final String QUESTIONNAIRE_ANSWERS_URL = "/patients/survey-answers";
    public static final String MESSAGE_FEEDBACK_URL = "/patients/messages";
    public static final String FCM_URL = "/patients/fcm";
    public static final String TAG = UploadJob.class.getSimpleName();
    private JobParameters params;
    private String patientCode;
    private int running = 0;
    private int uploadWhat = 1;
    private Timer stopServiceTimer;
    private static final int TIME_OUT = 15000;
    private Context context;

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d(TAG, "UploadJob started");
        this.params = params;
        context = getApplicationContext();

        stopServiceTimer = new Timer();
        stopServiceTimer.schedule(new TimerTask() {
            public void run() {
                jobFinished(params, false);
            }
        }, TIME_OUT);

        initUpload();
        upload();

        if (running <= 0) {
            jobFinished(params, false);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job stopped");
        return true;
    }

    public void onDestroy() {
        if (stopServiceTimer != null) {
            stopServiceTimer.cancel();
        }
        Log.w(TAG, "UploadJob finished");
        super.onDestroy();
    }

    private void initUpload() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //uploadWhat = params.getExtras().getInt(de.neurosys.piato.sync.JobManager.UPLOAD_WHAT);
        //patientCode = sp.getString("patient-code", "");
    }

    private void upload() {
        uploadFCMToken();
    //switch (uploadWhat) {
        //case GET_ACCESS_CODES:
            //Log.w(TAG, "UploadJob GetAccessCodes");
            //getAccessCodes();
            //break;
        //case UPLOAD_FEELINGS:
            //Log.w(TAG, "UploadJob Feelings");
            //uploadFeelings();
            //break;
        //case UPLOAD_SYMPTOM_VALUES:
            //Log.w(TAG, "UploadJob Symptom Values");
            //uploadSymptomValues();
            //break;
        //case UPLOAD_LIVABILLITY:
            //Log.w(TAG, "UploadJob Livability");
            //uploadLivability();
            //break;
        //case UPLOAD_MFIS:
            //Log.w(TAG, "UploadJob MFIS");
            //uploadMfis();
            //break;
        //case UPLOAD_EDSS:
            //Log.w(TAG, "UploadJob EDSS");
            //uploadEdss();
            //break;
        //case UPLOAD_RELAPSE_SYMPTOM_VALUES:
            //Log.w(TAG, "UploadJob Relapse Symptom Values");
            //uploadRelapse();
            //break;
        //case UPLOAD_FLOODLIGHT_RESULTS:
            //Log.w(TAG, "UploadJob Floodlight Results");
            //uploadFloodlightResults();
            //break;
        //case UPLOAD_QUESTIONNAIRE_ANSWERS:
            //Log.w(TAG, "UploadJob Questionnaire Answers");
            //uploadQuestionnaireAnswers();
            //break;
        //case UPLOAD_MESSAGE_FEEDBACK:
            //Log.w(TAG, "UploadJob Message Feedback");
            //uploadMessageFeedback();
            //break;
        //case UPLOAD_FCM_TOKEN:
            //Log.w(TAG, "UploadJob FCM Token");
            //uploadFCMToken();
            //break;
        //case UPLOAD_ALL: default:
            //Log.w(TAG, "UploadJob all");
            //uploadFeelings();
            //uploadSymptomValues();
            //uploadLivability();
            //uploadMfis();
            //uploadEdss();
            //uploadRelapse();
            //uploadFloodlightResults();
            //uploadQuestionnaireAnswers();
            //uploadMessageFeedback();
            //uploadFCMToken();
            //break;
        //}
    }

    /*private void uploadFeelings() {
        ArrayList<Feeling> feelings = new MySQLiteFeelingHelper(context).getUnsubmittedFeelings();
        Log.w(TAG, "Feelings: " + feelings.size());
        if (feelings.size() > 0) {
            running++;
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray feelingsArray = new JSONArray();
                for (Feeling feeling : feelings) {
                    JSONObject jsonObjectFeeling = new JSONObject();
                    jsonObjectFeeling.put("value", 6-feeling.getValue());
                    jsonObjectFeeling.put("completed_at", feeling.getTimestamp());
                    jsonObjectFeeling.put("completed_at_millis", feeling.getMillis());
                    feelingsArray.put(jsonObjectFeeling);
                }

                jsonObject.put("code", patientCode);
                jsonObject.put("values", feelingsArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            de.neurosys.emendia.sync.SyncClient.uploadPost(context, de.neurosys.emendia.sync.SyncClient.URL_V1, FEELING_URL, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    for (Feeling feeling : feelings) {
                        new MySQLiteFeelingHelper(context).setFeelingSubmitted(feeling);
                    }
                    Log.w(TAG, "UploadJob feelings - successful");
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.w(TAG, "UploadJob feelings - Error: " + throwable.toString());
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }
            });
        }
    }*/

    /*private void uploadSymptomValues() {
        ArrayList<ArrayList<SymptomValue>> symptomValueLists = new MySQLiteSymptomValueHelper(context).getUnsubmittedSymptomValues();
        Log.w(TAG, "Symptom Values: " + symptomValueLists.size());
        if (symptomValueLists.size() > 0) {
            running++;
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray symptomValueArray = new JSONArray();
                for (ArrayList<SymptomValue> symptomValueList : symptomValueLists) {
                    JSONObject symptoms = new JSONObject();
                    ArrayList<String> symptomSlugs = new MySQLiteSymptomHelper(context).getSymptomSlugs();
                    for (SymptomValue symptomValue : symptomValueList) {
                        symptoms.put(symptomValue.getSlug(), symptomValue.getValue());
                        symptomSlugs.remove(symptomValue.getSlug());
                    }
                    for (String symptomSlug : symptomSlugs) {
                        symptoms.put(symptomSlug, -1);
                    }
                    JSONObject jsonObjectSymptomValue = new JSONObject();
                    jsonObjectSymptomValue.put("symptoms", symptoms);
                    jsonObjectSymptomValue.put("completed_at", symptomValueList.get(0).getTimestamp());
                    jsonObjectSymptomValue.put("completed_at_millis", symptomValueList.get(0).getMillis());
                    symptomValueArray.put(jsonObjectSymptomValue);
                }

                jsonObject.put("code", patientCode);
                jsonObject.put("values", symptomValueArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            de.neurosys.emendia.sync.SyncClient.uploadPost(context, de.neurosys.emendia.sync.SyncClient.URL_V1, SYMPTOMS_URL, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    for (ArrayList<SymptomValue> symptomValueList : symptomValueLists) {
                        for (SymptomValue symptomValue : symptomValueList) {
                            new MySQLiteSymptomValueHelper(context).setSymptomValueSubmitted(symptomValue);
                        }
                    }
                    Log.w(TAG, "UploadJob symptoms - successful");
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.w(TAG, "UploadJob symptoms - Error: " + throwable.toString());
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }
            });
        }
    }*/

    /*private void uploadLivability() {
        ArrayList<Livability> livabilities = new MySQLiteLivabilityHelper(context).getUnsubmittedLivabilities();
        Log.w(TAG, "Livability: " + livabilities.size());
        if (livabilities.size() > 0) {
            running++;
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray livabilityArray = new JSONArray();
                for (Livability livability : livabilities) {
                    JSONObject jsonObjectLivability = new JSONObject();
                    jsonObjectLivability.put("value", livability.getValue());
                    jsonObjectLivability.put("completed_at", livability.getTimestamp());
                    jsonObjectLivability.put("completed_at_millis", livability.getMillis());
                    livabilityArray.put(jsonObjectLivability);
                }

                jsonObject.put("code", patientCode);
                jsonObject.put("values", livabilityArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            de.neurosys.emendia.sync.SyncClient.uploadPost(context, de.neurosys.emendia.sync.SyncClient.URL_V1, LIVABILITY_URL, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    for (Livability livability : livabilities) {
                        new MySQLiteLivabilityHelper(context).setLivabilitySubmitted(livability);
                    }
                    Log.w(TAG, "UploadJob livability - successful");
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.w(TAG, "UploadJob livability - Error: " + throwable.toString());
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }
            });
        }
    }*/

    /*private void uploadMfis() {
        ArrayList<Score> mfisScores = new MySQLiteScoreHelper(context).getUnsubmittedScores(StringHelper.MFIS);
        Log.w(TAG, "MFIS: " + mfisScores.size());
        if (mfisScores.size() > 0) {
            running++;
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray mfisValues = new JSONArray();
                for (Score mfisScore : mfisScores) {
                    JSONObject jsonObjectMfisValue = new JSONObject();
                    JSONObject jsonObjectMfisScores = new JSONObject();
                    jsonObjectMfisScores.put("value", (int) mfisScore.getValue());
                    jsonObjectMfisScores.put("physical", (int) mfisScore.getValueMfisPhysical());
                    jsonObjectMfisScores.put("cognitive", (int) mfisScore.getValueMfisCognitive());
                    jsonObjectMfisScores.put("psychosocial", (int) mfisScore.getValueMfisPsychosocial());

                    ArrayList<MFIS> mfisAnswerList = new MySQLiteMFISHelper(context).getMFISAnswerListForBatch(mfisScore.getBatch());
                    JSONObject jsonObjectMfisAnswers = new JSONObject();
                    for (MFIS mfisAnswer : mfisAnswerList) {
                        String answerKey = "";
                        switch (mfisAnswer.getQuestionId()) {
                            case 0:
                                answerKey = "q1";
                                break;
                            case 1:
                                answerKey = "q2";
                                break;
                            case 2:
                                answerKey = "q3";
                                break;
                            case 3:
                                answerKey = "q4";
                                break;
                            case 4:
                                answerKey = "q5";
                                break;
                            case 5:
                                answerKey = "q6";
                                break;
                            case 6:
                                answerKey = "q7";
                                break;
                            case 7:
                                answerKey = "q8";
                                break;
                            case 8:
                                answerKey = "q9";
                                break;
                            case 9:
                                answerKey = "q10";
                                break;
                            case 10:
                                answerKey = "q11";
                                break;
                            case 11:
                                answerKey = "q12";
                                break;
                            case 12:
                                answerKey = "q13";
                                break;
                            case 13:
                                answerKey = "q14";
                                break;
                            case 14:
                                answerKey = "q15";
                                break;
                            case 15:
                                answerKey = "q16";
                                break;
                            case 16:
                                answerKey = "q17";
                                break;
                            case 17:
                                answerKey = "q18";
                                break;
                            case 18:
                                answerKey = "q19";
                                break;
                            case 19:
                                answerKey = "q20";
                                break;
                            case 20:
                                answerKey = "q21";
                                break;

                        }
                        jsonObjectMfisAnswers.put(answerKey, mfisAnswer.getAnswer());
                    }
                    jsonObjectMfisValue.put("answers", jsonObjectMfisAnswers);
                    jsonObjectMfisValue.put("scores", jsonObjectMfisScores);
                    jsonObjectMfisValue.put("completed_at", mfisScore.getTimestamp());
                    jsonObjectMfisValue.put("completed_at_millis", mfisScore.getMillis());
                    mfisValues.put(jsonObjectMfisValue);
                }

                jsonObject.put("code", patientCode);
                jsonObject.put("values", mfisValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            de.neurosys.emendia.sync.SyncClient.uploadPost(context, de.neurosys.emendia.sync.SyncClient.URL_V2, MFIS_URL, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    for (Score mfisScore : mfisScores) {
                        new MySQLiteScoreHelper(context).setScoreSubmitted(mfisScore);
                    }
                    Log.w(TAG, "UploadJob MFIS - successful");
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.w(TAG, "UploadJob MFIS - Error: " + throwable.toString());
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }
            });
        }
    }*/

    /*private void uploadEdss() {
        ArrayList<Score> edssScores = new MySQLiteScoreHelper(context).getUnsubmittedScores(StringHelper.EDSS);
        Log.w(TAG, "EDSS: " + edssScores.size());
        if (edssScores.size() > 0) {
            running++;
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray edssValues = new JSONArray();
                for (Score edssScore : edssScores) {
                    ArrayList<EDSS> edssAnswerList = new MySQLiteEDSSHelper(context).getEDSSAnswerListForBatch(edssScore.getBatch());
                    JSONObject jsonObjectEdssAnswers = new JSONObject();
                    for (EDSS edssAnswer : edssAnswerList) {
                        String answerKey = "";
                        switch (edssAnswer.getQuestionId()) {
                            case 0:
                                answerKey = "ambulation";
                                break;
                            case 1:
                                answerKey = "pyramidal";
                                break;
                            case 2:
                                answerKey = "cerebellar";
                                break;
                            case 3:
                                answerKey = "brainstem";
                                break;
                            case 4:
                                answerKey = "sensory";
                                break;
                            case 5:
                                answerKey = "bowelAndBladder";
                                break;
                            case 6:
                                answerKey = "visual";
                                break;
                            case 7:
                                answerKey = "cerebral";
                                break;
                        }
                        jsonObjectEdssAnswers.put(answerKey, edssAnswer.getAnswer());
                    }
                    JSONObject jsonObjectEdssSet = new JSONObject();
                    jsonObjectEdssSet.put("answers", jsonObjectEdssAnswers);
                    jsonObjectEdssSet.put("scores", new JSONObject().put("value", edssScore.getValue()));
                    jsonObjectEdssSet.put("completed_at", edssScore.getTimestamp());
                    jsonObjectEdssSet.put("completed_at_millis", edssScore.getMillis());
                    edssValues.put(jsonObjectEdssSet);
                }

                jsonObject.put("code", patientCode);
                jsonObject.put("values", edssValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            de.neurosys.emendia.sync.SyncClient.uploadPost(context, de.neurosys.emendia.sync.SyncClient.URL_V2, EDSS_URL, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    for (Score edssScore : edssScores) {
                        new MySQLiteScoreHelper(context).setScoreSubmitted(edssScore);
                    }
                    Log.w(TAG, "UploadJob EDSS - successful");
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.w(TAG, "UploadJob EDSS - Error: " + throwable.toString());
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }
            });
        }
    }*/

    /*private void uploadRelapse() {
        ArrayList<RelapseSymptomValue> relapseSymptomValues = new MySQLiteRelapseSymptomValueHelper(context).getUnsubmittedRelapseSymptomValues();
        Log.w(TAG, "Relapse Symptom Value: " + relapseSymptomValues.size());
        if (relapseSymptomValues.size() > 0) {
            running++;
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray relapseSymptomValueArray = new JSONArray();
                for (RelapseSymptomValue relapseSymptomvalue : relapseSymptomValues) {
                    JSONObject jsonObjectRelapseSymptomValue = new JSONObject();
                    jsonObjectRelapseSymptomValue.put("symptom_id", relapseSymptomvalue.getRelapseSymptomId());
                    jsonObjectRelapseSymptomValue.put("value", relapseSymptomvalue.getValue());
                    jsonObjectRelapseSymptomValue.put("completed_at", relapseSymptomvalue.getTimestamp());
                    jsonObjectRelapseSymptomValue.put("completed_at_millis", relapseSymptomvalue.getMillis());
                    relapseSymptomValueArray.put(jsonObjectRelapseSymptomValue);
                }

                jsonObject.put("code", patientCode);
                jsonObject.put("values", relapseSymptomValueArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            de.neurosys.emendia.sync.SyncClient.uploadPost(context, de.neurosys.emendia.sync.SyncClient.URL_V1, RELAPSE_URL, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    for (RelapseSymptomValue relapseSymptomValue : relapseSymptomValues) {
                        new MySQLiteRelapseSymptomValueHelper(context).setRelapseSymptomValueSubmitted(relapseSymptomValue);
                    }
                    Log.w(TAG, "UploadJob Relapse Symptom Value - successful");
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.w(TAG, "UploadJob Relapse Symptom Value - Error: " + throwable.toString());
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }
            });
        }
    }*/

    /*private void uploadFloodlightResults() {
        ArrayList<FloodlightResult> floodlightResults = new MySQLiteFloodlightResultsHelper(context).getUnsubmittedFloodlightResults();
        Log.w(TAG, "Floodlight Results: " + floodlightResults.size());
        if (floodlightResults.size() > 0) {
            running++;
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray floodlightResultsArray = new JSONArray();
                for (FloodlightResult floodlightResult : floodlightResults) {
                    JSONObject jsonObjectFloodlightResult = new JSONObject();
                    jsonObjectFloodlightResult.put("exercise", floodlightResult.getExercise());
                    jsonObjectFloodlightResult.put("value", floodlightResult.getValue());
                    jsonObjectFloodlightResult.put("date", floodlightResult.getDate());
                    jsonObjectFloodlightResult.put("average", floodlightResult.getAverage());
                    jsonObjectFloodlightResult.put("handUsed", floodlightResult.getHandUsed());
                    jsonObjectFloodlightResult.put("featureName", floodlightResult.getFeatureName());
                    floodlightResultsArray.put(jsonObjectFloodlightResult);
                }

                jsonObject.put("code", patientCode);
                jsonObject.put("values", floodlightResultsArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            de.neurosys.emendia.sync.SyncClient.uploadPost(context, de.neurosys.emendia.sync.SyncClient.URL_V1, FLOODLIGHT_URL, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    for (FloodlightResult floodlightResults : floodlightResults) {
                        new MySQLiteFloodlightResultsHelper(context).setFloodlightResultSubmitted(floodlightResults);
                    }
                    Log.w(TAG, "UploadJob Floodlight Results - successful");
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.w(TAG, "UploadJob Floodlight Results - Error: " + throwable.toString());
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }
            });
        }
    }*/

    /*private void uploadQuestionnaireAnswers() {
        ArrayList<Questionnaire> questionnaires = new MySQLiteQuestionnaireHelper(context).getUnsubmittedQuestionnaires();
        Log.w(TAG, "Questionnaire answer sets: " + questionnaires.size());
        if (questionnaires.size() > 0) {
            running++;
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray questionnaireAnswerSets = new JSONArray();
                for (Questionnaire questionnaire : questionnaires) {
                    JSONObject jsonObjectQuestionnaireAnswerSet = new JSONObject();
                    jsonObjectQuestionnaireAnswerSet.put("surveyKey", questionnaire.getKey());
                    jsonObjectQuestionnaireAnswerSet.put("completed_at", questionnaire.getCompletedAt());
                    ArrayList<Answer> answers = new MySQLiteAnswerHelper(context).getAnswers(questionnaire);
                    JSONArray questionnaireAnswers = new JSONArray();
                    for (Answer answer : answers) {
                        JSONObject jsonObjectAnswer = new JSONObject();
                        jsonObjectAnswer.put("questionKey", answer.getQuestionKey());
                        switch (answer.getQuestionType()) {
                            case QuestionFragment.QUESTION_TYPE_RADIO:
                            case QuestionFragment.QUESTION_TYPE_NUMERIC:
                            case QuestionFragment.QUESTION_TYPE_TEXT:
                                jsonObjectAnswer.put("value", answer.getAnswer());
                                break;
                            case QuestionFragment.QUESTION_TYPE_CHECKBOX:
                                JSONArray JSONArrayAnswerCheckbox = new JSONArray();
                                for (String answerItem : new ArrayList<>(Arrays.asList(answer.getAnswer().split(",")))) {
                                    JSONArrayAnswerCheckbox.put(answerItem);
                                }
                                jsonObjectAnswer.put("values", JSONArrayAnswerCheckbox);
                                break;
                            case QuestionFragment.QUESTION_TYPE_DATE:
                                jsonObjectAnswer.put("value", !answer.getAnswer().equals("") ? DateTimeHelper.getYYYY_MM_DD_FromDDMMYYYY(answer.getAnswer()) : "");
                                break;
                            case QuestionFragment.QUESTION_TYPE_SLIDER:
                                JSONObject jsonObjectAnswerSlider = new JSONObject();
                                JSONObject answerJSONObject = new JSONObject(answer.getAnswer());
                                JSONArray keys = answerJSONObject.names();
                                for (int i = 0; i < Objects.requireNonNull(keys).length(); i++) {
                                    String key = keys.getString(i);
                                    String value = answerJSONObject.getString(key);
                                    jsonObjectAnswerSlider.put(key, value);
                                }
                                jsonObjectAnswer.put("values", jsonObjectAnswerSlider);
                                break;
                        }
                        questionnaireAnswers.put(jsonObjectAnswer);
                    }
                    jsonObjectQuestionnaireAnswerSet.put("answers", questionnaireAnswers);
                    questionnaireAnswerSets.put(jsonObjectQuestionnaireAnswerSet);
                }

                jsonObject.put("code", patientCode);
                jsonObject.put("surveys", questionnaireAnswerSets);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            de.neurosys.emendia.sync.SyncClient.uploadPost(context, de.neurosys.emendia.sync.SyncClient.URL_V1, QUESTIONNAIRE_ANSWERS_URL, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    for (Questionnaire questionnaire : questionnaires) {
                        new MySQLiteQuestionnaireHelper(context).setQuestionnaireSubmitted(questionnaire);
                    }
                    Log.w(TAG, "UploadJob Questionnaire answer sets - successful");
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.w(TAG, "UploadJob Questionnaire answer sets - Error: " + throwable.toString());
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }
            });
        }
    }*/

    /*private void uploadMessageFeedback() {
        ArrayList<Message> messages = new MySQLiteMessageHelper(context).getUnsubmittedMessages();
        Log.w(TAG, "Message Feedback: " + messages.size());
        if (messages.size() > 0) {
            running++;
            JSONObject jsonObject = new JSONObject();
            try {
                JSONArray messagesJSONArray = new JSONArray();
                for (Message message : messages) {
                    JSONObject messageJSONObject = new JSONObject();
                    messageJSONObject.put("key", message.getKey());
                    messageJSONObject.put("received_at", message.getReceivedAt());
                    if (message.getRequestReadConfirmation() > 0) {
                        messageJSONObject.put("read_at", message.getReadAt());
                    }
                    messagesJSONArray.put(messageJSONObject);
                }

                jsonObject.put("code", patientCode);
                jsonObject.put("messages", messagesJSONArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            de.neurosys.emendia.sync.SyncClient.uploadPost(context, de.neurosys.emendia.sync.SyncClient.URL_V1, MESSAGE_FEEDBACK_URL, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    for (Message message : messages) {
                        new MySQLiteMessageHelper(context).setMessageSubmitted(message);
                    }
                    Log.w(TAG, "UploadJob Message Feedback - successful");
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.w(TAG, "UploadJob Message Feedback - Error: " + throwable.toString());
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }
            });
        }
    }*/

    private void uploadFCMToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Log.w(TAG, "Upload FCM Token: " + sp.getBoolean(FCMService.FCM_TOKEN_UPLOAD, true));
        if (sp.getBoolean(FCMService.FCM_TOKEN_UPLOAD, true) && !patientCode.equals("")) {
            running++;
            String fcmToken = sp.getString(FCMService.FCM_TOKEN, "");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("code", patientCode);
                jsonObject.put("fcm_token", fcmToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            de.neurosys.piato.sync.SyncClient.uploadPost(context, de.neurosys.piato.sync.SyncClient.URL_V1, FCM_URL, entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean(FCMService.FCM_TOKEN_UPLOAD, false);
                    editor.apply();
                    Log.w(TAG, "UploadJob FCM Token - successful");
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.w(TAG, "UploadJob FCM Token - Error: " + throwable.toString());
                    running--;
                    if (running <= 0) {
                        jobFinished(params, false);
                        stopSelf();
                    }
                }
            });
        }
    }
}