package com.innovative.csbandroid.data;

public class Book
{
	// Attributes
	public int mBookID;
	public String mFullName;
	public String mShortName;
	public int mChapterCount;
	public int mCommentCount;
	
	//Actual Value
	public String mBookName;
	
	public Book()
	{
		init();
	}
	
	public Book(int bookID)
	{
		init();
		mBookID = bookID;
	}
	
	public Book(int bookID, String name)
	{
		init();
		mBookID = bookID;
		mBookName = name;
	}
	
	private void init()
	{
		mBookID = 0;
		mFullName = "";
		mShortName = "";
		mChapterCount = 0;
		mCommentCount = 0;
	}
	
	public String toString()
	{
		return mBookName;
	}
}