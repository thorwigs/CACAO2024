package abstraction.eq9Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;

public abstract class Distributeur2ContratCadre extends Distributeur2Vente implements IAcheteurContratCadre{

	protected Journal journal_CC;
	
	public Distributeur2ContratCadre() {
		super();
		this.journal_CC= new Journal(this.getNom()+" journal Contrat Cadre", this);
	}
	
	@Override
	public boolean achete(IProduit produit) {
		return produit.getType().equals("ChocolatDeMarque") || produit.getType().equals("Chocolat"); // verifier stock 
	}

	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque") || !contrat.getProduit().getType().equals("Chocolat")) {
			return null;
		}
		Echeancier e = contrat.getEcheancier();
		LinkedList<Double> quantites = new LinkedList<Double>();
		boolean modif = false;
		for (int i=0; i<e.getNbEcheances(); i++) {
			if (e.getQuantite(e.getStepDebut()+i)>this.getCapaciteStockage()) {
				quantites.add((double)this.getCapaciteStockage());
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
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque") || !contrat.getProduit().getType().equals("Chocolat")) {
			return 0.;
		}
		return contrat.getPrix();
	}
	
	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.getJournaux().get(1).ajouter("Nouveau Contrat Cadre : "+contrat.toString());
	}

	@Override
	public void receptionner(IProduit p, double quantiteEnTonnes, ExemplaireContratCadre contrat) {
		this.getJournaux().get(1).ajouter("Livraison du produit "+quantiteEnTonnes+" tonnes de "+p+", issu du contrat #"+contrat.getNumero());
		
		if (p.getType().equals("ChocolatDeMarque")) {
			this.getStockChocoMarque().put((ChocolatDeMarque) p, quantiteEnTonnes);
		}
		if (p.getType().equals("Chocolat")) {
			
		}
	}

	
	
	public List<Journal> getJournaux() {
		List<Journal> res= super.getJournaux();
		res.add(this.journal_CC);
		return res;
	}
	
}
