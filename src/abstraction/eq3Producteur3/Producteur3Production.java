package abstraction.eq3Producteur3;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur3Production extends Producteur3Plantation { 
	// prix production/ha.step
	protected double kg_ha_BQ = 28.125 ;						//avec pesticide
	protected double kg_ha_MQ = 26.5625 ;
	protected double kg_ha_HQ = 25.0 ;
	protected double kg_ha_HQ_E = 22.5 ;     					//sans pesticide (bio équitable)
	protected HashMap< Integer , HashMap<Feve,Double> > prodTemps;
	
	protected void setProdTemps(HashMap<Feve, Double> d0,HashMap<Feve, Double> d1) {
		prodTemps.put(0, d0);
		prodTemps.put(1, d1);
	}
	
	
	/**
	 * Dictionnaire renvoyant la quantité produite pour chaque type de cacao (et types bio/équitable). 
	 * Prend en compte les surfaces de plantation et le prix.
	 * @author galem (Gabin)
	 */
	protected HashMap<Feve,Double> newQuantite() {
		HashMap<Feve,Double> quantite = new HashMap<Feve,Double>();
		HashMap<Feve,Double> plant = plantation();
		for(Feve f1 : plant.keySet()) {
			if (f1.getGamme() == Gamme.BQ) {            		// FEVE BASSE QUALITE (NI BIO NI EQUITABLE)
				quantite.put(f1, kg_ha_BQ*plant.get(f1));
			}			
			else if (f1.getGamme() == Gamme.MQ) { 
				if (f1.isEquitable() == false) {				// FEVE MOYENNE QUALITE (NI BIO NI EQUITABLE)
					quantite.put(f1, kg_ha_MQ*plant.get(f1));
				} 
				else {											// FEVE MOYENNE QUALITE (EQUITABLE , NON BIO )
					quantite.put(f1, kg_ha_MQ*plant.get(f1));
				}
			}	
			else if (f1.getGamme() == Gamme.HQ) {      
				if ( f1.isBio() == false) {
					if (f1.isEquitable()== false) { 			// HAUTE QUALITE (NI BIO NI EQUITABLE)
						quantite.put(f1, kg_ha_HQ*plant.get(f1));
					}
					else {										// HAUTE QUALITE (EQUITABLE, NON BIO)
						quantite.put(f1, kg_ha_HQ*plant.get(f1));
					}
				}
				else {											// HAUTE QUALITE (BIO EQUITABLE)
					quantite.put(f1, kg_ha_HQ_E*plant.get(f1)); 
				}

			}
		}
		return quantite;
		
		
	}	
	//stockage dans le temps de 2 dictionnaires
	protected HashMap< Integer , HashMap<Feve,Double> > prodTemps() {
		prodTemps.put(0, prodTemps.get(1));
		prodTemps.put(1, newQuantite());
		return prodTemps;
		
	}
	
	//renvoie la quantité de fèves après séchage, soit 1 step avant (2 semaines plus tard)
	// la quantité dispo pour la vente.
	protected HashMap<Feve,Double> quantite(){
		HashMap<Feve,Double> quantite = new HashMap<Feve,Double>();
		quantite=prodTemps().get(0);
		return quantite;
	}
	
}
