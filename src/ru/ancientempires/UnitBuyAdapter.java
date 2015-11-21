package ru.ancientempires;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Unit;

public class UnitBuyAdapter extends BaseAdapter
{
	
	private static final int	BLACK	= Color.BLACK;
	private static final int	GREY	= 0xFFAAAAAA;
	
	private Unit[]		unitsOld;
	private Unit[]		units	= new Unit[0];
	private boolean[]	available;
	
	private View[]	viewsOld;
	private View[]	views;
	
	public UnitBuyAdapter start(Unit[] units, boolean[] available)
	{
		unitsOld = this.units;
		this.units = units;
		viewsOld = views;
		views = new View[units.length];
		this.available = available;
		return this;
	}
	
	@Override
	public int getCount()
	{
		return units.length;
	}
	
	@Override
	public Object getItem(int position)
	{
		return units[position];
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (views[position] == null)
			views[position] = getView(units[position]);
			
		int color = available[position] ? UnitBuyAdapter.BLACK : UnitBuyAdapter.GREY;
		((TextView) views[position].findViewById(R.id.textUnitName)).setTextColor(color);
		((TextView) views[position].findViewById(R.id.textUnitCost)).setTextColor(color);
		
		return views[position];
	}
	
	private View getView(Unit unit)
	{
		for (int i = 0; i < unitsOld.length; i++)
			if (unitsOld[i] == unit)
				return viewsOld[i];
				
		View view = GameActivity.gameActivity.getLayoutInflater().inflate(R.layout.listitem, null);
		
		TextView textView = (TextView) view.findViewById(R.id.textUnitName);
		textView.setText(unit.type.name);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageUnit);
		Bitmap bitmap = UnitImages.getUnitBitmapBuy(unit);
		imageView.setImageBitmap(bitmap);
		
		TextView textViewUnitCost = (TextView) view.findViewById(R.id.textUnitCost);
		textViewUnitCost.setText(String.valueOf(unit.cost));
		
		return view;
	}
	
}
