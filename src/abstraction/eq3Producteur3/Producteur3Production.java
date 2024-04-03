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
	
	protected HashMap<Feve,Double> quantite(Feve f, double p) {
		HashMap<Feve,Double> quantite = new HashMap<Feve,Double>();
		HashMap<Feve,Double> plant = plantation();
		for(Feve f1 : plant.keySet()) {
			if (f1.getGamme() == Gamme.BQ) {
			quantite.put(f1, kg_ha_BQ*plant.get(f1));
			}			
			else if (f1.getGamme() == Gamme.MQ) {
				quantite.put(f1, kg_ha_MQ*plant.get(f1));
				}	
			else if (f1.getGamme() == Gamme.HQ) {
				quantite.put(f1, kg_ha_HQ*plant.get(f1));
				}
		}
		return quantite;
		
	}
	
}
