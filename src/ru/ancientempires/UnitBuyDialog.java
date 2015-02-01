package ru.ancientempires;

import ru.ancientempires.model.UnitType;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class UnitBuyDialog extends DialogFragment
{
	
	private NoticeUnitBuy	listener;
	
	private String[]		buyStrings;
	private UnitType[]		buyTypes;
	private boolean[]		isAvailable;
	
	public UnitBuyDialog(UnitType[] types, boolean[] isAvailable)
	{
		this.buyTypes = types;
		this.isAvailable = isAvailable;
		this.buyStrings = new String[types.length];
		for (int i = 0; i < types.length; i++)
			this.buyStrings[i] = types[i].name;
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
							UnitBuyDialog.this.listener.onUnitBuy(UnitBuyDialog.this.buyTypes[which]);
						}
						else
							Toast.makeText(getActivity(), "Not enough money", Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{}
				});
		return builder.create();
	}
	
}
