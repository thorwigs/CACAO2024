package abstraction.eqXRomu.produits;

import abstraction.eqXRomu.filiere.Filiere;

public class ChocolatDeMarque implements IProduit {
	private Chocolat chocolat;
	private String marque;
	private int pourcentageCacao; // Pourcentage de cacao, de [0, 100]

	public ChocolatDeMarque(Chocolat chocolat, String marque, int pourcentageCacao) {
		if (chocolat==null) {
			throw new IllegalArgumentException("Tentative de creer une instance de ChocolatDeMarque avec null pour premier parametre");
		}
		if (marque==null) {
			throw new IllegalArgumentException("Tentative de creer une instance de ChocolatDeMarque avec null pour second parametre");
		}
		if (marque.length()<1) {
			throw new IllegalArgumentException("Tentative de creer une instance de ChocolatDeMarque avec une chaine vide pour second parametre");
		}
		if (Filiere.LA_FILIERE!=null && !Filiere.LA_FILIERE.getMarquesChocolat().contains(marque)) {
			throw new IllegalArgumentException("Tentative de creer une instance de ChocolatDeMarque de marque \""+marque+"\" alors qu'aucun acteur n'a depose cette marque");
		}
		if (pourcentageCacao<0 || pourcentageCacao>100) {
			throw new IllegalArgumentException("Tentative de creer une instance de ChocolatDeMarque de marque \""+marque+"\" en specifiant "+pourcentageCacao+" pour pourcentage de Cacao");
		}
		if ( Filiere.LA_FILIERE!=null && Filiere.LA_FILIERE.getEtape()>0 &&
			(	(chocolat.getGamme()==Gamme.HQ && pourcentageCacao<Filiere.LA_FILIERE.getParametre("pourcentage min cacao HQ").getValeur())
				||(chocolat.getGamme()==Gamme.MQ && pourcentageCacao<Filiere.LA_FILIERE.getParametre("pourcentage min cacao MQ").getValeur())
				||(chocolat.getGamme()==Gamme.BQ && pourcentageCacao<Filiere.LA_FILIERE.getParametre("pourcentage min cacao BQ").getValeur()))) {
			throw new IllegalArgumentException("Tentative de creer une instance de ChocolatDeMarque de marque \""+marque+"\" en specifiant "+pourcentageCacao+" pour pourcentage de Cacao pour un chocolat de gamme "+chocolat.getGamme());
		}
		this.chocolat = chocolat;
		this.marque = marque;
		this.pourcentageCacao = pourcentageCacao;
	}

	public String getType() {
		return "ChocolatDeMarque";
	}

	public String getNom() {
		return chocolat.name()+" "+marque;
	}

	public Chocolat getChocolat() {
		return chocolat;
	}

	public int getPourcentageCacao() {
		return this.pourcentageCacao;
	}

	public Gamme getGamme() {
		return this.chocolat.getGamme();
	}

	public boolean isEquitable() {
		return this.chocolat.isEquitable();
	}

	public boolean isBio() {
		return this.chocolat.isBio();
	}

	public String getMarque() {
		return marque;
	}

	/**
	 * 
	 * @return La qualite percue d'un chocolat qui depend
	 * de la qualite intrinseque du chocolat (sa gamme et son
	 * pourcentage de cacao) mais aussi de la qualite
	 * percue de sa marque, du fait qu'il soit bioEquitable ou pas
	 * et le pourcentage reverse pour la RSE.
	 */
	public double qualitePercue() {
		double importanceMarque = Filiere.LA_FILIERE.getParametre("impact marque qualite percue").getValeur();
		double importanceCacao = Filiere.LA_FILIERE.getParametre("impact cacao qualite percue").getValeur();

		double deltaCacao=0.0;
		if (this.getGamme()==Gamme.HQ) {
			deltaCacao = (this.pourcentageCacao - Filiere.LA_FILIERE.getParametre("pourcentage min cacao HQ").getValeur())
					/(99.0-Filiere.LA_FILIERE.getParametre("pourcentage min cacao HQ").getValeur());
		} else if (this.getGamme()==Gamme.MQ) {
			deltaCacao = (this.pourcentageCacao - Filiere.LA_FILIERE.getParametre("pourcentage min cacao MQ").getValeur())
					/(Filiere.LA_FILIERE.getParametre("pourcentage min cacao HQ").getValeur()-Filiere.LA_FILIERE.getParametre("pourcentage min cacao MQ").getValeur());
		} else { // Basse qualite
			deltaCacao = (this.pourcentageCacao - Filiere.LA_FILIERE.getParametre("pourcentage min cacao BQ").getValeur())
					/(Filiere.LA_FILIERE.getParametre("pourcentage min cacao MQ").getValeur()-Filiere.LA_FILIERE.getParametre("pourcentage min cacao BQ").getValeur());
		}
		return (1-importanceMarque)*this.getChocolat().qualite()
				+importanceMarque*Filiere.LA_FILIERE.qualitePercueMarque(this.marque)
				+importanceCacao*deltaCacao;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chocolat == null) ? 0 : chocolat.hashCode());
		result = prime * result + ((marque == null) ? 0 : marque.hashCode());
		return result;
	}

	/**
	 * this est egal a un objet obj si et seulement si
	 * (1) obj est un ChocolatDeMarque
	 * (2) de la meme marque que this
	 * (3) et leurs chocolats sont egaux
	 * 
	 * Le taux de cacao n'intervient pas dans l'egalite : les deux ont forcement au moins
	 * le taux requis et le fait que l'un des deux a un peu plus de cacao n'en fait pas un 
	 * chocolat different (juste un peu plus apprecie par les clients).
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (!(obj instanceof ChocolatDeMarque)) {
			return false;
		} else {
			ChocolatDeMarque cdmobj = (ChocolatDeMarque) obj;
			return cdmobj.getChocolat()!=null 
					&& cdmobj.getMarque()!=null 
					&& cdmobj.getChocolat().equals(this.getChocolat()) 
					&& cdmobj.getMarque().equals(this.getMarque());
		}
	}

	public String toString() {
		return this.chocolat.toString()+"_"+this.marque;
	}

	/**
	 * this peut se substituer a p si
	 * p est du chocolat de la meme marque que this et la qualite de this est au moins egale a celui de p
	 * ou p est du chocolat sans marque et la qualite de this est au moins egale a celle de p
	 */
	public boolean greaterThan(IProduit p) {
		if (p instanceof ChocolatDeMarque) {
			return (this.getMarque().equals(((ChocolatDeMarque)p).getMarque()))
					&& this.getChocolat().greaterThan((((ChocolatDeMarque)p).getChocolat()));
		} else {
			return (p instanceof Chocolat)
					&& this.getChocolat().greaterThan(p);
		}
	}
}
