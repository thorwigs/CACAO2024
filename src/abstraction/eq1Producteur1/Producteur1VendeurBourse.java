
package abstraction.eq1Producteur1;

import java.util.List;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur1VendeurBourse extends Producteur1Production implements  IVendeurBourse {
	public double  prixSeuilHQ ;
	public double  prixSeuilBQ ;
	public double  prixSeuilMQ ;
	private Journal journalBourse;
	public Producteur1VendeurBourse() {
		super();
		this.journalBourse = new Journal(this.getNom()+" journal Bourse", this);
	}
	
	
	
// Fatima-ezzahra
	@Override
	

	public double offre(Feve f, double cours) {
		// TODO Auto-generated method stub
		
	   double quantiteEnT = this.getQuantiteEnStock(  f ,   cryptogramme);
	   
	   
	   if (quantiteEnT!=0) { 
		   
	  
		if (f.getGamme()==Gamme.MQ) {
			if(cours>=prixSeuilMQ) {
				journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente "+quantiteEnT+" T de "+f);
				
				return quantiteEnT;
				
			}
			else {
				return 0;
			}
		}
		if (f.getGamme()==Gamme.HQ) {
			if(cours>=prixSeuilHQ) {
				journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente "+quantiteEnT+" T de "+f);
				
				return quantiteEnT;
			}
			else {
				return 0;
			}
		}
		if (f.getGamme()==Gamme.BQ) {
			if(cours>=prixSeuilBQ) {
				//double offre =  this.stock.get(f).getValeur()*(Math.min(cours, 3000)/3000.0);
				journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente "+quantiteEnT+" T de "+f);
				return quantiteEnT;
			}
			else {
				return 0;
			}
		}
	   }
	   journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je met en vente 0.0 T de "+f);
		return 0;
	   
	
	}

	@Override
	public double notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		// TODO Auto-generated method stub
		
		double retire = Math.min(this.stock.get(f).getValeur(), quantiteEnT);
		this.stock.get(f).retirer(this, retire, cryptogramme);
		
		journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : j'ai vendu "+quantiteEnT+" T de "+f+" -> je retire "+retire+" T du stock qui passe a "+this.stock.get(f).getValeur((Integer)cryptogramme));
		super.notificationOperationBancaire(retire*coursEnEuroParT);
		super.getSolde();
		String s = this.stock.get(f).toString();
		
		return retire;
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
		journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je suis blackliste pour une duree de "+dureeEnStep+" etapes");
	
		
	}
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalBourse);
		return res;
		
		
		
		
		
	}

}
