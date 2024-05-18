package abstraction.eq3Producteur3;

/**
 * Producteur3Acteur
 *    /|\
 *     |
 * Producteur3Plantation
 *    /|\
 *     |
 * Producteur3Production
 * 	  /|\
 *     |
 * Producteur3VendeurBourse
 *    /|\
 *     |
 * Producteur3VendeurCCadre
 *    /|\
 *     |
 * Producteur3
 */

public class Producteur3 extends Producteur3VendeurContratCadre  {
	
	public Producteur3() {
		super();
	}
	public void next() {
		super.next();
	}
}

/**Ordre execution
 * 
 * Clients Finaux -> eq 1 -> eq2 -> ... -> eq9 -> Romu -> SuperviseurCC -> bourse -> superviseur AO -> superviseur OA
 *
**/