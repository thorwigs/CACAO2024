package abstraction.eqXRomu.appelDOffre;

import abstraction.eqXRomu.filiere.IActeur;

public interface IVendeurAO extends IActeur {

	/**
	 * 
	 * @param offre
	 * @return Retourne une offre de vente dont le produit correspond au produit de offre,
	 *         vendeur correspond a this, et le prix est determine par le IVendeurAO this
	 *         (retourne null si le IVendeurAO ne peut pas ou ne veut pas vendre un tel lot)
	 */
	public OffreVente proposerVente(AppelDOffre offre);
	
	/**
	 * Methode appelee lorsque la proposition de prix du vendeur a ete retenue 
	 * par l'acheteur. La transaction d'argent a deja ete effectuee mais il reste 
	 * au vendeur a mettre a jour ses stock pour tenir compte de l'achat qu'il
	 * vient de faire ( propositionRetenue.getOffre() ).
	 * @param propositionRetenue la proposition qu'a fait l'acheteur this et qui
	 *  vient d'etre retenue par le vendeur.
	 */
	public void notifierVenteAO(OffreVente propositionRetenue);

	/**
	 * Methode appelee pour avertir le vendeur que sa proposition de vente n'a pas ete retenue 
	 * @param propositionRefusee, la proposition qui avait ete faite mais qui n'a pas ete retenue
	 */
	public void notifierPropositionNonRetenueAO(OffreVente propositionRefusee);
}
