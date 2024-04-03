package abstraction.eq5Transformateur2;

import java.awt.Color;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;


public class Transformateur2AcheteurBourse extends Transformateur2VendeurCCadre implements IAcheteurBourse {
	protected Journal journalBourse;
	private double achatMaxParStep;
	
	/* A faire : 
	 * --> Stratégie sur la demande (nbr de tonne demandée)
	 * --> 
	 */


	////////////////////////////////////////////
	// Constructeur --> met à jour le journal //
	////////////////////////////////////////////
	public Transformateur2AcheteurBourse() {
		super();
		this.journalBourse = new Journal(this.getNom()+" journal Bourse", this);
	}

	
	/////////////	
	// Demande //
	/////////////
	public double demande(Feve f, double cours) {

		//à faire : faire les strat sur le nbr de tonne demandé sur BQ
		//à faire : faire les strat sur le nbr de tonne demandé sur HQ
		
		//Stratégie sur le BQ
		if (f.getGamme()==Gamme.BQ) {
			return 50;
		}
		
		//Stratégie sur le MQ
		if (f.getGamme()==Gamme.MQ) {
			return 50;
		}
		
		//Stratégie sur le HG => pas d'achat de HQ
		else {
			return 0;
		}
	}
	

	///////////////////////////////////////////////////////////////////////
	// Notifs de la vente ou de la BlackList + Mise à jour JournalBourse //	
	///////////////////////////////////////////////////////////////////////
	public void next() {
		super.next();
		this.journalBourse.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
	}
	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
		journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : j'ai acheté "+quantiteEnT+" T de "+f+"");
	}
	public void notificationBlackList(int dureeEnStep) {
		journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" BLACKLIST pendant "+dureeEnStep+" etapes");
	}
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalBourse);
		return res;
	}
}
