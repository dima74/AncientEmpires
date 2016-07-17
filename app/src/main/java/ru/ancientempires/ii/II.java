package ru.ancientempires.ii;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import ru.ancientempires.actions.Action;
import ru.ancientempires.actions.ActionCellBuy;
import ru.ancientempires.actions.ActionGameEndTurn;
import ru.ancientempires.actions.ActionGetCellBuy;
import ru.ancientempires.actions.ActionGetRandomNumber;
import ru.ancientempires.actions.ActionUnitAttack;
import ru.ancientempires.actions.ActionUnitCapture;
import ru.ancientempires.actions.ActionUnitMove;
import ru.ancientempires.actions.ActionUnitRaise;
import ru.ancientempires.actions.ActionUnitRepair;
import ru.ancientempires.actions.BuyStatus;
import ru.ancientempires.actions.Checker;
import ru.ancientempires.actions.result.ActionResult;
import ru.ancientempires.actions.result.ActionResultGetCellBuy;
import ru.ancientempires.actions.result.ActionResultGetRandomNumber;
import ru.ancientempires.bonuses.BonusCreatorWisp;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.ActionHelper;
import ru.ancientempires.helpers.UnitHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.GameHandler;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Range;
import ru.ancientempires.model.Team;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.rules.Rules;

public class II extends GameHandler
{
	
	public Rules rules;
	ArrayList<Action> actions;
	
	public II(Rules rules)
	{
		this.rules = rules;
	}
	
	public ActionResult perform(Action action)
	{
		/*
		action.game = game;
		System.out.println(game.fieldUnits[5][7] == null ? "" : game.fieldUnits[5][7].isMove + " " + action);
		action.game = null;
		// */
		
		ActionResult result = action.perform(game);
		action.game = null;
		actions.add(action);
		return result;
	}
	
	public void turnFull(Game mainGame) throws Exception
	{
		ArrayList<Action> actions = turn(mainGame);
		for (Action action : actions)
			action.perform(mainGame);
	}
	
	public ArrayList<Action> turn(Game mainGame)
	{
		actions = new ArrayList<>();
		try
		{
			//mainGame.saver.waitSave();
			setGame(mainGame.myClone());
			MyAssert.a(game, mainGame);

			/*
			System.out.println();
			System.out.println();
			System.out.println(game.get());
			*/
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		
		init();
		initTurn();
		
		ArrayList<Unit> allUnits = new ArrayList<Unit>(var_3aad);
		for (Unit unit : allUnits)
			MyAssert.a(!unit.isMove && !unit.isTurn);
		while (!var_3aad.isEmpty())
		{
			Unit var1 = var_381f;
			if (var_381f == null)
				var1 = var_3aad.elementAt(0);
			
			int i = allUnits.indexOf(var1);
			int j = allUnits.lastIndexOf(var1);
			MyAssert.a(i == j);
			// System.out.println(allUnits.get(i).equals(allUnits.get(j)));
			// MyLog.l(var1, this.var_381f);
			
			this.turn(var1);
		}
		
		while (true)
		{
			Unit var1 = null;
			int var2 = 0;
			int var3 = 6666;
			int var4 = 0;
			int var5 = 0;
			
			int var6;
			int var8;
			
			for (var6 = 0; var6 < buildings.length; ++var6)
			{
				// byte var7 = this.buildingData[var6][0];
				// var8 = this.buildingData[var6][1];
				Cell cell = buildings[var6];
				int var7 = cell.j;
				var8 = cell.i;
				// if (getTileType(var7, var8) == 9 && isBuildingOfFraction(var7, var8, this.currentTurningPlayer))
				if (cell.type.name == "CASTLE" && cell.player == game.currentPlayer)
				{
					if (var2 == 0)
					{
						var4 = var7;
						var5 = var8;
					}
					
					++var2;
					int var9 = 0;
					
					int var10;
					
					Vector<Unit> units = new Vector<Unit>();
					for (Unit[] line : fieldUnits)
						for (Unit unit : line)
							if (unit != null)
								units.addElement(unit);
					
					for (var10 = units.size() - 1; var10 >= 0; --var10)
					{
						Unit var11 = units.elementAt(var10);
						if (var11.player.team != game.currentPlayer.team && !var11.type.hasTombstone)
						{
							int var12;
							if ((var12 = Math.abs(var11.j - var7) + Math.abs(var11.i - var8)) < var3)
							{
								var3 = var12;
								var4 = var7;
								var5 = var8;
							}
							
							++var9;
						}
					}
					
					if (var9 == 0)
						for (var10 = 0; var10 < buildings.length; ++var10)
						{
							// byte var20 = this.buildingData[var10][0];
							// byte var17 = this.buildingData[var10][1];
							int var20 = cell.j;
							int var17 = cell.i;
							int var13;
							// if (getTileType(var20, var17) == 9 && !isBuildingOfFraction(var20, var17, this.currentTurningPlayer) && (var13 = Math.abs(var20 - var7) + Math.abs(var17 - var8))
							// < var3)
							if (cell.type.name == "CASTLE" && cell.player != game.currentPlayer && (var13 = Math.abs(var20 - var7) + Math.abs(var17 - var8)) < var3)
							{
								var3 = var13;
								var4 = var7;
								var5 = var8;
							}
						}
				}
			}
			
			if (var2 > 0)
			{
				ActionResultGetCellBuy result = (ActionResultGetCellBuy) new ActionGetCellBuy()
						.setIJ(var5, var4)
						.perform(game);
				Unit[] units = result.units;
				BuyStatus[] statuses = result.statuses;
				Unit kingToBuy = null;
				Unit soldierToBuy = null;
				Unit archerToBuy = null;
				int iKing = -1;
				int iSoldier = -1;
				int iArcher = -1;
				for (int i = 0; i < units.length; i++)
					if (statuses[i] == BuyStatus.SUCCESS)
					{
						Unit unit = units[i];
						if (unit.type.isStatic)
						{
							kingToBuy = unit;
							iKing = i;
						}
						if (unit.type.name == "SOLDIER")
						{
							soldierToBuy = unit;
							iSoldier = i;
						}
						if (unit.type.name == "ARCHER")
						{
							archerToBuy = unit;
							iArcher = i;
						}
					}
				
				if (kingToBuy != null && canBuyUnit(kingToBuy, var4, var5))
				{
					// for (var6 = 0; var6 < fractionsKingCount[this.currentTurningPlayer]; ++var6)
					// if (fractionsAllKings[this.currentTurningPlayer][var6] != null && fractionsAllKings[this.currentTurningPlayer][var6].unitState == 3
					// && canBuyUnit(fractionsAllKings[this.currentTurningPlayer][var6], var4, var5))
					// var1 = buyUnit(fractionsAllKings[this.currentTurningPlayer][var6], var4, var5);
					// Если можем купит короля -> покупаем
					perform(new ActionCellBuy()
							.setUnit(iKing)
							.setIJ(var5, var4));
					var1 = fieldUnits[var5][var4];
				}
				
				if (var1 == null)
					// if (countUnits(0, -1, this.currentTurningPlayer) < 2 && canBuyUnit((byte) 0, var4, var5))
					// var1 = buyUnit((byte) 0, var4, var5);
					// else if (countUnits(1, -1, this.currentTurningPlayer) < 2 && canBuyUnit((byte) 1, var4, var5))
					// var1 = buyUnit((byte) 1, var4, var5);
					if (soldierToBuy != null && countUnits(soldierToBuy.type) < 2 && canBuyUnit(soldierToBuy, var4, var5))
					{
						// мечников меньше 2, покупаем в {var4, var5}
						perform(new ActionCellBuy()
								.setUnit(iSoldier)
								.setIJ(var5, var4));
						var1 = fieldUnits[var5][var4];
					}
					else if (archerToBuy != null && countUnits(rules.getUnitType("ARCHER")) < 2 && canBuyUnit(archerToBuy, var4, var5))
					{
						// лучников меньше 2, покупаем в {var4, var5}
						perform(new ActionCellBuy()
								.setUnit(iArcher)
								.setIJ(var5, var4));
						var1 = fieldUnits[var5][var4];
					}
					else
					{
						var6 = 0; // количество юнитов нашей команды (не цвета!)
						int var21 = 0;// количество юнитов не нашей команды
						
						for (Player player : game.players)
							if (player.team == game.currentPlayer.team)
								var6 += player.units.size();
							else
								var21 += player.units.size();
								
						/*
						for (var8 = 0; var8 < turnQueueLength; ++var8)
							if (this.playerTeams[var8] == this.playerTeams[this.currentTurningPlayer])
								var6 += countUnits(-1, -1, var8);
							else
								var21 += countUnits(-1, -1, var8);
						//*/
						
						if (game.currentPlayer.gold >= 1000 || game.currentPlayer.units.size() < 8 || var6 < var21)
						{
							/*
							int var14 = 0;
							byte[] var15 = new byte[12];
							
							byte var19;
							for (var19 = 1; var19 < 12; ++var19)
								if (countUnits(var19, -1, this.currentTurningPlayer) < 1 || UNIT_COST[var19] >= 600 && canBuyUnit(var19, var4, var5))
								{
									var15[var14] = var19;
									++var14;
								}
							
							if (var14 > 0)
							{
								var19 = var15[Math.abs(new Random().nextInt()) % var14];
								var1 = null;// buyUnit((byte) var19, var4, var5);
							}
							//*/
							
							ArrayList<Integer> unitsToBuy = new ArrayList<Integer>();
							for (int i = 0; i < units.length; i++)
								if (statuses[i] == BuyStatus.SUCCESS)
								{
									Unit unit = units[i];
									if ((countUnits(unit.type) < 1 || unit.getCost() >= 600) && canBuyUnit(unit, var4, var5))
										unitsToBuy.add(i);
								}
							if (!unitsToBuy.isEmpty())
							{
								ActionResultGetRandomNumber resultRandom = (ActionResultGetRandomNumber) perform(new ActionGetRandomNumber().setBound(unitsToBuy.size()));
								// System.out.println(resultRandom.number);
								perform(new ActionCellBuy()
										.setUnit(unitsToBuy.get(resultRandom.number))
										.setIJ(var5, var4));
								var1 = fieldUnits[var5][var4];
							}
						}
					}
			}
			
			if (var1 == null)
				break;
			this.turn(var1);
		}
		
		var_38d5 = null;
		var_3aad = null;
		
		perform(new ActionGameEndTurn());
		MyAssert.a(!actions.isEmpty());
		return actions;
	}
	
	public void turn(Unit var1)
	{
		sub_109f(var1);
		// MyLog.l(this.var_381f);
		if (var_381f == null)
			var_3aad.removeElement(var1);
		else
			return;
		
		if (var1.isTurn)
			return;
		perform(new ActionUnitMove()
				.setIJ(currentSelectedUnit.i, currentSelectedUnit.j)
				.setTargetIJ(var_3781, var_3733));
		
		if (var_37c2 == null && var_37e4 == null)
		{
			int var18;
			
			// if (sub_d5e(this.currentSelectedcurrentMapPosX, this.currentSelectedcurrentMapPosY, this.currentSelectedUnit))
			
			Cell cell = fieldCells[currentSelectedUnit.i][currentSelectedUnit.j];
			if (currentSelectedUnit.canCapture(cell.type) && cell.getTeam() != var1.player.team)
			// может захватить
			{
				var18 = sub_110d(currentSelectedUnit.j, currentSelectedUnit.i);
				
				if (var_3a5c != -1 && var_3a5c != var18)
				{
					var_3903[var_3a5c] = var_3903[var18];
					var_3903[var18] = currentSelectedUnit;
				}
				
				perform(new ActionUnitCapture()
						.setIJ(currentSelectedUnit.i, currentSelectedUnit.j));
				// setBuildingFraction(this.currentSelectedcurrentMapPosX, this.currentSelectedcurrentMapPosY, fractionsTurnQueue[this.currentSelectedfractionPosInTurnQueue]);
				// PaintableObject.currentRenderer.setCurrentDisplayable(createMessageScreen((String) null, PaintableObject.getLocaleString(73), height_, 1000));
				// Renderer.startPlayer(9, 1);
				// gameState = 9;
				// var_12ff = delayCounter;
			}
			// else if (sub_d41(this.currentSelectedcurrentMapPosX, this.currentSelectedcurrentMapPosY, this.currentSelectedUnit))
			else if (currentSelectedUnit.canRepair(cell.type))
			// может чинить
			{
				var18 = sub_110d(currentSelectedUnit.j, currentSelectedUnit.i);
				
				if (var_3a5c != -1 && var_3a5c != var18)
				{
					var_3903[var_3a5c] = var_3903[var18];
					var_3903[var18] = currentSelectedUnit;
				}
				
				perform(new ActionUnitRepair()
						.setIJ(currentSelectedUnit.i, currentSelectedUnit.j));
				// setMapElement(FRACTION_BUILDINGS, this.currentSelectedcurrentMapPosX, this.currentSelectedcurrentMapPosY);
				// PaintableObject.currentRenderer.setCurrentDisplayable(createMessageScreen((String) null, PaintableObject.getLocaleString(74), height_, 1000));
				// Renderer.startPlayer(9, 1);
				// gameState = 0;
				// var_12ff = delayCounter;
			}
			// else
			// gameState = 0;
			
			// this.currentSelectedfinishMove();
			currentSelectedUnit = null;
			var_36e4 = 0;
		}
		else
		{
			var_36e4 = 5;
			// this.currentSelectedfillAttackRangeDataEx(this.mapAlphaData, this.currentSelectedcurrentMapPosX, this.currentSelectedcurrentMapPosY);
			// showAtackRange = true;
			// paintMapAlphaDataFlag = true;
			// var_38a4 = delayCounter;
			if (var_37c2 != null)
			{
				// sprCursor.setExternalFrameSequence(CURSOR_FRAME_SEQUENCES[1]);
				// moveCursor(this.var_37c2.currentMapPosX, this.var_37c2.currentMapPosY);
				perform(new ActionUnitAttack()
						.setIJ(currentSelectedUnit.i, currentSelectedUnit.j)
						.setTargetIJ(var_37c2.i, var_37c2.j));
				var_37c2 = null;
			}
			else if (var_37e4 != null)
			{
				// moveCursor(this.var_37e4.currentMapPosX, this.var_37e4.currentMapPosY);
				perform(new ActionUnitRaise()
						.setIJ(currentSelectedUnit.i, currentSelectedUnit.j)
						.setTargetIJ(var_37e4.i, var_37e4.j));
				var_37e4 = null;
				var_36e4 = 7;
			}
		}
	}
	
	private Cell[] buildings;
	
	public void init()
	{
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for (Cell[] line : fieldCells)
			for (Cell cell : line)
				if (cell.type.isCapturing || cell.type.repairType != null || cell.type.destroyingType != null)
					cells.add(cell);
		buildings = cells.toArray(new Cell[0]);
		mapAlphaData = new int[w][h];
		var_39e3 = new int[w][h];
	}
	
	// private byte[][] buildingData; // new byte[buildingCount][3] {x, y, номер игрока, -1 разрушенный, 0 ничейный, 1 синий, 2 красный, 3 зеленый, 4 черный}
	private int[][] mapAlphaData;        // new byte[mapWidth][mapHeight]
	
	private int var_3733;            // 0
	private int var_3781;            // 0
	
	private Unit var_37c2;            // null
	private Unit var_37e4;            // null
	private Unit var_381f;            // null
	
	public  Unit[]  var_38d5;
	private Unit[]  var_3903;            // new Unit[buildings.length];
	private byte[]  var_3947;            // new byte[buildings.length];
	private int[][] var_395f;            // local
	
	private int[]   var_399e;            // new int[buildingCount] каждый ход обнуляется
	public  int     var_39d0;            // local
	private int[][] var_39e3;            // new byte[mapWidth][mapHeight];
	
	private int var_36e4;            // 0-выбор юнита, 3-ход, 4-в конце хода что делать дальше, 5-атака
	
	private int var_3a15;
	private int var_3a34;
	public  int var_3a41;
	private int var_3a5c;
	
	private Unit         currentSelectedUnit;
	private Vector<Unit> var_3aad;
	
	private boolean[] isKingsLive;
	private boolean   isKingLive;
	
	public void initTurn()
	{
		for (Player player : game.players)
			for (Unit unit : player.units)
				unit.setGame(game);
		
		// мечники
		// виспы
		// лучники
		// остальные
		final int[] priority = new int[rules.numberUnitTypes()];
		priority[rules.getUnitType("SOLDIER").ordinal] = 3;
		priority[rules.getUnitType("WISP").ordinal] = 2;
		priority[rules.getUnitType("ARCHER").ordinal] = 1;
		
		var_3aad = new Vector<Unit>(game.currentPlayer.units);
		Collections.sort(var_3aad, new Comparator<Unit>()
		{
			public boolean less(Unit a, Unit b)
			{
				return priority[a.type.ordinal] > priority[b.type.ordinal] || priority[a.type.ordinal] == priority[b.type.ordinal] && a.health > b.health;
			}
			
			@Override
			public int compare(Unit a, Unit b)
			{
				return less(a, b) ? -1 : less(b, a) ? 1 : 0;
			}
			
		});
		
		isKingsLive = new boolean[numberPlayers];
		for (Player player : game.players)
		{
			Unit king = this.getKing(player);
			isKingsLive[player.ordinal] = king != null;
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					isKingsLive[player.ordinal] |= fieldCells[i][j].type.name == "CASTLE" && fieldCells[i][j].player == player;
		}
		isKingLive = isKingsLive[game.currentPlayer.ordinal];
		
		var_3903 = new Unit[buildings.length];
		var_3947 = new byte[buildings.length];
		var_399e = new int[buildings.length];
		var_36e4 = 0;
	}
	
	ArrayList<Unit> nextMove = new ArrayList<Unit>();
	
	public final void sub_109f(Unit var1)
	{
		currentSelectedUnit = var1;
		// currentSelectedUnit.fillMoveRangeData(this.mapAlphaData);
		fillMoveRangeDataEx(mapAlphaData, var1.j, var1.i, var1.type.moveRadius + 1, var1);
		// this.var_38d5 = listUnits(0, -1, this.currentTurningPlayer);
		var_38d5 = getSoldiers();
		int var2 = 0;
		
		int var3 = numberPlayers + var_38d5.length + buildings.length;
		var_395f = new int[var3][5];
		var_39d0 = 0;
		int var5 = 10000;
		var_3a15 = -1;
		var_3a34 = -1;
		var_3a41 = -1;
		var_3a5c = -1;
		
		int var6;
		int var9;
		for (var6 = 0; var6 < var_38d5.length + numberPlayers; ++var6)
		{
			Unit var7 = null;
			if (var6 >= var_38d5.length)
			{
				var7 = this.getKing(var6 - var_38d5.length);
				if (var7 != null)
					if (var7.player.team != game.currentPlayer.team && !isKingLive)
						var_395f[var2][2] += var7.sub_462(var7.j, var7.i, (Unit) null) * 2;
					else if (game.currentTurn >= 15 && var7.player.team != game.currentPlayer.team && var7.player.units.size() < 4
					         && game.currentPlayer.units.size() >= 8)
						var_395f[var2][2] += var7.sub_462(var7.j, var7.i, (Unit) null) * 2;
					else if (var7.player != game.currentPlayer)
						var7 = null;
			}
			else if (isKingLive)
				var7 = var_38d5[var6];
			
			if (var7 != null)
			{
				var_395f[var2][0] = var7.j;
				var_395f[var2][1] = var7.i;
				if (var7.player == game.currentPlayer)
				{
					Unit[] var8 = var7.getUnitsWithinRange(var7.j, var7.i, 1, 5, (byte) 0);
					
					for (var9 = 0; var9 < var8.length; ++var9)
						var_395f[var2][2] += var8[var9].sub_462(var8[var9].j, var8[var9].i, var7);
				}
				
				if (var_395f[var2][2] > 0)
				{
					var_395f[var2][4] += var7.sub_462(var7.j, var7.i, (Unit) null);
					var_395f[var2][4] += var7.var_8f7;
					if (var_395f[var2][2] > var_39d0)
						var_39d0 = var_395f[var2][2];
					
					var_395f[var2][3] = Math.abs(var7.j - var1.j) + Math.abs(var7.i - var1.i);
					if (var_395f[var2][3] < 1)
						var_395f[var2][3] = 1;
					
					if (var_395f[var2][3] < var5)
						var5 = var_395f[var2][3];
				}
				else
					var_395f[var2][2] = -6666;
			}
			else
				var_395f[var2][2] = -6666;
			
			++var2;
		}
		
		var6 = 666;
		int var24 = 666;
		int var20 = -1;
		var9 = -1;
		
		int var10;
		int var16;
		Unit var28;
		for (var10 = 0; var10 < buildings.length; ++var10)
		{
			// byte var11 = this.buildingData[var10][0];
			// byte var12 = this.buildingData[var10][1];
			Cell cell = buildings[var10];
			int var11 = cell.j;
			int var12 = cell.i;
			// byte var13 = getTileType(var11, var12);
			boolean var14 = isBuildingOfFraction(var11, var12, var1.player);
			var_395f[var2][2] = -6666;
			if (var14 || var_3903[var10] != null)
			{
				Unit[] var15 = var1.getUnitsWithinRange(var11, var12, 1, 5, (byte) 0);
				var_395f[var2][0] = var11;
				var_395f[var2][1] = var12;
				var_395f[var2][2] = 0;
				
				for (var16 = 0; var16 < var15.length; ++var16)
					if (var_3903[var10] != null && !var14)
						var_395f[var2][2] += var15[var16].sub_462(var15[var16].j, var15[var16].i, (Unit) null);
						// else if (var13 == 8 && var15[var16].hasProperty((short) 8) || var13 == 9 && var15[var16].hasProperty((short) 16))
					else if (var15[var16].canCapture(cell.type) || var15[var16].canRepair(cell.type))
						var_395f[var2][2] += var15[var16].sub_462(var15[var16].j, var15[var16].i, (Unit) null);
				
				if (var_395f[var2][2] == 0)
					if (var_3903[var10] != null && !var14)
					{
						var_395f[var2][2] = 100;
						var_395f[var2][4] += 2000;
					}
					else
						var_395f[var2][2] = -6666;
				
				if (var_395f[var2][2] != -6666)
				{
					var_395f[var2][4] += var_399e[var10];
					if (var_395f[var2][2] > var_39d0)
						var_39d0 = var_395f[var2][2];
					
					var_395f[var2][3] = Math.abs(var11 - var1.j) + Math.abs(var12 - var1.i);
					if (var_395f[var2][3] < 1)
						var_395f[var2][3] = 1;
					
					if (var_395f[var2][3] < var5)
						var5 = var_395f[var2][3];
				}
			}
			
			if (sub_e68(var11, var12, var1.player.team))
			{
				if (((var28 = getUnitEx(var11, var12)) == null || var28.player == var1.player) && (var16 = Math.abs(var11 - var1.j) + Math.abs(var12 - var1.i)) < var24)
				{
					var9 = var10;
					var24 = var16;
				}
			}
			// else if ((this.var_3903[var10] == null || this.var_3903[var10] == var1) && (var13 == 8 && var1.hasProperty((short) 8) || var13 == 9 && var1.hasProperty((short) 16)))
			else if ((var_3903[var10] == null || var_3903[var10] == var1) && var1.canCapture(cell.type))
			{
				int var26;
				if ((var26 = Math.abs(var11 - var1.j) + Math.abs(var12 - var1.i)) < var6)
				{
					var20 = var10;
					var6 = var26;
				}
				
				Unit var19 = getUnitEx(var11, var12);
				if (var26 < var24 && mapAlphaData[var11][var12] > 0 && (var19 == null || var19.player == var1.player))
				{
					var9 = var10;
					var24 = var26;
				}
			}
			
			++var2;
		}
		
		int var21;
		int var25;
		if (var1.health < 50 && var9 != -1)
		{
			if (var9 == var20)
				var_3a5c = var20;
			
			var_3903[var9] = var1;
			var_3a15 = buildings[var9].j;
			var_3a34 = buildings[var9].i;
			fillMoveRangeDataEx(var_39e3, var_3a15, var_3a34, 10, var1);
		}
		else if (isKingLive && var20 != -1 && var1.type.captureTypes.length > 0)
		{
			var_3a5c = var20;
			var_3903[var20] = var1;
			var_3a15 = buildings[var20].j;
			var_3a34 = buildings[var20].i;
			fillMoveRangeDataEx(var_39e3, var_3a15, var_3a34, 10, var1);
		}
		else
		{
			var10 = -1;
			var21 = -6666;
			
			for (var25 = 0; var25 < var2; ++var25)
				if (var_395f[var25][2] > -6666)
				{
					if (var_395f[var25][2] > 0)
						var_395f[var25][2] = var_395f[var25][2] * var5 / var_395f[var25][3];
					
					var_395f[var25][2] -= var_395f[var25][4];
					if (var_395f[var25][2] > var21)
					{
						var21 = var_395f[var25][2];
						var10 = var25;
					}
				}
			
			if (var10 != -1)
			{
				var25 = var1.sub_462(var1.j, var1.i, (Unit) null);
				if (var10 < var_38d5.length)
					var_38d5[var10].var_8f7 += var25;
				else if (var10 < numberPlayers + var_38d5.length)
					this.getKing(var10 - var_38d5.length).var_8f7 += var25;
				else
				{
					var_3a41 = var10 - numberPlayers - var_38d5.length;
					var_399e[var_3a41] += var25;
				}
				
				var_3a15 = var_395f[var10][0];
				var_3a34 = var_395f[var10][1];
				fillMoveRangeDataEx(var_39e3, var_3a15, var_3a34, 10, var1);
			}
		}
		
		var10 = -10000;
		var21 = 0;
		
		for (var25 = mapAlphaData.length; var21 < var25; ++var21)
		{
			int var23 = 0;
			
			for (int var29 = mapAlphaData[var21].length; var23 < var29; ++var23)
				if (mapAlphaData[var21][var23] > 0 && ((var28 = getUnitEx(var21, var23)) == null
				                                       || var28 == var1 && game.checkFloating()
				                                       || var28 != var1 && var_381f == null && var28.player == var1.player && !var28.isMove))
				{
					if (var1.type.canDoTwoActionAfterOne || var28 == var1)
					{
						Unit[] var17 = var1.getUnitsToAttack(var21, var23);
						
						var16 = 0;
						for (Unit element : var17)
							if ((var16 = sub_10cb(var1, var21, var23, element, (Unit) null)) > var10)
							{
								var_37e4 = null;
								var_37c2 = element;
								var10 = var16;
								var_3733 = var21;
								var_3781 = var23;
							}
						// if (var17.length > 0)
						// System.out.println(var23 + " " + var21 + ": " + var16);
					}
					
					if (var1.type.raiseRange.name != "EMPTY")
					{
						Unit[] unitsToRaise = var1.getUnitsToRaise(var21, var23);
						
						for (Unit unit : unitsToRaise)
							if ((var16 = sub_10cb(var1, var21, var23, (Unit) null, unit)) > var10)
							{
								var_37c2 = null;
								var_37e4 = unit;
								var10 = var16;
								var_3733 = var21;
								var_3781 = var23;
							}
					}
					
					if ((var16 = sub_10cb(var1, var21, var23, (Unit) null, (Unit) null)) > var10)
					{
						var_37c2 = null;
						var_37e4 = null;
						var10 = var16;
						var_3733 = var21;
						var_3781 = var23;
					}
					// System.out.println(var23 + " " + var21 + ": " + var16);
				}
		}
		
		if (var_3a5c != -1)
			var_3947[var_3a5c] = (byte) (10 - var_39e3[var_3733][var_3781]);
		
		var_381f = null;
		Unit var27;
		if ((var27 = getUnitEx(var_3733, var_3781)) != null && var27 != var1)
		{
			var_381f = var27;
			var_36e4 = 0;
		}
		else
			// moveCursor(var1.j, var1.i);
			// this.var_31be = var1;
			var_36e4 = 3;
	}
	
	public final int sub_10cb(final Unit var1, int var2, int var3, Unit var4, Unit var5)
	{
		Cell cell = fieldCells[var3][var2];
		int var6 = 0;
		int var7;
		int var8;
		if (var_3a5c != -1 && isKingLive)
		{
			if (var_3a15 != -1)
				if (var_39e3[var2][var3] > 0)
					var6 = 0 + 100 + 100 * var_39e3[var2][var3] / 10;
				else
				{
					var7 = Math.abs(var_3a15 - var1.j) + Math.abs(var_3a34 - var1.i);
					var8 = Math.abs(var_3a15 - var2) + Math.abs(var_3a34 - var3);
					var6 = 0 + 100 * (var7 - var8) / var1.getMoveRadius();
					// if (II.TERRAIN_STEPS_REQUIRED[getTileType(var2, var3)] <= 1)
					if (cell.getSteps() <= 1)
						var6 += 20;
				}
			
			if (var4 == null && !sub_e68(var2, var3, var1.player.team))
				// if (var1.hasProperty((short) 16) && getTileType(var2, var3) == 9)
				if (cell.type.name == "CASTLE" && var1.canCapture(cell.type))
					var6 += 300;
					// else if (var1.hasProperty((short) 8) && (getTileType(var2, var3) == 8 || this.mapData[var2][var3] == 27))
				else if (var1.canCapture(cell.type))
					var6 += 200;
		}
		
		/*
		switch (var1.type)
		{
			case 2:
				if (getTileType(var2, var3) == 5)
					var6 += 25;
				break;
			case 3:
				if (var5 != null)
					var6 += 100;
				break;
			case 4:
				Unit[] var11 = var1.getUnitsWithinRange(var2, var3, 1, 2, (byte) 2);
				if (var5 != null)
					var6 += 25 * var11.length;
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
		}
		//*/
		
		if (var1.type.name == "ELEMENTAL" && rules.getCellGroup("WATER").contains(cell.type))
			var6 += 25;
		if (var1.type.name == "SORCERESS" && var5 != null)
			var6 += 100;
		if (var1.type.name == "WISP")
			var6 += 25 * new ActionHelper(game).getInRange(game.fieldUnits, var3, var2, ((BonusCreatorWisp) rules.getUnitType("WISP").creators[0]).range, new Checker<Unit>()
			{
				@Override
				public boolean check(Unit targetUnit)
				{
					return targetUnit != null && var1.player.team == targetUnit.player.team;
				}
			}).size();
		
		// if (var4 != null && var2 == 8 && var3 == 13)
		// System.out.println();
		
		if (var4 != null)
		/*
		if (var4.unitState == 4)
		{
			var7 = getBuildingFraction(var4.j, var4.i);
			var8 = sub_110d(var4.j, var4.i);
			if (var7 != 0 && var8 != -1 && this.var_3903[var8] == null)
				var6 += var1.sub_462(var2, var3, var4) / 2;
		}
		else
		//*/
		{
			if (!canPerformCloseAttack(var4, var2, var3))
				var6 += var1.sub_462(var2, var3, var4) * 2;
				// var6 += UnitHelper.getDecreaseHealth(var1, var4);
			else
				var6 += var1.sub_462(var2, var3, var4) * 3 / 2 - var4.sub_462(var1);
			
			if (var4.type.name == "KING")
				var6 += 25;
			if (var4.type.name == "CRYSTAL")
				var6 += 100;
		}
		
		var6 += cell.type.defense;
		if (var1.health < 100 && sub_e68(var2, var3, var1.player.team))
			var6 += 100 - var1.health;
		
		var7 = sub_110d(var2, var3);
		int var9;
		if (var_39e3[var2][var3] > 0)
		{
			var8 = var_39e3[var2][var3];
			var9 = 10 - (var1.getMoveRadius() + 1) / 2;
			if (var8 > var9)
				var8 = var9;
			
			var6 += 50 + 100 * var8 / var9;
		}
		else if (var_3a15 != -1)
		{
			var8 = Math.abs(var_3a15 - var1.j) + Math.abs(var_3a34 - var1.i);
			var9 = Math.abs(var_3a15 - var2) + Math.abs(var_3a34 - var3);
			var6 += 50 * (var8 - var9) / var1.getMoveRadius();
		}
		
		Unit var10;
		if (var7 != -1 && (var10 = var_3903[var7]) != null && var10 != var1 && !var10.isMove && var_3947[var7] <= var10.getMoveRadius())
			var6 -= 200;
		
		int ret = var6 + 20 * (Math.abs(var2 - var1.j) + Math.abs(var3 - var1.i)) / var1.getMoveRadius();
		// System.out.println("(" + var3 + ", " + var2 + "): " + ret + " = " + var6 + " + " + 20 * (Math.abs(var2 - var1.j) + Math.abs(var3 - var1.i)) / var1.moveRadius);
		return var6 += 20 * (Math.abs(var2 - var1.j) + Math.abs(var3 - var1.i)) / var1.getMoveRadius();
	}
	
	private boolean canBuyUnit(Unit unit, int x, int y)
	{
		if (game.currentPlayer.gold < unit.type.cost)
			return false;
		
		int[][] alphaData = new int[w][h];
		for (int x2 = 0; x2 < w; x2++)
			for (int y2 = 0; y2 < h; y2++)
				alphaData[x2][y2] = 0;
		alphaData[x][y] = unit.type.moveRadius;
		boolean[][] used = new boolean[alphaData.length][alphaData[0].length];
		
		while (true)
		{
			int max = 0;
			int maxX = -1, maxY = -1;
			for (int i = 0; i < w; i++)
				for (int j = 0; j < h; j++)
					if (!used[i][j] && alphaData[i][j] > max)
					{
						max = alphaData[i][j];
						maxX = i;
						maxY = j;
					}
			if (maxX == -1)
				return false;
			
			if (fieldUnits[maxY][maxX] == null)
				return true;
			
			used[maxX][maxY] = true;
			update(alphaData, maxX, maxY, unit, -1, 0);
			update(alphaData, maxX, maxY, unit, 0, -1);
			update(alphaData, maxX, maxY, unit, 0, +1);
			update(alphaData, maxX, maxY, unit, +1, 0);
		}
	}
	
	private int countUnits(UnitType type)
	{
		int number = 0;
		for (Unit unit : game.currentPlayer.units)
			if (unit.type == type)
				number++;
		return number;
	}
	
	private boolean canPerformCloseAttack(Unit unit, int targetJ, int targetI)
	{
		Range type = unit.type.attackRangeReverse;
		boolean[][] field = type.table;
		int size = field.length;
		
		int relI = targetI - unit.i + type.radius;
		int relJ = targetJ - unit.j + type.radius;
		
		return relI >= 0 && relI < size && relJ >= 0 && relJ < size && field[relI][relJ];
	}
	
	private void fillMoveRangeDataEx(int[][] alphaData, int x, int y, int stepsCount, Unit unit)
	{
		for (int x2 = 0; x2 < w; x2++)
			for (int y2 = 0; y2 < h; y2++)
				alphaData[x2][y2] = 0;
		fillMoveRangeDataEx(game, alphaData, x, y, stepsCount, -1, unit);
	}
	
	public static boolean fillMoveRangeDataEx(Game game, int[][] alphaData, int x, int y, int stepsCount, int var4, Unit unit)
	{
		Unit nextUnit = game.fieldUnits[y][x];
		if (stepsCount > alphaData[x][y] && (nextUnit == null || nextUnit.player.team == unit.player.team || var4 == -1))
		{
			alphaData[x][y] = stepsCount;
			int var8;

			/*
			boolean flag;

			if (currentMainDisplayable.getUnit(x, y, (byte) 0) == null)
			{
				flag = true;
			}
			else
			{
				var8 = stepsCount - getRequiredSteps(x, y - 1, unitType, turnPos);

				if (var4 != 1 && var8 >= 0 && sub_695(alphaData, x, y - 1, var8, 2, unitType, turnPos, var7) && var7)
				{
					flag = true;
				}
				else
				{
					var8 = stepsCount - getRequiredSteps(x, y + 1, unitType, turnPos);

					if (var4 != 2 && var8 >= 0 && sub_695(alphaData, x, y + 1, var8, 1, unitType, turnPos, var7) && var7)
					{
						flag = true;
					}
					else
					{
						var8 = stepsCount - getRequiredSteps(x - 1, y, unitType, turnPos);

						if (var4 != 4 && var8 >= 0 && sub_695(alphaData, x - 1, y, var8, 8, unitType, turnPos, var7) && var7)
						{
							flag = true;
						}
						else
						{
							var8 = stepsCount - getRequiredSteps(x + 1, y, unitType, turnPos);

							if (var4 != 8 && var8 >= 0 && sub_695(alphaData, x + 1, y, var8, 4, unitType, turnPos, var7) && var7)
							{
								flag = true;
							}
							else
							{
								flag = false;
							}
						}
					}
				}
			}
			//*/
			
			// @formatter:off
			return    var4 != 1 && y > 0          && (var8 = stepsCount - unit.getSteps(y - 1, x)) >= 0 && fillMoveRangeDataEx(game, alphaData, x, y - 1, var8, 2, unit)
			       || var4 != 2 && y < game.h - 1 && (var8 = stepsCount - unit.getSteps(y + 1, x)) >= 0 && fillMoveRangeDataEx(game, alphaData, x, y + 1, var8, 1, unit)
			       || var4 != 4 && x > 0          && (var8 = stepsCount - unit.getSteps(y, x - 1)) >= 0 && fillMoveRangeDataEx(game, alphaData, x - 1, y, var8, 8, unit)
			       || var4 != 8 && x < game.w - 1 && (var8 = stepsCount - unit.getSteps(y, x + 1)) >= 0 && fillMoveRangeDataEx(game, alphaData, x + 1, y, var8, 4, unit);
			// @formatter:on
		}
		else
		{
			return false;
		}
	}

	private void update(int[][] alphaData, int x, int y, Unit unit, int dx, int dy)
	{
		int nx = x + dx;
		int ny = y + dy;
		if (!game.checkCoordinates(ny, nx))
			return;
		Unit nextUnit = fieldUnits[ny][nx];
		if (nextUnit != null && nextUnit.player.team != unit.player.team)
			return;
		final int cellWeight = unit.getSteps(ny, nx);
		alphaData[nx][ny] = Math.max(alphaData[nx][ny], alphaData[x][y] - cellWeight);
	}

	// Возвращает номер здания по координатам, -1 если не найден
	private int sub_110d(int x, int y)
	{
		for (int i = 0; i < buildings.length; i++)
			if (buildings[i] == fieldCells[y][x])
				return i;
		return -1;
	}
	
	// 0 ничейный, 1 синий, 2 красный, 3 зеленый, 4 черный, -1 иначе
	// private int getBuildingFraction(int x, int y)
	
	private Unit getUnitEx(int j, int i)
	{
		return game.getUnit(i, j);
	}
	
	// ровно того же цвета
	private boolean isBuildingOfFraction(int x, int y, Player player)
	{
		return fieldCells[y][x].player == player;
	}
	
	// принадлежит ли здание этой команде
	public final boolean sub_e68(int x, int y, Team team)
	{
		// int var4 = getFractionPosInTurnQueue(getBuildingFraction(var1, var2));
		// return var4 > -1 ? var3 == this.playerTeams[var4] : false;
		return fieldCells[y][x].getTeam() == team;
	}
	
	private Unit getKing(int iPlayer)
	{
		return getKing(game.players[iPlayer]);
	}
	
	private Unit getKing(Player player)
	{
		UnitType king = new UnitHelper(game).getKingType(player);
		for (Unit unit : player.units)
			if (unit.type == king)
				return unit;
		return null;
	}
	
	private Unit[] getSoldiers()
	{
		ArrayList<Unit> soldiers = new ArrayList<Unit>();
		for (Unit unit : game.currentPlayer.units)
			if (unit.type.name == "SOLDIER")
				soldiers.add(unit);
		return soldiers.toArray(new Unit[0]);
	}
	
}
