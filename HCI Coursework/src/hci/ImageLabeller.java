package hci;

import hci.utils.MyImage;
import hci.utils.MyPoint;
import hci.utils.MyPolygon;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
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
 * @author Bogdan Georgios Michal
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
	JButton renameLabelButton = null;
	JButton editLabelButton = null;
	JButton deleteLabelButton = null;
	JButton saveLabelsButton = null;
	JButton loadLabelsButton = null;
	JButton closeImageButton = null;
	JComboBox changeImageDropdown = null;
	JButton openImageButton = null;
	DefaultListModel labelsListModel = null;
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
		
	public void setupGUI() throws Exception {
		this.addWindowListener(new WindowAdapter() {
		  	public void windowClosing(WindowEvent event) {
		  		if(imagePanel.image != null){
		  		
			  		int result = JOptionPane.showConfirmDialog(ImageLabeller.this, "Do you want to save the labels for this image?", "Save labels for image", JOptionPane.YES_NO_OPTION);
					
					if(result == JOptionPane.YES_OPTION){
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
		  		}
		    	System.exit(0);
		  	}
		});

		//setup main window panel
		appPanel = new JPanel();
		helpLabel = new JLabel("Left click to draw polygon. Right click to finish polygon.");
		pointLabel = new JLabel("X,Y of mouse");
		labelsListModel = new DefaultListModel();
		imagesListModel = new DefaultComboBoxModel();
		labelList = new JList(labelsListModel);
		fc = new JFileChooser();
		renameLabelButton = new JButton("Rename");
		editLabelButton = new JButton("Edit");
		deleteLabelButton = new JButton("Delete");
		saveLabelsButton = new JButton("Save");
		loadLabelsButton = new JButton("Load");
		closeImageButton = new JButton("Remove image from list");
		changeImageDropdown = new JComboBox(imagesListModel);
		openImageButton = new JButton("Add image to list");

		this.setLayout(new BoxLayout(appPanel, BoxLayout.X_AXIS));
		this.setContentPane(appPanel);
		
        //Create and set up the image panel.
		imagePanel = new ImagePanel(this);
		imagePanel.setOpaque(true); //content panes must be opaque
		

        //create toolbox panel
        toolboxPanel = new JPanel();
        //create imgbox panel
        imgboxPanel = new JPanel();
        rightPanel = new JPanel();

       
        
        //Add Add button
		renameLabelButton.setMnemonic(KeyEvent.VK_N);
		renameLabelButton.setSize(50, 20);
		renameLabelButton.setEnabled(true);
		renameLabelButton.setToolTipText("Click to Add labels");
		renameLabelButton.setIcon(new ImageIcon("./Rename.png"));
		renameLabelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int polygonIndex = (int) labelList.getSelectedIndex();
			    imagePanel.renamePolygon(polygonIndex, "Enter new name for the polygon");
			}
		});
		
        //Add Edit button
		editLabelButton.setMnemonic(KeyEvent.VK_N);
		editLabelButton.setSize(50, 20);
		editLabelButton.setEnabled(true);
		editLabelButton.setToolTipText("Click to edit the selected label");
		editLabelButton.setIcon(new ImageIcon("./Edit.png"));
		editLabelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int polygonIndex = (int) labelList.getSelectedIndex();
			    imagePanel.editPolygon(polygonIndex);
			}
		});
		
        //Add Delete button
		deleteLabelButton.setMnemonic(KeyEvent.VK_N);
		deleteLabelButton.setSize(50, 20);
		deleteLabelButton.setEnabled(true);
		deleteLabelButton.setToolTipText("Click to delete the selected labels");
		deleteLabelButton.setIcon(new ImageIcon("./Close.png"));
		deleteLabelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int polygonIndex = (int) labelList.getSelectedIndex();
			    imagePanel.deletePolygon(polygonIndex);
			}
		});
		
		
        //Add Save button
		saveLabelsButton.setMnemonic(KeyEvent.VK_N);
		saveLabelsButton.setSize(50, 20);
		saveLabelsButton.setEnabled(true);
		saveLabelsButton.setToolTipText("Click to save the current labels");
		saveLabelsButton.setIcon(new ImageIcon("./Save.png"));
		saveLabelsButton.addActionListener(new ActionListener() {
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
		loadLabelsButton.setMnemonic(KeyEvent.VK_N);
		loadLabelsButton.setSize(50, 20);
		loadLabelsButton.setEnabled(true);
		loadLabelsButton.setToolTipText("Click to load labels");
		loadLabelsButton.setIcon(new ImageIcon("./Open.png"));
		loadLabelsButton.addActionListener(new ActionListener() {
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
								String polygonName = "";
								while((currentLine = br.readLine()) != null){
								if(result == JOptionPane.OK_OPTION){
										
										if(isPolygonName){
											labelsListModel.add(labelsListModel.getSize(), currentLine);
											polygonName = currentLine;
										}
										else{
											String[] points = currentLine.split(";");
																						
											ArrayList<MyPoint> polygonPoints = new ArrayList<MyPoint>();
											
											for(String point : points){
												String[] pointXY = point.split(",");
												polygonPoints.add(new MyPoint(Integer.parseInt(pointXY[0]), Integer.parseInt(pointXY[1])));
											}
											
											imagePanel.currentPolygonsList.add(new MyPolygon(polygonPoints, polygonName, imagePanel.image.getName()));
											
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
		closeImageButton.setMnemonic(KeyEvent.VK_N);
		closeImageButton.setSize(50, 20);
		closeImageButton.setEnabled(true);
		closeImageButton.setToolTipText("Click to Close Image");
		closeImageButton.setIcon(new ImageIcon("./Close.png"));
		closeImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(ImageLabeller.this, "Do you want to save the labels for this image?", "Save labels for image", JOptionPane.YES_NO_OPTION);
			
				if(result == JOptionPane.YES_OPTION){
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
				
				labelsListModel.clear();
				imagePanel.currentPolygonsList.clear();
				
				String imageName = imagePanel.image.getName();

				imagesListModel.removeElement(imagePanel.image.getName());
				
				if(imagesListModel.getSize() > 0){
					imagesListModel.setSelectedItem(imagesListModel.getElementAt(0));
				}
				
				for(MyImage i : imagePanel.imagesList){
					if(i.getName().equals(imageName)){
						imagePanel.imagesList.remove(i);
						break;
					}
				}
				
				if(imagePanel.imagesList.size() > 0){
					imagePanel.image = imagePanel.imagesList.firstElement();
				}
				else {
					imagePanel.image = null;
				}
				
				repaint();				
			}
		});
		
        //Add Chose Image button
		changeImageDropdown.setSize(100, 20);
		changeImageDropdown.setEnabled(true);
		changeImageDropdown.setToolTipText("Choose an image file.");
		changeImageDropdown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(loadingNewImage)
					loadingNewImage = false;
				else
				 imagePanel.changeCurrentImage((String) imagesListModel.getSelectedItem());
			}
		});
		

        //Add Open Close button
		openImageButton.setMnemonic(KeyEvent.VK_N);
		openImageButton.setSize(50, 20);
		openImageButton.setEnabled(true);
		openImageButton.setToolTipText("Click to Open Image");
		openImageButton.setIcon(new ImageIcon("./Open.png"));
		openImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fc.showOpenDialog(ImageLabeller.this);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
				        File file = fc.getSelectedFile();
				        try {
				        	loadingNewImage = true;
				        	boolean imageAlreadyLoaded = false;
				        	
				        	for(MyImage i : imagePanel.imagesList){
				        		if(i.getName().equals(file.getName()))
				        			imageAlreadyLoaded = true;
				        	}
				        	
				        	if(imageAlreadyLoaded){
				        		imagePanel.changeCurrentImage(file.getName());
				        		imagesListModel.setSelectedItem(file.getName());
				        	}
				        	else
				        		imagePanel.loadNewImage(file);
				        	
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				    }
			}
		});
		
		imgboxPanel.setLayout(new GridLayout(0,3));
		imgboxPanel.add(changeImageDropdown);
		imgboxPanel.add(openImageButton);
		imgboxPanel.add(closeImageButton);
		toolboxPanel.setLayout(new GridLayout(5,0));
		toolboxPanel.add(renameLabelButton);
		toolboxPanel.add(editLabelButton);
		toolboxPanel.add(deleteLabelButton);
		toolboxPanel.add(loadLabelsButton);
		toolboxPanel.add(saveLabelsButton);
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
		gridBag.weightx = 0.2;
		gridBag.gridx = 0;
		gridBag.gridy = 2;
		appPanel.add(helpLabel, gridBag);
		
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.weightx = 0.8;
		gridBag.gridx = 3;
		gridBag.gridy = 2;
		appPanel.add(pointLabel, gridBag);
		
		
		//display all the stuff
		this.pack();
        this.setVisible(true);
        this.setResizable(false);
	}
	
	
	
	/**
	 * Runs the program
	 * @param argv path to an image
	 */
	public static void main(String argv[]) {
		try {
			//create a window and display the image
			ImageLabeller window = new ImageLabeller();
			window.setupGUI();
		} catch (Exception e) {
			System.err.println("Image: " + argv[0]);
			e.printStackTrace();
		}
	}
}