package abstraction.eq5Transformateur2;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Banque;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Transformateur2MasseSalariale2 extends Transformateur2Acteur {
	protected int NbSalaries;
	protected double salaire;// 1salaire / step
	protected double coutLicenciement1Salarie; 
	protected double capaciteTransfo;// tonnes transformées par 1 salarié / step
	protected double coutAdjuvants;// cout des adjuvants pour 1 tonne / step
	protected double coutMachines;// cout des machines pour 1 tonne / step
	
	protected double moyProd;
	protected double totalProd;
	
	protected Journal JournalProduction;
	
	////////////////////////////////////////////
	// Constructor & Initialization of values //
	////////////////////////////////////////////
	/**
	 * @Erwann
	 */
	public Transformateur2MasseSalariale2() {
		super();
		this.JournalProduction=new Journal(this.getNom()+" journal Production", this);
	}
	/**
	* @Erwann
	*/
	public void initialiser() {
		super.initialiser();
		NbSalaries = 5000;
		salaire = 1000;
		coutLicenciement1Salarie = 4*salaire;
		capaciteTransfo = 3.7;
		coutAdjuvants = 370;
		coutMachines = 8;
		moyProd=0;
		totalProd=0;
	
		this.JournalProduction.ajouter("_____________Initialement_______________________________________");
		this.JournalProduction.ajouter("Nombre de salarié :"+NbSalaries);
		this.JournalProduction.ajouter("coût d'un salarié par step :"+salaire);
		this.JournalProduction.ajouter("coût de licenciement d'un salarié :"+coutLicenciement1Salarie);
		this.JournalProduction.ajouter("coût entretien/achat des machines par step :"+coutMachines);
		this.JournalProduction.ajouter("coût 1 tonne d'Adjuvants :"+coutAdjuvants);
		this.JournalProduction.ajouter("1 salarié peut transformer "+capaciteTransfo+" tonnes de fèves en chocolat par step");
		this.JournalProduction.ajouter("________________________________________________________________");
	}
	
	
	/////////////////////////////////////////////////////////////////////
	//  Méthodes pour la mise à jour des stocks et du calcul des couts //
	/////////////////////////////////////////////////////////////////////
	/**
	 * @Erwann
	 */
	public void Transformation(Feve f, double tonnes) {
		Chocolat c = Chocolat.get(f.getGamme(), f.isBio(), f.isEquitable());
		if (this.stockFeves.containsKey((Feve)f)){
			this.stockFeves.get((Feve)f).retirer(this, tonnes, this.cryptogramme); //Maj stock de feves 
			this.totalStocksFeves.retirer(this, tonnes, this.cryptogramme);
		}
		if (this.stockChoco.containsKey((Chocolat)c)){
			this.stockChoco.get((Chocolat) c).ajouter(this, tonnes, this.cryptogramme); //Maj stock choco
			this.totalStocksChoco.ajouter(this, tonnes, this.cryptogramme);
		}
	}
	/**
	 * @Erwann
	 */
	public double CoutTransformation(ChocolatDeMarque cm, double tonnes) {
		return tonnes*coutMachines + tonnes*(100-cm.getPourcentageCacao())*coutAdjuvants ;
	}
	
	
	
	
	
	////////////////////////////////////////////////////
	//                 Méthode Next                   //
	////////////////////////////////////////////////////
	/**
	 * @apiNote Détermine la capacité de production en fonction des salariés
	 * @apiNote Détermine si l'on embauche ou l'on licencie
	 * @apiNote Transforme les feves et paye les coûts
	 * @Erwann
	 * @Vincent
	 * @Victor
	 */
	public void next() {
		super.next();
		this.JournalProduction.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		
		////////////////////////////////////////////////////
		// Determination de la Capacité de Transformation //
		////////////////////////////////////////////////////
		double capaciteTransfoTotal = capaciteTransfo * NbSalaries;
		double coutMasseSalariale = 0;
		
		/* Stratégie d'embauche/licenciement : 
		 * --> On embauche si notre capacité de transformation ne permet pas de transformer plus de 30% de nos stocks.
		 * 	   On embauche au maximum 2000 salarié par step
		 * --> On licencie si notre capacité de transformation est 2 fois supérieur à nos stocks.
		 *     On licencie 30% de notre effectif
		 */
		
		if (capaciteTransfoTotal < 0.3 * this.totalStocksFeves.getValeur()) {
			int embauche =(int)((0.4 * this.totalStocksFeves.getValeur() - capaciteTransfoTotal) / capaciteTransfo);
			if (embauche> 2000){
				embauche=2000;
			}
			this.NbSalaries += embauche;
			this.JournalProduction.ajouter("On embauche"+embauche+"personnes");
			coutMasseSalariale = NbSalaries * salaire;

		} else if (capaciteTransfoTotal > 2 * this.totalStocksFeves.getValeur()) {
			int licencié = (int) (0.3 * NbSalaries);
			this.NbSalaries -= licencié;
			this.JournalProduction.ajouter("On licencie"+licencié+"personnes");
			coutMasseSalariale = NbSalaries * salaire + licencié * coutLicenciement1Salarie;
			
		} else {
			this.JournalProduction.ajouter("Aucune embauche, ni licenciement");
			coutMasseSalariale = NbSalaries * salaire;
		}
		
		// Paiement des coût de la masse salariale
		this.JournalProduction.ajouter("Nbr salariés : "+NbSalaries);
		this.JournalProduction.ajouter("cout Masse Salariale : "+coutMasseSalariale);
		Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût MS", coutMasseSalariale );
		
		// Recalcul de la Capacité de Transformation après Embauche/Licenciement
		capaciteTransfoTotal = capaciteTransfo * NbSalaries;
		this.JournalProduction.ajouter("Capacité de Transformation :"+(capaciteTransfoTotal)+"tonnes");
		
		
		
		////////////////////////////////////////////////////
		//           Transformation des Fèves             //
		////////////////////////////////////////////////////
		double TransfoTotal = 0;
		
		/* Stratégie de transformation :
		 * --> La capacité de Transformation totale est repartie au prorata des fèves en stock
		 */
		
		// Création d'un HashMap contenant la répartition de chaque fève en stock
		HashMap<Feve, Double> repartition = new HashMap<Feve, Double>();
		for (Feve f : lesFeves) {
			repartition.put(f, this.stockFeves.get((Feve)f).getValeur() / this.totalStocksFeves.getValeur());
		}
		
		// Transformation des feves avec la méthode "Transformation (Feve, tonnes)" qui mert à jour les stocks
		for (Feve f : lesFeves) {
			double TonnesTranfo = capaciteTransfoTotal * repartition.get(f);
			Transformation(f, TonnesTranfo);
			TransfoTotal += TonnesTranfo;
		}
		this.JournalProduction.ajouter("Tonnes de feves transformées : "+TransfoTotal);
		
		// Calcul des cout de Transformation avec la méthode "CoutTransformation(ChocolatDeMarque, tonnes)"
		double coutTransfoTotal = 0;
		for (ChocolatDeMarque cm : chocosProduits) {
			if (cm.getGamme()!= Gamme.HQ) {
				double t = VariationStockChocoMarque.get(cm);
				coutTransfoTotal += this.CoutTransformation(cm,t);	
			}
		}
		this.JournalProduction.ajouter("Coût de la transformation : "+coutTransfoTotal);
		Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût Transformation" , coutTransfoTotal);


		
		////////////////////////////////////////////////////
		//       Calcul de la moyenne de production       //
		////////////////////////////////////////////////////
		this.totalProd += TransfoTotal;
		this.moyProd = this.totalProd/(Filiere.LA_FILIERE.getEtape()+1);
		this.JournalProduction.ajouter("Production moyenne de l'acteur : "+moyProd+" tonnes/step");	
	}
	
	
	/**
	 * @Erwann
	 */
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(JournalProduction);
		return jx;
	}
}
