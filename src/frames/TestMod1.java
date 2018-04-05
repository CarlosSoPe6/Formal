package frames;

import javax.swing.JOptionPane;

import modules.ReversePolishNotation;

public class TestMod1 {

	public static void main(String[] args) {
		String string = JOptionPane.showInputDialog("Ingresa una cadena: ");
		JOptionPane.showMessageDialog(null, new ReversePolishNotation(string).getPostfix());
	}

}
