package ru.ancientempires;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import ru.ancientempires.model.Unit;

public class UnitBuyDialog extends DialogFragment
{
	
	private NoticeUnitBuy listener;
	
	private String[]	buyStrings;
	private Unit[]		buyUnits;
	private boolean[]	isAvailable;
	
	public UnitBuyDialog(Unit[] units, boolean[] isAvailable)
	{
		this.buyUnits = units;
		this.isAvailable = isAvailable;
		this.buyStrings = new String[units.length];
		for (int i = 0; i < units.length; i++)
			this.buyStrings[i] = this.buyUnits[i].type.name;
	}
	
	public UnitBuyDialog setNoticeListener(NoticeUnitBuy listener)
	{
		this.listener = listener;
		return this;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.action_buy)
				.setSingleChoiceItems(this.buyStrings, -1, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						if (UnitBuyDialog.this.isAvailable[which])
						{
							dismiss();
							UnitBuyDialog.this.listener.onUnitBuy(which);
						}
						else
							Toast.makeText(getActivity(), "Not enough money", Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton(android.R.string.cancel, null);
		return builder.create();
	}
	
}
