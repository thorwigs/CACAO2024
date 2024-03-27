package abstraction.eq9Distributeur2;

import java.awt.Color;
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

public class Distributeur2ContratCadre extends Distributeur2Vente implements IAcheteurContratCadre{

	@Override
	public boolean achete(IProduit produit) {
		return produit.getType().equals("ChocolatDeMarque"); // verifier stock //acheter du Chocolat pour notre marque
	}

	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")) {
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
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")) {
			return 0.;
		}
		return contrat.getPrix();
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		
	}

	@Override
	public void receptionner(IProduit p, double quantiteEnTonnes, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		if (p.getType().equals("ChocolatDeMarque")) {
			this.getStockChocoMarque().put((ChocolatDeMarque) p, quantiteEnTonnes);
		}
	}

	
	
}
