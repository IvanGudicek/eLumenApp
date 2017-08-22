package com.elumenapp.elumenapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.data.MySingleton;
import com.elumenapp.elumenapp.models.Answer;
import com.elumenapp.elumenapp.models.Question;
import com.elumenapp.elumenapp.models.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuizActivity extends AppCompatActivity {

    private AlertDialog.Builder alertBuilder;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private TextView questionText;
    public TextView explanationText;
    private static View globalView;
    private static String explanationString = new String();
    //   private Entirety globalEntirety = new Entirety(null, null, null, null);
    private List<Answer> answers = new ArrayList<>();
    private List<CheckBox> listOfCheckBoxes = new ArrayList<>(), listOfUnCheckboxes = new ArrayList<>();
    private List<RadioButton> listOfRadioButtons = new ArrayList<>(), listOfUnRadioButtons = new ArrayList<>();
    private BigDecimal score = new BigDecimal(0.00), totalScore = new BigDecimal(0.00);

    private Question globalQuestion = new Question();
    private List<Question> questionList = new ArrayList<>();
    private List<Question> randomQuestionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        questionText = (TextView) findViewById(R.id.questionText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        displayQuiz();
    }


    public List<Answer> shuffleListOfAnswers(Question question) {
        List<Answer> listOfAnswers = new ArrayList<>();
        listOfAnswers.addAll(question.getAnswerList());
        Collections.shuffle(listOfAnswers);
        return listOfAnswers;
    }

    public void addLayout(String condition) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.feedbackRelativeLayout);
        LayoutInflater inflater = (LayoutInflater) QuizActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childView = null;
        if (condition.equals("positive")) {
            childView = inflater.inflate(R.layout.correct_answer, (ViewGroup) findViewById(R.id.correctAnswerRelativeLayout));
        } else if (condition.equals("negative")) {
            childView = inflater.inflate(R.layout.incorrect_answer, (ViewGroup) findViewById(R.id.incorrectAnswerRelativeLayout));
        } else if (condition.equals("neutral")) {
            childView = inflater.inflate(R.layout.neutral_answer, (ViewGroup) findViewById(R.id.neturalAnswerRelativeLayout));
        }
        relativeLayout.removeAllViews();
        relativeLayout.addView(childView);
        explanationText = (TextView) findViewById(R.id.explanationText);
        explanationText.setText(explanationString);
        scoreTextView = (TextView) findViewById(R.id.scoreText);
        scoreTextView.setText("Rezultat: " + score + ", ukupni rezultat: " + totalScore);
    }


    public void changeLayout(boolean condition) {
        LinearLayout linearView = (LinearLayout) findViewById(R.id.linearLayout);
        LayoutInflater inflater = (LayoutInflater) QuizActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout;
        if (condition) {

            childLayout = inflater.inflate(R.layout.activity_radio,
                    (ViewGroup) findViewById(R.id.radioLinearLayout));
        } else {

            childLayout = inflater.inflate(R.layout.activity_check,
                    (ViewGroup) findViewById(R.id.checkLinearLayout));
        }

        linearView.removeAllViews();
        linearView.addView(childLayout);
        answers = new ArrayList<>();
        if (condition) {
            radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
            radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
            radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
            radioButton4 = (RadioButton) findViewById(R.id.radioButton4);
            answers.addAll(shuffleListOfAnswers(globalQuestion));
            radioButton1.setText(answers.get(0).getText());
            radioButton2.setText(answers.get(1).getText());
            radioButton3.setText(answers.get(2).getText());
            radioButton4.setText(answers.get(3).getText());
        } else {
            checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
            checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
            checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
            checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
            answers.addAll(shuffleListOfAnswers(globalQuestion));
            checkBox1.setText(answers.get(0).getText());
            checkBox2.setText(answers.get(1).getText());
            checkBox3.setText(answers.get(2).getText());
            checkBox4.setText(answers.get(3).getText());
        }

        enabledButtons();
    }

    public void disableButtons() {
        if (globalQuestion.getAnswerList().stream().filter(answer1 -> answer1.isCorrect()).count() > 1) {
            checkBox1.setEnabled(false);
            checkBox2.setEnabled(false);
            checkBox3.setEnabled(false);
            checkBox4.setEnabled(false);
        } else {
            radioButton1.setEnabled(false);
            radioButton2.setEnabled(false);
            radioButton3.setEnabled(false);
            radioButton4.setEnabled(false);
        }

    }

    public void enabledButtons() {
        if (globalQuestion.getAnswerList().stream().filter(answer1 -> answer1.isCorrect()).count() > 1) {
            checkBox1.setEnabled(true);
            checkBox2.setEnabled(true);
            checkBox3.setEnabled(true);
            checkBox4.setEnabled(true);
        } else {
            radioButton1.setEnabled(true);
            radioButton2.setEnabled(true);
            radioButton3.setEnabled(true);
            radioButton4.setEnabled(true);
        }
    }

    public void resetValuesOfAnswers() {
        if (globalQuestion.getAnswerList().stream().filter(answer1 -> answer1.isCorrect()).count() > 1) {
            checkBox1.setChecked(false);
            checkBox2.setChecked(false);
            checkBox3.setChecked(false);
            checkBox4.setChecked(false);
            checkBox1.setBackgroundColor(getResources().getColor(R.color.none));
            checkBox2.setBackgroundColor(getResources().getColor(R.color.none));
            checkBox3.setBackgroundColor(getResources().getColor(R.color.none));
            checkBox4.setBackgroundColor(getResources().getColor(R.color.none));
        } else {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            radioButton1.setBackgroundColor(getResources().getColor(R.color.none));
            radioButton2.setBackgroundColor(getResources().getColor(R.color.none));
            radioButton3.setBackgroundColor(getResources().getColor(R.color.none));
            radioButton4.setBackgroundColor(getResources().getColor(R.color.none));
        }
    }


    public void throwNextQuestion() {
        List<Question> questions = new ArrayList<>();
        questions.addAll(randomQuestionList);
        int number = randomQuestionList.size() - 1;
        if (number >= 0) {
            globalQuestion = globalQuestion.copyQuestion(questions.get(number));
            explanationString = globalQuestion.getExplanation();
            questionText.setText(globalQuestion.getText());
            if (globalQuestion.getAnswerList().stream().filter(answer1 -> answer1.isCorrect()).count() > 1) {
                changeLayout(false);
            } else {
                changeLayout(true);
            }
            randomQuestionList.remove(number);
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.feedbackRelativeLayout);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View childView = inflater.inflate(R.layout.nextquestion, (ViewGroup) findViewById(R.id.nextquestionRelativeLayout));
            relativeLayout.removeAllViews();
            relativeLayout.addView(childView);
            resetValuesOfAnswers();
        } else {
            throwLastAlert();
        }

    }

    public boolean checkStrings(String string) {
        boolean bool = false;
        for (Answer answer : globalQuestion.getAnswerList().stream().filter(answer1 -> answer1.isCorrect()).collect(Collectors.toList())) {
            if (answer.getText().equals(string)) {
                bool = true;
                break;
            }
        }
        return bool;
    }


    public void changeLayoutOfAnswer() {
        if (globalQuestion.getAnswerList().stream().filter(answer1 -> answer1.isCorrect()).count() > 1) {
            for (CheckBox checkBox : listOfCheckBoxes) {
                if (checkStrings(checkBox.getText().toString())) {
                    checkBox.setBackgroundColor(getResources().getColor(R.color.correctColor));
                } else {
                    checkBox.setBackgroundColor(getResources().getColor(R.color.incorrectColor));
                }
            }
            for (CheckBox checkBox : listOfUnCheckboxes) {
                if (checkStrings(checkBox.getText().toString())) {
                    checkBox.setBackgroundColor(getResources().getColor(R.color.neutralColor));
                } else {
                    checkBox.setBackgroundColor(getResources().getColor(R.color.none));
                }
            }
        } else {
            for (RadioButton radioButton : listOfRadioButtons) {
                if (checkStrings(radioButton.getText().toString())) {
                    radioButton.setBackgroundColor(getResources().getColor(R.color.correctColor));
                } else {
                    radioButton.setBackgroundColor(getResources().getColor(R.color.incorrectColor));
                }
            }
            for (RadioButton radioButton : listOfUnRadioButtons) {
                if (checkStrings(radioButton.getText().toString())) {
                    radioButton.setBackgroundColor(getResources().getColor(R.color.neutralColor));
                }
            }
        }
    }

    public String getInformationOfAnswer() {
        int numberOfCorrectAnswers = (int) globalQuestion.getAnswerList().stream().filter(answer1 -> answer1.isCorrect()).count();
        int numberOfAnswers = globalQuestion.getAnswerList().size();
        boolean positiveScore = false;
        listOfCheckBoxes = new ArrayList<>();
        listOfRadioButtons = new ArrayList<>();
        listOfUnCheckboxes = new ArrayList<>();
        listOfUnRadioButtons = new ArrayList<>();
        String returnString = null;
        score = new BigDecimal(0.00);
        score.setScale(3);
        if (numberOfCorrectAnswers > 1) {
            if (checkBox1.isChecked()) {
                listOfCheckBoxes.add(checkBox1);
            } else {
                listOfUnCheckboxes.add(checkBox1);
            }
            if (checkBox2.isChecked()) {
                listOfCheckBoxes.add(checkBox2);
            } else {
                listOfUnCheckboxes.add(checkBox2);
            }
            if (checkBox3.isChecked()) {
                listOfCheckBoxes.add(checkBox3);
            } else {
                listOfUnCheckboxes.add(checkBox3);
            }
            if (checkBox4.isChecked()) {
                listOfCheckBoxes.add(checkBox4);
            } else {
                listOfUnCheckboxes.add(checkBox4);
            }
            for (CheckBox checkBox : listOfCheckBoxes) {
                for (Answer answer : globalQuestion.getAnswerList().stream().filter(answer1 -> answer1.isCorrect()).collect(Collectors.toList())) {
                    if (checkBox.getText().toString().equals(answer.getText())) {
                        positiveScore = true;
                    }
                }
                if (positiveScore) {
                    score = score.add(new BigDecimal(1).divide(new BigDecimal(numberOfCorrectAnswers)));
                } else {
                    BigDecimal up = new BigDecimal(1);
                    up = up.divide(new BigDecimal(numberOfAnswers));
                    BigDecimal down = new BigDecimal(numberOfCorrectAnswers);
                    score = score.subtract(up.divide(down));
                }
                positiveScore = false;

            }


        } else {
            if (radioButton1.isChecked()) {
                listOfRadioButtons.add(radioButton1);
            } else {
                listOfUnRadioButtons.add(radioButton1);
            }
            if (radioButton2.isChecked()) {
                listOfRadioButtons.add(radioButton2);
            } else {
                listOfUnRadioButtons.add(radioButton2);
            }
            if (radioButton3.isChecked()) {
                listOfRadioButtons.add(radioButton3);
            } else {
                listOfUnRadioButtons.add(radioButton3);
            }
            if (radioButton4.isChecked()) {
                listOfRadioButtons.add(radioButton4);
            } else {
                listOfUnRadioButtons.add(radioButton4);
            }
            for (RadioButton radioButton : listOfRadioButtons) {
                for (Answer answer : globalQuestion.getAnswerList().stream().filter(answer1 -> answer1.isCorrect()).collect(Collectors.toList())) {
                    if (radioButton.getText().toString().equals(answer.getText())) {
                        positiveScore = true;
                    }
                }
                if (positiveScore) {
                    score = score.add(new BigDecimal(1).divide(new BigDecimal(numberOfCorrectAnswers)));
                } else {
                    BigDecimal up = new BigDecimal(1);
                    up = up.divide(new BigDecimal(numberOfAnswers));
                    BigDecimal down = new BigDecimal(numberOfCorrectAnswers);
                    score = score.subtract(up.divide(down));
                }
                positiveScore = false;
            }
        }
        totalScore.setScale(3);
        totalScore = totalScore.add(score);
        if (score.compareTo(new BigDecimal(0)) > 0) {
            returnString = "positive";
        } else if (score.compareTo(new BigDecimal(0)) < 0) {
            returnString = "negative";
        } else {
            returnString = "neutral";
        }

        return returnString;
    }

    private TextView scoreTextView;

    public void exitOfQuizButtonAnswerListener(View view) {
        globalView = view;
        exitOfQuizButtonListener(view);
    }

    public void nextQuestionButtonAnswerListener(View view) {
        globalView = view;
        throwNextQuestion();
    }


    public void submitAnswerButtonListener(View view) {
        globalView = view;
        addLayout(getInformationOfAnswer());
        changeLayoutOfAnswer();
        disableButtons();
    }

    public void exitOfQuizButtonListener(View view) {
        alertBuilder = new AlertDialog.Builder(QuizActivity.this);
        alertBuilder.setTitle("pitanje...");
        alertBuilder.setMessage("Jesi li siguran/a da želiš prekinuti kviz?");

        LayoutInflater inflater = QuizActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating, null);
        alertBuilder.setView(dialogView);
        ratingText = (TextView) dialogView.findViewById(R.id.mediumText);
        ratingText.setText("Tvoj ostvareni rezultat je: ");
        ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBar);
        ratingBar.setStepSize((float) 0.01);
        ratingBar.setRating(totalScore.floatValue());
        // ratingBar.setEnabled(false);

        alertBuilder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateScoreToPersonDatabase(totalScore);
                finish();
            }
        }).setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private TextView ratingText;
    private RatingBar ratingBar;
    private String jsonResponse = null;


    public void updateScoreToPersonDatabase(BigDecimal score) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("score", score);
            jsonResponse = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                    MainActivity.SERVER_CONNECTION_URL + "/score/update/user/" + MainActivity.getUser().getId() + "/subject/" + StartQuizActivity.chosenSubject.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(QuizActivity.this, "Ponovno pokrenite server te probajte ponovno!", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return String.format("application/json; charset=utf-8");
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonResponse == null ? null : jsonResponse.getBytes("utf-8");
                    } catch (Exception error) {
                        error.printStackTrace();
                        return null;
                    }
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void throwLastAlert() {
        alertBuilder = new AlertDialog.Builder(QuizActivity.this);
        alertBuilder.setTitle("Kviz je završio!");
        alertBuilder.setMessage("Čestitamo na rezultatu.");

        LayoutInflater inflater = QuizActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating, null);
        alertBuilder.setView(dialogView);
        ratingText = (TextView) dialogView.findViewById(R.id.mediumText);
        ratingText.setText("Tvoj postignuti rezultat je: ");
        ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBar);
        ratingBar.setStepSize((float) 0.01);
        ratingBar.setRating(totalScore.floatValue());
        // ratingBar.setEnabled(false);

        alertBuilder.setPositiveButton("Idi na glavni menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //      BigDecimal score = UserActivity.getGlobalStaticPerson().getTotalScore();
                //      UserActivity.getGlobalStaticPerson().setTotalScore(score.add(totalScore));
                updateScoreToPersonDatabase(totalScore);
                finish();
            }
        }).setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("Započni ponovo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //    BigDecimal score = UserActivity.getGlobalStaticPerson().getTotalScore();
                //    UserActivity.getGlobalStaticPerson().setTotalScore(score.add(totalScore));
                updateScoreToPersonDatabase(totalScore);
                startActivity(new Intent(QuizActivity.this, StartQuizActivity.class));
                finish();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    public void showAlert() {
        alertBuilder = new AlertDialog.Builder(QuizActivity.this);
        alertBuilder.setTitle("Informacije");
        alertBuilder.setMessage("Jesi li spreman/a da započneš kviz?");
        alertBuilder.setPositiveButton("Oke može!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setNegativeButton("Ne, odustajem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(QuizActivity.this, StartQuizActivity.class));
                finish();
                shuffleListOfEntirety();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        exitOfQuizButtonListener(globalView);
    }


    public void shuffleListOfEntirety() {
        randomQuestionList = new ArrayList<>();
        randomQuestionList.addAll(questionList);
        Collections.shuffle(randomQuestionList);
    }

    public void displayQuiz() {
        JsonArrayRequest userJsonRequest = new JsonArrayRequest(MainActivity.SERVER_CONNECTION_URL + "/question/list/" + StartQuizActivity.chosenSubject.getId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    List<Answer> answerList = new ArrayList<>();
                                    JSONObject object = response.getJSONObject(i);
                                    for (int j = 0; j < object.getJSONArray("answerList").length(); j++) {
                                        JSONObject answerObject = object.getJSONArray("answerList").getJSONObject(j);
                                        answerList.add(new Answer(answerObject.getInt("id"), answerObject.getString("text"), answerObject.getBoolean("correct")));
                                    }
                                    questionList.add(new Question(object.getInt("id"),
                                            object.getString("text"), object.getString("explanation"), new Subject(object.getJSONObject("subject").getInt("id"), object.getJSONObject("subject").getString("name")),
                                            answerList));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            shuffleListOfEntirety();
                            throwNextQuestion();
                            showAlert();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(QuizActivity.this, "Dohvat liste pitanja nije uspio, pokrenite server te probajte ponovno!", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(QuizActivity.this).addToRequestQueue(userJsonRequest);
    }

}
