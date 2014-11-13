package ru.ancientempires;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.client.Client;
import android.os.AsyncTask;

public abstract class AbstractActionAsyncTask extends AsyncTask<Action, Void, ActionResult>
{
	
	@Override
	protected ActionResult doInBackground(Action... params)
	{
		return Client.action(params[0]);
	}
	
}
