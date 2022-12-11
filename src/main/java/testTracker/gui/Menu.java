package testTracker.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;

public class Menu extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
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
	public Menu() {
		setResizable(false);
		setLocationRelativeTo(null);
		
		DataManager.loadData(); 
		
		setTitle("Test Tracker"); 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setBounds((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (650 / 2)), 150, 650, 400);

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Test Tracker");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblTitle.setBounds(40, 111, 142, 44);
		contentPane.add(lblTitle);
		
		JButton btnStart = new JButton("Launch");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				TestExplorer testExplorer = new TestExplorer ();
				
				testExplorer.setVisible(true);
				setVisible(false); 
			}
		});
		btnStart.setBounds(68, 193, 90, 25);
		contentPane.add(btnStart);
		
		JButton btnSettings = new JButton("Settings");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				openSettingsWindow(); 
			}
		});
		btnSettings.setBounds(68, 223, 90, 25);
		contentPane.add(btnSettings);
		
		
		JLabel lblImage = new JLabel("");
		lblImage.setBounds(214, 49, 400, 267);
		ImageIcon icon = new ImageIcon(TestExplorer.class.getResource("/icons/menuImg.png")); 
		Image image = icon.getImage(); 
    	Image newImg = image.getScaledInstance(400, 267, Image.SCALE_SMOOTH); 
    	icon = new ImageIcon(newImg); 
    	lblImage.setHorizontalAlignment(SwingConstants.CENTER);
    	lblImage.setIcon(icon); 
		contentPane.add(lblImage);
		
		ColourManager.globalStyling(this); 
	}
	
	private void openSettingsWindow()
	{
		Settings settings = new Settings(this); 
		settings.setVisible(true);
		setVisible(false); 
	}
}
