import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AhoCorasick {
	public static void main(String[] args) {				
		String cadenas[] = { "he", "she", "his", "hers" };
		char target[] = { 'u', 's', 'h', 'e', 'r', 'h', 'i', 's' };
		
		System.out.println("Cadena a buscar:" + Arrays.toString(target) + "\n");

		AhoCorasick ac = new AhoCorasick();						
		List<int[]> res = ac.buscar(cadenas, target);
		
		System.out.println("\nResultados...\n");
		for(int[] r : res) {
		  System.out.println("cadena:{" + cadenas[r[1]] + "} aparece en la pos:{" + r[0] + "}");
		}
		

	}

	LinkedList<Nodo> nodos;
	Nodo root;
	int nNodos;

	public AhoCorasick() {
		/* inicializo Aho y nodo inicial root */
		this.nodos = new LinkedList<>();

		this.root = new Nodo();
		this.root.esHoja = false;
		this.root.padre = null;
		this.root.fail = null;

		this.nodos.add(this.root);
		this.nNodos = 1;
	}

	public static class Nodo {
		char ch;
		int id, length;
		Nodo padre;
		Nodo fail;
		boolean esHoja;

		LinkedList<Nodo> hijos;

		public Nodo() {

		}

		Nodo traerHijo(char c) {
			if (hijos != null)
				for (Nodo t : this.hijos)
					if (t.ch == c)
						return t;
			return null;
		}

		Nodo traerFail(Nodo root, char ch) {
			Nodo t = traerHijo(ch);
			return t != null ? t : (this == root ? root : null);
		}

		@Override
		public String toString() {
			String res = "Nodo[" + this.ch + "] esHoja[" + (this.esHoja ? "S" : "N") + "]";
			res += " Padre[" + (padre != null ? "" + padre.ch : null) + "]";
			res += " Fail[" + (fail != null ? "" + fail.ch : null) + "]";
			return res;
		}
	}

	public void adicionarCadena(String cadena, int id) {
		System.out.println("agregando:{" + cadena + "}");
		Nodo ini = this.root, temp;
		int i = 0;
		for (char ch : cadena.toCharArray()) {
			i++;
			/* valido si este nodo ini ya tiene este hijo */
			if ((temp = ini.traerHijo(ch)) == null) {
				if (ini.hijos == null)
					ini.hijos = new LinkedList<Nodo>();
				Nodo hijo = new Nodo();
				hijo.padre = ini;
				hijo.ch = ch;
				hijo.id = id;				
				ini.hijos.add(hijo);

				this.nodos.add(hijo);

				ini = hijo;

				this.nNodos++;
			} else
				ini = temp;
		}

		ini.esHoja = true;
		ini.length = i; 
	}

	public void PrepararTrie() {
		Queue<Nodo> queue = new LinkedList<Nodo>();
		Nodo r, v, p;
		if (root.hijos != null)
			for (Nodo n : root.hijos) {
				n.fail = root;
				queue.add(n);
			}

		while (!queue.isEmpty()) {
			if ((r = queue.poll()).hijos != null)
				for (Nodo u : r.hijos) {
					queue.add(u);

					for (v = r.fail; (p = v.traerFail(root, u.ch)) == null;)
						v = v.fail;
					u.fail = p;

				}
		}
	}

	public List<int[]> buscar(String[] cadenas, char[] target) {

		List<int[]> res = new ArrayList<int[]>();
		Nodo q = root, p;

		for (int k = 0; k < cadenas.length; k++)
			adicionarCadena(cadenas[k], k);

		System.out.println("\nPreparar Trie...");
		PrepararTrie();
		
		for (int j = 0; j < target.length; j++) {
			// System.out.println(target[j]);
						
			while ((p = q.traerFail(root, target[j])) == null) {				
				q = q.fail;				
			}
			q = p;
			
			// System.out.println(q);
			Nodo t = q;

			while (t.esHoja) {
			    //System.out.println(t.ch + " " + t.id + " " + t.length + " " + j);
				res.add(new int[] {j - t.length + 1, t.id } );
				t=t.fail;
			}
		}
		return res;

	}

	public void printTrie() {
		System.out.println("Trie");
		for (Nodo nodo : nodos) {
			System.out.println(nodo);
			if (nodo.hijos != null)
				System.out.println("hijos (" + Arrays.toString(nodo.hijos.toArray()) + ")");
		}
	}
}
