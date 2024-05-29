package abstraction.eq4Transformateur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
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

public class Transformateur1Acteur implements IActeur, IMarqueChocolat, IFabricantChocolatDeMarque {

	protected HashMap<Gamme, Double> PRIX_DEFAUT;
	
	protected int quantiteMiniCC = 1200;
	protected int nombreMois = 3;
	protected int stockCibleMini = 40000;

	protected List<ExemplaireContratCadre> contratsEnCoursVente;
	protected List<ExemplaireContratCadre> contratsEnCoursAchat;
	
	protected int nbSalaries = (int) (this.stockCibleMini / 3.75);
	protected HashMap<Gamme, Double> listePourcentageMarque;
	
	protected int cryptogramme;
	protected Journal journal;
	private double coutStockage;
	private double coutProduction;
	private double coutMainDOeuvre;

	protected List<Feve> lesFeves;
	protected HashMap<Feve, Variable> stockFeves;
	protected HashMap<Chocolat, Variable> stockChoco;
	protected HashMap<ChocolatDeMarque, Variable> stockChocoMarque;
	protected HashMap<Feve, HashMap<Chocolat, Double>> pourcentageTransfo; // pour les differentes feves, le chocolat qu'elle peuvent contribuer a produire avec le ratio
	private List<Chocolat> chocolatsAVendre;
	private List<ChocolatDeMarque>chocosProduits;
	protected Variable totalStocksFeves;  // La qualite totale de stock de feves 
	protected Variable totalStocksChoco;  // La qualite totale de stock de chocolat 
	protected Variable totalStocksChocoMarque;  // La qualite totale de stock de chocolat de marque 
	
	protected HashMap<Gamme, Double> demandeCC;

	
	public Transformateur1Acteur() {
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
		//this.getChocolatsProduits();
		this.journal = new Journal(this.getNom()+" journal", this);
		this.totalStocksFeves = new VariablePrivee("Eq4_StockFeves", "<html>Quantite totale de feves en stock</html>",this);
		this.totalStocksChoco = new VariablePrivee("Eq4_StockChoco", "<html>Quantite totale de chocolat en stock</html>",this);
		this.totalStocksChocoMarque = new VariablePrivee("Eq4_StockChocoMarque", "<html>Quantite totale de chocolat de marque en stock</html>",this);
		

		this.lesFeves = new LinkedList<Feve>();
		this.journal.ajouter("Les Feves sont :");
		for (Feve f : Feve.values()) {
			this.lesFeves.add(f);
			this.journal.ajouter("   - "+f);
		}
		
		this.stockFeves=new HashMap<Feve,Variable>();
		for (Feve f : this.lesFeves) {
			this.stockFeves.put(f, new Variable("EQ4_stock_feve_"+f, this));
		}
		
		this.stockChoco = new HashMap<Chocolat,Variable>();
		this.chocolatsAVendre = new LinkedList<Chocolat>();
		this.chocolatsAVendre.add(Chocolat.C_MQ);
		for (Chocolat c : Chocolat.values()) {
			this.stockChoco.put(c, new Variable("EQ4_stock_choco_"+c, this));
		}
		
		this.stockChocoMarque = new HashMap<ChocolatDeMarque,Variable>();

		this.demandeCC = new HashMap<Gamme, Double>();
		this.listePourcentageMarque = new HashMap<Gamme, Double>();
		this.PRIX_DEFAUT = new HashMap<Gamme, Double>();
		this.PRIX_DEFAUT.put(Gamme.HQ, 4800.0);
		this.PRIX_DEFAUT.put(Gamme.MQ, 4500.0);
	}
/**
*@author Noemie_Grosset
*/
	public void initialiser() {
		this.coutStockage = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*4;
		
		this.pourcentageTransfo = new HashMap<Feve, HashMap<Chocolat, Double>>();
		
		double conversion = 1.0 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao HQ").getValeur())/100.0;
		this.pourcentageTransfo.put(Feve.F_HQ_BE, new HashMap<Chocolat, Double>());
		this.pourcentageTransfo.get(Feve.F_HQ_BE).put(Chocolat.C_HQ_BE, conversion);// la masse de chocolat obtenue est plus importante que la masse de feve vue l'ajout d'autres ingredients
		
		conversion = 1.0 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao MQ").getValeur())/100.0;
		this.pourcentageTransfo.put(Feve.F_MQ_E, new HashMap<Chocolat, Double>());
		this.pourcentageTransfo.get(Feve.F_MQ_E).put(Chocolat.C_MQ, conversion);
		
		this.pourcentageTransfo.put(Feve.F_MQ, new HashMap<Chocolat, Double>());
		this.pourcentageTransfo.get(Feve.F_MQ).put(Chocolat.C_MQ, conversion);
		
		//this.pourcentageTransfo.put(Feve.F_BQ, new HashMap<Chocolat, Double>());
		//conversion = 1.0 + (100.0 - Filiere.LA_FILIERE.getParametre("pourcentage min cacao BQ").getValeur())/100.0;
		//this.pourcentageTransfo.get(Feve.F_BQ).put(Chocolat.C_BQ, conversion);
		
		this.getChocolatsProduits();
		for (ChocolatDeMarque c : this.chocosProduits) {
			this.stockChocoMarque.put(c, new Variable("EQ4_stock_choco_"+c, this));
			this.demandeCC.put(c.getGamme(), 0.0);
		}

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
		
		for (Feve f : stockFeves.keySet()) {
			this.journal.ajouter(" - Stock de feves "+f.getGamme()+" : "+stockFeves.get(f).getValeur());
		}
		
		for (ChocolatDeMarque cm : stockChocoMarque.keySet()) {
			this.journal.ajouter(" - Stock de "+cm.getNom()+" : "+stockChocoMarque.get(cm).getValeur());
		}
		
		double nbTonnesProduites = 0.0;
		//transformation des feves en chocolat
		for (Feve f : this.pourcentageTransfo.keySet()) {
			for (Chocolat c : this.pourcentageTransfo.get(f).keySet()) {
				int transfo = (int) this.stockFeves.get(f).getValeur();//V1 capacite de tranformation illimitee ... (int) (Math.min(this.stockFeves.get(f), Filiere.random.nextDouble()*30));
				if (transfo>0) {
					this.stockFeves.get(f).setValeur(this, this.stockFeves.get(f).getValeur()-transfo);
					this.totalStocksFeves.retirer(this, transfo, this.cryptogramme);
					this.journal.ajouter(Romu.COLOR_LLGRAY, Color.PINK, "Transfo de "+Journal.entierSur6(transfo)+" T de "+f+" en :"+Journal.doubleSur(transfo*this.pourcentageTransfo.get(f).get(c),3,2)+" T de "+c);

					// La moitie (newChoco) sera stockee sous forme de chocolat, l'autre moitie directement etiquetee "LeaderKakao"
					boolean tropDeChoco = this.totalStocksChoco.getValeur((Integer)cryptogramme)>=0; // 100000 pour la V2
					double newChoco = tropDeChoco ? 0.0 : ((transfo/2.0)*this.pourcentageTransfo.get(f).get(c)); // la moitie en chocolat tant qu'on n'en n'a pas trop
					double newChocoMarque = ((transfo)*this.pourcentageTransfo.get(f).get(c))-newChoco;
					if (newChoco>0.0) {
						this.stockChoco.get(c).setValeur(this, this.stockChoco.get(c).getValeur()+newChoco);
						this.totalStocksChoco.ajouter(this, newChoco, cryptogramme);
						this.journal.ajouter(Romu.COLOR_LLGRAY, Color.PINK, " - "+Journal.doubleSur(newChoco,3,2)+" T de "+c);
					}
					int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+c.getGamme()).getValeur());
					ChocolatDeMarque cm;
					if(f.getGamme()==Gamme.MQ) {
						cm= new ChocolatDeMarque(c, "CacaoMagic", pourcentageCacao);
					} else {
						cm= new ChocolatDeMarque(c, "LeaderKakao", pourcentageCacao);
					}
					if (this.stockChocoMarque.keySet().contains(cm)) {
						this.stockChocoMarque.get(cm).setValeur(this, this.stockChocoMarque.get(cm).getValeur()+newChocoMarque);
					} else {
						this.stockChocoMarque.put(cm, new Variable(cm.getNom(), this, newChocoMarque));
					}
					this.journal.ajouter(Romu.COLOR_LLGRAY, Color.PINK, " - "+Journal.doubleSur(newChocoMarque,3,2)+" T de "+cm);
					this.totalStocksChocoMarque.ajouter(this,newChocoMarque, this.cryptogramme);
					
					nbTonnesProduites = nbTonnesProduites + transfo;
					coutProduction = transfo*(1-pourcentageCacao) * 1200 + transfo*8;
					if (coutProduction > 0.0) {
						Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Production", coutProduction);
					}
						
					//this.journal.ajouter(Romu.COLOR_LLGRAY, Color.PINK, "Transfo de "+(transfo<10?" "+transfo:transfo)+" T de "+f+" en "+Journal.doubleSur(transfo*this.pourcentageTransfo.get(f).get(c),3,2)+" T de "+c);
					this.journal.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN," stock("+f+")->"+this.stockFeves.get(f).getValeur());
					this.journal.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN," stock("+c+")->"+this.stockChoco.get(c).getValeur());
					this.journal.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN," stock("+cm+")->"+this.stockChocoMarque.get(cm).getValeur());
				}
			}
		}
/**
* @author Noemie_Grosset
*/	
		//payer les couts
		double couts = (this.totalStocksFeves.getValeur(cryptogramme)+this.totalStocksChoco.getValeur(cryptogramme)+this.totalStocksChocoMarque.getValeur(cryptogramme))*this.coutStockage;
		if (couts > 0.0) {
			Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", couts);
		}
		
		
		double nbInterim = Math.max(0, nbTonnesProduites*0.27-this.nbSalaries);
		coutMainDOeuvre = 2*1000*nbInterim + this.nbSalaries*1000;
		if (coutMainDOeuvre > 0.0) {
			Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Main d'oeuvre", coutMainDOeuvre);
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
		res.add(this.stockFeves.get(Feve.F_HQ));
		res.add(this.stockFeves.get(Feve.F_MQ));
		//res.addAll(this.stockChoco.values());
		res.addAll(this.stockChocoMarque.values());
		res.add(this.totalStocksChocoMarque);
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
                return Math.max(stockFeves.get((Feve) p).getValeur(), 0.0);
            } else if (p instanceof Chocolat) {
                return Math.max(stockChoco.get((Chocolat) p).getValeur(), 0.0);
            } else if (p instanceof ChocolatDeMarque) {
                return Math.max(stockChocoMarque.get((ChocolatDeMarque) p).getValeur(), 0.0);
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
			//Chocolat cmce = Chocolat.C_MQ_E;
			//int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+cmce.getGamme()).getValeur());
			//this.chocosProduits.add(new ChocolatDeMarque(cmce, "CacaoMagic", pourcentageCacao));
			
			Chocolat cmc = Chocolat.C_MQ;
			int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+cmc.getGamme()).getValeur());
			this.chocosProduits.add(new ChocolatDeMarque(cmc, "CacaoMagic", pourcentageCacao));
				
			Chocolat chc = Chocolat.C_HQ_BE;
			pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+chc.getGamme()).getValeur());
			this.chocosProduits.add(new ChocolatDeMarque(chc, "LeaderKakao", pourcentageCacao));
			
			this.listePourcentageMarque.put(Gamme.MQ, 0.25);
			this.listePourcentageMarque.put(Gamme.HQ, 0.75);
		}
		return this.chocosProduits;
	}
	
/**
* @author Yannig
*/
	@Override
	public List<String> getMarquesChocolat() {
		LinkedList<String> marques = new LinkedList<String>();

		marques.add("LeaderKakao"); // HQ
		marques.add("CacaoMagic"); // MQ
		return marques;
	}
}
//
