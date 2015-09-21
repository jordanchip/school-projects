package client_indexer;

public class GuiCell {
	public int record;
	public int field;
	
	public GuiCell(int record, int field) {
		this.record = record;
		this.field = field;
	}
	
	public int getRecord() {
		return record;
	}
	
	public int getField() {
		return field;
	}
}
