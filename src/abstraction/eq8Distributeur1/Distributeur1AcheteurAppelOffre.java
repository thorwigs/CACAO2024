package abstraction.eq8Distributeur1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.appelDOffre.SuperviseurVentesAO;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Distributeur1AcheteurAppelOffre extends Distributeur1AcheteurContratCadre implements IAcheteurAO{
	private HashMap<ChocolatDeMarque, List<Double>> prixRetenus;
	private SuperviseurVentesAO supAO;
	protected Journal journalAO;
	
	public Distributeur1AcheteurAppelOffre() {
		super();
		this.journalAO= new Journal (this.getNom() +"Journal AO", this);
	}
	public void initialiser() {
		super.initialiser();
		this.supAO = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));
		this.prixRetenus = new HashMap<ChocolatDeMarque, List<Double>>();
		for (ChocolatDeMarque cm : this.stock_Choco.keySet()) {
			this.prixRetenus.put(cm, new LinkedList<Double>());
		}
	}
	
	
	public void next() {
		super.next();
	}
	
	
	public OffreVente choisirOV(List<OffreVente> propositions) {
		double solde = Filiere.LA_FILIERE.getBanque().getSolde(this, cryptogramme);
		int moins_cher_total=0;
		for (int i=0; i<propositions.size();i++) {
			if (propositions.get(moins_cher_total).getPrixT()*propositions.get(moins_cher_total).getQuantiteT()>propositions.get(i).getPrixT()*propositions.get(i).getQuantiteT()) {
				moins_cher_total=i;
			}
		}
		if ((solde<propositions.get(moins_cher_total).getPrixT()*propositions.get(moins_cher_total).getQuantiteT())
				&& (solde<propositions.get(0).getPrixT()*propositions.get(0).getQuantiteT())) {
			journalAO.ajouter("   refus de l'AO : pas assez d'argent sur le compte");
			return null;
		}
		int choisi=-1; // permet de connaître la proposition choisi à la fin, la moins chere, ou renverra -1 si pas d'offre correspondante
		int parcourir=0; //permet de parcourir la liste des propositions pour trouver la bonne
		while(choisi==-1 && parcourir<propositions.size()) {
			if (propositions.get(0).getOffre().getProduit().equals(propositions.get(parcourir).getProduit())!=true) {
				parcourir++;
			}
			else {
				choisi=parcourir;
			}
		}
		if (choisi==-1) {
			journalAO.ajouter("   refus de l'AO : produit pas correspondant à la demande");
			return null;
		}
		if ((solde<propositions.get(choisi).getPrixT()*propositions.get(choisi).getQuantiteT()))
				 {
			journalAO.ajouter("   refus de l'AO : pas assez d'argent sur le compte");
			return null;
				 }
		else {
			return propositions.get(choisi);
		}
	}
	
	public List<Journal> getJournaux(){
		List<Journal> jx=super.getJournaux();
		jx.add(journalAO);
		return jx;
	}
	
	

}
