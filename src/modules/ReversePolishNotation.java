package modules;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ReversePolishNotation {
	String infixExpression;
	String postfixExpression;

	public ReversePolishNotation(String notation) {
		this.infixExpression = notation;
		this.postfixExpression = infixToPostfix(notation);
	}

	public String getPostfix() {
		return this.postfixExpression;
	}

	public String getInfix() {
		return this.infixExpression;
	}

	public static String infixToPostfix(String expresion) {
		char[] cadena = expresion.toCharArray();
		Stack<Character> pila = new Stack<Character>();
		Queue<Character> cola = new LinkedList<Character>(), q = new LinkedList<Character>();
		pila.add('(');
		for (char c : cadena) {
			cola.add(c);
		}
		cola.add(')');
		// Repeats until the end of the expression
		do {
			char c = (char) cola.poll();
			// Character validation.
			if ((c > 47 && c < 58) || (c > 96 && c < 123) || (c > 64 && c < 91)
					|| (c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú')
					|| (c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó' || c == 'Ú') || c == '\n' || c == ' '
					||  c == '&') {
				q.add(c);
			}

			// If left parenthesis found, push it to stack.
			else if (c == 40) {
				pila.push(c);
			}

			// If it is an operator, check precedence
			else if (c == '+' || c == ',' || c == '*' || c == '•') {
				char car = 0;
				if (pila.size() != 0) {
					car = pila.peek();
				}

				while ((pila.size() != 0) && car != 40) {
					if (hasHigherPrecedence(car, c)) {
						q.add((char) pila.pop());
					} else
						break;
					if (!pila.empty())
						car = pila.peek();
				}
				pila.push(c);
			}

			else if (c == ')') {
				char car = pila.peek();
				while ((!pila.empty()) && car != '(') {
					q.add((char) pila.pop());
					car = pila.peek();
				}
				if (pila.size() != 0) {
					pila.pop();
				}
			}
			// The character isn't supported by the language.
			else {
				System.out.println("Invalid expression. It contains unrecognizable characters.");
				break;
			}

		} while (cola.size() != 0);

		while (pila.size() != 0) {
			q.add(pila.pop());
		}

		String expresionFinal = "";

		do {
			expresionFinal += q.poll();

		} while (q.size() != 0);

		return expresionFinal;
	}

	public static boolean hasHigherPrecedence(char top, char operator) {
		if (top == ',')
			return false;
		else if (top == '•') {
			if (operator == '*' || operator == '+' || operator == '(' || operator == ')')
				return false;
			else
				return true;
		} else if (top == '+') {
			if (operator == '(' || operator == '*' || operator == ')')
				return false;
			else
				return true;
		} else {
			if (operator == '(' || operator == ')')
				return false;
			return true;
		}
	}

}