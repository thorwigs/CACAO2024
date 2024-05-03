package abstraction.eq6Transformateur3;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.ExempleTransformateurContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur3VendeurCCadre extends Transformateur3Produit implements IVendeurContratCadre{

	public Transformateur3VendeurCCadre() {
		super();
	}
	
	
	/**
	 * @author Thomas 
	 */
	public void next() {
		super.next();
		this.journalCC6.ajouter("=== Partie Vente chocolat ====================");
		for (ChocolatDeMarque c : chocosProduits) { 
			if (stockChocoMarque.get(c) - restantDu(c)>200) { 
				this.journalCC6.ajouter("   "+c+" suffisamment en stock pour passer un CC");
				double parStep = Math.max(100, (stockChocoMarque.get(c)-restantDu(c))/12); 
				
				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);
				List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(c);
				if (acheteurs.size()>0) {
					IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
					journalCC6.ajouter(Color.BLACK,Color.WHITE,"   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");
					ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, c, e, cryptogramme, false);
					if (contrat == null) {
						journalCC6.ajouter(Color.RED, Color.BLACK,"   echec des negociations");
					}
					else {
						this.contratsEnCours.add(contrat);
						journalCC6.ajouter(Color.GREEN, Color.BLACK, "   contrat signe");
					}
				} else {
					journalCC6.ajouter("   pas d'acheteur");
				}
			}
		}
		//On archive les contrats termines
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getQuantiteRestantALivrer()==0.0) {
				this.contratsTermines.add(c);
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			this.contratsEnCours.remove(c);
		}
		this.journalCC6.ajouter("=== Partie réception Fèves ====================");
	}
	/**
	 * @author Thomas
	 */
	public double restantDu(ChocolatDeMarque c) {
		double res=0;
		for (ExemplaireContratCadre p : this.contratsEnCours) {
			if (p.getProduit().equals(c)) {
				res+=p.getQuantiteRestantALivrer();
			}
		}
		return res;
	}
	/**
	 * @author Thomas
	 */
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		return jx;
	}

	/**
	 * @author Arthur
	 */
	public boolean vend(IProduit produit) {
		
		return (this.chocosProduits.contains(produit) && this.getQuantiteEnStock(produit, cryptogramme)- restantDu((ChocolatDeMarque)produit)>200) ;
		
	}
	/**
	 * @author Thomas
	 */
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		float T = 0;
		for(ChocolatDeMarque c : stockChocoMarque.keySet()) {
			T += stockChocoMarque.get(c);
		}
		
		if ( contrat.getEcheancier().getQuantiteTotale()< T){
			return contrat.getEcheancier();
		}
		else {
			return null;
		}
	}

	/**
	 * @author Thomas et Cédric
	 */
	public double propositionPrix(ExemplaireContratCadre contrat) {
		
		Chocolat c = ((ChocolatDeMarque) contrat.getProduit()).getChocolat();
		Feve f = super.Correspond(c);
		double prix = coûtMoyenAchatFeve.get(f) + 0.5 * 1200 + 8; // prise en compte du cout de production ( pas exactement car non prise en compte de la qualité de notre chocolat,0.5 choisi arbitrairementet(pourcentage d'adjuvants)) et du prix moyen de la tonne de fève qu'on achète dans nos contrats en cours
		
		return 1.03 * prix; //  un petit +3% pour maximiser le prfofit
		
	}
	/**
	 * @author Thomas et Cédric
	 */
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if (Filiere.random.nextDouble()<0.5) {
			return 0.5*contrat.getPrix(); // Premier pile ou face, pour savoir si on négocie ou non (ici si la 
									  // condition est vérifiée on ne négocie pas)
		} 
		else if (Filiere.random.nextDouble()<0.5){ // Deuxième pile ou face, s'il est vérifié on négocie la vente à +4,9%
			return 1.049*contrat.getPrix();
		}
		else {
			return 1.099*contrat.getPrix();  //  Si les 2 premiers pile ou face ont échoué, on négocie la vente à +9,9%
		}
	}

	/**
	 * @author Arthur
	 */
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		if(contrat.getAcheteur()==this) {
			journalCC6.ajouter(Color.ORANGE, Color.WHITE,"Nouveau contrat initié par un producteur");
			journalCC6.ajouter("Nouveau contrat accepté : "+"#"+contrat.getNumero()+" | Acheteur : "+contrat.getAcheteur()+" | Vendeur : "+contrat.getVendeur()+" | Produit : "+contrat.getProduit()+" | Quantité totale : "+contrat.getQuantiteTotale()+" | Prix : "+contrat.getPrix());	
			this.contratsEnCours.add(contrat);
		}
		else {
	
			this.journalCC6.ajouter("Nouveau contrat de ventes " + contrat.getNumero());
			this.contratsEnCours.add(contrat);
		}
	}
	/**
	 * @author Arthur
	 */
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		this.journalCC6.ajouter("Livraison de : "+quantite+", tonnes de :"+produit.getType()+" provenant du contrat : "+contrat.getNumero());
		this.stockChocoMarque.put((ChocolatDeMarque)produit, this.stockChocoMarque.get((ChocolatDeMarque)produit)-quantite);
		this.totalStocksChocoMarque.retirer(this, quantite, cryptogramme);
		return quantite;
		}
	

}

