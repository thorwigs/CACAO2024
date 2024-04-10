package abstraction.eq7Transformateur4;

import java.util.List;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;


public class Transformateur4AcheteurBourse extends Transformateur4Acteur implements IAcheteurBourse {

	private Journal journalBourse;
	public int demande;
	
	public Transformateur4AcheteurBourse () {
		super();
		this.journalBourse = new Journal(this.getNom()+" journalBourse", this);
	}
	

	public double demande(Feve f, double cours) {
		demande = 20;
		journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je souhaite acheter "+demande+" T de "+f);
		return demande;
	}
	
	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
	}

	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
	}
	

	public void next() {
		super.next();
		this.journalBourse.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		
	}
	
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalBourse);
		return res;
	}



}
