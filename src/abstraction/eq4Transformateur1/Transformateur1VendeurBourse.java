package abstraction.eq4Transformateur1;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur1VendeurBourse extends Transformateur1AcheteurCCadre implements IVendeurBourse{
	private Journal journalBourse;
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