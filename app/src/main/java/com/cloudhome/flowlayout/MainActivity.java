package com.cloudhome.flowlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cloudhome.flowlayout.view.FlowLayout;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private String[] mVals = new String[]
            {
                    "Hello", "Android", "Welcome", "hello", "Button", "TextView",
                    "Hello", "Android", "Welcome", "hello World", "Button", "TextView",
                    "Hello", "Android", "Welcome", "hello World", "ButtonText", "TextView",
            };
    private FlowLayout mFlowLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlowLayout = (FlowLayout) findViewById(R.id.id_flowlayout);
        initData();
    }

    public void initData()
    {
//        for(int i  = 0;i < mVals.length;i++)
//        {
//            Button btn =new Button(this);
//
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//            btn.setText(mVals[i]);
//            mFlowLayout.addView(btn,lp);
//        }

        LayoutInflater mInflater = LayoutInflater.from(this);

        for(int i = 0; i < mVals.length;i++)
        {
         TextView tv= (TextView) mInflater.inflate(R.layout.tv,mFlowLayout,false);
            tv.setText(mVals[i]);
            mFlowLayout.addView(tv);
        }
    }

}
