package hci;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
	
	/**
	 * toolbox - put all buttons and stuff here!
	 */
	JPanel toolboxPanel = null;
	
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
		imagePanel.addNewPolygon();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		imagePanel.paint(g); //update image panel
	}
	
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



		this.setLayout(new BoxLayout(appPanel, BoxLayout.X_AXIS));
		this.setContentPane(appPanel);
		
        //Create and set up the image panel.
		imagePanel = new ImagePanel(imageFilename);
		imagePanel.setOpaque(true); //content panes must be opaque
		

        //create toolbox panel
        toolboxPanel = new JPanel();
        //create imgbox panel
        imgboxPanel = new JPanel();

       
        
        //Add Add button
		JButton newAddButton = new JButton("Add");
		newAddButton.setMnemonic(KeyEvent.VK_N);
		newAddButton.setSize(50, 20);
		newAddButton.setEnabled(true);
		newAddButton.setToolTipText("Click to Add labels");
		
        //Add Edit button
		JButton newEditButton = new JButton("Edit");
		newEditButton.setMnemonic(KeyEvent.VK_N);
		newEditButton.setSize(50, 20);
		newEditButton.setEnabled(true);
		newEditButton.setToolTipText("Click to edit the selected label");
		
        //Add Delete button
		JButton newDeleteButton = new JButton("Delete");
		newDeleteButton.setMnemonic(KeyEvent.VK_N);
		newDeleteButton.setSize(50, 20);
		newDeleteButton.setEnabled(true);
		newDeleteButton.setToolTipText("Click to delete the selected labels");
		
		
        //Add Save button
		JButton newSaveButton = new JButton("Save");
		newSaveButton.setMnemonic(KeyEvent.VK_N);
		newSaveButton.setSize(50, 20);
		newSaveButton.setEnabled(true);
		newSaveButton.setToolTipText("Click to save the current labels");
		
        //Add Load button
		JButton newLoadButton = new JButton("Load");
		newLoadButton.setMnemonic(KeyEvent.VK_N);
		newLoadButton.setSize(50, 20);
		newLoadButton.setEnabled(true);
		newLoadButton.setToolTipText("Click to load labels");
		
        //Add Open Close button
		JButton newOpenButton = new JButton("+");
		newOpenButton.setMnemonic(KeyEvent.VK_N);
		newOpenButton.setSize(50, 20);
		newOpenButton.setEnabled(true);
		
        //Add Chose Close button
		JButton newCloseButton = new JButton("X");
		newCloseButton.setMnemonic(KeyEvent.VK_N);
		newCloseButton.setSize(50, 20);
		newCloseButton.setEnabled(true);
		newCloseButton.setToolTipText("Click to Close Image");
		
        //Add Chose Image button
		JButton newChoseButton = new JButton("Img Name");
		newChoseButton.setMnemonic(KeyEvent.VK_N);
		newChoseButton.setSize(50, 20);
		newChoseButton.setEnabled(true);
		newChoseButton.setToolTipText("Click to Select Image");
		
		
		
		newOpenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    	addNewPolygon();
			}
		});
		newOpenButton.setToolTipText("Click to Open Image");
		imgboxPanel.setLayout(new GridLayout(0,3));
		imgboxPanel.add(newChoseButton);
		imgboxPanel.add(newCloseButton);
		imgboxPanel.add(newOpenButton);
		toolboxPanel.setLayout(new GridLayout(5,0));
		toolboxPanel.add(newAddButton);
		toolboxPanel.add(newEditButton);
		toolboxPanel.add(newDeleteButton);
		toolboxPanel.add(newLoadButton);
		toolboxPanel.add(newSaveButton);
		
		//add toolbox to window
        //appPanel.add(imagePanel);
		//appPanel.add(imgboxPanel);
		//appPanel.add(toolboxPanel);
		
		appPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBag = new GridBagConstraints();
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.weightx = 0.5;
		gridBag.gridx = 3;
		gridBag.gridy = 1;
		appPanel.add(toolboxPanel, gridBag);
		
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.weightx = 0.5;
		gridBag.gridx = 2;
		gridBag.gridy = 0;
		appPanel.add(imgboxPanel, gridBag);
		
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.ipady = 40;
		gridBag.weightx = 0.0;
		gridBag.gridwidth = 3;
		gridBag.gridx = 0;
		gridBag.gridy = 1;
		appPanel.add(imagePanel, gridBag);

		
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