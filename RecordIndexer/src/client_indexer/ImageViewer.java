package client_indexer;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

import shared_model.Batch;
import shared_model.Field;
import shared_model.Project;
import client_facade.BatchState;

import java.util.*;
import java.util.List;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


@SuppressWarnings("serial")
public class ImageViewer extends JComponent implements BatchState.BatchStateListener {

	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	
	private int w_translateX;
	private int w_translateY;
	private double scale;
	private int imageWidth;
	private int imageHeight;
	
	private boolean dragging;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartTranslateX;
	private int w_dragStartTranslateY;
	private AffineTransform dragTransform;
	private BatchState batchState;
	private boolean highlightEnabled = true;

	private ArrayList<DrawingShape> shapes;
	
	public ImageViewer(BatchState batchState) {
		this.batchState = batchState;
		
		w_translateX = 0;
		w_translateY = 0;
		scale = 1;
		
		initDrag();

		shapes = new ArrayList<DrawingShape>();
		
		this.setBackground(Color.GRAY);
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(200, 200));
		
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addComponentListener(componentAdapter);
		batchState.addListener(this);
		this.addMouseWheelListener(mouseAdapter);
	}
	
	public void loadImage(String imageURL) {
		try {
			URL url = new URL(imageURL);
			Image image = loadImage(url);
			imageWidth = image.getWidth(null);
			imageHeight = image.getHeight(null);
			shapes.add(new DrawingImage(image, new Rectangle2D.Double(0, 0,
													image.getWidth(null), image.getHeight(null))));
			shapes.add(new DrawingRect(new Rectangle2D.Double(0, 0, 0, 0), new Color(12, 43, 245, 100)));
			this.repaint();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}	
	
	private Image loadImage(URL url) {
		try {
			return ImageIO.read(url);
		}
		catch (IOException e) {
			return NULL_IMAGE;
		}
	}
	
	private void initDrag() {
		dragging = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartTranslateX = 0;
		w_dragStartTranslateY = 0;
		dragTransform = null;
	}

	
	public void setScale(double newScale) {
		if (newScale < 0.2)
			scale = 0.2;
		else if (newScale > 20)
			scale = 20;
		else
			scale = newScale;
		this.repaint();
	}
	public double getScale() {
		return scale;
	}
	
	public void setTranslation(int w_newTranslateX, int w_newTranslateY) {
		w_translateX = w_newTranslateX;
		w_translateY = w_newTranslateY;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);
		
		g2.translate(getWidth() / 2.0, getHeight() /2.0);
		g2.scale(scale, scale);
		g2.translate( -imageWidth / 2.0 + w_translateX, -imageHeight / 2.0 + w_translateY);
		
		
		drawShapes(g2);
	}
	
	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0,  0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) {
		for (DrawingShape shape : shapes) {
			if (highlightEnabled)
				shape.draw(g2);
			else
				if (!(shape instanceof DrawingRect))
					shape.draw(g2);
		}
	}

	@Override
	public void valueChanged(GuiCell cell, String newValue) {
		
	}

	@Override
	public void selectedCell(GuiCell newCell) {
		List<Field> fields = batchState.getFields();
		int xCoord = fields.get(newCell.field).getxCoord();
		int width = fields.get(newCell.field).getWidth();
		Project project = batchState.getProject();
		int height = project.getRecordsHeight();
		int yCoord = project.getFirstYCoord() + height*(newCell.record);
		for (DrawingShape ds : shapes) {
			if (ds instanceof DrawingRect) {
				((DrawingRect) ds).setRect(new Rectangle2D.Double(xCoord, yCoord, width, height));
			}
		}
		repaint();
	}
	@Override
	public void batchChanged(Batch batch) {
		if (batch.getID() == -1) 
			shapes.clear();
		else
			loadImage(batch.getPath());
	}

	@Override
	public void projectChanged(Project project) {
		if (project.getID() <= 0)
			shapes.clear();
		repaint();
	}
	@Override
	public void fieldsChanged(List<Field> fields) {
	}

	@Override
	public void invert() {
		for (DrawingShape ds : shapes) {
			if (ds instanceof DrawingImage) {
				RescaleOp op = new RescaleOp(-1.0f, 255f, null);
				BufferedImage negative = op.filter((BufferedImage) ((DrawingImage) ds).getImage(), null);
				((DrawingImage) ds).setImage((Image)negative);
			}
		}
		repaint();
	}
	
	@Override
	public void toggleHighlights(boolean highlightEnabled) {
		this.highlightEnabled = highlightEnabled;
		repaintImages();
	}
	@Override
	public void setPos(int x, int y) {
		w_translateX = x;
		w_translateY = y;
	}
	@Override
	public void setZoom(double zoom) {
		setScale(zoom);
	}
	
	public void repaintImages() {
		SwingUtilities.updateComponentTreeUI(this);
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			transform.translate(getWidth() / 2.0, getHeight() /2.0);
			transform.scale(scale, scale);
			transform.translate( -imageWidth / 2.0 + w_translateX, -imageHeight / 2.0 + w_translateY);
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			boolean hitShape = false;
			
			Graphics2D g2 = (Graphics2D)getGraphics();
			for (DrawingShape shape : shapes) {
				if (shape.contains(g2, w_X, w_Y)) {
					hitShape = true;
					break;
				}
			}
			
			if (hitShape) {
				dragging = true;		
				w_dragStartX = w_X;
				w_dragStartY = w_Y;		
				w_dragStartTranslateX = w_translateX;
				w_dragStartTranslateY = w_translateY;
				dragTransform = transform;
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {		
			if (dragging) {
				int d_X = e.getX();
				int d_Y = e.getY();
				
				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					dragTransform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();
				
				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;
				
				//Ensure the image doesn't go off the screen
				int tempX = w_dragStartTranslateX + w_deltaX;
				if (tempX > -600 && tempX < 600)
					w_translateX = tempX;
				int tempY = w_dragStartTranslateY + w_deltaY;
				if (tempY > -400 && tempY < 400)
					w_translateY = tempY;
				batchState.setTranslate(w_translateX, w_translateY);
				
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initDrag();
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			AffineTransform transform = new AffineTransform();
			transform.translate(getWidth() / 2.0, getHeight() /2.0);
			transform.scale(scale, scale);
			transform.translate( -imageWidth / 2.0 + w_translateX, -imageHeight / 2.0 + w_translateY);
			
			int d_X = e.getX();
			int d_Y = e.getY();
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			if (batchState.getProject().getID() > 0 ||
					batchState.getFields().size() > 0) {
				int row = 0;
				int column = 0;
				
				//Check if the X coord is contained within a field.
				boolean foundXCoord = false;
				List<Field> fields = batchState.getFields();
				int fieldX = 0;
				int fieldWidth = 0;
				for (int i = 0; i < fields.size(); i++) {
					fieldX = fields.get(i).getxCoord();
					fieldWidth = fields.get(i).getWidth();
					if (w_X > fieldX && w_X < fieldX+fieldWidth) {
						foundXCoord = true;
						column = i;
						break;
					}
				}
				//If the xCoord wasn't hit, don't check any further
				if (!foundXCoord)
					return;
				
				boolean foundYCoord = false;
				Project project = batchState.getProject();
				int recordHeight = project.getRecordsHeight();
				int recordY = project.getFirstYCoord();
				for (int i = 0; i < project.getRecordsPerImage(); i++) {
					if (w_Y > recordY && w_Y < recordY+recordHeight) {
						foundYCoord = true;
						row = i;
						break;
					}
					recordY += recordHeight;
				}
				if (foundYCoord) {
					//Find the rectangle, and create it in the right place
					for (DrawingShape ds : shapes) {
						if (ds instanceof DrawingRect) {
							((DrawingRect) ds).setRect(new Rectangle2D.Double(fieldX, recordY, fieldWidth, recordHeight));
						}
					}
					batchState.setSelectedCell(new GuiCell(row, column));
					repaintImages();
				}
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			//Mouse scroll up
			if (e.getWheelRotation() < 0)
				batchState.setZoom(scale*1.1);
			//Mouse scroll down
			else
				batchState.setZoom(scale*0.9);
		}	
	};
	
	private ComponentAdapter componentAdapter = new ComponentAdapter() {

		@Override
		public void componentHidden(ComponentEvent e) {
			return;
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			return;
		}

		@Override
		public void componentResized(ComponentEvent e) {
			return;
			//updateTextShapes();
		}

		@Override
		public void componentShown(ComponentEvent e) {
			return;
		}	
	};

	
	/////////////////
	// Drawing Shape
	/////////////////
	
	
	interface DrawingShape {
		boolean contains(Graphics2D g2, double x, double y);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
	}


	class DrawingRect implements DrawingShape {

		private Rectangle2D rect;
		private Color color;
		
		public DrawingRect(Rectangle2D rect, Color color) {
			this.rect = rect;
			this.color = color;
		}
		
		public void setRect(Rectangle2D rect) {
			this.rect = rect;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fill(rect);
		}
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}


	class DrawingImage implements DrawingShape {

		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) {
			this.image = image;
			this.rect = rect;
		}
		
		public void setImage(Image image) {
			this.image = image;
		}
		
		public Image getImage() {
			return image;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.drawImage(image, (int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getMaxX(), (int)rect.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}	
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}
	}

}