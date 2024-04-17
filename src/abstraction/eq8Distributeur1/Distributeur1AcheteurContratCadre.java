package abstraction.eq8Distributeur1;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.ContratCadre;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;

public class Distributeur1AcheteurContratCadre extends Distributeur1Vendeur implements IAcheteurContratCadre{
	private SuperviseurVentesContratCadre supCC;
	protected List<ExemplaireContratCadre> contrat_en_cours;
	protected List<ExemplaireContratCadre> contrat_term;
	protected Journal journalCC;
	
	public Distributeur1AcheteurContratCadre() {
		super();
		this.contrat_en_cours = new LinkedList<ExemplaireContratCadre>();
		this.contrat_term= new LinkedList<ExemplaireContratCadre>();
		this.journalCC= new Journal (this.getNom() + "journal CC", this);
	}
	
	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}

	public String getNom() {
		return (super.getNom());
	}

	public Color getColor() {
		return(super.getColor());
	}

	public String getDescription() {
		return(super.getDescription());
	}
	
	public List<Variable> getIndicateurs() {
		return(super.getIndicateurs());
	}

	public List<Variable> getParametres() {
		return(super.getParametres());
	}

	public List<Journal> getJournaux() {
		List<Journal> jour = super.getJournaux();
		jour.add(journalCC);
		return jour;
	}

	public void setCryptogramme(Integer crypto) {
		super.setCryptogramme(crypto);	
	}

	public void notificationFaillite(IActeur acteur) {
		super.notificationFaillite(acteur);
		
	}


	public void notificationOperationBancaire(double montant) {
		super.notificationOperationBancaire(montant);
	}


	public List<String> getNomsFilieresProposees() {
		return(super.getNomsFilieresProposees());
	}

	public Filiere getFiliere(String nom) {
		return(super.getFiliere(nom));
	}

	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		return(super.getQuantiteEnStock(p, cryptogramme));
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
//				&& 1000 < this.prevision(produit, 24) - this.stock_Choco.get(produit) - a ); // a changer  
	}

	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")
			|| !this.stock_Choco.containsKey(contrat.getProduit())
			|| !this.achete(contrat.getProduit())) {
			return null;
		}
		Echeancier x = contrat.getEcheancier();
		if (x.getNbEcheances()>=24 && x.getNbEcheances()<=72
			&& this.achete(contrat.getProduit()) 
			&&	contrat.getQuantiteTotale()>= 30 ) {
			if (contrat.getProduit().toString().contains("C_BQ")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*40)/(x.getNbEcheances()*100*100)) {
			} 
			if ( contrat.getProduit().toString().contains("C_MQ")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*15)/(x.getNbEcheances()*100*100))  {
				}
			if (contrat.getProduit().toString().contains("C_MQ_E")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*15)/(x.getNbEcheances()*100*100)) {
				}
			if (contrat.getProduit().toString().contains("C_HQ")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*15)/(x.getNbEcheances()*100*100)) {
				}
			if (contrat.getProduit().toString().contains("C_HQ_E")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*10)/(x.getNbEcheances()*100*100)) {
				} 
			if (contrat.getProduit().toString().contains("C_HQ_BE")
				&& contrat.getQuantiteTotale()<= (7200000*24*40*5)/(x.getNbEcheances()*100*100)) {
				}	
		} else {
			int a = Filiere.LA_FILIERE.getEtape()+1;
			int b = 24 ; 
			double c = this.prevision(contrat.getProduit(), b) ;	
			double d = 0 ; 
			for (int i=0; i<contrat_en_cours.size(); i++) {
				if (contrat_en_cours.get(i).getProduit().equals(contrat.getProduit())) {
					d = d + contrat_en_cours.get(i).getQuantiteRestantALivrer();
				}
			}
			double e = this.stock_Choco.get(contrat.getProduit()); 
		    x = new Echeancier (a,b,50);	
//		    x = new Echeancier (a,b,c-d-e);
		}
		return x;
	}
	
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("ChocolatDeMarque")) {
			return 0.0;
		}
		
//		if (this.prix_a_perte(contrat.getProduit(),contrat.getPrix())<=this.prix(((ChocolatDeMarque)contrat.getProduit()))){
//			&& contrat.getPrix()<=Math.pow(contrat.getQuantiteTotale(),1/3)){ //peut être rajouter un coef
//				return contrat.getPrix();
//		}
		if (contrat.getPrix()>0) {
			return contrat.getPrix();
		}
		else {
			return Math.pow(contrat.getQuantiteTotale(),1/3)*(1-1/Math.pow(2, contrat.getListePrix().size()))  ;
		}
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.contrat_en_cours.add(contrat);
		this.journalCC.ajouter("Nouveau contrat cadre :"+contrat);
	}

	public void receptionner(IProduit p, double quantiteEnTonnes, ExemplaireContratCadre contrat) {
		this.journalCC.ajouter("Reception de "+quantiteEnTonnes+" T de "+p+" du contrat "+contrat.getNumero());
		stock_Choco.put((ChocolatDeMarque)p, stock_Choco.get((ChocolatDeMarque)p)+quantiteEnTonnes);
		this.totalStockChoco.ajouter(this, quantiteEnTonnes,cryptogramme);
	}
	
	public double prix_a_perte(IProduit p, Double prix) {
		return prix + (1350*(stock_Choco.get(p)*this.prix(((ChocolatDeMarque)p)))) + 120 / 10375;
	}
	
	public double prevision (IProduit p,int b) {
		double d = 0.0;
		int a = Filiere.LA_FILIERE.getEtape();
		for (int i =a-b; i<a ; i++ ) {
			d = d + Filiere.LA_FILIERE.getVentes((ChocolatDeMarque)p,i);
		}
		d = d * (1+(Filiere.LA_FILIERE.getIndicateur("C.F. delta annuel max conso").getValeur() + Filiere.LA_FILIERE.getIndicateur("C.F. delta annuel min conso").getValeur())/2);
		return d ; 
	}
	
	public void next() {
		super.next();
		this.journalCC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (ExemplaireContratCadre contrat : contrat_en_cours) {
			if (contrat.getMontantRestantARegler()==0 && contrat.getQuantiteRestantALivrer()==0) {
				contrat_term.add(contrat);
			} else {
				stock_Choco.put((ChocolatDeMarque)contrat.getProduit(),contrat.getQuantiteALivrerAuStep() );
				totalStockChoco.ajouter(this, contrat.getQuantiteALivrerAuStep(), cryptogramme);
			}
			
		}
		for(ExemplaireContratCadre contrat : contrat_term) {
			contrat_en_cours.remove(contrat);
		}
		for (ChocolatDeMarque choc : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (this.achete(choc)) {
				this.journalCC.ajouter("Recherche d'un vendeur aupres de qui acheter");
				List<IVendeurContratCadre> vendeurs = supCC.getVendeurs(choc);
				if (vendeurs.contains(this)) {
					vendeurs.remove(this);
				}
				IVendeurContratCadre vendeur = null;
				if (vendeurs.size()==1) {
					vendeur=vendeurs.get(0);
				} else if (vendeurs.size()>1) {
					vendeur = vendeurs.get((int)( Filiere.random.nextDouble()*vendeurs.size())); // a améliorer dans la V2
				}
				if (vendeur!=null) {
					this.journalCC.ajouter("Demande au superviseur de debuter les negociations pour un contrat cadre de "+choc+" avec le vendeur "+vendeur);
					int a = Filiere.LA_FILIERE.getEtape()+1;
					int b = 24 ; 
					double c = this.prevision(choc, b) ;	
					double d = 0 ; 
					for (int i=0; i<contrat_en_cours.size(); i++) {
						if (contrat_en_cours.get(i).getProduit().equals(choc)) {
							d = d + contrat_en_cours.get(i).getQuantiteRestantALivrer();
						}
					}
					double e = this.stock_Choco.get(choc); 
				    Echeancier x = new Echeancier (a,b,50);
					ExemplaireContratCadre cc = supCC.demandeAcheteur((IAcheteurContratCadre)this, vendeur, choc, x, cryptogramme,false);
					this.journalCC.ajouter("-->aboutit au contrat "+cc);
				}	
			}
		}
		
		this.journalCC.ajouter("=================================");

	}
}
