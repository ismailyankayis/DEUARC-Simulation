import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class firstPage {

	private DeuArc deu = new DeuArc();
	private JFrame firstPage;
	
	JTextField fileField = new JTextField("Browse an asm/basm file...");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					firstPage window = new firstPage();
					window.firstPage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public firstPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		firstPage = new JFrame();
		firstPage.setTitle("DEUARC SIMULATOR");
		firstPage.setBounds(100, 100, 432, 511);
		firstPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		firstPage.getContentPane().setLayout(null);

		JButton btnRead = new JButton("Read");
		btnRead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(DeuArc.file != null)
				{

					
					deu.initialize();
					
					try {
						deu.readAssembly();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// Label Table
					DeuArc.table_LabelTable.setModel(new DefaultTableModel(
							DeuArc.labelData,
							new String[] {
								"Label","Address","Value"
							}
						));
					
					// Instruction Table
					
					DeuArc.table_Instruction.setModel(new DefaultTableModel(
							DeuArc.InstructionMemory.createTableBinary(),
							new String[] {
								"Address", "Value"
							}
						));
					
					// Data Table
					
					DeuArc.table_Data.setModel(new DefaultTableModel(
							DeuArc.DataMemory.createTableBinary(),
							new String[] {
								"Address", "Value"
							}
						));
					
					// Stack Table
					
					DeuArc.table_Stack.setModel(new DefaultTableModel(
							DeuArc.StackMemory.createTableBinary(),
							new String[] {
								"Address", "Value"
							}
						));
					deu.initializeRegisters();
					//firstPage = deu.frmDeuarcSmulator;
					firstPage.setVisible(false);
					deu.frmDeuarcSmulator.setVisible(true);
				}
			}
		});
		btnRead.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnRead.setBounds(119, 206, 144, 78);
		firstPage.getContentPane().add(btnRead);

		JButton btnBrowse = new JButton(". . . ");
		btnBrowse.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				
				FileFilter filter = new FileNameExtensionFilter("ASM File,BASM File", new String[] {"asm", "basm"});
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileFilter(filter);
				
				int returnVal = fileChooser.showOpenDialog(fileChooser);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					DeuArc.file = fileChooser.getSelectedFile();
					if (DeuArc.file.getName().substring(DeuArc.file.getName().length()-4,DeuArc.file.getName().length()).equalsIgnoreCase(".asm") || 
							DeuArc.file.getName().substring(DeuArc.file.getName().length()-5,DeuArc.file.getName().length()).equalsIgnoreCase(".basm")) {
						fileField.setText(DeuArc.file.getName());
					}
					else{
						fileField.setText("Wrong File Format");
					}
				}
			}
		});
		btnBrowse.setBounds(317, 136, 65, 23);
		firstPage.getContentPane().add(btnBrowse);

		
		fileField.setBounds(28, 137, 279, 20);
		firstPage.getContentPane().add(fileField);
		fileField.setColumns(10);
		
		JLabel lblDeuarcSmulator = new JLabel("DEUARC SIMULATOR");
		lblDeuarcSmulator.setFont(new Font("Tahoma", Font.BOLD, 23));
		lblDeuarcSmulator.setBounds(82, 30, 268, 43);
		firstPage.getContentPane().add(lblDeuarcSmulator);
	}

}
