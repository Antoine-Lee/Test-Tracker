package testTracker.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	private JTextArea jtaDescription;
	private JLabel lblScorePercValue;
	
	private Boolean initialising = true; 
	
	private WarningManager warningManager = null; 
	private String originalName = ""; 

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
		this(); // call constructor overload
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
		initialising = true; 
		
		setResizable(false);
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (650 / 2)), 150, 500, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTestName = new JLabel("Test Name");
		lblTestName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTestName.setBounds(7, 53, 99, 16);
		contentPane.add(lblTestName);
		
		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSubject.setBounds(45, 86, 61, 16);
		contentPane.add(lblSubject);
		
		JLabel lblScore = new JLabel("Marks");
		lblScore.setHorizontalAlignment(SwingConstants.RIGHT);
		lblScore.setBounds(45, 142, 61, 16);
		contentPane.add(lblScore);
		
		JLabel lblTotalMarks = new JLabel("Total");
		lblTotalMarks.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalMarks.setBounds(45, 175, 61, 16);
		contentPane.add(lblTotalMarks);
		
		txtTestName = new JTextField();
		txtTestName.addKeyListener(new KeyAdapter() {
			@Override
			 public void keyPressed(KeyEvent e) {
				if (txtTestName.getText().length() > ColourManager.charLimit)
					txtTestName.setText(txtTestName.getText().substring(0, ColourManager.charLimit)); 
			}
		});
		txtTestName.setBounds(118, 48, 95, 27);
		contentPane.add(txtTestName);
		txtTestName.setColumns(10);
		
		cmbSubject = new JComboBox();
		cmbSubject.setEditable(true);
		cmbSubject.setBounds(118, 81, 95, 27);
		contentPane.add(cmbSubject);
		
		spnTotal = new JSpinner();
		spnTotal.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (initialising) return; 
				validateSpinner(spnTotal, 1, 1000); 
				updatePercentage(); 
				if ((Integer) spnTotal.getValue() < (Integer)spnScore.getValue())
					spnScore.setValue((Integer)spnTotal.getValue());
			}
		});
		spnTotal.setBounds(118, 170, 95, 26);
		spnTotal.setValue(100);
		contentPane.add(spnTotal);
		
		spnScore = new JSpinner();
		spnScore.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (initialising) return; 
				validateSpinner(spnScore, 0, (Integer)spnTotal.getValue()); 
				updatePercentage(); 
			}
		});
		spnScore.setBounds(118, 137, 95, 26);
		spnScore.setValue(85);
		contentPane.add(spnScore);
		
		JLabel lblReflection = new JLabel("Reflection");
		lblReflection.setBounds(241, 121, 99, 16);
		contentPane.add(lblReflection);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(241, 142, 236, 56);
		contentPane.add(scrollPane_1);
		
		jtaReflection = new JTextArea();
		scrollPane_1.setViewportView(jtaReflection);
		jtaReflection.setLineWrap(true);
		
		chkMock = new JCheckBox("");
		chkMock.setBounds(118, 248, 20, 20);
		contentPane.add(chkMock);
		
		JButton btlCancel = new JButton("Cancel");
		btlCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				cancel(); 
			}
		});
		btlCancel.setBounds(319, 245, 74, 29);
		contentPane.add(btlCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				saveTest(); 
			}
		});
		btnSave.setBounds(406, 245, 74, 29);
		contentPane.add(btnSave);
		
		JLabel lblScorePercTitle = new JLabel("Percentage");
		lblScorePercTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblScorePercTitle.setBounds(7, 211, 99, 16);
		contentPane.add(lblScorePercTitle);
		
		lblScorePercValue = new JLabel("85%");
		lblScorePercValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblScorePercValue.setBounds(127, 211, 76, 16);
		contentPane.add(lblScorePercValue);
		
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setBounds(241, 29, 99, 16);
		contentPane.add(lblDescription);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(241, 53, 236, 56);
		contentPane.add(scrollPane);
		
		jtaDescription = new JTextArea();
		scrollPane.setViewportView(jtaDescription);
		jtaDescription.setLineWrap(true);
		
		JLabel lblMock = new JLabel("Mock");
		lblMock.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMock.setBounds(45, 252, 61, 16);
		contentPane.add(lblMock);
		
		JLabel lblWarning = new JLabel("");
		lblWarning.setForeground(new Color(255, 52, 48));
		lblWarning.setBounds(241, 210, 236, 16);
		contentPane.add(lblWarning);
		
		warningManager = new WarningManager(lblWarning); 
		
		initialising = false; 
		
		ColourManager.globalStyling(this); 
	}
	
	public TestEditor(TestExplorer parent, Test targetTest) // constructor overload to receiving an existing Test object for editing
	{
		this(parent); 
		this.targetTest = targetTest; 
		originalName = targetTest.getTestName(); 
		if (targetTest != null)
		{
			txtTestName.setText(targetTest.getTestName()); 
			cmbSubject.setSelectedItem(targetTest.getSubject());
			spnScore.setValue(targetTest.getScore());
			spnTotal.setValue(targetTest.getTotal());
			jtaReflection.setText(targetTest.getReflection());
			jtaDescription.setText(targetTest.getDescription());
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
		String nameInput = txtTestName.getText();
		
		if (nameInput.equals(""))
		{
			warningManager.showWarning("Please enter test name");
			warningManager.highlightField(txtTestName);
			return; 
		}
		
		if (cmbSubject.getSelectedItem().toString().equals(""))
		{
			warningManager.showWarning("Please enter a subject");
			warningManager.highlightField(cmbSubject);
			return; 
		}
		
		System.out.println(originalName);
		Boolean validName = true; 
		for (Test test : TestExplorer.tests)
		{
			if (test.getTestName().equals(nameInput) && !nameInput.equals(originalName))
			{
				validName = false; 
				break; 
			}
		}
		
		warningManager.hideWarning(); 
		
		if (!validName)
		{
			warningManager.showWarning("Name already in use");
			warningManager.highlightField(txtTestName);
			return; 
		}
		
		if (targetTest == null) // target test null for new tests
		{
			if (chkMock.isSelected())
				targetTest = new MockExam(txtTestName.getText(), (Integer)spnScore.getValue(), jtaReflection.getText(), jtaDescription.getText(), (Integer)spnTotal.getValue(), (String)cmbSubject.getSelectedItem());
			else
				targetTest = new Test(txtTestName.getText(), (Integer)spnScore.getValue(), jtaReflection.getText(), jtaDescription.getText(), (Integer)spnTotal.getValue(), (String)cmbSubject.getSelectedItem());
			
			parent.storeTest(targetTest);
		}
		else // existing test (editing)
		{
			targetTest.setTestName(txtTestName.getText());
			targetTest.setScore((Integer)spnScore.getValue());
			targetTest.setTotal((Integer)spnTotal.getValue());
			targetTest.setReflection(jtaReflection.getText());
			targetTest.setDescription(jtaDescription.getText()); 
			targetTest.setSubject((String)cmbSubject.getSelectedItem());
			targetTest.calculateLevel(); 
			
			if (chkMock.isSelected() != targetTest instanceof MockExam)
				parent.mockToggle(targetTest);
		}
		
		Test.removeInvalidSubjects(); 
		
		this.setVisible(false);
		parent.setVisible(true);
	}
	
	private void validateSpinner (JSpinner spinner, int min, int max)
	{
		if ((Integer)spinner.getValue() < min)
			spinner.setValue(min);
		else if ((Integer)spinner.getValue() > max)
			spinner.setValue(max);
	}
	
	private void updatePercentage() 
	{
		int score = (Integer) spnScore.getValue();
		int total = (Integer) spnTotal.getValue();
		
		double perc = ((double) score / (double)total * 100);

		DecimalFormat df = new DecimalFormat("#.#");
		lblScorePercValue.setText(df.format(perc) + "%");
	}
}
