package abstraction.eq9Distributeur2;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;


public abstract class Distributeur2Vente extends Distributeur2Stocks implements IDistributeurChocolatDeMarque {

	@Override
	public double prix(ChocolatDeMarque choco) {
		// TODO Auto-generated method stub
		if( Filiere.LA_FILIERE.getEtape() < 1) {
		return 20000;}
		return Filiere.LA_FILIERE.prixMoyen(choco,Filiere.LA_FILIERE.getEtape()-1)*0.95;
	}

	@Override
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		
		return this.getQuantiteEnStock(choco,crypto)*0.9;
		
	}

	@Override
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		return this.quantiteEnVente(choco, crypto)*ClientFinal.POURCENTAGE_MAX_EN_TG;
	}

	@Override
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		// TODO Auto-generated method stub
		if (this.stockChocoMarque!=null && this.stockChocoMarque.keySet().contains(choco)) {
		this.stockChocoMarque.put(choco, this.stockChocoMarque.get(choco)-quantite);
		this.totalStocksChocoMarque.retirer(this,  quantite, cryptogramme);
		}

		
	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
	if (this.getQuantiteEnStock(choco, crypto)==0.0) {
		journal.ajouter("plus de chocolat"+choco+"");
	}
		
	}

}
