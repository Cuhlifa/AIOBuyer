package scripts;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import scripts.GUI.ActionMode;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	
    enum ActionMode {
    	NONE,
        ONCE,
        ALL
    }
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setTitle("AIOBuyer++");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 392, 425);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JList list = new JList();
		list.setFont(new Font("Tahoma", Font.PLAIN, 9));
		list.setBounds(10, 29, 153, 346);
		contentPane.add(list);
		
		JLabel lblNewLabel = new JLabel("Items");
		lblNewLabel.setBounds(10, 11, 46, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblItemId = new JLabel("Item ID:");
		lblItemId.setBounds(175, 31, 46, 14);
		contentPane.add(lblItemId);
		
		textField = new JTextField();
		textField.setBounds(280, 26, 86, 17);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblAddItems = new JLabel("Add items:");
		lblAddItems.setBounds(175, 11, 64, 14);
		contentPane.add(lblAddItems);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(280, 50, 86, 17);
		contentPane.add(textField_1);
		
		JLabel lblLowestStock = new JLabel("Lowest stock:");
		lblLowestStock.setBounds(173, 52, 97, 14);
		contentPane.add(lblLowestStock);
		
		JLabel lblNewLabel_1 = new JLabel("NPC:");
		lblNewLabel_1.setBounds(177, 155, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(177, 169, 55, 14);
		contentPane.add(lblName);
		
		textField_2 = new JTextField();
		textField_2.setBounds(236, 166, 130, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(236, 191, 130, 20);
		contentPane.add(textField_3);
		
		JLabel lblOption = new JLabel("Option:");
		lblOption.setBounds(177, 194, 56, 14);
		contentPane.add(lblOption);
		
		JCheckBox chckbxFreeToPlay = new JCheckBox("Free To Play");
		chckbxFreeToPlay.setBounds(173, 218, 97, 23);
		contentPane.add(chckbxFreeToPlay);
		
		JCheckBox chckbxWorldHop = new JCheckBox("World Hop");
		chckbxWorldHop.setBounds(173, 244, 97, 23);
		contentPane.add(chckbxWorldHop);
		
		JTextArea txtrReportAnyBugs = new JTextArea();
		txtrReportAnyBugs.setFont(new Font("Monospaced", Font.PLAIN, 10));
		txtrReportAnyBugs.setText("Report any bugs on the \r\nscript thread or contact me \r\nthrough teamspeak at \r\nelitescripts.tk");
		txtrReportAnyBugs.setBounds(173, 308, 193, 67);
		contentPane.add(txtrReportAnyBugs);
		
		JCheckBox chckbxOpenPacks = new JCheckBox("Open packs:");
		chckbxOpenPacks.setBounds(169, 101, 121, 23);
		contentPane.add(chckbxOpenPacks);
		
		JCheckBox chckbxBank = new JCheckBox("Bank");
		chckbxBank.setBounds(169, 127, 97, 23);
		contentPane.add(chckbxBank);
		
		JButton btnNewButton_1 = new JButton("Start");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Main.guiComplete = true;
				Main.freeToPlay = chckbxFreeToPlay.isSelected();
				Main.worldHop = chckbxWorldHop.isSelected();
				Main.openPacks = chckbxOpenPacks.isSelected();
				Main.bank = chckbxBank.isSelected();
				Main.npcName = textField_2.getText();
				Main.npcOption = textField_3.getText();
				
				for(int i = 0;i < list.getModel().getSize();i++){
					
					String[] args = list.getModel().getElementAt(i).toString().split(":");
					
					int ID = Integer.parseInt(args[0]);
					int lowestStock = Integer.parseInt(args[1]);
					
					Main.items.add(new Item(ID, lowestStock));
					
				}
				
				Main.guiComplete = true;
				System.out.println("GUI done");
				setVisible(false);
				dispose();
				
			}
		});
		btnNewButton_1.setBounds(177, 274, 189, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("Add item");
	    DefaultListModel model = new DefaultListModel<>();
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String id = textField.getText();
				String stock = textField_1.getText();
				
				if(!stock.equalsIgnoreCase("") && !id.equalsIgnoreCase("")){
					model.addElement(id + ":" + stock);
					list.setModel(model);
				}
			    
			}
		});
		btnNewButton.setBounds(173, 71, 90, 23);
		contentPane.add(btnNewButton);
		
		JButton btnDeleteItem = new JButton("Delete Item");
		btnDeleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(list.getSelectedIndex() >= 0){
					model.remove(list.getSelectedIndex());
				}
				
			}
		});
		btnDeleteItem.setBounds(262, 71, 104, 23);
		contentPane.add(btnDeleteItem);
		
	}
}

