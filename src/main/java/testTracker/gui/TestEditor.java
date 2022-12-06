package testTracker.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class TestEditor extends JFrame {

	private JPanel contentPane;
	private JTextField txtTestName;
	
	private JComboBox<String> cmbSubject; 
	
	private TestExplorer parent; 
	
	private Test targetTest;
	private JCheckBox chkMock;
	
	private JSpinner spnScore;
	private JSpinner spnTotal; 
	private JTextArea jtaReflection; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestEditor frame = new TestEditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public TestEditor (TestExplorer parent)
	{
		this(); // call other constructor
		this.parent = parent; 
		
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		
		for (String subject : Test.globalSubjects)  
		    model.addElement(subject);
		
		cmbSubject.setModel(model);
	}

	/**
	 * Create the frame.
	 */
	public TestEditor() 
	{
		setResizable(false);
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTestName = new JLabel("Test Name");
		lblTestName.setBounds(22, 25, 99, 16);
		contentPane.add(lblTestName);
		
		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setBounds(22, 81, 61, 16);
		contentPane.add(lblSubject);
		
		JLabel lblScore = new JLabel("Marks");
		lblScore.setBounds(22, 134, 61, 16);
		contentPane.add(lblScore);
		
		JLabel lblTotalMarks = new JLabel("Total Marks");
		lblTotalMarks.setBounds(22, 190, 99, 16);
		contentPane.add(lblTotalMarks);
		
		txtTestName = new JTextField();
		txtTestName.setBounds(21, 53, 130, 26);
		contentPane.add(txtTestName);
		txtTestName.setColumns(10);
		
		cmbSubject = new JComboBox();
		cmbSubject.setEditable(true);
		cmbSubject.setBounds(22, 103, 129, 27);
		contentPane.add(cmbSubject);
		
		spnScore = new JSpinner();
		spnScore.setBounds(22, 152, 130, 26);
		contentPane.add(spnScore);
		
		spnTotal = new JSpinner();
		spnTotal.setBounds(22, 207, 130, 26);
		contentPane.add(spnTotal);
		
		JLabel lblReflection = new JLabel("Reflection");
		lblReflection.setBounds(170, 25, 99, 16);
		contentPane.add(lblReflection);
		
		jtaReflection = new JTextArea();
		jtaReflection.setBounds(176, 58, 251, 165);
		contentPane.add(jtaReflection);
		
		chkMock = new JCheckBox("Mock Exam");
		chkMock.setBounds(22, 243, 128, 23);
		contentPane.add(chkMock);
		
		JButton btlCancel = new JButton("Cancel");
		btlCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				cancel(); 
			}
		});
		btlCancel.setBounds(186, 235, 117, 29);
		contentPane.add(btlCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				saveTest(); 
			}
		});
		btnSave.setBounds(297, 235, 117, 29);
		contentPane.add(btnSave);
	}
	
	public TestEditor(TestExplorer parent, Test targetTest) // constructor overload to receiving an existing Test object for editing
	{
		this(parent); 
		this.targetTest = targetTest; 
		if (targetTest != null)
		{
			txtTestName.setText(targetTest.getTestName()); 
			cmbSubject.setSelectedItem(targetTest.getSubject());
			spnScore.setValue(targetTest.getScore());
			spnTotal.setValue(targetTest.getTotal());
			jtaReflection.setText(targetTest.getReflection());
			chkMock.setSelected(targetTest instanceof MockExam);
		}
	}
	
	private void cancel()
	{
		this.setVisible(false);
		parent.setVisible(true); 
	}
	
	private void saveTest()
	{
		if (targetTest == null) // target test null for new tests
		{
			if (chkMock.isSelected())
				targetTest = new MockExam(txtTestName.getText(), (Integer)spnScore.getValue(), jtaReflection.getText(), (Integer)spnTotal.getValue(), (String)cmbSubject.getSelectedItem());
			else
				targetTest = new Test(txtTestName.getText(), (Integer)spnScore.getValue(), jtaReflection.getText(), (Integer)spnTotal.getValue(), (String)cmbSubject.getSelectedItem());
			
			parent.storeTest(targetTest);
		}
		else // existing test (editing)
		{
			targetTest.setTestName(txtTestName.getText());
			targetTest.setScore((Integer)spnScore.getValue());
			targetTest.setTotal((Integer)spnTotal.getValue());
			targetTest.setReflection(jtaReflection.getText());
			targetTest.setSubject((String)cmbSubject.getSelectedItem());
			targetTest.calculateLevel(); 
			
			if (chkMock.isSelected() != targetTest instanceof MockExam)
				parent.mockToggle(targetTest);
		}
		
		Test.removeInvalidSubjects(); 
		
		this.setVisible(false);
		parent.setVisible(true);
	}
}
