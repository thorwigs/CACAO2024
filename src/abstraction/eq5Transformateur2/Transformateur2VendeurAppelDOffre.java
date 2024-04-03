package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.appelDOffre.AppelDOffre;
import abstraction.eqXRomu.appelDOffre.IVendeurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur2VendeurAppelDOffre extends Transformateur2AcheteurBourse implements IVendeurAO {
	protected Journal journalAO;
	
	public OffreVente proposerVente(AppelDOffre offre) {
		return null;
	}

	
	public void notifierVenteAO(OffreVente propositionRetenue) {
		// TODO Auto-generated method stub
		
	}

	
	public void notifierPropositionNonRetenueAO(OffreVente propositionRefusee) {
		// TODO Auto-generated method stub
		
	}

}
