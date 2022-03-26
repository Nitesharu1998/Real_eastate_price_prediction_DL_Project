package com.example.realeastatepriceprediction;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class emicalultor extends AppCompatActivity {
    TextView tv_quotation;
    EditText P;
    EditText I;
    EditText Y;
    EditText TI,result;
    Button emiCalcBtn;
    Context context;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emicalultor);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9006DC")));

        initUI();
        initListener();

        String sample = getIntent().getStringExtra("emitext");
        if (sample != null) {
            P.setText(sample);
        }
    }

    private void initListener() {
        emiCalcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st1 = P.getText().toString();
                String st2 = I.getText().toString();
                String st3 = Y.getText().toString();
                if (TextUtils.isEmpty(st1)) {
                    P.setError("Enter Principal Amount");
                    P.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(st2)) {
                    I.setError("Enter Interest Rate");
                    I.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(st3)) {
                    Y.setError("Enter Years");
                    Y.requestFocus();
                    return;
                }
                float p = Float.parseFloat(st1);
                float i = Float.parseFloat(st2);
                float y = Float.parseFloat(st3);

                float Principal = calPric(p);
                float Rate = calInt(i);
                float Months = calMonth(y);
                float Dvdnt = calDvdnt(Rate, Months);
                float FD = calFinalDvdnt(Principal, Rate, Dvdnt);
                float D = calDivider(Dvdnt);
                float emi = calEmi(FD, D);
                float TA = calTa(emi, Months);
                float ti = calTotalInt(TA, Principal);
                result.setText(String.valueOf(emi));
                TI.setText(String.valueOf(ti));
                keyboardhidder();

            }
        });

        tv_quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Global.isNull(result.getText().toString())){
                    openShareDialogBox();
                }
            }
        });
    }

    private void openShareDialogBox() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.supportpopup);
        overridePendingTransition(android.R.style.Widget_DeviceDefault_Light_PopupWindow, android.R.id.accessibilityActionScrollDown);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        TextInputEditText query = bottomSheetDialog.findViewById(R.id.supportquery);
        TextInputLayout queryinput = bottomSheetDialog.findViewById(R.id.supportinputlayout);
        Button btn_sendquotation = bottomSheetDialog.findViewById(R.id.send_query);
        queryinput.setHint("Enter Your Email Here");
        bottomSheetDialog.show();

        btn_sendquotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validator.emailValidation(activity,query) ){
                    String str_finalQuotation = "Principal amount: "+P.getText().toString()
                            +"\n Interest Rate: "+I.getText().toString()
                            +"\n Duration: "+Y.getText().toString()
                            +"\n Total Interest: "+TI.getText().toString()
                            +"\n Final Amount: "+result.getText().toString();

                    ShareOptions.shareOnEmail(activity,query.getText().toString(),"Quotation",str_finalQuotation);
                }
            }
        });
    }

    private void initUI() {
        P = (EditText) findViewById(R.id.principal);
        I = (EditText) findViewById(R.id.interest);
        Y = (EditText) findViewById(R.id.years);
        TI = (EditText) findViewById(R.id.interest_total);
        result = (EditText) findViewById(R.id.emi);
        emiCalcBtn = (Button) findViewById(R.id.btn_calculate2);
        tv_quotation=findViewById(R.id.tv_quotation);
        activity=emicalultor.this;
        context=this;
    }

    void keyboardhidder() {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public float calPric(float p) {
        return (float) (p * 100);
    }

    public float calInt(float i) {
        return (float) (i / 12 / 100);
    }

    public float calMonth(float y) {
        return (float) (y * 12);
    }

    public float calDvdnt(float Rate, float Months) {
        return (float) (Math.pow(1 + Rate, Months));
    }

    public float calFinalDvdnt(float Principal, float Rate, float Dvdnt) {
        return (float) (Principal * Rate * Dvdnt);
    }

    public float calDivider(float Dvdnt) {
        return (float) (Dvdnt - 1);
    }

    public float calEmi(float FD, Float D) {
        return (float) (FD / D);
    }

    public float calTa(float emi, Float Months) {
        return (float) (emi * Months);
    }

    public float calTotalInt(float TA, float Principal) {
        return (float) (TA - Principal);
    }


    public void hide(View view) {
        keyboardhidder();
    }
}
