package main;

public class Town extends Field {
	
	enum Status {
		old, toBuild, toDestroy
	}
	
	int citizens;
	Status status;

	public Town(int x, int y, int citizens, Status status) {
		super(x, y);
		this.citizens = citizens;
		this.status = status;
	}

}
