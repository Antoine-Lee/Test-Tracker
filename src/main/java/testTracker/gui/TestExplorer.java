package testTracker.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TestExplorer extends JFrame {

	private JPanel contentPane;
	
	public static ArrayList <Test> tests = new ArrayList <Test>(); // GLOBAL ALL TESTS (static)
	private ArrayList <Test> filteredTests = new ArrayList <Test>(); 
	
	private JComboBox<String> cmbSubjectFilter; 

	private JPanel pnlChart; 
	private JToggleButton tglSort;
	private JTable tblTestList;
	private JLabel lblWarning;

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
		lblTests.setBounds(32, 18, 61, 16);
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
		cmbSubjectFilter.setBounds(79, 13, 216, 27);
		contentPane.add(cmbSubjectFilter);
		
		DefaultTableModel tblModel = new DefaultTableModel () {
			@Override
		    public boolean isCellEditable(int row, int column) { return false; } // make uneditable
		}; 
		
		Vector<String> columnTitles = new Vector<String> ();
		columnTitles.add("Subject"); 
		columnTitles.add("Test"); 
		columnTitles.add("Score"); 
		columnTitles.add("Level"); 
		columnTitles.add("Mock");  
		tblModel.setColumnIdentifiers(columnTitles);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(32, 52, 318, 250);
		contentPane.add(scrollPane);

		tblTestList = new JTable(tblModel);
		tblTestList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				hideWarning(); 
			}
		});
		tblTestList.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        if (isSelected)
		        	c.setBackground(Color.BLUE);
		        else
		        	c.setBackground(row % 2 == 0 ? Color.decode("#ebe8e8") : Color.WHITE); 
		        return c;
		    }
		});
		tblTestList.setSelectionMode(0);
//		tblTestList.setSelectionForeground(Color.BLUE);
		scrollPane.setViewportView(tblTestList);
		
		JButton btnNewTest = new JButton("New");
		btnNewTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				newTest(); 
			}
		});
		btnNewTest.setBounds(357, 324, 70, 29);
		contentPane.add(btnNewTest);
		
		JButton btnEditTest = new JButton("Edit");
		btnEditTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				editTest(); 
			}
		});
		btnEditTest.setBounds(424, 324, 70, 29);
		contentPane.add(btnEditTest);
		
		JButton btnDeleteTest = new JButton("Delete");
		btnDeleteTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				deleteTest(); 
			}
		});
		btnDeleteTest.setBounds(489, 324, 70, 29);
		contentPane.add(btnDeleteTest);
		
		JButton btnMock = new JButton("Mock");
		btnMock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				toggleMock(); 
			}
		});
		btnMock.setBounds(554, 324, 70, 29);
		contentPane.add(btnMock);
		
		JButton btnShowLog = new JButton("L");
		btnShowLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				for (int i = 0; i < Test.getChangeLog().size(); i++)
					System.out.println(Test.getChangeLog().get(i)); 
			}
		});
		btnShowLog.setBounds(489, 13, 35, 29);
		contentPane.add(btnShowLog);
		
		pnlChart = new JPanel();
		pnlChart.setBounds(364, 52, 254, 250);
		contentPane.add(pnlChart);
		
		tglSort = new JToggleButton("Sort");
		tglSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				buildTestList(); 
				drawChart(); 
			}
		});
		tglSort.setBounds(303, 13, 51, 29);
		contentPane.add(tglSort);
		
		JButton btnDashboard = new JButton("D");
		btnDashboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dashboard dashboard = new Dashboard();
				dashboard.setVisible(true);
				setVisible(false); 
			}
		});
		btnDashboard.setBounds(536, 13, 35, 29);
		contentPane.add(btnDashboard);
		
		JButton btnSettings = new JButton("S");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSettingsWindow(); 
			}
		});
		btnSettings.setBounds(583, 13, 35, 29);
		contentPane.add(btnSettings);
		
		lblWarning = new JLabel("");
		lblWarning.setForeground(new Color(255, 52, 48));
		lblWarning.setBounds(32, 329, 318, 16);
		contentPane.add(lblWarning);
		
//		tests.add(new Test("Test 1", 88, "Test 1 reflection", 100, "Math"));
//		tests.add(new MockExam("Test 1", 96, "Test 1 reflection", 120, "Math"));
//		tests.add(new Test("Test 2", 81, "Test 2 reflection", 120, "Computer Science"));
//		tests.add(new Test("Test 3", 92, "Test 3 reflection", 130, "English"));
//		tests.add(new Test("Test 4", 78, "Test 4 reflection", 80, "Economics"));
		
		DataManager.loadData(); 
		
		resetSubjectFilter();
		buildTestList(); 
		drawChart(); 
		
		
		
//		for (int i = 0; i < Test.globalSubjects.size(); i++)
//			Test.globalTargets.add(Integer.valueOf(85)); 
//		
//		System.out.println(Test.globalTargets);
	}
	
	private void resetSubjectFilter () 
	{
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		
		model.addElement("All");
		for (String subject : Test.globalSubjects)  
		    model.addElement(subject); 
		
		cmbSubjectFilter.setModel(model);
	}
	
	@SuppressWarnings("unchecked")
	private void buildTestList ()
	{
		if (cmbSubjectFilter.getEditor().getItem().toString().equals("All"))
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
					if (filteredTests.get(i).testPercentage(true) < filteredTests.get(i + 1).testPercentage(true)) 
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
			testData.add(test.getLevel() + ""); 
			testData.add(test instanceof MockExam ? "Y" : "N"); 
			
			testTable.addRow(testData);
		}
	}
	
	private void newTest () // create new test entry
	{
		TestEditor testEditor = new TestEditor (this);
		
		testEditor.setVisible(true);
		this.setVisible(false);
		
		hideWarning();
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
			DataManager.saveData();
		}
		
		super.setVisible(visible);
	}
	
	public void deleteTest()
	{
		int selectedIndex = tblTestList.getSelectedRow(); 
		
		if (selectedIndex == -1)
		{
			showWarning ("Please select a test");
			return; 
		}
		
		Test targetTest = filteredTests.get(selectedIndex); 
		tests.remove(targetTest); 
		
		Test.removeInvalidSubjects();
		
		DataManager.saveData();
		buildTestList(); 
		
		hideWarning();
	}
	
	private void editTest()
	{
		if (tblTestList.getSelectedRow() == -1)
		{
			showWarning ("Please select a test");
			return; 
		}
		
		Test targetTest = filteredTests.get(tblTestList.getSelectedRow()); 
		TestEditor editor = new TestEditor(this, targetTest); 
		
		editor.setVisible(true);
		this.setVisible(false);
		
		hideWarning();
	}
	
	private void toggleMock() // toggles status of test between mock and normal test
	{
		int selectedIndex = tblTestList.getSelectedRow(); 
		
		if (selectedIndex == -1)
		{
			showWarning ("Please select a test"); 
			return; 
		}

		Test oldTest = (Test) filteredTests.get(selectedIndex);
		mockToggle (oldTest); 
		
		hideWarning();
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
		DataManager.saveData();
	}
	
	
	
	private void drawChart()
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (Test test : filteredTests)
			dataset.addValue(test.testPercentage(false), test.getTestName(), "");
		
		JFreeChart chart = ChartFactory.createBarChart(
			cmbSubjectFilter.getEditor().getItem().toString(), 
			"Tests", 
			"Results", 
			dataset, 
			PlotOrientation.VERTICAL, 
			!cmbSubjectFilter.getEditor().getItem().toString().equals("All"), 
			false, 
			false
		); 
		
	    String fontName = "Lucida Sans";

	    StandardChartTheme theme = (StandardChartTheme)org.jfree.chart.StandardChartTheme.createJFreeTheme();

	    theme.setTitlePaint( Color.decode( "#4572a7" ) );
	    theme.setExtraLargeFont( new Font(fontName,Font.PLAIN, 16) ); //title
	    theme.setLargeFont( new Font(fontName,Font.BOLD, 15)); //axis-title
	    theme.setRegularFont( new Font(fontName,Font.PLAIN, 11));
	    theme.setRangeGridlinePaint( Color.decode("#C0C0C0"));
	    theme.setPlotBackgroundPaint( Color.white );
	    theme.setChartBackgroundPaint( Color.white );
	    theme.setGridBandPaint( Color.red );
	    theme.setAxisOffset( new RectangleInsets(0,0,0,0) );
	    theme.setBarPainter(new StandardBarPainter());
	    theme.setAxisLabelPaint( Color.decode("#666666")  );
	    theme.apply( chart );
	    chart.getCategoryPlot().setOutlineVisible( false );
	    chart.getCategoryPlot().getRangeAxis().setAxisLineVisible( false );
	    chart.getCategoryPlot().getRangeAxis().setTickMarksVisible( false );
	    chart.getCategoryPlot().setRangeGridlineStroke( new BasicStroke() );
	    chart.getCategoryPlot().getRangeAxis().setTickLabelPaint( Color.decode("#666666") );
	    chart.getCategoryPlot().getDomainAxis().setTickLabelPaint( Color.decode("#666666") );
	    chart.setTextAntiAlias( true );
	    chart.setAntiAlias( true );
	    chart.getCategoryPlot().getRenderer().setSeriesPaint( 0, Color.decode( "#4572a7" ));
	    BarRenderer rend = (BarRenderer) chart.getCategoryPlot().getRenderer();
	    rend.setShadowVisible( true );
	    rend.setShadowXOffset( 2 );
	    rend.setShadowYOffset( 0 );
	    rend.setShadowPaint( Color.decode( "#C0C0C0"));
	    rend.setMaximumBarWidth( 0.1);
	    chart.setPadding(new RectangleInsets(15,5,5,10)); // top left bottom right
		
		pnlChart.removeAll(); 
		pnlChart.setLayout(new BorderLayout(0, 0));
		ChartPanel chartPanel = new ChartPanel(chart);
		pnlChart.add(chartPanel, BorderLayout.CENTER);
		this.repaint();
		
		super.setVisible(true); // solves bug where graph disappears on rerender
	}
	
	private void openSettingsWindow()
	{
		Settings settings = new Settings(this); 
		settings.setVisible(true);
		setVisible(false); 
	}
	
	private void showWarning(String warning)
	{
		lblWarning.setText("WARNING: " + warning);
	}
	
	private void hideWarning()
	{
		lblWarning.setText("");
	}
}
