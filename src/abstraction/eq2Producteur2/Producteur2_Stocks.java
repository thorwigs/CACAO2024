package abstraction.eq2Producteur2;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

/** Classe permettant de gérer les stocks
 * @author Quentin
 */

public abstract class Producteur2_Stocks extends Producteur2Acteur {
	
	
	/** Définition des variables
	 * @author Quentin
	 */	
	//seuil max de la production stockee
	private static final double SEUIL = 300000;
	
	//délais avant de passer à une qualité inférieure
	private static final double DELAI_HQ_MQ = 4;
	private static final double DELAI_MQ_BQ = 8;
	private static final double DELAI_BQ_JETE = 12;
		
	private List<Producteur2_Lot> lst_stock_total;
	protected Journal journalStocks;
	
	protected abstract void trop_d_employes();
	
	/** Constructeur de classe
	 * @author Quentin
	 */
	public Producteur2_Stocks() {
		super();
		this.journalStocks = new Journal(this.getNom()+" journalStocks", this);
		this.lst_stock_total = new ArrayList<Producteur2_Lot>();
	}
	
	/** Initialisation
	 * @author Quentin
	 */
	public void initialiser() {
		super.initialiser();
	}
	
	/** Getter
	 * @author Quentin
	 */
	public static double getSeuil() {
		return SEUIL;
	}
	
	/** Getter
	 * @author Quentin
	 */
	public static double getDelaiHqMq() {
		return DELAI_HQ_MQ;
	}
	
	/** Getter
	 * @author Quentin
	 */
	public static double getDelaiMqBq() {
		return DELAI_MQ_BQ;
	}
	
	/** Getter
	 * @author Quentin
	 */
	public List<Producteur2_Lot> getLst_Stock_total() {
		return this.lst_stock_total;
	}
	
	/** Setter
	 * @author Quentin
	 */
	public void SetLst_Stock_total( List<Producteur2_Lot> lst) {
		this.lst_stock_total = lst;
	}
	
	/** Méthode pour retirer un lot de la liste des lots
	 * @author Quentin
	 */
	public void retire_lot(Producteur2_Lot  l) {
		this.lst_stock_total.remove(l);
	}
	
	/** Méthode pour obtenir les journaux
	 * @author Quentin
	 */
	public List<Journal> getJournaux() {
		List<Journal> jx = super.getJournaux();
		jx.add(journalStocks);
		return jx;
	}
	
	/** Met à jour la liste des stocks en ajoutant un lot produit
	 * @param type_feve
	 * @param quantite
	 * @author Quentin
	 */
	public void ajout_stock(Feve type_feve, double quantite) {
		
		//Si on dépasse le seuil de stockage
		if (this.getQuantiteEnStock(type_feve, this.cryptogramme)+ quantite > SEUIL && quantite > 0) {
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
				//System.out.println(" ajout nouveau lot de hq_e avec " + quantite + " feve");
				this.lst_stock_total.add(new Producteur2_Lot(quantite, Feve.F_HQ_E));
			}
			if(quantite != 0 && type_feve == Feve.F_HQ_BE) {
				this.lst_stock_total.add(new Producteur2_Lot(quantite, Feve.F_HQ_BE));
			}
			this.lot_to_hashmap();
		}
	}
	
	/** Initialiser les stocks à l'étape 0 de la filière
	 * @param type_feve
	 * @param quantite
	 * @author Quentin
	 */
	public void init_stock(Feve type_feve, double quantite) {
		List<Producteur2_Lot> stocks =  new ArrayList<Producteur2_Lot>();
		if(quantite > 0 && type_feve == Feve.F_BQ) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_BQ,0));
		}
		if(quantite > 0 && type_feve == Feve.F_MQ) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_MQ,0));
		}
		if(quantite > 0 && type_feve == Feve.F_MQ_E) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_MQ_E,0));
		}
		if(quantite > 0 && type_feve == Feve.F_HQ) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_HQ,0));
		}
		if(quantite > 0 && type_feve == Feve.F_HQ_E) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_HQ_E,0));
		}
		if(quantite > 0 && type_feve == Feve.F_HQ_BE) {
			stocks.add(new Producteur2_Lot(quantite, Feve.F_HQ_BE,0));
		}
		this.lst_stock_total = stocks;
	}
	
	
	/** Fonction qui parcourt l'ensemble des lots et récupère la quantité de fève pour chaque type de fèves
	 * @author Noémie
	 */
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
	
	/** Change la qualité des fèves en fonction de la durée de stockage
	 * @author Quentin
	 */
	public void changement_qualite() {
		List<Producteur2_Lot> lst = new LinkedList<Producteur2_Lot>();
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

				lst.add(lot);
			}
		}
		// on retire les lots périmés
		for (Producteur2_Lot lot :lst) {
			retire_lot(lot);
		}
		lot_to_hashmap();
	}
	
	
	/** Retourne une liste contenant tous les lots d'un même type de fèves
	 * @param type_feve
	 * @author Noémie
	 */
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
	
	/** Calcule le coût total de stockage
	 * @author Quentin
	 */
	public double cout_total_stock() {
		double cout_moyen = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur();
		return cout_moyen * this.getStockTotal(this.cryptogramme);
	}
	
	/** Met à jour la liste des lots dans le stock en fonction du type de fève et de la quantité à vendre souhaités
	 * @param type_feve
	 * @param quantite_demandee
	 * @author Quentin
	 */
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
		lot_to_hashmap();
		return quantite_prise;
	}
	
	/** Permet de gérer le surplus de stock après avoir dépassé le seuil défini
	 * @param type_feve
	 * @param quantite
	 * @author Noémie
	 */
	public void trop_de_stock(Feve type_feve, double quantite) {
		this.trop_d_employes();
		double stock_init = this.getStockTotal(cryptogramme);
		List<Producteur2_Lot> lst_lot_feve = this.lot_type_feve(type_feve);
		double quantite_retiree = 0;
		for(Producteur2_Lot l : lst_lot_feve) {
			if(quantite_retiree/2 >= quantite && stock_init-quantite_retiree < SEUIL ) {
				break;
			}
			else{
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
		lot_to_hashmap();
	}
	
	/** Ajoute les nouvelles informations sur le stock au journal du stock
	 * @author Quentin
	 */
	public void ajout_stock_journal() {
		this.journalStocks.ajouter(" ");
		this.journalStocks.ajouter("------------ ETAPE " + Filiere.LA_FILIERE.getEtape() + " ---------------");
		this.journalStocks.ajouter("La quantité de fèves_HQ en stock est de "+this.getQuantiteEnStock(Feve.F_HQ, this.cryptogramme)+"T");
		this.journalStocks.ajouter("La quantité de fèves_HQ_BE en stock est de "+this.getQuantiteEnStock(Feve.F_HQ_BE, this.cryptogramme)+"T");
		this.journalStocks.ajouter("La quantité de fèves_MQ en stock est de "+this.getQuantiteEnStock(Feve.F_MQ, this.cryptogramme)+"T");
		this.journalStocks.ajouter("La quantité de fèves_MQ_E en stock est de "+this.getQuantiteEnStock(Feve.F_MQ_E, this.cryptogramme)+"T");
		this.journalStocks.ajouter("La quantité de fèves_HQ_E en stock est de "+this.getQuantiteEnStock(Feve.F_HQ_E, this.cryptogramme)+"T");
		this.journalStocks.ajouter("La quantité de fèves_BQ en stock est de "+this.getQuantiteEnStock(Feve.F_BQ, this.cryptogramme)+"T");
		this.journalStocks.ajouter(" ");
		this.journalStocks.ajouter("La quantité totale de fèves en stock est de "+this.getStockTotal(this.cryptogramme)+"T");
		this.journalStocks.ajouter("Le coût total du stock est de "+this.cout_total_stock()+"€");
	}
	
	/** Next
	 * @author Quentin
	 */
	public void next() {
		super.next();
		this.lot_to_hashmap();
		this.changement_qualite();
		this.ajout_stock_journal();
	}
}


	