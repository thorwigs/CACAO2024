package abstraction.eq5Transformateur2;
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
	protected double salaire;//salaire par step
	protected double coutLicenciement1Salarie;
	protected double capaciteTransfo;// tonnes transformées par 1 salarié par step
	protected double coutAdjuvants;//cout des adjuvants pour 1 tonne de chocolat
	protected double coutMachines;//cout des machines pour 1 tonne de chocolat
	
	protected double moyProd;
	protected double totalProd;
	
	protected Journal JournalMasseSalariale;
	
	////////////////////////////////////////////
	// Constructor & Initialization of values //
	////////////////////////////////////////////
	/**
	 * @Erwann
	 */
	public Transformateur2MasseSalariale2() {
		super();
		this.JournalMasseSalariale=new Journal(this.getNom()+" journal MS", this);
	}
	/**
	* @Erwann
	*/
	public void initialiser() {
		super.initialiser();
		NbSalaries = 100;
		salaire = 2000;
		coutLicenciement1Salarie = 4*salaire;
		capaciteTransfo = 3.7;
		coutAdjuvants = 370;
		coutMachines = 8;
		moyProd=0;
		totalProd=0;
	
		this.JournalMasseSalariale.ajouter("_____________Initialement_______________________________________");
		this.JournalMasseSalariale.ajouter("Nombre de salarié :"+NbSalaries);
		this.JournalMasseSalariale.ajouter("coût d'un salarié par step :"+salaire);
		this.JournalMasseSalariale.ajouter("coût de licenciement d'un salarié :"+coutLicenciement1Salarie);
		this.JournalMasseSalariale.ajouter("coût entretien/achat des machines par step :"+coutMachines);
		this.JournalMasseSalariale.ajouter("coût 1 tonne d'Adjuvants :"+coutAdjuvants);
		this.JournalMasseSalariale.ajouter("1 salarié peut transformer "+capaciteTransfo+" tonnes de fèves en chocolat par step");
		this.JournalMasseSalariale.ajouter("________________________________________________________________");
	}
	
	
	////////////////////////////////////////////
	//  //
	////////////////////////////////////////////
		
		
		
		
	public void Transformation(Feve f, double tonnes) {
		Chocolat c = Chocolat.get(f.getGamme(), f.isBio(), f.isEquitable());
		if (this.stockFeves.containsKey((Feve)f)){
			this.stockFeves.get((Feve)f).retirer(this, tonnes, this.cryptogramme); //Maj stock de feves 
		}
		if (this.stockChoco.containsKey((Chocolat)c)){
			this.stockChoco.get((Chocolat) c).ajouter(this, tonnes, this.cryptogramme); //Maj stock choco
		}
	}
	
	/**
	 * @Erwann
	 * @param f
	 * @return la repartition de la Feve f dans le stock total de fèves
	 */
	public double Repartition(Feve f) {
		return (this.stockFeves.get((Feve)f).getValeur() / this.totalStocksFeves.getValeur());
	}
	
	public double CoutTransformation(ChocolatDeMarque cm, double tonnes) {
		return tonnes*coutMachines + tonnes*(100-cm.getPourcentageCacao())*coutAdjuvants ;
	}
	
	/**
	 * @Erwann
	 * @Vincent
	 * @Victor
	 */
	public void next() {
		super.next();
		this.JournalMasseSalariale.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		
		double capaciteTransfoTotal = capaciteTransfo * NbSalaries;
		double coutMasseSalariale = 0;
		
		if (capaciteTransfoTotal < 0.4 * this.totalStocksFeves.getValeur()) {
			int embauche =(int)((0.4*this.totalStocksFeves.getValeur() - capaciteTransfoTotal) / capaciteTransfo);
			if (embauche> 1000){
				embauche=1000;
			}
			this.NbSalaries += embauche;
			this.JournalMasseSalariale.ajouter("On embauche"+embauche+"personnes");
			coutMasseSalariale = NbSalaries * salaire;

		} else if (capaciteTransfoTotal > 4 * this.totalStocksFeves.getValeur()) {
			int licencié = (int) (0.1 * NbSalaries);
			this.NbSalaries -= licencié;
			this.JournalMasseSalariale.ajouter("On licencie"+licencié+"personnes");
			coutMasseSalariale = NbSalaries * salaire + licencié * coutLicenciement1Salarie;
			
		} else {
			this.JournalMasseSalariale.ajouter("Aucune embauche, ni licenciement");
			coutMasseSalariale = NbSalaries * salaire;
		}
		this.JournalMasseSalariale.ajouter("Nbr salariés : "+NbSalaries);
		
		// Paiement des coût de la masse salariale
		Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût MS", coutMasseSalariale );
		
		double NewCapaciteTransfoTotal = capaciteTransfo * NbSalaries;
		this.JournalMasseSalariale.ajouter("Capacité de Transfo Total :"+(NewCapaciteTransfoTotal)+"tonnes");
		
		
		double TransfoTotal = 0;
		System.out.println(lesFeves);
		for (Feve f : lesFeves) {
			double TonnesTranfo = NewCapaciteTransfoTotal * Repartition(f);
			Transformation(f, TonnesTranfo);
			TransfoTotal += TonnesTranfo;
		}
		this.JournalMasseSalariale.ajouter("Tonnes de feves transformées : "+TransfoTotal);
		
		
		
		double coutTransfoTotal = 0;
		for (ChocolatDeMarque cm : chocosProduits) {
			if (cm.getGamme()!= Gamme.HQ) {
				double t = VariationStockChocoMarque.get(cm);
				coutTransfoTotal += this.CoutTransformation(cm,t);	
			}
		}
		this.JournalMasseSalariale.ajouter("Coût de la transformation : "+coutTransfoTotal);
		Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût Transformation" , coutTransfoTotal);

		
		this.totalProd += TransfoTotal;
		this.moyProd = this.totalProd/(Filiere.LA_FILIERE.getEtape()+1);
		this.JournalMasseSalariale.ajouter("Production moyenne de l'acteur : "+moyProd+" tonnes/step");	
	}
	
	
	/**
	 * @Erwann
	 */
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(JournalMasseSalariale);
		return jx;
	}
}
