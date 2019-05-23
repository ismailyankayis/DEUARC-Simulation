import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.NumberFormatter;

import javafx.stage.FileChooser;

import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Rectangle;
import java.awt.Font;
import javax.swing.JDesktopPane;
import javax.swing.JToolBar;
import javax.swing.JPanel;
import java.awt.SystemColor;
import java.awt.Window.Type;
import java.awt.Frame;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JMenu;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JFormattedTextField;
import javax.swing.DropMode;
import java.awt.event.FocusAdapter;

public class DeuArc {

	// MEMORY
	static ReadOnlyMemory InstructionMemory = new ReadOnlyMemory(11, 32);
	static WritibleMemory DataMemory = new WritibleMemory(4, 16);
	static WritibleMemory StackMemory = new WritibleMemory(5, 16);
	int addressCount = -1;
	int programCount = 0;
	static int numberType = 0; // 0 -- binary; 1 -- decimal; 2 -- hexa
	boolean programOver = false;
	ButtonGroup group = new ButtonGroup();


	ALU alu = new ALU();

	// REGISTER
	ProgramCounter programCounter = new ProgramCounter(5);
	StackPointer stackPointer = new StackPointer(4);
	Register addressRegister = new Register(4);
	InstructionRegister instructionRegister = new InstructionRegister(11);

	Register inputRegister = new Register(4);
	Register outputRegister = new Register(4);

	Register register0 = new Register(4);
	Register register1 = new Register(4);
	Register register2 = new Register(4);
	SequenceCounter SC = new SequenceCounter();

	Register S1reg;
	Register S2reg;
	Register DestReg;
	int overFlow = 0;


	// FILE OPERATION
	static File file;
	FileReader fileReader=null;
	BufferedReader br = null;

	// INTERFACE
	public JFrame frmDeuarcSmulator;
	//private JFrame firstPage;

	//static JTextField fileField = new JTextField("Browse an asm/basm file...");
	static JTable table_LabelTable;
	static JTable table_Instruction;
	static JTable table_Data;
	static JTable table_Stack;

	static String[][] labelData = new String[16][3];
	int dataCount = 0;


	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeuArc window = new DeuArc();
					window.frmDeuarcSmulator.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public DeuArc() {

		initialize();
		//firstPane();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	JLabel lblInstValue;
	JLabel lblArvalue;
	JLabel lblPCValue;
	JLabel lblSPValue;
	JLabel lblOutputValue;
	JLabel lblReg0Value;
	JLabel lblReg1Value;
	JLabel lblReg2Value;
	JTextArea txtrMcroOperaton;
	JToggleButton VButton;

	public void initialize() {
		frmDeuarcSmulator = new JFrame();
		frmDeuarcSmulator.setResizable(false);
		frmDeuarcSmulator.setTitle("DEUARC SIMULATOR");
		frmDeuarcSmulator.setBounds(100, 100, 1149, 891);
		frmDeuarcSmulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDeuarcSmulator.getContentPane().setLayout(null);

		addressRegister.data = 0;
		instructionRegister.data = convertBinary(0, 11);


		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaption);
		panel.setBounds(31, 28, 487, 391);
		frmDeuarcSmulator.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblAddressRegister = new JLabel("REGISTERS");
		lblAddressRegister.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAddressRegister.setBounds(175, 11, 125, 25);
		panel.add(lblAddressRegister);

		JLabel lblAddressRegister_1 = new JLabel("Address Register : ");
		lblAddressRegister_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAddressRegister_1.setBounds(10, 50, 119, 25);
		panel.add(lblAddressRegister_1);

		lblArvalue = new JLabel("0000");
		lblArvalue.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblArvalue.setBounds(139, 56, 54, 14);
		panel.add(lblArvalue);

		JLabel lblProgramCounter = new JLabel("Program Counter : ");
		lblProgramCounter.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblProgramCounter.setBounds(10, 86, 119, 14);
		panel.add(lblProgramCounter);

		lblPCValue = new JLabel("00000");
		lblPCValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPCValue.setBounds(139, 87, 46, 14);
		panel.add(lblPCValue);

		JLabel lblStackPointer = new JLabel("Stack Pointer : ");
		lblStackPointer.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStackPointer.setBounds(10, 111, 119, 25);
		panel.add(lblStackPointer);

		lblSPValue = new JLabel("0000");
		lblSPValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSPValue.setBounds(139, 117, 46, 14);
		panel.add(lblSPValue);

		JLabel lblInstructionRegister = new JLabel("Instruction Register : ");
		lblInstructionRegister.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblInstructionRegister.setBounds(10, 142, 141, 25);
		panel.add(lblInstructionRegister);

		lblInstValue = new JLabel("00000000000");
		lblInstValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblInstValue.setBounds(161, 145, 97, 19);
		panel.add(lblInstValue);

		JLabel lblInputRegister = new JLabel("Input Register :");
		lblInputRegister.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblInputRegister.setBounds(10, 201, 119, 25);
		panel.add(lblInputRegister);

		JLabel lblOutputRegister = new JLabel("Output Register : ");
		lblOutputRegister.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblOutputRegister.setBounds(10, 237, 119, 25);
		panel.add(lblOutputRegister);

		lblOutputValue = new JLabel("0000");
		lblOutputValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblOutputValue.setBounds(139, 237, 46, 25);
		panel.add(lblOutputValue);

		JLabel lblRegister = new JLabel("Register 0 :");
		lblRegister.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRegister.setBounds(10, 273, 97, 25);
		panel.add(lblRegister);

		JLabel lblRegister_1 = new JLabel("Register 1 : ");
		lblRegister_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRegister_1.setBounds(10, 305, 97, 25);
		panel.add(lblRegister_1);

		JLabel lblRegister_2 = new JLabel("Register 2 : ");
		lblRegister_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRegister_2.setBounds(10, 341, 97, 19);
		panel.add(lblRegister_2);

		lblReg0Value = new JLabel("0000");
		lblReg0Value.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblReg0Value.setBounds(101, 279, 46, 19);
		panel.add(lblReg0Value);

		lblReg1Value = new JLabel("0000");
		lblReg1Value.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblReg1Value.setBounds(101, 309, 46, 21);
		panel.add(lblReg1Value);

		lblReg2Value = new JLabel("0000");
		lblReg2Value.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblReg2Value.setBounds(101, 341, 46, 19);
		panel.add(lblReg2Value);

		VButton = new JToggleButton("0");
		VButton.setForeground(new Color(0, 0, 0));
		VButton.setEnabled(false);
		VButton.setBounds(357, 307, 83, 23);
		panel.add(VButton);

		JLabel lblV = new JLabel("V");
		lblV.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblV.setBounds(394, 282, 46, 14);
		panel.add(lblV);

		textField = new JTextField();
		textField.setToolTipText("External Input Value(Decimal)");
		textField.setBounds(120, 204, 86, 20);
		panel.add(textField);
		textField.setColumns(10);

		JButton btnInput = new JButton("Input");
		btnInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String temp = textField.getText();
				if(!temp.matches(".*[a-zA-z].*") && !temp.equals(""))
				{
					int x = Integer.parseInt(temp);
					if(x < Math.pow(2, DataMemory.wordSize))
					{
						inputRegister.data = x;
					}
				}
				else{
					textField.setText("");
					inputRegister.data = 0;
					JOptionPane.showMessageDialog(frmDeuarcSmulator,"Input value is not acceptable!", "Input Warning",JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnInput.setBounds(211, 203, 64, 23);
		panel.add(btnInput);



		JLabel lblInstructionMemory = new JLabel("Instruction Memory");
		lblInstructionMemory.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblInstructionMemory.setBounds(564, 28, 174, 32);
		frmDeuarcSmulator.getContentPane().add(lblInstructionMemory);

		JLabel lblDataMemory = new JLabel("Data Memory");
		lblDataMemory.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblDataMemory.setBounds(882, 28, 146, 26);
		frmDeuarcSmulator.getContentPane().add(lblDataMemory);

		JLabel lblStackMemory = new JLabel("Stack Memory");
		lblStackMemory.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStackMemory.setBounds(564, 411, 146, 26);
		frmDeuarcSmulator.getContentPane().add(lblStackMemory);

		JButton btnInstructon = new JButton("INSTRUCTION");
		btnInstructon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(programOver) return;

				/*overFlow = 0;
				VButton.setSelected(false);
				VButton.setText("0");*/

				execution();
				while(SC.T != 0) execution();
			}
		});
		btnInstructon.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnInstructon.setBounds(61, 467, 193, 41);
		frmDeuarcSmulator.getContentPane().add(btnInstructon);

		JLabel lblReadDataFrom = new JLabel("READ DATA FROM MEMORY AS ");
		lblReadDataFrom.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblReadDataFrom.setBounds(149, 430, 220, 26);
		frmDeuarcSmulator.getContentPane().add(lblReadDataFrom);

		JButton btnMcroOperaton = new JButton("MICRO OPERATION");
		btnMcroOperaton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(programOver) return;

				/*if(SC.T == 0)
				{
					overFlow = 0;
					VButton.setSelected(false);
					VButton.setText("0");
				}*/
				execution();


			}
		});
		btnMcroOperaton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnMcroOperaton.setBounds(292, 467, 206, 41);
		frmDeuarcSmulator.getContentPane().add(btnMcroOperaton);

		JLabel lblDataTable = new JLabel("Data Table");
		lblDataTable.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblDataTable.setBounds(882, 415, 111, 18);
		frmDeuarcSmulator.getContentPane().add(lblDataTable);

		JScrollPane scrollPane_InstMemory = new JScrollPane();
		scrollPane_InstMemory.setBounds(564, 71, 252, 329);
		frmDeuarcSmulator.getContentPane().add(scrollPane_InstMemory);

		table_Instruction = new JTable();
		table_Instruction.setEnabled(false);

		scrollPane_InstMemory.setViewportView(table_Instruction);

		JScrollPane scrollPane_DataMemory = new JScrollPane();
		scrollPane_DataMemory.setBounds(862, 71, 252, 329);
		frmDeuarcSmulator.getContentPane().add(scrollPane_DataMemory);

		table_Data = new JTable();
		table_Data.setEnabled(false);

		scrollPane_DataMemory.setViewportView(table_Data);

		JScrollPane scrollPane_LabelTable = new JScrollPane();
		scrollPane_LabelTable.setBounds(861, 455, 253, 366);
		frmDeuarcSmulator.getContentPane().add(scrollPane_LabelTable);

		table_LabelTable = new JTable();
		table_LabelTable.setEnabled(false);

		scrollPane_LabelTable.setViewportView(table_LabelTable);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(564, 448, 252, 366);
		frmDeuarcSmulator.getContentPane().add(scrollPane);

		table_Stack = new JTable();
		table_Stack.setEnabled(false);

		scrollPane.setViewportView(table_Stack);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(163, 539, 206, 282);
		frmDeuarcSmulator.getContentPane().add(scrollPane_2);

		txtrMcroOperaton = new JTextArea();
		scrollPane_2.setViewportView(txtrMcroOperaton);
		txtrMcroOperaton.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtrMcroOperaton.setBackground(SystemColor.inactiveCaption);



		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1143, 26);
		frmDeuarcSmulator.getContentPane().add(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setPreferredSize(new Dimension(50, 22));
		menuBar.add(mnFile);

		JMenuItem mnýtmOpen = new JMenuItem("Open...");
		mnýtmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("ASM File,BASM File", new String[] {"asm", "basm"});
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileFilter(filter);
				int returnVal = fileChooser.showOpenDialog(fileChooser);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					DeuArc.file = fileChooser.getSelectedFile();
					if (file.getName().substring(DeuArc.file.getName().length()-4,file.getName().length()).equalsIgnoreCase(".asm") || 
							file.getName().substring(DeuArc.file.getName().length()-5,file.getName().length()).equalsIgnoreCase(".basm")) {
						addressCount = -1;
						programCount = 0;
						//numberType = 0;
						programOver = false;
						InstructionMemory = new ReadOnlyMemory(11, 32);
						DataMemory = new WritibleMemory(4, 16);
						StackMemory = new WritibleMemory(5, 16);
						stackPointer = new StackPointer(4);
						programCounter = new ProgramCounter(5);
						instructionRegister = new InstructionRegister(11);
						addressRegister = new Register(4);
						SC.T = 0;
						dataCount = 0;
						labelData = new String[16][3];
						pcOrg = false;
						try {
							readAssembly();
							printingScreen();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					else{
						return;
					}
				}
			}
		});
		mnFile.add(mnýtmOpen);

		JMenuItem mnýtmExportMifFile = new JMenuItem("Export Mif File...");
		mnýtmExportMifFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportMIF();
			}
		});
		mnFile.add(mnýtmExportMifFile);

		JMenu mnNewMenu = new JMenu("Data Type");
		menuBar.add(mnNewMenu);

		JMenuItem mnýtmBinary = new JMenuItem("Binary");
		mnýtmBinary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mnýtmBinary.setSelected(true);
				numberType = 0;
				printingScreen();
			}
		});
		mnNewMenu.add(mnýtmBinary);

		JMenuItem mnýtmDecimal = new JMenuItem("Decimal");
		mnýtmDecimal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mnýtmDecimal.setSelected(true);
				numberType = 1;
				printingScreen();
			}
		});
		mnNewMenu.add(mnýtmDecimal);

		JMenuItem mnýtmHexadecimal = new JMenuItem("HexaDecimal");
		mnýtmHexadecimal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mnýtmHexadecimal.setSelected(true);
				numberType = 2;
				printingScreen();
			}
		});
		mnNewMenu.add(mnýtmHexadecimal);
		group.add(mnýtmBinary);
		group.add(mnýtmDecimal);
		group.add(mnýtmHexadecimal);
		mnýtmBinary.setSelected(true);

	}



	public void readAssembly() throws FileNotFoundException
	{
		if(file != null)
		{
			fileReader = new FileReader(file.getAbsolutePath());
			br = new BufferedReader(fileReader);

			String line;



			try {
				// Parsing of Assembly

				// reading variables
				while((line = br.readLine()) != null)
				{
					fillingDataTable(line);

				}

				// reading main program
				programCount = 0;
				fileReader.close();
				fileReader = new FileReader(file.getAbsolutePath());
				br = new BufferedReader(fileReader);
				while((line = br.readLine()) != null)
				{
					fillingMemory(line);

				}


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	boolean pcOrg = false;
	public void fillingMemory(String data)
	{

		String[] parsing = data.split("\\s+");
		for (int i = 0; i < parsing.length; i++) {

			if(parsing[i].contains(":")) i++;

			if(parsing[i].equalsIgnoreCase("ORG")) // Initializing of PC and AR
			{
				if(parsing[++i].equals("C"))
				{
					if(!pcOrg)
					{
						programCounter.data = Integer.parseInt(parsing[++i]);
						pcOrg = true;
					}
					else i++;
					programCount =  Integer.parseInt(parsing[i]);
				}
				else if(parsing[i].equals("S"))
				{
					stackPointer.data = Integer.parseInt(parsing[++i]);
				}
			}
			else if(parsing[i].equalsIgnoreCase("ADD"))
			{
				String opcode = "00000";
				addToInstructionMemory(opcode,parsing[++i]);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("INC"))
			{
				String opcode = "00001";
				addToInstructionMemory(opcode,parsing[++i]);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("DBL"))
			{
				String opcode = "00010";
				addToInstructionMemory(opcode,parsing[++i]);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("DBT"))
			{
				String opcode = "00011";
				addToInstructionMemory(opcode,parsing[++i]);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("NOT"))
			{
				String opcode = "00100";
				addToInstructionMemory(opcode,parsing[++i]);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("AND"))
			{
				String opcode = "00101";
				addToInstructionMemory(opcode,parsing[++i]);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("LD"))
			{
				String opcode = "00110";
				addToInstructionMemory(opcode,parsing[++i]);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("ST"))
			{
				String opcode = "00111";
				addToInstructionMemory(opcode,parsing[++i]);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("TSF"))
			{
				String opcode = "01001";
				addToInstructionMemory(opcode,parsing[++i]);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("CAL"))
			{
				String opcode = "01010";
				addToInstructionMemory(opcode,parsing[++i]);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("RET"))
			{
				String opcode = "01011000000";

				// No need any source and destination
				InstructionMemory.dataInput(programCount++, opcode);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("JMP"))
			{
				String opcode = "01100";
				String temp="";
				for (int j = i+1; j < parsing.length; j++) {
					if(parsing[j].contains("%")) break;
					temp+=parsing[j]+",";
				}
				addToInstructionMemory(opcode,temp); 
				break;
			}
			else if(parsing[i].equalsIgnoreCase("JMR"))
			{
				String opcode = "01101";

				String temp="";
				for (int j = i+1; j < parsing.length; j++) {
					if(parsing[j].contains("%")) break;
					temp+=parsing[j]+",";
				}
				addToInstructionMemory(opcode,temp);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("PSH"))
			{
				String opcode = "01110";
				String temp="";
				for (int j = i+1; j < parsing.length; j++) {
					if(parsing[j].contains("%")) break;
					temp+=parsing[j]+",";
				}

				addToInstructionMemory(opcode,temp);
				
				break;
			}
			else if(parsing[i].equalsIgnoreCase("POP"))
			{
				String opcode = "01111000000";

				// No need any source and destination
				InstructionMemory.dataInput(programCount++, opcode);
				break;
			}
			else if(parsing[i].equalsIgnoreCase("HLT"))
			{
				String opcode = "01000000000";

				// No need any source and destination
				InstructionMemory.dataInput(programCount++, opcode);
				break;
			}

		}
	}

	public void addToInstructionMemory(String opcode,String registers)
	{


		String[] secondPart = registers.split(",");
		for (int j = 0; j < secondPart.length; j++) 
		{
			if(opcode.equalsIgnoreCase("01010")) // CAL INSTRUCTION
			{
				if(secondPart[j].matches(".*[A-Z].*"))
				{
					for (int i = 0; i < labelData.length; i++) {
						if(labelData[i][0].equalsIgnoreCase(secondPart[j]))
						{
							String temp = labelData[i][1];
							while(temp.length() != 6) temp = "0" + temp;
							opcode += temp;
							break;
						}
						break;
					}
				}
				else
				{
					String temp = Integer.toBinaryString(Integer.parseInt(secondPart[j]));
					while(temp.length() != 6) temp = "0" + temp;
					opcode += temp;
					break;
				}
			}
			else if(opcode.equalsIgnoreCase("01101")) // JMR INSTRUCTION
			{
				//String temp = Integer.toBinaryString(Integer.parseInt(secondPart[j]));
				String temp = null;
				//secondPart = secondPart[0].split("\\s+");
				if(secondPart.length == 1 )
				{
					int x = Integer.parseInt(secondPart[j]);
					temp = Integer.toBinaryString(x);
					if(temp.length() > 6) temp = temp.substring(27);
					if(temp.charAt(0) == '0')
						while(temp.length() < 6) temp = "0" + temp;
					else while(temp.length() < 6) temp = "1" + temp;
				}
				else if(secondPart[j+1].equalsIgnoreCase("BIN"))
				{
					temp = secondPart[j];
					if(temp.charAt(0) == '0')
						while(temp.length() < 6) temp = "0" + temp;
					else while(temp.length() < 6) temp = "1" + temp;
				}

				opcode += temp;
				break;
			}
			else if(opcode.equalsIgnoreCase("01110")) // PSH INSTRUCTION
			{
				if(secondPart[j].contains("@"))
				{
					if(secondPart.length > 1 && secondPart[j+1].equalsIgnoreCase("C"))
					{
						opcode+="01";
						int x = Integer.parseInt(secondPart[j].substring(1));
						String temp = Integer.toBinaryString(x);
						while(temp.length() < 4) temp = "0" + temp;
						
						opcode+=temp;
						break;
						
					}
					else if(secondPart.length > 1 && secondPart[j+1].equalsIgnoreCase("D"))
					{
						opcode+="10";
						int x = Integer.parseInt(secondPart[j].substring(1));
						String temp = Integer.toBinaryString(x);
						while(temp.length() < 4) temp = "0" + temp;
						
						opcode+=temp;
						break;
					}
					else
					{
						opcode+="11";
						for (int i = 0; i < labelData.length; i++) {
							if(labelData[i][0].equalsIgnoreCase(secondPart[j].substring(1)))
							{
								opcode+=labelData[i][2];
								break;
							}
						}
						break;
					}
				}
				else if(secondPart[j].contains("#"))
				{
					opcode += "00";
					int x = Integer.parseInt(secondPart[j].substring(1));
					String temp = Integer.toBinaryString(x);
					while(temp.length() < 4) temp = "0" + temp;
					opcode += temp;
					break;
				}
			}
			else if(opcode.equalsIgnoreCase("01100")) // JMP INSTRUCTION
			{
				if(secondPart[j].matches(".*[A-Za-z].*")){
					for (int i = 0; i < labelData.length; i++) {
						if(labelData[i][0].equalsIgnoreCase(secondPart[j]))
						{
							String temp = labelData[i][1];
							while(temp.length() < 6) temp = "0" + temp;
							opcode+=temp;
							break;
						}
					}
					break;
				}
				else
				{
					opcode = "1" + opcode.substring(1);
					int x = Integer.parseInt(secondPart[j]);
					String temp = Integer.toBinaryString(x);
					while(temp.length() < 6) temp = "0" + temp;
					opcode+=temp;
					break;
				}
			}
			else if(secondPart[j].equalsIgnoreCase("R2"))
			{
				opcode += "10";
			}
			else if(secondPart[j].equalsIgnoreCase("R1"))
			{
				opcode += "01";
			}
			else if(secondPart[j].equalsIgnoreCase("R0"))
			{
				opcode += "00";
			}
			else if(secondPart[j].equalsIgnoreCase("INPR"))
			{
				opcode += "11";
			}
			else if(secondPart[j].equalsIgnoreCase("OUTR"))
			{
				opcode += "11";
			}
			else if(secondPart[j].contains("@"))
			{
				for (int i = 0; i < labelData.length; i++) {
					if(labelData[i][0].equalsIgnoreCase( secondPart[j].substring(1, secondPart[j].length()) ) )
					{
						/*if(opcode.equalsIgnoreCase("01100")) // JMP INSTRUCTION
						{
							if(labelData[i][1].length() != 5)
								opcode += "0" + labelData[i][1];
							else opcode += labelData[i][1];
							break;
						}*/

						opcode += labelData[i][1];
						break;
					}
				}
			}
			else if(secondPart[j].contains("#"))
			{
				String temp = "1";
				temp += opcode.substring(1, opcode.length());
				int data = Integer.parseInt(secondPart[j].substring(1));
				String temp2 = Integer.toBinaryString(data);
				while(temp2.length() < 4) temp2 = "0" + temp2;

				if(opcode.equalsIgnoreCase("01100"))
				{
					while(temp2.length() < 6) temp2 = "0" + temp2;
				}
				opcode = temp2;
				break;
			}
			else if(secondPart[j].charAt(0) == '%')
			{
				break;
			}
		}
		if(opcode.length() == 7 && (opcode.substring(1,5).equals("0001") || opcode.substring(1,5).equals("0100") || opcode.substring(1,5).equals("0111")
				|| opcode.substring(1,5).equals("0010") || opcode.substring(1,5).equals("0011")))
		{
			opcode += opcode.substring(5);
		}

		while(opcode.length() < 11) opcode += "0";

		InstructionMemory.dataInput(programCount++, opcode);
		if(programCount >= 32) programCount -=32;
	}


	public void fillingDataTable(String data)
	{

		String[] parsing = data.split("\\s+");
		for (int i = 0; i < parsing.length; i++) 
		{
			if( parsing[i].contains(":") && addressCount != -1) // Add data to data table
			{
				// write label to the label table
				labelData[dataCount][0] = parsing[i].substring(0, parsing[i].length()-1);

				//write address to the label table
				String binary = Integer.toBinaryString(addressCount);
				while(binary.length() < DataMemory.wordSize)
					binary = "0" + binary;

				labelData[dataCount][1] = binary;

				i++;
				if(parsing[i].equalsIgnoreCase("BIN"))
				{
					labelData[dataCount][2] = parsing[++i];
					DataMemory.data[addressCount] = parsing[i];
				}
				else if(parsing[i].equalsIgnoreCase("DEC"))
				{
					int x = Integer.parseInt(parsing[++i]);

					String temp = Integer.toBinaryString(x);

					while(temp.length() < 4) temp = "0" + temp;

					labelData[dataCount][2] = temp;
					DataMemory.data[addressCount] = temp;
				}
				else if(parsing[i].equalsIgnoreCase("HEX"))
				{
					int x = Integer.parseInt(parsing[++i],16);
					String temp = Integer.toBinaryString(x);
					while(temp.length() < 4) temp = "0" + temp;

					labelData[dataCount][2] = temp;
					DataMemory.data[addressCount] = temp;
				}
				// sayýlar arasý çeviri gelicek

				//DataMemory.write(programCount, );
				//
				//DataMemory.data[addressCount] = parsing[i];
				dataCount++;
				addressCount++;

			}
			else if(parsing[i].equalsIgnoreCase("ORG")) // Initializing of PC and AR
			{
				if(parsing[++i].equals("D"))
				{
					//addressRegister.data = Integer.parseInt(parsing[++i]);
					addressCount = Integer.parseInt(parsing[++i]);
				}
				else if(parsing[i].equals("C"))
				{
					programCount = Integer.parseInt(parsing[++i]);
				}
			}
			else if(parsing[i].contains(":"))
			{
				// write label to the label table
				labelData[dataCount][0] = parsing[i].substring(0, parsing[i].length()-1);

				//write address to the label table
				String binary = Integer.toBinaryString(programCount);
				while(binary.length() < 5)
					binary = "0" + binary;

				labelData[dataCount][1] = binary;


				labelData[dataCount][2] = "0";
				dataCount++;
				//programCount++;
			}
			else if(!parsing[i].equals(""))
			{
				programCount++;
				break;
			}
		}

	}

	public void initializeRegisters()
	{
		lblArvalue.setText(convertBinary(addressRegister.data,addressRegister.wordSize));
		lblPCValue.setText(convertBinary(programCounter.data,programCounter.wordSize));
		lblSPValue.setText(convertBinary(stackPointer.data,stackPointer.wordSize));
		//lblInstValue.setText(instructionRegister.data);

	}


	int D;
	int destination;
	int S1,S2;
	int Q;
	private JTextField textField;

	private void fetchAndDecode()
	{
		if(SC.T == 0)
		{
			instructionRegister.data = InstructionMemory.read(programCounter.data);
			SC.inc();

			lblInstValue.setText(instructionRegister.data);
			txtrMcroOperaton.append("T0 -> IR <- IM["+programCounter.data+"]\n");
		}
		else if(SC.T == 1)
		{
			programCounter.increment();
			SC.inc();

			lblPCValue.setText(convertBinary(programCounter.data,programCounter.wordSize));
			txtrMcroOperaton.append("T1 -> PC <- PC + 1\n");

		}
		else if(SC.T == 2)
		{
			Q = Integer.parseInt(instructionRegister.data.substring(0, 1), 2);
			D = Integer.parseInt(instructionRegister.data.substring(1, 5), 2);

			// finding destination register 
			destination = Integer.parseInt(instructionRegister.data.substring(5, 7), 2);
			if(destination == 0)
			{
				DestReg = register0;
			}
			else if(destination == 1)
			{
				DestReg = register1;
			}
			else if(destination == 2)
			{
				DestReg = register2;
			}
			else if(destination == 3)
			{
				DestReg = outputRegister;
			}
			// finding S1 register
			S1 = Integer.parseInt(instructionRegister.data.substring(7, 9), 2);
			if(S1 == 0)
			{
				S1reg = register0;
			}
			else if(S1 == 1)
			{
				S1reg = register1;
			}
			else if(S1 == 2)
			{
				S1reg = register2;
			}
			else if(S1 == 3)
			{
				S1reg = inputRegister;
			}
			// finding S2 register
			S2 = Integer.parseInt(instructionRegister.data.substring(9, 11), 2);
			if(S2 == 0)
			{
				S2reg = register0;
			}
			else if(S2 == 1)
			{
				S2reg = register1;
			}
			else if(S2 == 2)
			{
				S2reg = register2;
			}
			else if(S2 == 3)
			{
				S2reg = inputRegister;
			}
			SC.inc();

			txtrMcroOperaton.append("T2 -> D0..D15 <- D"+D+"\n");
			txtrMcroOperaton.append("Q <- "+Q+" D <- "+destination+"\n");
			txtrMcroOperaton.append("S1 <- "+S1+" S2 <- "+S2+"\n");


		}
	}

	private void execution()
	{
		if(SC.T < 3)
			fetchAndDecode();
		else
		{
			int x = Integer.parseInt(instructionRegister.data.substring(1, 5), 2);
			switch(x)
			{
			case 0: // ADD
				if(alu.add(S1reg, S2reg, DestReg))
				{
					overFlow = 1;
					VButton.setSelected(true);
					VButton.setText("1");
				}
				txtrMcroOperaton.append("T3 -> R"+destination+" <- "+S1reg.data+" + "+S2reg.data+" ,SC <- 0\n");
				SC.T = 0;
				break;
			case 1:
				if(alu.inc(S1reg, DestReg))
				{
					overFlow = 1;
					VButton.setSelected(true);
					VButton.setText("1");
				}
				txtrMcroOperaton.append("T3 -> R"+destination+" <- "+S1reg.data+" + 1 ,SC <- 0\n");
				SC.T = 0;
				break;
			case 2:
				if(alu.dbl(S1reg, DestReg)) 
				{
					overFlow = 1;
					VButton.setSelected(true);
					VButton.setText("1");
				}
				txtrMcroOperaton.append("T3 -> R"+destination+" <- "+S1reg.data+" + "+S1reg.data+" ,SC <- 0\n");
				SC.T = 0;
				break;
			case 3:
				alu.dbt(S1reg, DestReg);
				txtrMcroOperaton.append("T3 -> R"+destination+" <- "+S1reg.data+" / 2 ,SC <- 0\n");
				SC.T = 0;
				break;
			case 4:
				DestReg.data = alu.NOT(S1reg.data, S1reg.wordSize);
				txtrMcroOperaton.append("T3 -> R"+destination+" <- NOT(R"+S1+") ,SC <- 0\n");
				SC.T = 0;
				break;
			case 5:
				DestReg.data = alu.AND(S1reg.data, S2reg.data, S1reg.wordSize);
				txtrMcroOperaton.append("T3 -> R"+destination+" <- R"+S1+" and R"+S2+" ,SC <- 0\n");
				SC.T = 0;
				break;
			case 6:
				LD();
				break;
			case 7:
				ST();
				break;
			case 8:
				txtrMcroOperaton.append("Main Program is Over !\n");
				programOver = true;
				SC.T = 0;
				break;
			case 9:
				TSF();
				break;
			case 10:
				CAL();
				break;
			case 11:
				RET();
				break;
			case 12:
				JMP();
				break;
			case 13:
				JMR();
				break;
			case 14:
				PSH();
				break;
			case 15:
				POP();
				break;

			}

			if(SC.T == 0)
			{
				DestReg = null;
				S1reg = null;
				S2reg = null;
			}
		}
		printingScreen();
	}

	public void LD()
	{
		if(SC.T==3&&Q==1)
		{
			DestReg.data=Integer.parseInt(instructionRegister.data.substring(7),2);
			txtrMcroOperaton.append("T3 -> R"+destination+" <- "+instructionRegister.data.substring(7)+" ,SC <- 0\n");
			SC.T=0;
		}
		else if(SC.T==3&&Q==0)
		{
			addressRegister.data=Integer.parseInt(instructionRegister.data.substring(7),2);
			txtrMcroOperaton.append("T3 -> AR <- "+instructionRegister.data.substring(7)+"\n");
			SC.inc();
		}
		else if(SC.T==4&&Q==0)
		{
			DestReg.data=Integer.parseInt(DataMemory.read(addressRegister.data),2);
			txtrMcroOperaton.append("T4 -> R"+destination+" <- DM["+addressRegister.data+"] ,SC <- 0\n");
			SC.T=0;

		}
	}

	public void ST()
	{
		if(Q==1&&SC.T==3)
		{ 
			S2reg.data=DestReg.data; // tekrar düþünülecek ???
			txtrMcroOperaton.append("T3 -> R"+S2+" <- "+instructionRegister.data.substring(7)+" ,SC <- 0\n");
			SC.T=0;   
		}
		else if(Q==0&&SC.T==3)
		{
			addressRegister.data=Integer.parseInt(instructionRegister.data.substring(7),2);
			txtrMcroOperaton.append("T3 -> AR <- "+instructionRegister.data.substring(7)+"\n");
			SC.inc();
		}
		else if(Q==0&&SC.T==4)
		{
			DataMemory.write(addressRegister.data,convertBinary(DestReg.data, DestReg.wordSize));
			txtrMcroOperaton.append("T4 -> DM["+addressRegister.data+"] <- R"+destination+" ,SC <- 0\n");
			SC.T=0;
		}
	}

	public void TSF()
	{
		if(SC.T==3)
		{
			DestReg.data=S1reg.data;
			txtrMcroOperaton.append("T3 -> R"+destination+" <- "+S1reg.data+" ,SC <- 0\n");
			SC.T=0;
		}

	}

	private void CAL()
	{
		if(SC.T == 3)
		{
			StackMemory.write(stackPointer.data, convertBinary(programCounter.data,programCounter.wordSize));
			txtrMcroOperaton.append("T3 -> SM["+stackPointer.data+"] <- "+programCounter.data+"\n");
			SC.inc();
		}
		else if(SC.T == 4)
		{
			programCounter.data = Integer.parseInt(instructionRegister.data.substring(6, 11),2);
			txtrMcroOperaton.append("T4 -> PC <- "+instructionRegister.data.substring(6)+",\nSP <- SP + 1 ,SC <- 0\n");
			stackPointer.increment();
			SC.T = 0;
		}
	}

	private void RET()
	{
		if(SC.T == 3)
		{
			stackPointer.decrement();
			txtrMcroOperaton.append("T3 -> SP <- SP - 1\n");
			SC.inc();
		}
		else if(SC.T == 4)
		{
			programCounter.data = Integer.parseInt(StackMemory.read(stackPointer.data),2);
			txtrMcroOperaton.append("T4 -> PC <- SM["+stackPointer.data+"] ,SC <- 0\n");
			SC.T = 0;
		}
	}

	private void JMP()
	{
		if(SC.T == 3 && Q == 0)
		{
			programCounter.data = Integer.parseInt(instructionRegister.data.substring(6, 11),2);
			txtrMcroOperaton.append("T3 -> PC <- "+instructionRegister.data.substring(6)+" ,SC <- 0\n");
		}
		else if(SC.T == 3 && Q == 1 && overFlow == 1)
		{
			programCounter.data = Integer.parseInt(instructionRegister.data.substring(6, 11),2);
			txtrMcroOperaton.append("T3 -> PC <- "+instructionRegister.data.substring(6)+" ,SC <- 0\n");
		}
		SC.T = 0;
	}

	private void JMR()
	{
		if(SC.T == 3)
		{
			if(instructionRegister.data.charAt(7) == '0')
			{
				programCounter.data = Integer.parseInt(instructionRegister.data.substring(7, 11),2) + programCounter.data;
			}
			else
			{
				int x = Integer.parseInt(instructionRegister.data.substring(7, 11),2) - 16;
				programCounter.data = x + programCounter.data;
			}
			txtrMcroOperaton.append("T3 -> PC <- PC + "+instructionRegister.data.substring(7)+" ,SC <- 0\n");
			SC.T = 0;
		}
	}

	private void PSH()
	{
		if(SC.T == 3)
		{
			addressRegister.data = Integer.parseInt(instructionRegister.data.substring(7, 11),2);
			txtrMcroOperaton.append("T3 -> AR <- "+instructionRegister.data.substring(7)+"\n");
			SC.inc();
		}
		else if(SC.T == 4)
		{
			if(instructionRegister.data.substring(5, 7).equals("00"))
			{
				StackMemory.write(stackPointer.data, instructionRegister.data.substring(7));
				txtrMcroOperaton.append("T4 -> SM["+stackPointer.data+"] <- "+instructionRegister.data.substring(7)+"\n");
			}
			else if(instructionRegister.data.substring(5, 7).equals("01"))  // from instruction memory
			{
				StackMemory.write(stackPointer.data, InstructionMemory.read(addressRegister.data).substring(7));
				txtrMcroOperaton.append("T4 -> SM["+stackPointer.data+"] <- IM["+addressRegister.data+"]\n");
			}
			else if(instructionRegister.data.substring(5, 7).equals("10")) // from data memory
			{
				StackMemory.write(stackPointer.data, DataMemory.read(addressRegister.data));
				txtrMcroOperaton.append("T4 -> SM["+stackPointer.data+"] <- DM["+addressRegister.data+"]\n");
			}
			else if(instructionRegister.data.substring(5, 7).equals("11")) // from label table
			{
				StackMemory.write(stackPointer.data, instructionRegister.data.substring(7));
				txtrMcroOperaton.append("T4 -> SM["+stackPointer.data+"] <- "+instructionRegister.data.substring(7)+"\n");
			}
			//StackMemory.write(stackPointer.data, DataMemory.read(addressRegister.data));
			
			SC.inc();
		}
		else if(SC.T == 5)
		{
			stackPointer.increment();
			txtrMcroOperaton.append("T5 -> SP <- SP + 1 ,SC <- 0\n");
			SC.T = 0;
		}
	}
	private void POP()
	{
		if(SC.T == 3)
		{
			addressRegister.data = Integer.parseInt(instructionRegister.data.substring(7, 11),2);
			txtrMcroOperaton.append("T3 -> AR <- "+instructionRegister.data.substring(7)+"\n");
			SC.inc();
		}
		else if(SC.T == 4)
		{
			stackPointer.decrement();
			txtrMcroOperaton.append("T4 -> SP <- SP - 1\n");
			SC.inc();
		}
		else if(SC.T == 5)
		{
			DataMemory.write(addressRegister.data, StackMemory.read(stackPointer.data));
			txtrMcroOperaton.append("T5 -> DM["+addressRegister.data+"] <- SM["+stackPointer.data+"] ,SC <- 0\n");
			SC.T = 0;
		}
	}

	public String convertBinary(int x,int length)
	{
		
		String temp = Integer.toBinaryString(x);
		while(temp.length()<length) temp = "0" + temp;
		if(x < 0)
		{
			temp = temp.substring(temp.length()-5);
		}

		return temp;
	}

	public void printingScreen()
	{
		if(numberType == 0) // Binary
		{
			lblArvalue.setText(convertBinary(addressRegister.data,addressRegister.wordSize));
			lblPCValue.setText(convertBinary(programCounter.data,programCounter.wordSize));
			lblSPValue.setText(convertBinary(stackPointer.data,stackPointer.wordSize));
			lblInstValue.setText(instructionRegister.data);
			//lblInputRegValue.setText(convertBinary(inputRegister.data,inputRegister.wordSize));
			lblOutputValue.setText(convertBinary(outputRegister.data,outputRegister.wordSize));
			lblReg0Value.setText(convertBinary(register0.data,register0.wordSize));
			lblReg1Value.setText(convertBinary(register1.data,register1.wordSize));
			lblReg2Value.setText(convertBinary(register2.data,register2.wordSize));

			// Label Table
			table_LabelTable.setModel(new DefaultTableModel(
					labelData,
					new String[] {
							"Label","Address","Value"
					}
					));

			// Instruction Table

			table_Instruction.setModel(new DefaultTableModel(
					InstructionMemory.createTableBinary(),
					new String[] {
							"Address", "Value"
					}
					));

			// Data Table

			table_Data.setModel(new DefaultTableModel(
					DataMemory.createTableBinary(),
					new String[] {
							"Address", "Value"
					}
					));
			// Stack Table

			table_Stack.setModel(new DefaultTableModel(
					StackMemory.createTableBinary(),
					new String[] {
							"Address", "Value"
					}
					));


		}
		else if(numberType == 1) // Decimal
		{
			lblArvalue.setText(Integer.toString(addressRegister.data));
			lblPCValue.setText(Integer.toString(programCounter.data));
			lblSPValue.setText(Integer.toString(stackPointer.data));
			lblInstValue.setText(Integer.toString(Integer.parseInt(instructionRegister.data,2)));
			//lblInputRegValue.setText(Integer.toString(inputRegister.data));
			lblOutputValue.setText(Integer.toString(outputRegister.data));
			lblReg0Value.setText(Integer.toString(register0.data));
			lblReg1Value.setText(Integer.toString(register1.data));
			lblReg2Value.setText(Integer.toString(register2.data));

			// Label Table
			table_LabelTable.setModel(new DefaultTableModel(
					convertingLabelTable(),
					new String[] {
							"Label","Address","Value"
					}
					));

			// Instruction Table

			table_Instruction.setModel(new DefaultTableModel(
					InstructionMemory.createTableDecimal(),
					new String[] {
							"Address", "Value"
					}
					));

			// Data Table

			table_Data.setModel(new DefaultTableModel(
					DataMemory.createTableDecimal(),
					new String[] {
							"Address", "Value"
					}
					));
			// Stack Table

			table_Stack.setModel(new DefaultTableModel(
					StackMemory.createTableDecimal(),
					new String[] {
							"Address", "Value"
					}
					));
		}
		else if(numberType == 2) // HexaDecimal
		{
			lblArvalue.setText(Integer.toHexString(addressRegister.data));
			lblPCValue.setText(Integer.toHexString(programCounter.data));
			lblSPValue.setText(Integer.toHexString(stackPointer.data));
			lblInstValue.setText(Integer.toHexString(Integer.parseInt(instructionRegister.data,2)));
			//lblInputRegValue.setText(Integer.toHexString(inputRegister.data));
			lblOutputValue.setText(Integer.toHexString(outputRegister.data));
			lblReg0Value.setText(Integer.toHexString(register0.data));
			lblReg1Value.setText(Integer.toHexString(register1.data));
			lblReg2Value.setText(Integer.toHexString(register2.data));

			// Label Table
			table_LabelTable.setModel(new DefaultTableModel(
					convertingLabelTable(),
					new String[] {
							"Label","Address","Value"
					}
					));

			// Instruction Table

			table_Instruction.setModel(new DefaultTableModel(
					InstructionMemory.createTableHexaDecimal(),
					new String[] {
							"Address", "Value"
					}
					));

			// Data Table

			table_Data.setModel(new DefaultTableModel(
					DataMemory.createTableHexaDecimal(),
					new String[] {
							"Address", "Value"
					}
					));
			// Stack Table

			table_Stack.setModel(new DefaultTableModel(
					StackMemory.createTableHexaDecimal(),
					new String[] {
							"Address", "Value"
					}
					));
		}
	}

	public Object[][] convertingLabelTable()
	{
		Object[][] a = new Object[labelData.length][3];
		if(numberType == 1) // Decimal
		{
			for (int i = 0; i < labelData.length; i++) {
				if(labelData[i][1] != null)
				{
					a[i][0] = labelData[i][0];
					a[i][1] = Integer.parseInt(labelData[i][1],2);
					a[i][2] = Integer.parseInt(labelData[i][2], 2);
				}

			}
		}
		else if(numberType == 2) // HexaDecimal
		{
			for (int i = 0; i < labelData.length; i++) {
				if(labelData[i][1] != null)
				{
					a[i][0] = labelData[i][0];
					a[i][1] = Integer.toHexString(Integer.parseInt(labelData[i][1],2));
					a[i][2] = Integer.toHexString(Integer.parseInt(labelData[i][2],2));
				}

			}
		}

		return a;
	}

	public void exportMIF()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("choosertitle");
		//chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//chooser.setFileSelectionMode(JFileChooser.);
		FileFilter filter = new FileNameExtensionFilter("MIF File","mif");
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setSelectedFile(new File("InstructionMemory.mif"));



		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			//System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			//System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
		} else {
			//System.out.println("No Selection ");
			return;
		}

		String fileName = chooser.getSelectedFile().getPath();

		File f = new File(fileName);
		FileWriter fw;
		BufferedWriter bw;
		try {
			fw = new FileWriter(f,true);
			bw = new BufferedWriter(fw);

			bw.write("DEPTH = "+InstructionMemory.numberOfWord+";\n");
			bw.write("WIDTH = "+InstructionMemory.wordSize+";\n");
			bw.write("ADDRESS_RADIX = DEC;\n");
			bw.write("DATA_RADIX = BIN;\n");
			bw.write("CONTENT\n");
			bw.write("BEGIN\n");

			for (int i = 0; i < InstructionMemory.data.length; i++) {
				bw.write(i+" : ");
				if(InstructionMemory.data[i] != null)
				{
					bw.write(InstructionMemory.data[i] + ";\n");
				}
				else
				{
					String temp = "00000000000;\n";
					bw.write(temp);
				}
			}
			bw.write("END;\n");
			bw.flush();
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
