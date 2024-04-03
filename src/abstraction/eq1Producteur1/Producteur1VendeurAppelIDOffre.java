package abstraction.eq1Producteur1;

import abstraction.eqXRomu.appelDOffre.AppelDOffre;
import abstraction.eqXRomu.appelDOffre.IVendeurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;

//J'ai ajoute ca car pourqoui pas? Ca nous aidera a remplir les offres des de
public class Producteur1VendeurAppelIDOffre extends Producteur1VendeurCCadre implements IVendeurAO{

	@Override
	public OffreVente proposerVente(AppelDOffre offre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifierVenteAO(OffreVente propositionRetenue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifierPropositionNonRetenueAO(OffreVente propositionRefusee) {
		// TODO Auto-generated method stub
		
	}

}
