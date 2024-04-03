package abstraction.eq2Producteur2;
import java.util.ArrayList;
import java.util.List;
import abstraction.eqXRomu.general.Journal;

import abstraction.eqXRomu.filiere.Filiere;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

public class Producteur2_Stocks extends Producteur2Acteur {
	
	private static final double SEUIL = 0; 
	//seuil max de la production stockee (voir ce qu'on fait du reste: vente, poubelle, produit moins, ...)
	//déterminer ce qu'on fait en fonction de comment on est proche du seuil max
	
	//private final int PRIX_STOCK_TONNE = 0; récupérable via la filière
	
	
	
	private static final double DELAI_HQ_MQ = 4;
	private static final double DELAI_MQ_BQ = 8;
	private static final double DELAI_BQ_JETE = 12;
	
	
	private List<Producteur2_Lot> stock_total;
	protected Journal journalStocks;
	
	//méthode pour gérer ce qui est retiré des stocks ou non
	
	//FILIERE.getEtape() pour avoir le numéro d'étape
	
	public Producteur2_Stocks() {
		super();
		List<Producteur2_Lot> stock_total = new ArrayList<Producteur2_Lot>();
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
	
	public List<Producteur2_Lot> getStock_total() {
		return this.stock_total;
	}
	
	/*public void action_seuil() {
		if(this.getQuantiteEnStock())
	}*/
	
	public void retire_lot(Producteur2_Lot  l) {
		this.stock_total.remove(l);
	}
	
	
	//Faite par Quentin
	//Met à jour la liste des stocks en ajoutant les lots invendus
	public void ajout_invendus(double quantite_rest_BQ, double quantite_rest_MQ, double quantite_rest_MQ_E, double quantite_rest_HQ, double quantite_rest_HQ_E, double quantite_rest_HQ_BE) {
		if(quantite_rest_BQ != 0) {
			this.stock_total.add(new Producteur2_Lot(quantite_rest_BQ, Feve.F_BQ));
		}
		if(quantite_rest_MQ != 0) {
			this.stock_total.add(new Producteur2_Lot(quantite_rest_MQ, Feve.F_MQ));
		}
		if(quantite_rest_MQ_E != 0) {
			this.stock_total.add(new Producteur2_Lot(quantite_rest_MQ_E, Feve.F_MQ_E));
		}
		if(quantite_rest_HQ != 0) {
			this.stock_total.add(new Producteur2_Lot(quantite_rest_HQ, Feve.F_HQ));
		}
		if(quantite_rest_HQ_E != 0) {
			this.stock_total.add(new Producteur2_Lot(quantite_rest_HQ_E, Feve.F_HQ_E));
		}
		if(quantite_rest_HQ_BE != 0) {
			this.stock_total.add(new Producteur2_Lot(quantite_rest_HQ_BE, Feve.F_HQ_BE));
		}
	}
	
	//Faite par Noémie
	//Fonction qui parcourt l'ensemble des lots et récupère la quantité de fève pour chaque type de fèves
	public void lot_to_hashmap() {
		List<Producteur2_Lot> l = getStock_total();
		double feve_bq = 0;
		double feve_mq = 0;
		double feve_hq = 0;
		double feve_mq_e= 0;
		double feve_hq_e = 0;
		double feve_hq_be = 0;
		
		for (int i = 0; i < getStock_total().size(); i++) {
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
		for(Producteur2_Lot lot : this.stock_total) {
			if((lot.getType_feve() == Feve.F_HQ_E || lot.getType_feve() == Feve.F_HQ_BE) && (Filiere.LA_FILIERE.getEtape() - lot.getEtape() >= DELAI_HQ_MQ)) {
				lot.setType_feve(Feve.F_MQ_E);
			}
			if(lot.getType_feve() == Feve.F_HQ && (Filiere.LA_FILIERE.getEtape() - lot.getEtape() >= DELAI_HQ_MQ)) {
				lot.setType_feve(Feve.F_MQ);
			}
			if((lot.getType_feve() == Feve.F_MQ_E || lot.getType_feve() == Feve.F_MQ) && (Filiere.LA_FILIERE.getEtape() - lot.getEtape() >= DELAI_MQ_BQ)) {
				lot.setType_feve(Feve.F_BQ);
			}
			if(lot.getType_feve() == Feve.F_BQ && (Filiere.LA_FILIERE.getEtape() - lot.getEtape() >= DELAI_BQ_JETE)) {
				this.retire_lot(lot);
			}
		}
	}

	public void mettreajour_journal() {
		journal.ajouter("La quantité de fèves_HQ en stock est de "+stock.get(Feve.F_HQ)+"T");
		journal.ajouter("La quantité de fèves_HQ_BE en stock est de "+stock.get(Feve.F_HQ_BE)+"T");
		journal.ajouter("La quantité de fèves_MQ en stock est de "+stock.get(Feve.F_MQ)+"T");
		journal.ajouter("La quantité de fèves_MQ_E en stock est de "+stock.get(Feve.F_MQ_E)+"T");
		journal.ajouter("La quantité de fèves_HQ_E en stock est de "+stock.get(Feve.F_HQ_E)+"T");
		journal.ajouter("La quantité de fèves_BQ en stock est de "+stock.get(Feve.F_BQ)+"T");
	}
	
	public List<Producteur2_Lot> lot_type_feve(Feve type_feve){
		List<Producteur2_Lot> lst_lot  = this.stock_total; 
		List<Producteur2_Lot> lst_lot_feve  = new ArrayList<Producteur2_Lot>();
		for(Producteur2_Lot lot : lst_lot) {
			if (lot.getType_feve() == type_feve) {
				lst_lot_feve.add(lot);
			}
		}
		return lst_lot_feve;
	}
	
	//Faite par Noémie
	//Calcule le coût total de stockage
	public double cout_total_stock() {
		double cout_moyen = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur();
		return cout_moyen* getStockTotal(this.cryptogramme);
	}
}


	