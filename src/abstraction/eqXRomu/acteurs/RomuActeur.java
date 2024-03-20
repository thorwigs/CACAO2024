package abstraction.eqXRomu.acteurs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.FiliereTestBourse;
import abstraction.eqXRomu.clients.FiliereTestClientFinal;
import abstraction.eqXRomu.contratsCadres.FiliereTestContratCadre;
import abstraction.eqXRomu.encheres.FiliereTestEncheres;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariableReadOnly;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class RomuActeur implements IActeur {
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	public static Color COLOR_BROWN  = new Color(141,100,  7);
	public static Color COLOR_PURPLE = new Color(100, 10,115);
	public static Color COLOR_LPURPLE= new Color(155, 89,182);
	public static Color COLOR_GREEN  = new Color(  6,162, 37);
	public static Color COLOR_LGREEN = new Color(  6,255, 37);
	public static Color COLOR_LBLUE = new Color(  6,130,230);

	
	protected Integer cryptogramme;
	protected Journal journal;

	private Variable qualiteHaute;  // La qualite d'un chocolat de gamme haute 
	private Variable qualiteMoyenne;// La qualite d'un chocolat de gamme moyenne  
	private Variable qualiteBasse;  // La qualite d'un chocolat de gamme basse
	private Variable gainQualiteBio;// Le gain en qualite des chocolats bio
	private Variable gainQualiteEquitable;// Le gain en qualite des chocolats equitables
	private Variable partMarqueQualitePercue;// Le gain en qualite des chocolats originaux
	private Variable pourcentageMinCacaoBQ; //Le pourcentage minimal de cacao dans un chocolat de basse qualite
	private Variable pourcentageMinCacaoMQ; //Le pourcentage minimal de cacao dans un chocolat de moyenne qualite
	private Variable pourcentageMinCacaoHQ; //Le pourcentage minimal de cacao dans un chocolat de haute qualite
	private Variable partCacaoQualitePercue ;//L'impact d'un % de cacao plus eleve dans la qualite percue du chocolat
	private Variable coutStockageProducteur;//Le cout moyen du stockage d'une Tonne a chaque step chez un producteur de feves
	private Variable coutMiseEnRayon;//Le cout moyen pour la mise en rayon d'une tonne de chocolat

	protected List<Feve> lesFeves;
	
	public RomuActeur() {
		this.qualiteHaute   = new VariableReadOnly("qualite haute", "<html>Qualite du chocolat<br>de gamme haute</html>",this, 0.0, 10.0, 3.0);
		this.qualiteMoyenne = new VariableReadOnly("qualite moyenne", "<html>Qualite du chocolat<br>de gamme moyenne</html>",this, 0.0, 10.0, 2.0);
		this.qualiteBasse   = new VariableReadOnly("qualite basse", "<html>Qualite du chocolat<br>de gamme basse</html>",this, 0.0, 10.0, 1.0);
		this.gainQualiteBio  = new VariableReadOnly("gain qualite bio", "<html>Gain en qualite des<br>chocolats bio</html>",this, 0.0, 5.0, 0.5);
		this.gainQualiteEquitable  = new VariableReadOnly("gain qualite equitable", "<html>Gain en qualite des<br>chocolats equitables</html>",this, 0.0, 5.0, 0.5);
		this.partMarqueQualitePercue  = new VariableReadOnly("impact marque qualite percue", "<html>% de la qualite percue de la marque dans la qualite percue du chocolat</html>",this, 0.0, 0.5, 0.3);

		this.pourcentageMinCacaoBQ  = new VariableReadOnly("pourcentage min cacao BQ", "<html>Le pourcentage minimal de cacao dans un chocolat de basse qualite</html>",this, 30.0, 45.0, 30.0);
		this.pourcentageMinCacaoMQ  = new VariableReadOnly("pourcentage min cacao MQ", "<html>Le pourcentage minimal de cacao dans un chocolat de moyenne qualite</html>",this, 45.0, 60.0, 50.0);
		this.pourcentageMinCacaoHQ  = new VariableReadOnly("pourcentage min cacao HQ", "<html>Le pourcentage minimal de cacao dans un chocolat de haute qualite</html>",this, 60.0, 90.0, 80.0);
		this.partCacaoQualitePercue = new VariableReadOnly("impact cacao qualite percue", "<html>L'impact d'un % de cacao plus eleve dans la qualite percue du chocolat</html>",this, 0.0, 0.5, 0.3);
		
		this.coutStockageProducteur = new VariableReadOnly("cout moyen stockage producteur", "<html>Le cout moyen du stockage par step d'une Tomme de produit chez un producteur</html>",this, 0.0, 100.0, 7.5);
		this.journal = new Journal("Journal "+this.getNom(), this);
		this.coutMiseEnRayon  = new VariableReadOnly("cout mise en rayon", "<html>Le cout pour la mise en rayon d'une tonne de chocolat</html>",this, 0.0, 1000.0, 34.19);
	}

	//========================================================
	//                         IActeur
	//========================================================


	public void initialiser() {
		this.lesFeves = new LinkedList<Feve>();
		this.journal.ajouter("Les Feves sont :");
		for (Feve f : Feve.values()) {
			this.lesFeves.add(f);
			this.journal.ajouter("   - "+f);
		}
	}
	
	public String getNom() {
		return "EQX";
	}

	public String getDescription() {
		return "";
	}

	public Color getColor() {
		return new Color(165,165,165);
	}

	public void next() {
	}

	public List<Variable> getIndicateurs() {
		List<Variable> res =  new ArrayList<Variable>();
		return res;
	}
	
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	public List<Variable> getParametres() {
		List<Variable> p= new ArrayList<Variable>();
		p.add(this.qualiteHaute);
		p.add(this.qualiteMoyenne);
		p.add(this.qualiteBasse);
		p.add(this.gainQualiteBio);
		p.add(this.gainQualiteEquitable);
		p.add(this.partMarqueQualitePercue);
		p.add(this.pourcentageMinCacaoBQ);
		p.add(this.pourcentageMinCacaoMQ);
		p.add(this.pourcentageMinCacaoHQ);
		p.add(this.partCacaoQualitePercue);
		p.add(this.coutStockageProducteur);
		p.add(this.coutMiseEnRayon);
		return p;
	}

	public List<Journal> getJournaux() {
		List<Journal> res = new LinkedList<Journal>();
		res.add(this.journal);
		return res;
	}

	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
			System.out.println("They killed Romu... ");
		} else {
			System.out.println("Poor "+acteur.getNom()+"... We will miss you. "+this.getNom());
		}
	}

	public void notificationOperationBancaire(double montant) {
	}

	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		filieres.add("TESTCLIENT"); 
		filieres.add("TESTCC"); 
		filieres.add("TESTBOURSE"); 
		filieres.add("TESTENCHERES"); 
		return filieres;
	}

	public Filiere getFiliere(String nom) {
		switch (nom) { 
		case "TESTCLIENT" : return new FiliereTestClientFinal();
		case "TESTCC" : return new FiliereTestContratCadre();
		case "TESTBOURSE" : return new FiliereTestBourse();
		case "TESTENCHERES" : return new FiliereTestEncheres();
		default : return null;
		}
	}
	
	public String toString() {
		return this.getNom();
	}

	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		return 0;
	}
}
