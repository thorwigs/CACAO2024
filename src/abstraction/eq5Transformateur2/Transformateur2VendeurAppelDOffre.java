package abstraction.eq5Transformateur2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.AppelDOffre;
import abstraction.eqXRomu.appelDOffre.IVendeurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur2VendeurAppelDOffre extends Transformateur2AcheteurBourse implements IVendeurAO {
	private HashMap<ChocolatDeMarque, List<Double>> prixAO;  //dictionnaire ( key --> ChocolatDeMarque // value --> prix )
	protected Journal journalAO;
	
	
	/////////////////
	// Constructor //
	/////////////////
	public Transformateur2VendeurAppelDOffre() {
		super();
		this.journalAO = new Journal(this.getNom()+" journal A.O.", this);
	}
	
	
	/////////////////////////////////////////////
	// Initialise et remplie le dico {c:prix} //
	/////////////////////////////////////////////
	public void initialiser() {
		super.initialiser();
		this.prixAO = new HashMap<ChocolatDeMarque, List<Double>>();
		for (ChocolatDeMarque cm : this.chocosProduits) {
			this.prixAO.put(cm, new LinkedList<Double>());
		}
	}
	
	
	////////////////////////////////////////////////////
	// Donne le prix moyen pour le ChocolatDeMarque c //
	////////////////////////////////////////////////////
	public double prixMoyen(ChocolatDeMarque c) {
		List<Double> prix=prixAO.get(c);
		if (prix.size()>0) {
			double somme =0.0;
			
			for (Double d : prix) {
				somme+=d;
			}
			return somme/prix.size();
		} else {
			return 0.0;
		}
	}
	
	
	//////////////////////////////////////////////////////////////
	// Proposition de vente (avec prix, qtté et type de produit //
	//////////////////////////////////////////////////////////////
	public OffreVente proposerVente(AppelDOffre offre) {
		//à faire : choisir et verifier le produit qu'on vend (verif stock)
		//à faire : choisir le prix de vente en fonciton du prix du marché 
		//			(prix moyen par rapport aux autres marques mais aussi au BQ et au MQ
		return new OffreVente(offre, this, offre.getProduit(), 1000); //Prix Arbitraire = 1000
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////
	// Notifs de si oui ou non la proposition est retenue  + Mise à jour JournalAO //	
	/////////////////////////////////////////////////////////////////////////////////
	public void next() {
		super.next();
		this.journalAO.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
	}
	
	public void notifierVenteAO(OffreVente propositionRetenue) {
		if(propositionRetenue.getProduit() instanceof ChocolatDeMarque) {
			ChocolatDeMarque cm =(ChocolatDeMarque)(propositionRetenue.getProduit());
			if (prixAO.get(cm)!=null) {
				double px = propositionRetenue.getPrixT();
				double quantite = propositionRetenue.getQuantiteT();
				prixAO.get(cm).add(px); // on fait comme si on avait accepte avec 5% d'augmentation afin que lors des prochains echanges on accepte des prix un peu plus eleves
				journalAO.ajouter("   Vente par AO de "+quantite+" T de "+cm+" au prix de  "+px);
				if (prixAO.get(cm).size()>10) {
					prixAO.get(cm).remove(0); // on ne garde que les dix derniers prix
				}
			}
		}
	}
	public void notifierPropositionNonRetenueAO(OffreVente propositionRefusee) {
		ChocolatDeMarque cm = (ChocolatDeMarque)(propositionRefusee.getProduit());
		double px = propositionRefusee.getPrixT();
		double quantite = propositionRefusee.getQuantiteT();
		if (prixAO.get(cm)!=null) {
			prixAO.get(cm).add(px*0.92); // on fait comme si on avait accepte avec 8% de baisse afin que lors des prochains echanges on fasse une meilleure offre
			journalAO.ajouter("   Echec de vente par AO de "+quantite+" T de "+cm+" au prix de  "+px);
			if (prixAO.get(cm).size()>10) {
				prixAO.get(cm).remove(0); // on ne garde que les dix derniers prix
			}
		}		
	}
	
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalAO);
		return jx;
	}

}
