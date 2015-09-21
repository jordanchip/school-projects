package client_indexer;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


public interface DrawingShape {
	boolean contains(Graphics2D g2, double x, double y);
	void draw(Graphics2D g2);
	Rectangle2D getBounds(Graphics2D g2);
}