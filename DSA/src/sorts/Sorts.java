package sorts;

//http://mathbits.com/MathBits/Java/arrays/arrays.htm

public class Sorts {
	
	public static int[] bubble(int[] array)
	{
		boolean thereAreSwaps = true;
		
		while(thereAreSwaps)
		{
			thereAreSwaps = false;
			
			for(int i = 0; i < array.length-1; i++)
			{
				if(array[i+1] < array[i])
				{
					int temp = array[i];
					array[i] = array[i+1];
					array[i+1] = temp;
					
					thereAreSwaps = true;
				}
			}
		}
		
		return array;
	}

	public static int[] exchange(int[] array)
	{
		for(int i = 0; i < array.length-1; i++)
		{
			for(int j = i+1; j < array.length; j++)
			{
				if(array[i] > array[j])
				{
					int temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}
		
		return array;
	}
	
	public static int[] selection(int[] array)
	{
		for(int i = 0; i < array.length-1; i++)
		{
			int smallestIndex = i;
			for(int j = i + 1; j < array.length; j++)
			{
				if(array[j] < array[smallestIndex])
				{
					smallestIndex = j;
				}
			}
			
			int temp = array[i];
			array[i] = array[smallestIndex];
			array[smallestIndex] = temp;
			
		}
		
		return array;
	}
}
