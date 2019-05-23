
public class WritibleMemory extends ReadOnlyMemory {

	public WritibleMemory(int bit, int number) {
		super(bit, number);
	}
	
	public void write(int address,String data)
	{
		this.data[address] = data;
	}

}
