package com.example.guillemmarcet.MultiQuizPro;

import android.content.DialogInterface;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    public static final String CORRECT_ANSWER = "correct_answer";
    public static final String CURRENT_QUESTION = "current_question";
    public static final String CORRECTAS = "correctas";
    public static final String RESPUESTAS = "respuestas";
    String[] all_preguntas;
    int[] id_radio={R.id.radioButton1,R.id.radioButton2,R.id.radioButton3,R.id.radioButton4};
    TextView enunciat;

    Button btn_check;
    Button btn_prev;
    RadioGroup group;

    int correct_answer=-1;
    int pregunta_actual=0;
    boolean[] correctas;
    int[] respuestas;

   @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       outState.putInt(CORRECT_ANSWER,correct_answer);
       outState.putInt(CURRENT_QUESTION,pregunta_actual);
       outState.putBooleanArray(CORRECTAS,correctas);
       outState.putIntArray(RESPUESTAS, respuestas);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        all_preguntas =getResources().getStringArray(R.array.RB_text);
        enunciat=(TextView) findViewById(R.id.text_question);
        group=(RadioGroup)findViewById(R.id.radioGrupo);

        btn_check=(Button)findViewById(R.id.btn_check);
        btn_prev=(Button)findViewById(R.id.btn_prev);

        if(savedInstanceState==null){
            inicializar();
        } else  {
            Bundle state=savedInstanceState;
            correct_answer=state.getInt(CORRECT_ANSWER);
            pregunta_actual=state.getInt(CURRENT_QUESTION);
            correctas=state.getBooleanArray(CORRECTAS);
            respuestas=state.getIntArray(RESPUESTAS);
            updateQuestion();
        }



        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id_checked=group.getCheckedRadioButtonId();
                int index_checked=-1;
                for(int i=0;i<id_radio.length;i++){
                    if(id_checked==id_radio[i]){
                        index_checked=i;
                    }
                }
                respuestas[pregunta_actual]=index_checked;
                if(index_checked==correct_answer){
                    correctas[pregunta_actual]=true;
                }
                else{

                    correctas[pregunta_actual]=false;
                }

                if(pregunta_actual==all_preguntas.length-1){
                    checkResults();
                }

                if(pregunta_actual<all_preguntas.length-1) {
                    pregunta_actual++;
                    group.clearCheck();
                    updateQuestion();
                }

            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id_checked=group.getCheckedRadioButtonId();
                int index_checked=-1;
                for(int i=0;i<id_radio.length;i++){
                    if(id_checked==id_radio[i]){
                        index_checked=i;
                    }
                }
                respuestas[pregunta_actual]=index_checked;

                if(pregunta_actual>0){
                    pregunta_actual--;
                    group.clearCheck();
                    updateQuestion();
                }
            }
        });
    }

    private void inicializar() {
        correctas=new boolean[all_preguntas.length];
        respuestas=new int[all_preguntas.length];
        for(int i=0;i<all_preguntas.length;i++) {
            respuestas[i] = -1;
        }
        pregunta_actual=0;
        updateQuestion();
    }

    private void checkResults() {
        int contador=0,incorrectas=0,noContestadas=0;
        for(int i=0;i<all_preguntas.length;i++){
            if(correctas[i]){
                contador++;
            }
            else if(respuestas[i]==-1){
                noContestadas++;
            }
            else incorrectas++;
        }
        String s= getResources().getString(R.string.correctas)+" "+String.valueOf(contador)+"\n"+
                getResources().getString(R.string.incorrectas)+" "+String.valueOf(incorrectas)+"\n"+
                getResources().getString(R.string.nocontestadas)+" "+String.valueOf(noContestadas);


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(R.string.results);
        builder.setMessage(s);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.start_over, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                inicializar();
            }
        });
        builder.create().show();





    }

    private void updateQuestion(){
        String[] text_actual=all_preguntas[pregunta_actual].split(";");
        enunciat.setText(text_actual[4]);
        for(int i=0;i<4;i++){
            RadioButton radio_btn=(RadioButton)findViewById(id_radio[i]);
            String opcio=text_actual[i];
            if(opcio.startsWith("*")){
                correct_answer=i;
                opcio=opcio.substring(1);
            }
            radio_btn.setText(opcio);
            if(respuestas[pregunta_actual]==i){
                radio_btn.setChecked(true);
            }
        }

        if(pregunta_actual==0){
            btn_prev.setVisibility(View.GONE);
        }else{
            btn_prev.setVisibility(View.VISIBLE);
        }
        if(pregunta_actual==all_preguntas.length-1){
            btn_check.setText(R.string.finish);
        }else{
            btn_check.setText((R.string.btn_next));
        }
    }
}
