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
import com.example.studyterminalapp.utils.UserManage;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.edittext.ClearEditText;
import com.xuexiang.xui.widget.edittext.PasswordEditText;
import com.xuexiang.xutil.common.StringUtils;

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
    private Button btnLogin, btnRegister, btnCookie, btnClear;
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
        btnClear = (Button) loginView.findViewById(R.id.btn_clear);

        etLoginUsername = loginView.findViewById(R.id.et_login_username);
        etLoginPassword = loginView.findViewById(R.id.et_login_password);
        etRegisterUsername = registerView.findViewById(R.id.et_register_username);
        etRegisterPassword = registerView.findViewById(R.id.et_register_password);
        etConfirmPassword = registerView.findViewById(R.id.et_confirm_password);

        loginRole = loginView.findViewById(R.id.rg_login_role);
        registerRole = registerView.findViewById(R.id.rg_register_role);



        btnLogin.setOnClickListener(new LoginListener());

        btnRegister.setOnClickListener(new RegisterListener());

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

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManage.getInstance().clearUserInfo(LoginActivity.this);
            }
        });
    }

    class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String username = etLoginUsername.getText().toString();
            String password = etLoginPassword.getText().toString();
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "请输入用户名、密码", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            HashMap<String, Object> param = new HashMap<>();
            param.put("username", username);
            param.put("password", password);
            int loginStatus = 0;
            String role = ((RadioButton) loginView.findViewById(loginRole.getCheckedRadioButtonId())).getText().toString();
            if ("教师".equals(role)) {
                loginStatus = 1;
                //param.put("loginStatus", 1);
            } else if ("管理员".equals(role)) {
                loginStatus = 2;
                //param.put("loginStatus", 2);
            }
            param.put("loginStatus", loginStatus);
            RequestManager request = RequestManager.getInstance();
            try {
                int loginRole = loginStatus;
                request.PostRequest(param, Constants.DO_LOGIN, new RequestManager.ResultCallback() {
                    @Override
                    public void onResponse(String responseCode, String response) {
                        Type dataType = new TypeToken<Result<SaTokenInfo>>(){}.getType();
                        Result<SaTokenInfo> result = JsonParse.getInstance().getResult(response, dataType);
                        Integer code = result.getStatus();
                        switch (code) {
                            case 200:
                                Log.i("Login Response", response);
                                // 跳转
                                SaTokenInfo data = result.getData();
                                String type = data.getLoginId().substring(0, 1);
                                int id = Integer.parseInt(data.getLoginId().substring(1));
                                MyApp.setId(id);
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
                                // 保存用户信息到SharedPreferences
                                UserManage.getInstance().saveUserInfo(LoginActivity.this, username, password, loginRole, data.getTokenValue());
                                Intent intent = new Intent(LoginActivity.this, cls);
                                intent.putExtra("id", id);
                                LoginActivity.this.startActivity(intent);
                                break;
                            default:
                                Log.e("Login Response Not 200", response);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UserManage.getInstance().clearUserInfo(LoginActivity.this);
                                        Toast.makeText(LoginActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("Login Error", msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    class RegisterListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String username = etRegisterUsername.getText().toString();
            String password = etRegisterPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPassword)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "请输入用户名、密码", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            if (!StringUtils.equals(password, confirmPassword)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            HashMap<String, Object> param = new HashMap<>();
            int registerStatus = 0;
            String role = ((RadioButton) registerView.findViewById(registerRole.getCheckedRadioButtonId())).getText().toString();
            if ("教师".equals(role)) {
                registerStatus = 1;
            }
            param.put("username", username);
            param.put("password", password);
            param.put("registerStatus", registerStatus);
            RequestManager request = RequestManager.getInstance();
            try {
                request.PostRequest(param, Constants.DO_REGISTER, new RequestManager.ResultCallback() {
                    @Override
                    public void onResponse(String responseCode, String response) {
                        Type dataType = new TypeToken<Result>(){}.getType();
                        Result result = JsonParse.getInstance().getResult(response, dataType);
                        Integer code = result.getStatus();
                        switch (code) {
                            case 200:
                                Log.i("Register Response", response);
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show());
                                break;
                            default:
                                Log.e("Register Response Not 200", response);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("Register Error", msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

}