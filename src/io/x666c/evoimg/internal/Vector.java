package io.x666c.evoimg.internal;

public class Vector {
	
	public int x, y;
	
	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vector create(int x, int y) {
		return new Vector(x, y);
	}
	
}
