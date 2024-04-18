package abstraction.eq6Transformateur3;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Transformateur3AcheteurBourse extends Transformateur3VendeurCCadre implements IAcheteurBourse {
	
	protected Journal journalBourse;

	public Transformateur3AcheteurBourse() {
		super();
		this.journalBourse = new Journal(this.getNom()+" Journal Bourse", this);
	}
	
	
	
	public void next() {
		super.next();
		this.journalBourse.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
	}
	@Override
	public double demande(Feve f, double cours) {
	  
		// Notre acteur achète des fèves selon leurs différents cours en bourse, et ses stocks.
		// Il a à la fois des seuils (pour le cours comme pour ses stocks), et des coefficients de 
		// proportionnalité (+ le prix est bas, + il va en acheter pour les 2 seuils bas, 
		// et + le prix est haut et moins il va en acheter pour le seuilHaut.
		
	    double seuilTrèsBas = 200;
	    double seuilBas = 5000;
	    double seuilHaut = 1000;
	    double demandeDefault = 1000; 
	    // Faut que je fasse différents seuils seulon les qualités
	   
	   
	     if (this.stockFeves.get(f)<10000) {
			return 10000-this.stockFeves.get(f); // Si on n'a plus de fèves, on en achète en urgence en bourse pour avoir un "fond de roulement" à 10k
		}
	
	
	    else if (cours < seuilTrèsBas) {
	        return demandeDefault * 3.0 * cours/seuilTrèsBas; // Si le cours est vraiment bas, on achète beaucoup
	    }
	    
	     if (this.stockFeves.get(f)>50000) { // Si on a trop de stocks, on achète uniquement si le cours est très bas (if du dessus, sinon on ignore les if suivants)
		    	return 0.0;
		    }
	   
	    else if (cours < seuilBas) {
	        return demandeDefault*1.5 *  cours/seuilBas;  // Si le cours est bas, on n'achète un peu plus que d'habitude
	    }
	    
	    else if (cours > seuilHaut) { // Si le cours est haut, on n'achète rien (les CC devraient suffire, et en cas d'urgence le premier if fera l'affaire)
	    	return 0;
	    }
	   	    else {
	        return demandeDefault *  (1-cours/seuilTrèsBas); // Si le cours est normal, on achète une quantité 
	    }
	}

	
	
	@Override
	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
		this.journalBourse.ajouter("Achat en bourse de " + quantiteEnT + "tonnes de fèves "+ f);
		
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {

	}

	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalBourse);
		return res;
	}
}
