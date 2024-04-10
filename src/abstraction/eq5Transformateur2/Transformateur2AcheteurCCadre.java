package abstraction.eq5Transformateur2;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.general.Variable;

public class Transformateur2AcheteurCCadre extends Transformateur2Acteur implements IAcheteurContratCadre {
	private SuperviseurVentesContratCadre supCC;
	private List<ExemplaireContratCadre> contratsEnCours;
	private List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalCC;
	
	/////////////////
	// Constructor //
	/////////////////
	public Transformateur2AcheteurCCadre() {
		super(); //récupère les infos de Transformateur2Acteur
		this.contratsEnCours = new LinkedList<ExemplaireContratCadre>();
		this.contratsTermines = new LinkedList<ExemplaireContratCadre>();
		this.journalCC = new Journal(this.getNom()+" journal CC", this);
	}
	
	///////////////////////////
	// Initialise le contrat //
	///////////////////////////
	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Permet d'enregistrer et de garder une trace des contrats en cours et des anciens contrats //
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void next() {
		super.next();
		this.journalCC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
				for (Feve f : stockFeves.keySet()) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
					if (this.stockFeves.get(f)<1200) { // Modifier quantité minimale avant achat
						this.journalCC.ajouter("   "+f+" suffisamment peu en stock pour passer un CC");
						double parStep = 100; // Changer quantité par Step
						Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 5, parStep); // Changer le 5 (durée du contrat)
						List<IVendeurContratCadre> vendeurs = supCC.getVendeurs(f);
						if (vendeurs.size()>0) {
							IVendeurContratCadre vendeur = vendeurs.get(Filiere.random.nextInt(vendeurs.size())); // Choisir le vendeur avec d'autres motifs
							journalCC.ajouter("   "+vendeur.getNom()+" retenu comme vendeur parmi "+vendeurs.size()+" vendeurs potentiels");
							ExemplaireContratCadre contrat = supCC.demandeAcheteur(this, vendeur, f, e, cryptogramme, false);
							if (contrat==null) {
								journalCC.ajouter(Color.RED, Color.white,"   echec des negociations");
							} else {
								this.contratsEnCours.add(contrat);
								journalCC.ajouter(Color.GREEN, vendeur.getColor(), "   contrat signe : #"+contrat.getNumero()+" | Acheteur : "+contrat.getAcheteur()+" | Vendeur : "+contrat.getVendeur()+" | Produit : "+contrat.getProduit()+" | Quantité totale : "+contrat.getQuantiteTotale()+" | Prix : "+contrat.getPrix());
							}
						} else {
							journalCC.ajouter("   pas de vendeur");
					}
					} else {
						journalCC.ajouter(f+" suffisament de stock pour ne pas passer de contrat cadre");
					}
				}	
		// On archive les contrats termines
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getQuantiteRestantALivrer()==0.0 && c.getMontantRestantARegler()<=0.0) {
				journalCC.ajouter(Color.YELLOW, Color.BLACK,"Archivage du contrat : "+"#"+c.getNumero()+" | Acheteur : "+c.getAcheteur()+" | Vendeur : "+c.getVendeur()+" | Produit : "+c.getProduit()+" | Quantité totale : "+c.getQuantiteTotale()+" | Prix : "+c.getPrix());
				this.contratsTermines.add(c);
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			this.contratsEnCours.remove(c);
		}
		this.journalCC.ajouter("Nombre de contrats en cours : "+this.contratsEnCours.size());
		this.journalCC.ajouter("Nombre de contrats termines : "+this.contratsTermines.size());
		this.journalCC.ajouter("=================================");
	}
	
	////////////
	// Getter //
	////////////
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalCC);
		return jx;
	}
	
	
	/////////////////////////////////////////////
	//     Fonctions du protocole de Vente     //
	/////////////////////////////////////////////
	
	public boolean achete(IProduit produit) {
		return produit.getType().equals("F_MQ") || produit.getType().equals("F_MQ_E") || produit.getType().equals("F_BQ");
	}

	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getProduit().getType().equals("F_HQ") || contrat.getProduit().getType().equals("F_HQ_BE") || contrat.getProduit().getType().equals("F_HQ_E")) {
			return null; // retourne null si ce n'est pas la bonne fève
		} else {
			return contrat.getEcheancier(); // retourne l'échéancier proposé par le vendeur
		}
	}

	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getEcheancier().getQuantiteTotale()*5<contrat.getPrix()) {
			this.journalCC.ajouter("=========NEGOCIATION===========");
			this.journalCC.ajouter("Proposition d'un nouveau prix de : "+contrat.getEcheancier().getQuantiteTotale()*5);
			this.journalCC.ajouter("===============================");
			return contrat.getEcheancier().getQuantiteTotale()*5;
		} else {
			this.journalCC.ajouter("=========NEGOCIATION===========");
			this.journalCC.ajouter("Acceptation de la proposition de : "+contrat.getPrix());
			this.journalCC.ajouter("===============================");
			return contrat.getPrix();
			}
	}
	
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		journalCC.ajouter("Nouveau contrat accepté : "+"#"+contrat.getNumero()+" | Acheteur : "+contrat.getAcheteur()+" | Vendeur : "+contrat.getVendeur()+" | Produit : "+contrat.getProduit()+" | Quantité totale : "+contrat.getQuantiteTotale()+" | Prix : "+contrat.getPrix());	
		this.contratsEnCours.add(contrat);
	}


	public void receptionner(IProduit p, double quantiteEnTonnes, ExemplaireContratCadre contrat) {
		journalCC.ajouter("Réception de : "+quantiteEnTonnes+", tonnes de : "+p+" provenant du contrat : "+contrat.getNumero());
		stockFeves.put((Feve)p, stockFeves.get((Feve)p)+quantiteEnTonnes);
		totalStocksFeves.ajouter(this, quantiteEnTonnes, cryptogramme);
	}

}
