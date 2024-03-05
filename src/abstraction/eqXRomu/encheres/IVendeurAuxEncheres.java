package abstraction.eqXRomu.encheres;

import java.util.List;

import abstraction.eqXRomu.filiere.IActeur;

public interface IVendeurAuxEncheres extends IActeur {
	
	/**
	 * @param propositions une liste non vide de propositions de prix pour une offre de vente emise par this
	 * @return retourne la proposition choisie parmi celles de propositions 
	 * (retourne null si aucune des propositions de propositions ne satisfait le vendeur this)
	 */
	public Enchere choisir(List<Enchere> propositions);

}
