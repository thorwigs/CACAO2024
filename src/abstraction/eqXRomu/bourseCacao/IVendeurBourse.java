package abstraction.eqXRomu.bourseCacao;

import abstraction.eqXRomu.produits.Feve;

public interface IVendeurBourse {

	/**
	 * Retourne la quantite en tonnes de feves de type f que le vendeur 
	 * souhaite vendre a cette etape sachant que le cours actuel de 
	 * la feve f est cours
	 * @param f le type de feve
	 * @param cours le cours actuel des feves de type f
	 * @return la quantite en tonnes de feves de type f que this souhaite vendre 
	 */
	public double offre(Feve f, double cours);

	/**
	 * Methode appelee par la bourse pour avertir le vendeur qu'il est parvenu
	 * a vendre quantiteEnT tonnes de feve f au prix de coursEnEuroParT euros par tonne.
	 * L'acteur this doit determiner la quantite qu'il livre reellement et destocker cette
	 * quantite. 
	 * La quantite quantiteEnT est inferieure ou egale a ce que le vendeur this a specifie
	 * vouloir vendre, et il doit donc normalement etre en mesure de retirer cette 
	 * quantite de ses sotcks afin de la livrer et de retourner quantiteEnT. 
	 * Mais il se peut qu'il retourne une quantite inferieure si ses stocks de feve f ne 
	 * sont pas suffisants pour livrer quantiteEnT tonnes.
	 * Remarque : le superviseur s'occupe des virements, vendeurs et acheteurs n'ont pas a
	 *  les gerer
	 * @return la quantite en tonnes de feves de type f rellement livree (retiree du stock) 
	 */
	public double notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT);

	/**
	 * Methode appelee par la bourse pour avertir le vendeur qu'il vient 
	 * d'etre ajoute a la black list : l'acteur a precise une quantite qu'il desirait 
	 * mettre en vente qu'il n'a pas pu honorer (il n'a pu livrer qu'une quantite insuffisante)
	 * this ne pourra pas vendre en bourse pendant la duree precisee en 
	 * parametre 
	 */
	public void notificationBlackList(int dureeEnStep);

}
