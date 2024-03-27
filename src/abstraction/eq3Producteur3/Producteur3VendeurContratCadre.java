package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur3VendeurContratCadre extends Producteur3VendeurBourse implements IVendeurContratCadre {

	@Override
	public boolean vend(IProduit produit) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double propositionPrix(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}
}
