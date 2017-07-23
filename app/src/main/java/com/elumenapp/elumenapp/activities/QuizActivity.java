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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.data.MySingleton;
import com.elumenapp.elumenapp.models.Answer;
import com.elumenapp.elumenapp.models.Entirety;
import com.elumenapp.elumenapp.models.Explanation;
import com.elumenapp.elumenapp.models.Question;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private AlertDialog.Builder alertBuilder;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private TextView questionText;
    public TextView explanationText;
    private static View globalView;
    private static String explanationString = new String();
    private Entirety globalEntirety = new Entirety(null, null, null, null);
    private List<Answer> answers = new ArrayList<>();
    private List<CheckBox> listOfCheckBoxes = new ArrayList<>(), listOfUnCheckboxes = new ArrayList<>();
    private List<RadioButton> listOfRadioButtons = new ArrayList<>(), listOfUnRadioButtons = new ArrayList<>();
    private BigDecimal score = new BigDecimal(0.00), totalScore = new BigDecimal(0.00);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        questionText = (TextView) findViewById(R.id.questionText);
        displayQuiz();
        shuffleListOfEntirety();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        throwNextQuestion();
        showAlert();
    }


    public List<Answer> shuffleListOfAnswers(Entirety entirety) {
        List<Answer> listOfAnswers = new ArrayList<>();
        listOfAnswers.addAll(entirety.getCorrectAnswers());
        listOfAnswers.addAll(entirety.getIncorrectAnswers());
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
        scoreTextView.setText("Score: " + score + ", total score: " + totalScore);
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
            answers.addAll(shuffleListOfAnswers(globalEntirety));
            radioButton1.setText(answers.get(0).getTextOfAnswer());
            radioButton2.setText(answers.get(1).getTextOfAnswer());
            radioButton3.setText(answers.get(2).getTextOfAnswer());
            radioButton4.setText(answers.get(3).getTextOfAnswer());
        } else {
            checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
            checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
            checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
            checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
            checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
            answers.addAll(shuffleListOfAnswers(globalEntirety));
            checkBox1.setText(answers.get(0).getTextOfAnswer());
            checkBox2.setText(answers.get(1).getTextOfAnswer());
            checkBox3.setText(answers.get(2).getTextOfAnswer());
            checkBox4.setText(answers.get(3).getTextOfAnswer());
            checkBox5.setText(answers.get(4).getTextOfAnswer());
        }

        enabledButtons();
    }

    public void disableButtons() {
        if (globalEntirety.getCorrectAnswers().size() > 1) {
            checkBox1.setEnabled(false);
            checkBox2.setEnabled(false);
            checkBox3.setEnabled(false);
            checkBox4.setEnabled(false);
            checkBox5.setEnabled(false);
        } else {
            radioButton1.setEnabled(false);
            radioButton2.setEnabled(false);
            radioButton3.setEnabled(false);
            radioButton4.setEnabled(false);
        }

    }

    public void enabledButtons() {
        if (globalEntirety.getCorrectAnswers().size() > 1) {
            checkBox1.setEnabled(true);
            checkBox2.setEnabled(true);
            checkBox3.setEnabled(true);
            checkBox4.setEnabled(true);
            checkBox5.setEnabled(true);
        } else {
            radioButton1.setEnabled(true);
            radioButton2.setEnabled(true);
            radioButton3.setEnabled(true);
            radioButton4.setEnabled(true);
        }
    }

    public void resetValuesOfAnswers() {
        if (globalEntirety.getCorrectAnswers().size() > 1) {
            checkBox1.setChecked(false);
            checkBox2.setChecked(false);
            checkBox3.setChecked(false);
            checkBox4.setChecked(false);
            checkBox5.setChecked(false);
            checkBox1.setBackgroundColor(getResources().getColor(R.color.none));
            checkBox2.setBackgroundColor(getResources().getColor(R.color.none));
            checkBox3.setBackgroundColor(getResources().getColor(R.color.none));
            checkBox4.setBackgroundColor(getResources().getColor(R.color.none));
            checkBox5.setBackgroundColor(getResources().getColor(R.color.none));
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
        List<Entirety> entireties = new ArrayList<>();
        entireties.addAll(randomListOfEntirety);
        int number = randomListOfEntirety.size() - 1;
        if (number >= 0) {
            globalEntirety = globalEntirety.copyEntirety(entireties.get(number));
            explanationString = globalEntirety.getExplanation().toString();
            questionText.setText(globalEntirety.getQuestion().toString());
            if (globalEntirety.getCorrectAnswers().size() > 1) {
                changeLayout(false);
            } else {
                changeLayout(true);
            }
            randomListOfEntirety.remove(number);
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.feedbackRelativeLayout);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View childView = inflater.inflate(R.layout.nextquestion, (ViewGroup) findViewById(R.id.nextquestionRelativeLayout));
            relativeLayout.removeAllViews();
            relativeLayout.addView(childView);
            resetValuesOfAnswers();
        } else {
            throwFinishAlert();
        }

    }

    public boolean checkStrings(String string) {
        boolean bool = false;
        for (Answer answer : globalEntirety.getCorrectAnswers()) {
            if (answer.getTextOfAnswer().equals(string)) {
                bool = true;
            }
        }
        return bool;
    }


    public void changeLayoutOfAnswer() {
        if (globalEntirety.getCorrectAnswers().size() > 1) {
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
        int numberOfCorrectAnswers = globalEntirety.getCorrectAnswers().size();
        int numberOfAnswers = numberOfCorrectAnswers + globalEntirety.getIncorrectAnswers().size();
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
            if (checkBox5.isChecked()) {
                listOfCheckBoxes.add(checkBox5);
            } else {
                listOfUnCheckboxes.add(checkBox5);
            }
            for (CheckBox checkBox : listOfCheckBoxes) {
                for (Answer answer : globalEntirety.getCorrectAnswers()) {
                    if (checkBox.getText().toString().equals(answer.getTextOfAnswer())) {
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
                for (Answer answer : globalEntirety.getCorrectAnswers()) {
                    if (radioButton.getText().toString().equals(answer.getTextOfAnswer())) {
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
        alertBuilder.setTitle("question");
        alertBuilder.setMessage("Are you sure you wanna quit?");

        LayoutInflater inflater = QuizActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating, null);
        alertBuilder.setView(dialogView);
        ratingText = (TextView) dialogView.findViewById(R.id.mediumText);
        ratingText.setText("Your score is: ");
        ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBar);
        ratingBar.setStepSize((float) 0.01);
        ratingBar.setRating(totalScore.floatValue());
        ratingBar.setEnabled(false);

        alertBuilder.setPositiveButton("Yes please!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BigDecimal score = PersonActivity.getGlobalStaticPerson().getTotalScore();
                PersonActivity.getGlobalStaticPerson().setTotalScore(score.add(totalScore));
                startActivity(new Intent(QuizActivity.this, MainActivity.class));
                finish();
            }
        }).setNegativeButton("Nooo bro ;)", new DialogInterface.OnClickListener() {
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
    private static final String update_person = "";


    public void updateScoreToPersonDatabase() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, update_person, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters
                params.put("username", RecyclerActivity.getCurrentPerson().getUsername());
                params.put("password", RecyclerActivity.getCurrentPerson().getPassword());
                params.put("score", RecyclerActivity.getCurrentPerson().getTotalScore().toString());
                return params;
            }
        };
        MySingleton.getInstance(QuizActivity.this).addToRequestQueue(stringRequest);
    }

    public void throwFinishAlert() {
        alertBuilder = new AlertDialog.Builder(QuizActivity.this);
        alertBuilder.setTitle("you are fininshed quiz");
        alertBuilder.setMessage("Congratulation");

        LayoutInflater inflater = QuizActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating, null);
        alertBuilder.setView(dialogView);
        ratingText = (TextView) dialogView.findViewById(R.id.mediumText);
        ratingText.setText("Your score is: ");
        ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBar);
        ratingBar.setStepSize((float) 0.01);
        ratingBar.setRating(totalScore.floatValue());
        ratingBar.setEnabled(false);

        alertBuilder.setPositiveButton("Go to Main menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BigDecimal score = PersonActivity.getGlobalStaticPerson().getTotalScore();
                PersonActivity.getGlobalStaticPerson().setTotalScore(score.add(totalScore));
                updateScoreToPersonDatabase();
                startActivity(new Intent(QuizActivity.this, MainActivity.class));
                finish();
            }
        }).setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("start again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BigDecimal score = PersonActivity.getGlobalStaticPerson().getTotalScore();
                PersonActivity.getGlobalStaticPerson().setTotalScore(score.add(totalScore));
                updateScoreToPersonDatabase();
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
        alertBuilder.setTitle("info");
        alertBuilder.setMessage("are you ready to start?");
        alertBuilder.setPositiveButton("Let's start!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setNegativeButton("give up", new DialogInterface.OnClickListener() {
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


    private String string = "ovo je question rednog broja 1. hehe ????#" + "točan question jedan@drugi točan question hehe&"
            + "sdfa s asf@asfda11111sdffads@asdf a11111111sdf adsf s#" + "objašnjen1111111111je pas mater#"

            + "ovo je question rednog broja 2. hehe ????#" + "točan question 2.1@točan odogovor 2.2&"
            + "sdfa 222s asfasfdasdffads@asdf 222as@df adsf s#" + "objašnje2222nje pas mater#"

            + "ovo je question rednog broja_3. za nadopunjavanje hehe ????#" + "točan question 3&"
            + "(sdfa 3333s asf)@(asfdasdffads)@(asdf asdf adsf s)#" + "objašnjenje pas 33333mater#"

            + "{ovo je question rednog broja_4. za nadopunjavanje hehe ????}#" + "točan question 4&"
            + "(sdfa 4444s asf)@(asfd44444asdffads)@(asdf 4444asdf adsf s)#" + "objašnjenje pas 44444mater#ovo je question rednog broja 1. hehe ????#"
            + "točan question jedan@drugi točan question hehe&"
            + "sdfa s asf@asfda11111sdffads@asdf a11111111sdf adsf s#" + "objašnjen1111111111je pas mater#"

            + "ovo je question rednog broja 2. hehe ????#" + "točan question 2.1@točan odogovor 2.2&"
            + "sdfa 222s asfasfdasdffads@asdf 222@asdf adsf s#" + "objašnje2222nje pas mater#"

            + "ovo je question rednog broja_3. za nadopunjavanje hehe ????#" + "točan question 3&"
            + "(sdfa 3333s asf)@(asfdasdffads)@(asdf asdf adsf s)#" + "objašnjenje pas 33333mater#"

            + "{ovo je question rednog broja_4. za nadopunjavanje hehe ????}#" + "točan question 4&"
            + "(sdfa 4444s asf)@(asfd44444asdffads)@(asdf 4444asdf adsf s)#" + "objašnjenje pas 44444mater";


    private String correct = null;
    private String incorrect = null;
    private String[] thirdsDevision = string.split("#");

    private Question question = new Question(null, false);
    private Explanation explanation = new Explanation(null);
    private Answer answer = new Answer(null);
    private List<Answer> correctAnswers = new ArrayList<>();
    private List<Answer> incorrectAnswers = new ArrayList<>();

    private Entirety entirety = new Entirety(question, explanation, correctAnswers, incorrectAnswers);
    private List<Entirety> listOfEntirety = new ArrayList<>();
    private List<Entirety> randomListOfEntirety = new ArrayList<>();


    public void shuffleListOfEntirety() {
        randomListOfEntirety = new ArrayList<>();
        randomListOfEntirety.addAll(listOfEntirety);
        Collections.shuffle(randomListOfEntirety);
        randomListOfEntirety.remove(7);
        randomListOfEntirety.remove(6);
        randomListOfEntirety.remove(5);
    }


    //ovdje ide funkcija koja ispisuje na ekran u tekstualnom obliku dobivenu podjelu :)


    public void displayQuiz() {
        int counter = 0;
        for (String string1 : thirdsDevision) {
            if (counter % 3 == 0) {
                String review = string1;
                boolean condition = false;
                if (review.contains("_")) {
                    condition = true;
                } else {
                    condition = false;
                }
                question = new Question(review, condition);
            } else if (counter % 3 == 1) {
                String sucks = string1;
                String[] middleDivision = sucks.split("&");
                for (int i = 0; i < middleDivision.length; i++) {
                    if (i % 2 == 0) {
                        correct = middleDivision[i];
                    } else {
                        incorrect = middleDivision[i];
                    }
                }
                String[] correctDivision = correct.split("@");
                String[] incorrectDivision = incorrect.split("@");

                for (String correctString : correctDivision) {
                    answer = new Answer(correctString);
                    correctAnswers.add(answer);
                }
                for (String incorrectString : incorrectDivision) {
                    answer = new Answer(incorrectString);
                    incorrectAnswers.add(answer);
                }
            } else if (counter % 3 == 2) {
                explanation = new Explanation(string1);
            }
            counter++;

            if (counter % 3 == 0) {
                if (question.isCheck()) {
                    String replacement = question.getQuestion();
                    replacement = replacement.replace("_", " _________ ");
                    question.setQuestion(replacement);
                }
                entirety = new Entirety(question, explanation, correctAnswers, incorrectAnswers);
                listOfEntirety.add(entirety);
                correctAnswers = new ArrayList<>();
                incorrectAnswers = new ArrayList<>();
                question = new Question(null, false);
                explanation = new Explanation(null);
            }

        }
    }


}
