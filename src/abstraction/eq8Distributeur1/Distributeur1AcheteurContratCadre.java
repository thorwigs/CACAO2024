package abstraction.eq8Distributeur1;

import java.awt.Color;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.IProduit;

public class Distributeur1AcheteurContratCadre extends Distributeur1Acteur implements IAcheteurContratCadre{
	private SuperviseurVentesContratCadre supCC;
	
	public Distributeur1AcheteurContratCadre() {
		
	}
	
	
	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		
	}

	public String getNom() {
		return (super.getNom());
	}

	public Color getColor() {
		return(super.getColor());
	}

	public String getDescription() {
		return(super.getDescription());
	}

	public void next() {
		super.next();
	}

	public List<Variable> getIndicateurs() {
		return(super.getIndicateurs());
	}

	public List<Variable> getParametres() {
		return(super.getParametres());
	}

	public List<Journal> getJournaux() {
		return(super.getJournaux());
	}

	public void setCryptogramme(Integer crypto) {
		super.setCryptogramme(crypto);	
	}

	public void notificationFaillite(IActeur acteur) {
		super.notificationFaillite(acteur);
		
	}


	public void notificationOperationBancaire(double montant) {
		super.notificationOperationBancaire(montant);
	}


	public List<String> getNomsFilieresProposees() {
		return(super.getNomsFilieresProposees());
	}

	public Filiere getFiliere(String nom) {
		return(super.getFiliere(nom));
	}

	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		return(super.getQuantiteEnStock(p, cryptogramme));
	}

	public boolean achete(IProduit produit) {
		return (produit.getType().equals("ChocolatDeMarque")
				&& this.données.get(produit)<1000);  // a redéfinir + ce qu'on va recevoir

	}

	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")) {
			return null;
		}
		
		Echeancier x = contrat.getEcheancier();
		if (x.getNbEcheances()>=24 && x.getNbEcheances()<=72) {
			if (this.données.get(contrat.getProduit())<1000) { // a redéfinir
//				if (contrat.getQuantiteTotale()>=1000 && )
			}
		}
		return null;
	}

	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

	public void receptionner(IProduit p, double quantiteEnTonnes, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

}
