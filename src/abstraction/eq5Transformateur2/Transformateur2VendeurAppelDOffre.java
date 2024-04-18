package abstraction.eq5Transformateur2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.AppelDOffre;
import abstraction.eqXRomu.appelDOffre.IVendeurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;


/* Pour la V2 :
 * Rajouter un boolean avec le prix proposée dans Listeprix 
 * pour prendre un compte si la vente a réussi ou non pour tel prix 
 */

public class Transformateur2VendeurAppelDOffre extends Transformateur2AcheteurBourse implements IVendeurAO {
	private HashMap<ChocolatDeMarque, List<Double>> prixAO;  //dictionnaire ( key --> ChocolatDeMarque // value --> prix )
	protected Journal journalAO;
	
	/////////////////
	// Constructor //
	/////////////////
	/**
	 * Erwann
	 */
	public Transformateur2VendeurAppelDOffre() {
		super();
		this.journalAO = new Journal(this.getNom()+" journal A.O.", this);
	}
	
	///////////////////////////////////////////////////////////////////
	// Initialise et remplie le dico {cm : ListePrix de nos Offres}  //
	///////////////////////////////////////////////////////////////////
	/**
	 * Erwann
	 */
	public void initialiser() {
		super.initialiser();
		this.prixAO = new HashMap<ChocolatDeMarque, List<Double>>();
		for (ChocolatDeMarque cm : this.chocosProduits) {
			this.prixAO.put(cm, new LinkedList<Double>());
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	// Donne le prix moyen d'un ChocolatDeMarque pour les 10 dernières offres que l'on a fait //
	////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Erwann
	 */
	public double prixMoyen(ChocolatDeMarque cm) {
		List<Double> ListePrix = prixAO.get(cm);
		if (ListePrix.size()>0) {
			double somme =0.0;
			for (Double d : ListePrix) {
				somme+=d;
			}
			return somme/ListePrix.size();
		} else {
			return 0.0;
		}
	}
	
	//////////////////////////////////////////////////////////////
	// Proposition de vente (avec prix, qtté et type de produit)//
	//////////////////////////////////////////////////////////////
	/**
	 * Erwann
	 */
	public OffreVente proposerVente(AppelDOffre offre) {
		// On verifie d'abord que l'offre est un chocolat de marque
		IProduit p = offre.getProduit();
		if (!(p instanceof ChocolatDeMarque)) {
			return null;
		}
		// On verif que le produit fait bien parti de ceux que l'on vend
		ChocolatDeMarque cm = (ChocolatDeMarque)p;
		if (!(stockChocoMarque.keySet().contains(cm))) {
			return null;
		}
		// Si on n'a jamais fait d'offre --> on se base sur la bourse pour donner un prix
		if (prixAO.get(cm).size()==0) {
			Gamme gamme = cm.getGamme();
			BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
			double prix = bourse.getCours(Feve.F_MQ).getMax()*2.5;
			if (gamme == Gamme.BQ) {
				prix = bourse.getCours(Feve.F_BQ).getMax()*2.0;
			}
			return new OffreVente(offre, this, cm, prix);
		} 
		// Sinon on met un prix 3% plus chère que la moyenne de nos derniers prix
		else {
			return new OffreVente(offre, this, cm, prixMoyen(cm)*1.03);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// Notifs de si oui ou non la proposition est retenue  + Mise à jour JournalAO //	
	/////////////////////////////////////////////////////////////////////////////////
	/**
	 * Erwann
	 */
	public void next() {
		super.next();
		this.journalAO.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
	}
	/**
	 * Erwann
	 */
	public void notifierVenteAO(OffreVente propositionRetenue) {
		if(propositionRetenue.getProduit() instanceof ChocolatDeMarque & chocosProduits.contains(propositionRetenue.getProduit())) {   // On verif que le produit est bien un chocolat de marque et qu'il appartient aux produits que l'on vends
			ChocolatDeMarque cm = (ChocolatDeMarque)(propositionRetenue.getProduit());
			if (prixAO.get(cm)!=null) {
				double prix = propositionRetenue.getPrixT();
				double quantite_vendu = propositionRetenue.getQuantiteT();
				double quantite_initiale = stockChocoMarque.get(cm);
				stockChocoMarque.put(cm, quantite_initiale - quantite_vendu);  // modif des stocks si la propisition est retenue
				prixAO.get(cm).add(prix);
				journalAO.ajouter("  Vente par AO de "+quantite_vendu+" tonnes de "+cm+" au prix de "+prix);
				if (prixAO.get(cm).size()>10) {
					prixAO.get(cm).remove(0); // on ne garde que les dix derniers prix
				}
			}
		}
	}
	/**
	 * Erwann
	 */
	public void notifierPropositionNonRetenueAO(OffreVente propositionRefusee) {
		ChocolatDeMarque cm = (ChocolatDeMarque)(propositionRefusee.getProduit());
		double prix = propositionRefusee.getPrixT();
		double quantite = propositionRefusee.getQuantiteT();
		if (prixAO.get(cm)!=null) {
			prixAO.get(cm).add(prix*0.92); // on fait comme si on avait accepte avec 8% de baisse afin que lors des prochains echanges on fasse une meilleure offre
			journalAO.ajouter("   Echec de vente par AO de "+quantite+" tonnes de "+cm+" au prix de  "+prix);
			if (prixAO.get(cm).size()>10) {
				prixAO.get(cm).remove(0); // on ne garde que les dix derniers prix
			}
		}		
	}
	/**
	 * Erwann
	 */
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalAO);
		return jx;
	}
}
