package com.innovative.csbandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.innovative.csbandroid.data.Book;
import com.innovative.csbandroid.data.Books;
import com.innovative.csbandroid.data.XmlHelperBooks;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static int LABEL_TEXT_COLOR = Color.rgb(111, 187, 222);
	private static int BOX_COLOR = Color.rgb(0x37, 0x3E, 0x48);
	private static float TEXT_SIZE = 22.00f;
	private final Handler mHandler = new Handler();
	XmlHelperBooks mXmlHelperBooks = new XmlHelperBooks();
	Books mBooks = null;
	Button mBtnLoad = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mBtnLoad = (Button) findViewById(R.id.btnLoad);
        if (mBtnLoad!=null)
        	mBtnLoad.setOnClickListener(mBtnClickListener);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    final Runnable updateRunnable = new Runnable() 
    {
    	public void run() 
    	{
    		//call the activity method that updates the UI
    		updateUI();
    	}
    };
    
    void updateUI()
    {
    	if (mBooks==null)
    		return;
    	RelativeLayout rlBooksBox = (RelativeLayout) findViewById(R.id.rlBooks);
    	
    	
    	final int BOOKBOX_BASE = 1000;
    	final int BOOKLABEL_BASE = 2000;
    	final int BOOKCHAPTERCOUNT_BASE = 3000;
    	final int BOOKCOMMENTCOUNT_BASE = 4000;
    	int prevBoxId = 0;
    	int prevLabelId = 0;
    	
    	Rect boxPadding = new Rect(5,15,5,15);
    	Rect boxMargin = new Rect(0,0,0,1);
		Rect labelPadding = new Rect(3,3,3,3);
		Rect labelMargin = new Rect(1,2,1,2);
    	
    	
    	int count = mBooks.getBookCount();
    	for (int i=0; i<count; i++)
    	{
    		Book book = mBooks.getBook(i+1);
    		if (book==null)
    			continue;
    		
    		int bookBoxId = BOOKBOX_BASE + i +1;
    		int bookLabelId = BOOKLABEL_BASE + i +1;
    		int bookChapterCountId = BOOKCHAPTERCOUNT_BASE + i +1;
    		int bookCommentCountId = BOOKCOMMENTCOUNT_BASE + i +1;
    		
    		RelativeLayout rlBookbox = createItemContainerBox(
    				rlBooksBox, 
    				bookBoxId,  
					prevBoxId, 
					0, 
					0, 
					0, 
					boxPadding, 
					boxMargin);
			if (rlBookbox!=null)
			{
				prevBoxId = bookBoxId;
				// Book name label
				createTextView(
					rlBookbox, 
					bookLabelId, 
					book.mBookName, 
					LABEL_TEXT_COLOR, 
					Color.TRANSPARENT, 
					0, //TARGETCONFIG_TARGETID_EDITBOX_ID, 
					0, 
					0, 
					0, 
					labelPadding, 
					labelMargin);
				
				// Book name label
				createTextView(
					rlBookbox, 
					bookChapterCountId, 
					String.valueOf(book.mChapterCount) + " Chapters", 
					Color.rgb(200, 200, 0),  
					Color.TRANSPARENT, 
					bookLabelId,
					0, 
					0, 
					0, 
					labelPadding, 
					labelMargin);
				
				// Book name label
				createTextView(
					rlBookbox, 
					bookCommentCountId, 
					String.valueOf(book.mCommentCount) + " Comments", 
					Color.rgb(200, 0, 200), 
					Color.TRANSPARENT, 
					bookChapterCountId,
					0, 
					0, 
					0, 
					labelPadding, 
					labelMargin);
				prevLabelId = bookCommentCountId;
			} // if (rlBookbox!=null)
    	}
    	
    	Toast.makeText(this, mBooks.mVersionName + " is parsed!", Toast.LENGTH_LONG).show();
    } //updateUI()
    
    private Books loadBooks()
    {
    	Books books = new Books();
    	
    	Thread th1 = new Thread(new Runnable() 
    	{
            public void run() {
            	
            	HttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost("http://cloudstudybible.com/php/getbooksxml.php");
                HttpResponse response = null;
                try 
                {
                	response = httpClient.execute(postRequest);
        		} 
                catch (ClientProtocolException e) 
                {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
                catch (IOException e) 
        		{
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
                
                if (response!=null)
                {
                	try 
                	{
						mBooks = mXmlHelperBooks.ParseBooksFromXmlStream(response.getEntity().getContent());
					} 
                	catch (IllegalStateException e) 
                	{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
                	catch (IOException e) 
                	{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	mHandler.post(updateRunnable);
                } //if (response!=null)
            } //run()
          });
        th1.start(); //new Thread
    	
    	return books;
    } //loadBooks()
    
    private OnClickListener mBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) 
		{
			int id = v.getId();
        	switch (id)
        	{
        	case R.id.btnLoad:
        		{
        			loadBooks();
        		}
        		break;
        	} //switch
		} //onClick()
    }; //OnClickListener
    
    private void createTextView(
			ViewGroup parent, 
			int id, 
			String text, 
			int textColor, 
			int backColor, 
			int idBelow, 
			int idAbove, 
			int leftOf, 
			int idRightOf, 
			Rect padding, 
			Rect margin)
	{
		TextView tvTextView = new TextView(this);
		if (id>0)
			tvTextView.setId(id);
		if (padding!=null)
			tvTextView.setPadding(padding.left, padding.top, padding.right, padding.bottom);
		//tvTextView.setPadding(3, 3, 3, 3);
		tvTextView.setBackgroundColor(backColor);
		tvTextView.setTextColor(textColor);
		tvTextView.setTextSize(TEXT_SIZE);
		tvTextView.setText(text);
		RelativeLayout.LayoutParams lpTextView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		//lpTextView.setMargins(2, 2, 2, 2);
		if (margin!=null)
			lpTextView.setMargins(margin.left, margin.top, margin.right, margin.bottom);
		if (idBelow>0)
			lpTextView.addRule(RelativeLayout.BELOW, idBelow);
		if (idAbove>0)
			lpTextView.addRule(RelativeLayout.ABOVE, idAbove);
		if (leftOf>0)
			lpTextView.addRule(RelativeLayout.LEFT_OF, leftOf);
		if (idRightOf>0)
			lpTextView.addRule(RelativeLayout.RIGHT_OF, idRightOf);
		tvTextView.setVisibility(View.VISIBLE);
		if (parent!=null)
			parent.addView(tvTextView, lpTextView);
	} //createTextView()
    
    private RelativeLayout createItemContainerBox(
			ViewGroup parent, 
			int id, 
			int idBelow, 
			int idAbove, 
			int leftOf, 
			int idRightOf, 
			Rect padding, 
			Rect margin)
	{
		RelativeLayout rlItemBox = new RelativeLayout(this);
		if (id>0)
			rlItemBox.setId(id);
		if (padding!=null)
			rlItemBox.setPadding(padding.left, padding.top, padding.right, padding.bottom);
		rlItemBox.setBackgroundColor(BOX_COLOR);
		//rlItemBox.setBackgroundResource(R.drawable.gradientbox);
		RelativeLayout.LayoutParams lpItemBox = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		if (margin!=null)
			lpItemBox.setMargins(margin.left, margin.top, margin.right, margin.bottom);
		
		if (idBelow>0)
			lpItemBox.addRule(RelativeLayout.BELOW, idBelow);
		if (idAbove>0)
			lpItemBox.addRule(RelativeLayout.ABOVE, idAbove);
		if (leftOf>0)
			lpItemBox.addRule(RelativeLayout.LEFT_OF, leftOf);
		if (idRightOf>0)
			lpItemBox.addRule(RelativeLayout.RIGHT_OF, idRightOf);
		
		rlItemBox.setVisibility(View.VISIBLE);
		if (parent!=null)
			parent.addView(rlItemBox, lpItemBox);
		
		return rlItemBox;
	} //createItemContainerBox()
}
