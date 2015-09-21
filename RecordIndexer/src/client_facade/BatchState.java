package client_facade;

import java.util.ArrayList;
import java.util.List;

import shared_model.*;
import client_indexer.GuiCell;

public class BatchState {

	private String[][] values;
	private boolean[][] misspelledWords;
	private GuiCell selectedCell;
	//Don't serialize this info!
	private transient List<BatchStateListener> listeners;
	private Project project;
	private List<Field> fields;
	private Batch batch;
	private boolean highlightEnabled;
	private boolean isInverted;
	private int xTranslate;
	private int yTranslate;
	private double zoom;
	private int windowX;
	private int windowY;
	private int windowWidth;
	private int windowHeight;
	private int horizontalDividerSplit;
	private int verticalDividerSplit;
	
	
	public BatchState() {
		values = null;
		selectedCell = null;
		listeners = new ArrayList<BatchStateListener>();
		project = new Project();
		fields = new ArrayList<Field>();
		batch = new Batch();
		highlightEnabled = true;
		isInverted = false;
		xTranslate = 0;
		yTranslate = 0;
		windowX = 0;
		windowY = 0;
		windowWidth = 0;
		windowHeight = 0;
		horizontalDividerSplit = 0;
		verticalDividerSplit = 0;
//		suggestedWords = new TreeSet<String>();
	}
	public void resetListeners() {
		listeners = new ArrayList<BatchStateListener>();
	}
	
	public void initializeCells() {
		if (fields.size() > 0 && project.getID() > 0) {
			int rows = project.getRecordsPerImage();
			int cols = fields.size();
			values = new String[rows][cols];
			misspelledWords = new boolean[rows][cols];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					values[i][j] = "";
					misspelledWords[i][j] = false;
				}
			}
		}
	}

	public String[][] getValues() {
		return values;
	}
	public void setValues(GuiCell cell, String value) {
		values[cell.record][cell.field] = value;
		
		for (BatchStateListener l : listeners) {
			l.valueChanged(cell, value);
		}
	}
	public void setMisspelledWord(GuiCell cell, boolean misspelled) {
		misspelledWords[cell.record][cell.field] = misspelled;
	}
	public boolean isMisspelledAt(GuiCell cell) {
		return misspelledWords[cell.record][cell.field];
	}
	public GuiCell getSelectedCell() {
		return selectedCell;
	}
	public void setSelectedCell(GuiCell selectedCell) {
		
		this.selectedCell = selectedCell;
		
		for (BatchStateListener l : listeners) {
			l.selectedCell(selectedCell);
		}
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
		initializeCells();
		for (BatchStateListener l : listeners) {
			l.projectChanged(project);
		}
		if (selectedCell == null && project.getID() > 0)
			setSelectedCell(new GuiCell(0,0));
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
		for (BatchStateListener l : listeners) {
			l.fieldsChanged(fields);
		}
	}
	
	public Batch getBatch() {
		return batch;
	}
	public void setBatch(Batch batch) {
		this.batch = batch;
		for (BatchStateListener l : listeners) {
			l.batchChanged(batch);
		}
	}
	public void invert() {
		if (isInverted)
			isInverted = false;
		else
			isInverted = true;
		for (BatchStateListener l : listeners) {
			l.invert();
		}
	}
	public boolean isHighlightEnabled() {
		return highlightEnabled;
	}
	public void toggleHighlights() {
		if (highlightEnabled)
			highlightEnabled = false;
		else
			highlightEnabled = true;
		for (BatchStateListener l : listeners) {
			l.toggleHighlights(highlightEnabled);
		}
	}
	public double getZoom() {
		return zoom;
	}
	public void setZoom(double zoom) {
		this.zoom = zoom;
		for (BatchStateListener l : listeners) {
			l.setZoom(zoom);
		}
	}
	public void setTranslate(int x, int y) {
		xTranslate = x;
		yTranslate = y;
		for (BatchStateListener l : listeners) {
			l.setPos(x, y);
		}
	}
	public int getWindowX() {
		return windowX;
	}
	public void setWindowX(int windowX) {
		this.windowX = windowX;
	}
	public int getWindowY() {
		return windowY;
	}
	public void setWindowY(int windowY) {
		this.windowY = windowY;
	}
	public int getWindowWidth() {
		return windowWidth;
	}
	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}
	public int getWindowHeight() {
		return windowHeight;
	}
	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}	
	public int getHorizontalDividerSplit() {
		return horizontalDividerSplit;
	}
	public void setHorizontalDividerSplit(int horizontalDividerSplit) {
		this.horizontalDividerSplit = horizontalDividerSplit;
	}
	
	public void addListener(BatchStateListener newListener) {
		listeners.add(newListener);
	}
	
	public void resetProjectStatus() {
		selectedCell = null;
		this.setFields(new ArrayList<Field>());
		this.setProject(new Project());
		this.setBatch(new Batch());
		this.initializeCells();
	}
	
	public void notifyAllListeners() {
		for (BatchStateListener l : listeners) {
			if (!highlightEnabled)
				l.toggleHighlights(highlightEnabled);
			l.setPos(xTranslate, yTranslate);
			l.setZoom(zoom);
			l.batchChanged(batch);
			l.fieldsChanged(fields);
			l.projectChanged(project);
			for (int i = 0; i < values.length; i++) {
				for (int j = 0; j < values[i].length; j++) {
					l.valueChanged(new GuiCell(i, j), values[i][j]);
				}
			}
			if (isInverted)
				l.invert();
			if (selectedCell != null)
				l.selectedCell(selectedCell);
			
		}
	}

	public int getVerticalDividerSplit() {
		return verticalDividerSplit;
	}
	public void setVerticalDividerSplit(int verticalDividerSplit) {
		this.verticalDividerSplit = verticalDividerSplit;
	}

	public interface BatchStateListener {
		
		public void valueChanged(GuiCell cell, String newValue);
		public void selectedCell(GuiCell newCell);
		public void projectChanged(Project project);
		public void fieldsChanged(List<Field> fields);
		public void batchChanged(Batch batch);
		public void toggleHighlights(boolean highlightEnabled);
		public void setZoom(double zoom);
		public void invert();
		public void setPos(int x, int y);
	}
	
	

	
	
}
