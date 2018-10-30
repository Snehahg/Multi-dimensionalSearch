/** Starter code for LP3
 *  @author
 */

// Change to your net id
package axc173730;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;


// If you want to create additional classes, place them in this file as subclasses of MDS

public class MDS {
	// Add fields of MDS here

	TreeMap<Long, Product> keyMap;
	HashMap<Money, TreeSet<Long>> priceMap;
	HashMap<Long, TreeSet<Long>> descMap;
	private class Product{
		private Money price;
		private List<Long> description;
		public Product( Money price, List<Long> list){
			this.price = price;
			this.description = new LinkedList<>(list);
		}
		
		public Money getPrice() {
			return price;
		}
		public void setPrice(Money price) {
			this.price = price;
		}
		public List<Long> getDescription() {
			return description;
		}
		public void setDescription(List<Long> description) {
			this.description = new LinkedList<>(description);
		}
		
	}
	// Constructors
	public MDS() {
		keyMap = new TreeMap<>();
		priceMap = new HashMap<>();
		descMap = new HashMap<>();
	}

	/* Public methods of MDS. Do not change their signatures.
       __________________________________________________________________
       a. Insert(id,price,list): insert a new item whose description is given
       in the list.  If an entry with the same id already exists, then its
       description and price are replaced by the new values, unless list
       is null or empty, in which case, just the price is updated. 
       Returns 1 if the item is new, and 0 otherwise.
	 */
	public int insert(long id, Money price, java.util.List<Long> list) {
		
		Product product = keyMap.get(id);
		boolean result =false;
		if(product==null) {
			result=true;
			product =new Product(price,list);
			keyMap.put(id, product); // Add the product to the treemap
		}else {
			Money oldPrice = product.getPrice();
			List<Long> oldDescList = product.getDescription();
			product.setPrice(price);
			if(list!=null)
				product.setDescription(list);
			
			if(!oldPrice.equals(price)) { // if new price is different, remove the product id from treeset of the old price
				TreeSet<Long> priceSet = priceMap.get(oldPrice);
				priceSet.remove(id);
				if(priceSet.size()==0)
					priceMap.remove(oldPrice);
			}
			
			for(Long desc : oldDescList) { //remove the old description list
				TreeSet<Long> descSet = descMap.get(desc);
				if(descSet!=null) {
					descSet.remove(id);
					if(descSet.size()==0) // if a particular description value does not have any ids, remove the description value from hashmap
						descMap.remove(desc);
				}
			}
		}
		TreeSet<Long> priceSet = priceMap.get(price); 
		if(priceSet==null) {
			priceSet = new TreeSet<>();
			priceSet.add(id);
			priceMap.put(price, priceSet);
		}else
			priceSet.add(id); //update the ids on new price
		for(Long desc : list) {
			TreeSet<Long> descSet = descMap.get(desc); // update the ids on the new description list
			if(descSet==null) {
				descSet = new TreeSet<>();
				descSet.add(id);
				descMap.put(desc, descSet);
			}else
				descSet.add(id);
		}
		printMaps();
		return result?1:0;
	}

	private void printMaps() {
		// TODO Auto-generated method stub
		System.out.println("KeyMap : ");
		for(Long id : keyMap.keySet()) {
			System.out.print(id+" : "+keyMap.get(id).price+"\t");
			for(Long desc : keyMap.get(id).getDescription()) {
				System.out.print(desc+"\t");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("PriceMap : "+priceMap.size());
		for(Money price : priceMap.keySet()) {
			System.out.print(price+" : ");
			for(Long ids : priceMap.get(price)) {
				System.out.print(ids+"	");
			}
			System.out.println();
		}
		System.out.println("DescMap : "+descMap.size());
		for(Long desc : descMap.keySet()) {
			System.out.print(desc+" : ");
			for(Long ids : descMap.get(desc)) {
				System.out.print(ids+"	");
			}
			System.out.println();
		}
	}

	// b. Find(id): return price of item with given id (or 0, if not found).
	public Money find(long id) {
		Product product = keyMap.get(id);
		if(product==null)
			return new Money();
		
		return product.getPrice();
	}

	/* 
       c. Delete(id): delete item from storage.  Returns the sum of the
       long ints that are in the description of the item deleted,
       or 0, if such an id did not exist.
	 */
	public long delete(long id) {
		return 0;
	}

	/* 
       d. FindMinPrice(n): given a long int, find items whose description
       contains that number (exact match with one of the long ints in the
       item's description), and return lowest price of those items.
       Return 0 if there is no such item.
	 */
	public Money findMinPrice(long n) {
		return new Money();
	}

	/* 
       e. FindMaxPrice(n): given a long int, find items whose description
       contains that number, and return highest price of those items.
       Return 0 if there is no such item.
	 */
	public Money findMaxPrice(long n) {
		return new Money();
	}

	/* 
       f. FindPriceRange(n,low,high): given a long int n, find the number
       of items whose description contains n, and in addition,
       their prices fall within the given range, [low, high].
	 */
	public int findPriceRange(long n, Money low, Money high) {
		return 0;
	}

	/* 
       g. PriceHike(l,h,r): increase the price of every product, whose id is
       in the range [l,h] by r%.  Discard any fractional pennies in the new
       prices of items.  Returns the sum of the net increases of the prices.
	 */
	public Money priceHike(long l, long h, double rate) {
		return new Money();
	}

	/*
      h. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
	 */
	public long removeNames(long id, java.util.List<Long> list) {
		return 0;
	}

	// Do not modify the Money class in a way that breaks LP3Driver.java
	public static class Money implements Comparable<Money> { 
		long d;  int c;
		public Money() { d = 0; c = 0; }
		public Money(long d, int c) { this.d = d; this.c = c; }
		public Money(String s) {
			String[] part = s.split("\\.");
			int len = part.length;
			if(len < 1) { d = 0; c = 0; }
			else if(part.length == 1) { d = Long.parseLong(s);  c = 0; }
			else { d = Long.parseLong(part[0]);  c = Integer.parseInt(part[1]); }
		}
		public long dollars() { return d; }
		public int cents() { return c; }
		public int compareTo(Money other) { // Complete this, if needed

			return new Double(Double.parseDouble(this.toString())).compareTo(Double.parseDouble(other.toString()));
		}
		public String toString() { return d + "." + c; }
		@Override
		public boolean equals(Object obj) {
			
			return this.toString().equals(((Money)obj).toString());
		}
		@Override
		public int hashCode() {
			
			return this.toString().hashCode();
		}
	}

}