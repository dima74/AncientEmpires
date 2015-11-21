package ru.ancientempires;

import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.model.Unit;
import ru.ancientempires.view.inputs.InputPlayer;

public class UnitBuyDialog
{
	
	private static AlertDialog	dialog;
	private static InputPlayer	input;
	private static Unit[]		units;
	private static boolean[]	available;
	
	public static void showDialog(InputPlayer input, Unit[] units, boolean[] available)
	{
		UnitBuyDialog.input = input;
		UnitBuyDialog.units = units;
		UnitBuyDialog.available = available;
		if (UnitBuyDialog.dialog != null)
			GameActivity.gameActivity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					UnitBuyDialog.useExistingDialog();
				}
			});
		else
			UnitBuyDialog.createNewDialog();
	}
	
	private static void createNewDialog()
	{
		View view = GameActivity.gameActivity.getLayoutInflater().inflate(R.layout.list, null);
		ListView listView = (ListView) view.findViewById(R.id.list);
		
		UnitBuyAdapter myAdapter = new UnitBuyAdapter().start(UnitBuyDialog.units, UnitBuyDialog.available);
		listView.setAdapter(myAdapter);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id)
			{
				if (UnitBuyDialog.available[position])
				{
					UnitBuyDialog.dialog.hide();
					UnitBuyDialog.input.onUnitBuy(position);
				}
			}
		});
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.gameActivity);
		builder.setView(view);
		builder.setTitle("Купить");
		
		GameActivity.gameActivity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				UnitBuyDialog.dialog = builder.show();
			}
		});
	}
	
	private static void useExistingDialog()
	{
		ListView listView = (ListView) UnitBuyDialog.dialog.findViewById(R.id.list);
		UnitBuyAdapter myAdapter = (UnitBuyAdapter) listView.getAdapter();
		myAdapter.start(UnitBuyDialog.units, UnitBuyDialog.available);
		myAdapter.notifyDataSetChanged();
		listView.setSelection(0);
		UnitBuyDialog.dialog.show();
	}
	
}
