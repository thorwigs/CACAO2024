package abstraction.eq4Transformateur1;

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
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur1Acteur implements IActeur, IMarqueChocolat, IFabricantChocolatDeMarque {
	
	protected int cryptogramme;
	protected Journal journal;
	private double coutStockage;

	protected List<Feve> lesFeves;
	private List<ChocolatDeMarque>chocosProduits;
	protected HashMap<Feve, Double> stockFeves;
	protected HashMap<Chocolat, Double> stockChoco;
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque;
	protected HashMap<Feve, HashMap<Chocolat, Double>> pourcentageTransfo; // pour les differentes feves, le chocolat qu'elle peuvent contribuer a produire avec le ratio
	protected List<ChocolatDeMarque> chocolatsVillors;
	protected Variable totalStocksFeves;  // La qualite totale de stock de feves 
	protected Variable totalStocksChoco;  // La qualite totale de stock de chocolat 
	protected Variable totalStocksChocoMarque;  // La qualite totale de stock de chocolat de marque 

	
	public Transformateur1Acteur() {
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
		this.journal = new Journal(this.getNom()+" journal", this);
		this.totalStocksFeves = new VariablePrivee("Eq4TStockFeves", "<html>Quantite totale de feves en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.totalStocksChoco = new VariablePrivee("Eq4TStockChoco", "<html>Quantite totale de chocolat en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.totalStocksChocoMarque = new VariablePrivee("EqXTStockChocoMarque", "<html>Quantite totale de chocolat de marque en stock</html>",this, 0.0, 1000000.0, 0.0);
	}
	
	public void initialiser() {
		this.stockChocoMarque=new HashMap<ChocolatDeMarque,Double>();

		this.lesFeves = new LinkedList<Feve>();
		this.journal.ajouter("Les Feves sont :");
		for (Feve f : Feve.values()) {
			this.lesFeves.add(f);
			this.journal.ajouter("   - "+f);
		}
		
		this.stockFeves=new HashMap<Feve,Double>();
		/*
		for (Feve f : this.lesFeves) {
			this.stockFeves.put(f, 20000.0);
			this.totalStocksFeves.ajouter(this, 20000.0, this.cryptogramme);
			this.journal.ajouter("ajout de 20000 de "+f+" au stock de feves --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
		}
		*/
		
		this.stockChoco=new HashMap<Chocolat,Double>();
		/*
		for (Chocolat c : Chocolat.values()) {
			this.stockChoco.put(c, 20000.0);
			this.totalStocksChoco.ajouter(this, 20000.0, this.cryptogramme);
			this.journal.ajouter("ajout de 20000 de "+c+" au stock de chocolat --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
		}
		*/
		
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

	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ4";
	}
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		// ajout dans le journal
		this.journal.ajouter("étape : "+Filiere.LA_FILIERE.getEtape());
		this.journal.ajouter("prix stockage chez producteur : "+Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		
		//transformation des feves en chocolat
		for (Feve f : this.pourcentageTransfo.keySet()) {
			for (Chocolat c : this.pourcentageTransfo.get(f).keySet()) {
				if (this.stockFeves.get(f) != null) {
					int transfo = this.stockFeves.get(f).intValue();//V1 capacite de tranformation illimitee ... (int) (Math.min(this.stockFeves.get(f), Filiere.random.nextDouble()*30));
					if (transfo>0) {
						this.stockFeves.put(f, this.stockFeves.get(f)-transfo);
						this.totalStocksFeves.retirer(this, transfo, this.cryptogramme);
						this.journal.ajouter(Romu.COLOR_LLGRAY, Color.PINK, "Transfo de "+Journal.entierSur6(transfo)+" T de "+f+" en :"+Journal.doubleSur(transfo*this.pourcentageTransfo.get(f).get(c),3,2)+" T de "+c);
	
						// La moitie (newChoco) sera stockee sous forme de chocolat, l'autre moitie directement etiquetee "Villors"
						boolean tropDeChoco = this.totalStocksChoco.getValeur((Integer)cryptogramme)>100000;
						double newChoco = tropDeChoco ? 0.0 : ((transfo/2.0)*this.pourcentageTransfo.get(f).get(c)); // la moitie en chocolat tant qu'on n'en n'a pas trop
						double newChocoMarque = ((transfo)*this.pourcentageTransfo.get(f).get(c))-newChoco;
						if (newChoco>0.0) {
							this.stockChoco.put(c, this.stockChoco.get(c)+newChoco);
							this.totalStocksChoco.ajouter(this, newChoco, cryptogramme);
							this.journal.ajouter(Romu.COLOR_LLGRAY, Color.PINK, " - "+Journal.doubleSur(newChoco,3,2)+" T de "+c);
						}
						int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+c.getGamme()).getValeur());
						ChocolatDeMarque cm= new ChocolatDeMarque(c, "LeaderKakao", pourcentageCacao);
						double scm = this.stockChocoMarque.keySet().contains(cm) ?this.stockChocoMarque.get(cm) : 0.0;
						this.stockChocoMarque.put(cm, scm+newChocoMarque);
						this.journal.ajouter(Romu.COLOR_LLGRAY, Color.PINK, " - "+Journal.doubleSur(newChocoMarque,3,2)+" T de "+cm);
						this.totalStocksChocoMarque.ajouter(this,newChocoMarque, this.cryptogramme);
						//this.journal.ajouter(Romu.COLOR_LLGRAY, Color.PINK, "Transfo de "+(transfo<10?" "+transfo:transfo)+" T de "+f+" en "+Journal.doubleSur(transfo*this.pourcentageTransfo.get(f).get(c),3,2)+" T de "+c);
						this.journal.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN," stock("+f+")->"+this.stockFeves.get(f));
						this.journal.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN," stock("+c+")->"+this.stockChoco.get(c));
						this.journal.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN," stock("+cm+")->"+this.stockChocoMarque.get(cm));
					}
				}
			}
		}
		
		//payer les couts
		double coutStockage = (this.totalStocksFeves.getValeur(cryptogramme)+this.totalStocksChoco.getValeur(cryptogramme)+this.totalStocksChocoMarque.getValeur(cryptogramme))*this.coutStockage;
		if (coutStockage > 0.0) {
			Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", coutStockage);
		}
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(229, 243, 157); 
	}

	public String getDescription() {
		return "équipe 4, transformateur 1 : LeaderKakao";
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
			if (p instanceof Feve) {
                return stockFeves.getOrDefault((Feve) p, 0.0);
            } else if (p instanceof Chocolat) {
                return stockChoco.getOrDefault((Chocolat) p, 0.0);
            } else if (p instanceof ChocolatDeMarque) {
                return stockChocoMarque.getOrDefault((ChocolatDeMarque) p, 0.0);
            }
        }
        return 0.0;
	}
	//Si l'acteur est autorisé, la méthode vérifie le type de produit demandé (p) 
	//et renvoie la quantité en stock correspondante à ce produit pour cet acteur 
	//Si le produit n'est pas présent en stock, la méthode renvoie 0.0

	@Override
	public List<ChocolatDeMarque> getChocolatsProduits() {

		if (this.chocosProduits.size()==0) {
			Chocolat cmc = Chocolat.C_MQ_E;
			int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+cmc.getGamme()).getValeur());
			this.chocosProduits.add(new ChocolatDeMarque(cmc, "LeaderKakao", pourcentageCacao));
			
			Chocolat chc = Chocolat.C_HQ_BE;
			pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+chc.getGamme()).getValeur());
			this.chocosProduits.add(new ChocolatDeMarque(chc, "LeaderKakao", pourcentageCacao));
		}
		return this.chocosProduits;
	}

	@Override
	public List<String> getMarquesChocolat() {
		LinkedList<String> marques = new LinkedList<String>();
		marques.add("LeaderKakao");
		return marques;
	}
}

