package hci;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

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
	MyImage image = null;
	
	/**
	 * list of current polygon's vertices 
	 */
	MyPolygon currentPolygon = null;
	
	/**
	 * list of polygons
	 */
	Vector<MyPolygon> currentPolygonsList = null;
	
	Vector<MyImage> imagesList = null;
	
	Vector<MyPolygon> polygonsList = null;
	
	/**
	 * Flag showing if a control point is being selected
	 */
	
	boolean isPointSelected = false;
	
	boolean isEditing = false;
	
	int editingIndex = -1;
	
	String editingName = "";
	
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
		currentPolygon = new MyPolygon();
		currentPolygonsList = new Vector<MyPolygon>();
		polygonsList = new Vector<MyPolygon>();
		imagesList = new Vector<MyImage>();

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
		File imgFile = new File(imageName);
		image = new MyImage(ImageIO.read(imgFile), imgFile.getName());
		//uneditedImage = HelperMethods.copyImage(image);
		/*if (image.getImage().getWidth() > 800 || image.getImage().getHeight() > 600) {
			int newWidth = image.getImage().getWidth() > 800 ? 800 : (image.getImage().getWidth() * 600)/image.getImage().getHeight();
			int newHeight = image.getImage().getHeight() > 600 ? 600 : (image.getImage().getHeight() * 800)/image.getImage().getWidth();
			System.out.println("SCALING TO " + newWidth + "x" + newHeight );
			Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
			image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(scaledImage, 0, 0, this);
		}*/
	}
	
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		if(image != null){
			BufferedImage img = image.getOriginalImage();
			Graphics2D g2D = img.createGraphics();
					
			for(MyPolygon polygon : currentPolygonsList){
				drawPolygon(polygon.getPoints(), g2D, false);
				finishPolygon(polygon.getPoints(), g2D);
			}
			
			//display current polygon
			drawPolygon(currentPolygon.getPoints(), g2D, true);
			
			//show the image
			if(img != null){
				g.drawImage(img, 0, 0, null);
			}
		}
	}
	
	/**
	 * displays a polygon without last stroke
	 * @param polygon to be displayed
	 */
	public void drawPolygon(ArrayList<MyPoint> polygon, Graphics2D g, boolean isCurrent) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(isCurrent)
			g.setColor(Color.BLUE);
		else
			g.setColor(Color.GREEN);
		for(int i = 0; i < polygon.size(); i++) {
			MyPoint currentVertex = polygon.get(i);
			if (i != 0) {
				MyPoint prevVertex = polygon.get(i - 1);
				g.drawLine(prevVertex.getX(), prevVertex.getY(), currentVertex.getX(), currentVertex.getY());
			}
			g.fillOval(currentVertex.getX() - 5, currentVertex.getY() - 5, 10, 10);
		}
	}
	
	/**
	 * displays last stroke of the polygon (arch between the last and first vertices)
	 * @param polygon to be finished
	 */
	public void finishPolygon(ArrayList<MyPoint> polygon, Graphics2D g) {
		//if there are less than 3 vertices than nothing to be completed
		if (polygon.size() >= 3) {
			MyPoint firstVertex = polygon.get(0);
			MyPoint lastVertex = polygon.get(polygon.size() - 1);
		
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.GREEN);
			g.drawLine(firstVertex.getX(), firstVertex.getY(), lastVertex.getX(), lastVertex.getY());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3 && currentPolygon != null && currentPolygon.getPoints().size() > 0) {
			
			if(isEditing){
				currentPolygonsList.add(editingIndex, new MyPolygon(currentPolygon.getPoints(),editingName,image.getName()));
				currentPolygon = new MyPolygon();
				isEditing = false;
				editingIndex = -1;
				editingName = "";				
			}
			else{
				addPolygon("Please enter name for the polygon");
			}
			
			repaint();
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
		if (x > image.getImage().getWidth() || y > image.getImage().getHeight()) {
			//if not do nothing
			return;
		}
		
		MyPoint p = new MyPoint(arg0.getX(), arg0.getY());
		
		pointIndex = getNearestControlPoint(p);
		
		//if pressed on an existing point
		if(pointIndex >= 0 && currentPolygon.getPoints().size() > 0){
			isPointSelected = true;
		}
		
		//if drawing a new point
		else {
			
			//if the left button than we will add a vertex to poly
			if (arg0.getButton() == MouseEvent.BUTTON1) {				
				currentPolygon.getPoints().add(new MyPoint(x,y));
			}
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		isPointSelected = false;
	}
	

	@Override
	public void mouseMoved(MouseEvent arg0) {
		parent.pointLabel.setText("X: " + arg0.getX() + " Y:" + arg0.getY());
	}
	
	@Override
	public void mouseDragged (MouseEvent arg0){
		MyPoint p = null;
		
		int x = arg0.getX();
		int y = arg0.getY();
		
		//if dragging an existing point
		if (isPointSelected) {
			if(arg0.getX() > image.getImage().getWidth() || arg0.getX() < 0)
				x = currentPolygon.getPoints().get(pointIndex).getX();
			
			if(arg0.getY() > image.getImage().getHeight() || arg0.getY() < 0)
				y = currentPolygon.getPoints().get(pointIndex).getY();
			
			p = new MyPoint(x,y);
			
        	currentPolygon.getPoints().set(pointIndex, p);
        }
		//if dragging a newly added point (mouse button has not been released yet)
		else{
			if(arg0.getX() > image.getImage().getWidth() || arg0.getX() < 0)
				x = currentPolygon.getPoints().get(currentPolygon.getPoints().size()-1).getX();
			
			if(arg0.getY() > image.getImage().getHeight() || arg0.getY() < 0)
				y = currentPolygon.getPoints().get(currentPolygon.getPoints().size()-1).getY();
			
			p = new MyPoint(x,y);
			
			currentPolygon.getPoints().set(currentPolygon.getPoints().size()-1, p);
		}
		
		repaint();
	}
	
	public int getNearestControlPoint(MyPoint p) {
		int radius = 5;
        double best = (radius << 3) + 1;
        int bestInd = -1;
        for (int i = 0; i < currentPolygon.getPoints().size(); i++) {
            int diffX = p.getX() - currentPolygon.getPoints().get(i).getX();
            int diffY = p.getY() - currentPolygon.getPoints().get(i).getY();
            double diff = diffX * diffX + diffY * diffY;
            if (best >= diff) {
                best = diff;
                bestInd = i;
            }
        }
        return bestInd;
    }
	
	public void addPolygon(String message){
		
		String polygonName = JOptionPane.showInputDialog(this.getParent(),
				message,
				null,
				JOptionPane.PLAIN_MESSAGE);
		
		if(polygonName == null || polygonName.equals("")){
			addPolygon("Polygon name must not be empty. Enter name again.");
		}
		
		for(MyPolygon p : currentPolygonsList){
			if(p.getName().equals((polygonName))){
				addPolygon("Polygon name already exist. Enter a different name.");
			}
		}
		
		currentPolygonsList.add(new MyPolygon(currentPolygon.getPoints(),polygonName, image.getName()));

		currentPolygon = new MyPolygon();
		int insertPosition = parent.labelsListModel.getSize();
		parent.labelsListModel.add(insertPosition, polygonName);
	}
	
	public void editPolygon(int polygonIndex){
		//finish the current polygon first
		if(currentPolygon != null && currentPolygon.getPoints().size() > 0){
			addPolygon("Please enter name for the current polygon");
		}
		
		MyPolygon editingPolygon = currentPolygonsList.get(polygonIndex);
		currentPolygon = editingPolygon;
		currentPolygonsList.remove(polygonIndex);
		
		editingIndex = polygonIndex;
		editingName = editingPolygon.getName();
		isEditing = true;
		repaint();
	}
	
	public void renamePolygon(int polygonIndex, String renameMessage){
		
		String polygonName = JOptionPane.showInputDialog(this.getParent(),
				renameMessage,
				null,
				JOptionPane.PLAIN_MESSAGE);
		
		if(polygonName == null || polygonName.equals("")){
			renamePolygon(polygonIndex, "Polygon name must not be empty. Enter name again.");
		}
		
		for(MyPolygon p : currentPolygonsList){
			if(p.getName().equals((polygonName))){
				renamePolygon(polygonIndex, "Polygon name already exist. Enter a different name.");
			}
		}
		
		currentPolygonsList.get(polygonIndex).setName(polygonName);
		parent.labelsListModel.set(polygonIndex, polygonName);
	}
	
	public void deletePolygon(int polygonIndex){
		String name = currentPolygonsList.get(polygonIndex).getName();
		currentPolygonsList.remove(polygonIndex);
		parent.labelsListModel.remove(polygonIndex);
		
		
		MyPolygon polygonToRemove = null;
		for(MyPolygon p : polygonsList){
			if(p.getName().equals(name) && p.getImageName().equals(image.getName())){
				polygonToRemove = p;
			}
		}
		
		polygonsList.remove(polygonToRemove);
		
		repaint();
	}
	
	public void loadNewImage(File newImage) throws IOException{
		
		if(currentPolygon.getPoints().size() > 0){
			addPolygon("Please enter name for the polygon");
		}
		
		for(MyPolygon p: currentPolygonsList){
			if(!polygonsList.contains(p))
				polygonsList.add(p);
		}
		
		currentPolygonsList.clear();
		
		image = new MyImage(ImageIO.read(newImage), newImage.getName());
		imagesList.add(image);
		
		parent.labelsListModel.clear();
		
		parent.imagesListModel.addElement(newImage.getName().length() > 28 ? newImage.getName().substring(0, 28) + "..." : newImage.getName());
		parent.imagesListModel.setSelectedItem(newImage.getName());
		repaint();
	}
	
	public void changeCurrentImage(String imageName){
		if(currentPolygon.getPoints().size() > 0){
			addPolygon("Please enter name for the polygon");
		}
		
		/*for(MyPolygon p : polygonsList){
			System.out.print(p.getName() + " ");
		}System.out.println();*/
		
		for(MyPolygon p: currentPolygonsList){
			if(!polygonsList.contains(p))
				polygonsList.add(p);
		}
		
		currentPolygonsList.clear();
		
		for(MyImage i : imagesList){
			if(i.getName().equals(imageName)){
				image = i;
			}
		}
		
	    parent.labelsListModel.clear();
		
	    int index = 0;
		for(MyPolygon p : polygonsList){
			if(p.getImageName().equals(imageName)){
				currentPolygonsList.add(p);
				parent.labelsListModel.add(index, p.getName());
				index++;
			}
		}
		
		repaint();
	}
}