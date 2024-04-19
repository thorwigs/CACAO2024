package abstraction.eq9Distributeur2;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.IMarqueChocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;


public class Distributeur2MarqueDistributeur extends Distributeur2AppelOffre implements IMarqueChocolat {

	@Override
	public List<String> getMarquesChocolat() {
		// TODO Auto-generated method stub
		LinkedList<String> choco = new LinkedList<String>();
		choco.add("Ecacaodor");
		return choco;
	}



}
