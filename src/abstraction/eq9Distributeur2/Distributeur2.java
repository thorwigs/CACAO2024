package abstraction.eq9Distributeur2;

public class Distributeur2 extends Distributeur2MarqueDistributeur  {
	
	public Distributeur2() {
		super();
	}
	public void next() {
		super.next();
		System.out.println(this.totalStocksChocoMarque.getValeur(cryptogramme));
		}
}
