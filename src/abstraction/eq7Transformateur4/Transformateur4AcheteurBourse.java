package abstraction.eq7Transformateur4;

import java.util.List;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;


public class Transformateur4AcheteurBourse extends Transformateur4Acteur implements IAcheteurBourse {

	protected Journal journalBourse;
	private int D;
	
	public Transformateur4AcheteurBourse () {
		super();
		this.journalBourse = new Journal(this.getNom()+" journal Bourse achat", this);
	}
	

	public double demande(Feve f, double cours) {
		D = 1;
		journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je souhaite acheter "+ D +" T de "+f);
		return D;
	}
	
	
	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
		
		
		this.journalBourse.ajouter("- achat de "+quantiteEnT+"T de fèves "+f);
	}

	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
	}
	
	
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalBourse);
		return res;
	}

	


}
