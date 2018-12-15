import java.io.*;
import java.util.*;
import java.util.Scanner;
public class logic 
{
	public static int index = 0;
	public static String fullString = "";
	public static ArrayList<members> obj = new ArrayList<members>();
	public static ArrayList<members> originals = new ArrayList<members>();	// ArrayList of original names, dependencies, and duration
	public static String[] tempRet;
	public static String crit_path;
	
	public void addToArrayList(members object)	//abbreviated form
	{
		obj.add(object);
		for(int i = 0; i < obj.size(); i++)
		{
			for(int j = 0; j < obj.get(i).dependencies.length; j++)
				System.out.println(obj.get(i).name + " " + obj.get(i).dependencies[j] + " " + obj.get(i).duration);
		}
	}
	
	public void addToOriginalsArrayList(members object) //original form
	{
		originals.add(object);
	}
	
	public boolean checkActivities(String activity)
	{
		for(int i = 0; i < originals.size(); i++)
		{
			if(activity.compareTo(originals.get(i).name) == 0)
				return true;
		}
		
		return false;
	}
	
	public boolean setduration(String activity, int newduration)
	{
		if(originals.size() == obj.size())
		{
			for(int i = 0; i < originals.size(); i++)
			{
				if(activity.compareTo(originals.get(i).name) == 0)
				{
					originals.get(i).duration = newduration;
					obj.get(i).duration = newduration;
					return true;
				}
			}
		}
		return false;
	}
	
	public String retactivities()
	{
   	 	String allActivities = "\n\nName of Activity \t\t Duration \n";
   	 	ArrayList<String> nms = new ArrayList<String>();
   	 	for(int i = 0 ; i < originals.size(); i++)
   	 	{
   	 		nms.add(originals.get(i).name);	
   	 	}
   	    Collections.sort(nms);
   	    for(int i = 0; i < nms.size(); i++)
   	    {
   	    		int tempduration = 0;
   	    		
   	    		for(int j = 0; j < originals.size(); j++)
   	    		{
   	    			if(nms.get(i).compareTo(originals.get(j).name) == 0)
   	    				tempduration = originals.get(j).duration;
   	    		}
   	    		
   	    		allActivities += nms.get(i) + "\t\t\t\t" + tempduration + "\n";
   	    }
   	   // System.out.println(allActivities);
   	    return allActivities;
	}
	
	public String compute()
	{
		
		// maybe reinitialize tempret and crit_path each time this function is called?
			tempRet = null;
			crit_path = "";
			index = 0;
			fullString = "";
			
		
		String endpoint = FindEndPoint(obj);
		String returned[] = FindStartPoint(obj);
		
		//Start of logical error checking
		boolean cond = checkEndPoints(obj);
		if(cond == false)
		{
			Restart();
			return "Error, incomplete connection found in the network. Restarting...";
		}
		
		cond = checkStartPoints(obj);
		if(cond == false)
		{
			Restart();
			return "Error, incomplete connection found in the network. Restarting...";
		}
		
		int NUMOFPATHS = 0, cc = 0; //cc ; cycleCount. INITIALIZED TO 0
		
		for(int i = 0; i < obj.size(); i++)
		{
			NUMOFPATHS += obj.get(i).dependencies.length;
		}
		
		cond = checkCycle(obj, endpoint, NUMOFPATHS, cc);
		if(cond == true)
		{
			Restart();
			return "Error, cycle found in the network. Restarting...";
		}
		
		cond = checkConnection(obj, endpoint);
		if(cond == false)
		{
			Restart();
			return "Error, incomplete connection found in the network. Restarting...";
		}
		//End of logical error checking
		
		//Network is assumed to be error free at this point
		pathFinder(obj,endpoint);
		System.out.println("FullString: " + fullString);
		//Result contains an array of all ordered pathways
		String result[] = findStringSplitter(obj, fullString);
		
		//Duration is an array of integers containing the duration for a corresponding pathway 
		int duration[] = findDuration(obj, result);
		
		String ret = sortNetwork(tempRet, duration);
		
		crit_path = returncritpaths(ret);
		System.out.println("\n\n\n\n\n\nThe CRITICAL PATH from compute() is " + crit_path);
		
		return ret;	
	}
	
	// THIS IS A PHASE 2 METHOD
	public String computecriticalpath()  // companion method to compute()
	{
		System.out.println(crit_path + " from LOGIC.java");
		return crit_path;
		//String newret = sortforCP(tempRet)
		//return a global String
		//return "";
	}
	
	public void Restart()
	{
		originals.clear();
		obj.clear();
		index = 0;
		fullString = "";
	}
	
	public String[] FindStartPoint(ArrayList<members> obje)
	{
		int count = 0;
		for(int i = 0; i < obj.size(); i++)
		{
			//System.out.println("Length: " + obj.get(i).dependencies.length);
			if(obj.get(i).dependencies[0].compareTo("") == 0)
			{
			  count++ ;
			  System.out.println("count = " + count);
			}
		}
		System.out.println("Count in FindStartPoint: " + count);
		
		String ret[] = new String[count]; //change 5 to count
		int k = 0;
		for(int i = 0; i < obje.size(); i++  ) // i < count
		{
			for(int j = 0; j < obje.get(i).dependencies.length; j++)
			{
				if(obje.get(i).dependencies[j].compareTo("") == 0)
				{
					
					System.out.println("obje.get(i).name :" + obje.get(i).name);
					ret[k] = obje.get(i).name;
					k++;
				}
			}
		}
		System.out.println("ret is:-");
		for(int i = 0; i < ret.length; i++)
			System.out.print(ret[i] + " ");
		return ret;
	}
	
	public String FindEndPoint(ArrayList<members> ob)
	{
		int countep = 0;
		int countstatus[] = new int[ob.size()];
		for(int i = 0; i < ob.size(); i++)
		{
			for(int j = 0; j < ob.size(); j++)
			{
				System.out.println(ob.get(j).name + ob.get(j).dependencies[0]);
				if(ob.get(j).dependencies.length == 0)
					break;
				for(int k = 0; k < ob.get(j).dependencies.length; k++ )
				{
					if(ob.get(i).name.compareToIgnoreCase(ob.get(j).dependencies[k]) == 0)
						countep++;
				}
			}
			countstatus[i] = countep;
			countep = 0;
			System.out.println("countstatus for " + ob.get(i).name + " " + countstatus[i]) ;
		}
		countep = 0;
		//error checking loop
		for(int i = 0; i < countstatus.length; i++)
		{
			if(countstatus[i] == 0)
				countep++;
		
		if(countep > 1)
		{
			return("Error");
			//call some method that would display the error message
		}
			countep = 0;
		}
		
		for(int i = 0; i < countstatus.length; i++)
		{
			if(countstatus[i] == 0)
				return ob.get(i).name;	//returns the name
		}
		return " "; //default error return value
	}
	
	
	public int retindex(ArrayList<members> obj, String chk)
	{
		for(int i = 0; i < obj.size(); i++)
		{
			if(obj.get(i).name.compareTo(chk) == 0)
			{
				return i;
			}
		}
		return 0;
	}
	
	public String[] retdependencies(ArrayList<members> obj, String srch)
	{
		int i = retindex(obj, srch);
		String toret[] = obj.get(i).dependencies;
		System.out.print("Dependencies for " + srch + " : ");
		for(int k = 0; k < toret.length; k++)
		{
			System.out.print(obj.get(i).dependencies[k] + ",");
		}	
		System.out.println("");
		return toret;
	}

	public boolean checkStartPoints(ArrayList<members>obj) //returns false if a start point isn't a dependency for a node
	{
	boolean flag = false;	
	String sparray[] = FindStartPoint(obj);
	for(int i = 0; i < sparray.length; i++)
	{
		System.out.println(sparray[i]);
	}
	for(int i = 0; i < sparray.length; i++)
	{
		flag = false;
		for(int j = 0; j < obj.size(); j++)
		{
			String arrdep[] = retdependencies(obj,obj.get(j).name);
			for(int k = 0; k < arrdep.length; k++)
			{
				if(sparray[i].compareTo(obj.get(j).dependencies[k]) == 0) 
					{
						flag = true;
						break;
					}
			}
			//break gets us here
			if(flag == true)
				break;
		}
		if (flag == false)
			return false;
	}
		return flag;
	}
	
	public boolean checkEndPoints(ArrayList<members> ob) //returns true if the number of end points is 1, else false
	{
		int countnumberofendpoints = 0;
		
		ArrayList<String> ni = new ArrayList<String>(); 
		for(int i = 0; i < ob.size();i++)
			for(int j = 0; j < ob.get(i).dependencies.length;j++)
				ni.add(ob.get(i).dependencies[j]);
		
		int tempcount = 0;
		for(int i = 0; i < ob.size(); i++)
		{
			for(int j = 0; j < ni.size(); j++)
			{
				if(ob.get(i).name.compareTo(ni.get(j)) == 0)
					tempcount++;
			}
			if(tempcount == 0)
				countnumberofendpoints++;
			tempcount = 0;
		}
		
		if(countnumberofendpoints == 1)
			return true;
		else
			return false;
	}
	
	public boolean checkCycle(ArrayList<members> ob, String en, int num_paths, int cycleCount)
	{
		boolean flag = false;
		if(cycleCount > num_paths)
			{
				flag =  true; //network cycle!
				return flag;
			}
		String arrdep[] = retdependencies(ob, en);
		
		if(arrdep[0].compareTo("") != 0)
		{
			for(int i = 0; i < arrdep.length; i++)
			{
				en = arrdep[i];
				cycleCount++;
				int temp = cycleCount;
				flag = checkCycle(ob, en, num_paths, temp);
				System.out.println("\ncycleCount status : " + cycleCount);
			}
		}
		return flag;
	}
	
public boolean checkConnection(ArrayList<members> ob, String en)
{
	boolean flag = false;
	String arrdep[] = retdependencies(ob, en);

	int leng = arrdep.length;
	
	String startNodes[] = FindStartPoint(ob);
	
	//base case 1 - recursion
	
	for(int i = 0; i < startNodes.length; i++)
	{
		if(en.compareTo(startNodes[i]) == 0)
			return true;
	}
	
	//diff. base case
	for(int i = 0; i < leng; i++)	//base case 2 - checks for outliers
	{
	for(int j = 0; j < ob.size(); j++)
		{
			if(ob.get(j).name.compareTo(arrdep[i]) == 0)
			{
				
				flag = true;
				break;
			}
			else
				flag = false;
		}
	if(flag == false)
			return flag;
	}
	
	
	for(int j = 0; j < arrdep.length; j++)
	{
		
		if(arrdep[j].compareTo("") != 0)
		{
			for(int i = 0; i < arrdep.length; i++)
			{
				en = arrdep[i];
				flag = checkConnection(ob,en);
			}
		}
			
	}
	return flag; // will only go here if flag = false, i.e. broken connection
}
	
public void pathFinder(ArrayList<members> ob, String en)
{
	fullString += en;
	System.out.println("Fullstring: " + fullString);
	System.out.println("Index: " + index);
	
	String startNodes[] = FindStartPoint(ob);
	String copy = "";
	Boolean flag = false;
	
	//base case 1 - recursion
	
	for(int i = 0; i < startNodes.length; i++)
	{
		try{
		if(en.compareTo(startNodes[i]) == 0){
			flag = true;
			fullString += ",";
		}
		}
		catch(NullPointerException e){}
	}
	
	if(flag == false)
	{
		String arrdep[] = retdependencies(ob, en);
		System.out.println("Index: " + index);		
				
		if(index < fullString.length())
		{
			copy = fullString.substring(index);
			System.out.println("Copy: " + copy);
		}
		for(int i = 0; i < arrdep.length; i++)
		{
			if(i == 0)
			{
				String temp = arrdep[i];
				index = index + arrdep[i].length() -1;
				
				pathFinder(ob, temp);
			}
			else
			{
				System.out.println("Copy1: " + copy);
				System.out.println("Index1: " + index);
				System.out.println("Fullstring1: " + fullString);
				String temp = arrdep[i];
				index = index + copy.length() - 1;
				fullString = fullString + copy;
				pathFinder(ob, temp);
			}
			//System.out.println("\ncycleCount status : " + cycleCount);
		}
	}
}

public String[] findStringSplitter(ArrayList<members> ob, String val)
{
	String temp[] = val.split(",");
	System.out.println("LENGTH OF TEMPORARY ARRAY: " + temp.length);
	int k = 0,count = 0;;
	String endpoint = FindEndPoint(ob);
	
	
	for(int i = 0; i < temp.length; i++)
	{
		System.out.println(temp[i]);
	}
	/*for(int i = 0; i < temp.length; i++)
	{
		if(temp[i].compareTo(endpoint) == 0)
		{
			count++;
		}
	}
	
	String ret[] = new String[count];
	for(int i = 0; i < ret.length; i++)
	{
		ret[i] = "";
	}*/
	
	String ret[] = new String[temp.length];
	
	for(int i = 0; i < ret.length; i++)
	{
		ret[i] = "";
	}
	
	for(int i = 0; i < ret.length; i++)
	{
		for(int j = temp[i].length()-1; j >= 0; j--)
		{
			ret[i] += temp[i].charAt(j);
		}
	}
	
	for(int i = 0; i < ret.length; i++){
		System.out.println(ret[i]);
	}
	return ret;
}

public int[] findDuration(ArrayList<members> ob, String[] val)
{
	//Removing duplicates and single elements from val
	ArrayList<String> t = new ArrayList<String>();	//temporary arraylist
	for(int i = 0; i < val.length; i++)
	{
		t.add(val[i]);
	}
	
	for(int i = 0; i < t.size(); i++)
	{
		int count = 0;
		for(int j = 0; j < t.size(); j++)
		{
			if(t.get(i).compareTo(t.get(j)) == 0)
			{
				count++;
			}
		}
		
		if(count >= 2 || t.get(i).length() == 1)
		{
			t.remove(i);
		}
	}
	
	for(int i = 0; i < t.size(); i++) 	// DUPLICATING THE PREVIOUS FOR CONSTRUCT, REMOVING ANY PASS-BY's
	{
		int count = 0;
		for(int j = 0; j < t.size(); j++)
		{
			if(t.get(i).compareTo(t.get(j)) == 0)
			{
				count++;
			}
		}
		
		if(count >= 2 || t.get(i).length() == 1)
		{
			t.remove(i);
		}
	}
	
	int duration[] = new int[t.size()];
	String[] temp;
	for(int i = 0; i < duration.length; i++)
	{
		duration[i] = 0;
	}
	
	for(int i = 0; i < duration.length; i++)
	{
		temp = new String[t.get(i).length()];
		temp = t.get(i).split("");
		for(int j = 0; j < temp.length; j++)
		{
			int index = retindex(ob, temp[j]);
			duration[i] += ob.get(index).duration;
		}
	}
	
	tempRet = new String[t.size()];
	for(int i = 0; i < tempRet.length; i++)//initializing tempRet
		tempRet[i] = "";
	
	for(int i = 0; i < t.size(); i++) //CAN EVEN BE tempret.length
	{
		//code to change acronyms to full forms
		
				for(int k = 0; k < t.get(i).length(); k++)
				{
					System.out.println("t.get(i).length = " + t.get(i).length());
					for(int j = 0; j < originals.size(); j++)
					{
						if((t.get(i).substring(k, k+1).compareTo(originals.get(j).name.substring(0, 1)) == 0))
						{
							tempRet[i] += " " + originals.get(j).name;
							break;
						}
					}

				}
		//tempRet[i] = t.get(i);
	}
	
	return duration;
}

public String sortNetwork(String[] pathways, int[] durations)
{
	for(int i = 0; i < durations.length - 1; i++)
	{
		for(int j = i + 1; j < durations.length; j++)
		{
			if(durations[i] < durations[j])
			{
				String temp1 = tempRet[i];
				tempRet[i] = pathways[j];
				tempRet[j] = temp1;
				
				int temp2 = durations[i];
				durations[i] = durations[j];
				durations[j] = temp2;
			}
		}
	}
	
	String ret = "";
	ret = "\n  Path\t\t\tDuration\n\n";
	
	for(int i = 0; i < tempRet.length; i++)
	{
		ret += " " + tempRet[i] + "\t\t\t" + durations[i] + "\n";
	}
	
	System.out.print(ret);
	return ret;
}

public String returncritpaths(String sortedstring)	// FIX THIS METHOD
{
	String ret = "\n Critical Path\t\t\tDuration\n\n";
	String tempS[] = sortedstring.split("\n");
	for(int i = 0; i < tempS.length; i++)
	{
		System.out.println(tempS[i]);
	}
	String fcp[] = tempS[3].split(" ");		// first critical path
	for(int i = 0; i < fcp.length; i++)
	{
		System.out.println("Index : " + i + " " + fcp[i]);
	}
	String maxduration = fcp[fcp.length - 1];
	System.out.println("maxduration = " + maxduration);
	for(int i = 0 ; i < tempS.length; i++)
	{
		String tcp[] = tempS[i].split(" ");// temporary critical path
		String tempdur = tcp[tcp.length - 1];
		if(tempdur.compareTo(maxduration) == 0)
		{
			ret += tempS[i] + "\n";
		}
	}
	
	return ret;
}

	public static void main(String args[]) /*throws IOException*/
	{
		Gui ob = new Gui();
	
    //Scanner sc = new Scanner(System.in); 
	// User adds when ActionListener for add is triggered. The new object is instantiated.
    //InputStreamReader read = new InputStreamReader(System.in);
    //BufferedReader stdin = new BufferedReader(read);
    /*for(int i = 0; i < 5; i++)
	{
		System.out.println("Name: ");
		String TEMPNAME = stdin.readLine();
		System.out.println("Dependencies: ");
		String temp = stdin.readLine();
		//separate the string into an array of strings breaking by commas
	     String tmp[] = temp.split(",");
	     for(int j = 0; j < tmp.length; j++)
	     {
	    	 	tmp[j] = tmp[j].trim();
	     }
	     
		System.out.println("Duration:");
		//error checking for non-integers
		int tempduration = Integer.parseInt(stdin.readLine());
		
		members tempobj = new members(TEMPNAME, tmp, tempduration);
		obj.add(tempobj);
	}*/
    
    //String endpoint = FindEndPoint(obj);
	//String returned[] = FindStartPoint(obj);
	//for(int i = 0; i < returned.length; i++)
	//{
		//System.out.print(returned[i] + " ");
	//}
	
	
	
	//boolean cond = checkEndPoints(obj);
	//System.out.println(cond);
	
	//boolean cond = checkStartPoints(obj);
	//System.out.println(cond);
	
	//boolean cond = checkCycle(obj, endpoint, NUMOFPATHS, cc); // to check for cycles
	//System.out.print("Cycle status :" + cond + "\n");
	
	
	//boolean cond = checkConnection(obj, endpoint);
	//System.out.println("Connection status :" + cond + "\n");
	
	//pathFinder(obj,endpoint);
	//System.out.println("fullString : " + fullString);
	
	//String result[] = findStringSplitter(obj, fullString);
	
	//int duration[] = findDuration(obj, fullString);
	}
}