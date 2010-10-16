package org.newdawn.fizzy.render;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.newdawn.fizzy.World;

public class WorldWindow extends Frame {
	private World world;
	private WorldCanvas canvas;
	
	public WorldWindow(World world) {
		super("Fizzy Render Window");
		this.world = world;
	
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		canvas = new WorldCanvas(world);
		add(canvas);
		setSize(500,500);
	}
	
	public void start() {
		setVisible(true);
		canvas.start();
	}
	
	public void stop() {
		canvas.stop();
	}
	
	public World getWorld() {
		return world;
	}
}
