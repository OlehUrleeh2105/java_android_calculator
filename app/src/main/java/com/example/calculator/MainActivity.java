package com.example.calculator;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.mariuszgromada.math.mxparser.Expression;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText solution;
    private TextView result;
    private boolean inp = false;
    private boolean op = false;
    private int frst = 0;
    public final int[] BUTTONS_ID = {
            R.id.b0,
            R.id.b1,
            R.id.b2,
            R.id.b3,
            R.id.b4,
            R.id.b5,
            R.id.b6,
            R.id.b7,
            R.id.b8,
            R.id.b9,
            R.id.bsin,
            R.id.bcos,
            R.id.btan,
            R.id.bctan,
            R.id.bsqrt,
            R.id.bsquare,
            R.id.blog,
            R.id.bper,
            R.id.bc,
            R.id.bac,
            R.id.bbrakes,
            R.id.bdiv,
            R.id.bmul,
            R.id.bplus,
            R.id.bminus,
            R.id.bequal,
            R.id.bdot
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.tvmain);
        solution = findViewById(R.id.tvsec);
        solution.setShowSoftInputOnFocus(false);
        solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getString(R.string.result).equals(solution.getText().toString())){
                    solution.setText("");
                }
            }
        });
        addListener();
    }

    private void addListener(){
        for (int i : BUTTONS_ID) {
            Button btn = findViewById(i);
            btn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button) view;
        String textBtn = btn.getText().toString();
        if(textBtn.equals(getString(R.string.point))){
            if (!inp) return;
            inp = false;
        }
        if(textBtn.equals(getString(R.string.minus))||textBtn.equals(getString(R.string.add))||
                textBtn.equals(getString(R.string.multiply))||textBtn.equals(getString(R.string.divide))||
                textBtn.equals(getString(R.string.prosent))){
            if (op) return;
            inp = true;
            op = true;
        }
        if(textBtn.equals("1")||textBtn.equals("2")||
                textBtn.equals("3")||textBtn.equals("4")||
                textBtn.equals("5")||textBtn.equals("6")||textBtn.equals("7")||textBtn.equals("8")
                ||textBtn.equals("9")||textBtn.equals("0")){
            op = false;
            if(frst == 0){
                inp = true;
                frst++;
            }
        }
        if(textBtn.equals(getString(R.string.clear))){
            int textLen = solution.getText().length();
            int cursorPos = solution.getSelectionStart();
            if (cursorPos != 0 && textLen != 0) {
                SpannableStringBuilder selection = (SpannableStringBuilder) solution.getText();
                selection.replace(cursorPos - 1, cursorPos, "");
                solution.setText(selection);
                solution.setSelection(cursorPos - 1);

            }
            inp = true;
            return;
        }
        if(textBtn.equals(getString(R.string.clear_all))){
            solution.setText(getString(R.string.result));
            result.setText(getString(R.string.result));
            inp = true;
            return;
        }
        if(textBtn.equals(getString(R.string.log))){
            int cursorPosStart = solution.getSelectionStart();
            int cursorPosEnd = solution.getSelectionEnd();
            if(cursorPosStart == cursorPosEnd){
                updateText(textBtn + "(,)");
                solution.setSelection(cursorPosStart + textBtn.length() + 1);
                return;
            }
            String oldStr = solution.getText().toString();
            String leftStr = oldStr.substring(0, cursorPosStart);
            String centerStr = oldStr.substring(cursorPosStart, cursorPosEnd);
            String rightStr = oldStr.substring(cursorPosEnd);
            solution.setText(String.format("%s%s%s%s%s%s", leftStr, textBtn ,"(", centerStr, ",)", rightStr));
            solution.setSelection(cursorPosStart + textBtn.length() + 1);
            inp = true;
            return;
        }
        if(textBtn.equals(getString(R.string.sin)) || textBtn.equals(getString(R.string.cos))
                || textBtn.equals(getString(R.string.tan)) || textBtn.equals(getString(R.string.ctan))
                || textBtn.equals(getString(R.string.sqrt))){
            int cursorPosStart = solution.getSelectionStart();
            int cursorPosEnd = solution.getSelectionEnd();
            if(cursorPosStart == cursorPosEnd){
                updateText(textBtn + "()");
                solution.setSelection(cursorPosStart + textBtn.length() + 1);
                inp = true;
                return;
            }
            String oldStr = solution.getText().toString();
            String leftStr = oldStr.substring(0, cursorPosStart);
            String centerStr = oldStr.substring(cursorPosStart, cursorPosEnd);
            String rightStr = oldStr.substring(cursorPosEnd);
            solution.setText(String.format("%s%s%s%s%s%s",leftStr, textBtn ,"(", centerStr, ")",rightStr));
            solution.setSelection(cursorPosStart + textBtn.length() + 1);
            inp = true;
            return;
        }
        if(textBtn.equals(getString(R.string.parentheses))){
            int cursorPosStart = solution.getSelectionStart();
            int cursorPosEnd = solution.getSelectionEnd();
            if(cursorPosStart == cursorPosEnd){
                updateText("()");
                solution.setSelection(cursorPosStart + 1);
                inp = true;
                return;
            }
            String oldStr = solution.getText().toString();
            String leftStr = oldStr.substring(0, cursorPosStart);
            String centerStr = oldStr.substring(cursorPosStart, cursorPosEnd);
            String rightStr = oldStr.substring(cursorPosEnd);
            solution.setText(String.format("%s%s%s%s%s", leftStr, "(", centerStr, ")",rightStr));
            solution.setSelection(cursorPosEnd);
            inp = true;
            return;
        }
        if(textBtn.equals(getString(R.string.equals))){
            String userExp = solution.getText().toString();
            userExp = userExp.replaceAll("÷","/");
            userExp = userExp.replaceAll("×", "*");
            Expression exp = new Expression(userExp);
            exp.disableAttemptToFixExpStrMode();
            String temp = String.valueOf(exp.calculate());

            if(temp.equals("NaN")) {
                String[] array = userExp.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)");
                for (int i = 0; i < array.length; i++) {
                    if(array[i].equals('.')) inp = false;
                    if(array[i].equals("/")){
                        if(tryParseDouble(array[i + 1])){
                            if(Double.parseDouble(array[i + 1]) == 0)
                                temp = "You can't";
                        }
                    }
                }
            }
            result.setText(temp);
//          inp = true;
            return;
        }
        updateText(textBtn);
    }
    private void updateText(String textBtn){
        String oldStr = solution.getText().toString();
        int cursorPos = solution.getSelectionStart();
        String leftStr = oldStr.substring(0, cursorPos);
        String rightStr = oldStr.substring(cursorPos);
        if(getString(R.string.result).equals(solution.getText().toString())) {
            solution.setText(textBtn);
            solution.setSelection(1);
        }
        else {
            solution.setText(String.format("%s%s%s", leftStr, textBtn, rightStr));
            solution.setSelection(cursorPos + 1);
        }
    }
    private Boolean tryParseDouble(String number){
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}