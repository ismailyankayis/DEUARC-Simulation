
public class InstructionRegister {
	
	int wordSize;
	String data;
	boolean load;
	
	public InstructionRegister(int bit)
	{
		for (int i = 0; i < bit; i++) {
			data +="0";
		}
		wordSize = bit;
	}

}
