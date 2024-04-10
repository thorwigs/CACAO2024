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
		double stock_hg_be = this.stockFeves.get(abstraction.eqXRomu.produits.Feve.F_HQ_BE);
		if (stock_hg_be > 0) {
			if (stock_hg_be > 500) {
				this.stockFeves.replace(abstraction.eqXRomu.produits.Feve.F_HQ_BE, stock_hg_be - 500);
				//on retire 500 du stock de haut de gamme pour faire du chocolat
				double qtechocoproduit = 500*this.pourcentageTransfo.get(Feve.F_HQ_BE).get(Chocolat.C_HQ_BE);
				
				
			} else {
				this.stockFeves.replace(abstraction.eqXRomu.produits.Feve.F_HQ_BE, 0.0);
				//on retire tout notre stock de haut de gamme pour faire du chocolat
				
				
			}
		}
		
		
		//jsp si c'est là qu'il faut l'ajouter...
		
	}

}
