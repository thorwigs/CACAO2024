package abstraction.eq6Transformateur3;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;




public class Transformateur3AcheteurBourse extends Transformateur3AcheteurCCadre implements IAcheteurBourse {
	protected Journal journalBourse;

	/**
	 * @author Cédric
	 * 
	 */
	public Transformateur3AcheteurBourse() {
		super();
		this.journalBourse = new Journal(this.getNom()+" Journal Bourse", this);
	}
	
	
	/**
	 * @author Cédric
	 * 
	 */
	public void next() {
		super.next();
		this.journalBourse.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
	}
	/**
	 * @author Cédric
	 * 
	 */

	public double demande(Feve f, double cours) {
	  
		// Stratégie : Notre acteur achète des fèves selon leurs différents cours en bourse, et ses stocks.
		// Il achète en fonction de seuils pour le cours comme pour ses stocks.
		
		if (f.getGamme()==Gamme.BQ) {
			 double seuilTrèsBas_BQ = 1000.0;
			    double seuilBas_BQ = 2000.0;
			    double seuilHaut_BQ = 3000.0;
			    double demandeDefault_BQ = 500.0; 

			     if (this.stockFeves.get(f)<10000.0) {
					return 10000-this.stockFeves.get(f); // Si on n'a plus de fèves, on en achète en urgence en bourse pour avoir un "fond de roulement" à 10k
				}
			
			
			    else if (cours < seuilTrèsBas_BQ) {
			        return demandeDefault_BQ * 3.0 ; // Si le cours est vraiment bas, on achète beaucoup
			    }
			    
			     if (this.stockFeves.get(f)>50000.0) { // Si on a trop de stocks, on achète uniquement si le cours est très bas (if du dessus, sinon on ignore les if suivants)
				    	return 0.0;
				    }
			   
			    else if (cours < seuilBas_BQ) {
			        return demandeDefault_BQ*2 ;  // Si le cours est bas, on n'achète un peu plus que d'habitude
			    }
			    
			    else if (cours > seuilHaut_BQ) { // Si le cours est haut, on n'achète rien (les CC devraient suffire, et en cas d'urgence le premier if fera l'affaire)
			    	return 0.0;
			    }
			   	    else {
			        return demandeDefault_BQ ; // Si le cours est normal, on achète une quantité normale
			   	    }
		}
		
		
		else if (f.getGamme()==Gamme.MQ) {
			
	    double seuilTrèsBas_MQ = 1500.0;
	    double seuilBas_MQ = 3000.0;
	    double seuilHaut_MQ = 4000.0;
	    double demandeDefault_MQ = 500.0; 

	     if (this.stockFeves.get(f)<10000.0) {
			return 10000-this.stockFeves.get(f); // Si on n'a plus de fèves, on en achète en urgence en bourse pour avoir un "fond de roulement" à 10k
		}
	
	
	    else if (cours < seuilTrèsBas_MQ) {
	        return demandeDefault_MQ * 3.0 ; // Si le cours est vraiment bas, on achète beaucoup
	    }
	    
	     if (this.stockFeves.get(f)>50000.0) { // Si on a trop de stocks, on achète uniquement si le cours est très bas (if du dessus, sinon on ignore les if suivants)
		    	return 0.0;
		    }
	   
	    else if (cours < seuilBas_MQ) {
	        return demandeDefault_MQ*2 ;  // Si le cours est bas, on n'achète un peu plus que d'habitude
	    }
	    
	    else if (cours > seuilHaut_MQ) { // Si le cours est haut, on n'achète rien (les CC devraient suffire, et en cas d'urgence le premier if fera l'affaire)
	    	return 0.0;
	    }
	   	    else {
	        return demandeDefault_MQ ; // Si le cours est normal, on achète une quantité normale
	    }
		}
		else  { // donc si (f.getGamme()==Gamme.HQ)
			 return 0.0; // car on n'achète pas de fèves hautes qualités en bourse
				
		}
	}

	

	/**
	 * @author Cedric 
	 * 
	 */	

	public void notificationAchat(Feve f, double quantiteEnT, double coursEnEuroParT) {
		this.stockFeves.put(f, this.stockFeves.get(f)+quantiteEnT);
		this.totalStocksFeves.ajouter(this, quantiteEnT, cryptogramme);
		this.journalBourse.ajouter("Achat en bourse de " + quantiteEnT + "tonnes de fèves "+ f);
		
	}

	// Pas de Blacklist en V1
	public void notificationBlackList(int dureeEnStep) {
	
	}
	/**
	 * @author Cedric 
	 * 
	 */
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalBourse);
		return res;
	}
}
