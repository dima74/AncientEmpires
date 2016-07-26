package ru.ancientempires.swing;

import android.graphics.Canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.draws.DrawMain;
import ru.ancientempires.draws.inputs.InputMain;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;

public class GameComponent extends JComponent implements MouseListener, MouseMotionListener
{
	
	public int h = 700;
	public int w = 600;

	public long begin = Long.MAX_VALUE;
	public long end   = Long.MIN_VALUE;
	public long last  = System.currentTimeMillis();

	public JFrame    frame;
	public Game      game;
	public DrawMain  drawMain;
	public InputMain inputMain;

	volatile public boolean needUpdateCampaign = false;

	public float   touchY;
	public float   touchX;
	public boolean isTouch;

	private int     lastY;
	private int     lastX;
	private boolean isDrag;

	public Runnable runnable;

	public void runOnGameThread(Runnable runnable)
	{
		MyAssert.a(this.runnable == null);
		MyAssert.a(runnable != null);
		this.runnable = runnable;
	}
	
	public GameComponent(Game game, JFrame frame)
	{
		this.frame = frame;
		setPreferredSize(new Dimension(w, h));
		GameActivity activity = new GameActivity();
		activity.view = this;
		this.game = activity.game = game;
		
		drawMain = new DrawMain(activity);
		inputMain = new InputMain(activity, drawMain);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	public void paint(Graphics graphics)
	{
		Graphics2D g = (Graphics2D) graphics;
		
		long current = System.currentTimeMillis();
		long delta = Math.max(1, current - last);
		last = current;
		
		g.setColor(Color.WHITE);
		g.fill(new Rectangle2D.Float(0, 0, w, h));
		g.setColor(Color.CYAN);
		g.fill(new Rectangle2D.Float(10, 10, w - 20, h - 20));
		
		Canvas canvas = new Canvas(g);
		drawMain.iFrame++;
		drawMain.draw(canvas);
		
		boolean isTouch = false;
		float touchY = 0, touchX = 0;
		synchronized (this)
		{
			if (this.isTouch)
			{
				this.isTouch = false;
				isTouch = true;
				touchY = this.touchY;
				touchX = this.touchX;
			}
		}
		if (isTouch)
			drawMain.touch(touchY, touchX);
		if (runnable != null)
		{
			runnable.run();
			runnable = null;
		}
		
		if (needUpdateCampaign)
		{
			game.campaign.update();
			needUpdateCampaign = false;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (drawMain.isActiveGame())
			synchronized (this)
			{
				isTouch = true;
				touchY = e.getY();
				touchX = e.getX();
			}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (!drawMain.isActiveGame())
			return;
		isDrag = true;
		lastY = e.getY();
		lastX = e.getX();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		isDrag = false;
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{}
	
	@Override
	public void mouseExited(MouseEvent e)
	{}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (isDrag && drawMain.isActiveGame())
		{
			int y = e.getY();
			int x = e.getX();
			int distanceY = y - lastY;
			int distanceX = x - lastX;
			lastY = y;
			lastX = x;
			drawMain.onScroll(-distanceY, -distanceX);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{}
	
}
