package abstraction.eq3Producteur3;

import java.util.HashMap;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur3VendeurContratCadre extends Producteur3VendeurBourse implements IVendeurContratCadre {

	@Override
	public boolean vend(IProduit produit) {
		//On accepte les contrats cadres sur le HQ et MQ
		if ((produit instanceof Feve)&&((((Feve)produit).getGamme() == Gamme.HQ)||(((Feve)produit).getGamme() == Gamme.MQ))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author mammouYoussef
	 */
	
	
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
	    Feve f = (Feve) contrat.getProduit();
	    Echeancier echeancierPropose = contrat.getEcheancier();
	    Echeancier nouvelEcheancier = new Echeancier(echeancierPropose.getStepDebut());


	    for (int step = echeancierPropose.getStepDebut(); step <= echeancierPropose.getStepFin(); step++) {
	        double quantiteDemandee = echeancierPropose.getQuantite(step);
	        
	     // Obtenir la quantité produite pour chaque étape
		    HashMap<Feve, Double> quantiteProduite = quantite();

	        if (quantiteProduite.containsKey(f) && quantiteProduite.get(f) >= quantiteDemandee) {
	            // Si la quantité produite est suffisante pour l'échéance, ajouter cette quantité à l'échéancier
	            nouvelEcheancier.ajouter(quantiteDemandee);
	        } else {
	            // Si la quantité produite est insuffisante, proposer la quantité disponible pour cette étape
	        	// simple vérification:
	        	double quantiteDisponible;
	        	if (quantiteProduite.containsKey(f)) {
	        	    quantiteDisponible = quantiteProduite.get(f);
	        	} else {
	        	    quantiteDisponible = 0;
	        	}
	        	
	            if (quantiteDisponible > SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER) {
	                nouvelEcheancier.ajouter(quantiteDisponible);
	            } else {
	                nouvelEcheancier = null; // dernier cas: On ne peut pas satisfaire le contrat
	                break; // Sortir de la boucle car on ne peut pas honorer le contrat
	            }
	        }
	    }
	    return nouvelEcheancier;
	}

	
	/**
	 * @author mammouYoussef
	 */
	public double propositionPrix(ExemplaireContratCadre contrat) {
	    IProduit produit = contrat.getProduit();
	    if (!(produit instanceof Feve)) { return 0;}
	    
	    Feve feve = (Feve) produit;
	    double prixBase=0;
	     if (feve.getGamme() == Gamme.HQ) {
	        prixBase = 3000; // à ajuster selon l'équitable et bio équitable
	    } else if (feve.getGamme() == Gamme.MQ) {
	       prixBase = 1910;
	    }
	  // Ajustements selon équitable et bio
	      if (feve.isEquitable() && feve.isBio()) {
	           prixBase = 3400; // Prix pour bio-équitable
	      } else if (feve.isEquitable()) {
	           prixBase = 3200; // Prix pour équitable 
	      }
	    return prixBase * 1.2; // Ajouter une marge de profit par exemple de 20% à modifier
	}

	/**
	 * @author mammouYoussef
	 */

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
	    IProduit produit = contrat.getProduit();
	    if (!(produit instanceof Feve)) {
	        return 0; }
	    double prixPropose = contrat.getPrix();
	    double prixMinimal= propositionPrix(contrat)/1.2;
	    // Si le prix proposé est supérieur au prixMinimal, accepter le prix proposé
	    if (prixPropose > prixMinimal) {
	        return prixPropose;
	    } else {
	        // Sinon, retourner le prix Minimal
	        return prixMinimal;
	    }
	}


	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journal_contrat_cadre.ajouter("Contrat cadre n°"+contrat.getNumero()+" avec "+contrat.getAcheteur().getNom()+" : "+contrat.getQuantiteTotale()+" T de "+contrat.getProduit()+" a "+contrat.getPrix()+" E/T");	
		ventefevecadre.put((Feve)contrat.getProduit(), contrat.getQuantiteTotale());
	}

	/**
	 * @author Arthur
	 */
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		double stock_inst = this.getQuantiteEnStock((Feve)produit, this.cryptogramme);
		if (quantite <= stock_inst) {
			//on verifie que l'on puisse fournir la quantite demande
			//il faut modifier les stocks suite a la vente
			this.setQuantiteEnStock((Feve)produit, stock_inst-quantite);
			this.journal_contrat_cadre.ajouter("Livraison totale : "+quantite+" T de feves "+((Feve)produit).getGamme()+" pour le CC n°"+contrat.getNumero());
			//on envoie ce que l'on a promis
			return quantite;
		} else {
			//on ne peut pas tout fournir, on envoie tout le stock
			this.setQuantiteEnStock((Feve)produit, 0);
			this.journal_contrat_cadre.ajouter("Livraison partielle : "+stock_inst+" T de feves "+((Feve)produit).getGamme()+" pour le CC n°"+contrat.getNumero());
			return stock_inst;
		}
	}
}
