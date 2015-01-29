package ru.ancientempires;

import ru.ancientempires.model.UnitType;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class UnitBuyDialog extends DialogFragment
{
	
	private NoticeUnitBuy	listener;
	
	private String[]		buyStrings;
	private UnitType[]		buyTypes;
	
	public UnitBuyDialog(UnitType[] types)
	{
		this.buyTypes = types;
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
				.setItems(this.buyStrings, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						UnitBuyDialog.this.listener.onUnitBuy(UnitBuyDialog.this.buyTypes[which]);
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
