package ru.ancientempires.bridge;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ru.ancientempires.MyColor;
import ru.ancientempires.PlayerType;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Team;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;

public class GameConverter
{
	
	public String[]	cellStrings;
	private Rules	rules;
					
	public GameConverter(Rules rules)
	{
		this.rules = rules;
		initCells();
	}
	
	public void initCells()
	{
		cellStrings = new String[48];
		cellStrings[0] = "WATER";
		cellStrings[1] = "WATER_SPARKS";
		cellStrings[3] = "WATER";
		cellStrings[4] = "WATER";
		cellStrings[5] = "WATER";
		cellStrings[6] = "WATER";
		cellStrings[7] = "WATER";
		cellStrings[8] = "WATER";
		cellStrings[9] = "WATER";
		cellStrings[10] = "WATER";
		cellStrings[11] = "WATER";
		cellStrings[12] = "WATER";
		cellStrings[13] = "WATER";
		cellStrings[14] = "WATER";
		cellStrings[15] = "THREE_TREES";
		cellStrings[16] = "TWO_TREES";
		cellStrings[17] = "MOUNT";
		cellStrings[18] = "PLAIN";
		cellStrings[19] = "HILL";
		cellStrings[20] = "WAY";
		cellStrings[21] = "WAY";
		cellStrings[22] = "WAY";
		cellStrings[23] = "WAY";
		cellStrings[24] = "WAY";
		cellStrings[25] = "WAY";
		cellStrings[26] = "WAY";
		cellStrings[27] = "BUILDING_DESTROYING";
		cellStrings[29] = "WATER_WAY_HORIZONTAL";
		cellStrings[30] = "WATER_WAY_VERTICAL";
		cellStrings[31] = "CAMP";
		cellStrings[32] = "TEMPLE";
		cellStrings[33] = "CITADEL_LEFT";
		cellStrings[34] = "CITADEL";
		cellStrings[35] = "CITADEL_RIGHT";
		cellStrings[36] = "CITADEL_UP";
		cellStrings[37] = "BUILDING";
		cellStrings[38] = "CASTLE";
		cellStrings[39] = "BUILDING";
		cellStrings[40] = "CASTLE";
		cellStrings[41] = "BUILDING";
		cellStrings[42] = "CASTLE";
		cellStrings[43] = "BUILDING";
		cellStrings[44] = "CASTLE";
		cellStrings[45] = "BUILDING";
		cellStrings[46] = "CASTLE";
	}
	
	private static boolean imagesEquals(BufferedImage image1, BufferedImage image2)
	{
		if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight())
			return false;
			
		for (int x = 0; x < image2.getWidth(); x++)
			for (int y = 0; y < image2.getHeight(); y++)
				if (image1.getRGB(x, y) != image2.getRGB(x, y))
					return false;
		return true;
	}
	
	public Game convertGame(InputStream in, int iMission, boolean isCampaign) throws IOException
	{
		int[][] money = new int[8][2];
		money[0][0] = 0;
		money[0][1] = 0;
		money[1][0] = 300;
		money[1][1] = 50;
		money[2][0] = 0;
		money[2][1] = 0;
		money[3][0] = 400;
		money[3][1] = 400;
		money[4][0] = 0;
		money[4][1] = 0;
		money[5][0] = 600;
		money[5][1] = 600;
		money[6][0] = 400;
		money[6][1] = 600;
		money[7][0] = 800;
		money[7][1] = 200;
		
		DataInputStream dis = new DataInputStream(in);
		Game game = new Game(rules);
		
		int w = dis.readInt();
		int h = dis.readInt();
		game.fieldCells = new Cell[h][w];
		byte[][] fieldBytes = new byte[h][w];
		for (int j = 0; j < w; j++)
			for (int i = 0; i < h; i++)
				fieldBytes[i][j] = dis.readByte();
		dis.skip(dis.readInt() * 4);
		int unitsAmount = dis.readInt()
				+ (isCampaign && iMission == 3 ? 3 : 0)
				+ (isCampaign && iMission == 6 ? 4 : 0);
		String[] unitTypes = new String[unitsAmount];
		int[] unitPlayerBaseIs = new int[unitsAmount];
		int[] unitIs = new int[unitsAmount];
		int[] unitJs = new int[unitsAmount];
		for (int i = 0; i < unitsAmount; i++)
			if (isCampaign && iMission == 6 && i > 0)
			{
				unitJs[i] = -i;
				unitIs[i] = 13;
				unitTypes[i] = GameConverter.getUnitTypeName(new int[]
				{
					0,
					1,
					2,
					4,
					11
				}[i], 0);
				unitPlayerBaseIs[i] = 0;
			}
			else if (isCampaign && iMission == 3 && i > 9)
			{
				unitJs[i] = 5;
				unitIs[i] = 9 - i;
				unitTypes[i] = GameConverter.getUnitTypeName(new int[]
				{
					0,
					2,
					3
				}[i - 10], 0);
				unitPlayerBaseIs[i] = 0;
			}
			else
			{
				byte unitType = dis.readByte();
				unitPlayerBaseIs[i] = unitType / 12;
				unitType %= 12;
				unitTypes[i] = GameConverter.getUnitTypeName(unitType, unitPlayerBaseIs[i]);
				unitIs[i] = dis.readShort() / 24;
				unitJs[i] = dis.readShort() / 24;
			}
			
		//
		Set<Integer> allPlayerIs = new HashSet<Integer>();
		for (int i = 0; i < unitsAmount; i++)
			allPlayerIs.add(unitPlayerBaseIs[i]);
		allPlayerIs.add(0);
		allPlayerIs.add(1);
		
		int[] basePlayerIToPlayerI = new int[]
		{
			-1,
			-1,
			-1,
			-1
		};
		Player[] players = new Player[allPlayerIs.size()];
		int playerOrdinalForArray = 0;
		for (Integer playerI : allPlayerIs)
		{
			Player player = new Player();
			player.color = playerI == 0 ? MyColor.BLUE : playerI == 1 ? MyColor.RED : playerI == 2 ? MyColor.GREEN : MyColor.BLACK;
			player.cursorI = 0;
			player.cursorJ = 0;
			player.gold = isCampaign ? money[iMission][playerOrdinalForArray] : 2000;
			player.ordinal = playerOrdinalForArray++;
			players[player.ordinal] = player;
			basePlayerIToPlayerI[playerI] = player.ordinal;
		}
		game.currentPlayer = players[0];
		
		game.fieldUnits = new Unit[h][w];
		game.fieldUnitsDead = new Unit[h][w];
		game.unitsOutside = new HashSet<Unit>();
		game.unitsStaticDead = new ArrayList[players.length];
		for (int i = 0; i < players.length; i++)
			game.unitsStaticDead[i] = new ArrayList<Unit>();
		for (int i = 0; i < unitsAmount; i++)
		{
			Unit unit = new Unit(game, rules.getUnitType(unitTypes[i]), players[basePlayerIToPlayerI[unitPlayerBaseIs[i]]]);
			unit.player.units.add(unit);
			unit.i = unitJs[i];
			unit.j = unitIs[i];
			if (isCampaign && iMission == 6)
				System.out.println(unit);
			if (isCampaign && iMission == 4)
				unit.i = unit.i == 2 ? -3 : unit.i == 1 ? -5 : -7;
			if (unit.type.isStatic)
			{
				unit.player.cursorI = unit.i;
				unit.player.cursorJ = unit.j;
				if (isCampaign && iMission == 4)
				{
					unit.player.cursorI = 1;
					unit.player.cursorJ = 11;
				}
			}
			if (unit.i >= 0 && unit.i < h && unit.j >= 0 && unit.j < w)
				game.fieldUnits[unit.i][unit.j] = unit;
			else
				game.unitsOutside.add(unit);
		}
		
		for (int j = 0; j < w; j++)
			for (int i = 0; i < h; i++)
			{
				int cellTypeI = fieldBytes[i][j];
				String cellName = cellStrings[cellTypeI];
				Cell cell = new Cell(game, rules.getCellType(cellName), i, j);
				if (cellTypeI >= 39 && cellTypeI <= 46)
				{
					int cellTypeBasePlayerI = (cellTypeI - 39) / 2;
					cell.player = players[basePlayerIToPlayerI[cellTypeBasePlayerI]];
				}
				game.fieldCells[i][j] = cell;
			}
			
		game.h = h;
		game.w = w;
		game.players = players;
		
		game.teams = new Team[game.players.length];
		for (int i = 0; i < game.players.length; i++)
			game.teams[i] = new Team(new Player[]
			{
				game.players[i]
			});
			
		if (isCampaign)
		{
			game.players[0].type = PlayerType.PLAYER;
			game.players[1].type = PlayerType.COMPUTER;
		}
		else
		{
			for (Player player : game.players)
				player.type = PlayerType.PLAYER;
			if (game.players.length == 2)
				game.players[1].type = PlayerType.COMPUTER;
		}
		game.unitsLimit = 20;
		game.allowedUnits = isCampaign ? new int[]
		{
			0, 2, 0, 8, 0, 8, 9, 9
		}[iMission] : -1;
		
		if (!isCampaign && iMission == 5 && false)
		{
			game.fieldCells[9][7] = new Cell(game, rules.getCellType("CASTLE"), 9, 7);
			game.fieldCells[9][7].player = game.players[0];
			
			game.fieldUnits[9][7] = game.fieldUnits[3][7];
			game.fieldUnits[3][7] = null;
			game.fieldUnits[9][7].i = 9;
			game.fieldUnits[9][7].j = 7;
			game.fieldUnits[9][7].health = 100;
			
			game.fieldUnits[10][7] = new Unit(game, rules.getUnitType("SOLDIER"), game.players[1]);
			game.fieldUnits[10][7].i = 10;
			game.fieldUnits[10][7].j = 7;
		}
		
		return game;
	}
	
	public static String getUnitTypeName(int unitType, int team)
	{
		String[] names = new String[12];
		names[0] = "SOLDIER";
		names[1] = "ARCHER";
		names[2] = "ELEMENTAL";
		names[3] = "SORCERESS";
		names[4] = "WISP";
		names[5] = "DIRE_WOLF";
		names[6] = "GOLEM";
		names[7] = "CATAPULT";
		names[8] = "DRAGON";
		// a[9] = "KING";
		names[10] = "SKELETON";
		names[11] = "CRYSTAL";
		
		if (unitType == 9)
		{
			String[] kings =
			{
				"KING_GALAMAR",
				"KING_DEMONLORD",
				"KING_VALADORN",
				"KING_SAETH"
			};
			return kings[team];
		}
		else
			return names[unitType];
	}
	
}
