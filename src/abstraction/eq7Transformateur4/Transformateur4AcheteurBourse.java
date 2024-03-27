package abstraction.eq7Transformateur4;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.produits.Feve;


public class Transformateur4AcheteurBourse extends Transformateur4Acteur implements IAcheteurBourse{

	@Override
	public double demande(Feve f, double cours) {
		// TODO Auto-generated method stub
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
