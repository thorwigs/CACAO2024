package abstraction.eq4Transformateur1;

import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.ventesComptoir.IVendeurComptoir;

/**
 * Classe gérant les ventes au comptant (marché physique) du transformateur 1.
 * 
 * @Author Nouveau_Auteur
 */
public class Transformateur1VendeurComptoir extends Transformateur1AcheteurCCadre implements IVendeurComptoir {
    
    protected Journal journalVenteComptoir;

    public Transformateur1VendeurComptoir() {
        super();
        this.journalVenteComptoir = new Journal(this.getNom() + " journalVenteComptoir", this);
    }

    @Override
    public List<Journal> getJournaux() {
        List<Journal> res = super.getJournaux();
        res.add(journalVenteComptoir);
        return res;
    }

    @Override
    public void next() {
        super.next();
        this.journalVenteComptoir.ajouter("=== STEP " + Filiere.LA_FILIERE.getEtape() + " ===");
        // Implémentation de la logique de vente au comptant
    }

    /**
     * Méthode pour obtenir la liste des chocolats de marque disponibles pour la vente au comptant.
     * 
     * @return une liste de chocolats de marque disponibles.
     */
    @Override
    public List<ChocolatDeMarque> getChocolatsEnVente() {
        List<ChocolatDeMarque> chocoEnVente = new ArrayList<>();
        for (ChocolatDeMarque choco : this.stockChocoMarque.keySet()) {
            if (this.stockChocoMarque.get(choco).getValeur() > 0) {
                chocoEnVente.add(choco);
            }
        }
        return chocoEnVente;
    }

    /**
     * Méthode pour vendre une quantité de chocolat de marque à un prix donné.
     * 
     * @param choco le chocolat de marque à vendre.
     * @param quantite la quantité en tonnes à vendre.
     * @param prix le prix par tonne.
     * @return true si la vente a été réalisée, false sinon.
     */
    @Override
    public boolean vendre(ChocolatDeMarque choco, double quantite, double prix) {
        if (this.stockChocoMarque.get(choco).getValeur() >= quantite) {
            this.stockChocoMarque.get(choco).retirer(this, quantite, this.cryptogramme);
            this.totalStocksChocoMarque.retirer(this, quantite, this.cryptogramme);
            this.journalVenteComptoir.ajouter("Vente de " + quantite + "T de " + choco + " au prix de " + prix + "€/T");
            return true;
        } else {
            this.journalVenteComptoir.ajouter("Échec de la vente de " + quantite + "T de " + choco + " (stock insuffisant)");
            return false;
        }
    }
    
    /**
     * Méthode pour notifier la vente réalisée.
     * 
     * @param choco le chocolat de marque vendu.
     * @param quantite la quantité en tonnes vendue.
     * @param prix le prix par tonne.
     */
    @Override
    public void notificationVente(ChocolatDeMarque choco, double quantite, double prix) {
        this.journalVenteComptoir.ajouter("Notification: Vente de " + quantite + "T de " + choco + " au prix de " + prix + "€/T");
    }
}