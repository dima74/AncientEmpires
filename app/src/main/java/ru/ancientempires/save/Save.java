package ru.ancientempires.save;

public interface Save
{
	// 0 -- обычное действие
	// 1 -- последнее перед тем как ИИ будет загружать игру
	// 2 -- самое последнее
	int save() throws Exception;
}
