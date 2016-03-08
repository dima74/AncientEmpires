package ru.ancientempires;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ru.ancientempires.action.BuyStatus;
import ru.ancientempires.action.result.ActionResultGetCellBuy;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.draws.inputs.InputPlayer;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Unit;

public class UnitBuyDialog
{
	
	public AlertDialog dialog;
	
	public void showDialog(final InputPlayer input, ActionResultGetCellBuy result)
	{
		Unit[] units = result.units;
		BuyStatus[] statuses = result.statuses;
		
		View scrollView = GameActivity.activity.getLayoutInflater().inflate(R.layout.unit_buy_linear_layout, null);
		LinearLayout layout = (LinearLayout) scrollView.findViewById(R.id.linear_layout);
		for (int i = 0; i < units.length; i++)
		{
			View view = getView(units[i], statuses[i]);
			final int finalI = i;
			final BuyStatus status = statuses[i];
			layout.addView(view);
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (status == BuyStatus.SUCCESS)
					{
						GameActivity.activity.dialog = null;
						input.onUnitBuy(finalI);
						dialog.hide();
					}
					else
						Toast.makeText(GameActivity.activity, status.toString(), Toast.LENGTH_SHORT).show();
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
										
	private View getView(Unit unit, BuyStatus status)
	{
		View view = GameActivity.activity.getLayoutInflater().inflate(R.layout.unit_buy_list_item, null);
		
		TextView textUnitName = (TextView) view.findViewById(R.id.textUnitName);
		TextView textUnitCost = (TextView) view.findViewById(R.id.textUnitCost);
		unit.updateName();
		textUnitName.setText(unit.name);
		textUnitCost.setText("" + unit.getCost());
		
		int color = status == BuyStatus.SUCCESS ? UnitBuyDialog.BLACK : UnitBuyDialog.GREY;
		textUnitName.setTextColor(color);
		textUnitCost.setTextColor(color);
		
		Bitmap bitmap = UnitImages.get().getUnitBitmapBuy(unit);
		((ImageView) view.findViewById(R.id.imageUnit)).setImageBitmap(bitmap);
		
		return view;
	}
	
}
