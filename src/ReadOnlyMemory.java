
public class ReadOnlyMemory {

	public int wordSize,numberOfWord;
	public boolean readEnable;
	public String[] data;

	public ReadOnlyMemory(int bit,int number)
	{
		wordSize = bit;
		numberOfWord = number;
		data = new String[numberOfWord];
	}

	public String read(int address)
	{
		return data[address];
	}

	public void dataInput(int address,String inst)
	{
		data[address] = inst;
	}

	public Object[][] createTableBinary()
	{
		Object[][] a = new Object[numberOfWord][2];

		for (int i = 0; i < numberOfWord; i++) {
			
			String temp = Integer.toBinaryString(i);

			while(temp.length() < (Math.log(data.length)/Math.log(2))) temp = "0" + temp;

			a[i][0] = temp;
			if(data[i] != null)
			{
				
				a[i][1] = data[i];
			}
		}

		return a;
	}

	public Object[][] createTableDecimal()
	{
		Object[][] a = new Object[numberOfWord][2];

		for (int i = 0; i < numberOfWord; i++) {
			a[i][0] = i;
			if(data[i] != null)
			{
				
				a[i][1] = Integer.parseInt(data[i],2);
			}
		}

		return a;
	}

	public Object[][] createTableHexaDecimal()
	{
		Object[][] a = new Object[numberOfWord][2];

		for (int i = 0; i < numberOfWord; i++) {
			String temp = Integer.toHexString(i);
			if(i < 16) temp = "0"+temp;
			a[i][0] = temp;
			if(data[i] != null)
			{
				
				
				String temp2 = Integer.toHexString(Integer.parseInt(data[i],2));
				
				if(wordSize > 8 && temp2.length() < 3) temp2 = "0" + temp2;
				else if(wordSize > 4 && temp2.length() < 2) temp2 = "0" + temp2;
				
				
				a[i][1] = temp2;
			}
		}

		return a;
	}

}
