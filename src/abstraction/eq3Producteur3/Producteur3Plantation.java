package abstraction.eq3Producteur3;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Feve;

public class Producteur3Plantation extends Producteur3Acteur {

	
	protected HashMap<Feve,Double> maindoeuvre(HashMap<Feve,Double> surfaces) {
		//renvoie le nombre d'ouvriers necessaires et le type de feve selon la superficie (en ha) et le type de plantation
		//HashMap<Feve,Double> surfaces = plantation();
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
