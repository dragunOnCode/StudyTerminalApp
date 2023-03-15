package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.student.StudentHomeActivity;
import com.example.studyterminalapp.activity.teacher.TeacherHomeActivity;
import com.example.studyterminalapp.activity.admin.AdminHomeActivity;
import com.example.studyterminalapp.adapter.MyPagerAdapter;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.SaTokenInfo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.edittext.ClearEditText;
import com.xuexiang.xui.widget.edittext.PasswordEditText;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;

public class LoginActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private View loginView, registerView;
    private ArrayList<View> mViews;  //存放视图的数组
    private ArrayList<String> mtitle;//存放标题的数组
    private MyPagerAdapter mAdapter;//适配器
    private Button btnLogin, btnRegister, btnCookie;
    private ClearEditText etLoginUsername, etRegisterUsername;
    private PasswordEditText etLoginPassword, etRegisterPassword, etConfirmPassword;
    private RadioGroup loginRole, registerRole;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();


    }

    private void initView() {
        mViewPager = findViewById(R.id.viewpager);

        LayoutInflater inflater = getLayoutInflater();
        loginView = inflater.inflate(R.layout.login_tab, null);
        registerView = inflater.inflate(R.layout.register_tab, null);

        mViews = new ArrayList<>();
        mViews.add(loginView);
        mViews.add(registerView);

        mtitle = new ArrayList<>();
        mtitle.add("登录");
        mtitle.add("注册");

        mAdapter = new MyPagerAdapter(mViews, mtitle);
        mViewPager.setAdapter(mAdapter);

        btnLogin = loginView.findViewById(R.id.btn_login);
        btnRegister = registerView.findViewById(R.id.btn_register);
        btnCookie = loginView.findViewById(R.id.btn_cookie);

        etLoginUsername = loginView.findViewById(R.id.et_login_username);
        etLoginPassword = loginView.findViewById(R.id.et_login_password);
        etRegisterUsername = registerView.findViewById(R.id.et_register_username);
        etRegisterPassword = registerView.findViewById(R.id.et_register_password);
        etConfirmPassword = registerView.findViewById(R.id.et_confirm_password);

        loginRole = loginView.findViewById(R.id.rg_login_role);
        registerRole = registerView.findViewById(R.id.rg_register_role);



        btnLogin.setOnClickListener(new LoginListener());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(LoginActivity.this, "register被按下", Toast.LENGTH_SHORT).show();
            }
        });

        btnCookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsCookiePersistor cookiePersistor = new SharedPrefsCookiePersistor(LoginActivity.this);
                List<Cookie> cookies = cookiePersistor.loadAll();
                for (Cookie cookie : cookies) {
                    Log.i("Cookie", cookie.toString());
                }
            }
        });
    }

    class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            HashMap<String, Object> param = new HashMap<>();
            param.put("username", etLoginUsername.getText().toString());
            param.put("password", etLoginPassword.getText().toString());
            param.put("loginStatus", 0);
            String role = ((RadioButton) loginView.findViewById(loginRole.getCheckedRadioButtonId())).getText().toString();
            if ("教师".equals(role)) {
                param.put("loginStatus", 1);
            } else if ("管理员".equals(role)) {
                param.put("loginStatus", 2);
            }
            RequestManager request = RequestManager.getInstance();
            try {
                request.PostRequest(param, Constants.DO_LOGIN, new RequestManager.ResultCallback() {
                    @Override
                    public void onResponse(String responseCode, String response) {
                        int code = Integer.parseInt(responseCode);
                        switch (code) {
                            case 200:
                                Log.i("Login Response", response);
                                Type dataType = new TypeToken<Result<SaTokenInfo>>(){}.getType();
                                Result<SaTokenInfo> result = JsonParse.getInstance().getResult(response, dataType);

                                SaTokenInfo data = result.getData();
                                String type = data.getLoginId().substring(0, 1);
                                int id = Integer.parseInt(data.getLoginId().substring(1));
                                MyApp.setId(id);
                                Log.i("Login Response", response);
                                Class cls = null;
                                if ("s".equals(type)) {
                                    cls = StudentHomeActivity.class;
                                } else if ("t".equals(type)) {
                                    cls = TeacherHomeActivity.class;
                                } else if ("a".equals(type)) {
                                    cls = AdminHomeActivity.class;
                                } else {
                                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "没有选择用户身份", Toast.LENGTH_SHORT));
                                    return;
                                }
                                Intent intent = new Intent(LoginActivity.this, cls);
                                // todo:这里测试用id=6的学生，以后换成正式id
                                intent.putExtra("id", id);
                                LoginActivity.this.startActivity(intent);
                                break;
                            default:
                                Log.e("Login Response Not 200", response);
                                break;
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("Login Error", msg);
                        //Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

//            Gson json = new Gson();
//
//            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toJson(loginDto));
//            Request request = new Request
//                    .Builder()
//                    .post(requestBody)
//                    .url(Constants.WEB_SITE + Constants.DO_LOGIN)
//                    .build();
//            Call call = client.newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    String json = response.body().string();
//                    Log.d("TEST", "JSON: " + json);
//                    Result result = JsonParse.getInstance().getResult(json);
//                    switch (result.getStatus()) {
//                        case 200:
//                            // todo: 跳转
//                            break;
//                        default:
//                            Log.i("Error Login", result.getMsg());
//                            break;
//                    }
//                }
//            });
        }
    }

}