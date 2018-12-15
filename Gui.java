import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
public class Gui extends Frame implements ActionListener{
    private JPanel p1, p2, p3, p4, p5;
    private JLabel outputTitle, nameLabel, dependenciesLabel, durationlabel;
    private JTextField nameField, dependenciesField, durationField;
    private JTextArea ta, commentsArea;
    private JFrame jf;
    private JButton aboutButton, helpButton, addButton, computeButton, restartButton, quitButton, criticalPathButton, changeDurationButton, generateReportButton;
    private JScrollPane commentsScrollPane, outputScrollPane;
    
    private String aboutInfo = "CSE 360 Phase 2 \n\nVersion 2.0.0 \n\n2018-2018. Group 1.\nKathryn Rawn, Mohit Doshi, Wesley Guerra and Shubham Mehta\n\nAll Rights Reserved.";
    private String restartInfo = "Restarting...";
    private String quitInfo = "Quitting...";
    private String computeInfo = "Successfully Computed!";
    private String durationInfo = "Duration Changed!";
    private String generateReportInfo = "Report Successfully Generated!"; 
    private String helpInfo = "ABOUT\n"
   		+ "The following program analyzes a network diagram given by the\n"
   		+ "user and determines all of the paths in the network. It also\n "
   		+ "gives the option to display all of the critical paths, change\n"
   		+ "the duration for an activity, and save all of the paths and\n"
   		+ "activities into a user-defined text file.\n"
   		+ "To learn more about the project and the team, press the ‘About’ button.\n\n"
   		+ "ADDING AN ACTIVITY\n"
   		+ "To add an activity to the network diagram, enter the activity's name,\n"
   		+ "dependencies (if any), and duration within the corresponding text\n"
   		+ "fields. If the activity has more than one dependency, separate the\n"
   		+ "dependencies with a comma (Ex: A,B).  Once finished entering\n"
   		+ "activity information, press the ‘Add’ button. If all information is\n"
   		+ "inputted correctly, a ‘Successfully Added’ message should appear\n"
   		+ "below. The activity will not be added and an error message will\n"
   		+ "appear if any of the follow occur:\n"
   		+ "1) The activity is not given a name.\n"
   		+ "2) The activity is not given a duration.\n"
   		+ "3) The duration is not written as an integer\n"
   		+ "    (Ex: A duration of 4 should be entered as '4', NOT 'four')."
   		+ "\n\nVIEW PATHS\n"
   		+ "Once you have successfully added all of the activities in the network,\n"
   		+ "press 'Compute' to view all the paths listed in the ‘OUTPUT’ panel\n"
   		+ "in descending order by duration. An error message will be\n"
   		+ "displayed and the program will automatically restart if any of the\n"
   		+ "following occur:\n"
   		+ "1) There is a cycle in the given network.\n"
   		+ "2) All activities are not connected to a single endpoint."
   		+ "\n\nVIEW CRITICAL PATHS\n"
   		+ "When the Display Critical Paths Button is clicked, all\n"
   		+ "critical paths in the given network will be displayed in the output window."
   		+ "\n\nCHANGE DURATION\n"
   		+ "When the Change Duration Button is clicked, pop-up dialog windows will\n" 
   		+ "appear, to obtain an activity name and a new duration value.An error message\n "
   		+ "will be displayed if any of the following occur:\n"
   		+ "1) The activity name does not exist.\n"
   		+ "2) The duration is not written as an integer."
   		+ "\n\nGENERATE REPORT\n"
   		+ "A pop-up window will appear asking you to name the report you wish to create.\n"
   		+ "A text file will be created containing the report name, path and activity\n"
   		+ "information, along with date and time of creation." 
   		+ "\n\nRESTARTING\n"
   		+ "If at any time you need to start over or enter a new network diagram,\n"
   		+ "press the 'Restart' button. This will delete all previously entered\n"
   		+ "data."
   		+ "\n\nQUITTING\n"
   		+ "To exit the program at any time, press the 'Quit' button. This will\n"
   		+ "close the application.";
   
    public static void main(String[] args){
        new Gui();
    }
     
    
    public Gui(){
        jf = new JFrame("Phase 2");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLayout(new GridLayout(1, 2)); //for input and output
        p1 = new JPanel();	//input
        
        p1.setLayout(new GridLayout(7,2)); //for labels, text fields, buttons
        
        p2 = new JPanel();	//output
        p2.setLayout(new BorderLayout());
        
        p3 = new JPanel(); //p3 is a parent panel containing p1 and the successLabel
        p3.setLayout(new GridLayout(2,1)); //first row contains input information (p1), second row contains success message
        
        p4 = new JPanel(); // p4 is a child panel of p2 
        p4.setLayout(new GridLayout(1,2));
        
        p1.setBackground(Color.WHITE);
        p2.setBackground(Color.ORANGE);
        // p4.setBackground(Color.ORANGE);
   
        aboutButton = new JButton("About");
        helpButton = new JButton("Help");
        nameLabel = new JLabel("\tName: ");
        dependenciesLabel = new JLabel("\tDependencies: ");
        durationlabel = new JLabel("\tDuration: ");
        nameField = new JTextField();
        dependenciesField = new JTextField();
        durationField = new JTextField();
        addButton = new JButton("Add"); //was jb1
        computeButton = new JButton("Compute"); //was jb2
        restartButton = new JButton("Restart");
        quitButton = new JButton("Quit");
        criticalPathButton = new JButton("Display Critical Paths");
        changeDurationButton = new JButton("Change Duration");
        generateReportButton = new JButton("Generate Report");
/*      quitButton.setBackground(Color.ORANGE);
        quitButton.setOpaque(true); 
        quitButton.setBorderPainted(false);		SAMPLE CODE TO ADD COLOR TO BUTTONS	*/ 
        commentsArea = new JTextArea(5,20);
        commentsArea.setBackground(new Color(224,224,224));
        commentsArea.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        commentsArea.setEditable(false);
        addButton.addActionListener(this);
        computeButton.addActionListener(this);
        aboutButton.addActionListener(this);
        helpButton.addActionListener(this);
        restartButton.addActionListener(this);
        quitButton.addActionListener(this);
        criticalPathButton.addActionListener(this);
        changeDurationButton.addActionListener(this);
        generateReportButton.addActionListener(this);
        ta = new JTextArea(5,20);	//for actual output
        ta.setEditable(false);
        
        commentsScrollPane = new JScrollPane(commentsArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outputScrollPane = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        
       // p1.add(space[0]);
        p1.add(aboutButton);
        p1.add(helpButton);
        p1.add(nameLabel);
        p1.add(nameField);
        p1.add(dependenciesLabel);
        p1.add(dependenciesField);
        p1.add(durationlabel);
        p1.add(durationField);
        p1.add(addButton);
       // p1.add(space[1]);
        p1.add(computeButton);
        //p1.add(space[2]);
        p1.add(restartButton);
        p1.add(quitButton);
        p1.add(generateReportButton);
        generateReportButton.setEnabled(false);
        generateReportButton.setVisible(false);
        p3.add(p1);
        //p3.add(commentsArea);
        p3.add(commentsScrollPane);
        jf.add(p3);
        outputTitle = new JLabel("OUTPUT");
        outputTitle.setForeground(Color.BLACK);
        outputTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        p4.add(criticalPathButton);
        criticalPathButton.setEnabled(false);
        criticalPathButton.setVisible(false);
        p4.add(changeDurationButton);
        changeDurationButton.setEnabled(false);
        changeDurationButton.setVisible(false);
        
        p2.add(outputTitle, BorderLayout.NORTH);
        //p2.add(ta);
        p2.add(outputScrollPane);
        p2.add(p4,BorderLayout.SOUTH);        
        jf.add(p2);
        jf.setSize(850, 650);
        jf.setVisible(true);
    }
   // private class BListen implements ActionListener {
        public void actionPerformed(ActionEvent e) 
        {
        	String buttonClicked = e.getActionCommand();
            logic obj = new logic();
            boolean dcpflag = true;
            		//ArrayList<members> originals = new ArrayList<members>();
            //Add button pressed
            if(buttonClicked == "Add") 
            {
        		commentsArea.setText("");
            	boolean flag = true;
            	String name = nameField.getText();
            	String dependencies = dependenciesField.getText();
            	String duration = durationField.getText();
            	
            	if(name.equals("") || duration.equals(""))
            	{
            		commentsArea.setText("Invalid input, name and duration must have values!");
            		flag = false;
            	}
            	
            	//checks if duration is an integer
            	try
            	{
            		Integer.parseInt(duration);
            	} catch (NumberFormatException ex)
            	{
            		commentsArea.setText("Invalid input, duration must be an integer!");
            		flag = false;
            	}
            	
            	if(flag == true)
            	{
            		 String tmp[] = dependencies.split(",");
            		 String tempdependencies[] = dependencies.split(",");
            	     for(int j = 0; j < tmp.length; j++)
            	     {
            	    	 	tmp[j] = tmp[j].trim();
            	    	 	try{
            	    	 	tempdependencies[j] = "" + tmp[j].charAt(0);
            	    	 	}catch(StringIndexOutOfBoundsException err)
            	    	 	{
            	    	 		
            	    	 	}
            	     }
            		commentsArea.setText(name + " has been successfully added.");
            		String tempname = "" + name.charAt(0);
            		members toAdd = new members(tempname, tempdependencies, Integer.parseInt(duration));
            		
            		members toAddoriginals = new members(name,tmp,Integer.parseInt(duration));            		
            		
            		obj.addToArrayList(toAdd);
            		
            		obj.addToOriginalsArrayList(toAddoriginals);
            		nameField.setText("");
            		dependenciesField.setText("");
            		durationField.setText("");
            	}
                
                
            }
            
            //Compute button pressed
            if(buttonClicked == "Compute") 
            {
        		commentsArea.setText("");	
        		String val = obj.compute();
        		boolean flag = true;
        		String emc[] = {"Error: The network has more than one end point.\nProgram has been restarted.", "Error, incomplete connection found in the network. Restarting...", "Error: Incomplete connection found in the network.\nProgram has been restarted.", "Error: Cycle found in the network.\nProgram has been restarted."};
        		for(int i = 0; i < 3; i++)
        		{ 	
        			if(val.compareTo(emc[i]) == 0)
        			{
        			commentsArea.setText(val);
        			flag = false;
        			}
        		}
        		if(flag == true)
        		{
        			ta.setText(val);
        			commentsArea.setText(computeInfo);
        			//dcpflag = true;
        			criticalPathButton.setEnabled(true);
        	        criticalPathButton.setVisible(true);
        	        changeDurationButton.setEnabled(true);
        	        changeDurationButton.setVisible(true);
        	        generateReportButton.setEnabled(true);
        	        generateReportButton.setVisible(true);
        		}
        			
            }
            //Display Critical Paths Button pressed
            if(buttonClicked == "Display Critical Paths")
            {
            		if(dcpflag == true)
            		{
            			
            			//TEMPORARY CODE 
            			//String val = obj.compute();
            			//System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + val);
            			
                		System.out.println("Displ Critic button pressed");
            			String cpval = obj.computecriticalpath();
            			System.out.println(cpval);
            			ta.setText(cpval);
                		System.out.println("Im here");
            		}
            }
            
            if(buttonClicked == "Change Duration")
            {
        		commentsArea.setText("");
        		//ta.setText("");
            	JFrame frame = new JFrame("");
           /* 	Object[] options = {"Enter the Name of the Activity", "Enter the new duration"};
            JPanel panel = new JPanel();
            panel.add(new JLabel("Enter name of activity"));
            JTextField tf = new JTextField();
            panel.add(tf);
            
            JPanel panel2 = new JPanel();
            panel2.add(new JLabel("Enter the new duration of activity"));
            JTextField tf2 = new JTextField();
            panel.add(tf2);
            
            frame.add(panel);
            frame.add(panel2);*/
           
           	String nam = (String)JOptionPane.showInputDialog( frame, "Enter name of Activity", "Change Duration", JOptionPane.PLAIN_MESSAGE); //If a string was returned, say so. 
           	if ((nam != null) && (nam.length() > 0)) 
           	{ 
	           	if(obj.checkActivities(nam) == true)
	           	{
	               	String duration  = (String)(JOptionPane.showInputDialog( frame, "Enter duration of " + nam, "Change Duration", JOptionPane.PLAIN_MESSAGE)); //If a string was returned, say so. 
	               	if ((duration != null) && (duration.length() > 0))
	               	{
		               	try
		                	{
		                		Integer.parseInt(duration);
		                		if(obj.setduration(nam, Integer.parseInt(duration)))
			               			commentsArea.setText(durationInfo); 
		                	} 
		               	catch (NumberFormatException ex)
		                	{
		                		commentsArea.setText("Invalid input, duration must be an integer!");
		                	}
	               	}
	               	else
	               		commentsArea.setText("Invalid Duration Value");
	           	}
	            else
	          		 commentsArea.setText("Invalid Activity Name");
           	}
       	 else
       		 commentsArea.setText("Invalid Activity Name");
            }
            
            //About button pressed
            if(buttonClicked == "About")
            {
        		commentsArea.setText("");	
            	commentsArea.setText(aboutInfo);
            }
            
            //Help button pressed
            if(buttonClicked == "Help")
            {
        		commentsArea.setText("");	
        		commentsArea.setText(helpInfo);
            }
            
            //Restart button pressed
            if(buttonClicked == "Restart")
            {
            	ta.setText("");
        		commentsArea.setText("");	
        		commentsArea.setText(restartInfo);
        		obj.Restart();
        		ta.setText("");
        		commentsArea.setText("Restarted.");
        		nameField.setText("");
        		dependenciesField.setText("");
        		durationField.setText("");
        		criticalPathButton.setEnabled(false);
            criticalPathButton.setVisible(false);
            changeDurationButton.setEnabled(false);
            changeDurationButton.setVisible(false);
            generateReportButton.setEnabled(false);
            generateReportButton.setVisible(false);
            }
            
            // Quit button pressed
            if(buttonClicked == "Quit")
            {
        		commentsArea.setText("");
        		obj.Restart();
        		commentsArea.setText(quitInfo);
        		System.exit(0);
            }
            
            // Generate Report Button pressed
            if(buttonClicked == "Generate Report")
            {
            	 report robj = new report();
            	 //String val = obj.compute();
            
            	 // add the Alert View Controller
            	 JFrame frame = new JFrame("");
            	 String s = (String)JOptionPane.showInputDialog( frame, "Enter name of report file", "Generate Report", JOptionPane.PLAIN_MESSAGE); //If a string was returned, say so. 
            
	            	 if ((s != null) && (s.length() > 0)) 
	            	 { 
	            		 if(robj.genreport(s, obj.retactivities(), obj.compute()) == true)
	            			 commentsArea.setText(generateReportInfo);
	            	 }
	            	 else
	            		 commentsArea.setText("Report not Generated");
            	 }

        }
}