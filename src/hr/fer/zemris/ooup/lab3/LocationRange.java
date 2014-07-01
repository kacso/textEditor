package hr.fer.zemris.ooup.lab3;

public class LocationRange {
	Location start;
	Location end;
	
	public LocationRange(){
		start = new Location();
		end = new Location();
	}
	
	public LocationRange(Location start, Location end){
		this.start = start;
		this.end = end;
	}
}
