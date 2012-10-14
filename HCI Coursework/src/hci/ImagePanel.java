package hci;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hci.utils.*;

/**
 * Handles image editing panel
 * @author Michal
 *
 */
public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {
	/**
	 * some java stuff to get rid of warnings
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * image to be tagged
	 */
	BufferedImage image = null;
	
	/**
	 * list of current polygon's vertices 
	 */
	ArrayList<Point> currentPolygon = null;
	
	/**
	 * list of polygons
	 */
	Map<String,ArrayList<Point>> polygonsList = null;
	
	/**
	 * Flag showing if a control point is being selected
	 */
	
	boolean isPointSelected = false;
	
	/**
	 * Nearest point index
	 */
	
	int pointIndex = -1;
	
	ImageLabeller parent = null;
	
	/**
	 * default constructor, sets up the window properties
	 */
	public ImagePanel(ImageLabeller parent) {
		this.parent = parent;
		currentPolygon = new ArrayList<Point>();
		polygonsList = new HashMap<String,ArrayList<Point>>();

		this.setVisible(true);

		Dimension panelSize = new Dimension(800, 600);
		this.setSize(panelSize);
		this.setMinimumSize(panelSize);
		this.setPreferredSize(panelSize);
		this.setMaximumSize(panelSize);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * extended constructor - loads image to be labelled
	 * @param imageName - path to image
	 * @throws Exception if error loading the image
	 */
	public ImagePanel(ImageLabeller parent, String imageName) throws Exception{
		this(parent);
		image = ImageIO.read(new File(imageName));
		if (image.getWidth() > 800 || image.getHeight() > 600) {
			int newWidth = image.getWidth() > 800 ? 800 : (image.getWidth() * 600)/image.getHeight();
			int newHeight = image.getHeight() > 600 ? 600 : (image.getHeight() * 800)/image.getWidth();
			System.out.println("SCALING TO " + newWidth + "x" + newHeight );
			Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
			image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(scaledImage, 0, 0, this);
		}
	}

	/**
	 * Displays the image
	 */
	public void ShowImage() {
		Graphics g = this.getGraphics();
		
		if (image != null) {
			g.drawImage(
					image, 0, 0, null);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		//display iamge
		ShowImage();
		
		//display all the completed polygons
		for(ArrayList<Point> polygon : polygonsList.values()) {
			drawPolygon(polygon);
			finishPolygon(polygon);
		}
		
		//display current polygon
		drawPolygon(currentPolygon);
	}
	
	/**
	 * displays a polygon without last stroke
	 * @param polygon to be displayed
	 */
	public void drawPolygon(ArrayList<Point> polygon) {
		Graphics2D g = (Graphics2D)this.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.GREEN);
		for(int i = 0; i < polygon.size(); i++) {
			Point currentVertex = polygon.get(i);
			if (i != 0) {
				Point prevVertex = polygon.get(i - 1);
				g.drawLine(prevVertex.getX(), prevVertex.getY(), currentVertex.getX(), currentVertex.getY());
			}
			g.fillOval(currentVertex.getX() - 5, currentVertex.getY() - 5, 10, 10);
		}
	}
	
	/**
	 * displays last stroke of the polygon (arch between the last and first vertices)
	 * @param polygon to be finished
	 */
	public void finishPolygon(ArrayList<Point> polygon) {
		//if there are less than 3 vertices than nothing to be completed
		if (polygon.size() >= 3) {
			Point firstVertex = polygon.get(0);
			Point lastVertex = polygon.get(polygon.size() - 1);
		
			Graphics2D g = (Graphics2D)this.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.GREEN);
			g.drawLine(firstVertex.getX(), firstVertex.getY(), lastVertex.getX(), lastVertex.getY());
		}
	}
	
	/**
	 * moves current polygon to the list of polygons and makes pace for a new one
	 */
	public void addNewPolygon() {
		//finish the current polygon if any
		if (currentPolygon != null ) {
			finishPolygon(currentPolygon);
			polygonsList.put(null, currentPolygon);
		}
		
		currentPolygon = new ArrayList<Point>();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			finishPolygon(currentPolygon);
			currentPolygon = new ArrayList<Point>();
			
			String polygonName = JOptionPane.showInputDialog(this.getParent(),
					"Enter polygon name",
					null,
					JOptionPane.PLAIN_MESSAGE);

			polygonsList.put(polygonName, currentPolygon);
			int insertPosition = parent.listModel.getSize();
			parent.listModel.add(insertPosition, polygonName);
			//parent.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		
		//check if the cursor is within image area
		if (x > image.getWidth() || y > image.getHeight()) {
			//if not do nothing
			return;
		}
		
		Point p = new Point(arg0.getX(), arg0.getY());
		
		pointIndex = getNearestControlPoint(p);
		
		//if pressed on an existing point
		if(pointIndex >= 0 && currentPolygon.size() > 0){
			isPointSelected = true;
		}
		
		//if drawing a new point
		else {
			
			Graphics2D g = (Graphics2D)this.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		
			
			//if the left button than we will add a vertex to poly
			if (arg0.getButton() == MouseEvent.BUTTON1) {
				g.setColor(Color.GREEN);
				if (currentPolygon.size() != 0) {
					Point lastVertex = currentPolygon.get(currentPolygon.size() - 1);
					g.drawLine(lastVertex.getX(), lastVertex.getY(), x, y);
				}
				g.fillOval(x-5,y-5,10,10);
				
				currentPolygon.add(new Point(x,y));
				System.out.println(x + " " + y);
			}
			ShowImage();
			/*for(ArrayList<Point> polygon : polygonsList.values()) {
				drawPolygon(polygon);
				finishPolygon(polygon);
			}*/
			drawPolygon(currentPolygon);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		isPointSelected = false;
	}
	

	@Override
	public void mouseMoved(MouseEvent arg0) {
		//Point p = new Point(arg0.getX(), arg0.getY());
		//pointIndex = getNearestControlPoint(p);
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0){
		Point p = null;
		
		int x = arg0.getX();
		int y = arg0.getY();
		
		//if dragging an existing point
		if (isPointSelected) {
			if(arg0.getX() > image.getWidth() || arg0.getX() < 0)
				x = currentPolygon.get(pointIndex).getX();
			
			if(arg0.getY() > image.getHeight() || arg0.getY() < 0)
				y = currentPolygon.get(pointIndex).getY();
			
			p = new Point(x,y);
			
        	currentPolygon.set(pointIndex, p);
        }
		//if dragging a newly added point (mouse button has not been released yet)
		else{
			if(arg0.getX() > image.getWidth() || arg0.getX() < 0)
				x = currentPolygon.get(currentPolygon.size()-1).getX();
			
			if(arg0.getY() > image.getHeight() || arg0.getY() < 0)
				y = currentPolygon.get(currentPolygon.size()-1).getY();
			
			p = new Point(x,y);
			
			currentPolygon.set(currentPolygon.size()-1, p);
		}
		
		ShowImage();
		drawPolygon(currentPolygon);
	}
	
	public int getNearestControlPoint(Point p) {
		int radius = 5;
        double best = (radius << 3) + 1;
        int bestInd = -1;
        for (int i = 0; i < currentPolygon.size(); i++) {
            int diffX = p.getX() - currentPolygon.get(i).getX();
            int diffY = p.getY() - currentPolygon.get(i).getY();
            double diff = diffX * diffX + diffY * diffY;
            if (best >= diff) {
                best = diff;
                bestInd = i;
            }
        }
        return bestInd;
    }	
}