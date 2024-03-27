package abstraction.eq1Producteur1;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.produits.Feve;

public class Producteur1VendeurBourse extends Producteur1Acteur implements  IVendeurBourse {

	@Override
	public double offre(Feve f, double cours) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
		
	}

}
