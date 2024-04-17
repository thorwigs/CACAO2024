package abstraction.eq6Transformateur3;

import java.util.LinkedList;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur3AcheteurBourse extends Transformateur3VendeurCCadre implements IAcheteurBourse {
	
	protected Journal journalBourse;

	public Transformateur3AcheteurBourse() {
		//super();
		
		this.journalBourse = new Journal(this.getNom()+" journal Bourse", this);
	}
	
	@Override
	public double demande(Feve f, double cours) {
		if (this.stockFeves.get(f)<30000) {
			this.journalBourse.ajouter("Achat en bourse de " + (30000 - this.stockFeves.get(f)) + " fèves "+ f);
			return 30000-this.stockFeves.get(f); 
			
		}
		this.journalBourse.ajouter(" ");
		return 0;
	}

	@Override
	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
		
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {

	}

}
