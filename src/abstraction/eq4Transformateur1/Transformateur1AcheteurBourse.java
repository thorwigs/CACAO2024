package abstraction.eq4Transformateur1;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur1AcheteurBourse extends Transformateur1Acteur implements IAcheteurBourse {

	@Override
	public double demande(Feve f, double cours) {
		if (this.stockFeves.get(f)<20000) {
			return Math.max(20000-this.stockFeves.get(f),  10); // on n'achete jamais moins de 10T
		}
		return 0;
	}

	@Override
	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
		
	}

}
