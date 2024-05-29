/**@authors Fatima-Ezzahra  */
package abstraction.eq1Producteur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur1VendeurBourse extends Producteur1Production implements  IVendeurBourse {
	public double  pourcentageHQ=0.1 ;
	public double  pourcentageBQ=0.05 ;
	public double  pourcentageMQ=0.05 ;
	public double quantiteEnTBQ;
	public double quantiteEnTMQ;
	public double quantiteEnTHQ;
	private Journal journalBourse;
	protected ArrayList<Double> bourseBQ; 
	protected ArrayList<Double> bourseMQ; 
	protected ArrayList<Double> bourseHQ; 
	/**
	 * Constructeur de la classe Producteur1VendeurBourse.
	 */
	public Producteur1VendeurBourse() {
		super();
		this.journalBourse = new Journal(this.getNom()+" journal Bourse", this);
		bourseBQ = new ArrayList<Double>();
		bourseMQ = new ArrayList<Double>();
		bourseHQ = new ArrayList<Double>();
	}



	// Fatima-ezzahra
	@Override

	/**
	 * Offre une quantité de fèves sur la bourse en fonction de son prix seuil et du cours actuel.
	 * @param f Le type de fève à offrir.
	 * @param cours Le cours actuel de la fève sur la bourse.
	 * @return La quantité de fèves offerte sur la bourse.
	 */
	public double offre(Feve f, double cours) {
		
        
	
		double quantiteEnT =this.getQuantiteEnStock(  f ,   cryptogramme);
		
       

		if (quantiteEnT!=0) { 
			double Seuil = getCoutUnitaireProduction(f);
			System.out.println("seuil"+Seuil);
			System.out.println("cours"+cours);
		

			if (f.getGamme()==Gamme.MQ) {
				//if(true) {
				
				if(cours>= 1400) {
					this.quantiteEnTMQ=0.4*quantiteEnT;
					journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente "+this.quantiteEnTMQ+" T de "+f);

					return quantiteEnTMQ;

				}
				
			}
			if (f.getGamme()==Gamme.HQ) {
				//if(true) {
				
				if(cours>= 1700) {
					this.quantiteEnTHQ=0.7*quantiteEnT;
					journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente "+this.quantiteEnTHQ+" T de "+f);

					return quantiteEnTHQ;
				}
			
			}
			if (f.getGamme()==Gamme.BQ) {
				//if(true) {
				if(cours>= 1200) {
					this.quantiteEnTBQ=0.3*quantiteEnT;
					//double offre =  this.stock.get(f).getValeur()*(Math.min(cours, 3000)/3000.0);
					journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente "+quantiteEnTBQ+" T de "+f);
					return quantiteEnTBQ;
				}
				
			}
		}
		
		
			plantation = new HashMap<Feve, Double>();
			double quantite = 1000;
			plantation.put(f, quantite);
			adjustPlantationSize(plantation);
			
			
		
		
		journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente 0.0 T de "+f);
		return 0;
		


	}
	/**
	 * Gère la notification de vente d'une quantité de fèves et met à jour le stock et le solde.
	 * @param f Le type de fève vendu.
	 * @param quantiteEnT La quantité de fèves vendue.
	 * @param coursEnEuroParT Le cours de vente de la fève.
	 * @return La quantité de fèves retirée du stock.
	 */
	@Override
	public double notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		// TODO Auto-generated method stub

		double retire = Math.min(this.stock.get(f).getValeur(), quantiteEnT);
		this.stock.get(f).retirer(this, retire, cryptogramme);

		journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : j'ai vendu "+quantiteEnT+" T de "+f+" -> je retire "+retire+" T du stock qui passe a "+this.stock.get(f).getValeur((Integer)cryptogramme));
		super.notificationOperationBancaire(retire*coursEnEuroParT);
		super.getSolde();
		

		return retire;
	}
	/**
	 * Notifie l'acteur qu'il est blacklisté pour une certaine durée.
	 * @param dureeEnStep La durée de la mise en blacklist en nombre d'étapes.
	 */
	@Override
	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
		journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je suis blackliste pour une duree de "+dureeEnStep+" etapes");


	}
	public void changePlant() {
		double ameBQ = 0; double ameMQ= 0; double ameHQ = 0;
		if (bourseBQ.size() > 12) {
			for (int i = 0; i < bourseBQ.size()-1;i++) {
				ameBQ += (bourseBQ.get(i+1)-bourseBQ.get(i+1))/bourseBQ.get(i);
			}
		}
	}
	
	/**
	 * Renvoie les journaux de l'acteur, y compris le journal de la bourse.
	 * @return Une liste contenant les journaux de l'acteur.
	 */
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalBourse);
		return res;
	}
	
	public void next() {
		super.next();
		changePlant();
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		bourseBQ.add(bourse.getCours(Feve.F_BQ).getValeur());
		bourseMQ.add(bourse.getCours(Feve.F_MQ).getValeur());
		bourseHQ.add(bourse.getCours(Feve.F_HQ).getValeur());
		
	}

}
