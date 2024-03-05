package abstraction.eqXRomu.produits;

public interface IProduit {
	public String getType();
	
	/**
	 * @param f
	 * @return Retourne true si p est substituable par this (this est un produit 
	 * equivalent a celui de p et de qualite au moins egale et on peut donc utiliser
	 * this a la place de p)
	 * En particulier, un vendeur tenu de fournir un produit p peut fournir
	 * un produit p2 si p2.greaterThan(p)
	 */
	public boolean greaterThan(IProduit p);

}
