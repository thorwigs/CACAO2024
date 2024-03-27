package abstraction.eq7Transformateur4;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur4VendeurContratCadre extends Transformateur4AcheteurContratCadre implements IVendeurContratCadre {

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
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

}
