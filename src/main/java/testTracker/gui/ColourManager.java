package testTracker.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.text.DefaultCaret;

import sun.swing.DefaultLookup;

public class ColourManager // manages design
{
    public static String colourBG = "#2d2f32"; 
    public static String colourContainer = "#3c4352";
    public static String colourText = "#ebeef5";
    
    public static String colourButton1 = "#555f73";
    public static String colourButton2 = "#66718a"; // alternate colour for 2 jtable items
    
    public static String colourLines = "#4a4e4d"; // graph lines
    
    public static String colourGreen = "#67856c"; // selection
    public static String colourYellow = "#b59235"; 
    public static String colourRed = "#b05a5a"; // warnings

    public static String defaultColourBG = "#2d2f32"; 
    public static String defaultColourContainer = "#3c4352";
    public static String defaultColourText = "#ebeef5";
    public static String defaultColourButton1 = "#555f73";
    public static String defaultColourButton2 = "#66718a"; // alternate colour for 2 jtable items
    public static String defaultColourLines = "#4a4e4d"; // graph lines
    public static String defaultColourGreen = "#67856c"; // selection
    public static String defaultColourYellow = "#b59235"; 
    public static String defaultColourRed = "#b05a5a"; // warnings
    
    public static int charLimit = 20; 
    
    private static ArrayList<JLabel> iconLabels = new ArrayList<JLabel>(); 

	public static void main(String[] args) 
	{
		colourBG = defaultColourBG; 
		colourContainer = defaultColourContainer; 
		colourText = defaultColourText; 
		colourButton1 = defaultColourButton1; 
		colourButton2 = defaultColourButton2; 
		colourLines = defaultColourLines; 
		colourGreen = defaultColourGreen; 
		colourYellow = defaultColourYellow; 
		colourRed = defaultColourRed; 
	}
    
    public static void globalStyling(JFrame frame)
    {
    	for (JLabel label : iconLabels) 
    		label.setBackground(Color.decode(colourButton1));
    	
    	frame.getContentPane().setBackground(Color.decode(colourBG));
    	List<Component> components = getAllComponents(frame);
		styleComponents(components);
    }
    
    private static List<Component> getAllComponents(final Container c) // recursively grab all components for styling
    {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
			{
				compList.addAll(getAllComponents((Container) comp));
			}
		}
		return compList;
	}
    
    private static void styleComponents(List<Component> components) // style all components provided
    {
    	for (Component component : components) 
    	{
			if (component instanceof JLabel) 
				styleLabel((JLabel)component);
			else if (component instanceof JButton)
				styleButton((JButton)component);
			else if (component instanceof JTable)
				styleTable((JTable)component);
			else if (component instanceof JCheckBox)
				styleCheckBox((JCheckBox)component);
			else if (component instanceof JScrollPane) 
				styleScrollPane((JScrollPane)component);
			else if (component instanceof JTextArea)
				styleTextArea((JTextArea)component);
			else if (component instanceof JComboBox)
				styleComboBox((JComboBox)component);
			else if (component instanceof JSpinner)
				styleSpinner((JSpinner)component);
			else if (component instanceof JTextField)
				styleTextField((JTextField)component); 
		}
    }
    
    private static void styleLabel(JLabel label)
    {
    	label.setForeground(Color.decode(colourText));
    }
    
    private static void styleButton(JButton button)
    {
    	button.setOpaque(true);
		button.setBorderPainted(false);
		button.setBackground(Color.decode(colourButton1));
		button.setForeground(Color.decode(colourText));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFocusPainted(false);
    }
    
    private static void styleTable(JTable table)
    {
    	table.setOpaque(true);
		table.setFillsViewportHeight(true);
		table.setBackground(Color.decode(colourContainer));
		table.getTableHeader().setBackground(Color.decode(colourContainer));
		table.getTableHeader().setForeground(Color.decode(colourText));
		table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
		table.setForeground(Color.decode(colourText));
		table.setShowGrid(false);
		table.setRowHeight(22);
    }
    
    private static void styleCheckBox(JCheckBox checkBox)
    {
    	
    	ImageIcon icon = new ImageIcon(ColourManager.class.getResource("/icons/blank.png")); 
    	Image image = icon.getImage(); 
    	Image newImg = image.getScaledInstance(13, 13, Image.SCALE_SMOOTH); 
    	icon = new ImageIcon(newImg); 
    	checkBox.setIcon(icon);
    	
    	icon = new ImageIcon(ColourManager.class.getResource("/icons/checkbox.png")); 
    	image = icon.getImage(); 
    	newImg = image.getScaledInstance(13, 13, Image.SCALE_SMOOTH); 
    	icon = new ImageIcon(newImg); 
    	checkBox.setSelectedIcon(icon);
    	
    	checkBox.setFocusable(false); 
    	checkBox.setOpaque(true); 
    	checkBox.setBackground(Color.decode(colourButton1)); 
    	checkBox.setHorizontalAlignment(SwingConstants.CENTER); 
    }
    
    private static void styleScrollPane(JScrollPane scrollPane)
    {
    	scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setBackground(Color.decode(colourContainer));
		scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = Color.decode(colourButton1);
		    }
		    @Override
		    protected JButton createDecreaseButton(int orientation) {
		        return createZeroButton();
		    }

		    @Override
		    protected JButton createIncreaseButton(int orientation) {
		        return createZeroButton();
		    }
		});
		scrollPane.getHorizontalScrollBar().setBackground(Color.decode(colourContainer));
		scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = Color.decode(colourButton1);
		    }
		    @Override
		    protected JButton createDecreaseButton(int orientation) {
		        return createZeroButton();
		    }

		    @Override
		    protected JButton createIncreaseButton(int orientation) {
		        return createZeroButton();
		    }
		});
		scrollPane.setOpaque(true);
		scrollPane.setBackground(Color.decode(colourContainer));
    }
    
    private static void styleTextArea (JTextArea textArea)
    {
    	textArea.setBackground(Color.decode(colourContainer));
		textArea.setForeground(Color.decode(colourText));
		textArea.setBorder(BorderFactory.createCompoundBorder(textArea.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		textArea.setWrapStyleWord(true); 
//		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
//		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
    }
    
    private static void styleComboBox(JComboBox comboBox)
    {	
    	comboBox.setUI(new BasicComboBoxUI() 
		{
            @Override
            protected JButton createArrowButton() {
            	BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode(colourContainer), null, Color.decode(colourText), null);
                return arrowButton;
            }
        });

		comboBox.setRenderer(new ListCellRenderer<String>() {
	        @Override
	        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
	                boolean isSelected, boolean cellHasFocus) {
	            JLabel result = new JLabel(value);
	            result.setOpaque(true);
	            result.setBackground(Color.decode(isSelected ? colourGreen : (index % 2 == 0 ? colourButton1 : colourButton2))); //---item background color
	            result.setForeground(Color.decode(colourText));
	            result.setPreferredSize(new Dimension(result.getPreferredSize().width, 25));
	            result.setBorder(new EmptyBorder(0, 10, 0, 0));
	            return result;
	        }
	    });
		
		if (comboBox.isEditable())
		{
			comboBox.getEditor().getEditorComponent().setBackground(Color.decode(colourButton1));
			comboBox.getEditor().getEditorComponent().setForeground(Color.decode(colourText));
			((JComponent) comboBox.getEditor().getEditorComponent()).setBorder(new EmptyBorder(0, 10, 0, 0)); 
		}
		else 
		{
			comboBox.setBackground(Color.decode(colourButton1)); 
			comboBox.setForeground(Color.decode(colourText)); 
			comboBox.setFocusable(false); 
		}
    }
    
    private static void styleSpinner(JSpinner spinner)
    {
    	spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
            	Component c = new BasicArrowButton(BasicArrowButton.NORTH, Color.decode(colourContainer), null, Color.decode(colourText), null);
                c.setName("Spinner.nextButton");
                c.setBackground(Color.decode(colourContainer));
                installNextButtonListeners(c);
                return c;
            }

            protected Component createPreviousButton() {
            	Component c = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode(colourContainer), null, Color.decode(colourText), null); 
                c.setName("Spinner.previousButton");
                c.setBackground(Color.decode(colourContainer));
                installPreviousButtonListeners(c);
                return c;
            }
        });
        
        spinner.setBackground(Color.decode(colourButton1));
        spinner.setForeground(Color.decode(colourText));
        
        spinner.getEditor().getComponent(0).setBackground(Color.decode(colourButton1));
		spinner.getEditor().getComponent(0).setForeground(Color.decode(colourText));
    }
    
    private static void styleTextField(JTextField textField)
    {
    	textField.setBackground(Color.decode(colourButton1));
		textField.setForeground(Color.decode(colourText));
		textField.setOpaque(true);	
    }
    
    private static JButton createZeroButton() {
        JButton button = new JButton("zero button");
        Dimension zeroDim = new Dimension(0,0);
        button.setPreferredSize(zeroDim);
        button.setMinimumSize(zeroDim);
        button.setMaximumSize(zeroDim);
        return button;
    }
    
    public static void styleLabelIcon(JLabel label, ImageIcon icon, int size)
    {
    	Image image = icon.getImage(); 
    	Image newImg = image.getScaledInstance(size, size, Image.SCALE_SMOOTH); 
    	icon = new ImageIcon(newImg); 
    	label.setHorizontalAlignment(SwingConstants.CENTER);
    	label.setIcon(icon); 
    	label.setBackground(Color.decode(colourButton1));
    	label.setOpaque(true);
    	
		iconLabels.add(label); 
    }
}
