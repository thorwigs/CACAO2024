package abstraction.eq8Distributeur1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;
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

/**
 * @author Clement E.
 */
public class Distributeur1AcheteurAppelOffre extends Distributeur1AcheteurContratCadre implements IAcheteurAO{
	private HashMap<ChocolatDeMarque, List<Double>> prixRetenus;
	private SuperviseurVentesAO supAO;
	protected Journal journalAO;
	
	/**
	 * @author Clement E.
	 */
	public Distributeur1AcheteurAppelOffre() {
		super();
		this.journalAO= new Journal (this.getNom() +"Journal AO", this);
	} 
	
	/**
	 * @author Clement E.
	 */
	public void initialiser() {
		super.initialiser();
		this.supAO = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));
		this.prixRetenus = new HashMap<ChocolatDeMarque, List<Double>>();
		for (ChocolatDeMarque cm : this.stock_Choco.keySet()) {
			this.prixRetenus.put(cm, new LinkedList<Double>());
		}
	}

	/**
	 * @author Clement E.
	 */
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
			journalAO.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LGREEN,"   refus de l'AO : pas assez d'argent sur le compte");
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
			journalAO.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LGREEN,"   refus de l'AO : produit pas correspondant à la demande");
			return null;
		}
		if ((solde<propositions.get(choisi).getPrixT()*propositions.get(choisi).getQuantiteT()))
				 {
			journalAO.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_LGREEN,"   refus de l'AO : pas assez d'argent sur le compte");
			return null;
				 }
		else if (propositions.get(choisi).getPrixT()>super.prix_a_perte(propositions.get(choisi).getProduit(),super.prix((ChocolatDeMarque)(propositions.get(choisi).getProduit())) )) {
			return null;
		}
		else {
			return propositions.get(choisi);
		}
	}
	
	/**
	 * @author Clement E.
	 */
	public List<Journal> getJournaux(){
		List<Journal> jx=super.getJournaux();
		jx.add(journalAO);
		return jx;
	}
	
	/**
	 * @author Clement E.
	 */
	public double achete_AO(IProduit produit) {
				double a = 0 ; 
				for (int i=0; i<contrat_en_cours.size(); i++) {
					if (contrat_en_cours.get(i).getProduit().equals(produit)) {
						a = a + contrat_en_cours.get(i).getQuantiteALivrerAuStep();
					}
				}
				
				if (produit.getType().equals("ChocolatDeMarque")
						&& this.stock_Choco.containsKey(produit)){
					
					double fluctuation = 1.0 ; 
					if (Filiere.LA_FILIERE.getMois()=="decembre") {
						 fluctuation = 1.10;
					} else if (Filiere.LA_FILIERE.getMois()=="fevrier" && Filiere.LA_FILIERE.getJour()==1) {
						fluctuation = 1.07;
					} else if (Filiere.LA_FILIERE.getMois()=="mai" && Filiere.LA_FILIERE.getJour()==1) {
						fluctuation = 1.10;
					} else if ((Filiere.LA_FILIERE.getMois()=="octobre" && Filiere.LA_FILIERE.getJour()==2)) {
						fluctuation = 1.03;
					}
					
					
					ChocolatDeMarque choco = (ChocolatDeMarque)produit;
					if (choco.getMarque()== "Chocoflow") {
						return ((capaciteDeVente*0.20*fluctuation)/chocoProduits.size())-(a+this.getQuantiteEnStock(choco,cryptogramme)) ;
					}
					if (choco.toString().contains("C_BQ")) {
						double x = (capaciteDeVente*0.32*fluctuation)/(this.nombreMarquesParType.get(choco.getChocolat())-1);
						return x -(a+this.getQuantiteEnStock(choco,cryptogramme)) ;
					}
					if (choco.toString().contains("C_MQ_E")) {
						double x = (capaciteDeVente*0.12*fluctuation)/this.nombreMarquesParType.get(choco.getChocolat());
						return x -(a+this.getQuantiteEnStock(choco,cryptogramme)) ;
					}
					if (choco.toString().contains("C_MQ")) {
						double x = (capaciteDeVente*0.12*fluctuation)/(this.nombreMarquesParType.get(choco.getChocolat())-1);
						return x -(a+this.getQuantiteEnStock(choco,cryptogramme)) ;
					}
					if (choco.toString().contains("C_HQ_BE")) {
						double x = (capaciteDeVente*0.04*fluctuation)/(this.nombreMarquesParType.get(choco.getChocolat())-1);
						return x -(a+this.getQuantiteEnStock(choco,cryptogramme)) ;
					}	
					if (choco.toString().contains("C_HQ_E")) {
						double x = (capaciteDeVente*0.08*fluctuation)/this.nombreMarquesParType.get(choco.getChocolat());
						return x -(a+this.getQuantiteEnStock(choco,cryptogramme)) ;
					}	
					if (choco.toString().contains("C_HQ")) {
						double x = (capaciteDeVente*0.12*fluctuation)/(this.nombreMarquesParType.get(choco.getChocolat())-1);
						return x -(a+this.getQuantiteEnStock(choco,cryptogramme)) ;
					}
				}
				return 0.0;
			}
	
	
	/**
	 * @author Clement E.
	 */
	public void next() {
		super.next();
		this.journalAO.ajouter("");
		this.journalAO.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_GREEN,"==================== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (ChocolatDeMarque choc : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (this.achete_AO(choc)>0) {
				double x = this.achete_AO(choc) + 100 ;
				OffreVente ov = supAO.acheterParAO(this,  cryptogramme, choc, x);
				journalAO.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_GREEN,"   Je lance un appel d'offre de "+x+" T de "+choc);
				if (ov!=null) {
					journalAO.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_GREEN,"   AO finalise : on ajoute "+x+" T de "+choc+" au stock");
					stock_Choco.put(choc,this.getQuantiteEnStock(choc,cryptogramme)+ x);
					totalStockChoco.ajouter(this, x, cryptogramme);
					Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), cryptogramme, "Coût Livraison", 0.05*ov.getPrixT());
				}
			}
		}
		this.journalAO.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_GREEN,"=================================");
		this.journalAO.ajouter("");

	}

}