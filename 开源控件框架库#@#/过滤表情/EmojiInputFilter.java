package com.nanbo.vocationalschools.utils;

import android.text.InputFilter;
import android.text.Spanned;

import com.chenzj.baselibrary.base.manager.ToastManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CHEN on 2018/1/2.
 */

public class EmojiInputFilter implements InputFilter {
    Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher emojiMatcher = emoji.matcher(source);
        if (emojiMatcher.find()) {
            ToastManager.getInstance().showToast("不支持输入表情");
            return "";
        }
        return null;
    }
}
