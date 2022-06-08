package com.dwiky.sigpertanian.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.contracts.EmailContract;
import com.dwiky.sigpertanian.databinding.ActivityCodeBinding;
import com.dwiky.sigpertanian.presenters.CodePresenter;
import com.dwiky.sigpertanian.utilities.Storage;

public class CodeActivity extends AppCompatActivity implements EmailContract.CodeView {
    ActivityCodeBinding binding;
    CodePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCodeBinding.inflate(getLayoutInflater());
        presenter = new CodePresenter(this);

        setContentView(binding.getRoot());

        binding.editText1.addTextChangedListener(new GenericTextWatcher(binding.editText1, binding.editText2));
        binding.editText2.addTextChangedListener(new GenericTextWatcher(binding.editText2, binding.editText3));
        binding.editText3.addTextChangedListener(new GenericTextWatcher(binding.editText3, binding.editText4));
        binding.editText4.addTextChangedListener(new GenericTextWatcher(binding.editText4, binding.editText5));
        binding.editText5.addTextChangedListener(new GenericTextWatcher(binding.editText5, binding.editText6));
        binding.editText6.addTextChangedListener(new GenericTextWatcher(binding.editText6, null));

        binding.editText1.setOnKeyListener(new GenericKeyEvent(binding.editText1, null));
        binding.editText2.setOnKeyListener(new GenericKeyEvent(binding.editText2, binding.editText1));
        binding.editText3.setOnKeyListener(new GenericKeyEvent(binding.editText3, binding.editText2));
        binding.editText4.setOnKeyListener(new GenericKeyEvent(binding.editText4, binding.editText3));
        binding.editText5.setOnKeyListener(new GenericKeyEvent(binding.editText5, binding.editText4));
        binding.editText6.setOnKeyListener(new GenericKeyEvent(binding.editText6, binding.editText5));

        validateToken();
    }

    public void validateToken() {
        binding.btnLogin.setOnClickListener(view-> {
            String token = binding.editText1.getText().toString().trim()
                    +binding.editText2.getText().toString().trim()
                    +binding.editText3.getText().toString().trim()
                    +binding.editText4.getText().toString().trim()
                    +binding.editText5.getText().toString().trim()
                    +binding.editText6.getText().toString().trim();

            presenter.sendToken(token);
        });
    }

    @Override
    public void success(String token) {
        Storage storage = new Storage();
        storage.setCode(this, token);

        startActivity(new Intent(CodeActivity.this, PasswordActivty.class));
    }

    @Override
    public void loading(boolean loading) {
        System.out.println(loading);
    }

    @Override
    public void toast(String message) {
        Toast.makeText(CodeActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    class GenericTextWatcher implements TextWatcher {
        View view;
        View nextView;

        public GenericTextWatcher(View view, View nextView){
            this.view = view;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();

            switch (this.view.getId()){
                case R.id.editText1:
                case R.id.editText2:
                case R.id.editText3:
                case R.id.editText4:
                case R.id.editText5:

                    if(text.length() == 1){
                        nextView.requestFocus();
                    }
                    break;
                case R.id.editText6:
                    break;
            }
        }
    }

    class GenericKeyEvent implements View.OnKeyListener{
        EditText currentView, previousView;

        GenericKeyEvent(EditText currentView, EditText previousView){
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_DEL && currentView.getId() != R.id.editText1 && currentView.getText().toString().isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                this.previousView.setText(null);
                this.previousView.requestFocus();
                return true;
            }
            return false;
        }
    }
}

