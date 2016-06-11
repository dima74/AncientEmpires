package ru.ancientempires.activity;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import ru.ancientempires.framework.MyAssert;

public class MyTextWatcher implements TextWatcher
{
	
	private int defaultValue;
	private int minValue;
	private int maxValue;

	public MyTextWatcher(int maxValue)
	{
		this(0, maxValue);
	}
	
	public MyTextWatcher(int minValue, int maxValue)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{}
	
	static int i = 0;
	
	@Override
	public void afterTextChanged(Editable s)
	{
		MyAssert.a(i++ < 100);
		int value = s.length() == 0 ? defaultValue : Integer.valueOf(s.toString());
		if (value > maxValue)
			s.replace(0, s.length(), "" + maxValue);
		else if (value < minValue)
			s.replace(0, s.length(), "" + minValue);
		{
			int valueNew = s.length() == 0 ? defaultValue : Integer.valueOf(s.toString());
			MyAssert.a(minValue <= valueNew && valueNew <= maxValue);
		}
	}
	
	public void addTo(Activity activity, int editText, int defaultValue)
	{
		addTo((EditText) activity.findViewById(editText), defaultValue);
	}
	
	public void addTo(EditText editText, int defaultValue)
	{
		this.defaultValue = defaultValue;
		editText.setHint("" + defaultValue);
		editText.addTextChangedListener(this);
	}
	
}