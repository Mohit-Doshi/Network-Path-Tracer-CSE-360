//importing java files required

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Date;

public class report {

    public boolean genreport(String title, String allActivities, String val)
    {

        try {

            //creating objects
            //reportsupport newObject = new reportsupport();
            Date date = new Date();

            //asking user for the file name
            //System.out.println("Please enter an output file: ");
            //Scanner s = new Scanner(System.in);
            String fileName = title;
            String str = fileName + ".txt" ;

            //creating the file
            PrintStream myconsole = new PrintStream( new File(str));

            //printing statements in the new file
            //System.out.println(newObject.consoleStr());
            System.setOut(myconsole);
            System.out.println("Title for the report is: " + fileName);
            System.out.println("\nDate and time of creation is: " + date.toString());
            System.out.println(" \nList of all activities in alphanumeric order with current duration: " + allActivities);
            System.out.println(" \nList of all paths with the activity names and total duration: \n" + val);
           // myconsole.print(newObject.printThis());
            
            return true;

        }

        catch (FileNotFoundException fx)
        {
            System.out.println(fx);
            return false;
        }

    }

}