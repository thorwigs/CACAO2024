package abstraction.eq7Transformateur4;

import java.util.List;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

//codé par Yanis et Anaïs

public class Transformateur4AcheteurBourse extends Transformateur4Acteur implements IAcheteurBourse {

	protected Journal journalBourse;
	private double D;
	
	public Transformateur4AcheteurBourse () {
		super();
		this.journalBourse = new Journal(this.getNom()+" journal Bourse achat", this);
	}
	
	


	public double demande(Feve f, double cours) { //changer selon conditions et qte d'achat de chaque fève
		if (f.equals(Feve.F_MQ) && stockFeves.get(f) + getQuantiteAuStep(f) - BesoinDeFeve(f) < 10000 ) {
			D = 10000 - (stockFeves.get(f) + getQuantiteAuStep(f) - BesoinDeFeve(f)) ;
			journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je souhaite acheter "+ D +" T de "+f);
			return D;
		}
		if (f.equals(Feve.F_HQ) && stockFeves.get(f) + getQuantiteAuStep(f) - BesoinDeFeve(f) < 10000 ) {
			D = 10000 - (stockFeves.get(f) + getQuantiteAuStep(f) - BesoinDeFeve(f)) ;
			journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je souhaite acheter "+ D +" T de "+f);
			return D;
		}
		else {
			return 0;
		}
	}

	public double restantALivrerDeTypeAuStep (Chocolat choco) { //permet d'obtenir le nombre de chocolat d'un type à livrer en CC, utile pour les CC de marque distributeur
		double res = 0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if ((c.getProduit().getType().equals("ChocolatDeMarque")) && ((ChocolatDeMarque)(c.getProduit())).getChocolat().equals(choco)) {
					res+=c.getQuantiteALivrerAuStep();
			}
		} 
		return res;
	}
	
	public double BesoinDeFeve(Feve f) {
		double BesoinPourChoco = 0.0;
		for (ExemplaireContratCadre contratC : this.contratsEnCours) {
			if ( contratC.getProduit().getType().equals("ChocolatDeMarque") ) {
				Chocolat c = ((ChocolatDeMarque)(contratC.getProduit())).getChocolat();
				if ( (c.getGamme().equals(f.getGamme())) && (c.isBio() == f.isBio()) && (c.isEquitable() == f.isEquitable())) {
					BesoinPourChoco += restantALivrerDeTypeAuStep( ((ChocolatDeMarque)(contratC.getProduit())).getChocolat() ) / (this.pourcentageTransfo.get(f).get(c));
				}
			}
		}
		return BesoinPourChoco;
	}
	
	public double getQuantiteAuStep (Feve f) {
		double res = 0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				res+=c.getQuantiteALivrerAuStep();
			}
		}
		return res;
	}
	
	
	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
		this.journalBourse.ajouter("- achat de "+quantiteEnT+"T de fèves "+f);
	}

	public void notificationBlackList(int dureeEnStep) {
		this.journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+"blacklisté pendant"+dureeEnStep+"etapes");
	}
	
	
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalBourse);
		return res;
	}
	
	public void next() {
		super.next();
		this.journalBourse.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
	}
}
