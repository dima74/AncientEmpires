package ru.ancientempires.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;
import android.util.JsonToken;

public class JsonHelper
{
	/*
	[
	   {
	     "id": 912345678901,
	     "text": "How do I read JSON on Android?",
	     "geo": null,
	     "user": {
	       "name": "android_newb",
	       "followers_count": 41
	      }
	   },
	   {
	     "id": 912345678902,
	     "text": "@android_newb just use android.util.JsonReader!",
	     "geo": [50.454722, -104.606667],
	     "user": {
	       "name": "jesse",
	       "followers_count": 2
	     }
	   }
	 ]}
	 */
	
	public class User
	{
		private String	username;
		private int		followersCount;
		
		public User(String username, int followersCount)
		{
			this.username = username;
			this.followersCount = followersCount;
		}
	}
	
	public class Message
	{
		private long	id;
		private String	text;
		private User	user;
		private List	geo;
		
		public Message(long id, String text, User user, List geo)
		{
			this.id = id;
			this.text = text;
			this.user = user;
			this.geo = geo;
		}
	}
	
	public List<Message> readJsonStream(InputStream in) throws IOException
	{
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try
		{
			return readMessagesArray(reader);
		}
		finally
		{
			reader.close();
		}
	}
	
	public List<Message> readMessagesArray(JsonReader reader) throws IOException
	{
		List<Message> messages = new ArrayList<Message>();
		
		reader.beginArray();
		while (reader.hasNext())
			messages.add(readMessage(reader));
		reader.endArray();
		return messages;
	}
	
	public Message readMessage(JsonReader reader) throws IOException
	{
		long id = -1;
		String text = null;
		User user = null;
		List<Double> geo = null;
		
		reader.beginObject();
		while (reader.hasNext())
		{
			String name = reader.nextName();
			if (name.equals("id"))
				id = reader.nextLong();
			else if (name.equals("text"))
				text = reader.nextString();
			else if (name.equals("geo") && reader.peek() != JsonToken.NULL)
				geo = readDoublesArray(reader);
			else if (name.equals("user"))
				user = readUser(reader);
			else
				reader.skipValue();
		}
		reader.endObject();
		return new Message(id, text, user, geo);
	}
	
	public List<Double> readDoublesArray(JsonReader reader) throws IOException
	{
		List<Double> doubles = new ArrayList<Double>();
		
		reader.beginArray();
		while (reader.hasNext())
			doubles.add(reader.nextDouble());
		reader.endArray();
		return doubles;
	}
	
	public User readUser(JsonReader reader) throws IOException
	{
		String username = null;
		int followersCount = -1;
		
		reader.beginObject();
		while (reader.hasNext())
		{
			String name = reader.nextName();
			if (name.equals("name"))
				username = reader.nextString();
			else if (name.equals("followers_count"))
				followersCount = reader.nextInt();
			else
				reader.skipValue();
		}
		reader.endObject();
		return new User(username, followersCount);
	}
}
