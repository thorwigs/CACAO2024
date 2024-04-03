package abstraction.eq9Distributeur2;

import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;

public abstract class Distributeur2AppelOffre extends Distributeur2ContratCadre implements IAcheteurAO {

	@Override
	public OffreVente choisirOV(List<OffreVente> propositions) {
		Double rapportmin=Double.MAX_VALUE;
		OffreVente meilleureProp = null;
		HashMap<OffreVente, Double> PropositionQuantitePrix = new HashMap<OffreVente, Double>();
		for (OffreVente proposition : propositions) {
			Double QuantitePrix =proposition.getQuantiteT()/ proposition.getPrixT();
			PropositionQuantitePrix.put(proposition, QuantitePrix);
			
			if (QuantitePrix<rapportmin) {
				meilleureProp = proposition;
			}
		}
		this.journal.ajouter(PropositionQuantitePrix.toString());
		return meilleureProp;
	}

	
}
