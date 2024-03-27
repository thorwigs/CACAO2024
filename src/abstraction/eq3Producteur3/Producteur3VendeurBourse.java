package abstraction.eq3Producteur3;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Producteur3VendeurBourse implements IVendeurBourse {
	
	
	@Override
	public double offre(Feve f, double cours) {
		//verifier si cours>couts sinon pas de ventes
		//vendre par bourse ce qui n'est pas vendue par contrat cadre
		//vendre toute la production BQ en bourse
		
		if (f.getGamme() != Gamme.BQ) {
			//pas de vente en bourse MQ et HQ pour le moment
			return 0;
		}
		else {
			//mettre la quantite de stock BQ
			return 0;
		}
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
