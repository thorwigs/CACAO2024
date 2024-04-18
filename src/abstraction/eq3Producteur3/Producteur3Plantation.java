package abstraction.eq3Producteur3;

import java.util.HashMap;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

public abstract class Producteur3Plantation extends Producteur3Acteur {
	abstract HashMap<Feve,Double> quantite();
	abstract void setProdTemps(HashMap<Feve, Double> d0,HashMap<Feve, Double> d1);
/**
 * les variables surfaceXQ donnent le nombre d'hectares qui produisent des feves
 * de qualité XQ @alexis
 * Calcul des valeurs initials par @gabin
 */
	private double surfaceHQ = 22.74*1000;
	private double surfaceHQBE = 8.42*1000;
	private double surfaceHQE = 7.58*1000;
	private double surfaceMQ = 47.57*1000;
	private double surfaceMQE = 11.89*1000;
	private double surfaceBQ = 134.775*1000;

///Gestion de la plantation
	
	/**
	 * Le dictionnaire plantation a pour cle Feve 
	 * et pour valeur la variable surfaceXQ associee du step precedent.
	 * @author Alexis
	 */
	protected HashMap<Feve, Double> plantation() {
		HashMap<Feve, Double> h = new HashMap<Feve, Double>();
	//on recupere les surfaces du step precedent (pour l'instant valeurs arbitraires)
		h.put(Feve.F_HQ, surfaceHQ);
		h.put(Feve.F_HQ_BE, surfaceHQBE);
		h.put(Feve.F_HQ_E, surfaceHQE);
		h.put(Feve.F_MQ, surfaceMQ);
		h.put(Feve.F_MQ_E, surfaceMQE);
		h.put(Feve.F_BQ, surfaceBQ);
	//on augmente la surface 
		achatPlantation(h);
		return h;
	}

	/**
	 * Cette methode determine la surface supplementaire a acheter pour chaque type de feve
	 * Elle s'appuie sur la production et les ventes du step precedent
	 * @author Alexis
	 */
	protected HashMap<Feve, Double> achatPlantation(HashMap<Feve, Double> surfaces) {
		for (Feve f : prodfeve.keySet()) {
			double supp = 0; //initialisation de la surface supplementaire
			double delta = ventefeve.get(f).getValeur() - prodfeve.get(f).getValeur();// difference entre vente et production de f
			// this.journal.ajouter(f.toString() + "ventes " + ventefeve.get(f).getValeur()+", "+"production " + prodfeve.get(f).getValeur());
			if (delta > 50) { // si on vend beaucoup plus que ce que l'on produit (en tonnes)
				supp += 10; 
			}
			surfaces.put(f, surfaces.get(f)+supp); // on augmente la surface de plantation pour le type f (en ha)
		}
		return surfaces;
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


