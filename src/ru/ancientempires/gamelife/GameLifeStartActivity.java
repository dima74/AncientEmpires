package ru.ancientempires.gamelife;

import ru.ancientempires.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class GameLifeStartActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_life_start);
		
		if (savedInstanceState == null)
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends DialogFragment implements OnClickListener
	{
		
		public Button			runButton;
		public static final int	MIN_ROWS_AMOUNT		= 5;
		public static final int	MAX_ROWS_AMOUNT		= 25;
		public static final int	MIN_COLUMNS_AMOUNT	= 5;
		public static final int	MAX_COLUMNS_AMOUNT	= 35;
		
		public PlaceholderFragment()
		{
			
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_game_life_start, container,
					false);
			
			this.runButton = (Button) rootView.findViewById(R.id.RunButton);
			this.runButton.setOnClickListener(this);
			
			return rootView;
		}
		
		@Override
		public void onClick(View view)
		{
			EditText rowsEditText = (EditText) getView().findViewById(R.id.RowsEditor);
			EditText columnsEditText = (EditText) getView().findViewById(R.id.ColumnsEditor);
			EditText cellsEditText = (EditText) getView().findViewById(R.id.CellsEditor);
			
			int rowsAmount = Integer.valueOf(rowsEditText.getText().toString());
			int columnsAmount = Integer.valueOf(columnsEditText.getText().toString());
			int cellsAmount = Integer.valueOf(cellsEditText.getText().toString());
			
			if (!check(rowsAmount, PlaceholderFragment.MIN_ROWS_AMOUNT, PlaceholderFragment.MAX_ROWS_AMOUNT))
			{
				showDialog(R.string.alert_rows);
				return;
			}
			if (!check(columnsAmount, PlaceholderFragment.MIN_COLUMNS_AMOUNT, PlaceholderFragment.MAX_COLUMNS_AMOUNT))
			{
				showDialog(R.string.alert_columns);
				return;
			}
			if (!check(cellsAmount, 1, PlaceholderFragment.MAX_ROWS_AMOUNT * PlaceholderFragment.MAX_COLUMNS_AMOUNT))
			{
				showDialog(R.string.alert_cells);
				return;
			}
			
			Intent intent = new Intent();
			intent.setClass(getActivity(), GameLifeActivity.class);
			
			intent.putExtra(GameLifeActivity.PlaceholderFragment.EXT_ROWS, rowsAmount);
			intent.putExtra(GameLifeActivity.PlaceholderFragment.EXT_COLUMNS, columnsAmount);
			intent.putExtra(GameLifeActivity.PlaceholderFragment.EXT_CELLS, cellsAmount);
			
			startActivity(intent);
			// getActivity().finish();
			
			// this.button.setText(String.format("It works! %s", this.i++));
			// Log.i("info", "onClick");
		}
		
		private boolean check(int amount, int min, int max)
		{
			return amount >= min && amount <= max;
		}
		
		private void showDialog(int id)
		{
			showDialog(getText(id).toString());
		}
		
		private void showDialog(String text)
		{
			// DialogFragment.show() will take care of adding the fragment
			// in a transaction. We also want to remove any currently showing
			// dialog, so make our own transaction and take care of that here.
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			
			DialogFragment newFragment = MyDialogFragment.newInstance(text);
			
			// Show the dialog.
			newFragment.show(ft, "dialog");
		}
	}
	
	public static class MyDialogFragment extends DialogFragment
	{
		
		public static MyDialogFragment newInstance(String title)
		{
			MyDialogFragment frag = new MyDialogFragment();
			Bundle args = new Bundle();
			args.putString("text", title);
			frag.setArguments(args);
			return frag;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			String text = getArguments().getString("text");
			
			return new AlertDialog.Builder(getActivity())
					.setTitle("A Dialog of Awesome")
					.setMessage(text)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int whichButton)
								{
								}
							}
					)
					.create();
		}
	}
	
}
