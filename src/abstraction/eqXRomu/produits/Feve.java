package abstraction.eqXRomu.produits;

public enum Feve implements IProduit {

	F_HQ_BE(Gamme.HQ, true, true), // FEVE HAUTE QUALITE BIO EQUITABLE
	F_HQ_E( Gamme.HQ, false,true), // FEVE HAUTE QUALITE EQUITABLE
	F_MQ_E( Gamme.MQ, false,true), // FEVE MOYENNE QUALITE EQUITABLE
	F_MQ  ( Gamme.MQ, false,false),// FEVE MOYENNE QUALITE (NI BIO NI EQUITABLE)
	F_BQ(   Gamme.BQ, false,false);// FEVE BASSE QUALITE (NI BIO NI EQUITABLE)
	
	private Gamme gamme;
	private boolean bio;
	private boolean equitable;

	
	Feve(Gamme gamme, boolean bio, boolean equitable) {
		this.gamme = gamme;
		this.bio = bio;
		this.equitable = equitable;
	}
	
	public String getType() {
		return "Feve";
	}

	public Gamme getGamme() {
		return this.gamme;
	}
	
	public boolean isBio() {
		return this.bio;
	}
	
	public boolean isEquitable() {
		return this.equitable;
	}
	
	/**
	 * @param f
	 * @return Retourne true si p est une Feve et la Feve this est au moins de la meme 
	 * qualite que p (et qu'il est donc possible d'employer la feve this en substitution
	 *  de la feve p).
	 * En particulier, un vendeur tenu de fournir de la feve F1 peut fournir
	 * une feve F2 si F2.greaterThan(F1), et un transformateur qui doit utiliser
	 * de la feve F1 peut utiliser de la feve F2 a la place si F2.greaterThan(F1)
	 */
	public boolean greaterThan(IProduit p) {
		Feve f = (p instanceof Feve ? (Feve)p:null);
		boolean labelsOK = f!=null && (!f.isEquitable()||this.isEquitable()) // si f n'est pas equitable alors peu importe que this soit ou non equitable
				                                    // mais si f est equitable alors this doit forcement etre equitable
				&& (!f.isBio()||this.isBio()); // si f n'est pas bio alors peu importe que this soit ou non bio
                                            // mais si f est bio alors this doit forcement etre bio
		return labelsOK && this.gamme.greaterThan(f.getGamme());	
	}
	
	/**
	 * Methode de classe (statique) permettant d'acceder a une Feve a partir de ses caracteristiques
	 * @param gamme
	 * @param bio
	 * @param equitable
	 * @return Retourne la feve ayant les caracteristiques precisees en parametre 
	 * (retourne null si aucune Feve n'existe avec de telles caracteristiques).
	 */
	public static Feve get(Gamme gamme, boolean bio, boolean equitable) {
		for (Feve c : Feve.values()) {
			if (c.gamme==gamme && c.bio==bio && c.equitable==equitable) {
				return c;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println("== Les differentes feves ==");
		for (Feve f : Feve.values()) {
			System.out.println(f);
		}
		
		Feve f = Feve.F_HQ_BE;
		System.out.println("\n"+f+" ->getGamme()="+f.getGamme()+"\n        ->IsBio()="+f.isBio()+"\n        ->IsEquitable()="+f.isEquitable()+"\n        ->getType()="+f.getType());
		
		System.out.println("\nFeve.get(Gamme.MQ, ,false, true)="+Feve.get(Gamme.MQ, false, true));
		for (Feve f1 : Feve.values()) {
			for (Feve f2 : Feve.values()) {
				if (f1.greaterThan(f2)) {
					System.out.println(f1+" greater than "+f2);
				}
			}
		}
	}
}
