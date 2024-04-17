package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur3VendeurContratCadre extends Producteur3VendeurBourse implements IVendeurContratCadre {
	private LinkedList<ExemplaireContratCadre> contratsEnCours = new LinkedList<>();
	@Override
	public boolean vend(IProduit produit) {
		//On accepte les contrats cadres sur le HQ et MQ
		if ((produit instanceof Feve)&&((((Feve)produit).getGamme() == Gamme.HQ)||(((Feve)produit).getGamme() == Gamme.MQ))) {
			return true;
		} else {
			return false;
		}
	}
	
	public void next() {
        super.next();
        SetContratsEnCours();
    }
	
	/**
	 * @author mammouYoussef
	 */
	//Nettoyer la liste des contrats en cours, en éliminant ceux dont les obligations de livraison ont été entièrement satisfaites
	public void SetContratsEnCours() {
	    LinkedList<ExemplaireContratCadre> contratsAConserver = new LinkedList<>();
	    for (ExemplaireContratCadre contrat : contratsEnCours) {
	        if (contrat.getQuantiteRestantALivrer() > 0) {
	            contratsAConserver.add(contrat);
	        }
	    }
	    contratsEnCours = contratsAConserver;
	}


	/**
	 * @author mammouYoussef
	 */
	//Calculer et retourner la quantité disponible d'une fève spécifique pour de nouveaux contrats, en prenant en compte les engagements existants
	 private double quantiteDisponiblePourNouveauContrat(Feve f) {
	        double quantiteDisponible = 0.0; // Valeur par défaut
	        if (quantite().containsKey(f)) {
	            quantiteDisponible = quantite().get(f);
	        }

	        for (ExemplaireContratCadre contrat : contratsEnCours) {
	            if (contrat.getProduit().equals(f)) {
	                quantiteDisponible -= contrat.getQuantiteRestantALivrer();
	            }
	        }
	        return quantiteDisponible;
	    }
	 
	 /**
		 * @author mammouYoussef
		 */
	
	 public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		    Feve f = (Feve) contrat.getProduit();
		    Echeancier echeancierPropose = contrat.getEcheancier();
		    Echeancier nouvelEcheancier = new Echeancier(echeancierPropose.getStepDebut());
		    double quantiteDisponible = quantiteDisponiblePourNouveauContrat(f);

		    for (int step = echeancierPropose.getStepDebut(); step <= echeancierPropose.getStepFin(); step++) {
		        double quantiteDemandee = echeancierPropose.getQuantite(step);
		        if (quantiteDemandee < SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER) {
	                return null;
		        }

		        if ( quantiteDisponible >= quantiteDemandee) {
		            // Si la quantité produite est suffisante pour l'échéance, ajouter cette quantité à l'échéancier
		            nouvelEcheancier.ajouter(quantiteDemandee);
		             // Ajouter le contrat à la liste des contrats en cours

		        } else {  	
		            if (quantiteDisponible > SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER) {
		                nouvelEcheancier.ajouter(quantiteDisponible);
		                
		            } else {
		                nouvelEcheancier = null; //// Si aucune quantité n'est disponible :( ne pas accepter le contrat
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
			ventefevecadre.put((Feve)contrat.getProduit(), quantite);
			return quantite;
		} else {
			//on ne peut pas tout fournir, on envoie tout le stock
			this.setQuantiteEnStock((Feve)produit, 0);
			this.journal_contrat_cadre.ajouter("Livraison partielle : "+stock_inst+" T de feves "+((Feve)produit).getGamme()+" pour le CC n°"+contrat.getNumero());
			ventefevecadre.put((Feve)contrat.getProduit(), stock_inst);
			return stock_inst;
		}
	}
}
