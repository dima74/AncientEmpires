package ru.ancientempires;

import ru.ancientempires.model.CellType;
import ru.ancientempires.model.UnitType;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity implements NoticeUnitBuy
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog();
			}
		});
	}
	
	protected void dialog()
	{
		DialogFragment dialog = new UnitBuyDialog(CellType.getType("CASTLE").buyUnits);
		dialog.show(getFragmentManager(), "dialog");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
			return true;
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onUnitBuy(UnitType type)
	{
		TextView textView = (TextView) findViewById(R.id.text);
		textView.setText("Select " + type.name);
	}
	
}
