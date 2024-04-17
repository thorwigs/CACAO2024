package abstraction.eq6Transformateur3;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur3AcheteurBourse extends Transformateur3VendeurCCadre implements IAcheteurBourse {

	@Override
	public double demande(Feve f, double cours) {
		if (this.stockFeves.get(f)<30000) {
			return this.stockFeves.get(f); 
		}
		
		return 0;
	}

	@Override
	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {

	}

}
