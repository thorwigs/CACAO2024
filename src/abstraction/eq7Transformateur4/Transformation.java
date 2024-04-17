package abstraction.eq7Transformateur4;

import java.util.HashMap;
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
	protected List<Double> lesqtproduite; //liste qui contiendra toutes nos qtés de chocolats produits pour ce step
	

	public Transformation() {
		super();
		this.journalTransfo = new Journal(this.getNom()+" journal transfo", this);
			}
	
	public void next() {
		super.next();
		double critere = 2000000.0;
		this.lescouts = new LinkedList<Double>();//on initialise avec une liste vide (supprime les données du step precedent)
		this.lesqtproduite = new LinkedList<Double>(); //on les reset à 0
		this.coutproduction_tonne_marque_step = new HashMap<ChocolatDeMarque,Double>(); //on les reset à 0
		this.coutproduction_tonne_step = new HashMap<Chocolat,Double>(); //on les reset à 0
		double peutproduireemploye = this.tauxproductionemploye*this.nbemployeCDI; //ce qu'on peut produire avec nos employés
		
		//Il faut fixer le critère!!!!!
		
		for (ChocolatDeMarque c : chocolatCocOasis) {
			//ça fait la boucle pour tout les chocos de marques
			//on produit deux chocos de marque, l'un HQ_BE, l'autre non. Donc on check si c est bio et equitable, si oui : fève BE /else : feve pas BE
			if (c.getChocolat().isBio() && c.getChocolat().isEquitable()) {
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
					this.lesqtproduite.add(qtechocoproduit);
					this.coutproduction_tonne_marque_step.put(c, (payermachine+payeradjuvant)/qtechocoproduit);
					//on ne va pas prendre en compte le prix des fèves utilisé dans cette transfo, il faudra une marge qui en tient compte
					//on va tenir compte des cout fixe après à la fin des boucle for
					
				}//else : on a assez de ce choco de marque, on en produit pas
			} else {
				//nécessairement c'est pas du BE
				double qtutile2 = 0; //correspond à la qte de fève qu'on va effectivement transformer
				double stock_hg = this.stockFeves.get(Feve.F_HQ);
				if (this.stockChocoMarque.get(c) < critere) {

					if (stock_hg > 0) {
						if (stock_hg > peutproduireemploye) {
						//on a assez en stock, on produit un maximum
						qtutile2 = peutproduireemploye;
						} else {
						//on retire tout notre stock de feve pour faire du chocolat
						qtutile2 = stock_hg;
						}
					} //else : on fait rien car on peut pas produire
					this.stockFeves.replace(Feve.F_HQ, stock_hg - qtutile2);//on retire qtutile1 du stock de feve haut de gamme pour faire du chocolat
					double qtechocoproduit = qtutile2*this.pourcentageTransfo.get(Feve.F_HQ).get(Chocolat.C_HQ); //la qte de choco produit à partir de qtutile1
					this.stockChocoMarque.replace(c, this.stockChocoMarque.get(c) + qtechocoproduit);
					this.totalStocksChocoMarque.ajouter(this, qtechocoproduit, cryptogramme);
					double payermachine = qtutile2*this.coutmachine; //prix des machines car on transforme une certaine qté de fèves
					double pourcentageadjuvant = this.pourcentageTransfo.get(Feve.F_HQ).get(Chocolat.C_HQ)-1;
					double payeradjuvant = this.coutadjuvant*pourcentageadjuvant*qtutile2;
					this.lescouts.add(payermachine);
					this.lescouts.add(payeradjuvant);
					this.lesqtproduite.add(qtechocoproduit);
					this.coutproduction_tonne_marque_step.put(c, (payermachine+payeradjuvant)/qtechocoproduit);
					//idem on ne va pas prendre en compte le prix des fèves utilisé dans cette transfo, il faudra une marge qui en tient compte
					//on va tenir compte des cout fixe après à la fin des boucle for
					
				}//else : on a assez de ce choco de marque, on en produit pas
			}
		}
		//refaire des boucles for pour les chocos qui n'ont pas de marques
		
		//là on paye

		double qtetotaleproduite = 0.0;
		for (double k : lesqtproduite) {
			qtetotaleproduite = qtetotaleproduite + k;
		}
		double a_payer = 1000*this.nbemployeCDI + 658; //cout des employes et cout fixe des machines par step 
		double cout_fixe_par_tonne = a_payer/qtetotaleproduite; //cout fixe d'1 tonne indépendamment de la qualité de la tonne
		for (ChocolatDeMarque c : chocolatCocOasis) {
			if (c.getChocolat().isBio() && c.getChocolat().isEquitable()) {
				this.coutproduction_tonne_marque_step.replace(c, this.coutproduction_tonne_marque_step.get(c) + cout_fixe_par_tonne);
			} else {
				//il s'agit du deuxième choco de marque
				this.coutproduction_tonne_marque_step.replace(c, this.coutproduction_tonne_marque_step.get(c) + cout_fixe_par_tonne);
			}
		}
		

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
