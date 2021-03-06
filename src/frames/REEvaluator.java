package frames;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import modules.NFDeToNFD;
import modules.NodeToMatrixConverter;

import modules.RPNToNFDE;
import modules.ReversePolishNotation;

/**
 * Class that evaluates a file. Contains methods to do so both from console or
 * from a text file.
 */
public class REEvaluator {

	public static void main(String[] args) {

		// Procesamiento de un archivo de texto con dicho AFN
		JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);

		// Restricts to only .txt files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
		chooser.setFileFilter(filter);

		int selection = chooser.showOpenDialog(null);

		if (selection == JFileChooser.APPROVE_OPTION) {
			String path = chooser.getSelectedFile().getAbsolutePath();
			int confirm = JOptionPane.showConfirmDialog(null, "Confirm file:\n" + path);
			if (confirm == JOptionPane.YES_OPTION) {
				// Calls the method that tests the Strings contained in the txt file
				evaluateFile(path);
			}
		}

	}

	/**
	 * Set containing all strings accepted by the language. Helps to avoid repeated
	 * strings.
	 */
	static HashSet<String> answers = new HashSet<>();

	/**
	 * Initial call that evaluates if a string is accepted by the NFD
	 * 
	 * @param automata
	 *            The NFD matrix generated.
	 * @param string
	 *            The string to be tested.
	 * @return True if accepted
	 */
	public static boolean isAccepted(LinkedList<Integer>[][] automata, String string) {
		int currentState = 0, charAt = 0;
		boolean accepted = false;
		char symbol = string.charAt(charAt);
		LinkedList<Integer> nextStates = automata[currentState][symbol];
		for (int nextState : nextStates) {
			if (charAt + 1 < string.length()) {
				accepted = isAcceptedRecursive(automata, string, nextState, charAt + 1);
			}
			if (nextState == (automata.length - 1) && charAt + 1 >= string.length()) {
				currentState = nextState;
				answers.add(string.substring(0, charAt + 1)); // charAt+1 since nextState is final and there's no more
																// input to process.
				return true;
			}
			// if(accepted) break;
		}

		LinkedList<Integer> wildcardStates = automata[currentState]['&'];
		for (int nextState : wildcardStates) {
			if (charAt + 1 < string.length()) {
				accepted = isAcceptedRecursive(automata, string, nextState, charAt + 1);
			}
			if (nextState == (automata.length - 1) && charAt + 1 >= string.length()) {
				currentState = nextState;
				answers.add(string.substring(0, charAt + 1)); // charAt+1 since nextState is final and there's no more
																// input to process.
				return true;
			}
			// if(accepted) break;
		}
		if (currentState == (automata.length - 1)) {// If currentState is final
			answers.add(string.substring(0, charAt));
			return true;
		}

		return accepted;
	}

	/**
	 * Helper method that recurs through states in order to test all possible paths
	 * to make decision if string is accepted.
	 * 
	 * @param automata
	 *            The NFD matrix representation.
	 * @param string
	 *            The string to be evaluated.
	 * @param currentState
	 *            The current State represented as a column value(int).
	 * @param charAt
	 *            The current amount of characters processed from string.
	 * @return True if there's an accepted string.
	 */
	public static boolean isAcceptedRecursive(LinkedList<Integer>[][] automata, String string, int currentState,
			int charAt) {
		boolean accepted = false;
		char symbol = string.charAt(charAt);
		LinkedList<Integer> nextStates = automata[currentState][symbol];
		for (int nextState : nextStates) {
			if (charAt + 1 < string.length()) {
				accepted = isAcceptedRecursive(automata, string, nextState, charAt + 1);
			}
			if (nextState == (automata.length - 1) && charAt + 1 >= string.length()) {
				currentState = nextState;
				answers.add(string.substring(0, charAt + 1)); // charAt+1 since nextState is final and there's no more
																// input to process.
				return true;
			}
			// if(accepted) break;
		}
		// After it evaluates a string for every accepted character, it searches for a
		// wildcard symbol transition to accept any input for one transition:
		LinkedList<Integer> wildcardStates = automata[currentState]['&'];
		for (int nextState : wildcardStates) {
			if (charAt + 1 < string.length()) {
				accepted = isAcceptedRecursive(automata, string, nextState, charAt + 1);
			}
			if (nextState == (automata.length - 1) && charAt + 1 >= string.length()) {
				currentState = nextState;
				answers.add(string.substring(0, charAt + 1)); // charAt+1 since nextState is final and there's no more
																// input to process.
				return true;
			}
			// if(accepted) break;
		}
		if (currentState == (automata.length - 1)) {// If currentState is final
			answers.add(string.substring(0, charAt));
			return true;
		}
		return accepted;
	}

	/**
	 * Method that evaluates a regular expression and tests string directly from
	 * console input. The input must contain a RegEx in the first line, followed by
	 * n strings to be tested. Last line must have a -1 input.
	 */
	public static void evaluateFromConsole() {
		Scanner s = new Scanner(System.in);
		String rpn = new ReversePolishNotation(s.nextLine()).getPostfix();
		// JOptionPane.showMessageDialog(null, rpn);
		RPNToNFDE converter = new RPNToNFDE();

		// We convert that expression to NFD-E
		RPNToNFDE.State n = converter.convert(rpn);

		LinkedList<Integer>[][] nfdeMatrix = NodeToMatrixConverter.convert(n);
		System.out.println("\nNFDe");
		for (int i = 0; i < nfdeMatrix.length; i++) {
			System.out.print("q" + i + " ");
			for (int j = 0; j < 255; j++) {
				if (!nfdeMatrix[i][j].isEmpty())
					System.out.print(nfdeMatrix[i][j].toString() + (char) (j) + " ");
			}
			System.out.println();
		}

		System.out.println("\nNFD");
		LinkedList<Integer>[][] nfdMatrix = new NFDeToNFD(nfdeMatrix).convert();
		for (int i = 0; i < nfdMatrix.length; i++) {
			System.out.print("q" + i + " ");
			for (int j = 0; j < 254; j++) {
				if (!nfdMatrix[i][j].isEmpty())
					System.out.print(nfdMatrix[i][j].toString() + (char) (j) + " ");
			}
			System.out.println();
		}

		String string = s.nextLine();
		while (!string.equals("-1")) {
			for (int i = 0; i < string.length(); i++) {
				isAccepted(nfdMatrix, string.substring(i, string.length()));
			}
			for (String accepted : answers)// Prints all results from iterations without repeats thanks to Set
											// properties
				System.out.println(accepted);
			answers.clear();// Clears set for next iteration
			string = s.nextLine();
		}
		s.close();
	}

	/**
	 * Method that evaluates a regular expression and tests string directly from txt
	 * file. The input must contain a RegEx in the first line, followed by n strings
	 * to be tested.
	 */
	public static void evaluateFile(String path) {
		// First we open our file
		FileReader fr;
		try {
			fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			// After that, the first line contains the RE to create the NFD. We save it and use module 1 to get Postfix
			String rpn = new ReversePolishNotation(br.readLine()).getPostfix();
			// JOptionPane.showMessageDialog(null, rpn);
			RPNToNFDE converter = new RPNToNFDE();

			// We convert that expression to NFD-E
			RPNToNFDE.State n = converter.convert(rpn);

			LinkedList<Integer>[][] nfdeMatrix = NodeToMatrixConverter.convert(n);
			System.out.println("\nNFDe");
			for (int i = 0; i < nfdeMatrix.length; i++) {
				System.out.print("q" + i + " ");
				for (int j = 0; j < 255; j++) {
					if (!nfdeMatrix[i][j].isEmpty())
						System.out.print(nfdeMatrix[i][j].toString() + (char) (j) + " ");
				}
				System.out.println();
			}

			System.out.println("\nNFD");
			LinkedList<Integer>[][] nfdMatrix = new NFDeToNFD(nfdeMatrix).convert();
			for (int i = 0; i < nfdMatrix.length; i++) {
				System.out.print("q" + i + " ");
				for (int j = 0; j < 254; j++) {
					if (!nfdMatrix[i][j].isEmpty())
						System.out.print(nfdMatrix[i][j].toString() + (char) (j) + " ");
				}
				System.out.println();
			}

			FileWriter fw;
			fw = new FileWriter((path.substring(0, path.lastIndexOf('\\') + 1)) + "Evaluation_Result_AAC.txt");
			BufferedWriter bw = new BufferedWriter(fw);

			try {
				String string = br.readLine();
				System.out.println(string);
				while (string != null) {
					for (int i = 0; i < string.length(); i++) {
						isAccepted(nfdMatrix, string.substring(i, string.length()));
					}
					for (String accepted : answers) {// Prints all results from iterations without repeats thanks to Set properties
						bw.write(accepted);
						bw.write("\r\n");
					}
					answers.clear();// Clears set for next iteration
					string = br.readLine();
				}
			} catch (IOException e1) {
				System.out.println("File Writing error.");
			}
			bw.close();
			fw.close();
			br.close();
			fr.close();
			// Sends information message of success.
			JOptionPane.showMessageDialog(null,
					"File processed successfully. Resulting file saved as Evaluation_Result_AAC.txt into same directory.",
					"File processed", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			System.out.println("File error.");
		}
	}

}
