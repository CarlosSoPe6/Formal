package frames;

import javax.swing.JOptionPane;

import modules.NodeToMatrixConverter;
import modules.RPNToNFDE;
import modules.ReversePolishNotation;

public class TestMod1 {

	public static void main(String[] args) {
		String string = JOptionPane.showInputDialog("Ingresa una cadena: ");
		String rpn = new ReversePolishNotation(string).getPostfix();
		JOptionPane.showMessageDialog(null, rpn);
		
		RPNToNFDE converter = new RPNToNFDE();
		RPNToNFDE.NFDENode n = converter.convert(rpn);

        NodeToMatrixConverter.convert(n);
	}

}
