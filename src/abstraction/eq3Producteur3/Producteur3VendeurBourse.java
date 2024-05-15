package abstraction.eq3Producteur3;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eq1Producteur1.Producteur1VendeurBourse;
import abstraction.eq2Producteur2.Producteur2VendeurBourse;
import abstraction.eq4Transformateur1.Transformateur1AcheteurBourse;
import abstraction.eq5Transformateur2.Transformateur2AcheteurBourse;
import abstraction.eq6Transformateur3.Transformateur3AcheteurBourse;
import abstraction.eq7Transformateur4.Transformateur4AcheteurBourse;

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
		double stock = getQuantiteEnStock(f,cryptogramme);
		if ((f.getGamme() == Gamme.BQ)&&(coutRevient(f,stock)<=cours)) {
			//mettre la quantite de stock BQ (on pourra mettre plus et ajuster selon la demande)
			//plus on demande, plus on vend (attention a l'offre et a la demande) (souvent on vend < 5% de ce qu'on veut vendre mais attention on vend plus mais ca fait baisser le cours)
			return quantiteAV(f,cours,stock);
		}
		else {
			if (coutRevient(f,stock)<=cours) {
				return quantiteAV(f,cours,stock);
			}
			else {
				return 0;
			}
		}
	}
	
	/**
	 * @author Arthur
	 * @param Feve f, double cours (un type de feve et son prix a la tonne a la bourse)
	 * return double (quantite a proposer de vendre)
	 * Renvoie la quantite que l'on va proposer a la vente de maniere a vendre nos stocks et pas moins (represente une etude du marche)
	 * La formule mathematique se base sur le fonctionnement du systeme
	 */
	private double quantiteAV(Feve f, double cours, double stock) {
		double autresAV = ((Producteur1VendeurBourse)Filiere.LA_FILIERE.getActeur("EQ1")).offre(f,cours)+((Producteur2VendeurBourse)Filiere.LA_FILIERE.getActeur("EQ2")).offre(f,cours);
		double dem = ((Transformateur1AcheteurBourse)Filiere.LA_FILIERE.getActeur("EQ4")).demande(f,cours)+((Transformateur2AcheteurBourse)Filiere.LA_FILIERE.getActeur("EQ5")).demande(f,cours)+((Transformateur3AcheteurBourse)Filiere.LA_FILIERE.getActeur("EQ6")).demande(f,cours)+((Transformateur4AcheteurBourse)Filiere.LA_FILIERE.getActeur("EQ7")).demande(f,cours);
		this.journal_bourse.ajouter("eq4"+((Transformateur1AcheteurBourse)Filiere.LA_FILIERE.getActeur("EQ4")).demande(f,cours));
		this.journal_bourse.ajouter("eq5"+((Transformateur2AcheteurBourse)Filiere.LA_FILIERE.getActeur("EQ5")).demande(f,cours));
		this.journal_bourse.ajouter("eq6"+((Transformateur3AcheteurBourse)Filiere.LA_FILIERE.getActeur("EQ6")).demande(f,cours));
		this.journal_bourse.ajouter("eq7"+((Transformateur4AcheteurBourse)Filiere.LA_FILIERE.getActeur("EQ7")).demande(f,cours));
		this.journal_bourse.ajouter("dem"+dem);
		if (dem >= autresAV+stock) {
			return stock;
		}
		else if (dem == 0) {
			return 0;
		} else if (stock < dem) {
			return stock/dem*autresAV/(1-stock/dem);
		} else {
			return 9*autresAV;
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
