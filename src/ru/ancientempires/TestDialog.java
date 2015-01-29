package ru.ancientempires;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class TestDialog extends DialogFragment
{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		String[] strings = new String[]
		{
				"1",
				"2",
				"3",
				"4",
				"5",
				"6",
				"7",
				"8",
				"9",
				"jagcsykhdsncgsdudfdguvnfdsangcfsynmdcsngyuncbh"
		};
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.action_buy)
				.setItems(strings, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{	
						
					}
				})
				/*
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int id)
							{
								// FIRE ZE MISSILES!
							}
						})
						*/
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						// User cancelled the dialog
					}
				});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
