package com.diversitisystems.primavacacional;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button calcular;
    private ImageView fechaInicio;
    private EditText editTextFechaInicio, editTextSueldo,editTextPrima,editTextFechaFin;
    private TextView textView;
    private int day, month, year, dias, diasVacaciones, prima;
    private int sueldo,aguinaldo;
    private Date fechaInicial, fechaFinal;
    private double aguinaldoAnual, aguinaldoProporcional,anio;
    /*private AdView mAdView;*/
    private String regexp;
    private AlertDialog alert;
    private SharedPreferences prefs;
    private float totalPrimaVacacional,sueldoDiario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindUI();
    }

    private void bindUI() {
        //Admob
        /*MobileAds.initialize(this, "ca-app-pub-9001957420901694~9538615966");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
        //Casteos
        fechaInicio=(ImageView) findViewById(R.id.imageView);
        calcular=(Button)findViewById(R.id.calcular);
        editTextFechaInicio=(EditText)findViewById(R.id.editTextFechaInicio);
        editTextFechaFin=(EditText)findViewById(R.id.editTextFechaFin);
        editTextSueldo=(EditText)findViewById(R.id.editTextSueldo);
        editTextPrima=(EditText) findViewById(R.id.editTextPrima);
        textView=(TextView) findViewById(R.id.textView);
        //onclick
        fechaInicio.setOnClickListener(this);
        calcular.setOnClickListener(this);
        //inicializar variables
        fechaFinal=null;
        fechaInicial=null;
        regexp = "\\d{4}/\\d{1,2}/\\d{1,2}";
        final Calendar c= Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        day=c.get(Calendar.DAY_OF_MONTH);
        month=c.get(Calendar.MONTH);
        String monthCalculo="";
        String dayCalculo="";

        monthCalculo = (month+1)>=10 ? ""+(month+1) : "0"+(month+1);
        dayCalculo = day>=10 ? ""+day : "0"+day;
        editTextFechaFin.setText(year+"/"+monthCalculo+"/"+dayCalculo);
        prefs = getSharedPreferences("PreferencesAhorro", Context.MODE_PRIVATE);

    }

    @Override
    public void onClick(View view) {
        if (view==fechaInicio){
            final Calendar c= Calendar.getInstance();
            day=c.get(Calendar.DAY_OF_MONTH);
            month=c.get(Calendar.MONTH);
            year=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    String monthCalculo="";
                    String dayCalculo="";

                    monthCalculo = (i1+1)>=10 ? ""+(i1+1) : "0"+(i1+1);
                    dayCalculo = i2>=10 ? ""+i2 : "0"+i2;

                    editTextFechaInicio.setText(i+"/"+monthCalculo+"/"+dayCalculo);
                    fechaInicial = new Date((i-1900),i1,i2);
                    /*fechaFinal = new Date(String.valueOf(Calendar.getInstance().getTime()));*/
                    fechaFinal = new Date(editTextFechaFin.getText().toString());
                }
            }, year, month,day );
            datePickerDialog.show();
        }else if (view==calcular){

            /*if (fechaInicial==null || fechaFinal==null){*/
            if (!Pattern.matches(regexp, editTextFechaInicio.getText().toString())){
                Toast.makeText(MainActivity.this,"Favor de seleccionar fecha de inicio valida en formato AAAA/MM/DD", Toast.LENGTH_LONG).show();
                return;
            }else if (!Pattern.matches(regexp, editTextFechaFin.getText().toString())){
                Toast.makeText(MainActivity.this,"Favor de seleccionar fecha de fin valida en formato AAAA/MM/DD", Toast.LENGTH_LONG).show();
                return;
            }else{
                fechaInicial = new Date(editTextFechaInicio.getText().toString());
                fechaFinal = new Date(editTextFechaFin.getText().toString());
            }

            if ( fechaFinal.before(fechaInicial)){
                Toast.makeText(MainActivity.this,"Ingresar una fecha menor a la de hoy", Toast.LENGTH_LONG).show();
            }else if (editTextSueldo.getEditableText().toString().isEmpty()){
                Toast.makeText(MainActivity.this,"Favor de llenar el campo de sueldo", Toast.LENGTH_LONG).show();
            }else if (editTextPrima.getEditableText().toString().isEmpty()){
                Toast.makeText(MainActivity.this,"Favor de llenar el campo de % de prima", Toast.LENGTH_LONG).show();
            }else {
                calcularPrima();
            }

        }
    }

    public void calcularPrima(){
        sueldo = Integer.parseInt(editTextSueldo.getEditableText().toString());
        prima = Integer.parseInt(editTextPrima.getEditableText().toString());
        dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000);
        anio=(double) dias/365;
        sueldoDiario= (float) sueldo/30;
        DecimalFormat formato = new DecimalFormat("#.00");
        float resultado;
        resultado= (float) prima/100;




        if (anio >=1 && anio <2){
            diasVacaciones=6;
        }else if (anio >=2 && anio <3){
            diasVacaciones=8;
        }else if (anio >=3 && anio <4){
            diasVacaciones=10;
        }else if (anio >=4 && anio <5){
            diasVacaciones=12;
        }else if (anio >=5 && anio <10){
            diasVacaciones=14;
        }else if (anio >=10 && anio <15){
            diasVacaciones=16;
        }else if (anio >=15 && anio <20){
            diasVacaciones=18;
        }else if (anio >=20 && anio <25){
            diasVacaciones=20;
        }else{
            diasVacaciones=0;
        }

        totalPrimaVacacional= (float) ((sueldoDiario*diasVacaciones)*(resultado));

        if (diasVacaciones==0){
            textView.setText("Aun no tienes una antigüedad superior a 1 año, tu antiguedad es de "+formato.format(anio)+" años.");
        }else{
            textView.setText("Te corresponden $"+formato.format(totalPrimaVacacional)+" de Prima Vacacional. Por "+diasVacaciones+" días de vacaciones con un "+prima+"% de prima vacacional. Y tienes una antiguedad de "+formato.format(anio)+" años");
        }




    }
}
