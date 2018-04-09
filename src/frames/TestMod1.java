package frames;

import javax.swing.JOptionPane;

import modules.RPNToNFDE;
import modules.ReversePolishNotation;

public class TestMod1 {

	public static void main(String[] args) {
		String string = JOptionPane.showInputDialog("Ingresa una cadena: ");
		String rpn = new ReversePolishNotation(string).getPostfix();
		JOptionPane.showMessageDialog(null, rpn);
		
		RPNToNFDE converter = new RPNToNFDE();
		converter.convert(rpn);
	}

}
