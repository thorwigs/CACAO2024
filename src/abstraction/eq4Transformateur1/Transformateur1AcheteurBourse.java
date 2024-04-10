package abstraction.eq4Transformateur1;

import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur1AcheteurBourse extends Transformateur1Acteur implements IAcheteurBourse {
	

	protected Journal journalAchatBourse;
	
	public Transformateur1AcheteurBourse() {
		super();

		this.journalAchatBourse = new Journal(this.getNom()+" journalAchatBourse", this);
	}
	
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalAchatBourse);
		return res;
	}
	
	/**
	 * Retourne la quantite en tonnes de feves de type f desiree par l'acheteur 
	 * sachant que le cours actuel de la feve f est cours
	 * @param f le type de feve
	 * ici 20000 correspond au stock total de fèves voulues - à changer en fonction dans la simulation
	 */
	public double demande(Feve f, double cours) {
		double stockCible=20000;
		double demandeMin=10;
		double stockCibleHQ = 0.3 * stockCible;
	    double stockCibleMQ = 0.7 * stockCible;
	    if (f == Feve.F_HQ_BE) {
	        if (this.stockFeves.get(f) < stockCibleHQ) {
	            return Math.max(stockCibleHQ - this.stockFeves.get(f), demandeMin);}}
	    else if (f == Feve.F_MQ) {
	        if (this.stockFeves.get(f) < stockCibleMQ) {
	            return Math.max(stockCibleMQ - this.stockFeves.get(f), demandeMin);
	        }
	    }
		return 0;
	}

	@Override
	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {

		// TODO Auto-generated method stub
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
		
		this.journalAchatBourse.ajouter("- achat de "+quantiteEnT+"T de fèves "+f);


	}

	@Override
	public void notificationBlackList(int dureeEnStep) {
		
	}

}
