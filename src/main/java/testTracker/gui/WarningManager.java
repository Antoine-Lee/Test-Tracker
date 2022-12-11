package testTracker.gui;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class WarningManager 
{
	private JLabel lblWarning = null; 
	private ArrayList<JComponent> highlightedFields = new ArrayList<JComponent>();
	private ArrayList<JComboBox> highlightedComboBoxes = new ArrayList<JComboBox>(); 
	
	public WarningManager (JLabel lblWarning)
	{
		this.lblWarning = lblWarning;
		hideWarning(); 
	}
	
	public void showWarning(String warning)
	{
		lblWarning.setForeground(Color.decode(ColourManager.colourRed));
		lblWarning.setText(warning);
//		lblWarning.setForeground(Color.decode(ColourManager.colourWarning));
	}
	
	public void hideWarning()
	{
		lblWarning.setText("");
		
		for (JComponent field : highlightedFields)
			field.setBackground(Color.decode(ColourManager.colourButton1)); 
		
		for (JComboBox comboBox : highlightedComboBoxes)
			comboBox.getEditor().getEditorComponent().setBackground(Color.decode(ColourManager.colourButton1));
	}
	
	public void highlightField (JComponent field)
	{
		field.setBackground(Color.decode(ColourManager.colourRed));
		highlightedFields.add(field); 
	}
	
	public void highlightField (JComboBox comboBox)
	{
		comboBox.getEditor().getEditorComponent().setBackground(Color.decode(ColourManager.colourRed));
		highlightedComboBoxes.add(comboBox); 
	}
}
