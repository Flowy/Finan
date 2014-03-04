package main;

public abstract class Field implements Comparable<Field>{
	
	private int positionX;
	private int positionY;
	
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
		return Math.abs( positionX + positionY - finish.getX() - finish.getY() );
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
			if (other.getX() == this.getX() && other.getY() == this.getY());
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
