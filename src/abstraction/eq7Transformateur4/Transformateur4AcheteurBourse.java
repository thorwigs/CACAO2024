package abstraction.eq7Transformateur4;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.produits.Feve;


public class Transformateur4AcheteurBourse extends Transformateur4Acteur implements IAcheteurBourse{

	public double demande(Feve f, double cours) {
		return 20;
	}
	
	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
	}

	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
	}
	
	public void next() {
		super.next();
	}

	

}
