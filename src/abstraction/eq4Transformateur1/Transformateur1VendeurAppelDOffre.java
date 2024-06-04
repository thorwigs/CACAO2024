package abstraction.eq4Transformateur1;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.AppelDOffre;
import abstraction.eqXRomu.appelDOffre.IVendeurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.appelDOffre.SuperviseurVentesAO;
import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur1VendeurAppelDOffre extends Transformateur1VendeurCCadre implements IVendeurAO{
	private HashMap<ChocolatDeMarque, List<Double>> prixAO;
	/**
	 * @author Noémie_Grosset
	 */
	protected Journal journalAO;

	public Transformateur1VendeurAppelDOffre() {
		super();
		this.journalAO = new Journal(this.getNom()+" journal A.O.", this);
	}

	public void initialiser() {
		super.initialiser();
		this.prixAO = new HashMap<ChocolatDeMarque, List<Double>>();
		for (ChocolatDeMarque cm : this.stockChocoMarque.keySet()) {
			this.prixAO.put(cm, new LinkedList<Double>());
		}		
	}
	public double prixMoyen(ChocolatDeMarque cm) {
		List<Double> prix=prixAO.get(cm);
		if (prix.size()>0) {
			double somme =0.0;
			
			for (Double d : prix) {
				somme+=d;
			}
			return somme/prix.size();
		} else {
			return PRIX_DEFAUT.get(cm.getGamme());
		}
	}



	public OffreVente proposerVente(AppelDOffre offre) {
		// Vérification que l'offre est un chocolat de marque
		if (offre == null) {
	        return null;}
		IProduit p = offre.getProduit();
		if (!(p instanceof ChocolatDeMarque)) {
			return null;
		}
		// Vérification que c'est un produit que l'on vend
		ChocolatDeMarque cm = (ChocolatDeMarque)p;
		if (!(stockChocoMarque.keySet().contains(cm))) {
			return null;
		}
		
		// Vérification que l'on a suffisament en stock du chocolat par rapport aux
		if (this.stockChocoMarque.get(cm).getValeur() - this.demandeCC.get(cm.getGamme())*this.nombreMois < offre.getQuantiteT()) {
			return null;
		}
		
		// Si pas d'offre auparavant-> on reg la bourse pour donner un prix
		//System.out.println(prixAO.get(cm)+" ; "+cm);
		if (prixAO.get(cm).size()==0) {
			BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
			double px = bourse.getCours(Feve.F_MQ).getMax()*1.75;
			if (cm.getChocolat().getGamme()==Gamme.HQ) {
				px = bourse.getCours(Feve.F_HQ).getMax()*2.5;
			}
			return new OffreVente(offre, this, cm, px);
			
		// Sinon on met un prix 5% plus élevée que la moyenne de nos derniers prix
		} else {
			return new OffreVente(offre, this, cm, prixMoyen(cm)*1.05);
		}
	}

	public void notifierVenteAO(OffreVente propositionRetenue) {
		ChocolatDeMarque cm = (ChocolatDeMarque)(propositionRetenue.getProduit());
		double px = propositionRetenue.getPrixT();
		double quantite = propositionRetenue.getQuantiteT();
		prixAO.get(cm).add(px); // on fait comme si on avait accepte avec 5% d'augmentation afin que lors des prochains echanges on accepte des prix un peu plus eleves
		journalAO.ajouter("   Vente par AO de "+quantite+" T de "+cm+" au prix de  "+px);
		if (prixAO.get(cm).size()>10) {
			prixAO.get(cm).remove(0); // on ne garde que les dix derniers prix
		}
		if(cm.getGamme() == Gamme.HQ) {
			this.venteHQ.ajouter(this, quantite);
		} else {
			this.venteMQ.ajouter(this, quantite);
		}
	}


	public void notifierPropositionNonRetenueAO(OffreVente propositionRefusee) {
		ChocolatDeMarque cm = (ChocolatDeMarque)(propositionRefusee.getProduit());
		double px = propositionRefusee.getPrixT();
		double quantite = propositionRefusee.getQuantiteT();
		prixAO.get(cm).add(px*0.92); // on fait comme si on avait accepte avec 8% de baisse afin que lors des prochains echanges on fasse une meilleure offre
		journalAO.ajouter("   Echec de vente par AO de "+quantite+" T de "+cm+" au prix de  "+px);
		if (prixAO.get(cm).size()>10) {
			prixAO.get(cm).remove(0); // on ne garde que les dix derniers prix
		}
	}
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalAO);
		return jx;
	}
}
