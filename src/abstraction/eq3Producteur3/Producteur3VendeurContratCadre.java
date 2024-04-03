package abstraction.eq3Producteur3;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
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
	    double quantiteDisponible = this.getQuantiteEnStock(f, this.cryptogramme);
	    // Echeancier proposé par l'acheteur
	    Echeancier echeancierPropose = contrat.getEcheancier() ;
	    
	    // Si la quantité totale demandée dépasse la quantité disponible, 
	    // on divise la quantité disponible par le nombre d'échéances dans l'échéancier initial pour trouver la nouvelle quantité par échéance.
	    if (echeancierPropose.getQuantiteTotale() > quantiteDisponible) {
	        int nbEcheances = echeancierPropose.getNbEcheances();
	        double quantiteParEcheance = quantiteDisponible / nbEcheances;
	        
	        // Création d'un nouvel échéancier avec la nouvelle quantité
	        Echeancier nouvelEcheancier = new Echeancier(echeancierPropose.getStepDebut(), nbEcheances, quantiteParEcheance);
	        return nouvelEcheancier;
	    } else {
	        // Si la quantité demandée peut être couverte par le stock disponible,
	        // on accepte l'échéancier proposé sans modification.
	        return echeancierPropose;
	    }
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
		this.journal.ajouter("Contrat cadre n°"+contrat.getNumero()+" avec "+contrat.getAcheteur().getNom()+" : "+contrat.getQuantiteTotale()+" de "+contrat.getProduit()+" a "+contrat.getPrix()+" E");		
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
			this.journal.ajouter("Livraison totale : "+quantite+" feves "+((Feve)produit).getGamme()+" pour le CC n°"+contrat.getNumero());
			//on envoie ce que l'on a promis
			return quantite;
		} else {
			//on ne peut pas tout fournir, on envoie tout le stock
			this.setQuantiteEnStock((Feve)produit, 0);
			this.journal.ajouter("Livraison partielle : "+stock_inst+" feves "+((Feve)produit).getGamme()+" pour le CC n°"+contrat.getNumero());
			return stock_inst;
		}
	}
}
