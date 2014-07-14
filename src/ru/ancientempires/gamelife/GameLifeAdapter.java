package ru.ancientempires.gamelife;

import ru.ancientempires.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class GameLifeAdapter extends BaseAdapter
{
	
	private GameLifeModel	gameModel;
	private Context			context;
	
	public GameLifeAdapter(Context context, int rowsAmount, int columnsAmount, int cellsAmount)
	{
		this.context = context;
		this.gameModel = new GameLifeModel(rowsAmount, columnsAmount, cellsAmount);
	}
	
	public void nextTurn()
	{
		this.gameModel.nextTurn();
	}
	
	@Override
	public int getCount()
	{
		return this.gameModel.getCount();
	}
	
	@Override
	public Object getItem(int position)
	{
		return this.gameModel.isLive(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		
		if (convertView == null)
		{
			imageView = new ImageView(this.context);
			
			imageView.setLayoutParams(new AbsListView.LayoutParams(15, 15));
			imageView.setAdjustViewBounds(false);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setPadding(1, 1, 1, 1);
		}
		else
		{
			imageView = (ImageView) convertView;
		}
		
		imageView.setImageResource(this.gameModel.isLive(position) ? R.drawable.cell : R.drawable.empty);
		return imageView;
	}
	
}
