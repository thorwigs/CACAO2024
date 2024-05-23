package abstraction.eq2Producteur2;

import java.util.List;

import abstraction.eqXRomu.encheres.Enchere;
import abstraction.eqXRomu.encheres.IVendeurAuxEncheres;
import abstraction.eqXRomu.encheres.SuperviseurVentesAuxEncheres;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;

/*
 * @author Maxime*/
public class Producteur2VendeurEncheres extends Producteur2VendeurCCadre implements IVendeurAuxEncheres {
	protected SuperviseurVentesAuxEncheres superviseur;
	private static int NB_INSTANCES = 0; // Afin d'attribuer un nom different a toutes les instances
	protected int numero;
	protected Integer cryptogramme;
	protected Journal journalE;
	protected Feve feve;
	protected double prixMin;
	
	public Producteur2VendeurEncheres() {
		super();
		this.journalE = new Journal(this.getNom()+" journal enchères", this);
	}
	public Producteur2VendeurEncheres(Feve f, double stock, double prixMin) {
		System.out.println(" deuxieme constructeur");
		if (f==null || stock<=0) {
			throw new IllegalArgumentException("creation d'une instance de ExempleAbsVendeurAuxEncheres avec des arguments non valides");
		}		
		NB_INSTANCES++;
		this.numero=NB_INSTANCES;
		//this.stock=new Variable(this.getNom()+"Stock"+f, this, 0.0, 1000000000.0,stock);
		this.feve = f;
		this.prixMin = prixMin;
		
	}
	
	public void initialiser() {
		super.initialiser();
		this.superviseur = (SuperviseurVentesAuxEncheres)(Filiere.LA_FILIERE.getActeur("Sup.Encheres"));
		this.journalE.ajouter("PrixMin=="+this.prixMin);
	}
	
	public void next() {
		super.next();

		if(feve==null) {
			
		}
		else{
			this.journalE.ajouter("Etape="+Filiere.LA_FILIERE.getEtape());
			if (Filiere.LA_FILIERE.getEtape()>=1) {
				//System.out.println("type de feve" + feve.getType());
				if (this.stock.get(feve)>200) {
					Enchere retenue = superviseur.vendreAuxEncheres(this, cryptogramme, feve, 200.0);
					if (retenue!=null) {
						this.stock_a_vendre(this.feve, this.stock.get(this.feve)-retenue.getMiseAuxEncheres().getQuantiteT());
						this.journalE.ajouter("vente de "+retenue.getMiseAuxEncheres().getQuantiteT()+" T a "+retenue.getAcheteur().getNom());
					} else {
						this.journalE.ajouter("pas d'offre retenue");
					}
				}
			}
		}
	}

	public Enchere choisir(List<Enchere> encheres) {
		this.journalE.ajouter("encheres : "+encheres);
		if (encheres==null) {
			return null;
		} else {
			Enchere retenue = encheres.get(0);
			if (retenue.getPrixTonne()>this.prixMin) {
				this.journalE.ajouter("  --> je choisis "+retenue);
				return retenue;
			} else {
				this.journalE.ajouter("  --> je ne retiens rien");
				return null;
			}
		}
	}
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalE);
		return jx;
	}
}
