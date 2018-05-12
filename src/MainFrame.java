import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JLabel textLabel;
	private JTextField regularExpression;
	private JButton fileSelectorButton;
	private JButton runTextFinderButton;
	private File file;
	
	public MainFrame() {
		frame = new JFrame();
		
		frame.setSize(370, 100);
		frame.setResizable(false);
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - 185, 
						  (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - 50);
		frame.setTitle("Text Finder with RE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textLabel = new JLabel("Regular Expression: ");
		regularExpression = new JTextField("", 6);					
		
		fileSelectorButton = new JButton("Choose File");			//ABRE EL FILE CHOOSER Y GUARDA EN file EL ARCHIVO A LEER
		fileSelectorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Frame f = new Frame();
				FileDialog fd = new FileDialog(f, "Choose a text file", FileDialog.LOAD);
		        fd.setDirectory("C:\\");
		        fd.setVisible(true);
		        file = fd.getFiles()[0];
				f.dispose();
			}
		});
		
		runTextFinderButton = new JButton("Find text");
		// Añadir el action listener que buscará el texto que aprueba la ER y guardar el archivo resultante
		
		
		frame.setLayout(new GridLayout(2, 2));
	
		frame.add(textLabel);
		frame.add(regularExpression);
		frame.add(fileSelectorButton);
		frame.add(runTextFinderButton);
		
		frame.setVisible(true);
	}
	
	public static void main(String args[]) {
		new MainFrame();
	}

	
}
