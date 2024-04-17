package abstraction.eq7Transformateur4;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

public class Transformation extends Transformateur4VendeurAuxEncheres{
	
	//objectifs : vérifier le stocks de fève : s'il est suffisant pour une fève, produire du chocolat avec, puis attribuer aux chocolat une marque ou nom.
	protected Journal journalTransfo ;
	protected List<Double> lescouts; //liste qui contiendra certains des couts à faire pour un step, initialiser à une liste vide à chaque début de l'appel next
	

	public Transformation() {
		super();
		this.journalTransfo = new Journal(this.getNom()+" journal transfo", this);
			}
	
	public void next() {
		super.next();
		double critere = 2000000.0;
		this.lescouts = new LinkedList<Double>();//on initialise avec une liste vide (supprime les données du step precedent)
		double peutproduireemploye = this.tauxproductionemploye*this.nbemployeCDI; //pour l'instant ça c'est 375, mais ça pourra évoluer si on change le nb d'employé
		//il faudras s'adapter, pour utiliser qu'une partie de la main d'oeuvre pour faire tel ou tel chocolat, pour l'instant on fait qu'un seul chocolat
		for (ChocolatDeMarque c : chocolatCocOasis) {
			
			//ATTENTION il faut ici une vérification de la gamme du chocolat pour savoir quelle type de fève on va utiliser
			
			double qtutile1 = 0; //correspond à la qte de fève qu'on va effectivement transformer
			double stock_hg_be = this.stockFeves.get(Feve.F_HQ_BE);
			if (this.stockChocoMarque.get(c) < critere) {

				if (stock_hg_be > 0) {
					if (stock_hg_be > peutproduireemploye) {
					//on a assez en stock, on produit un maximum
					qtutile1 = peutproduireemploye;
					} else {
					//on retire tout notre stock de feve pour faire du chocolat
					qtutile1 = stock_hg_be;
					}
				} //else : on fait rien car on peut pas produire
				this.stockFeves.replace(Feve.F_HQ_BE, stock_hg_be - qtutile1);//on retire qtutile1 du stock de feve haut de gamme pour faire du chocolat
				double qtechocoproduit = qtutile1*this.pourcentageTransfo.get(Feve.F_HQ_BE).get(Chocolat.C_HQ_BE); //la qte de choco produit à partir de qtutile1
				this.stockChocoMarque.replace(c, this.stockChocoMarque.get(c) + qtechocoproduit);
				this.totalStocksChocoMarque.ajouter(this, qtechocoproduit, cryptogramme);
				double payermachine = qtutile1*this.coutmachine; //prix des machines car on transforme une certaine qté de fèves
				double pourcentageadjuvant = this.pourcentageTransfo.get(Feve.F_HQ_BE).get(Chocolat.C_HQ_BE)-1;
				double payeradjuvant = this.coutadjuvant*pourcentageadjuvant*qtutile1;
				this.lescouts.add(payermachine);
				this.lescouts.add(payeradjuvant);
			}//else : on a assez de ce choco de marque, on en produit pas
			//ça fait la boucle pour tout les chocos de marques
		}
		//refaire des boucles for pour les chocos qui n'ont pas de marques
		
		//là on paye
		double a_payer = 1000*this.nbemployeCDI + 658; //cout des employes et cout fixe des machines par step
		for (double i : lescouts) {
			a_payer = a_payer + i;
		}
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "CoûtTransformation", a_payer); //on paye tout d'un coup
		//TEST :
		for (ChocolatDeMarque c : chocolatCocOasis) {
			this.journalTransfo.ajouter("stock de " + c + " est "+ this.stockChocoMarque.get(c));
		}

	}
	
		public List<Journal> getJournaux() {
			List<Journal> res=super.getJournaux();
			res.add(journalTransfo);
			return res;
		}
}
