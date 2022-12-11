package testTracker.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.general.DefaultPieDataset;

public class Dashboard extends JFrame {
	private class Category {
		String name;
		double score;

		public Category(String name, double score) {
			this.name = name;
			this.score = score;
		}
	}

	private ArrayList<Category> categories = new ArrayList<Category>();
	private int categoryIndex = 0; // index of current category

	private ArrayList<Test> tests = new ArrayList<Test>(); // all tests
	private ArrayList<Test> filteredTests = new ArrayList<Test>(); // filtered tests (for a single subject only)

	private JPanel contentPane;
	private JTable tblTestList;
	private JLabel lblTitle;
	private JLabel lblAvgScore;
	private JSpinner spnTargetScore;
	private JTable tblRanking;
	private JScrollPane rankingScrollPane;
	private JPanel pnlChart;
	private JLabel lblTargetScore;
	private JLabel lblStatus;
	private JButton btnSettings;
	private JButton btnDashboard;
	private JComboBox cmbSortType;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dashboard frame = new Dashboard();
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
	public Dashboard() {
		setResizable(false);
		setLocationRelativeTo(null);
		
		setTitle("Analytics"); 

		DataManager.loadData();
		tests = (ArrayList<Test>) TestExplorer.tests.clone();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (650 / 2)), 150, 650, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblTitle = new JLabel("Overview");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(225, 18, 200, 16);
		contentPane.add(lblTitle);

		JScrollPane testsScrollPane = new JScrollPane();
		testsScrollPane.setBounds(32, 52, 318, 250);
		contentPane.add(testsScrollPane);

		DefaultTableModel tblModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			} // make uneditable
		};

		Vector<String> columnTitles = new Vector<String>();
		columnTitles.add("Subject");
		columnTitles.add("Test");
		columnTitles.add("Score");
		columnTitles.add("Level");
		columnTitles.add("Mock");
		tblModel.setColumnIdentifiers(columnTitles);

		tblTestList = new JTable(tblModel);
		tblTestList.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
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
		        		openTestInfo();  
		        }
		    }
		});
		tblTestList.setSelectionMode(0);
		testsScrollPane.setViewportView(tblTestList);
		
		TableColumnModel columnModel = tblTestList.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(30);
		columnModel.getColumn(1).setPreferredWidth(60);
		columnModel.getColumn(2).setPreferredWidth(5);
		columnModel.getColumn(3).setPreferredWidth(5);
		columnModel.getColumn(4).setPreferredWidth(5);
		tblTestList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		((DefaultTableCellRenderer)tblTestList.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		

		lblAvgScore = new JLabel("Average Score: 90%");
		lblAvgScore.setBounds(33, 324, 161, 16);
		contentPane.add(lblAvgScore);

		lblTargetScore = new JLabel("Target Score: ");
		lblTargetScore.setBounds(205, 324, 91, 16);
		contentPane.add(lblTargetScore);

		JButton btnLeft = new JButton("<");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rotateCategory(-1);
			}
		});
		btnLeft.setBounds(178, 13, 41, 29);
		contentPane.add(btnLeft);

		JButton btnRight = new JButton(">");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rotateCategory(+1);
			}
		});
		btnRight.setBounds(437, 13, 41, 29);
		contentPane.add(btnRight);

		spnTargetScore = new JSpinner();
		spnTargetScore.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Test.globalTargets.set(categoryIndex - 1, (Integer) spnTargetScore.getValue());
				DataManager.saveData();
				
				if ((Integer) spnTargetScore.getValue() > 100)
					spnTargetScore.setValue(100);
				else if ((Integer) spnTargetScore.getValue() < 0)
					spnTargetScore.setValue(0); 
				
				updateStatusText();
			}
		});
		spnTargetScore.setBounds(299, 319, 52, 26);
		contentPane.add(spnTargetScore);

		pnlChart = new JPanel();
		pnlChart.setBounds(364, 52, 272, 250);
		contentPane.add(pnlChart);
		pnlChart.setLayout(new BorderLayout(0, 0));

		categories.add(0, new Category("Overview", getAvgScore("Overview")));
		for (int i = 0; i < Test.globalSubjects.size(); i++) {
			String name = Test.globalSubjects.get(i);
			categories.add(i + 1, new Category(name, getAvgScore(name)));
		}

		rankingScrollPane = new JScrollPane();
		rankingScrollPane.setBounds(364, 52, 261, 250);
		contentPane.add(rankingScrollPane);

		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			} // make uneditable
		};

		Vector<String> rankingColumnTitles = new Vector<String>();
		rankingColumnTitles.add("Rank");
		rankingColumnTitles.add("Subject");
		rankingColumnTitles.add("Score");
		model.setColumnIdentifiers(rankingColumnTitles);

		Category overviewCategory = categories.get(0);
		categories.sort((a, b) -> Double.compare(b.score, a.score));

		categories.remove(categories.indexOf(overviewCategory)); // MOVE OVERVIEW BACK TO FIRST POSITION
		categories.add(0, overviewCategory);

		for (int i = 1; i < categories.size(); i++) // start at 1 to skip "Overview"
		{
			Vector<String> testData = new Vector<String>();

			testData.add(Integer.toString(i));
			testData.add(categories.get(i).name);
			testData.add(categories.get(i).score + "%");

			model.addRow(testData);
		}
		tblRanking = new JTable(model);
		tblRanking.setRowSelectionAllowed(false);
		tblRanking.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				
				if (Settings.getLevel(categories.get(row + 1).score) == 7)
					c.setBackground(Color.decode(ColourManager.colourGreen)); // green
				else if(Settings.getLevel(categories.get(row + 1).score) <= 5) 
					c.setBackground(Color.decode(ColourManager.colourRed)); // red
				else
					c.setBackground(Color.decode(ColourManager.colourYellow));
				
				setHorizontalAlignment(JLabel.CENTER);
				
				return c;
			}
		});
		rankingScrollPane.setViewportView(tblRanking);
		
		TableColumnModel rankColumnModel = tblRanking.getColumnModel();
		rankColumnModel.getColumn(0).setPreferredWidth(5);
		rankColumnModel.getColumn(1).setPreferredWidth(100);
		rankColumnModel.getColumn(2).setPreferredWidth(5);
		tblTestList.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		((DefaultTableCellRenderer)tblRanking.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		

		lblStatus = new JLabel("Well done!");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(368, 318, 261, 29);
		contentPane.add(lblStatus);
		
		JLabel lblSettingsButton = new JLabel("");
		lblSettingsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { openSettingsWindow(); }
		});
		lblSettingsButton.setBounds(590, 10, 35, 35);
		ColourManager.styleLabelIcon(lblSettingsButton, new ImageIcon(TestExplorer.class.getResource("/icons/settings.png")), 20); 
		contentPane.add(lblSettingsButton);
		
		JLabel lblDashboardButton = new JLabel("");
		lblDashboardButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { 
				TestExplorer testExplorer = new TestExplorer();
				testExplorer.setVisible(true);
				setVisible(false);
			}
		});
		lblDashboardButton.setBounds(547, 10, 35, 35);
		ColourManager.styleLabelIcon(lblDashboardButton, new ImageIcon(TestExplorer.class.getResource("/icons/analytics.png")), 25); 
		contentPane.add(lblDashboardButton);
		
		cmbSortType = new JComboBox();
		cmbSortType.setModel(new DefaultComboBoxModel(new String[] {"None", "Score ↑", "Score ↓", "A to Z", "Z to A"}));
		cmbSortType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buildTestList(); 
				drawChart(); 
			}
		});
		cmbSortType.setBounds(32, 14, 88, 27);
		contentPane.add(cmbSortType);
		
		ColourManager.globalStyling(this); 

		updateUI();
	}

	private void updateUI() // build test list, recalculate avg score, etc
	{
		buildTestList();

		double avgScore = categories.get(categoryIndex).score;
		int level = Settings.getLevel(avgScore);
		lblAvgScore.setText("Average Score: " + avgScore + "% (" + level + ")");

		Boolean isOverview = categoryIndex == 0;

		rankingScrollPane.setVisible(isOverview);
		pnlChart.setVisible(!isOverview);
		lblStatus.setVisible(!isOverview);

		drawChart();
		updateTargetScore();
	}

	private void rotateCategory(int change) {
		categoryIndex += change;

		if (categoryIndex < 0)
			categoryIndex = categories.size() - 1;
		else if (categoryIndex > categories.size() - 1)
			categoryIndex = 0;

		lblTitle.setText(categories.get(categoryIndex).name);

		updateUI();
	}

	@SuppressWarnings("unchecked")
	private void buildTestList() {
		if (categoryIndex == 0) // overview
			filteredTests = (ArrayList<Test>) tests.clone();
		else {
			filteredTests = new ArrayList<Test>();

			for (Test test : tests) {
				if (test.getSubject().equals(categories.get(categoryIndex).name))
					filteredTests.add(test);
			}
		}

		int sortType = cmbSortType.getSelectedIndex();
		if (sortType != 0) {
			boolean searching = true;

			while (searching) {
				searching = false;

				for (int i = 0; i < filteredTests.size() - 1; i++) {
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

		DefaultTableModel testTable = (DefaultTableModel) tblTestList.getModel();

		testTable.setRowCount(0); // remove old rows (building new list)

		for (Test test : filteredTests) {
			Vector<String> testData = new Vector<String>();

			testData.add(test.getSubject());
			testData.add(test.getTestName());
			testData.add(test.testPercentage() + "%");
			testData.add(test.getLevel() + "");
			testData.add(test instanceof MockExam ? "Y" : "N");

			testTable.addRow(testData);
		}
	}

	private double getAvgScore(String categoryName) {
		double sum = 0;
		int count = 0;

		for (Test test : tests) {
			if (test.getSubject().equals(categoryName) || categoryName.equals("Overview")) {
				sum += test.testPercentage(false);
				count++;
			}
		}

		double percentage = sum / count;
		percentage = (double) Math.round(percentage * 10) / 10;

		return percentage;
	}

	private void drawChart() {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();

		for (Test test : filteredTests)
			dataset.setValue(test.getTestName(), test.testPercentage(false));

		JFreeChart chart = ChartFactory.createPieChart(categories.get(categoryIndex).name, dataset, false, false,
				false);

		String fontName = "Lucida Sans";

		StandardChartTheme theme = (StandardChartTheme) org.jfree.chart.StandardChartTheme.createJFreeTheme();

		theme.setTitlePaint(Color.decode(ColourManager.colourText));
		theme.setExtraLargeFont(new Font(fontName, Font.PLAIN, 16)); // title
		theme.setLargeFont(new Font(fontName, Font.BOLD, 15)); // axis-title
		theme.setRegularFont(new Font(fontName, Font.PLAIN, 11));
		theme.setPlotBackgroundPaint(Color.decode(ColourManager.colourBG));
		theme.setChartBackgroundPaint(Color.decode(ColourManager.colourBG));
		theme.setGridBandPaint(Color.red);
		theme.setBarPainter(new StandardBarPainter());
		theme.apply(chart);
		chart.getPlot().setOutlineVisible(false);
		chart.setTextAntiAlias(true);
		chart.setAntiAlias(true);
		chart.setPadding(new RectangleInsets(20, 0, 20, 0)); // top left bottom right

		pnlChart.removeAll();
		pnlChart.setLayout(new BorderLayout(0, 0));
		ChartPanel chartPanel = new ChartPanel(chart);
		pnlChart.add(chartPanel, BorderLayout.CENTER);
		this.repaint();

		super.setVisible(true); // solves bug where graph disappears on rerender
	}

	private void updateTargetScore() {
		lblTargetScore.setVisible(categoryIndex != 0);
		spnTargetScore.setVisible(categoryIndex != 0);

		if (categoryIndex == 0)
			return;

		int score = Test.globalTargets.get(categoryIndex - 1);
		spnTargetScore.setValue(Integer.valueOf(score));

		updateStatusText();
	}

	private void updateStatusText() {
		if (categoryIndex == 0)
			return;

		double goalDiff = categories.get(categoryIndex).score - (Integer) spnTargetScore.getValue();
		goalDiff = (double) Math.round(goalDiff * 1000) / 1000;

		if (goalDiff >= 0)
			lblStatus.setText("Well done, you have reached your goal!");
		else
			lblStatus.setText(Math.abs(goalDiff) + "% to go, keep it up!");
	}

	private void openSettingsWindow() {
		Settings settings = new Settings(this);
		settings.setVisible(true);
		setVisible(false);
	}

	public void updateLevels() // update levels (7, 6, 5 etc) when settings window closed
	{
		for (Test test : tests)
			test.calculateLevel();

		updateUI();
	}
	
	private void openTestInfo()
	{
		Test test = filteredTests.get(tblTestList.getSelectedRow()); 
		TestInfo infoWindow = new TestInfo(test);
		
		infoWindow.setVisible(true);
	}
}
