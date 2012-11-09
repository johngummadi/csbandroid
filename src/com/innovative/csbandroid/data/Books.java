package com.innovative.csbandroid.data;

import java.util.HashMap;

public class Books
{
	// Attributes
	public String mVersionName;
	
	//Actual Value
	private HashMap<String, Book> mBooksMap = null;
	
	public Books()
	{
		init();
	}
	
	public Books(String versionName)
	{
		init();
		mVersionName = versionName;
	}
	
	private void init()
	{
		mVersionName = "";
		mBooksMap = new HashMap<String, Book>();
	}
	
	public void addBook(Book book)
	{
		if (mBooksMap==null)
			mBooksMap = new HashMap<String, Book>();
		
		if (book!=null)
			mBooksMap.put(String.valueOf(book.mBookID), book);
	}
	
	public Book getBook(int bookID)
	{
		return mBooksMap.get(String.valueOf(bookID));
	}
	
	public int getBookCount()
	{
		return mBooksMap.size();
	}
	
	public String toString()
	{
		return mVersionName;
	}
}