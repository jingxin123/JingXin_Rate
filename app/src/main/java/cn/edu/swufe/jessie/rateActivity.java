package cn.edu.swufe.jessie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class rateActivity extends AppCompatActivity {

    private final String TAG = "Rate";
    private float dollarRate = 0.0f;
    private float euroRate = 0.0f;
    private float wonRate = 0.0f;

    EditText rmb;
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView) findViewById(R.id.showOut);

        //获取SP里保存的数据
        SharedPreferences sharedpreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate = sharedpreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedpreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedpreferences.getFloat("won_rate",0.0f);

        Log.i(TAG,"onCreate: sp dollarRate "+ dollarRate);
        Log.i(TAG,"onCreate: sp euroRate "+ euroRate);
        Log.i(TAG,"onCreate: sp wonRate "+ wonRate);

    }
    public void onClickRMB(View btn){
        Log.i(TAG,"onClick:");
        String str = rmb.getText().toString();
        Log.i(TAG,"onClick: get str = "+str);

        float r = 0;

        if(str.length()>0){
            //获取用户输入信息
            r = Float.parseFloat(str);
        }
        else{
            //提示用户输入信息
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }

        Log.i(TAG,"onClick: r = "+r);

        //计算
        if(btn.getId()==R.id.btn_dollar){
            show.setText(String.format("%.2f",r*dollarRate));
        }
        else if(btn.getId()==R.id.btn_euro){
            show.setText(String.format("%.2f",r*euroRate));
        }
        else{
            show.setText(String.format("%.2f",r*wonRate));
        }
    }

    public void openOne(View btn){
        //打开一个页面Activity
        //Intent hello = new Intent(this, SecondActivity.class);
        //Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
        //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:87092173"));
        Log.i("open","openOne:");
        openConfig();
    }

    private void openConfig() {
        Intent config = new Intent(this,configActivity.class);
        startActivity(config);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i(TAG,"openOne: dollarRate "+ dollarRate);
        Log.i(TAG,"openOne: euroRate "+ euroRate);
        Log.i(TAG,"openOne: wonRate "+ wonRate);

        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==2){

            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);

            Log.i(TAG,"onActvityResult:dollarRate = "+dollarRate);
            Log.i(TAG,"onActvityResult:euroRate = "+euroRate);
            Log.i(TAG,"onActvityResult:wonRate = "+wonRate);

            //将新设置的汇率写到SP里
            SharedPreferences sharedpreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG,"onActvityResult:wonRate = 数据已保存到SharedPreferences");
        }
    }
}
