package abstraction.eq3Producteur3;

import java.util.HashMap;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

public abstract class Producteur3Plantation extends Producteur3Acteur {
	abstract HashMap<Feve,Double> quantite();
	abstract void setProdTemps(HashMap<Feve, Double> d0,HashMap<Feve, Double> d1);
/**
 * @author Alexis et Gabin
 * les variables surfaceXQ donnent le nombre d'hectares qui produisent des feves de qualité XQ 
 * Calcul des valeurs initials par @gabin
 */
	private double surfaceHQ = 22.74*1000;
	private double surfaceHQBE = 8.42*1000;
	private double surfaceHQE = 7.58*1000;
	private double surfaceMQ = 47.57*1000;
	private double surfaceMQE = 11.89*1000;
	private double surfaceBQ = 134.775*1000;
	
/**
 * @author Arthur
 * variable qui répertorie l'âge des plants	
 * pour chaque type de fève, il y a un dictionnaire qui donne le nombre de plants achetés lors d'un step (clé)
 */
	private HashMap<Feve,HashMap<Integer,Double>> agePlant;
	
	public void intialiser() {
		super.initialiser();
		agePlant = new HashMap<Feve,HashMap<Integer,Double>>();
		agePlant.get(Feve.F_BQ).put(-700, surfaceBQ*0.3);
		agePlant.get(Feve.F_BQ).put(-300, surfaceBQ*0.3);
		agePlant.get(Feve.F_BQ).put(0, surfaceBQ*0.4);
		agePlant.get(Feve.F_MQ).put(-650, surfaceMQ*0.2);
		agePlant.get(Feve.F_MQ).put(-200, surfaceMQ*0.3);
		agePlant.get(Feve.F_MQ).put(0, surfaceMQ*0.5);
		agePlant.get(Feve.F_MQ_E).put(-150, surfaceMQE*0.1);
		agePlant.get(Feve.F_MQ_E).put(-50, surfaceMQE*0.2);
		agePlant.get(Feve.F_MQ_E).put(0, surfaceMQE*0.7);
		agePlant.get(Feve.F_HQ).put(-710, surfaceHQ*0.1);
		agePlant.get(Feve.F_HQ).put(-300, surfaceHQ*0.4);
		agePlant.get(Feve.F_HQ).put(0, surfaceHQ*0.5);
		agePlant.get(Feve.F_HQ_E).put(-100, surfaceHQE*0.7);
		agePlant.get(Feve.F_HQ_E).put(0, surfaceHQE*0.3);
		agePlant.get(Feve.F_HQ_BE).put(-50, surfaceHQBE*0.1);
		agePlant.get(Feve.F_HQ_BE).put(0, surfaceHQBE*0.9);	
	}



///Gestion de la plantation
	
	/**
	 * @author Alexis
	 * @return HashMap<Feve,Double> (tableau des surfaces par feve)
	 * Le dictionnaire plantation a pour cle Feve et pour valeur la variable surfaceXQ associee du step precedent + les eventuelles extensions.
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
	 * @author Alexis
	 * @param HashMap<Feve,Double> surfaces (tableau des surfaces par feve)
	 * @return HashMap<Feve,Double> surfaces (tableau des surfaces par feve)
	 * Cette methode determine la surface supplementaire a acheter pour chaque type de feve
	 * Elle s'appuie sur la production et les ventes du step precedent
	 */
	protected HashMap<Feve, Double> achatPlantation(HashMap<Feve, Double> surfaces) {
		for (Feve f : prodfeve.keySet()) {
			double supp = 0; //initialisation de la surface supplementaire
			double delta = ventefeve.get(f).getValeur() - prodfeve.get(f).getValeur();// difference entre vente et production de f
			if (delta > 50) { // si on vend beaucoup plus que ce que l'on produit (en tonnes)
				supp += 100; 
			}
			surfaces.put(f, surfaces.get(f)+supp); // on augmente la surface de plantation pour le type f (en ha)
		}
		return surfaces;
	}
			
	/**
	 * @author Alexis
	 * @param 
	 * @return HashMap<Feve,Double> aRemplacer (tableau des surfaces à remplacer par feve)
	 * Cette methode détermine la quantité de plants trop vieux à remplacer
	 * Elle s'appuie sur 
	 */
	protected HashMap<Feve, Double> aRemplacer(HashMap<Feve, Double> surfaces) {
		return surfaces;
	}
	
///Gestion de la main d'oeuvre///

	/**
	 * @author Arthur
	 * @return HashMap<Feve,Double> ouvriers (nombre d'ouvriers par type de feve)
	 * Renvoie le nombre d'ouvriers necessaires et le type de feve selon la superficie (en ha) et le type de plantation
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


