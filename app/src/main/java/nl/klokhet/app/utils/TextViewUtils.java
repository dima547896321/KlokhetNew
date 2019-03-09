package nl.klokhet.app.utils;

import android.support.annotation.StyleRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import nl.klokhet.app.utils.Func.Func;


public class TextViewUtils {

    public static void makePartTextViewClickable(TextView textView, CharSequence charSequence,
                                                 Func onClick, @StyleRes int style) {
        String originalString = textView.getText().toString();
        SpannableString ss = new SpannableString(textView.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                onClick.call();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        int start = originalString.indexOf(charSequence.toString());
        int end = start + charSequence.length();
        ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (style != 0) {
            ss.setSpan(
                    new TextAppearanceSpan(textView.getContext(), style),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(ss);
    }

    public static boolean isTextEquals(String textOne, String textTwo) {
        return textOne.trim().equals(textTwo.trim());
    }

    public static boolean isTextEquals(TextView textOne, String textTwo) {
        if (textOne == null) {
            return false;
        }
        if (textTwo == null) {
            return true;
        }
        return textOne.getText().toString().trim().equals(textTwo.trim());
    }

    public static void setTextIfNonNull(TextView view, String text) {
        if (view != null && text != null) {
            view.setText(text);
        }
    }
}