package abstraction.eq9Distributeur2;

import abstraction.eqXRomu.filiere.Filiere;

public class Distributeur2 extends Distributeur2MarqueDistributeur  {
	private double frais;
	
	public Distributeur2() {
		super();
	}
	
	
	public void next() {
		super.next();
		//System.out.println(this.totalStocksChocoMarque.getValeur(Filiere.LA_FILIERE.getEtape(),cryptogramme));
		//System.out.println(this.getTotalStock(cryptogramme));
		frais = this.getTotalStock(cryptogramme)*this.getCoutStockage();   // ajout des frais de stockage par maxime
		if (frais>0) {																										//On paye des frais énormes car la quantité du stock l'est aussi
			Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme,"frais de stockage", frais);}
			this.getJournaux().get(0).ajouter("Ce coup si, nous avons payé : "+frais+" en coût de stockage : ");
		}
}
