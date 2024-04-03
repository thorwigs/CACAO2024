package abstraction.eq8Distributeur1;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Distributeur1Vendeur extends Distributeur1Acteur implements IDistributeurChocolatDeMarque {
	private double capaciteDeVente;
	private double[] prix;
	private String[] marques;

		
	public double prix(ChocolatDeMarque choco) {
		return 0;
	}


	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		return 0;
	}


	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		return 0;
	}


	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		
	}


	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		
	}
	

}
