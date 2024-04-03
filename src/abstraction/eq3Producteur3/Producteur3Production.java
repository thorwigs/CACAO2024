package abstraction.eq3Producteur3;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur3Production extends Producteur3Plantation { 
	// prix production/ha.step
	protected double kg_ha_BQ = 83.3 ;
	protected double kg_ha_MQ = 33.3 ;
	protected double kg_ha_HQ = 16.6 ;
	
	/**
	 * Dictionnaire renvoyant la quantité produite pour chaque type de cacao. 
	 * Prend en compte les surfaces de plantaion et le prix
	 * @author galem (Gabin)
	 */
	protected HashMap<Feve,Double> quantite() {
		HashMap<Feve,Double> quantite = new HashMap<Feve,Double>();
		HashMap<Feve,Double> plant = plantation();
		for(Feve f1 : plant.keySet()) {
			if (f1.getGamme() == Gamme.BQ) {            // FEVE BASSE QUALITE (NI BIO NI EQUITABLE)
			quantite.put(f1, kg_ha_BQ*plant.get(f1));
			}			
			else if (f1.getGamme() == Gamme.MQ) { 
				if (f1.isEquitable() == false) {				// FEVE MOYENNE QUALITE (NI BIO NI EQUITABLE)
					quantite.put(f1, kg_ha_MQ*plant.get(f1));
					} 
					else {									// FEVE MOYENNE QUALITE (EQUITABLE , NON BIO )
						quantite.put(f1, kg_ha_MQ*plant.get(f1));
					}
				}	
			else if (f1.getGamme() == Gamme.HQ) {      
				if ( f1.isEquitable() == false) {// HAUTE MOYENNE QUALITE (NI BIO NI EQUITABLE))
				quantite.put(f1, kg_ha_HQ*plant.get(f1));  
				}
		}
		return quantite;
		
	}
	
}
