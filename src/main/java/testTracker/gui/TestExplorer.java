package testTracker.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class TestExplorer extends JFrame {

	private JPanel contentPane;
	
	public static ArrayList <Test> tests = new ArrayList <Test>(); // GLOBAL ALL TESTS (static)
	private ArrayList <Test> filteredTests = new ArrayList <Test>(); 
	
	private JComboBox<String> cmbSubjectFilter; 

	private JPanel pnlChart; 
	private JTable tblTestList;
	private JLabel lblWarning;
	
	private WarningManager warningManager = null; 
	private JCheckBox chkMock;
	private JLabel lblMock; 
	private JComboBox cmbSortType;

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
		
		this.setTitle("Test Explorer");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (650 / 2)), 150, 650, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		cmbSubjectFilter = new JComboBox();
		cmbSubjectFilter.setEditable(true);
		cmbSubjectFilter.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) 
			{
				buildTestList(); 
				drawChart(); 
			}
		});
		cmbSubjectFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				buildTestList();
				drawChart(); 
			}
		});
		cmbSubjectFilter.setBounds(32, 13, 218, 27);
		contentPane.add(cmbSubjectFilter);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(32, 52, 318, 250);
		contentPane.add(scrollPane);
		
		chkMock = new JCheckBox("");
		chkMock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				if (!selectedNewTest)
//					return; 
				
				toggleMock();
			}
		});
		chkMock.setBounds(330, 326, 20, 20);
		contentPane.add(chkMock);
		
		lblMock = new JLabel("Mock");
		lblMock.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMock.setBounds(255, 329, 61, 16);
		contentPane.add(lblMock);
		
		
		
		
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

		tblTestList = new JTable(tblModel);
		tblTestList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				warningManager.hideWarning();
				
				if (tblTestList.getSelectedRow() == -1)
					return; 
				
				chkMock.setVisible(true);
				lblMock.setVisible(true);
				chkMock.setSelected(((Test) filteredTests.get(tblTestList.getSelectedRow()) instanceof MockExam));
				
			}
		});
		tblTestList.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        if (isSelected)
		        	c.setBackground(Color.decode(ColourManager.colourGreen));
		        else
		        	c.setBackground(row % 2 == 0 ? Color.decode(ColourManager.colourButton1) : Color.decode(ColourManager.colourButton2));
		        
	        	setHorizontalAlignment(column != 1 ? JLabel.CENTER : JLabel.LEADING);
		        
		        return c;
		    }
		});
		tblTestList.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        JTable table =(JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
		        	editTest();
		        }
		    }
		});
		tblTestList.setSelectionMode(0);
		scrollPane.setViewportView(tblTestList);
		

		TableColumnModel columnModel = tblTestList.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(30);
		columnModel.getColumn(1).setPreferredWidth(60);
		columnModel.getColumn(2).setPreferredWidth(5);
		columnModel.getColumn(3).setPreferredWidth(5);
		columnModel.getColumn(4).setPreferredWidth(5);
		tblTestList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		((DefaultTableCellRenderer)tblTestList.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		JLabel lblNewTestButton = new JLabel("");
		lblNewTestButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { newTest(); }
		});
		lblNewTestButton.setBounds(32, 314, 35, 35);
		ColourManager.styleLabelIcon(lblNewTestButton, new ImageIcon(TestExplorer.class.getResource("/icons/new.png")), 20); 
		contentPane.add(lblNewTestButton);
		
		JLabel lblEditTestButton = new JLabel("");
		lblEditTestButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { editTest();  }
		});
		lblEditTestButton.setBounds(79, 314, 35, 35);
		ColourManager.styleLabelIcon(lblEditTestButton, new ImageIcon(TestExplorer.class.getResource("/icons/pencil.png")), 20); 
		contentPane.add(lblEditTestButton);

		JLabel lblDeleteTestButton = new JLabel("");
		lblDeleteTestButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { deleteTest();  }
		});
		lblDeleteTestButton.setBounds(126, 314, 35, 35);
		ColourManager.styleLabelIcon(lblDeleteTestButton, new ImageIcon(TestExplorer.class.getResource("/icons/bin.png")), 20); 
		contentPane.add(lblDeleteTestButton);
		
		
		pnlChart = new JPanel();
		pnlChart.setBounds(364, 52, 272, 250);
		contentPane.add(pnlChart);
		
		JLabel lblDashboardButton = new JLabel("");
		lblDashboardButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { 
				Dashboard dashboard = new Dashboard();
				dashboard.setVisible(true);
				setVisible(false);  
			}
		});
		lblDashboardButton.setBounds(547, 10, 35, 35);
		ColourManager.styleLabelIcon(lblDashboardButton, new ImageIcon(TestExplorer.class.getResource("/icons/analytics.png")), 25); 
		contentPane.add(lblDashboardButton);
		
	
		
		JLabel lblShowLog = new JLabel("");
		lblShowLog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { 
				openChangeLog();
			}
		});
		lblShowLog.setBounds(505, 10, 35, 35);
		ColourManager.styleLabelIcon(lblShowLog, new ImageIcon(TestExplorer.class.getResource("/icons/log.png")), 30); 
		contentPane.add(lblShowLog);
		
		
		
		
		JLabel lblSettingsButton = new JLabel("");
		lblSettingsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { openSettingsWindow(); }
		});
		lblSettingsButton.setBounds(590, 10, 35, 35);
		ColourManager.styleLabelIcon(lblSettingsButton, new ImageIcon(TestExplorer.class.getResource("/icons/settings.png")), 20); 
		contentPane.add(lblSettingsButton);
		
		lblWarning = new JLabel("");
		lblWarning.setHorizontalAlignment(SwingConstants.CENTER);
		lblWarning.setForeground(new Color(255, 52, 48));
		lblWarning.setBounds(383, 328, 235, 16);
		contentPane.add(lblWarning);
		
		warningManager = new WarningManager(lblWarning); 
		
		cmbSortType = new JComboBox();
		cmbSortType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buildTestList(); 
				drawChart(); 
			}
		});
		cmbSortType.setModel(new DefaultComboBoxModel(new String[] {"None", "Score ↑", "Score ↓", "A to Z", "Z to A"}));
		cmbSortType.setBounds(262, 14, 88, 27);
		contentPane.add(cmbSortType);
		
		ColourManager.globalStyling(this); 
		
		DataManager.loadData(); 
		
		resetSubjectFilter();
		buildTestList(); 
		drawChart(); 
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
		
//		filteredTests.get(0).getTestName().charAt(0); 
		
		int sortType = cmbSortType.getSelectedIndex(); 
		
		if (sortType != 0)
		{
			boolean searching = true;
			
			while (searching)
			{
				searching = false;
				
				for (int i = 0; i < filteredTests.size() - 1; i++)
				{
					if ((filteredTests.get(i).testPercentage(true) < filteredTests.get(i + 1).testPercentage(true) && sortType == 1) || 
							(filteredTests.get(i).testPercentage(true) > filteredTests.get(i + 1).testPercentage(true) && sortType == 2) || 
							(filteredTests.get(i).getTestName().charAt(0) > filteredTests.get(i + 1).getTestName().charAt(0) && sortType == 3) || 
							(filteredTests.get(i).getTestName().charAt(0) < filteredTests.get(i + 1).getTestName().charAt(0) && sortType == 4)) 
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
			testData.add(test.testPercentage() + "%");
			testData.add(test.getLevel() + ""); 
			testData.add(test instanceof MockExam ? "Y" : "N"); 
			
			testTable.addRow(testData);
		}
		
		lblMock.setVisible(tblTestList.getSelectedRow() != -1);
		chkMock.setVisible(tblTestList.getSelectedRow() != -1);
		
	}
	
	private void newTest () // create new test entry
	{
		TestEditor testEditor = new TestEditor (this);
		
		testEditor.setVisible(true);
		this.setVisible(false);
		
		warningManager.hideWarning();
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
//		else 
//			ColourManager.clearIconLabels(); 
		
		super.setVisible(visible);
		
//		if (visible)
//			ColourManager.globalStyling(this);
	}
	
	public void deleteTest()
	{
		int selectedIndex = tblTestList.getSelectedRow(); 
		
		if (selectedIndex == -1)
		{
			warningManager.showWarning ("Please select a test");
			return; 
		}
		
		Test targetTest = filteredTests.get(selectedIndex); 
		tests.remove(targetTest); 
		
		Test.removeInvalidSubjects();
		
		DataManager.saveData();
		buildTestList(); 
		
		warningManager.hideWarning();
	}
	
	private void editTest()
	{
		if (tblTestList.getSelectedRow() == -1)
		{
			warningManager.showWarning ("Please select a test");
			return; 
		}
		
		Test targetTest = filteredTests.get(tblTestList.getSelectedRow()); 
		TestEditor editor = new TestEditor(this, targetTest); 
		
		editor.setVisible(true);
		this.setVisible(false);
		
		warningManager.hideWarning();
	}
	
	private void toggleMock() // toggles status of test between mock and normal test
	{
		int selectedIndex = tblTestList.getSelectedRow(); 
		
		if (selectedIndex == -1)
		{
			warningManager.showWarning ("Please select a test"); 
			return; 
		}

		Test oldTest = (Test) filteredTests.get(selectedIndex);
		mockToggle (oldTest); 
		
		warningManager.hideWarning();
	}
	
	public void mockToggle(Test oldTest)
	{
		Test newTest; 
		
		if (oldTest instanceof MockExam)
			newTest = new Test(oldTest.getTestName(), oldTest.getScore(), oldTest.getReflection(), oldTest.getDescription(), oldTest.getTotal(), oldTest.getSubject());
		else
			newTest = new MockExam(oldTest.getTestName(), oldTest.getScore(), oldTest.getReflection(), oldTest.getDescription(), oldTest.getTotal(), oldTest.getSubject());
		
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

	    theme.setTitlePaint(Color.decode(ColourManager.colourText));
	    theme.setExtraLargeFont( new Font(fontName,Font.PLAIN, 16) ); //title
	    theme.setLargeFont( new Font(fontName,Font.BOLD, 15)); //axis-title
	    theme.setRegularFont( new Font(fontName,Font.PLAIN, 11));
	    theme.setRangeGridlinePaint(Color.decode(ColourManager.colourLines));
	    theme.setPlotBackgroundPaint(Color.decode(ColourManager.colourBG));
	    theme.setChartBackgroundPaint(Color.decode(ColourManager.colourBG));
	    theme.setAxisOffset( new RectangleInsets(0,0,0,0) );
	    theme.setBarPainter(new StandardBarPainter());
	    theme.setAxisLabelPaint(Color.decode(ColourManager.colourText));
	    theme.apply( chart );
	    chart.getCategoryPlot().setOutlineVisible( false );
	    chart.getCategoryPlot().getRangeAxis().setAxisLineVisible( false );
	    chart.getCategoryPlot().getRangeAxis().setTickMarksVisible( false );
	    chart.getCategoryPlot().setRangeGridlineStroke( new BasicStroke() );
	    chart.getCategoryPlot().getRangeAxis().setTickLabelPaint(Color.decode(ColourManager.colourText));
	    chart.getCategoryPlot().getDomainAxis().setTickLabelPaint(Color.decode(ColourManager.colourText));
	    chart.setTextAntiAlias( true );
	    chart.setAntiAlias( true );
	    chart.getCategoryPlot().getRenderer().setSeriesPaint( 0, Color.decode(ColourManager.colourLines));
	    BarRenderer rend = (BarRenderer) chart.getCategoryPlot().getRenderer();
	    rend.setShadowVisible( true );
	    rend.setShadowXOffset( 2 );
	    rend.setShadowYOffset( 0 );
	    rend.setShadowVisible(false);
	    rend.setMaximumBarWidth( 0.1);
	    chart.setPadding(new RectangleInsets(15,5,5,10)); // top left bottom right
	    
	    NumberAxis rangeAxis = (NumberAxis) ((CategoryPlot) chart.getPlot()).getRangeAxis();
	    rangeAxis.setRange(0, 100);
	    
	    if (!cmbSubjectFilter.getEditor().getItem().toString().equals("All"))
	    {
	    	chart.getLegend().setBackgroundPaint(Color.decode(ColourManager.colourBG));
	    	chart.getLegend().setItemPaint(Color.decode(ColourManager.colourText));
	    }
	    
	    rend.setSeriesPaint(0, Color.decode("#7dab8d"));
	    
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
	
	private void openChangeLog() 
	{
		ChangeLog changeLog = new ChangeLog(this); 
		changeLog.setVisible(true);
		setVisible(false);
	}
}
