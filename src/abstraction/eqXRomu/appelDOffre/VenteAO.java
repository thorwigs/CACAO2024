package abstraction.eqXRomu.appelDOffre;

import abstraction.eqXRomu.produits.IProduit;

public class VenteAO {
	private int step;
	private IVendeurAO vendeur;
	private IAcheteurAO acheteur;
	private IProduit produit;
	private double quantiteT;
	private double prix;
	
	public VenteAO(int step, IVendeurAO vendeur, IAcheteurAO acheteur, IProduit produit, double quantiteT, double prix) {
		super();
		this.step=step;
		this.vendeur = vendeur;
		this.acheteur = acheteur;
		this.produit = produit;
		this.quantiteT = quantiteT;
		this.prix = prix;
	}

	public int getStep() {
		return step;
	}
	
	public IVendeurAO getVendeur() {
		return vendeur;
	}

	public IAcheteurAO getAcheteur() {
		return acheteur;
	}

	public IProduit getProduit() {
		return produit;
	}

	public double getQuantiteT() {
		return quantiteT;
	}

	public double getPrix() {
		return prix;
	}

	public String toCSV() {
		return this.getStep()+";"+this.getVendeur()+";"+this.getAcheteur()+";"+this.getProduit()+";"+this.getQuantiteT()+";"+this.getPrix()+";";
	}
}
