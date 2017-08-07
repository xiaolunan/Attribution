package com.example.renchunlin.attribution;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //输入框
    private EditText mEditText;
    //运营商Logo
    private ImageView mImageView;
    //显示结果
    private TextView mTextView;
    //按钮
    private Button btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_del,btn_query;

    //标记位
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mEditText= (EditText) findViewById(R.id.mEditText);
        mImageView= (ImageView) findViewById(R.id.mImageView);
        mTextView= (TextView) findViewById(R.id.mTextView);

        btn_0= (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_1= (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2= (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3= (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4= (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5= (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6= (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7= (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8= (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9= (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_del= (Button) findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        btn_query= (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);

        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mEditText.setText("");
                mTextView.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
                return false;
            }
        });
    }

    //点击事件
    @Override
    public void onClick(View view) {
        //获取输入框的内容
        String str = mEditText.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:

                if(flag){
                    flag=false;
                    str="";
                    mEditText.setTag(str);
                    mTextView.setVisibility(View.GONE);
                    mImageView.setVisibility(View.GONE);
                }

                //每次结尾添加所按的数字
                mEditText.setText(str + ((Button) view).getText());

                break;

            case R.id.btn_del:
                if (!TextUtils.isEmpty(str) && str.length() > 0) {
                    mEditText.setText(str.substring(0, str.length() - 1));
                }
                mTextView.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
                break;

            case R.id.btn_query:
                if (!TextUtils.isEmpty(str) && str.length() > 0) {
                    getPhone(str);
                }else{
                    Toast.makeText(this, "号码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getPhone(String str) {
        String url="https://www.iteblog.com/api/mobile.php?mobile="+str+"";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Method.PUBLIC, url,
                new Response.Listener<String>() {
                    // 成功
                    @Override
                    public void onResponse(String json) {
                        Volley_Json(json);
                        //Toast.makeText(MainActivity.this, "成功："+json, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            // 失败
            @Override
            public void onErrorResponse(VolleyError errorLog) {
                Toast.makeText(MainActivity.this, "失败："+errorLog.toString(), Toast.LENGTH_LONG).show();
                Log.d("TGA","失败："+errorLog.toString());
            }
        });
        queue.add(request);

    }

    private void Volley_Json(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            String province=jsonObject.getString("province");
            String city=jsonObject.getString("city");
            String operator=jsonObject.getString("operator");
            String areaCode=jsonObject.getString("areaCode");
            String zip=jsonObject.getString("zip");
            String searchStr=jsonObject.getString("searchStr");
            String ret=jsonObject.getString("ret");

                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText("归属地："+province+city+"\n"+
                        "区号："+areaCode+"\n"+
                        "邮编："+zip+"\n"+
                        "运营商："+operator+"\n"+
                        "手机号："+searchStr);

            switch (operator){
                case "中国移动":
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setBackgroundResource(R.drawable.china_mobile);
                    break;

                case "中国联通":
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setBackgroundResource(R.drawable.china_unicom);
                    break;

                case "中国电信":
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }

            flag=true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
