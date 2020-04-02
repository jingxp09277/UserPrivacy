package com.ask.userprivatcy.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.ask.userprivatcy.R;
import com.ask.userprivatcy.WebActivity;

import java.util.Locale;
import java.util.Objects;

/**
 * 用户安全隐私声明
 */
public class UserPrivacyProtocolDialog extends DialogFragment {

    private static final String CONFIG_NAME = "UserPrivacyProtocol";
    private static final String FIRST_SHOW = "first_show";
    private static final String TAG = UserPrivacyProtocolDialog.class.getSimpleName();

    private String content;

    public static UserPrivacyProtocolDialog newInstance(String content) {
        UserPrivacyProtocolDialog dialog = new UserPrivacyProtocolDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Pro_Round_Dialog);
        content = getArguments().getString("content", null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_privacy_protocol, container);
        TextView tv = rootView.findViewById(R.id.tv);

        String str = getString(R.string.tv_privacy_protocol);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        final int start = str.indexOf("《");//第一个出现的位置
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String link = getResources().getString(R.string.privacy_policy_url);
                WebActivity.start(requireActivity(), link, getString(R.string.privacy_policy));
                Toast.makeText(requireActivity(), getString(R.string.privacy_policy), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(requireActivity(), R.color.pro_blue));
                ds.setUnderlineText(false);
            }
        }, start, start + 6, 0);

        int end = str.lastIndexOf("《");
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String link = getResources().getString(R.string.user_agreement_url);
                WebActivity.start(requireActivity(), link, getString(R.string.user_agreement));
                Toast.makeText(requireActivity(), getString(R.string.user_agreement), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(requireActivity(), R.color.pro_blue));
                ds.setUnderlineText(false);
            }
        }, end, end + 6, 0);

        tv.setText(ssb, TextView.BufferType.SPANNABLE);
        // support scroll move
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv.setMovementMethod(LinkMovementMethod.getInstance());


        rootView.findViewById(R.id.btn_agree).setOnClickListener(view -> {
            if (listener != null) {
                listener.agree();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        rootView.findViewById(R.id.btn_disagree).setOnClickListener(view -> {
            if (listener != null) {
                listener.disagree();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        TextView tvTitle = rootView.findViewById(R.id.tv_title0);
        tvTitle.setText(R.string.pro_privacy_protocol);

        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        return rootView;
    }

    private IListener listener;

    public void setListener(IListener listener) {
        this.listener = listener;
    }

    public interface IListener {
        void agree();

        void disagree();
    }


    /**
     * 是否为第一次运行
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isFirstShow(Context context) {
        Log.d(TAG, "isFirstShow()");
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(UserPrivacyProtocolDialog.FIRST_SHOW, true);
    }

    /**
     * 同意用户隐私政策
     *
     * @param context Context
     */
    public static void agreeUserPrivacyProtocol(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        boolean firstShow = sharedPreferences.getBoolean(UserPrivacyProtocolDialog.FIRST_SHOW, true);
        if (firstShow) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(UserPrivacyProtocolDialog.FIRST_SHOW, false);
            editor.apply();
        }
    }

    /**
     * Des:获取系统语言 格式：zh
     */
    private String getSystemLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        String lang = locale.getLanguage();
        Log.d("SplashActivity", "getSystemLanguage: " + lang);
        return lang;
    }
}
