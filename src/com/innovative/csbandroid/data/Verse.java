package com.innovative.csbandroid.data;

public class Verse
{
	// Attributes
	public int mVerseID;
	public int mVerseNumber;
	public int mCommentCount;
	public String mVerseText;
	
	public Verse()
	{
		init();
	}
	
	public Verse(int verseID)
	{
		init();
		mVerseID = verseID;
	}
	
	private void init()
	{
		mVerseID = 0;
		mVerseNumber = 0;
		mCommentCount = 0;
		mVerseText = "";
	}
	
	public String toString()
	{
		return "Verse " + String.valueOf(mVerseNumber);
	}
}