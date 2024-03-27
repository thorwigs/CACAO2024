package abstraction.eq1Producteur1;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur1VendeurBourse extends Producteur1Acteur implements  IVendeurBourse {
	public double  prixSeuilHQ ;
	public double  prixSeuilBQ ;
	public double  prixSeuilMQ ;
	private Journal journalBourse;
	
	
// Fatima-ezzahra
	@Override
	public double offre(Feve f, double cours) {
		// TODO Auto-generated method stub
		
	   double quantiteEnT = this.getQuantiteEnStock(  f ,   cryptogramme);
	   Gamme gamme = f.getGamme();
	   boolean bio = f.isBio();
		boolean equi = f.isEquitable();
	   
	   if (quantiteEnT!=0) {
		   
	  
		if (f.getGamme()==Gamme.MQ) {
			if(cours>=prixSeuilMQ) {
				journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente "+quantiteEnT+" T de "+f);
				
				return quantiteEnT;
				
			}
		}
		if (f.getGamme()==Gamme.HQ) {
			if(cours>=prixSeuilHQ) {
				journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente "+quantiteEnT+" T de "+f);
				return quantiteEnT;
			}
		}
		if (f.getGamme()==Gamme.BQ) {
			if(cours>=prixSeuilBQ) {
				journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente "+quantiteEnT+" T de "+f);
				return quantiteEnT;
			}
		}
	   }
		
		return 0;
	}

	@Override
	public double notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
		
	}

}
