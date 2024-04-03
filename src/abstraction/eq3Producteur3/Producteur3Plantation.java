package abstraction.eq3Producteur3;

import java.util.HashMap;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

public abstract class Producteur3Plantation extends Producteur3Acteur {
	abstract HashMap<Feve,Double> quantite();

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
	//on recupere les surfaces du step precedent (pour l'instant valeurs arbitraires)
		h.put(Feve.F_BQ, surfaceHQ); 
		h.put(Feve.F_MQ, surfaceMQ);
		h.put(Feve.F_HQ, surfaceBQ);
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
		for (Feve f : surfaces.keySet()) {
			double supp = 0; //initialisation de la surface supplementaire
			double delta = Filiere.LA_FILIERE; // difference entre ventes et production
			if (delta > ) { // si on vend plus que ce que l'on produit
				supp += 10;
				
			}
			surfaces.put(f, surfaces.get(f)+supp);
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


