package abstraction.eq3Producteur3;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Producteur3VendeurBourse extends Producteur3Production implements IVendeurBourse {
	
	
	/**
	 * @author Arthur
	 */
	public double offre(Feve f, double cours) {
		//vendre par bourse ce qui n'est pas vendue par contrat cadre (a faire)
		//vend toute la production BQ en bourse
		//verifie si cours>couts sinon pas de ventes (a voir si sur le point de perimer si on garde ca)
		this.journal_bourse.ajouter("Q"+getQuantiteEnStock(f,cryptogramme));
		this.journal_bourse.ajouter("cout"+coutRevient(f,getQuantiteEnStock(f,cryptogramme)));
		this.journal_bourse.ajouter("cours"+cours);
		if ((f.getGamme() == Gamme.BQ)&&(coutRevient(f,getQuantiteEnStock(f,cryptogramme))<=cours)) {
			//mettre la quantite de stock BQ (mettre plus et ajuster selon la demande)
			//plus on demande, plus on vend (attention a l'offre et a la demande)
			return this.getQuantiteEnStock(f,this.cryptogramme);
		}
		else {
			//pas de vente en bourse MQ et HQ pour le moment
			return 0;
		}
	}

	/*
	 * @author Arthur
	 */
	public double notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		double stock_inst = this.getQuantiteEnStock(f, this.cryptogramme);
		if (quantiteEnT <= stock_inst) {
			//on verifie que l'on puisse fournir la quantite demande
			//il faut modifier les stocks suite a la vente
			this.setQuantiteEnStock(f, stock_inst-quantiteEnT);
			this.journal_bourse.ajouter("Bourse: Vente de "+quantiteEnT+" T de feves "+f.getGamme()+" pour "+coursEnEuroParT*quantiteEnT+" E");
			ventefevebourse.put(f, quantiteEnT);
			//on envoie ce que l'on a promis
			return quantiteEnT;
		} else {
			//on ne peut pas tout fournir, on envoie tout le stock
			this.setQuantiteEnStock(f, 0);
			this.journal_bourse.ajouter("Bourse: Vente de "+stock_inst+" T de feves "+f.getGamme()+" pour "+coursEnEuroParT*stock_inst+" E");
			ventefevebourse.put(f, stock_inst);
			return stock_inst;
		}
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {
		this.journal_bourse.ajouter("Le producteur 3 a ete blacklist de la bourse pour "+dureeEnStep);
	}
}
