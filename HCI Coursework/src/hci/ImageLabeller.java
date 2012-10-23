package hci;

import hci.utils.MyPoint;
import hci.utils.MyPolygon;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main class of the program - handles display of the main window
 * @author Michal
 *
 */
public class ImageLabeller extends JFrame {
	/**
	 * some java stuff to get rid of warnings
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * main window panel
	 */
	JPanel appPanel = null;
	JLabel helpLabel =null;
	JLabel pointLabel  =null;
	JList labelList  =null;
	JButton newAddButton = null;
	JButton newEditButton = null;
	JButton newDeleteButton = null;
	JButton newSaveButton = null;
	JButton newLoadButton = null;
	JButton newCloseButton = null;
	JComboBox newChooseButton = null;
	JButton newOpenButton = null;
	DefaultListModel listModel = null;
	DefaultComboBoxModel imagesListModel = null;
	JFileChooser fc = null;
	boolean loadingNewImage = false;
	
	/**
	 * toolbox - put all buttons and stuff here!
	 */
	JPanel toolboxPanel = null;
	JPanel rightPanel = null;

	
	/**
	 * imgbox - put all buttons and stuff here!
	 */
	JPanel imgboxPanel = null;
	
	/**
	 * image panel - displays image and editing area
	 */
	ImagePanel imagePanel = null;
	
	/**
	 * handles New Object button action
	 */
	public void addNewPolygon() {
		//imagePanel.addNewPolygon();
	}
	
	/*@Override
	public void paint(Graphics g) {
		super.paint(g);
		appPanel.paint(g); //update image panel
	}*/
	
	/**
	 * sets up application window
	 * @param imageFilename image to be loaded for editing
	 * @throws Exception
	 */
	public void setupGUI(String imageFilename) throws Exception {
		this.addWindowListener(new WindowAdapter() {
		  	public void windowClosing(WindowEvent event) {
		  		//here we exit the program (maybe we should ask if the user really wants to do it?)
		  		//maybe we also want to store the polygons somewhere? and read them next time
		  		System.out.println("Bye bye!");
		    	System.exit(0);
		  	}
		});

		//setup main window panel
		appPanel = new JPanel();
		helpLabel = new JLabel("Put help here.");
		pointLabel = new JLabel("X,Y of mouse");
		listModel = new DefaultListModel();
		imagesListModel = new DefaultComboBoxModel();
		labelList = new JList(listModel);
		fc = new JFileChooser();
		newAddButton = new JButton("Add");
		newEditButton = new JButton("Edit");
		newDeleteButton = new JButton("Delete");
		newSaveButton = new JButton("Save");
		newLoadButton = new JButton("Load");
		newCloseButton = new JButton("Remove image from list");
		newChooseButton = new JComboBox(imagesListModel);
		newOpenButton = new JButton("Add image to list");

		this.setLayout(new BoxLayout(appPanel, BoxLayout.X_AXIS));
		this.setContentPane(appPanel);
		
        //Create and set up the image panel.
		imagePanel = new ImagePanel(this);//, imageFilename);
		imagePanel.setOpaque(true); //content panes must be opaque
		

        //create toolbox panel
        toolboxPanel = new JPanel();
        //create imgbox panel
        imgboxPanel = new JPanel();
        rightPanel = new JPanel();

       
        
        //Add Add button
		newAddButton.setMnemonic(KeyEvent.VK_N);
		newAddButton.setSize(50, 20);
		newAddButton.setEnabled(true);
		newAddButton.setToolTipText("Click to Add labels");
		
        //Add Edit button
		newEditButton.setMnemonic(KeyEvent.VK_N);
		newEditButton.setSize(50, 20);
		newEditButton.setEnabled(true);
		newEditButton.setToolTipText("Click to edit the selected label");
		newEditButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int polygonIndex = (int) labelList.getSelectedIndex();
			    imagePanel.editPolygon(polygonIndex);
			}
		});
		
        //Add Delete button
		newDeleteButton.setMnemonic(KeyEvent.VK_N);
		newDeleteButton.setSize(50, 20);
		newDeleteButton.setEnabled(true);
		newDeleteButton.setToolTipText("Click to delete the selected labels");
		newDeleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int polygonIndex = (int) labelList.getSelectedIndex();
			    imagePanel.deletePolygon(polygonIndex);
			}
		});
		
		
        //Add Save button
		newSaveButton.setMnemonic(KeyEvent.VK_N);
		newSaveButton.setSize(50, 20);
		newSaveButton.setEnabled(true);
		newSaveButton.setToolTipText("Click to save the current labels");
		newSaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fc.showSaveDialog(ImageLabeller.this);
								
				if (returnValue == JFileChooser.APPROVE_OPTION) {
				        File file = fc.getSelectedFile();
				        file.setWritable(true);
				        
				        try {
							BufferedWriter br = new BufferedWriter(new FileWriter(file));
							
							br.write(imagePanel.image.getName() + "\n");
							for(MyPolygon p : imagePanel.currentPolygonsList){
								br.write(p.getName() + "\n");
								for(MyPoint pt : p.getPoints()){
									br.write(pt.getX() + "," + pt.getY() + ";");
								}
								br.write("\n");
							}
							
							br.flush();
							br.close();
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				    }
			}
		});
		
        //Add Load button
		newLoadButton.setMnemonic(KeyEvent.VK_N);
		newLoadButton.setSize(50, 20);
		newLoadButton.setEnabled(true);
		newLoadButton.setToolTipText("Click to load labels");
		newLoadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fc.showOpenDialog(ImageLabeller.this);
								
				if (returnValue == JFileChooser.APPROVE_OPTION) {
				        File file = fc.getSelectedFile();
				        file.setWritable(true);
				        
				        try {
							BufferedReader br = new BufferedReader(new FileReader(file));
							String currentLine;

								int result = JOptionPane.OK_OPTION;
								if(((currentLine = br.readLine()) != null) && !currentLine.equals(imagePanel.image.getName())){
									
									result = JOptionPane.showConfirmDialog(ImageLabeller.this, "This set of labels is for a different image. Are you sure you want to load it?", "Label set belongs to different image", JOptionPane.OK_CANCEL_OPTION);
								}
								
								boolean isPolygonName = true;
								while((currentLine = br.readLine()) != null){
								if(result == JOptionPane.OK_OPTION){
										
										if(isPolygonName){
											listModel.add(listModel.getSize(), currentLine);
										}
										else{
											String[] points = currentLine.split(";");
											
											//System.out.println(points.length);
											//for(String s : points){System.out.println(s);}
											
											ArrayList<MyPoint> polygonPoints = new ArrayList<MyPoint>();
											
											for(String point : points){
												String[] pointXY = point.split(",");
												polygonPoints.add(new MyPoint(Integer.parseInt(pointXY[0]), Integer.parseInt(pointXY[1])));
											}
											
											imagePanel.currentPolygonsList.add(new MyPolygon(polygonPoints));
											
										}
										
										isPolygonName = !isPolygonName;
								}
							}
							
							br.close();
							imagePanel.repaint();
							
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch ( IOException e2) {
							e2.printStackTrace();
						}
				    }
			}
		});
		
		
        //Add Chose Close button
		newCloseButton.setMnemonic(KeyEvent.VK_N);
		newCloseButton.setSize(50, 20);
		newCloseButton.setEnabled(true);


		newCloseButton.setToolTipText("Click to Close Image");
		
        //Add Chose Image button
		newChooseButton.setSize(100, 20);
		newChooseButton.setEnabled(true);
		newChooseButton.setToolTipText("Choose an image file.");
		newChooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(loadingNewImage)
					loadingNewImage = false;
				else
				 imagePanel.changeCurrentImage((String) imagesListModel.getSelectedItem());
			}
		});
		

        //Add Open Close button
		newOpenButton.setMnemonic(KeyEvent.VK_N);
		newOpenButton.setSize(50, 20);
		newOpenButton.setEnabled(true);
		newOpenButton.setToolTipText("Click to Open Image");
		newOpenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fc.showOpenDialog(ImageLabeller.this);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
				        File file = fc.getSelectedFile();
				        try {
				        	loadingNewImage = true;
							imagePanel.loadNewImage(file);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				    }
			}
		});
		
		imgboxPanel.setLayout(new GridLayout(0,3));
		imgboxPanel.add(newChooseButton);
		imgboxPanel.add(newOpenButton);
		imgboxPanel.add(newCloseButton);
		toolboxPanel.setLayout(new GridLayout(5,0));
		//toolboxPanel.add(newAddButton);
		toolboxPanel.add(newEditButton);
		toolboxPanel.add(newDeleteButton);
		toolboxPanel.add(newLoadButton);
		toolboxPanel.add(newSaveButton);
		rightPanel.setLayout(new GridLayout(2,0));
		rightPanel.add(labelList);
		rightPanel.add(toolboxPanel);
				
		appPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBag = new GridBagConstraints();
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.weightx = 0.5;
		gridBag.gridx = 3;
		gridBag.gridy = 1;
		appPanel.add(rightPanel, gridBag);
		
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.weightx = 0.5;
		gridBag.gridx = 2;
		gridBag.gridy = 0;
		appPanel.add(imgboxPanel, gridBag);
		
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.gridwidth = 3;
		gridBag.gridx = 0;
		gridBag.gridy = 1;
		appPanel.add(imagePanel, gridBag);

		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.weightx = 0.5;
		gridBag.gridx = 0;
		gridBag.gridy = 2;
		appPanel.add(pointLabel, gridBag);
		
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.weightx = 0.5;
		gridBag.gridx = 3;
		gridBag.gridy = 2;
		appPanel.add(helpLabel, gridBag);
		
		
		//display all the stuff
		this.pack();
        this.setVisible(true);
	}
	
	
	
	/**
	 * Runs the program
	 * @param argv path to an image
	 */
	public static void main(String argv[]) {
		try {
			//create a window and display the image
			ImageLabeller window = new ImageLabeller();
			window.setupGUI(argv[0]);
		} catch (Exception e) {
			System.err.println("Image: " + argv[0]);
			e.printStackTrace();
		}
	}
}