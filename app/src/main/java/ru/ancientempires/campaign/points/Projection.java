package ru.ancientempires.campaign.points;

public enum Projection
{
	LEFT_IN(0, 0, 1, 0, 0, 0),
	RIGHT_IN(0, 1, 1, 0, 0, -1),
	UP_IN(0, 0, 0, 1, 0, 0),
	DOWN_IN(1, 0, 0, 1, -1, 0),

	LEFT_OUT(0, 0, 1, 0, 0, -1),
	RIGHT_OUT(0, 1, 1, 0, 0, 0),
	UP_OUT(0, 0, 0, 1, -1, 0),
	DOWN_OUT(1, 0, 0, 1, 0, 0);

	public int multiH;
	public int multiW;
	public int multiI;
	public int multiJ;
	public int offsetI;
	public int offsetJ;

	Projection(int multiH, int multiW, int multiI, int multiJ, int offsetI, int offsetJ)
	{
		this.multiH = multiH;
		this.multiW = multiW;
		this.multiI = multiI;
		this.multiJ = multiJ;
		this.offsetI = offsetI;
		this.offsetJ = offsetJ;
	}

}
