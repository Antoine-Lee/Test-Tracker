package testTracker.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ChangeLog extends JFrame {

	private JPanel contentPane;
	
	private TestExplorer parent = null; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeLog frame = new ChangeLog();
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
	public ChangeLog() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (650 / 2)), 150, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 57, 383, 194);
		contentPane.add(scrollPane);
		
		JTextArea jtaChangeLog = new JTextArea();
		jtaChangeLog.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		jtaChangeLog.setEditable(false);
		scrollPane.setViewportView(jtaChangeLog);
		
		JLabel lblTitle = new JLabel("Changelog");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(172, 22, 104, 16);
		contentPane.add(lblTitle);
		
		for (int i = 0; i < Test.getChangeLog().size(); i++)
		{
			jtaChangeLog.setText(jtaChangeLog.getText() + Test.getChangeLog().get(i) + "\n");
		}
		
		JLabel lblCloseButton = new JLabel("");
		lblCloseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { 
				closeWindow(); 
			}
		});
		lblCloseButton.setBounds(35, 20, 25, 25);
		ColourManager.styleLabelIcon(lblCloseButton, new ImageIcon(TestExplorer.class.getResource("/icons/close.png")), 16); 
		contentPane.add(lblCloseButton);
		
		ColourManager.globalStyling(this);
	}
	
	public ChangeLog(TestExplorer parent)
	{
		this(); 
		this.parent = parent; 
	}
	
	private void closeWindow()
	{
		parent.setVisible(true);
		setVisible(false);
	}
}
