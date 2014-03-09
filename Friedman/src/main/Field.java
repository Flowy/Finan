package main;

import java.util.Collection;

public abstract class Field implements Comparable<Field>{
	
	private final int positionX;
	private final int positionY;
	
	Field(int x, int y) {
		positionX = x;
		positionY = y;
	}
		
	int getX() {
		return positionX;
	}
	
	int getY() {
		return positionY;
	}
	
	int distanceFrom(Field finish) {
		return distanceFrom(this.getX(), this.getY(), finish);
	}
	
	static int distanceFrom(int x, int y, Field finish) {
		return Math.abs(x - finish.getX()) + Math.abs(y - finish.getY());
	}
	
	Field chooseClosest(Collection<Shop> fields) {
		Field closest = null;
		int actualDistance;
		int minDistance;
		for (Field field: fields) {
			if (closest == null) {
				closest = field;
			} else {
				actualDistance = this.distanceFrom(field);
				minDistance = closest.distanceFrom(field);
				if (actualDistance < minDistance) {
					closest = field;
				}
			}
		}
		return closest;		
	}
	
	static Field chooseClosest(int x, int y, Collection<Field> fields) {
		Field closest = null;
		for (Field field: fields) {
			if (closest == null) {
				closest = field;
			} else {
				closest = closest.distanceFrom(field) < distanceFrom(x, y, field) ? closest : field;
			}
		}
		return closest;
	}
	
	@Override
	public int compareTo(Field o) {
		int diff;
		diff = this.getX() - o.getX();
		if (diff == 0) {
			diff = this.getY() - o.getY();
		}
		return diff;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean result = false;
		
		if (o instanceof Field) {
			Field other = (Field) o;
			if (other.getX() == this.getX() && other.getY() == this.getY()) {
				result = true;
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "Position: " + positionX + ", " + positionY;
	}
	
	@Override
	public int hashCode() {
		int hash = 31;
		hash = hash * positionX + positionY;
		return hash;
	}
}
