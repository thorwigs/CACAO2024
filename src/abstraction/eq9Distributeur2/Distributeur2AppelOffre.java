package abstraction.eq9Distributeur2;

import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.appelDOffre.SuperviseurVentesAO;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public abstract class Distributeur2AppelOffre extends Distributeur2ContratCadre implements IAcheteurAO {
	protected Journal journal_AO;
	public Distributeur2AppelOffre() {
		super();
		this.journal_AO= new Journal(this.getNom()+" journal Appel d'offre", this);
	}
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

	public void FaireAppelDOffre() {
		for (ChocolatDeMarque chocolat : this.stockChocoMarque.keySet()) {
			if (this.stockChocoMarque.get(chocolat)<=0) {
				Double quantite = Filiere.LA_FILIERE.getVentes(chocolat, -24)*0.5;
				((SuperviseurVentesAO) Filiere.LA_FILIERE.getActeur("Sup.AO")).acheterParAO(this,this.cryptogramme,chocolat,quantite);
			}
		}
	}
	
	public List<Journal> getJournaux() {
		List<Journal> res= super.getJournaux();
		res.add(this.journal_AO);
		return res;
	}
	
}
