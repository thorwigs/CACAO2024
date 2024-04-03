package abstraction.eq1Producteur1;

import java.util.List;


import abstraction.eqXRomu.encheres.Enchere;
import abstraction.eqXRomu.encheres.IVendeurAuxEncheres;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Producteur1VendeurAuxEncheres extends Producteur1VendeurAppelIDOffre implements IVendeurAuxEncheres {

	
	private double prixEnchereBq;
	
	private double prixEnchereHqEqui = 0;
	private double prixEnchereHqBio=0;
	private double prixEnchereHq=0;
	private double prixEnchereHqBioEqui=0;
	
	private double prixEnchereMq=0;
	private double prixEnchereMqEqui=0;
	private double prixEnchereMqbio=0;
	private double prixEnchereMqBioEqui=0;
	protected Journal journalEncheres;
	
	
	
	// Fatima-ezzahra 
	public void initialiser() {
     Journal journalEncheres = new Journal(this.getNom()+" journal Encheres", this);
	}
	
	public Enchere choisir(List<Enchere> propositions) {
		int i = propositions.size();
		double prix = 0;
		int indice =0;
		for (int j=0; j<i; i++) { 
			if (propositions.get(j).getPrixTonne()>prix) {
				prix=propositions.get(j).getPrixTonne();
				indice=j;
			}
			
		}
		boolean bio =   ( (Feve  ) propositions.get(0).getProduit()).isBio();
		boolean equitable  =   ( (Feve  ) propositions.get(0).getProduit()).isEquitable();
		Gamme gamme = ( (Feve  ) propositions.get(0).getProduit()).getGamme();
	
		    if (gamme ==  Gamme.BQ ) {
		    	if (prix >= prixEnchereBq ) {
		    		journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
		    		return propositions.get(indice);
		    	}
		    	else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
		    		return null;
		    }
		    	
		    	else if (gamme == Gamme.MQ) {
		    		if (bio) {
		    			if (equitable) {
		    				if (prix >=  prixEnchereMqBioEqui ) {
		    					journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
		    					return propositions.get(indice);
		    					
		    	             }
		    				else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
		    					return null;
		    			}
		    			else if (prix >= prixEnchereMqbio ) {
		    				journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
		    				return propositions.get(indice);
		    			}
		    			else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
		    				return null;
		    			
		    		}
		    		else if (equitable){
		    			if (prix >= prixEnchereMqEqui ) {
		    				journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
		    				return propositions.get(indice);
		    				
		    			}
		    			else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
		    				return null;
		    			
		    		}
		    		
		    		else if (prix >=  prixEnchereMq) {
		    			journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
		    			return propositions.get(indice);
		    			
		    		}
		    		else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
		    			return null;
		    		
		    		
		    		
		    		
		    		
		    	}
		    	
		    	else if (gamme == Gamme.HQ) {
		    		if (bio) {
		    			if (equitable) {
		    				if (prix >=  prixEnchereHqBioEqui ) {
		    					journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
		    					return propositions.get(indice);
		    					
		    	             }
		    				else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
		    					return null;
		    			}
		    			else if (prix >= prixEnchereHqBio) {
		    				journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
		    				return propositions.get(indice);
		    			}
		    			else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
		    				return null;
		    			
		    		}
		    		else if (equitable){
		    			if (prix >= prixEnchereHqEqui ) {
		    				journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
		    				return propositions.get(indice);
		    				
		    			}
		    			else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
		    				return null;
		    			
		    		}
		    		
		    		else if (prix >=  prixEnchereHq) {
		    			journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
		    			return propositions.get(indice);
		    			
		    		}
		    		else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
		    			return null;
		    		
		    		
		    		
		    		
		    		
		    	}
		    return null;
		    	
	}
	

}
