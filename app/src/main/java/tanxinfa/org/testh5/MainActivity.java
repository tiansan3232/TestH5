package tanxinfa.org.testh5;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText et_user;
    private final int TYPE_GET = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = msg.what;
            if (type == TYPE_GET){
                webView.loadUrl("javascript:javaCallJs(" + "'" + et_user.getText().toString()+"'"+")");
            }
            Log.e("MainActivity","线程名称：(handler)" + Thread.currentThread().getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_user = (EditText) findViewById(R.id.et_user);
        webView = (WebView) findViewById(R.id.wvMain);
        initWebView();
    }

    //初始化WebView

    private void initWebView() {
        //不跳转到其他浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        //支持JS
        settings.setJavaScriptEnabled(true);
        //加载本地html文件
        webView.loadUrl("file:///android_asset/JavaAndJavaScriptCall.html");
        webView.addJavascriptInterface(new JSInterface(),"Android");
    }

    //按钮的点击事件
    public void click(View view){
        //java调用JS方法
        webView.loadUrl("javascript:javaCallJs(" + "'" + et_user.getText().toString()+"'"+")");
    }

    //在页面销毁的时候将webView移除
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }

    private class JSInterface {
        //JS需要调用的方法
        @JavascriptInterface
        public void showToast(String arg){
            Toast.makeText(MainActivity.this,arg,Toast.LENGTH_SHORT).show();
            Message msg = new Message();
            msg.what = TYPE_GET;
            handler.sendMessage(msg);
            Log.e("MainActivity","线程名称：(JSInterface)" + Thread.currentThread().getName());
        }

        @JavascriptInterface
        public void shareAndroid(){
            Toast.makeText(MainActivity.this,"shareAndroid()",Toast.LENGTH_SHORT).show();
            Log.e("MainActivity","线程名称：(JSInterface)" + Thread.currentThread().getName());
        }
    }
}
