package abstraction.eq3Producteur3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.ContratCadre;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur3VendeurContratCadre extends Producteur3VendeurBourse implements IVendeurContratCadre {
	//@youssef
	private LinkedList<ExemplaireContratCadre> contratsEnCours = new LinkedList<>();
	private SuperviseurVentesContratCadre superviseur;
	
	/**
	 * @author Arthur
	 * @param IProduit produit
	 * @return boolean
	 * La fonction renvoie si oui ou non, on veut vendre du produit proposer en CC (oui si feve HQ et MQ)
	 */
	public boolean vend(IProduit produit) {
		//On accepte les contrats cadres sur le HQ et MQ en V1
		if ((produit instanceof Feve)&&((((Feve)produit).getGamme() == Gamme.HQ)||(((Feve)produit).getGamme() == Gamme.MQ))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @author Youssef (modification Arthur)
	 * Initalise le super et invoque le superviseur
	 */
	public void initialiser() {
		super.initialiser();
		//On appelle le superviseur de la filiere
		superviseur = (SuperviseurVentesContratCadre) Filiere.LA_FILIERE.getActeur("Sup."+(SuperviseurVentesContratCadre.NB_SUPRVISEURS_CONTRAT_CADRE>1?SuperviseurVentesContratCadre.NB_SUPRVISEURS_CONTRAT_CADRE+"":"")+"CCadre");
	}
	
	/**
	 * @author Youssef
	 * Fait le next du super et lance des CC
	 */
	public void next() {
        super.next();
        //on lance de nouveaux contrats (a verifier)
        proposerContrats();
    }
	
	/**
	 * @author mammouYoussef
	 * Fonction qui lance des CC selon la feve et notre capacite et fournir
	 */
	public void proposerContrats() {
	
	    // Créer une liste de fèves de qualité MQ et HQ uniquement
	    List<Feve> feves = new ArrayList<Feve>();
	    for (Feve feve : Feve.values()) {
	        if (feve.getGamme() == Gamme.MQ || feve.getGamme() == Gamme.HQ) {
	            feves.add(feve);
	        }
	    }
	    for (Feve f : feves) { 
	    	//pour tous les acheteurs de chaque feves on propose un echeancier de 10 step
	        List<IAcheteurContratCadre> acheteurs = superviseur.getAcheteurs(f);
	        for (IAcheteurContratCadre acheteur : acheteurs) {
		        double quantiteDisponible = quantiteDisponiblePourNouveauContrat(f);
		        if (quantiteDisponible*10 > SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER) {
                    //on propose de livrer a chaque step la quantite qui nous reste apres livraison des autres CC
		        	Echeancier echeancier = new Echeancier(Filiere.LA_FILIERE.getEtape(), 10, quantiteDisponible); // Crée un échéancier avec des livraisons réparties sur 10 étapes (à modifier)
                    superviseur.demandeVendeur(acheteur, this, f, echeancier, this.cryptogramme, false); // Démarre la négociation
	            }
            }
	    }
	}
	
	
	/**
	 * @author mammouYoussef
	 * Nettoie la liste des contrats en cours, en éliminant ceux dont les obligations de livraison ont été entièrement satisfaites
	 */
	public void setContratsEnCours() {
	    LinkedList<ExemplaireContratCadre> contratsAConserver = new LinkedList<>();
	    for (ExemplaireContratCadre contrat : contratsEnCours) {
	        if (contrat.getQuantiteRestantALivrer() > 0) {
	            contratsAConserver.add(contrat);
	        }
	    }
	    contratsEnCours = contratsAConserver;
	}


	/**
	 * @author mammouYoussef (et modification Arthur)
	 * @param Feve f
	 * @return double quantiteDisponible
	 * Calcule et retourne la quantité disponible d'une fève spécifique pour de nouveaux contrats, en prenant en compte les engagements existants
	 */
	 private double quantiteDisponiblePourNouveauContrat(Feve f) {
	        double quantiteDisponible = 0.0; // Valeur par défaut
	        if (quantite().containsKey(f)) {
	        	//La quantite disponible de base correspond a ce que l'on produit
	            quantiteDisponible = quantite().get(f);
	        }

	        for (ExemplaireContratCadre contrat : contratsEnCours) {
	            if (contrat.getProduit().equals(f)) {
	            	//il faut ensuite enlever ce que l'on doit livrer pour avoir la quantite disponible pour d'autres CC
	                quantiteDisponible -= contrat.getQuantiteALivrerAuStep();
	            }
	        }
	        if (quantiteDisponible < 0) {
	        	//la quantite disponible ne peut pas etre negative
	        	quantiteDisponible = 0;
	        }
	        return quantiteDisponible;
	    }
	 
	 /**
	  * @author mammouYoussef (et modification Arthur)
	  * @param ExemplaireContratCadre contrat
	  * @return Echeancier nouvelEcheancier
	  * Propose un echeancier dans le but de satisfaire au mieux celui proposé par le vendeur tout en prenant en compte nos capacités à fournir ce qui est demandé
	  */
	 public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		    Feve f = (Feve) contrat.getProduit();
		    Echeancier echeancierPropose = contrat.getEcheancier();
		    Echeancier nouvelEcheancier = new Echeancier(echeancierPropose.getStepDebut());
		    //on estime la quantite disponible est similaire a chaque step en V1
		    double quantiteDisponible = quantiteDisponiblePourNouveauContrat(f);

		    for (int step = echeancierPropose.getStepDebut(); step <= echeancierPropose.getStepFin(); step++) {
		        double quantiteDemandee = echeancierPropose.getQuantite(step);

		        if (quantiteDisponible >= quantiteDemandee) {
		        	//si on peut fournir ce qui est demande, on le fait
		            nouvelEcheancier.ajouter(quantiteDemandee);
		        } else {
		        	//sinon on propose de fournir ce que l'on peut
		            nouvelEcheancier.ajouter(quantiteDisponible);
		        }
		    }
		    if (nouvelEcheancier.getQuantiteTotale()>= SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER) {
		        return nouvelEcheancier;
		    } else {
		        return null; // Retourner null si la quantite totale est trop faible
		    }
		}
	 
	
	
	/**
	 * @author mammouYoussef
	 * @param ExemplaireContratCadre contrat
	 * @return double prixBase
	 * Propose un prix de base du cacao en fonction de la feve du contrat
	 */
	public double propositionPrix(ExemplaireContratCadre contrat) {
	    IProduit produit = contrat.getProduit();
	    if (!(produit instanceof Feve)) { return 0;}
	    
	    Feve feve = (Feve) produit;
	    double prixBase=0;
	    //on fixe un prix de base selon la gamme
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
	 * @param ExemplaireContratCadre contrat
	 * @return double prix
	 * On propose un nouveau prix (potentiellement le meme) suite a la contre-proposition faite par l'acheteur
	 */
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


	/**
	 * @author Arthur
	 * @param ExemplaireContratCadre contrat
	 * Prend en compte le nouveau contrat conclu 
	 */
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		//CC conclu, on l'affiche dans les journaux et on met a jour les variables pour estimer la quantite disponible de feve
		this.journal_contrat_cadre.ajouter("Contrat cadre n°"+contrat.getNumero()+" avec "+contrat.getAcheteur().getNom()+" : "+contrat.getQuantiteTotale()+" T de "+contrat.getProduit()+" a "+contrat.getPrix()+" E/T");	
		this.contratsEnCours.add(contrat);
		this.setContratsEnCours();
	}

	/**
	 * @author Arthur
	 * @param Iproduit produit, double quantite, ExemplaireContratCadre contrat
	 * @return double quantiteLivrer
	 * Renvoie la quantite livrée 
	 */
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		double stock_inst = this.getQuantiteEnStock((Feve)produit, this.cryptogramme);
		if (quantite <= stock_inst) {
			//on verifie que l'on puisse fournir la quantite demande
			//il faut modifier les stocks suite a la vente
			this.setQuantiteEnStock((Feve)produit, stock_inst-quantite);
			this.journal_contrat_cadre.ajouter("Livraison totale : "+quantite+" T de feves "+((Feve)produit).getGamme()+" pour le CC n°"+contrat.getNumero());
			//on envoie ce que l'on a promis et on met a jour les variables
			ventefevecadre.put((Feve)contrat.getProduit(), quantite);
			return quantite;
		} else {
			//on ne peut pas tout fournir, on envoie tout le stock et met a jour les variables
			this.setQuantiteEnStock((Feve)produit, 0);
			this.journal_contrat_cadre.ajouter("Livraison partielle : "+stock_inst+" T de feves "+((Feve)produit).getGamme()+" pour le CC n°"+contrat.getNumero());
			ventefevecadre.put((Feve)contrat.getProduit(), stock_inst);
			return stock_inst;
		}
	}
}
