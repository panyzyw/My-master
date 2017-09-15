package com.yongyida.robot.voice.master.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yongyida.robot.voice.master.constant.ContsHtml;
import com.yongyida.robot.voice.master1.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BaiduActivity extends BaseActivity {

    private WebView webView;
    private ImageButton buttonBack;
    private boolean isFirstIn = true;
	private static String resultschange;
    private boolean isreloadLocal;
    private boolean isHTMLScoregetFinish = false;
	private int count =0;
    public static String[] BAIDU_KEY = {"百度一下","搜索一下","搜一下","帮我查一下","查一下"};
    DestroyActivityReceiver receiver;
	private String key;
	private TextView search_title;
	private LinearLayout webview_layout;
    private int onclickcount = 0 ;
    private Handler mhandler = new Handler();
	private ProgressDialog progDialog;
	private boolean isneedCleanHistory = false;
	private ArrayList<String> loadHistoryUrls = new ArrayList<String>(); 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new DestroyActivityReceiver();
        IntentFilter intentFilter = new IntentFilter("com.yydrobot.STOP");
        registerReceiver(receiver, intentFilter);

        setContentView(R.layout.activity_baidu);
        key = getIntent().getStringExtra("text");
        initView();
        showProgressDialog();
        mhandler.postDelayed(mRunnable, 10000);
        baidu(key);
    }
   Runnable mRunnable = new Runnable() {
	
	@Override
	public void run() {
		dissmissProgressDialog();
		finish();
		Toast.makeText(BaiduActivity.this, getString(R.string.web_page_loading_timeout), Toast.LENGTH_SHORT).show();
	}
};
  private void initView() {
      webView = (WebView) findViewById(R.id.webView);
      webView.setBackgroundColor(0); //设置背景色  
	  webView.getBackground().setAlpha(0); 
	  webView.setSaveEnabled(true);
      webView.getSettings().setJavaScriptEnabled(true);
      webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");  
      webView.setWebViewClient(new MyWebViewClient());
      webView.setVisibility(View.INVISIBLE);
	  search_title = (TextView)findViewById(R.id.search_title);
	  search_title.setText("已为您找到与"+"“"+key+"”"+"相关信息");
	  webview_layout = (LinearLayout)findViewById(R.id.webview_layout);
	  //initFirst();
	  
	}
private void nextPage() {
	search_title.setVisibility(View.GONE);
	webview_layout.setBackgroundResource(0);
	webView.setBackgroundColor(Color.WHITE);
	webView.getBackground().setAlpha(100);
	webview_layout.setPadding(0, 0, 0, 0);
	webView.setVisibility(View.INVISIBLE);
}
private void initFirst() {
	webView.setBackgroundColor(0);
	webView.getBackground().setAlpha(0);
	search_title.setVisibility(View.VISIBLE);
	webView.setVisibility(View.INVISIBLE);
	webview_layout.setBackgroundResource(R.drawable.webbackground);
	webview_layout.setVisibility(View.VISIBLE);
	webview_layout.setPadding(10, 10, 10, 10);
}
private void showProgressDialog() {
    if (progDialog == null){
    	progDialog = new ProgressDialog(this);    	
    	progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	progDialog.setIndeterminate(false);
    	progDialog.setCancelable(true);
    	progDialog.setMessage(getResources().getString(R.string.dyc_map_search));
    }
    if(!progDialog.isShowing()){
    	progDialog.show();    	
    }
}

private void dissmissProgressDialog() {
    if (progDialog != null) {
        progDialog.dismiss();
    }
}
@Override
   protected void onPause() {
	  finish();
	super.onPause();
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webView!=null){
        	webView.getSettings().setJavaScriptEnabled(false);
        	webView.setSaveEnabled(false);
        	webView.clearHistory();
        	if(mRunnable!=null&&mhandler!=null){
        		mhandler.removeCallbacks(mRunnable);        		
        	}
        }
        resultschange = null;
        unregisterReceiver(receiver);
    }

    private void baidu(String key) {

        Log.e("jlog", "-----str:" + key);
        try {
            // 字符串转码

            key = URLEncoder.encode(key, "utf-8");

//            int n = findKey(key);
//            Log.e("jlog", "-----n:" + n);
//
//            key = key.substring(n);
            // WebView加载web资源
            webView.loadUrl("http://m.baidu.com/s?wd="+ key + "&pn=0&rn=50&tn=jsons");
            // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
//            webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    // TODO Auto-generated method stub
//                    // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                    view.loadUrl(url);
//                    return true;
//                }
//            });
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        buttonBack = (ImageButton) findViewById(R.id.imageButton_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {


			

			@Override
            public void onClick(View view) {
				WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
				int currentIndex = mWebBackForwardList.getCurrentIndex();
                if (webView.canGoBack()&&(!mWebBackForwardList.getCurrentItem().getUrl().equals("about:blank"))) {
                	
                	Log.d("WebViewbb", "mWebBackForwardList.getCurrentIndex():"+mWebBackForwardList.getCurrentIndex());
                    if(currentIndex>=1){
                    	if(mWebBackForwardList.getItemAtIndex(currentIndex-1).getUrl().equals("about:blank")){
                    		webView.loadDataWithBaseURL(null, resultschange, "text/html", "utf-8", null);
                    	}else{
                    		webView.goBack();	
                    	}
                    }else{
                      finish();
                    }    
                }else{
                	finish();
                } 
            }
        });
    }
    /**
     * 页面跳转开始，结束与重定向监听
     * */
    final class MyWebViewClient extends WebViewClient{    
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		showProgressDialog();
    		Log.d("WebView","shouldOverrideUrlLoading");
    		return super.shouldOverrideUrlLoading(view, url);
    	}
    	@Override
    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
    		Log.d("WebView","onPageStarted");
    		super.onPageStarted(view, url, favicon);
    	}
    	@Override
    	public void onPageFinished(WebView view, String url) {
    		if(isFirstIn){
        		view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +  
        				"document.getElementsByTagName('html')[0].innerHTML+'</head>');");
         	}else{
         		dissmissProgressDialog();
         	}
    		if(isneedCleanHistory){
    			isneedCleanHistory = false;
    			webView.clearHistory();
    		}
    		Log.d("WebView","onPageFinished");
    		super.onPageFinished(view, url);
    	}

    }  
    private int findKey(String str)
    {
        int ret = -1;
        for(int i=0;i<BAIDU_KEY.length;i++)
        {
            String key = BAIDU_KEY[i].substring(0);
            try {
                key = URLEncoder.encode(key, "utf-8");
                ret = str.indexOf(key);
                if(ret == 0)
                {
                    ret = key.length();
                    break;
                }
            } catch (UnsupportedEncodingException e) {
            
                e.printStackTrace();
            }

        }
        return ret;
    }
    /**
     * javaScript 获取百度界面源码，解析并构建新html字符串
     * */
    final class InJavaScriptLocalObj { 
 	   @JavascriptInterface
        public void showSource(final String html) {  
            Log.d("HTML", html);
            
            new Thread(new Runnable() {
 			@Override
 			public void run() {
 				try {
 					if(isFirstIn){
 						isFirstIn = false;				
 						   Document doc = Jsoup.parse(html);
 						   
 						  Element results = doc.getElementById("results");
 						  results.attr("style", "background:none");
				 		  Elements result_styles_bg = results.getElementsByClass("c-container");
				 		  for (Element element : result_styles_bg) {
				 			 element.attr("style","background:none");
						}
 						   Element head = doc.head();
 						   head.getElementsByTag("noscript").remove();
 						   String Shead = head.text();
 				           StringBuffer str = new StringBuffer();
 				           str.append(ContsHtml.HTML_HEAD);
                           str.append(""); 				         
 				           str.append(results.toString());
 				           str.append(head.toString());
 				           str.append(ContsHtml.HTML_END);
 						   resultschange = str.toString();
 						   Thread.sleep(500);
 						   runOnUiThread(new Runnable() {
 							public void run() {
 								webView.loadDataWithBaseURL(null, resultschange, "text/html", "utf-8", null);	 						
 						//		webView.getSettings().setJavaScriptEnabled(true);
 								mhandler.postDelayed(new Runnable() {
									
									@Override
									public void run() {
								    mhandler.removeCallbacks(mRunnable);
									webView.clearHistory();
									initFirst();
									dissmissProgressDialog();
									webView.setVisibility(View.VISIBLE);
									}
								}, 1500);
 							}
 						});
 					}
 				  
 				} catch (Exception e) {
 					
 					e.printStackTrace();
 				}
 			}
 		}).start();
        }  
    } 
    class DestroyActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("com.yydrobot.STOP")) {
                finish();
            }
        }
    }
}

