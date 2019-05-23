
public class ProgramCounter extends Register{

	
	public ProgramCounter(int bit) {
		super(bit);
		// TODO Auto-generated constructor stub
	}
	
	public void increment()
	{
		data++;
		if(data >= Math.pow(2, wordSize)) data -= Math.pow(2, wordSize);
	}
}
