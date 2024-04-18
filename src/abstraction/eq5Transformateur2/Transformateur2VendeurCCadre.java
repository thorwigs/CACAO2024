package abstraction.eq5Transformateur2;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;


public class Transformateur2VendeurCCadre extends Transformateur2AcheteurCCadre implements IVendeurContratCadre {
	private HashMap<IAcheteurContratCadre, Integer> BlackListAcheteur;
	private int EtapenegoVente;
	
	public Transformateur2VendeurCCadre () {
		super();
	}
	/***
	 * @author Robin
	 */
	public void initialiser() {
		super.initialiser();
		this.BlackListAcheteur = new HashMap<IAcheteurContratCadre, Integer>();
	}
	
	/***
	 * @author Robin
	 */
	public void next() {
		super.next();
		this.journalCC.ajouter("===VENDEUR=========STEP"+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (ChocolatDeMarque cm : chocosProduits) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
			if (this.stockChocoMarque.get(cm)>0) { // Modifier quantité minimale avant achat
				this.journalCC.ajouter("   "+cm+" suffisamment de stock pour passer un CC");
				double parStep = 27500; // Changer quantité par Step
				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 78, parStep);
				List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(cm);
				if (acheteurs.size()>0) {
					IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
					for (IAcheteurContratCadre a : this.BlackListAcheteur.keySet()) {
						if (this.BlackListAcheteur.containsKey(acheteur)) {
							if (this.BlackListAcheteur.get(acheteur)>this.BlackListAcheteur.get(a)) { // Choisit le vendeur avec qui le moins de négociations a échoué
								acheteur = a;
							}
						}
					}
					journalCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");
					ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, cm, e, cryptogramme, false);
					if (contrat==null) {
						if (this.BlackListAcheteur.containsKey(acheteur)) {
							this.BlackListAcheteur.put(acheteur,this.BlackListAcheteur.get(acheteur)+1);
						} else {
							this.BlackListAcheteur.put(acheteur, 1);
						}
						journalCC.ajouter(Color.RED, Color.white,"   echec des negociations -- échec de "+this.BlackListAcheteur.get(acheteur)+" contrats avec : "+acheteur);
						this.EtapenegoVente=0;
					} else {
						this.contratsEnCours.add(contrat);
						this.EtapenegoVente=0;
						journalCC.ajouter(Color.GREEN, acheteur.getColor(), "   contrat signe : #"+contrat.getNumero()+" | Acheteur : "+contrat.getAcheteur()+" | Vendeur : "+contrat.getVendeur()+" | Produit : "+contrat.getProduit()+" | Quantité totale : "+contrat.getQuantiteTotale()+" | Prix : "+contrat.getPrix());
					}
				} else {
					journalCC.ajouter("   pas de vendeur");
					this.EtapenegoVente=0;
			}
			} else {
				journalCC.ajouter(cm+" pas asssez de stock pour passer un contrat cadre");
				this.EtapenegoVente=0;
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
	
	/***
	 * @author Robin, Erwann
	 */
	public boolean vend(IProduit produit) {
		return (this.chocosProduits.contains(produit) && this.getQuantiteEnStock(produit, cryptogramme)>0) ; //Valeur à changer
	}
	
	/***
	 * @author Robin, Vincent
	 */
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getEcheancier().getQuantiteTotale()< this.totalStocksChocoMarque.getValeur()){
			this.EtapenegoVente++;
			return new Echeancier(Filiere.LA_FILIERE.getEtape()+1,52,this.totalStocksChocoMarque.getValeur()/52) ; //on ramène la durée et la quantité aux bornes fixées
		}else {
			return contrat.getEcheancier();
			}
		}
	
	/***
	 * @author Robin,Erwann
	 */
	public double propositionPrix(ExemplaireContratCadre contrat) {
		ChocolatDeMarque cm = (ChocolatDeMarque)contrat.getProduit();
		Gamme gamme = cm.getGamme();
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		double prix = bourse.getCours(Feve.F_MQ).getMax()*2.5;
		if (gamme == Gamme.BQ) {
			prix = bourse.getCours(Feve.F_BQ).getMax()*2.0;
		}
		return prix;
	}
	/**
	 * @author Robin,Vincent
	 */
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if (Filiere.random.nextDouble() < 0.05) { // 5% des cas
	        return contrat.getPrix(); // ne refait pas de contreproposition
	    } else {
	    	EtapenegoVente++;
	        return contrat.getPrix() * 1.07; // Contreproposition de 7% à la hausse
	    }
	}
	
	/***
	 * @author Robin
	 */
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		if (contrat.getVendeur().equals(this)) {
			this.journalCC.ajouter("Nouveau contrat vendeur accepté : "+"#"+contrat.getNumero()+" | Acheteur : "+contrat.getAcheteur()+" | Vendeur : "+contrat.getVendeur()+" | Produit : "+contrat.getProduit()+" | Quantité totale : "+contrat.getQuantiteTotale()+" | Prix : "+contrat.getPrix());	
			this.contratsEnCours.add(contrat);
		} else {
			super.notificationNouveauContratCadre(contrat);
		}
	}
	
	/***
	 * @author Robin
	 */
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		this.journalCC.ajouter("Livraison de : "+quantite+", tonnes de :"+produit.getType()+" provenant du contrat : "+contrat.getNumero());
		this.stockChocoMarque.put((ChocolatDeMarque)produit, this.stockChocoMarque.get((ChocolatDeMarque)produit)-quantite);
		this.totalStocksChocoMarque.retirer(this, quantite, cryptogramme);
		return quantite;
		}

}
