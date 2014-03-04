package main;

public class Town extends Field {
	

	
	int citizens;

	Town(int x, int y, int citizens) {
		super(x, y);
		this.citizens = citizens;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", Population: " + citizens;
	}

}
