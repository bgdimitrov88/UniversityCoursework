package sorts;

public class Start { 
	
	public static void main(String[] args)
	{
		int[] array = new int[] { 10, 2, 8, 3, 6, 55, 0, 1};
		
		for(int i : Sorts.bubble(array))
		{
			System.out.print(i + " ");
		}
		
		System.out.println();
		
		for(int i : Sorts.exchange(array))
		{
			System.out.print(i + " ");
		}
		
		System.out.println();
		
		for(int i : Sorts.selection(array))
		{
			System.out.print(i + " ");
		}
		
	}

}
