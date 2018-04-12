package frames;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import modules.NFDeToNFD;
import modules.RPNToNFDE;
import modules.ReversePolishNotation;

public class TestMod1 {

	@SuppressWarnings({ "unused", "unchecked" })
	public static void main(String[] args) {
//		String string = JOptionPane.showInputDialog("Ingresa una cadena: ");
//		String rpn = new ReversePolishNotation(string).getPostfix();
//		JOptionPane.showMessageDialog(null, rpn);
//		
//		RPNToNFDE converter = new RPNToNFDE();
//		converter.convert(rpn);
//		
		LinkedList<Integer>[][] matrizConEpsilon = new LinkedList[5][255]; // [numero de estados][simbolos posibles]
		for(int i = 0; i < 5; i ++) {
			for(int j = 0; j < 255; j ++) {
				matrizConEpsilon[i][j] = new LinkedList<Integer>();
			}
		}
		
		// Automata hardcodeado
		matrizConEpsilon[0][254].add(1);
		matrizConEpsilon[1]['0' - ' '].add(1);
		matrizConEpsilon[1]['1' - ' '].add(1);
		matrizConEpsilon[1]['1' - ' '].add(2);
		matrizConEpsilon[2]['0' - ' '].add(3);
		matrizConEpsilon[2]['1' - ' '].add(3);
		matrizConEpsilon[3]['0' - ' '].add(4);
		matrizConEpsilon[3]['1' - ' '].add(4);
		matrizConEpsilon[4][254].add(1);
		
		//Prints hardcodeados
		for(int i = 0; i < 5; i ++) {
			for(int j = 0; j < 255; j ++) {
				if(!matrizConEpsilon[i][j].isEmpty()) System.out.print(matrizConEpsilon[i][j].toString() + j + " ");
			}
			System.out.println();
		}
		
		//Llamadas a la funcion de conversión
		LinkedList<Integer>[][] matrizSinEpsilon = new LinkedList[5][255];
		NFDeToNFD converter2 = new NFDeToNFD(matrizConEpsilon);
		matrizSinEpsilon = converter2.upperDeltaFunc();
		
		System.out.println();
		System.out.println();
		System.out.println("Matriz de transición sin epsilon trnasiciones!");
		
		for(int i = 0; i < 5; i ++) {
			for(int j = 0; j < 254; j ++) {
				if(!matrizSinEpsilon[i][j].isEmpty()) System.out.print(matrizSinEpsilon[i][j].toString() + j + " ");
			}
			System.out.println();
		}
		
	}

}
