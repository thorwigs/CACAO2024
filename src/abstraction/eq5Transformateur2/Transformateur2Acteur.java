package abstraction.eq5Transformateur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.filiere.IMarqueChocolat;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur2Acteur implements IActeur,IMarqueChocolat, IFabricantChocolatDeMarque {
	
	protected Journal journal;
	protected Journal JournalProduction;
	protected int cryptogramme;
	
	protected List<Feve> lesFeves;
	protected List<Chocolat> lesChocolats;
	protected List<ChocolatDeMarque>chocosProduits;
	protected List<ChocolatDeMarque> chocolatsFusion;
	
	protected HashMap<Feve, Variable> stockFeves;
	protected HashMap<Chocolat, Variable> stockChoco;
	protected HashMap<ChocolatDeMarque, Variable> stockChocoMarque;
	protected HashMap<ChocolatDeMarque, Double> VariationStockChocoMarque; // pour le calcul des coûts de transfo
	protected HashMap<Feve, HashMap<Chocolat, Double>> pourcentageTransfo; // dictionnaire de dictionnaire [feve : [Type chocolat : % cacao ]]
	
	protected Variable totalStocksFeves; // La quantite totale de stock de feves 
	protected Variable totalStocksChoco; // La qualntite totale de stock de chocolat 
	protected Variable totalStocksChocoMarque; // La quantite totale de stock de chocolat de marque 
	
	protected int NbSalaries;
	protected double salaire; // 1salaire / step
	protected double coutLicenciement1Salarie; 
	protected double capaciteTransfo; // tonnes transformées par 1 salarié / step
	
	protected double coutAdjuvants; // cout des adjuvants pour 1 tonne / step
	protected double coutMachines; // cout des machines pour 1 tonne / step
	protected double coutStockage; // cout stockage pour 1 tonne / step
	
	protected double moyProd; // moyenne de production de l'acteur
	protected double totalProd; // qtté total transformée / produite
	
	protected static final double STOCKINITIAL = 50000.0;
	
	
	
	////////////////////////////////////////////
	//      Constructor & Initialization      //
	////////////////////////////////////////////
	/**
	 * @Robin 
	 */
	public Transformateur2Acteur() {
		this.journal = new Journal(this.getNom()+" journal", this);
		this.JournalProduction=new Journal(this.getNom()+" journal Production", this);
		this.totalStocksFeves = new Variable("Eq5TStockFeves", "<html>Quantite totale de feves en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.totalStocksChoco = new Variable("Eq5TStockChoco", "<html>Quantite totale de chocolat en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.totalStocksChocoMarque = new VariablePrivee("Eq5TStockChocoMarque", "<html>Quantite totale de chocolat de marque en stock</html>",this, 0.0, 1000000.0, 0.0);
		
		this.lesFeves = new LinkedList<Feve>();
		this.journal.ajouter("Les Feves sont :");
		for (Feve f : Feve.values()) {
			if (f.getGamme()!=Gamme.HQ) {
			this.lesFeves.add(f);
			this.journal.ajouter("   - "+f);
			}
		}
		this.stockFeves=new HashMap<Feve,Variable>();
		for (Feve f : this.lesFeves) {
			if (f.getGamme()!=Gamme.HQ) {
				this.stockFeves.put(f, new Variable("Eq5Stock "+f, this, STOCKINITIAL));
				this.journal.ajouter("ajout de "+STOCKINITIAL+" tonnes de : "+f+" au stock total de fèves // stock total : "+this.totalStocksFeves.getValeur(this.cryptogramme));
			}
		}
		
		this.lesChocolats = new LinkedList<Chocolat>();
		this.journal.ajouter("Nos Chocolats sont :");
		for (Chocolat c : Chocolat.values()) {
			if (c.getGamme()!=Gamme.HQ) {
				this.lesChocolats.add(c);
				this.journal.ajouter("   - "+c);
			}
		}
		this.stockChoco=new HashMap<Chocolat,Variable>();
		for (Chocolat c : this.lesChocolats) {
			this.stockChoco.put(c, new Variable("Eq5Stock "+c, this, STOCKINITIAL));
			this.journal.ajouter("ajout de "+STOCKINITIAL+" tonnes de : "+c+" au stock total de Chocolat // stock total : "+this.totalStocksChoco.getValeur(this.cryptogramme));
		}
	}
	
	/**
	 * @Robin 
	 * @Erwann
	 */
	public void initialiser() {

		this.totalStocksFeves.ajouter(this, this.lesFeves.size()*STOCKINITIAL, this.cryptogramme);
		this.totalStocksChoco.ajouter(this, this.lesChocolats.size()*STOCKINITIAL, this.cryptogramme);

		// Initialisation des HashMap
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
		this.journal.ajouter("Les Chocolats de marque sont :");
		for (ChocolatDeMarque cm : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if ((Filiere.LA_FILIERE.getMarquesDistributeur().contains(cm.getMarque()) & cm.getGamme()!=Gamme.HQ )  || cm.getMarque().equals("CacaoFusion")){
				this.chocosProduits.add(cm);
				this.journal.ajouter("   - "+cm);
			}
		}
		this.stockChocoMarque = new HashMap<ChocolatDeMarque,Variable>();
		for (ChocolatDeMarque cm : this.chocosProduits) {
			this.stockChocoMarque.put(cm, new Variable("Eq5Stock "+cm, this, STOCKINITIAL));
			this.totalStocksChocoMarque.ajouter(this, STOCKINITIAL, this.cryptogramme);
			this.journal.ajouter("ajout de "+STOCKINITIAL+" tonnes de : "+cm+" au stock total de Chocolat de marque // stock total : "+this.totalStocksChocoMarque.getValeur(this.cryptogramme));
		}
		this.VariationStockChocoMarque = new HashMap<ChocolatDeMarque,Double>();
		for (ChocolatDeMarque cm : this.chocosProduits) {
			this.VariationStockChocoMarque.put(cm, 0.0);
		
		}
		
		// Remplissage de pourcentageTransfo avec 0.1% de plus de cacao que le seuil minimal
		this.pourcentageTransfo = new HashMap<Feve, HashMap<Chocolat, Double>>();
		this.pourcentageTransfo.put(Feve.F_HQ_BE, new HashMap<Chocolat, Double>());
		double conversion = 0.1 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao HQ").getValeur())/100.0;
		this.pourcentageTransfo.get(Feve.F_HQ_BE).put(Chocolat.C_HQ_BE, conversion);// la masse de chocolat obtenue est plus importante que la masse de feve vue l'ajout d'autres ingredients
				
		this.pourcentageTransfo.put(Feve.F_MQ_E, new HashMap<Chocolat, Double>());
		conversion = 0.1 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao MQ").getValeur())/100.0;
		this.pourcentageTransfo.get(Feve.F_MQ_E).put(Chocolat.C_MQ_E, conversion);
		this.pourcentageTransfo.put(Feve.F_MQ, new HashMap<Chocolat, Double>());
		this.pourcentageTransfo.get(Feve.F_MQ).put(Chocolat.C_MQ, conversion);
				
		this.pourcentageTransfo.put(Feve.F_BQ, new HashMap<Chocolat, Double>());
		conversion = 0.1 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao BQ").getValeur())/100.0;
		this.pourcentageTransfo.get(Feve.F_BQ).put(Chocolat.C_BQ, conversion);
		
		// Initialisation des valeurs
		this.NbSalaries = 5000;
		this.salaire = 1000;
		this.coutLicenciement1Salarie = 4*salaire;
		this.capaciteTransfo = 3.7;
		
		this.coutAdjuvants = 370;
		this.coutMachines = 8;
		this.coutStockage = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*4;
		
		this.moyProd=0;
		this.totalProd=0;
		
		// Initialisation Journal Production
		this.JournalProduction.ajouter("_____________Initialement_______________________________________");
		this.JournalProduction.ajouter("Nombre de salarié :"+NbSalaries);
		this.JournalProduction.ajouter("coût d'un salarié par step :"+salaire);
		this.JournalProduction.ajouter("coût de licenciement d'un salarié :"+coutLicenciement1Salarie);
		this.JournalProduction.ajouter("coût entretien/achat des machines par step :"+coutMachines);
		this.JournalProduction.ajouter("coût 1 tonne d'Adjuvants :"+coutAdjuvants);
		this.JournalProduction.ajouter("1 salarié peut transformer "+capaciteTransfo+" tonnes de fèves en chocolat par step");
		this.JournalProduction.ajouter("________________________________________________________________");
		
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ5";
	}
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
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
	
	
	
	
	////////////////////////////////////////////////////////
	//     En lien avec l'interface graphique  +  Next    //
	////////////////////////////////////////////////////////
	/**
	 * @Robin 
	 * @Erwann
	 * @Victor
	 * @Vincent
	 */
	public void next() {
		
		this.JournalProduction.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		
		
		
		////////////////////////////////////////////////////
		//            Remplissage Journal Eq5             //   (Robin)
		////////////////////////////////////////////////////
		this.journal.ajouter(" ===ETAPE = " + Filiere.LA_FILIERE.getEtape()+ " A L'ANNEE " + Filiere.LA_FILIERE.getAnnee()+" ===");
		this.journal.ajouter("=====STOCKS=====");
		this.journal.ajouter("prix stockage chez producteur : "+ Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		for (Feve f : lesFeves) {
		this.journal.ajouter("Quantité en stock de feves " +f+ ": "+stockFeves.get(f).getValeur());
		}
		for (Chocolat c : lesChocolats) {
		this.journal.ajouter("Quantité en stock de Chocolat " +c+ ": "+stockChoco.get(c).getValeur());
		}
		for (ChocolatDeMarque cm : chocosProduits) {
		this.journal.ajouter("Quantité en stock de chocolat de marque " +cm+ ": " +stockChocoMarque.get(cm).getValeur());
		}
		this.journal.ajouter("stocks feves : "+this.totalStocksFeves.getValeur(this.cryptogramme));
		this.journal.ajouter("stocks chocolat : "+this.totalStocksChoco.getValeur(this.cryptogramme));
		this.journal.ajouter("stocks chocolat marque: "+this.totalStocksChocoMarque.getValeur(this.cryptogramme));
		
		
		
		////////////////////////////////////////////////////
		//            Paiement cout de stockage           //   (Robin)
		////////////////////////////////////////////////////
		//this.totalStocksChocoMarque.getValeur(this.cryptogramme);
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", (this.totalStocksFeves.getValeur(this.cryptogramme)+this.totalStocksChoco.getValeur(this.cryptogramme))*this.coutStockage);
		
		
		
		
		//
		double somme = 0;
		for (ChocolatDeMarque cm : this.chocosProduits) {
			somme += this.stockChocoMarque.get(cm).getValeur();
		}
		System.out.println("_______________VERIF__________________");
		System.out.println(this.totalStocksChocoMarque.getValeur(this.cryptogramme));
		System.out.println(somme);
		//
		
		
		
		
		////////////////////////////////////////////////////
		// Determination de la Capacité de Transformation //   (Erwann & Vincent & Victor)
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
		//   Transformation des Fèves en ChocoDeMarque    //   (Erwann)
		////////////////////////////////////////////////////
		double TransfoTotal = 0;
		
		/* Stratégie de transformation :
		 * --> La capacité de Transformation totale est repartie au prorata des fèves en stock
		 * --> on ne garde pas de chocolat en stock (ils sont directement transformé en Choco Marque sans coût supplémentaire)
		 * --> On repartie équitable la transformation entre les marques :
		 */
		
		// Création d'un HashMap contenant la répartition de chaque fève en stock
		HashMap<Feve, Double> repartition = new HashMap<Feve, Double>();
		for (Feve f : lesFeves) {
			repartition.put(f, this.stockFeves.get((Feve)f).getValeur() / this.totalStocksFeves.getValeur());
		}
		
		// Transformation des feves avec la méthode "Transformation (Feve, tonnes)" qui mets à jour les stocks
		for (Feve f : lesFeves) {
			double TonnesTranfo = capaciteTransfoTotal * repartition.get(f);
			Transformation(f, TonnesTranfo);
			TransfoTotal += TonnesTranfo;
		}
		this.JournalProduction.ajouter("Tonnes de feves transformées : "+TransfoTotal);
		
		// Transformation de tous les chocolats en chocolats de marque`avec une répartition équitable entre les marques
		for (Chocolat c : lesChocolats) {
			for (ChocolatDeMarque cm : chocosProduits) {
				if(c.getGamme() == cm.getGamme()) {
					double nbr_de_marque = chocosProduits.size();
					stockChocoMarque.get((ChocolatDeMarque) cm).ajouter(this, stockChoco.get(c).getValeur()/nbr_de_marque, this.cryptogramme);
					stockChoco.get((Chocolat) c).retirer(this, stockChoco.get(c).getValeur()/nbr_de_marque, this.cryptogramme);
					VariationStockChocoMarque.replace(cm, stockChoco.get(c).getValeur()/nbr_de_marque);
					totalStocksChocoMarque.ajouter(this, stockChoco.get(c).getValeur()/nbr_de_marque, this.cryptogramme);
					totalStocksChoco.retirer(this, stockChoco.get(c).getValeur()/nbr_de_marque, this.cryptogramme);
						}
					}
				}
		
		// Calcul des cout de Transformation avec la méthode "CoutTransformation(ChocolatDeMarque, tonnes)"
		double coutTransfoTotal = 0;
		for (ChocolatDeMarque cm : chocosProduits) {
			if (cm.getGamme()!= Gamme.HQ) {
				double t = VariationStockChocoMarque.get(cm);
				coutTransfoTotal += this.CoutTransformation(cm,t);	
			}
		}
		
		// Paiement des cout de transformation
		this.JournalProduction.ajouter("Coût de la transformation : "+coutTransfoTotal);
		Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût Transformation" , coutTransfoTotal);
		
		
		
		////////////////////////////////////////////////////
		//       Calcul de la moyenne de production       //    (Robin)
		////////////////////////////////////////////////////
		this.totalProd += TransfoTotal;
		this.moyProd = this.totalProd/(Filiere.LA_FILIERE.getEtape()+1);
		this.JournalProduction.ajouter("Production moyenne de l'acteur : "+moyProd+" tonnes/step");	
	
	}
	
	
	
	////////////////////////////////////////////////////////
	//        Déclaration de la marque CacaoFusion        //
	////////////////////////////////////////////////////////
	/**
	 * @Erwann
	 */
	public List<String> getMarquesChocolat() {
		LinkedList<String> marques = new LinkedList<String>();
		marques.add("CacaoFusion");
		return marques;
	}
	/**
	 * @Erwann
	 */
	public List<ChocolatDeMarque> getChocolatsProduits() {
		List<String> marquesDistributeurs = Filiere.LA_FILIERE.getMarquesDistributeur();
		if (this.chocosProduits == null) {
			this.chocosProduits = new LinkedList<ChocolatDeMarque>();
			for (Chocolat c : Chocolat.values()) {
				if (c.getGamme()!= Gamme.HQ) {
					int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+c.getGamme()).getValeur());
					this.chocosProduits.add(new ChocolatDeMarque(c, "CacaoFusion", pourcentageCacao));
					for (String marque : marquesDistributeurs) {
						this.chocosProduits.add(new ChocolatDeMarque(c, marque, pourcentageCacao));
					}
				}	
			}
		}
		return chocosProduits;
	}
	
	
	////////////////////////////////////////////////////////
	//               Méthodes Complémentaires             //
	////////////////////////////////////////////////////////
	public Color getColor() {// NE PAS MODIFIER
		return new Color(165, 235, 195); 
	}
	public String getDescription() {
		return "Fuuuuuuusion";
	}
	/** 
	 * Renvoie les indicateurs
	 */
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		for (Feve f : lesFeves) {
				res.add(this.stockFeves.get(f));
		}
		for (Chocolat c : lesChocolats) {
				res.add(this.stockChoco.get(c));
		}
		res.add(this.totalStocksChocoMarque);
		
		return res;
	}
	/**
	 * Renvoie les parametres
	 */
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}
	/**
	 * Renvoie les journaux
	 */
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(this.journal);
		res.add(this.JournalProduction);
		return res;
	}

	
	
	
	////////////////////////////////////////////////////////
	//               En lien avec la Banque               //
	////////////////////////////////////////////////////////

	// Appelee en debut de simulation pour vous communiquer 
	// votre cryptogramme personnel, indispensable pour les
	// transactions.
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	// Appelee lorsqu'un acteur fait faillite (potentiellement vous)
	// afin de vous en informer.
	public void notificationFaillite(IActeur acteur) {
	}

	// Apres chaque operation sur votre compte bancaire, cette
	// operation est appelee pour vous en informer
	public void notificationOperationBancaire(double montant) {
	}
	
	// Renvoie le solde actuel de l'acteur
	protected double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme);
	}

	
	
	////////////////////////////////////////////////////////
	//        Pour la creation de filieres de test        //
	////////////////////////////////////////////////////////

	// Renvoie la liste des filieres proposees par l'acteur
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		return(filieres);
	}

	// Renvoie une instance d'une filiere d'apres son nom
	public Filiere getFiliere(String nom) {
		return Filiere.LA_FILIERE;
	}
	
	/***
	 * @Robin
	 * @Erwann
	 */
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			if (p.getType().equals("Feve")) {
				if (this.stockFeves.keySet().contains(p)) {
					return this.stockFeves.get(p).getValeur();
				} else {
					return 0.0;
				}
			} else if (p.getType().equals("Chocolat")) {
				if (this.stockChoco.keySet().contains(p)) {
					return this.stockChoco.get(p).getValeur();
				} else {
					return 0.0;
				}
			} else {
				if (this.stockChocoMarque.keySet().contains(p)) {
					return this.stockChocoMarque.get(p).getValeur();
				} else {
					return 0.0;
				}
			}
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre stock
		}
	}
}
