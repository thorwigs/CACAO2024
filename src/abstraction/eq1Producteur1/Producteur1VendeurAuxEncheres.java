package abstraction.eq1Producteur1;

import java.util.List;


import abstraction.eqXRomu.encheres.Enchere;
import abstraction.eqXRomu.encheres.IVendeurAuxEncheres;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Producteur1VendeurAuxEncheres extends Producteur1VendeurAppelIDOffre implements IVendeurAuxEncheres {

	
	private double prixEnchereBq;
	
	private double prixEnchereHqEqui;
	private double prixEnchereHqBio;
	private double prixEnchereHq;
	private double prixEnchereHqBioEqui;
	
	private double prixEnchereMq;
	private double prixEnchereMqEqui;
	private double prixEnchereMqbio;
	private double prixEnchereMqBioEqui;
	
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
		    		return propositions.get(indice);
		    	}
		    	else return null;
		    }
		    	
		    	else if (gamme == Gamme.MQ) {
		    		if (bio) {
		    			if (equitable) {
		    				if (prix >=  prixEnchereMqBioEqui ) {
		    					return propositions.get(indice);
		    					
		    	             }
		    				else return null;
		    			}
		    			else if (prix >= prixEnchereMqbio ) {
		    				return propositions.get(indice);
		    			}
		    			else return null;
		    			
		    		}
		    		else if (equitable){
		    			if (prix >= prixEnchereMqEqui ) {
		    				return propositions.get(indice);
		    				
		    			}
		    			else return null;
		    			
		    		}
		    		
		    		else if (prix >=  prixEnchereMq) {
		    			return propositions.get(indice);
		    			
		    		}
		    		else return null;
		    		
		    		
		    		
		    		
		    		
		    	}
		    	
		    	else if (gamme == Gamme.HQ) {
		    		if (bio) {
		    			if (equitable) {
		    				if (prix >=  prixEnchereHqBioEqui ) {
		    					return propositions.get(indice);
		    					
		    	             }
		    				else return null;
		    			}
		    			else if (prix >= prixEnchereHqBio) {
		    				return propositions.get(indice);
		    			}
		    			else return null;
		    			
		    		}
		    		else if (equitable){
		    			if (prix >= prixEnchereHqEqui ) {
		    				return propositions.get(indice);
		    				
		    			}
		    			else return null;
		    			
		    		}
		    		
		    		else if (prix >=  prixEnchereHq) {
		    			return propositions.get(indice);
		    			
		    		}
		    		else return null;
		    		
		    		
		    		
		    		
		    		
		    	}
		    return null;
		    	
		    	
		    
		    
		
	
	}

}
