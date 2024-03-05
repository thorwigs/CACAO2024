package abstraction.eqXRomu.appelDOffre;

import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.IProduit;


public class AppelDOffre {
	public static final double AO_QUANTITE_MIN = 5.0; // pas moins de 5 tonnes 
	
	private IAcheteurAO acheteur;
	private IProduit produit;
	private double quantiteT;
	
	/**
	 * Constructeur initialisant l'offre d'achat avec les informations fournies en parametres 
	 * (leve une IllegalArgumentException si les conditions sur les parametres ne sont
	 * pas respectees). 
	 * @param feve!=null
	 * @param quantite>=AO_FEVES_QUANTITE_MIN
	 */
	public AppelDOffre(IAcheteurAO acheteur, IProduit produit, double quantiteT) {
		if (acheteur==null) {
			throw new IllegalArgumentException("Appel du constructeur de OffreAchat avec null pour vendeur");
		}
		if (produit==null) {
			throw new IllegalArgumentException("Appel du constructeur de OffreAchat avec null pour type de produit");
		}
		if (quantiteT<AO_QUANTITE_MIN) {
			throw new IllegalArgumentException("Appel du constructeur de OffreAchat avec une quantite de "+quantiteT+" (min="+AO_QUANTITE_MIN+")");
		}
		this.acheteur = acheteur;
		this.produit = produit;
		this.quantiteT = quantiteT;
	}
	
	public IAcheteurAO getAcheteur() {
		return this.acheteur;
	}
	
	public IProduit getProduit() {
		return this.produit;
	}
	
	public double getQuantiteT() {
		return this.quantiteT;
	}
	
	public boolean equals(Object o) {
		return (o instanceof AppelDOffre) 
				&& this.getAcheteur().equals(((AppelDOffre)o).getAcheteur())
				&& this.getProduit().equals(((AppelDOffre)o).getProduit())
				&& this.getQuantiteT()==((AppelDOffre)o).getQuantiteT();
	}
	
	public String toString() {
		return "("+getAcheteur()+" veut acheter "+Journal.doubleSur(this.getQuantiteT(), 3)+" de "+this.getProduit()+")";
	}
}
