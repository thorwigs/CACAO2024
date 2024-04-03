package abstraction.eq7Transformateur4;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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

public class Transformateur4Acteur implements IActeur, IFabricantChocolatDeMarque, IMarqueChocolat {
	
	protected int cryptogramme;
	private Journal journal;
	private double coutStockageTransfo; //pour simplifier, on aura juste a appeler cette variable pour nos coût de stockage
	protected List<Feve> lesFeves; //la liste de toutes les fèves qui existent
	private List<ChocolatDeMarque>chocosProduits; 
	protected HashMap<Feve, Double> stockFeves; //un truc qui contiendra tout nos stocks pour chaque fèves
	protected HashMap<Chocolat, Double> stockChoco; //idem pour les chocolats, donc on aura 2 chocos (un BQ/MH et un HQ)
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque; //idem pour les chocolat de marques, donc on aura un seul choco, le HQ de stockChoco une fois qu'on lui aura apposé la marque Mirage
	protected HashMap<Feve, HashMap<Chocolat, Double>> pourcentageTransfo; // pour les differentes feves, le chocolat qu'elle peuvent contribuer a produire avec le ratio
	protected List<ChocolatDeMarque> chococolatCocOasis;
	protected Variable totalStocksFeves;  // La quantite totale de stock de feves 
	protected Variable totalStocksChoco;  // La quantite totale de stock de chocolat 
	protected Variable totalStocksChocoMarque;  // La quantite totale de stock de chocolat de marque 

	public Transformateur4Acteur() {
		this.journal = new Journal(this.getNom()+" journal", this);
		this.totalStocksFeves = new VariablePrivee("Eq4TStockFeves", "<html>Quantite totale de feves en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.totalStocksChoco = new VariablePrivee("Eq4TStockChoco", "<html>Quantite totale de chocolat en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.totalStocksChocoMarque = new VariablePrivee("Eq4TStockChocoMarque", "<html>Quantite totale de chocolat de marque en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
	}
	
	public void initialiser() {
		this.coutStockageTransfo = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*4;
		
		this.lesFeves = new LinkedList<Feve>();
		this.journal.ajouter("Les Feves sont :");
		for (Feve f : Feve.values()) {
			this.lesFeves.add(f);
			this.journal.ajouter("   - "+f);
		}
		
		//////////a changer
		this.stockFeves=new HashMap<Feve,Double>();
		for (Feve f : this.lesFeves) {
			this.stockFeves.put(f, 20000.0);
			this.totalStocksFeves.ajouter(this, 20000.0, this.cryptogramme);
			this.journal.ajouter("ajout de 20000 de "+f+" au stock de feves --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
		}
		
		//////////a changer
		this.stockChoco=new HashMap<Chocolat,Double>();
		for (Chocolat c : Chocolat.values()) {
			this.stockChoco.put(c, 20000.0);
			this.totalStocksChoco.ajouter(this, 20000.0, this.cryptogramme);
			this.journal.ajouter("ajout de 20000 de "+c+" au stock de chocolat --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
		}
	


		this.stockChocoMarque=new HashMap<ChocolatDeMarque,Double>();
		
		//on créé la Hashmap de pourcentageTransfo, qu'on va compléter ensuite avec les infos connues par tout le monde
		this.pourcentageTransfo = new HashMap<Feve, HashMap<Chocolat, Double>>();
		this.pourcentageTransfo.put(Feve.F_HQ_BE, new HashMap<Chocolat, Double>());
		double conversion = 1.0 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao HQ").getValeur())/100.0;
		this.pourcentageTransfo.get(Feve.F_HQ_BE).put(Chocolat.C_HQ_BE, conversion);// la masse de chocolat obtenue est plus importante que la masse de feve vue l'ajout d'autres ingredients
		this.pourcentageTransfo.put(Feve.F_MQ_E, new HashMap<Chocolat, Double>());
		conversion = 1.0 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao MQ").getValeur())/100.0;
		this.pourcentageTransfo.get(Feve.F_MQ_E).put(Chocolat.C_MQ_E, conversion);
		this.pourcentageTransfo.put(Feve.F_MQ, new HashMap<Chocolat, Double>());
		this.pourcentageTransfo.get(Feve.F_MQ).put(Chocolat.C_MQ, conversion);
		this.pourcentageTransfo.put(Feve.F_BQ, new HashMap<Chocolat, Double>());
		conversion = 1.0 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao BQ").getValeur())/100.0;
		this.pourcentageTransfo.get(Feve.F_BQ).put(Chocolat.C_BQ, conversion);
		
		// à continuer..

	}
	
	

	public String getNom() {// NE PAS MODIFIER
		return "EQ7";
	}
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		this.journal.ajouter("etape=" + Filiere.LA_FILIERE.getEtape());
		this.journal.ajouter("coût de stockage producteur : " + Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		
		//on paye notre coût de stockage:
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "CoûtStockage", (this.totalStocksFeves.getValeur(cryptogramme)+this.totalStocksChoco.getValeur(cryptogramme)+this.totalStocksChocoMarque.getValeur(cryptogramme))*this.coutStockageTransfo);

		this.journal.ajouter("" + this.getMarquesChocolat());

		this.getJournaux();
		
		
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(162, 207, 238); 
	}

	public String getDescription() {
		return "CocOasis";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(journal);
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

	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			return 0; // A modifier
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre stock
		}
	}

	@Override
	public List<ChocolatDeMarque> getChocolatsProduits() {  
		// TODO Auto-generated method stub
		this.chocosProduits.add(new ChocolatDeMarque(Chocolat.C_HQ_BE, "Mirage", 80));
		return this.chocosProduits;
	}

	@Override
	public List<String> getMarquesChocolat() {
		// TODO Auto-generated method stub
		LinkedList<String> marques = new LinkedList<String>();
		marques.add("Mirage");
		return marques;
	}
}
