package abstraction.eq9Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public abstract class Distributeur2ContratCadre extends Distributeur2Vente implements IAcheteurContratCadre{
	private SuperviseurVentesContratCadre supCC;
	private List<ExemplaireContratCadre> contratsEnCours;
	private List<ExemplaireContratCadre> contratsTermines;
	protected Journal journal_CC;
	
	public Distributeur2ContratCadre() {
		super();
		this.contratsEnCours=new LinkedList<ExemplaireContratCadre>();
		this.contratsTermines=new LinkedList<ExemplaireContratCadre>(); 
		this.journal_CC= new Journal(this.getNom()+" journal Contrat Cadre", this);
	}
	
	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}

	
	public void next() {
		super.next();
		this.journal_CC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (ChocolatDeMarque cm : this.stockChocoMarque.keySet()) { 
			if (this.stockChocoMarque.get(cm)-this.restantDu(cm)<20000 && this.totalStocksChocoMarque.getValeur(cryptogramme)<100000) {
				this.journal_CC.ajouter("Pas assez de "+cm+" en stock donc Contrat Cadre à lancer");
				double parStep = Math.max(200, (20000-this.stockChocoMarque.get(cm)-this.restantDu(cm))/12); // au moins 200
				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);
				List<IVendeurContratCadre> vendeurs = supCC.getVendeurs(cm);
				boolean est_contratPasse = false;
				for (IVendeurContratCadre vendeur : vendeurs ) {
					journal_CC.ajouter("   "+vendeur.getNom()+" retenu comme vendeur parmi "+vendeurs.size()+" vendeurs potentiels");
					ExemplaireContratCadre contrat = supCC.demandeAcheteur(this, vendeur, cm, e, cryptogramme, false);
					if (contrat==null) {
						journal_CC.ajouter(Color.RED, Color.white,"   echec des negociations, tentative suivante");
					} else {
						this.contratsEnCours.add(contrat);
						est_contratPasse=true;
						journal_CC.ajouter(Color.GREEN, vendeur.getColor(), "   contrat signe");
						journal_CC.ajouter("Nouveau Contrat Cadre : "+contrat.toString());
						break;
					}
				}
				if (!est_contratPasse) {
					journal_CC.ajouter("Contrat cadre a échoué car pas de vendeur");
				}
			}
		}
	}
	
	public double restantDu(ChocolatDeMarque cm) {
		double res=0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(cm)) {
				res+=c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}
	
	
	@Override
	public boolean achete(IProduit produit) {
		if (produit.getType().equals("ChocolatDeMarque")) {
			ChocolatDeMarque cm = (ChocolatDeMarque) produit;
			if (this.stockChocoMarque.get(cm)!=null) {
				return this.stockChocoMarque.get(cm)-this.restantDu(cm)<20000 && this.totalStocksChocoMarque.getValeur(cryptogramme)<100000;
			} else {
				return this.totalStocksChocoMarque.getValeur(cryptogramme)<100000;
			}
		} else {
			return false;
		}
		
	}

	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")) {
			return null;
		}
		Echeancier e = contrat.getEcheancier();
		LinkedList<Double> quantites = new LinkedList<Double>();
		boolean modif = false;
		ChocolatDeMarque cm = (ChocolatDeMarque) contrat.getProduit();
		for (int i=0; i<e.getNbEcheances(); i++) {
			if (e.getQuantite(e.getStepDebut()+i)>this.getCapaciteStockage()/12) {
				quantites.add(Math.max(200, (20000-this.stockChocoMarque.get(cm)-this.restantDu(cm))/12));
				modif=true;
			} else {
				quantites.add(e.getQuantite(e.getStepDebut()+i));
			}
		}
		if (modif) {
			Echeancier new_e = new Echeancier(e.getStepDebut(), quantites);
			return new_e;
		} else {
			return e;
		}
	}

	@Override
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")) {
			return 0.;
		}
		return contrat.getPrix();
	}
	
	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journal_CC.ajouter("Nouveau Contrat Cadre : "+contrat.toString());
		this.contratsEnCours.add(contrat);
	}

	@Override
	public void receptionner(IProduit p, double quantiteEnTonnes, ExemplaireContratCadre contrat) {
		this.journal_CC.ajouter("Livraison du produit "+quantiteEnTonnes+" tonnes de "+p+", issu du contrat #"+contrat.getNumero());
		
		if (p.getType().equals("ChocolatDeMarque")) {
			this.getStockChocoMarque().put((ChocolatDeMarque) p, quantiteEnTonnes);
			this.totalStocksChocoMarque.ajouter(this, quantiteEnTonnes, cryptogramme);
		}
		if (Filiere.LA_FILIERE.getEtape() == contrat.getEcheancier().getStepFin()) {
			this.contratsTermines.add(contrat);
			this.contratsEnCours.remove(contrat);
		}
	}

	
	
	public List<Journal> getJournaux() {
		List<Journal> res= super.getJournaux();
		res.add(this.journal_CC);
		return res;
	}
	
}
