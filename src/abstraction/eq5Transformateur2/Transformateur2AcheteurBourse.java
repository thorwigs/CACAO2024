package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur2AcheteurBourse extends Transformateur2Acteur implements IAcheteurBourse {

	public double demande(Feve f, double cours) {
		return 15;
	}

	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
	}

	public void notificationBlackList(int dureeEnStep) {
	}
	

}
