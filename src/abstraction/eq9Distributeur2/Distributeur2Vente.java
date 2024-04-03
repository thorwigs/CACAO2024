package abstraction.eq9Distributeur2;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;


public class Distributeur2Vente extends Distributeur2Stocks implements IDistributeurChocolatDeMarque {

	@Override
	public double prix(ChocolatDeMarque choco) {
		// TODO Auto-generated method stub
		return 20000;
	}

	@Override
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		return 10000;//this.getQuantiteEnStock(choco, crypto);
	}

	@Override
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		return this.quantiteEnVente(choco, crypto)/10;
	}

	@Override
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		// TODO Auto-generated method stub
		if (this.stockChocoMarque!=null && this.stockChocoMarque.keySet().contains(choco)) {
		this.stockChocoMarque.put(choco, this.stockChocoMarque.get(choco)-quantite);
		this.totalStocksChocoMarque.retirer(this,  quantite, cryptogramme);
		}
		System.out.println("vente");
		
	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		
	}

}
