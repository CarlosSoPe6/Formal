package frames;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import modules.NFDeToNFD;
import modules.NodeToMatrixConverter;

import modules.RPNToNFDE;
import modules.ReversePolishNotation;

public class REEvaluator {

	public static void main(String[] args) {
		//JFile Chooser to make our evaluator more intuitive.
		JFileChooser chooser=new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);

		//Restricts to only .txt files
		FileNameExtensionFilter filter=new FileNameExtensionFilter("Text files", "txt");
		chooser.setFileFilter(filter);

		int selection=chooser.showOpenDialog(null);

		if(selection==JFileChooser.APPROVE_OPTION) {
			String path=chooser.getSelectedFile().getAbsolutePath();
			int confirm=JOptionPane.showConfirmDialog(null, "Confirm compression from file:\n"+path);
			if(confirm==JOptionPane.YES_OPTION) {
				//Calls the method that tests the Strings contained in the txt file
				evaluateFile(path);
			}
		}

	}

	private static void evaluateFile(String path) {
		//First we open our file
		FileReader fr;
		try {
			fr = new FileReader(path);
			BufferedReader br=new BufferedReader(fr);

			//After that, the first line contains the RE to create the NFA. We save it and use module 1 to get Postfix
			String rpn = new ReversePolishNotation(br.readLine()).getPostfix();
			//JOptionPane.showMessageDialog(null, rpn);
			RPNToNFDE converter = new RPNToNFDE();

			//We convert that expression to NFA-E
			RPNToNFDE.NFDENode n = converter.convert(rpn);

			LinkedList<Integer>[][] nfdeMatrix = NodeToMatrixConverter.convert(n);
			/*System.out.println("\nNFDe");
		for(int i = 0; i < nfdeMatrix.length; i ++) {
			System.out.print("q" + i + " ");
			for(int j = 0; j < 255; j ++) {
				if(!nfdeMatrix[i][j].isEmpty()) System.out.print(nfdeMatrix[i][j].toString() + (char) (j + ' ') + " ");
			}
			System.out.println();
		}*/

			//System.out.println("\nNFD");
			//We convert that NFA-E TO NFA
//			LinkedList<Integer>[][] nfdMatrix = new NFDeToNFD(nfdeMatrix).convert();
			/*for(int i = 0; i < nfdMatrix.length; i ++) {
        	System.out.print("q" + i + " ");
			for(int j = 0; j < 254; j ++) {
				if(!nfdMatrix[i][j].isEmpty()) System.out.print(nfdMatrix[i][j].toString() + (char) (j + ' ') + " ");
			}
			System.out.println();
		}*/


			FileWriter fw;
			fw = new FileWriter("Evaluation_Result_AAC.txt");//AAC stands for Alfredo Alejandro Carlos initials.
			BufferedWriter bw=new BufferedWriter(fw);
			//After we have the automaton and our result file open, we evaluate the file's next lines
//			String text="";
			try { 
				String line= br.readLine();
				while(line != null) {
					//
					for(int i=0; i<line.length();i++) {
						String temp=line.substring(i, line.length()-1);
						//Evaluates if current string is accepted by the NFA. Prints if true.
						if(true) {//Here goes evaluation method that returns a boolean if accepted.
						System.out.println(temp);
						bw.write(temp);;
						}
					}
					line=br.readLine();
				}
			} catch (IOException e1) {
				System.out.println("File Writing error.");
			} 
			//Closing buffers and files
			bw.close();
			fw.close();
			br.close();
			fr.close();
			//Sends information message of success.
			JOptionPane.showMessageDialog(null, "File processed succesfully. Resulting file saved as Evaluation_Result_AAC.txt on project folder.", "File processed", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			System.out.println("File error.");
		}
	}

}
