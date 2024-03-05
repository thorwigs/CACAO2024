package abstraction.eqXRomu.acteurs;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class ProducteurXVendeurBourse extends ProducteurXActeur implements IVendeurBourse{
	public ProducteurXVendeurBourse() {
		super();
	}

	public double offre(Feve f, double cours) {
		if (f.getGamme()==Gamme.MQ) {
			return this.stock.get(f).getValeur()*(Math.min(cours, 5000)/5000.0);
		} else if (f.getGamme()==Gamme.BQ) {
			return this.stock.get(f).getValeur()*(Math.min(cours, 3000)/3000.0);
		} else { // normalement impossible vu que le HQ n'est pas en bourse
			return 0.0;
		}
	}

	public double notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		double retire = Math.min(this.stock.get(f).getValeur(), quantiteEnT);
		this.stock.get(f).retirer(this, retire, cryptogramme);
		return retire;
	}

	public void notificationBlackList(int dureeEnStep) {
	}

}
