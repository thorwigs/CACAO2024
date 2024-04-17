package abstraction.eq2Producteur2;
import java.util.ArrayList;
import java.util.List;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

public abstract class Producteur2_Stocks extends Producteur2Acteur {
	
	//seuil max de la production stockee
	private static final double SEUIL = 400000;
	
	//délais avant de passer à une qualité inférieure
	private static final double DELAI_HQ_MQ = 4;
	private static final double DELAI_MQ_BQ = 8;
	private static final double DELAI_BQ_JETE = 12;
		
	private List<Producteur2_Lot> lst_stock_total;
	protected Journal journalStocks;
	
	protected abstract void trop_d_employes();
	
	public Producteur2_Stocks() {
		super();
		this.journalStocks = new Journal(this.getNom()+" journalStocks", this);
		this.lst_stock_total = new ArrayList<Producteur2_Lot>();
		
	}
	
	public void initialiser() {
		super.initialiser();
	}
	
	public static double getSeuil() {
		return SEUIL;
	}
	
	public static double getDelaiHqMq() {
		return DELAI_HQ_MQ;
	}
	
	public static double getDelaiMqBq() {
		return DELAI_MQ_BQ;
	}
	
	public List<Producteur2_Lot> getLst_Stock_total() {
		return this.lst_stock_total;
	}
	
	public void SetLst_Stock_total( List<Producteur2_Lot> lst) {
		this.lst_stock_total = lst;
	}
	
	public void retire_lot(Producteur2_Lot  l) {
		this.lst_stock_total.remove(l);
	}
	
	public List<Journal> getJournaux() {
		List<Journal> jx = super.getJournaux();
		jx.add(journalStocks);
		return jx;
	}
	
	//Faite par Quentin
	//Met à jour la liste des stocks en ajoutant un lot produit
	public void ajout_stock(Feve type_feve, double quantite) {
		
		//Si on dépasse le seuil de stockage
		if (this.getStockTotal(this.cryptogramme)+ quantite > SEUIL && quantite > 0) {
			trop_de_stock(type_feve, quantite);
		}
		else {
			if(quantite != 0 && type_feve == Feve.F_BQ) {
				this.lst_stock_total.add(new Producteur2_Lot(quantite, Feve.F_BQ));
			}
			if(quantite != 0 && type_feve == Feve.F_MQ) {
				this.lst_stock_total.add(new Producteur2_Lot(quantite, Feve.F_MQ));
			}
			if(quantite != 0 && type_feve == Feve.F_MQ_E) {
				this.lst_stock_total.add(new Producteur2_Lot(quantite, Feve.F_MQ_E));
			}
			if(quantite != 0 && type_feve == Feve.F_HQ) {
				this.lst_stock_total.add(new Producteur2_Lot(quantite, Feve.F_HQ));
			}
			if(quantite != 0 && type_feve == Feve.F_HQ_E) {
				this.lst_stock_total.add(new Producteur2_Lot(quantite, Feve.F_HQ_E));
			}
			if(quantite != 0 && type_feve == Feve.F_HQ_BE) {
				this.lst_stock_total.add(new Producteur2_Lot(quantite, Feve.F_HQ_BE));
			}
			this.lot_to_hashmap();
		}
	}
	
	//Faite par Quentin
	//Initialiser les stocks à l'étape 0 de la filière
	public void init_stock(Feve type_feve, double quantite) {
		List<Producteur2_Lot> stocks =  new ArrayList<Producteur2_Lot>();
		if(quantite != 0 && type_feve == Feve.F_BQ) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_BQ,0));
		}
		if(quantite != 0 && type_feve == Feve.F_MQ) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_MQ,0));
		}
		if(quantite != 0 && type_feve == Feve.F_MQ_E) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_MQ_E,0));
		}
		if(quantite != 0 && type_feve == Feve.F_HQ) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_HQ,0));
		}
		if(quantite != 0 && type_feve == Feve.F_HQ_E) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_HQ_E,0));
		}
		if(quantite != 0 && type_feve == Feve.F_HQ_BE) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_HQ_BE,0));
		}
		this.lst_stock_total = stocks;
	}
	
	//Faite par Noémie
	//Fonction qui parcourt l'ensemble des lots et récupère la quantité de fève pour chaque type de fèves
	public void lot_to_hashmap() {
		List<Producteur2_Lot> l = this.getLst_Stock_total();
		double feve_bq = 0;
		double feve_mq = 0;
		double feve_hq = 0;
		double feve_mq_e= 0;
		double feve_hq_e = 0;
		double feve_hq_be = 0;
		
		for (int i = 0; i < getLst_Stock_total().size(); i++) {
			  if (l.get(i).getQuantite() == 0){
				  this.retire_lot(l.get(i));
			  }
			  else if (l.get(i).getType_feve() == Feve.F_BQ) {
		    	  feve_bq += l.get(i).getQuantite();
		      }
		      else if (l.get(i).getType_feve() == Feve.F_MQ) {
		    	  feve_mq += l.get(i).getQuantite();
		      }
		      else if (l.get(i).getType_feve() == Feve.F_HQ) {
		    	  feve_hq += l.get(i).getQuantite();
		      }
		      else if (l.get(i).getType_feve() == Feve.F_MQ_E) {
		    	  feve_mq_e += l.get(i).getQuantite();
		      }
		      else if (l.get(i).getType_feve() == Feve.F_HQ_E) {
		    	  feve_hq_e += l.get(i).getQuantite();
		      }
		      else if (l.get(i).getType_feve() == Feve.F_HQ_BE) {
		    	  feve_hq_be += l.get(i).getQuantite();
		      }
		}
		
		stock.put(Feve.F_BQ, feve_bq);
		stock.put(Feve.F_MQ, feve_mq);
		stock.put(Feve.F_HQ, feve_hq);
		stock.put(Feve.F_MQ_E, feve_mq_e);
		stock.put(Feve.F_HQ_E, feve_hq_e);
		stock.put(Feve.F_HQ_BE, feve_hq_be);
		
	}
	
	//Faite par Quentin
	//Change la qualité des fèves en fonction de la durée de stockage
	public void changement_qualite() {
		for(Producteur2_Lot lot : this.lst_stock_total) {
			if((lot.getType_feve() == Feve.F_HQ_E || lot.getType_feve() == Feve.F_HQ_BE) && (Filiere.LA_FILIERE.getEtape() - lot.getEtape() >= DELAI_HQ_MQ)) {
				lot.setType_feve(Feve.F_MQ_E);
			}
			else if(lot.getType_feve() == Feve.F_HQ && (Filiere.LA_FILIERE.getEtape() - lot.getEtape() >= DELAI_HQ_MQ)) {
				lot.setType_feve(Feve.F_MQ);
			}
			else if((lot.getType_feve() == Feve.F_MQ_E || lot.getType_feve() == Feve.F_MQ) && (Filiere.LA_FILIERE.getEtape() - lot.getEtape() >= DELAI_MQ_BQ)) {;
				lot.setType_feve(Feve.F_BQ);
			}
			else if(lot.getType_feve() == Feve.F_BQ && (Filiere.LA_FILIERE.getEtape() - lot.getEtape() >= DELAI_BQ_JETE)) {
				//this.retire_lot(lot); ne fonctionne pas, je ne sais pas pourquoi
				lot.setQuantite(0);
				}
			}
	}
	
	//Faite par Noémie
	//Retourne une liste contenant tous les lots d'un même type de fèves
	public List<Producteur2_Lot> lot_type_feve(Feve type_feve){
		
		List<Producteur2_Lot> lst_lot  = this.lst_stock_total; 
		List<Producteur2_Lot> lst_lot_feve  = new ArrayList<Producteur2_Lot>();
		
		for(Producteur2_Lot lot : lst_lot) {
			if(lot.getType_feve() == type_feve) {
				lst_lot_feve.add(lot);
			}
		}
		return lst_lot_feve;
	}
	
	//Faite par Quentin
	//Calcule le coût total de stockage
	public double cout_total_stock() {
		double cout_moyen = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur();
		return cout_moyen * this.getStockTotal(this.cryptogramme);
	}
	
	//Faite par Quentin
	//Met à jour la liste des lots dans le stock en fonction du type de fève et de la quantité à vendre souhaités
	public double stock_a_vendre(Feve type_feve, double quantite_demandee) {
		List<Producteur2_Lot> lst_lot_feve = this.lot_type_feve(type_feve);
		double quantite_prise = 0;
		for(Producteur2_Lot l : lst_lot_feve) {
			if(quantite_prise == quantite_demandee) {
				return quantite_prise;
			}
			else {
				if(quantite_prise + l.getQuantite() <= quantite_demandee) {
					quantite_prise += l.getQuantite();
					this.retire_lot(l);
				}
				else {
					l.setQuantite(l.getQuantite() - (quantite_demandee - quantite_prise));
					quantite_prise += (quantite_demandee - quantite_prise);
				}
			}
		}
		return quantite_prise;
	}
	
	//Faite par Noemie 
	//Permet de gérer le surplus de stock après avoir dépassé le seuil défini
	public void trop_de_stock(Feve type_feve, double quantite) {
		this.trop_d_employes();
		double stock_init = this.getStockTotal(cryptogramme);
		List<Producteur2_Lot> lst_lot_feve = this.lot_type_feve(type_feve);
		double quantite_retiree = 0;
		for(Producteur2_Lot l : lst_lot_feve) {
			if(quantite_retiree >= quantite && stock_init-quantite_retiree < SEUIL ) {
				break;
			}
			else {
				if(quantite_retiree + l.getQuantite() <= quantite) {
					quantite_retiree += l.getQuantite();
					this.retire_lot(l);
				}
				else {
					l.setQuantite(l.getQuantite() - (quantite - quantite_retiree));
					quantite_retiree += (quantite - quantite_retiree);
				}
			}
		}
		if (quantite + this.getStockTotal(cryptogramme) < SEUIL) {
			this.ajout_stock(type_feve, quantite);
		}
	}
	 

	//Faite par Quentin
	//Ajoute les nouvelles informations sur le stock au journal du stock
	public void ajout_stock_journal() {
		this.journalStocks.ajouter(" ");
		this.journalStocks.ajouter("------------ ETAPE " + Filiere.LA_FILIERE.getEtape() + " ---------------");
		this.journalStocks.ajouter("La quantité de fèves_HQ en stock est de "+this.getQuantiteEnStock(Feve.F_HQ, this.cryptogramme)+"kg");
		this.journalStocks.ajouter("La quantité de fèves_HQ_BE en stock est de "+this.getQuantiteEnStock(Feve.F_HQ_BE, this.cryptogramme)+"kg");
		this.journalStocks.ajouter("La quantité de fèves_MQ en stock est de "+this.getQuantiteEnStock(Feve.F_MQ, this.cryptogramme)+"kg");
		this.journalStocks.ajouter("La quantité de fèves_MQ_E en stock est de "+this.getQuantiteEnStock(Feve.F_MQ_E, this.cryptogramme)+"kg");
		this.journalStocks.ajouter("La quantité de fèves_HQ_E en stock est de "+this.getQuantiteEnStock(Feve.F_HQ_E, this.cryptogramme)+"kg");
		this.journalStocks.ajouter("La quantité de fèves_BQ en stock est de "+this.getQuantiteEnStock(Feve.F_BQ, this.cryptogramme)+"kg");
		this.journalStocks.ajouter("La quantité totale de fèves en stock est de "+this.getStockTotal(this.cryptogramme)+"T");
		this.journalStocks.ajouter("Le coût total du stock est de "+this.cout_total_stock()+"€");
	}
	
	//Faite par Quentin
	
	public void next() {
		super.next();
		this.lot_to_hashmap();
		this.changement_qualite();
		this.ajout_stock_journal();
	}
}


	