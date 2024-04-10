package abstraction.eq7Transformateur4;

import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.Feve;

public class Transformation extends Transformateur4VendeurAuxEncheres{
	
	//objectifs : vérifier le stocks de fève : s'il est suffisant pour une fève, produire du chocolat avec, puis attribuer aux chocolat une marque ou nom.
	
	public Transformation() {
		super();
	}
	
	public void next() {
		super.next();
		double stock_hg_be = this.stockFeves.get(Feve.F_HQ_BE);
		double peutproduireemploye = this.tauxproductionemploye*this.nbemployeCDI; //pour l'instant ça c'est 375, mais ça pourra évoluer si on change le nb d'employé
		//là faudras s'adapter, pour utiliser qu'une partie de la main d'oeuvre pour faire tel ou tel chocolat, pour l'instant on fait qu'un seul chocolat
		if (stock_hg_be > 0) {
			if (stock_hg_be > peutproduireemploye) {
				//on a assez en stock, on produit un maximum
				this.stockFeves.replace(Feve.F_HQ_BE, stock_hg_be - peutproduireemploye);//on retire peutproduireemploye du stock de feve haut de gamme pour faire du chocolat
				double qtechocoproduit = peutproduireemploye*this.pourcentageTransfo.get(Feve.F_HQ_BE).get(Chocolat.C_HQ_BE); //la qte de choco produit à partir de peutproduireemploye
				double payermachine = peutproduireemploye*this.coutmachine; //prix des machines car on transforme une certaine qté de fèves
				double payeradjuvant = this.coutadjuvant*8;
			} else {
				this.stockFeves.replace(Feve.F_HQ_BE, 0.0);
				//on retire tout notre stock de haut de gamme pour faire du chocolat
				
			}
				
		
		} //else : on fait rien car on peut pas produire
		
		//là on paye les trucs généraux : cout fixe des machines, employés qu'on doit payer dans tous les cas, etc...
		
		
		
	}

}
