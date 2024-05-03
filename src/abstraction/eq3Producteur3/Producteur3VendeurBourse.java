package abstraction.eq3Producteur3;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Producteur3VendeurBourse extends Producteur3Production implements IVendeurBourse {
	
	
	/**
	 * @author Arthur
	 * @param Feve f, double cours (un type de feve et son prix a la tonne a la bourse)
	 * @return double (quantité de feve f que l'on est pret a vendre a la bourse
	 * Renvoie selon la feve et son cours, la quantité que l'on est pret a vendre (potentiellement 0)
	 */
	public double offre(Feve f, double cours) {
		//vendre par bourse ce qui n'est pas vendue par contrat cadre (a faire)
		//vend toute la production BQ en bourse
		//verifie si cours>couts sinon pas de ventes (a voir si sur le point de perimer si on garde ca)
		if ((f.getGamme() == Gamme.BQ)&&(coutRevient(f,getQuantiteEnStock(f,cryptogramme))<=cours)) {
			//mettre la quantite de stock BQ (on pourra mettre plus et ajuster selon la demande)
			//plus on demande, plus on vend (attention a l'offre et a la demande) (souvent on vend < 5% de ce qu'on veut vendre mais attention on vend plus mais ca fait baisser le cours)
			return this.getQuantiteEnStock(f,this.cryptogramme);
		}
		else {
			if ((getQuantiteEnStock(f,cryptogramme) - ventefeve.get(f).getValeur() > 10)&&(coutRevient(f,getQuantiteEnStock(f,cryptogramme))<=cours)) {
				return this.getQuantiteEnStock(f, cryptogramme);
			}
			else {
				return 0;
			}
		}
	}

	/**
	 * @author Arthur
	 * @param Feve f, double quantiteEnT, double coursEnEuroParT (données de la vente)
	 * @return double (quantité effectivement envoyé)
	 * On nous informe de ce que l'on peut vendre et on renvoie ce qu'on peut effectivement envoyer
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
			this.journal_bourse.ajouter("Bourse: Vente de "+stock_inst+" T de feves "+f.getGamme()+ "pour "+coursEnEuroParT*stock_inst+" E");
			ventefevebourse.put(f, stock_inst);
			return stock_inst;
		}
	}

	/**
	 * @author Arthur
	 * @param int dureeEnStep (durée du ban)
	 * Si on n'honore pas nos promesses, on ne peut plus vendre et on l'écrit dans le journal
	 */
	public void notificationBlackList(int dureeEnStep) {
		this.journal_bourse.ajouter("Le producteur 3 a ete blacklist de la bourse pour "+dureeEnStep);
	}
}
