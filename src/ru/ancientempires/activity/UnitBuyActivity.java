package ru.ancientempires.activity;

import ru.ancientempires.R;
import ru.ancientempires.graphics.RippleDrawable;
import ru.ancientempires.model.UnitType;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class UnitBuyActivity extends Activity
{
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a {@link FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory. If
	 * this becomes too memory intensive, it may be best to switch to a {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter	mSectionsPagerAdapter;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager				mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unit_buy);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		this.mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		this.mViewPager = (ViewPager) findViewById(R.id.pager);
		this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
		this.mViewPager.setCurrentItem(1);
		
		this.mSectionsPagerAdapter.setPager(this.mViewPager);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.unit_buy, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
			return true;
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{
		
		public ViewPager	pager;
		
		public void setPager(ViewPager pager)
		{
			this.pager = pager;
			pager.setOnPageChangeListener(new OnPageChangeListener()
			{
				@Override
				public void onPageSelected(int position)
				{}
				
				@Override
				public void onPageScrolled(int position, float positionOffset, int pixels)
				{}
				
				@Override
				public void onPageScrollStateChanged(int state)
				{
					if (state == ViewPager.SCROLL_STATE_IDLE)
					{
						int position = SectionsPagerAdapter.this.pager.getCurrentItem();
						if (position > 0 && position < getCount() - 1)
							return;
						if (position == 0)
							position = getCount() - 2;
						else if (position == getCount() - 1)
							position = 1;
						SectionsPagerAdapter.this.pager.setCurrentItem(position, false);
					}
				}
			});
		}
		
		public SectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position)
		{
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			if (position == 0)
				position = getCount() - 2;
			else if (position == getCount() - 1)
				position = 1;
			return PlaceholderFragment.newInstance(position - 1);
		}
		
		@Override
		public int getCount()
		{
			return UnitType.amount + 2;
		}
		
		@Override
		public CharSequence getPageTitle(int position)
		{
			return getString(R.string.title_activity_unit_buy);
		}
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment
	{
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		private static final String	ARG_SECTION_NUMBER	= "section_number";
		
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber)
		{
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		public PlaceholderFragment()
		{}
		
		private UnitType	unitType;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			int sectionNumber = getArguments().getInt(PlaceholderFragment.ARG_SECTION_NUMBER);
			this.unitType = UnitType.getType(sectionNumber);
			
			View rootView = inflater.inflate(R.layout.fragment_unit_buy, container, false);
			PlaceholderFragment.setText(rootView, R.id.textViewName, this.unitType.name);
			PlaceholderFragment.setText(rootView, R.id.textViewCost, this.unitType.cost);
			PlaceholderFragment.setText(rootView, R.id.textViewAttack,
					this.unitType.baseAttackMin + "-" + this.unitType.baseAttackMax);
			PlaceholderFragment.setText(rootView, R.id.textViewDefence, this.unitType.baseDefence);
			PlaceholderFragment.setText(rootView, R.id.textViewWay, this.unitType.baseMaxWay);
			
			Button buttonCampaign = (Button) rootView.findViewById(R.id.button_buy);
			RippleDrawable.createRipple(buttonCampaign, 0xff00ffff);
			buttonCampaign.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					performAction();
				}
			});
			
			return rootView;
		}
		
		private static void setText(View rootView, int id, Object text)
		{
			((TextView) rootView.findViewById(id)).setText(text.toString());
		}
		
		protected void performAction()
		{
			GameActivity.gameView.performActionBuy(this.unitType);
			getActivity().finish();
		}
	}
	
}
