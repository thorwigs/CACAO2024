package abstraction.eq3Producteur3;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur3Production extends Producteur3Plantation { 
	//@Gabin
	// prix production/ha/step
	protected double T_ha_BQ = 28.125/1000 ;	//avec pesticide
	protected double T_ha_MQ = 26.5625/1000 ;
	protected double T_ha_HQ = 25.0/1000 ;
	protected double T_ha_HQ_BE = 22.5/1000 ;   //sans pesticide (bio équitable)
	protected HashMap< Integer , HashMap<Feve,Double> > prodTemps = new HashMap<Integer,HashMap<Feve,Double>>(); //variable qui sert a prendre en compte le temps de séchage
	
	/**
	 * @author Gabin
	 * @param HashMap<Feve,Double> d0, d1 (production pour les différents types de fèves au temps 0 et 1
	 * Initialise la variable prodTemps qui sert a prendre en compte le temps de séchage
	 */
	protected void setProdTemps(HashMap<Feve, Double> d0,HashMap<Feve, Double> d1) {
		prodTemps.put(0, d0);
		prodTemps.put(1, d1);
	}
	
	
	/**
	 * @author Gabin
	 * @return HashMap<Feve,Double> (tableau des récoltes selon les fèves)
	 * Renvoie un dictionnaire avec la quantité récoltée pour chaque type de cacao (et types bio/équitable). Il faut encore faire sécher les fèves après
	 * Prend en compte les surfaces de plantation et le prix.
	 */
	protected HashMap<Feve,Double> newQuantite() {
		HashMap<Feve,Double> quantite = new HashMap<Feve,Double>();
		HashMap<Feve,Double> plant = plantation();
		for(Feve f1 : plant.keySet()) {
			if (f1.getGamme() == Gamme.BQ) {            		// FEVE BASSE QUALITE (NI BIO NI EQUITABLE)
				quantite.put(f1, T_ha_BQ*plant.get(f1));
			}			
			else if (f1.getGamme() == Gamme.MQ) { 
				if (f1.isEquitable() == false) {				// FEVE MOYENNE QUALITE (NI BIO NI EQUITABLE)
					quantite.put(f1, T_ha_MQ*plant.get(f1));
				} 
				else {											// FEVE MOYENNE QUALITE (EQUITABLE , NON BIO )
					quantite.put(f1, T_ha_MQ*plant.get(f1));
				}
			}	
			else if (f1.getGamme() == Gamme.HQ) {      
			
				if ( f1.isBio() == false) {
					if (f1.isEquitable()== false) { 			// HAUTE QUALITE (NI BIO NI EQUITABLE)
						quantite.put(f1, T_ha_HQ*plant.get(f1));
					}
					else {										// HAUTE QUALITE (EQUITABLE, NON BIO)
						quantite.put(f1, T_ha_HQ*plant.get(f1));
					}
				}
				else {											// HAUTE QUALITE (BIO EQUITABLE)
					quantite.put(f1, T_ha_HQ_BE*plant.get(f1)); 
				}

			}
		}
		return quantite;
		
		
	}	
	/**
	 * @author Gabin
	 * @return HashMap<Integer,HashMap<Feve,Double>> prodTemps (quantités produites par fèves sur ce step et le précédent)
	 * Permet de prendre en compte le temps de séchage (1 step) en le stockant dans un dictionnaire et en le faisant glisser a chaque step
	 */
	protected HashMap< Integer , HashMap<Feve,Double> > prodTemps() {
		prodTemps.put(0, prodTemps.get(1));
		prodTemps.put(1, newQuantite());
		return prodTemps;
		
	}
	
	/**
	 * @author Gabin
	 * @return HashMap<Feve,Double> quantite (quantités produites par fèves)
	 * Permet de savoir la production que l'on peut effectivement vendre (après le temps de séchage)
	 */
	protected HashMap<Feve,Double> quantite(){
		HashMap<Feve,Double> quantite = new HashMap<Feve,Double>();
		quantite=prodTemps().get(0);
		return quantite;
	}
	
}
