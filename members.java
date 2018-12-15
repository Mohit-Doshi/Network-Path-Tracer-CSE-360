public class members 
{
	String name;
	String dependencies[];
	int duration;
	
	public members()
	{
		name = " ";
		dependencies = null;
		duration = 0;
	}
	public members(String x, String[] y, int z) 
	{
		name = x;
		dependencies = y;
		duration = z;
	}
}
