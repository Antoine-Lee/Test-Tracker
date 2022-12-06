package testTracker.gui;

import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		
//		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
//		
//		for (String subject : Test.globalSubjects)  
//		    model.addElement(subject);
//		
//		cmbSubject.setModel(model);
	}

	public Settings() 
	{
		setResizable(false);
		setLocationRelativeTo(null);
		
		DataManager.loadData(); 
		
//		boundaries = new int[] { 85, 75, 65 };
//		DataManager.saveData();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 137, 252);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Settings");
		lblTitle.setBounds(31, 23, 61, 16);
		contentPane.add(lblTitle);
		
		JLabel lblLevel7 = new JLabel("7:");
		lblLevel7.setBounds(20, 51, 16, 16);
		contentPane.add(lblLevel7);
		
		spnLevel7 = new JSpinner();
		spnLevel7.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateBoundary(0, spnLevel7, boundaries[1], 100);
			}
		});
		spnLevel7.setBounds(48, 46, 55, 26);
		contentPane.add(spnLevel7);
		spnLevel7.setValue(boundaries[0]);
		
		JLabel lblLevel6 = new JLabel("6:");
		lblLevel6.setBounds(20, 82, 16, 16);
		contentPane.add(lblLevel6);
		
		spnLevel6 = new JSpinner();
		spnLevel6.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateBoundary(1, spnLevel6, boundaries[2], boundaries[0]); 
			}
		});
		spnLevel6.setBounds(48, 77, 55, 26);
		contentPane.add(spnLevel6);
		spnLevel6.setValue(boundaries[1]);
		
		JLabel lblLevel5 = new JLabel("5:");
		lblLevel5.setBounds(20, 116, 16, 16);
		contentPane.add(lblLevel5);
		
		spnLevel5 = new JSpinner();
		spnLevel5.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateBoundary(2, spnLevel5, 0, boundaries[1]); 
			}
		});
		spnLevel5.setBounds(48, 111, 55, 26);
		contentPane.add(spnLevel5);
		spnLevel5.setValue(boundaries[2]);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { close(); }
		});
		btnClose.setBounds(31, 157, 67, 29);
		contentPane.add(btnClose);
	}
	
	private void updateBoundary (int boundaryIndex, JSpinner spinner, int lowerLim, int upperLim)
	{
		boundaries[boundaryIndex] = (Integer)spinner.getValue();
		
//		System.out.println("Restricting boundary " + boundaryIndex + " between " + lowerLim + " and " + upperLim);
		
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
//			(Dashboard) parent.updateLevels(); 
		}
		
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
}
