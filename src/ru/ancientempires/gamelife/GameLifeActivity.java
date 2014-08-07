package ru.ancientempires.gamelife;

import java.util.Timer;
import java.util.TimerTask;

import ru.ancientempires.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class GameLifeActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_life);
		
		if (savedInstanceState == null) getFragmentManager().beginTransaction()
				.add(R.id.container, new PlaceholderFragment())
				.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_life, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_back)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment
	{
		
		public static final String	EXT_ROWS			= "rows";
		public static final String	EXT_COLUMNS			= "columns";
		public static final String	EXT_CELLS			= "cell1s";
		private GridView			gameGridView;
		private GameLifeAdapter		gameAdapter;
		private Timer				timer;
		
		public Handler				updateGridHandler	= new Handler()
														{
															@Override
															public void handleMessage(Message message)
															{
																PlaceholderFragment.this.gameAdapter.nextTurn();
																PlaceholderFragment.this.gameGridView.setAdapter(PlaceholderFragment.this.gameAdapter);
																
																super.handleMessage(message);
															}
														};
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_game_life, container, false);
			
			Bundle extras = getActivity().getIntent().getExtras();
			
			int rowsAmount = extras.getInt(EXT_ROWS);
			int columnsAmount = extras.getInt(EXT_COLUMNS);
			int cellsAmount = extras.getInt(EXT_CELLS);
			
			this.gameAdapter = new GameLifeAdapter(getActivity(), rowsAmount, columnsAmount, cellsAmount);
			this.gameGridView = (GridView) rootView.findViewById(R.id.GameGrid);
			this.gameGridView.setAdapter(this.gameAdapter);
			this.gameGridView.setNumColumns(columnsAmount);
			this.gameGridView.setEnabled(false);
			this.gameGridView.setStretchMode(0);
			
			this.timer = new Timer();
			this.timer.scheduleAtFixedRate(new SendMessageTask(), 1000, 1000);
			
			return rootView;
		}
		
		public class SendMessageTask extends TimerTask
		{
			
			@Override
			public void run()
			{
				Message message = new Message();
				
				PlaceholderFragment.this.updateGridHandler.sendMessage(message);
			}
		}
		
	}
	
}
