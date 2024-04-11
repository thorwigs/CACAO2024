package abstraction.eq8Distributeur1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.appelDOffre.SuperviseurVentesAO;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;

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
		this.journalAO.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (ChocolatDeMarque choc : Filiere.LA_FILIERE.getChocolatsProduits()) {
			System.out.println("2"+this.achete(choc));
			if (this.achete(choc)) {
				double x = 1000 ;
				OffreVente ov = supAO.acheterParAO(this,  cryptogramme, choc, x);
				journalAO.ajouter("   Je lance un appel d'offre de "+x+" T de "+choc);
				if (ov!=null) {
					journalAO.ajouter("   AO finalise : on ajoute "+x+" T de "+choc+" au stock");
					stock_Choco.put(choc, x);
					totalStockChoco.ajouter(this, x, cryptogramme);
				}
			
			}
			
		}
		this.journalAO.ajouter("=================================");
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
	
	public boolean achete(IProduit produit) {
				double a = 0 ; 
				for (int i=0; i<contrat_en_cours.size(); i++) {
					if (contrat_en_cours.get(i).getProduit().equals(produit)) {
						a = a + contrat_en_cours.get(i).getQuantiteRestantALivrer();
					}
				}


				return (produit.getType().equals("ChocolatDeMarque"));
		//				&& this.stock_Choco.containsKey(produit)
	//					&& 0 < this.prevision(produit, 24) - this.stock_Choco.get(produit) - a 
//						&& this.prevision(produit, 24) - this.stock_Choco.get(produit) - a <= 1000); 

			}
	

	public double prevision (ChocolatDeMarque p,int b) {
		double d = 0.0;
		int a = Filiere.LA_FILIERE.getEtape();
		for (int i =a-b; i<a ; i++ ) {
			d = d + Filiere.LA_FILIERE.getVentes(p,i);
		}
		d = d * ((Filiere.LA_FILIERE.getIndicateur("C.F. delta annuel max conso").getValeur() + Filiere.LA_FILIERE.getIndicateur("C.F. delta annuel min conso").getValeur())/2);
		return d ; 
	}	

}