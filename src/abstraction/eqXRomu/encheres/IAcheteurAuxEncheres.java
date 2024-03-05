package abstraction.eqXRomu.encheres;

import abstraction.eqXRomu.filiere.IActeur;

public interface IAcheteurAuxEncheres extends IActeur {
	
	/**
	 * @param miseAuxEncheres miseAuxEncheres!=null
	 * @return Retourne une proposition de prix a la tonne pour la mise aux encheres indiquee en parametre
	 * (retourne 0.0 si l'acheteur n'est pas interesse par cette offre).
	 */
	public double proposerPrix(MiseAuxEncheres miseAuxEncheres);
	
	/**
	 * Methode appelee lorsque la proposition de prix de l'acheteur a ete retenue 
	 * par le vendeur. La transaction d'argent a deja ete effectuee mais il reste 
	 * a l'acheteur a mettre a jour notamment ses stocks pour tenir compte de 
	 * l'achat qu'il vient de faire ( enchereRetenue.getMiseAuxEncheres() ).
	 * @param enchereRetenue l'enchere qu'a fait l'acheteur this et qui
	 *  vient d'etre retenue par le vendeur.
	 */
	public void notifierAchatAuxEncheres(Enchere enchereRetenue);

	/**
	 * Methode appelee lorsque la proposition de prix de l'acheteur n'a pas ete retenue.
	 * @param enchereNonRetenue Une enchere faite par l'acheteur this
	 * qui n'a pas ete retenue par le vendeur.
	 */
	public void notifierEnchereNonRetenue(Enchere enchereNonRetenue);

}
