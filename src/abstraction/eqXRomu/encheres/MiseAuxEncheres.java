package abstraction.eqXRomu.encheres;

import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.IProduit;


public class MiseAuxEncheres {
	public static final double QUANTITE_MIN = 5.0; // pas moins de 5 tonnes 
	
	private IVendeurAuxEncheres vendeur;
	private IProduit produit;
	private double quantiteT;
	
	/**
	 * Constructeur initialisant la mise aux encheres avec les informations fournies en parametres 
	 * (leve une IllegalArgumentException si les conditions sur les parametres ne sont
	 * pas respectees). 
	 * @param produit!=null
	 * @param quantite>=QUANTITE_MIN
	 */
	public MiseAuxEncheres(IVendeurAuxEncheres vendeur, IProduit produit, double quantiteT) {
		if (vendeur==null) {
			throw new IllegalArgumentException("Appel du constructeur de MiseAuxEncheres avec null pour vendeur");
		}
		if (produit==null) {
			throw new IllegalArgumentException("Appel du constructeur de MiseAuxEncheres avec null pour produit");
		}
		if (quantiteT<QUANTITE_MIN) {
			throw new IllegalArgumentException("Appel du constructeur de MiseAuxEncheres avec une quantite de "+quantiteT+" (min="+QUANTITE_MIN+")");
		}
		this.vendeur = vendeur;
		this.produit = produit;
		this.quantiteT = quantiteT;
	}
	
	public IVendeurAuxEncheres getVendeur() {
		return this.vendeur;
	}
	
	public IProduit getProduit() {
		return this.produit;
	}
	
	public double getQuantiteT() {
		return this.quantiteT;
	}
		
	public boolean equals(Object o) {
		return (o instanceof MiseAuxEncheres) 
				&& this.getVendeur().equals(((MiseAuxEncheres)o).getVendeur())
				&& this.getProduit().equals(((MiseAuxEncheres)o).getProduit())
				&& this.getQuantiteT()==((MiseAuxEncheres)o).getQuantiteT();
	}
	
	public String toString() {
		return "("+Journal.doubleSur(this.getQuantiteT(), 3)+" de "+this.getProduit()+")";
	}
}
