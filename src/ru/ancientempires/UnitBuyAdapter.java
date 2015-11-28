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
	
	private class ViewHolder
	{
		
		public TextView		textUnitName;
		public ImageView	imageView;
		public TextView		textUnitCost;
		
	}
	
	private static final int	BLACK	= Color.BLACK;
	private static final int	GREY	= 0xFFAAAAAA;
	
	private Unit[]		unitsOld;
	private Unit[]		units	= new Unit[0];
	private boolean[]	available;
	private boolean[]	handle;
	
	private View[]	viewsOld;
	private View[]	views;
	
	public UnitBuyAdapter start(Unit[] units, boolean[] available)
	{
		unitsOld = this.units;
		this.units = units;
		viewsOld = views;
		views = new View[units.length];
		this.available = available;
		handle = new boolean[units.length];
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
		if (handle[position])
			return views[position];
		handle[position] = true;
		if (views[position] == null)
			views[position] = getView(units[position]);
			
		int color = available[position] ? UnitBuyAdapter.BLACK : UnitBuyAdapter.GREY;
		ViewHolder holder = (ViewHolder) views[position].getTag();
		holder.textUnitName.setTextColor(color);
		holder.textUnitCost.setTextColor(color);
		
		return views[position];
	}
	
	private View getView(Unit unit)
	{
		for (int i = 0; i < unitsOld.length; i++)
			if (unitsOld[i] == unit)
				return viewsOld[i];
				
		View view = GameActivity.gameActivity.getLayoutInflater().inflate(R.layout.unit_buy_list_item, null);
		ViewHolder holder = new ViewHolder();
		view.setTag(holder);
		
		holder.textUnitName = (TextView) view.findViewById(R.id.textUnitName);
		holder.textUnitName.setText(unit.type.name);
		
		holder.imageView = (ImageView) view.findViewById(R.id.imageUnit);
		Bitmap bitmap = UnitImages.getUnitBitmapBuy(unit);
		holder.imageView.setImageBitmap(bitmap);
		
		holder.textUnitCost = (TextView) view.findViewById(R.id.textUnitCost);
		holder.textUnitCost.setText(String.valueOf(unit.cost));
		
		return view;
	}
	
}
