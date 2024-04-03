package abstraction.eq3Producteur3;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur3Plantation extends Producteur3Acteur {

/**
 * les variables surfaceXQ donnent le nombre d'hectares qui produisent des feves
 * de qualité XQ
 */
	private double surfaceHQ;
	private double surfaceMQ;
	private double surfaceBQ;

	/**
	 * Le dictionnaire plantation a pour cle Feve 
	 * et pour valeur la variable surfaceXQ associee du step precedent.
	 * 
	 */
	protected HashMap<Feve, Double> plantation() {
		HashMap<Feve, Double> h = new HashMap<Feve, Double>();
		return h;
	}
}


