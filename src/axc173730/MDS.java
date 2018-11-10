/**
 * 
 * @author pushpita panigrahi - pxp171530
 * @author deeksha lakshmeesh mestha - dxm172630
 * @author sneha hulivan girisha - sxh173730
 * @author akash chand - axc173730 
 * 
 * MDS class implements multi-dimensional search for objects having id, price(in dollar and cents) and list of description as long values
 */
package axc173730;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MDS {

	TreeMap<Long, Product> keyMap;
	HashMap<Long, TreeSet<Long>> descMap;

	private class Product {
		private Money price;
		private List<Long> description;

		public Product(Money price, List<Long> list) {
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
		//priceMap = new HashMap<>();
		descMap = new HashMap<>();
	}


	/**
	 * Adds new item to the store. Updates the price and description if item already
	 * present. If only price is supplied then updates only price keeping the old
	 * description
	 * 
	 * @param id,
	 *            item id to be added
	 * @param price,
	 *            price of item to be added
	 * @param list,
	 *            list of long values for description of item to be added
	 * @return 1 if new item is added to store, 0 if item is updated or could not be
	 *         added
	 */
	public int insert(long id, Money price, java.util.List<Long> list) {
		List<Long> deduped = list.stream().distinct().collect(Collectors.toList());
		list = deduped;
		Product product = keyMap.get(id);

		boolean result =false;
		if(product==null) {
			result=true;
			product =new Product(price,list);

			keyMap.put(id, product); // Add the product to the treemap
		} else {// when entry already exists
			List<Long> oldDescList = product.getDescription();
			product.setPrice(price);
			if (list!=null && !list.isEmpty())
				product.setDescription(list);


			if(list!=null && !list.isEmpty())
				removeDesc(id,oldDescList);
		}

		// update the ids on the new description list
		for (Long desc : list) {
			TreeSet<Long> descSet = descMap.get(desc);
			if (descSet == null) {
				descSet = new TreeSet<>();
				descSet.add(id);
				descMap.put(desc, descSet);
			} else
				descSet.add(id);
		}
		return result?1:0;
	}

	/**
	 * Deletes the item description from store
	 * @param id - product id
	 * @param oldDescList - product's old description list
	 * @return
	 */
	private long removeDesc(long id, List<Long> oldDescList) {
		long descSum = 0;
		for (Long desc : oldDescList) { // remove the old description list
			descSum+=desc;
			TreeSet<Long> descSet = descMap.get(desc);
			if (descSet != null) {
				descSet.remove(id);
				// if a particular description value does not have any ids, remove the
				// description value from hashmap
				if (descSet.size() == 0)
					descMap.remove(desc);
			}
		}
		return descSum;
	}

	// b. Find(id): return price of item with given id (or 0, if not found).
	/**
	 * Returns the price of the item with given id, 0 if item not found
	 * 
	 * @param id
	 *            to find price for
	 * @return price of item
	 */
	public Money find(long id) {
		Product product = keyMap.get(id);
		if (product == null)
			return new Money();

		return product.getPrice();
	}

	/**
	 * Deletes an item with given id from store. returns 0 if no such item
	 * 
	 * @param id,
	 *            description to be matched with
	 * @return sum of all ints in the description of deleted item
	 */
	public long delete(long id) {
		Product product = keyMap.get(id);
		long descSum = 0;
		if (product != null) {
			List<Long> descList = product.getDescription();
			// removing id from keymap
			keyMap.remove(id);
			descSum = removeDesc(id, descList);

		}
		return descSum;
	}

	/**
	 * Gives the min price among all items containing a given description. Returns 0
	 * if no item satisfy conditions
	 * 
	 * @param n,
	 *            description to be matched with
	 * @return min price among all products satisfying given condition
	 */
	public Money findMinPrice(long n) {
		TreeSet<Long> descSet = descMap.get(n);
		Money lowest = new Money(Long.MAX_VALUE+"");
		Money price;
		if(descSet == null) {
			return new Money();
		}else {
			for(Long id : descSet) {
				Product product = keyMap.get(id);
				price = product.getPrice();
				if(!(price.compareTo(lowest)>0)) {
					lowest = price;
				}
			}
		}
		return lowest;
	}

	/**
	 * Gives the max price among all items containing a given description. Returns 0
	 * if no item satisfy conditions
	 * 
	 * @param n,
	 *            description to be matched with
	 * @return max price among all products satisfying given condition
	 */
	public Money findMaxPrice(long n) {
		TreeSet<Long> descSet = descMap.get(n);
		Money highest = new Money(Long.MIN_VALUE+"");
		Money price;
		if(descSet == null) {
			return new Money();
		}else {
			for(Long id : descSet) {
				Product product = keyMap.get(id);
				price = product.getPrice();
				if(!(price.compareTo(highest)<0)) {
					highest = price;
				}
			}
		}
		return highest;
	}

	/**
	 * Gives the number of items containing given description and within given price
	 * range
	 * 
	 * @param n,
	 *            description to be matched with
	 * @param low,
	 *            lowest value for price range, inclusive
	 * @param high
	 *            highest value for price range, inclusive
	 * @return number of items satisfying both conditions
	 */
	public int findPriceRange(long n, Money low, Money high) {
		int result = 0;
		// get all ids matching given description
		TreeSet<Long> idsMatchingDesc = descMap.get(n);
		// for every id, get the price and check if it falls in range
		for (Long id : idsMatchingDesc) {
			Money price = keyMap.get(id).price;
			if (price.compareTo(low) >= 0 && price.compareTo(high) <= 0) {
				result++;
			}
		}
		return result;
	}


	/**
	 * increases the price of even item whose id is within given range by given rate
	 * percentage
	 * 
	 * @param l,
	 *            lower limit for ids
	 * @param h,
	 *            highest limit for ids
	 * @param rate,
	 *            % rate to be increased, eg: 10, 20.5, etc
	 * @return sum of net increases of the prices
	 */
	public Money priceHike(long l, long h, double rate) {
		Money price, newPrice=new Money(), netIncrease=new Money();
		BigDecimal newRate = BigDecimal.valueOf(rate).divide(BigDecimal.valueOf(100));
		long increase = 0, increasedPrice = 0, sum = 0;
		for(Long id : keyMap.keySet()) {
			if(id>=l && id<=h) {
				Product product = keyMap.get(id);
				price = product.getPrice();
				long priceInDouble = Money.getMoney(price);
				increase = newRate.multiply(BigDecimal.valueOf(priceInDouble)).longValue();
				increasedPrice = priceInDouble + increase;
				newPrice = Money.putMoney(increasedPrice);
				product.setPrice(newPrice);
				sum = sum + increase;
			}
		}
		
		netIncrease = Money.putMoney(sum);
		return netIncrease;
	}

	/**
	 * Remove elements of list from the description of id. Returns 0 if there is no
	 * such id.
	 * 
	 * @param id,
	 *            id of item to be updated
	 * @param list,
	 *            list of descriptions to be deleted from given item
	 * @return sum of the numbers that are actually deleted from description of id
	 */
	public long removeNames(long id, java.util.List<Long> list) {
		long sum = 0;//to keep track of sum of elements of description removed from id's
		if (list != null) {
			//iterate through the list passed.If the id is present in the description set of each description 
			//then remove the id from descMap and remove the description element from the keyMap.
			for (Long desc : list) {
				TreeSet<Long> descSet = descMap.get(desc);
				if (descSet != null) {
					if (descSet.contains(id)) {
						sum+=desc;
						descSet.remove(id);
						Product product = keyMap.get(id);
						product.description.remove(desc);
					}
					if (descSet.size() == 0)
						descMap.remove(desc);
				}	
					
				}
				
		}
		return sum;
	}

	public static class Money implements Comparable<Money> {
		long d;
		int c;

		public Money() {
			d = 0;
			c = 0;
		}

		public Money(long d, int c) {
			this.d = d;
			this.c = c;
		}

		public Money(String s) {
			String[] part = s.split("\\.");
			int len = part.length;
			if (len < 1) {
				d = 0;
				c = 0;
			} else if (part.length == 1) {
				d = Long.parseLong(s);
				c = 0;
			} else {
				d = Long.parseLong(part[0]);
				c = Integer.parseInt(part[1]);
			}
		}

		public long dollars() {
			return d;
		}

		public int cents() {
			return c;
		}

		/**
		 * Compares Money objects, returns -1 if this money < other money, 0 if equal,
		 * +1 if this money > other money
		 */
		public int compareTo(Money other) {
			if (this.d == other.d) {
				if (this.c == other.c)
					return 0;
				else if (this.c < other.c)
					return -1;
				else
					return 1;
			} else if (this.d < other.d)
				return -1;
			else
				return 1;
		}
		
		public String toString() {
			if (c > 10)
				return d + "." + c;
			else
				return d + ".0" + c;
		}

		@Override
		public boolean equals(Object obj) {
			return this.toString().equals(((Money) obj).toString());
		}

		@Override
		public int hashCode() {
			return this.toString().hashCode();
		}
		
		/**
		 * Converts a Money object to Long object, last 2 digits of long should be
		 * treated as cents, eg: 24.57 -> 2457
		 * 
		 * @param money,
		 *            Money object to be converted to long
		 * @return money value in long
		 */
		public static long getMoney(Money money) {
			return money.d * 100 + money.c;

		}

		/**
		 * Converts a Long to Money object, takes last 2 digits as cents eg: 567 -> 5.67
		 * 
		 * @param price,
		 *            Long value for money
		 * @return money as Money object
		 */
		public static Money putMoney(long price) throws NumberFormatException {

		
			long d = price/100;
			long cents = price - (d*100);
			int c= (int)cents;
			return new Money(d, c);
		}
	}

}