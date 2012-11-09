package com.innovative.csbandroid.data;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XmlHelperBooks extends DefaultHandler {
	boolean isParsing = false;
	Books mBooks = null;
	Book mTmpBook = null;
	String bookName = "";
	
	public Books ParseBooksFromXmlFile(String xmlFilePath) {
		FileInputStream fstream = null;
		try 
		{
			fstream = new FileInputStream(xmlFilePath);
		}
		catch (FileNotFoundException e1) 
		{
			fstream = null;
			e1.printStackTrace();
		}
		if (fstream!=null)
			return ParseBooksFromXmlStream(fstream);
		return null;
	} //ParseBooksFromXmlFile()
	
	public Books ParseBooksFromXmlString(String xmlString)
	{
		InputStream inStream = null;
		try 
		{
			inStream = new ByteArrayInputStream(xmlString.getBytes("UTF8"));
		}
		catch (UnsupportedEncodingException e) 
		{
			inStream = null;
			e.printStackTrace();
		}
		if (inStream!=null)
			return ParseBooksFromXmlStream(inStream);
		return null;
	} //ParseBooksFromXmlString()
	
	public Books ParseBooksFromXmlStream(InputStream inStream) 
	{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		if (inStream==null)
			return null;
		try
		{
			isParsing = true;
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse
			sp.parse(inStream, this);
		}
		catch(SAXException se) 
		{
			mBooks = null;
			se.printStackTrace();
		}
		catch(ParserConfigurationException pce) 
		{
			mBooks = null;
			pce.printStackTrace();
		}
		catch (IOException ie) 
		{
			mBooks = null;
			ie.printStackTrace();
		}
		isParsing = false;
		return mBooks;
	} //ParseBooksFromXmlStream()
	
	int makeInt(String intValueStr)
	{
		if (intValueStr==null || intValueStr.length()<=0)
			return 0;
		return Integer.parseInt(intValueStr);
	}
	//Event Handlers
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{
		//reset
		bookName = "";
		if(localName.equalsIgnoreCase("books"))
		{
			mTmpBook = null;
			mBooks = new Books();
			mBooks.mVersionName = attributes.getValue("Version");
		}
		else if(localName.equalsIgnoreCase("book"))
		{
			if (mBooks==null)
				return;
			mTmpBook = new Book();
			mTmpBook.mBookID = makeInt(attributes.getValue("BookID"));
			mTmpBook.mFullName = attributes.getValue("FullName");
			mTmpBook.mShortName = attributes.getValue("ShortName");
			mTmpBook.mChapterCount = makeInt(attributes.getValue("ChapterCount"));
			mTmpBook.mCommentCount =  makeInt(attributes.getValue("CommentCount"));
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		bookName = new String(ch,start,length);
		bookName = bookName.trim();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException 
	{
		if (mTmpBook==null)
			return;
		if(localName.equalsIgnoreCase("book")) 
		{
			mTmpBook.mBookName = bookName;
			mBooks.addBook(mTmpBook);
		}
	}
}