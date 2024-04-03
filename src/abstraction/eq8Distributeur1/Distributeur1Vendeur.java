package abstraction.eq8Distributeur1;


import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;

public class Distributeur1Vendeur extends Distributeur1Acteur implements IDistributeurChocolatDeMarque {
	private double capaciteDeVente;
	private double[] prix;
	private String[] marques;
	
	public Distributeur1Vendeur(double capaciteDeVente, double[] prix, String[]marques) {
		this.capaciteDeVente = capaciteDeVente;
		this.prix = prix;
		this.marques = marques;
	}

	public double prix(ChocolatDeMarque choco) {
		int pos= (chocolats.indexOf(choco));
		if (pos<0) {
			return 0.0;
		} else {
			return prix[pos];
		}
	}

	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		if (crypto!=this.cryptogramme) {
			journal.ajouter("Quelqu'un essaye de me pirater !");
			return 0.0;
		} else {
			int pos= (chocolats.indexOf(choco));
			if (pos<0) {
				return 0.0;
			} else {
				return Math.min(capaciteDeVente, this.getQuantiteEnStock(choco,crypto));
			}
		}
	}

	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		double capaciteTG = 0.1 * this.capaciteDeVente;
		if (choco.getMarque()== "Chocoflow") {
			return 0.06 * capaciteTG;
		}
		else {
			if(choco.getChocolat().isEquitable()) {
				if(choco.getChocolat().getGamme()==Gamme.MQ) {
					return 0.01 * capaciteTG;
				}
				if(choco.getChocolat().getGamme()==Gamme.HQ) {
					return 0.01 * capaciteTG;
				}
			}
		}
		return 0;

	}
	
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		
	}

	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		journal.ajouter(" Aie... j'aurais du mettre davantage de "+choco.getNom()+" en vente");
	}
	


}
