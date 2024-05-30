package abstraction.eq6Transformateur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
//commmentaire pour pascal
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
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;



public class Transformateur3Acteur implements IActeur,IMarqueChocolat, IFabricantChocolatDeMarque {
	
	protected Journal journal;
	
	private double coutStockage;

	protected List<Feve> lesFeves;
	protected List<ChocolatDeMarque> chocosProduits;
	protected HashMap<Feve, Double> stockFeves;
	protected HashMap<Chocolat, Double> stockChoco;
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque;
	protected HashMap<Feve, HashMap<Chocolat, Double>> pourcentageTransfo; // pour les differentes feves, le chocolat qu'elle peuvent contribuer a produire avec le ratio
	protected List<ChocolatDeMarque> chocolatsChocoSharks;
	protected Variable totalStocksFeves;  // La qualite totale de stock de feves 
	protected Variable totalStocksChoco;  // La qualite totale de stock de chocolat 
	protected Variable totalStocksChocoMarque;  // La qualite totale de stock de chocolat de marque 
	
	protected int cryptogramme;

	public Transformateur3Acteur() {
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
		this.journal = new Journal(this.getNom()+" journal", this);
		this.totalStocksFeves = new VariablePrivee("EqXTStockFeves", "<html>Quantite totale de feves en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.totalStocksChoco = new VariablePrivee("EqXTStockChoco", "<html>Quantite totale de chocolat en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.totalStocksChocoMarque = new VariablePrivee("EqXTStockChocoMarque", "<html>Quantite totale de chocolat de marque en stock</html>",this, 0.0, 1000000.0, 0.0);

	}
	/**
	 * @author Mahel
	 */
	public void initialiser() {
		
		this.lesFeves = new LinkedList<Feve>();
		this.coutStockage = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*4;
		this.stockFeves = new HashMap<Feve,Double>();
		stockFeves.put(Feve.F_BQ,0.0);
		stockFeves.put(Feve.F_MQ,0.0);
		stockFeves.put(Feve.F_MQ_E,0.0);
		stockFeves.put(Feve.F_HQ,0.0);
		stockFeves.put(Feve.F_HQ_E,0.0);
		stockFeves.put(Feve.F_HQ_BE,0.0);
		
		this.stockChoco=new HashMap<Chocolat,Double>();
		for (Chocolat c : Chocolat.values()) {
			this.stockChoco.put(c, 20000.0);
			this.totalStocksChoco.ajouter(this, 20000.0, this.cryptogramme);
			this.journal.ajouter("ajout de 20000 de "+c+" au stock de chocolat --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
		}
		this.stockChocoMarque=new HashMap<ChocolatDeMarque,Double>();
		this.chocosProduits = getChocolatsProduits();
		this.journal.ajouter("Produit de la marque : ");
		for(IProduit c : this.chocosProduits) {
			this.journal.ajouter(c.toString());
		}
		for(ChocolatDeMarque c : chocosProduits) {
			stockChocoMarque.put(c, 0.0);
			
		}
		this.coutStockage = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*4;
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
		
		for (Chocolat c : Chocolat.values()) {
			this.stockChoco.put(c, 20000.0);
			this.totalStocksChoco.ajouter(this, 20000.0, this.cryptogramme);
			this.journal.ajouter("ajout de 20000 de "+c+" au stock de chocolat --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
		}
		
		
		
		
	
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ6";
	}
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////
	/**
	 * @author Thomas
	 */
	public void next() {
		this.journal.ajouter("etape=" + Filiere.LA_FILIERE.getEtape() );
		this.journal.ajouter("=== STOCKS === ");
		if ((this.totalStocksFeves.getValeur(this.cryptogramme)+this.totalStocksChoco.getValeur(this.cryptogramme))*this.coutStockage>0) {
			this.journal.ajouter("Stock total " + this.coutStockage);
			this.journal.ajouter("cout de stockage "+this.totalStocksFeves.getValeur(this.cryptogramme)+this.totalStocksChoco.getValeur(this.cryptogramme)*this.coutStockage); 
			Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", (this.totalStocksFeves.getValeur(this.cryptogramme)+this.totalStocksChoco.getValeur(this.cryptogramme))*this.coutStockage);
		}
		for (Feve f : stockFeves.keySet()) {
			this.journal.ajouter("Stock de "+f+" = "+this.stockFeves.get(f));
		}
		this.journal.ajouter("Stock de "+Chocolat.C_BQ+" = "+this.stockChoco.get(Chocolat.C_BQ));
		this.journal.ajouter("Stock de "+Chocolat.C_MQ+" = "+this.stockChoco.get(Chocolat.C_MQ));
		this.journal.ajouter("Stock de "+Chocolat.C_HQ+" = "+this.stockChoco.get(Chocolat.C_HQ));
		this.journal.ajouter("Stock de "+Chocolat.C_MQ_E+" = "+this.stockChoco.get(Chocolat.C_MQ_E));
		this.journal.ajouter("Stock de "+Chocolat.C_HQ_E+" = "+this.stockChoco.get(Chocolat.C_HQ_E));
		this.journal.ajouter("Stock de "+Chocolat.C_HQ_BE+" = "+this.stockChoco.get(Chocolat.C_HQ_BE));
	
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(160, 242, 226); 
	}

	public String getDescription() {
		return "Transformateur 3 : ChocoSharks";
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
		res.add(this.journal);
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

	
	/**
	 * @author Mahel
	 */
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme!=cryptogramme) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			return 0;
	}
		for(IProduit f : this.stockFeves.keySet()) {
			if (f.equals(p)) { 
			}
		}
		for(IProduit c : this.stockChoco.keySet()) {
				if (c.equals(p)) { 
				return stockChoco.get(c);
			}
		}
		return 0;
	}

	/**
	 * @author Mahel
	 */
	public List<String> getMarquesChocolat() {
		LinkedList<String> marques = new LinkedList<String>();
		marques.add("ChocoSharks");
		return marques;
	}
	/**
	 * @author Mahel
	 */
	@Override
	public List<ChocolatDeMarque> getChocolatsProduits() {
		if (this.chocosProduits.size()==0) {
			this.chocosProduits.add(new ChocolatDeMarque(Chocolat.C_BQ, "ChocoSharks", 30));
			this.chocosProduits.add(new ChocolatDeMarque(Chocolat.C_MQ, "ChocoSharks", 50));
			this.chocosProduits.add(new ChocolatDeMarque(Chocolat.C_MQ_E, "ChocoSharks", 50));
			this.chocosProduits.add(new ChocolatDeMarque(Chocolat.C_HQ, "ChocoSharks", 80));
			this.chocosProduits.add(new ChocolatDeMarque(Chocolat.C_HQ_E, "ChocoSharks", 80));
			this.chocosProduits.add(new ChocolatDeMarque(Chocolat.C_HQ_BE, "ChocoSharks", 80));
		}
		return this.chocosProduits;
	}
	public void Apposage_de_marque() {
		for(Chocolat c : stockChoco.keySet()) {
			
		}
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}