package abstraction.eq9Distributeur2;

import abstraction.eqXRomu.filiere.Filiere;

public class Distributeur2 extends Distributeur2MarqueDistributeur  {
	private double frais;
	
	public Distributeur2() {
		super();
	}
	
	
	public void next() {
		super.next();
		System.out.println(this.totalStocksChocoMarque.getValeur(Filiere.LA_FILIERE.getEtape(),cryptogramme));
		frais = this.totalStocksChocoMarque.getValeur(Filiere.LA_FILIERE.getEtape(),cryptogramme)*this.getCoutStockage();   // ajout des frais de stockage par maxime
		if (frais>0) {
			Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, getDescription(), frais);}
			this.getJournaux().get(0).ajouter("Ce coup si, nous avons payé : "+frais+" en coût de stockage : ");
		}
}
