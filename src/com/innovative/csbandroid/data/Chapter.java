package com.innovative.csbandroid.data;

import java.util.HashMap;

public class Chapter
{
	// Attributes
	public int mChapterID;
	public int mChapterNumber;
	public int mCommentCount;
	public int mBookID;
	public String mBookName;
	public String mBookShortName;
	public int mVerseCount;
	public String mVersion; //"KJV", "NIV", etc.,
	
	//Actual Value
	private HashMap<String, Verse> mVersesMap = null;
	
	public Chapter()
	{
		init();
	}
	
	public Chapter(int chapterID)
	{
		init();
		mChapterID = chapterID;
	}
	
	public void addVerse(Verse verse)
	{
		if (mVersesMap==null)
			mVersesMap = new HashMap<String, Verse>();
		
		if (verse!=null)
			mVersesMap.put(String.valueOf(verse.mVerseID), verse);
	}
	
	public Verse getVerse(String verseID)
	{
		return mVersesMap.get(String.valueOf(verseID));
	}
	
	public int getVerseCount()
	{
		return mVersesMap.size();
	}
	
	private void init()
	{
		mChapterID = 0;
		mChapterNumber = 0;
		mCommentCount = 0;
		mBookID = 0;
		mBookName = "";
		mBookShortName = "";
		mVerseCount = 0;
		mVersion = ""; //"KJV", "NIV", etc.,
		mVersesMap = new HashMap<String, Verse>();
	}
	
	
	public String toString()
	{
		return "Chapter " + String.valueOf(mChapterNumber);
	}
}