package abstraction.eq3Producteur3;

import java.util.HashMap;
import abstraction.eqXRomu.produits.Feve;

public abstract class Producteur3Plantation extends Producteur3Acteur {
	abstract HashMap<Feve,Double> quantite();
	abstract void setProdTemps();
/**
 * les variables surfaceXQ donnent le nombre d'hectares qui produisent des feves
 * de qualité XQ
 */
	private double surfaceHQ;
	private double surfaceMQ;
	private double surfaceBQ;

///Gestion de la plantation
	/**
	 * Le dictionnaire plantation a pour cle Feve 
	 * et pour valeur la variable surfaceXQ associee du step precedent.
	 * @author Alexis
	 */
	protected HashMap<Feve, Double> plantation() {
		HashMap<Feve, Double> h = new HashMap<Feve, Double>();
		return h;
	}

	
///Gestion de la main d'oeuvre///

	/**
	 * Renvoie le nombre d'ouvriers necessaires et le type de feve 
	 * selon la superficie (en ha) et le type de plantation
	 * @author Arthur
	*/
	protected HashMap<Feve,Double> maindoeuvre() {
		//renvoie le nombre d'ouvriers necessaires et le type de feve selon la superficie (en ha) et le type de plantation
		HashMap<Feve,Double> surfaces = plantation();
		HashMap<Feve,Double> ouvriers = new HashMap<Feve,Double>();
		for (Feve f : surfaces.keySet()) {
			if (f.isBio()) {
				//1.5 ouvriers par ha en bio
				ouvriers.put(f, 1.5*surfaces.get(f));
			} else {
				//1 ouvrier par ha en conventionnel
				ouvriers.put(f, surfaces.get(f));
			}
		}
		return ouvriers;
	}	 

}


