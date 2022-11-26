package testTracker.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import javax.swing.JToggleButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TestExplorer extends JFrame {

	private JPanel contentPane;
	
	private ArrayList <Test> tests = new ArrayList <Test>();
	private ArrayList <Test> filteredTests = new ArrayList <Test>(); 
	
	private JComboBox<String> cmbSubjectFilter; 

	private JPanel pnlChart; 
	private JToggleButton tglSort;
	private JTable tblTestList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestExplorer frame = new TestExplorer();
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
	public TestExplorer() 
	{
		setResizable(false);
		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTests = new JLabel("Tests");
		lblTests.setBounds(22, 17, 61, 16);
		contentPane.add(lblTests);
		
		cmbSubjectFilter = new JComboBox();
		cmbSubjectFilter.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) 
			{
				buildTestList(); 
				drawChart(); 
			}
		});
		cmbSubjectFilter.setEditable(true);
		cmbSubjectFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				buildTestList();
				drawChart(); 
			}
		});
		cmbSubjectFilter.setBounds(81, 13, 271, 27);
		contentPane.add(cmbSubjectFilter);
		
		DefaultTableModel tblModel = new DefaultTableModel (); 
		
		Vector<String> columnTitles = new Vector<String> ();
		columnTitles.add("Subject"); 
		columnTitles.add("Test"); 
		columnTitles.add("Score"); 
		columnTitles.add("Mock");  
		tblModel.setColumnIdentifiers(columnTitles);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(32, 56, 318, 298);
		contentPane.add(scrollPane);

		tblTestList = new JTable(tblModel);
		scrollPane.setViewportView(tblTestList);
		
		
		
		JButton btnNewTest = new JButton("New");
		btnNewTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				newTest(); 
			}
		});
		btnNewTest.setBounds(412, 248, 84, 29);
		contentPane.add(btnNewTest);
		
		JButton btnEditTest = new JButton("Edit");
		btnEditTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				editTest(); 
			}
		});
		btnEditTest.setBounds(508, 248, 84, 29);
		contentPane.add(btnEditTest);
		
		JButton btnDeleteTest = new JButton("Delete");
		btnDeleteTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				deleteTest(); 
			}
		});
		btnDeleteTest.setBounds(419, 314, 84, 29);
		contentPane.add(btnDeleteTest);
		
		JButton btnMock = new JButton("Mock");
		btnMock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				toggleMock(); 
			}
		});
		btnMock.setBounds(508, 313, 84, 29);
		contentPane.add(btnMock);
		
		JButton btnShowLog = new JButton("Log");
		btnShowLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				for (int i = 0; i < Test.getChangeLog().size(); i++)
					System.out.println(Test.getChangeLog().get(i)); 
			}
		});
		btnShowLog.setBounds(430, 14, 84, 29);
		contentPane.add(btnShowLog);
		
		pnlChart = new JPanel();
		pnlChart.setBounds(364, 53, 228, 168);
		contentPane.add(pnlChart);
		
		tglSort = new JToggleButton("Sort");
		tglSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				buildTestList(); 
				drawChart(); 
			}
		});
		tglSort.setBounds(375, 12, 51, 29);
		contentPane.add(tglSort);
		
//		tests.add(new Test("Test 1", 88, "Test 1 reflection", 100, "Math"));
//		tests.add(new MockExam("Test 1", 96, "Test 1 reflection", 120, "Math"));
//		tests.add(new Test("Test 2", 81, "Test 2 reflection", 120, "Computer Science"));
//		tests.add(new Test("Test 3", 92, "Test 3 reflection", 130, "English"));
//		tests.add(new Test("Test 4", 78, "Test 4 reflection", 80, "Economics"));
		
		loadData(); 
		
		resetSubjectFilter();
		buildTestList(); 
		drawChart(); 
	}
	
	private void resetSubjectFilter () 
	{
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		
		model.addElement("All Tests");
		for (String subject : Test.globalSubjects)  
		    model.addElement(subject); 
		
		cmbSubjectFilter.setModel(model);
	}
	
	@SuppressWarnings("unchecked")
	private void buildTestList ()
	{
		if (cmbSubjectFilter.getEditor().getItem().toString().equals("All Tests"))
			filteredTests = (ArrayList<Test>)tests.clone(); 
		else
		{
			filteredTests = new ArrayList<Test>(); 
			String searchKey = cmbSubjectFilter.getEditor().getItem().toString().trim().toLowerCase(); 
			
			for (Test test : tests)
			{
				if (test.toString().toLowerCase().contains(searchKey))
					filteredTests.add(test); 
			}
		}
		
		if (tglSort.isSelected())
		{
			boolean searching = true;
			
			while (searching)
			{
				searching = false;
				
				for (int i = 0; i < filteredTests.size() - 1; i++)
				{
					if (filteredTests.get(i).testPercentage(true) > filteredTests.get(i + 1).testPercentage(true)) 
					{
						Test tempTest = filteredTests.get(i); 
						filteredTests.set(i, filteredTests.get(i + 1)); 
						filteredTests.set(i + 1, tempTest); 
						searching = true; 
					}
				}
				
			}
		}
		
		DefaultTableModel testTable = (DefaultTableModel)tblTestList.getModel();
		
		testTable.setRowCount(0); // remove old rows (building new list)
		
		for (Test test : filteredTests)
		{
			Vector<String> testData = new Vector<String>(); 
			
			testData.add(test.getSubject()); 
			testData.add(test.getTestName());
			testData.add(test.testPercentage());
			testData.add(test instanceof MockExam ? "Y" : "N"); 
			
			testTable.addRow(testData);
		}
	}
	
	private void newTest () // create new test entry
	{
		TestEditor testEditor = new TestEditor (this);
		
		testEditor.setVisible(true);
		this.setVisible(false);
	}
	
	public void storeTest(Test newTest)
	{
		tests.add(newTest); 
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		if (visible)
		{
			resetSubjectFilter(); 
			buildTestList(); 
			drawChart(); 
			saveData();
		}
		
		super.setVisible(visible);
	}
	
	public void deleteTest()
	{
		int selectedIndex = tblTestList.getSelectedRow(); 
		
		if (selectedIndex != -1)
		{
			Test targetTest = filteredTests.get(selectedIndex); 
			tests.remove(targetTest); 
			buildTestList(); 
		}
		
		saveData();
	}
	
	private void editTest()
	{
		if (tblTestList.getSelectedRow() == -1)
			return; 
		
		Test targetTest = filteredTests.get(tblTestList.getSelectedRow()); 
		TestEditor editor = new TestEditor(this, targetTest); 
		
		editor.setVisible(true);
		this.setVisible(false);
	}
	
	private void toggleMock() // toggles status of test between mock and normal test
	{
		int selectedIndex = tblTestList.getSelectedRow(); 
		
		if (selectedIndex == -1)
			return; 

		Test oldTest = (Test) filteredTests.get(selectedIndex);
		mockToggle (oldTest); 
	}
	
	public void mockToggle(Test oldTest)
	{
		Test newTest; 
		
		if (oldTest instanceof MockExam)
			newTest = new Test(oldTest.getTestName(), oldTest.getScore(), oldTest.getReflection(), oldTest.getTotal(), oldTest.getSubject());
		else
			newTest = new MockExam(oldTest.getTestName(), oldTest.getScore(), oldTest.getReflection(), oldTest.getTotal(), oldTest.getSubject());
		
		tests.add(tests.indexOf(oldTest), newTest); 
		tests.remove(oldTest); 
		
		buildTestList();
		drawChart(); 
		saveData();
	}
	
	private void saveData()
	{
		try
		{
			FileOutputStream FOS = new FileOutputStream("TestData.dat"); 
			ObjectOutputStream OOS = new ObjectOutputStream(FOS); 
			OOS.writeObject(tests);
			OOS.writeObject(Test.getGlobalSubjects());
			OOS.writeObject(Test.getChangeLog()); 
			OOS.close(); 
			FOS.close(); 
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	private void loadData()
	{
		try
		{
			FileInputStream FIS = new FileInputStream ("TestData.dat"); 
			ObjectInputStream OIS = new ObjectInputStream(FIS); 
			
			tests = (ArrayList<Test>)OIS.readObject(); 
			Test.setGlobalSubjects((ArrayList<String>)OIS.readObject());
			Test.setChangeLog((ArrayList<String>)OIS.readObject());
			OIS.close();
			FIS.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void drawChart()
	{
		System.out.println("DRAW CHART");
//		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		DefaultPieDataset dataset = new DefaultPieDataset(); 
		
		for (Test test : filteredTests)
			dataset.setValue(test.getTestName(), test.testPercentage(false));  
//			dataset.addValue(test.testPercentage(false), test.getTestName(), "");
		
//		JFreeChart chart = ChartFactory.createBarChart(
//			"Test Results: " + cmbSubjectFilter.getSelectedItem().toString(), 
//			"Tests", 
//			"Results", 
//			dataset, 
//			PlotOrientation.VERTICAL, 
//			false, 
//			false, 
//			false
//		); 
		
		JFreeChart chart = ChartFactory.createPieChart("Test Results: " + cmbSubjectFilter.getSelectedItem().toString(), dataset); 
		
		pnlChart.removeAll(); 
		pnlChart.setLayout(new BorderLayout(0, 0));
		ChartPanel chartPanel = new ChartPanel(chart);
		pnlChart.add(chartPanel, BorderLayout.CENTER);
		this.repaint(); 
	}
}
