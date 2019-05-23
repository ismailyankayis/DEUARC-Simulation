import com.sun.javafx.fxml.expression.BinaryExpression;

public class ALU {

	public ALU()
	{

	}

	public boolean add(Register S1,Register S2,Register d)
	{
		d.data = S1.data+S2.data;
		if(d.data > 15)
		{
			d.data = d.data - 16;
			return true;
		}
		else return false;
	}
	
	public boolean inc(Register S1,Register d)
	{
		d.data = S1.data + 1;
		if(d.data > 15)
		{
			d.data = d.data - 16;
			return true;
		}
		else return false;
	}
	
	public boolean dbl(Register S1,Register d)
	{
		d.data = S1.data * 2;
		if(d.data > 15)
		{
			d.data = d.data - 16;
			return true;
		}
		else return false;
	}
	
	public void dbt(Register S1,Register d)
	{
		d.data = S1.data / 2;
	}

	public int NOT(int number,int len)
	{
		String binary = Integer.toBinaryString(number);

		while(binary.length() < len) binary = "0" + binary;

		for (int i = 0; i < binary.length(); i++) {
			if(binary.charAt(i) == '1')
			{
				binary = binary.substring(0, i) + "0" + binary.substring(i+1);
			}
			else
			{
				binary = binary.substring(0, i) + "1" + binary.substring(i+1);
			}
		}

		return Integer.parseInt(binary, 2);
	}

	public int AND(int n1,int n2,int len)
	{
		String binary1 = Integer.toBinaryString(n1);

		while(binary1.length() < len) binary1 = "0" + binary1;

		String binary2 = Integer.toBinaryString(n2);

		while(binary2.length() < len) binary2 = "0" + binary2;
		
		String temp = "";
		for (int i = 0; i < binary1.length(); i++) {
			if(binary1.charAt(i) == '1' && binary2.charAt(i) == '1')
			{
				temp += "1";
			}
			else
			{
				temp += "0";
			}
		}
		
		return Integer.parseInt(temp, 2);
	}



}
