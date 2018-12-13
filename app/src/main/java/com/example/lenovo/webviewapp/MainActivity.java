package com.example.lenovo.webviewapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    WebView webView;
    ImageView imageView;
    LinearLayout linearLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    String myCurrentURL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=findViewById(R.id.Pb1);
        webView=findViewById(R.id.Wv1);
        imageView=findViewById(R.id.Iv1);
        linearLayout=findViewById(R.id.WebLinear);
        swipeRefreshLayout=findViewById(R.id.refresh_layout);




        progressBar.setMax(100);//sets progress bar from 0  to 100
        webView.loadUrl("https://www.google.com/");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                linearLayout.setVisibility(View.VISIBLE);//shows the progress bar
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                linearLayout.setVisibility(View.GONE);//hides the progress bar
                swipeRefreshLayout.setRefreshing(false);//takes out the reloading icon
                super.onPageFinished(view, url);
                myCurrentURL=url;// creating the page url to share it
            }
        });// takes out the error of opening youtube in the youtube app

        webView.setWebChromeClient(new WebChromeClient(){


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                imageView.setImageBitmap(icon);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.super_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_back:
                onBackPressed();
                break;

            case R.id.menu_forward:
                onForwardPressed();
                break;

            case R.id.menu_refresh:
                webView.reload();
                break;

            case R.id.menu_share:
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,myCurrentURL);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Url copied");
                startActivity(Intent.createChooser(shareIntent,"Share url with your budddiesssss"));

        }
        return super.onOptionsItemSelected(item);
    }

    private void onForwardPressed(){
        if(webView.canGoForward()){
            webView.goForward();
        }else{
            Toast.makeText(this, "cannot go further",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
    if(webView.canGoBack()){
        webView.goBack();

    }else{
        finish();
    }
    }


}
