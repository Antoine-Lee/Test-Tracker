package testTracker.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;
import java.awt.event.ActionEvent;

public class TestInfo extends JFrame {

	private JPanel contentPane;
	private JLabel lblTitle;
	private JLabel lblMarksValue;
	private JLabel lblScoreValue;
	private JTextArea jtaDescription;
	private JTextArea jtaReflection;
//	
//	private Dashboard parent = null; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestInfo frame = new TestInfo();
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
	public TestInfo() {
		setResizable(false);
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 200, 300, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblTitle = new JLabel("Test Name - Subject (Mock)");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(6, 30, 288, 16);
		contentPane.add(lblTitle);
		
		JLabel lblMarksTitle = new JLabel("Marks: ");
		lblMarksTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblMarksTitle.setBounds(44, 72, 62, 16);
		contentPane.add(lblMarksTitle);
		
		lblMarksValue = new JLabel("85/100");
		lblMarksValue.setHorizontalAlignment(SwingConstants.LEFT);
		lblMarksValue.setBounds(189, 72, 62, 16);
		contentPane.add(lblMarksValue);
		
		lblScoreValue = new JLabel("85% (7)");
		lblScoreValue.setHorizontalAlignment(SwingConstants.LEFT);
		lblScoreValue.setBounds(189, 100, 62, 16);
		contentPane.add(lblScoreValue);
		
		JLabel lblScoreTitle = new JLabel("Score: ");
		lblScoreTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblScoreTitle.setBounds(44, 100, 62, 16);
		contentPane.add(lblScoreTitle);
		
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setHorizontalAlignment(SwingConstants.LEFT);
		lblDescription.setBounds(44, 128, 91, 16);
		contentPane.add(lblDescription);
		
		JScrollPane descriptionScrollPane = new JScrollPane();
		descriptionScrollPane.setBounds(44, 156, 232, 52);
		contentPane.add(descriptionScrollPane);
		
		jtaDescription = new JTextArea();
		descriptionScrollPane.setViewportView(jtaDescription);
		jtaDescription.setEditable(false);
		jtaDescription.setLineWrap(true);
		
		JLabel lblReflection = new JLabel("Reflection");
		lblReflection.setHorizontalAlignment(SwingConstants.LEFT);
		lblReflection.setBounds(44, 220, 91, 16);
		contentPane.add(lblReflection);
		
		JScrollPane reflectionScrollPane = new JScrollPane();
		reflectionScrollPane.setBounds(44, 248, 232, 52);
		contentPane.add(reflectionScrollPane);
		
		jtaReflection = new JTextArea();
		reflectionScrollPane.setViewportView(jtaReflection);
		jtaReflection.setLineWrap(true);
		jtaReflection.setEditable(false);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close(); 
			}
		});
		btnClose.setBounds(96, 321, 106, 23);
		contentPane.add(btnClose);
		
		ColourManager.globalStyling(this); 
	}
	
	public TestInfo (Test test)
	{	
		this(); // call other constructor
		setTitle(test.getTestName() + " - Info"); 
		
		lblTitle.setText(test.getSubject() + " - " + test.getTestName() + (test instanceof MockExam ? " (Mock)" : ""));
		
		lblMarksValue.setText(test.getScore() + "/" + test.getTotal());
		lblScoreValue.setText(test.testPercentage() + "% (" + test.getLevel() + ")"); 
		
		jtaDescription.setText(test.getDescription()); 
		jtaReflection.setText(test.getReflection()); 
	}
	
	private void close()
	{
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

//		this.setVisible(false);		
//		parent.setVisible(true);
	}
}
