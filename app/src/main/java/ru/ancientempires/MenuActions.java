package ru.ancientempires;

public enum MenuActions
{
	
	// MainActivity
	PLAY,
	ONLINE,
	SETTINGS,
	MAP_EDITOR,
	INSTRUCTIONS,
	AUTHORS,
	
	// PlayMenuActivity
	CAMPAIGN,
	SKIRMISH,
	USER_MAPS,
	LOAD,
	
	// MapEditorActivity
	CREATE_GAME,
	EDIT_GAME;
	
	@Override
	public String toString()
	{
		return Localization.get(name());
	}
	
	public static String[] convertToNames(MenuActions[] actions)
	{
		String[] names = new String[actions.length];
		for (int i = 0; i < actions.length; i++)
			names[i] = actions[i].toString();
		return names;
	}
	
}
