package abstraction.eq4Transformateur1;

import java.util.HashMap;
import java.util.List;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Transformateur1Distribution {
    
    private Transformateur1Acteur transformateur;
    private double pourcentageVenteDirecte;
    private HashMap<ChocolatDeMarque, Variable> ventesDirectes;
    private Journal journal;

    public Transformateur1Distribution(Transformateur1Acteur transformateur, double pourcentageVenteDirecte) {
        this.transformateur = transformateur;
        this.pourcentageVenteDirecte = pourcentageVenteDirecte;
        this.ventesDirectes = new HashMap<>();
        this.journal = new Journal("Ventes Directes", transformateur);
        
        List<ChocolatDeMarque> chocosProduits = transformateur.getChocolatsProduits();
        for (ChocolatDeMarque choco : chocosProduits) {
            if (choco.getMarque().equals("LeaderKakao")) {
                this.ventesDirectes.put(choco, new Variable("Ventes directes de " + choco.getNom(), transformateur));
            }
        }
    }

    public void vendreDirectement(ChocolatDeMarque choco, double quantite) {
        if (this.ventesDirectes.containsKey(choco) && quantite > 0) {
            double stock = transformateur.getQuantiteEnStock(choco, transformateur.cryptogramme);
            double quantiteVendue = Math.min(stock * this.pourcentageVenteDirecte, quantite);
            this.ventesDirectes.get(choco).ajouter(this.transformateur, quantiteVendue);
            transformateur.stockChocoMarque.get(choco).retirer(this.transformateur, quantiteVendue);
            this.journal.ajouter("Vente directe de " + quantiteVendue + " tonnes de " + choco.getNom());
        }
    }

    public HashMap<ChocolatDeMarque, Variable> getVentesDirectes() {
        return this.ventesDirectes;
    }

    public Journal getJournal() {
        return this.journal;
    }
}