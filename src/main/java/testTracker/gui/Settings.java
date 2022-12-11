package testTracker.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Settings extends JFrame {

	private JPanel contentPane;
	JFrame parent; 
	
	public static int[] boundaries = { 85, 75, 65 }; 
	private JSpinner spnLevel7;
	private JSpinner spnLevel6;
	private JSpinner spnLevel5;

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Settings frame = new Settings();
					frame.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public Settings (JFrame parent)
	{
		this(); // call main constructor
		
		this.parent = parent; 
	}

	public Settings() 
	{
		setResizable(false);
		setLocationRelativeTo(null);
		
		setTitle("Settings");
		
		DataManager.loadData(); 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (650 / 2)), 150, 420, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Grade Boundaries");
		lblTitle.setBounds(223, 50, 109, 16);
		contentPane.add(lblTitle);
		
		JLabel lblLevel7 = new JLabel("7:");
		lblLevel7.setBounds(235, 83, 16, 16);
		contentPane.add(lblLevel7);
		
		spnLevel7 = new JSpinner();
		spnLevel7.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateBoundary(0, spnLevel7, boundaries[1], 100);
			}
		});
		spnLevel7.setBounds(263, 78, 55, 26);
		contentPane.add(spnLevel7);
		spnLevel7.setValue(boundaries[0]);
		
		JLabel lblLevel6 = new JLabel("6:");
		lblLevel6.setBounds(235, 114, 16, 16);
		contentPane.add(lblLevel6);
		
		spnLevel6 = new JSpinner();
		spnLevel6.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateBoundary(1, spnLevel6, boundaries[2], boundaries[0]); 
			}
		});
		spnLevel6.setBounds(263, 109, 55, 26);
		contentPane.add(spnLevel6);
		spnLevel6.setValue(boundaries[1]);
		
		JLabel lblLevel5 = new JLabel("5:");
		lblLevel5.setBounds(235, 148, 16, 16);
		contentPane.add(lblLevel5);
		
		spnLevel5 = new JSpinner();
		spnLevel5.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateBoundary(2, spnLevel5, 0, boundaries[1]); 
			}
		});
		spnLevel5.setBounds(263, 143, 55, 26);
		contentPane.add(spnLevel5);
		spnLevel5.setValue(boundaries[2]);
		
		JButton btnClose = new JButton("Done");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { close(); }
		});
		btnClose.setBounds(263, 207, 55, 24);
		contentPane.add(btnClose);
		
		JLabel lblColours = new JLabel("Colours");
		lblColours.setBounds(105, 50, 109, 16);
		contentPane.add(lblColours);
		
		
				
		JLabel lblColourBG = new JLabel("Background");
		lblColourBG.setHorizontalAlignment(SwingConstants.RIGHT);
		lblColourBG.setBounds(30, 83, 93, 16);
		contentPane.add(lblColourBG);
		
		JLabel lblEditColourBG = new JLabel("");
		lblEditColourBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColourManager.colourBG = pickColour();
				updateColours();
			}
		});
		lblEditColourBG.setBounds(145, 80, 24, 24);
		ColourManager.styleLabelIcon(lblEditColourBG, new ImageIcon(TestExplorer.class.getResource("/icons/pencil.png")), 16); 
		contentPane.add(lblEditColourBG);
		
		JLabel lblResetColourBG = new JLabel("");
		lblResetColourBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColourManager.colourBG = ColourManager.defaultColourBG;
				updateColours();
			}
		});
		lblResetColourBG.setBounds(178, 80, 24, 24);
		ColourManager.styleLabelIcon(lblResetColourBG, new ImageIcon(TestExplorer.class.getResource("/icons/reset.png")), 16); 
		contentPane.add(lblResetColourBG);
		
		
		
		
		JLabel lblColourContainer = new JLabel("Containers");
		lblColourContainer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblColourContainer.setBounds(30, 114, 93, 16);
		contentPane.add(lblColourContainer);
		
		JLabel lblEditColourContainer = new JLabel("");
		lblEditColourContainer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColourManager.colourContainer = pickColour();
				updateColours();
			}
		});
		lblEditColourContainer.setBounds(145, 111, 24, 24);
		ColourManager.styleLabelIcon(lblEditColourContainer, new ImageIcon(TestExplorer.class.getResource("/icons/pencil.png")), 16); 
		contentPane.add(lblEditColourContainer);
		
		JLabel lblResetColourContainer = new JLabel("");
		lblResetColourContainer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColourManager.colourContainer = ColourManager.defaultColourContainer;
				updateColours();
			}
		});
		lblResetColourContainer.setBounds(178, 111, 24, 24);
		ColourManager.styleLabelIcon(lblResetColourContainer, new ImageIcon(TestExplorer.class.getResource("/icons/reset.png")), 16); 
		contentPane.add(lblResetColourContainer);
		
		
		
		
		
		JLabel lblColourText = new JLabel("Text");
		lblColourText.setHorizontalAlignment(SwingConstants.RIGHT);
		lblColourText.setBounds(30, 148, 93, 16);
		contentPane.add(lblColourText);
		
		JLabel lblEditColourText = new JLabel("");
		lblEditColourText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColourManager.colourText = pickColour();
				updateColours();
			}
		});
		lblEditColourText.setBounds(145, 145, 24, 24);
		ColourManager.styleLabelIcon(lblEditColourText, new ImageIcon(TestExplorer.class.getResource("/icons/pencil.png")), 16); 
		contentPane.add(lblEditColourText);
		
		JLabel lblResetColourText = new JLabel("");
		lblResetColourText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColourManager.colourText = ColourManager.defaultColourText;
				updateColours();
			}
		});
		lblResetColourText.setBounds(178, 145, 24, 24);
		ColourManager.styleLabelIcon(lblResetColourText, new ImageIcon(TestExplorer.class.getResource("/icons/reset.png")), 16); 
		contentPane.add(lblResetColourText);
		
		
		
		
		
		JLabel lblColourButton1 = new JLabel("Button 1");
		lblColourButton1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblColourButton1.setBounds(30, 179, 93, 16);
		contentPane.add(lblColourButton1);
		
		JLabel lblEditColourButton1 = new JLabel("");
		lblEditColourButton1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColourManager.colourButton1 = pickColour();
				updateColours();
			}
		});
		lblEditColourButton1.setBounds(145, 176, 24, 24);
		ColourManager.styleLabelIcon(lblEditColourButton1, new ImageIcon(TestExplorer.class.getResource("/icons/pencil.png")), 16); 
		contentPane.add(lblEditColourButton1);
		
		JLabel lblResetColourButton1= new JLabel("");
		lblResetColourButton1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColourManager.colourButton1 = ColourManager.defaultColourButton1;
				updateColours();
			}
		});
		lblResetColourButton1.setBounds(178, 176, 24, 24);
		ColourManager.styleLabelIcon(lblResetColourButton1, new ImageIcon(TestExplorer.class.getResource("/icons/reset.png")), 16); 
		contentPane.add(lblResetColourButton1);
		
		
		
		
		JLabel lblColourButton2 = new JLabel("Button 2");
		lblColourButton2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblColourButton2.setBounds(30, 210, 93, 16);
		contentPane.add(lblColourButton2);
		
		JLabel lblEditColourButton2 = new JLabel("");
		lblEditColourButton2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColourManager.colourButton2 = pickColour();
				updateColours();
			}
		});
		lblEditColourButton2.setBounds(145, 207, 24, 24);
		ColourManager.styleLabelIcon(lblEditColourButton2, new ImageIcon(TestExplorer.class.getResource("/icons/pencil.png")), 16); 
		contentPane.add(lblEditColourButton2);
		
		JLabel lblResetColourButton2= new JLabel("");
		lblResetColourButton2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColourManager.colourButton2 = ColourManager.defaultColourButton2;
				updateColours();
			}
		});
		lblResetColourButton2.setBounds(178, 207, 24, 24);
		ColourManager.styleLabelIcon(lblResetColourButton2, new ImageIcon(TestExplorer.class.getResource("/icons/reset.png")), 16); 
		contentPane.add(lblResetColourButton2);
		
		JLabel lblColours_1 = new JLabel("SETTINGS");
		lblColours_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblColours_1.setBounds(147, 21, 109, 16);
		contentPane.add(lblColours_1);
		
		
		updateColours(); 
	}
	
	private void updateBoundary (int boundaryIndex, JSpinner spinner, int lowerLim, int upperLim)
	{
		boundaries[boundaryIndex] = (Integer)spinner.getValue();

		if (boundaries[boundaryIndex] > upperLim)
		{
			boundaries[boundaryIndex] = upperLim; 
			spinner.setValue(upperLim);
		}
		else if (boundaries[boundaryIndex] < lowerLim)
		{
			boundaries[boundaryIndex] = lowerLim; 
			spinner.setValue(lowerLim);
		}
		
		for (Test test : TestExplorer.tests)
			test.calculateLevel(); 
		
		DataManager.saveData(); 
	}
	
	private void close()
	{
		if (parent instanceof Dashboard)
		{
			Dashboard dashboard = (Dashboard) parent; 
			dashboard.updateLevels();
		}
		
		ColourManager.globalStyling(parent);
		
		this.setVisible(false);
		parent.setVisible(true); 
	}
	
	public static int getLevel (double score)
	{
		if (score >= Settings.boundaries[0])
			return 7; 
		else if (score >= Settings.boundaries[1])
			return 6; 
		else if (score >= Settings.boundaries[2]) 
			return 5; 
		else
			return 4; 	
	}
	
	private String pickColour ()
	{
		Color color = JColorChooser.showDialog(this, "Select a Colour", Color.WHITE, false);
		String hex = "#"+Integer.toHexString(color.getRGB()).substring(2);  
		return hex; 
	}
	
	private void updateColours()
	{
		DataManager.saveData();
		ColourManager.globalStyling(this);
	}
}
