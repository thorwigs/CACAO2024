package abstraction.eqXRomu.bourseCacao;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

public class ExempleVendeurBourseCacao extends ExempleAbsVendeurBourseCacao implements IVendeurBourse{

	public ExempleVendeurBourseCacao(Feve feve, double stock) {
		super(feve, stock);
	}

	public double offre(Feve f, double cours) {
		if (this.getFeve().equals(f)) {
			BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
			double pourcentage = (bourse.getCours(getFeve()).getValeur()-bourse.getCours(getFeve()).getMin())/(bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getMin());
			return this.stockFeve.getValeur()*pourcentage;
		} else {
			return 0.0;
		}
	}

	public double notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		double livrable = Math.min(quantiteEnT, this.stockFeve.getValeur());
		this.stockFeve.setValeur(this, this.stockFeve.getValeur()-livrable);
		return livrable;
	}


	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter("Aie... blackliste pendant 6 steps");
	}
}
