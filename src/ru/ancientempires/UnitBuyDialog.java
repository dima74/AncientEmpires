package ru.ancientempires;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.draws.inputs.InputPlayer;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Unit;

public class UnitBuyDialog
{
	
	public AlertDialog dialog;
	
	public void showDialog(final InputPlayer input, Unit[] units, final boolean[] available)
	{
		View scrollView = GameActivity.activity.getLayoutInflater().inflate(R.layout.unit_buy_linear_layout, null);
		LinearLayout layout = (LinearLayout) scrollView.findViewById(R.id.linear_layout);
		for (int i = 0; i < units.length; i++)
		{
			View view = getView(units[i], i, available[i]);
			layout.addView(view);
			if (available[i])
				view.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						GameActivity.activity.dialog = null;
						input.onUnitBuy((int) v.getTag());
						dialog.hide();
					}
				});
		}
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.activity);
		builder.setView(scrollView);
		builder.setTitle(GameActivity.activity.getString(R.string.buy));
		
		GameActivity.activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				dialog = builder.show();
				GameActivity.activity.dialog = dialog;
			}
		});
	}
	
	private static final int	BLACK	= Color.BLACK;
	private static final int	GREY	= 0xFFAAAAAA;
										
	private View getView(Unit unit, int i, boolean available)
	{
		View view = GameActivity.activity.getLayoutInflater().inflate(R.layout.unit_buy_list_item, null);
		view.setTag(i);
		
		TextView textUnitName = (TextView) view.findViewById(R.id.textUnitName);
		TextView textUnitCost = (TextView) view.findViewById(R.id.textUnitCost);
		unit.updateName();
		textUnitName.setText(unit.name);
		textUnitCost.setText("" + unit.getCost());
		
		int color = available ? UnitBuyDialog.BLACK : UnitBuyDialog.GREY;
		textUnitName.setTextColor(color);
		textUnitCost.setTextColor(color);
		
		Bitmap bitmap = UnitImages.get().getUnitBitmapBuy(unit);
		((ImageView) view.findViewById(R.id.imageUnit)).setImageBitmap(bitmap);
		
		return view;
	}
	
}
