package com.month.markethelper.utils;

import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class BindingSetting {

    @BindingAdapter("addTextChangedListener")
    public static void addTextChangedListener(EditText et, TextWatcher textWatcher) {
        et.addTextChangedListener(textWatcher);
    }

    @BindingAdapter("selected")
    public static void setSelected(TextView tv, boolean selected) {
        tv.setSelected(selected);
    }

}
