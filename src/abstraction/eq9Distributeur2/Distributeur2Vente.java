package abstraction.eq9Distributeur2;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;


public class Distributeur2Vente extends Distributeur2Stocks implements IDistributeurChocolatDeMarque {

	@Override
	public double prix(ChocolatDeMarque choco) {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		return 10;//this.getQuantiteEnStock(choco, crypto);
	}

	@Override
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		return this.quantiteEnVente(choco, crypto)/100;
	}

	@Override
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		// TODO Auto-generated method stub
		this.stockChocoMarque.put(choco, this.stockChocoMarque.get(choco)-quantite);
		this.totalStocksChocoMarque.retirer(this,  quantite, cryptogramme);
		System.out.println("vente");
		
	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		
	}

}
