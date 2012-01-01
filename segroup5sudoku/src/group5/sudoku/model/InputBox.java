package group5.sudoku.model;

public class InputBox {

	private int timestamp = 0;
	private int value = 0;
	
	public int getTimestamp() {
		return timestamp;
	}
	public int getValue() {
		return value;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public void resetTimestamp() {
		this.timestamp = 0;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
}
