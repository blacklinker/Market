
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class customer {
	public static int interarrivalt=0;			//the inter-arrival time of the customer
	public static int shopt=0;					//the customer exit time spot of the shopping 
	public static int ATMt=0;					//the customer exit time spot of the ATM
	public static int checkt=0;					//the customer exit time spot of the checkout 
	public static Queue<Integer> shopque;		//the queue saves the shopping exit time spot of customers
	public static Queue<Integer> ATMque;		//the queue saves the ATM exit time spot of customers
	public static Queue<Integer> checkque;		//the queue saves the checkout exit time spot of customers
	
	public static double shop=0;					//save the total queue length of shopping
	public static double ATM=0;					//save the total queue length of ATM
	public static double checkout=0;				//save the total queue length of checkout

	public static int time=0;					//the time counter 
	protected static int totaltime =0;			//the total time 
	public static int ATMstat =0;				//the total time customers spent on ATM
	public static ArrayList<Integer> ATMpoll = new ArrayList<Integer>();	//the arraylist saves the exit time spots of the ATM
	public static int shopstat =0;				//the total time customers spent on shopping 
	public static ArrayList<Integer> shoppoll = new ArrayList<Integer>();	//the arraylist saves the exit time spots of the shopping
	public static int checkstat =0;				//the total time customers spent on checkout
	public static ArrayList<Integer> checkpoll = new ArrayList<Integer>();	////the arraylist saves the exit time spots of the checkout
	public static int customernum =0;			//the total number of the customers
	public static int atmuser=0;				//the total number of the ATM users
	
	public customer() {
		shopque = new LinkedList<Integer>();
		ATMque = new LinkedList<Integer>();
		checkque = new LinkedList<Integer>();
	
		ATMpoll.add(0);
		shoppoll.add(0);
		checkpoll.add(0);
		
 		totaltime(); 			//get the total time (like 5 hours)
 		int first = interarrivaltime();		//create the first interval time of first customer
 		interarrivalt += first;
 		time = first;
 		int money = moneyMaker();		//give a random money (0-40)
 		
 		System.out.println("first customer, money " +money);
		process(money);
		exit();
	}
	
	public void exit() {			//print all the result
		System.out.println("==========================================");
		System.out.println("In the end, Customers remaining in the checkout queue is "+checkque.size());
		System.out.println("In the end, Customers remaining in the shopping queue is "+shopque.size());
		System.out.println("In the end, Customers remaining in the ATM queue is "+ ATMque.size());
		System.out.println("==========================================");
		System.out.println("the Average time a customer spends at the Fish Market " + " " +(ATMt+checkt+shopt)/customernum+" seconds");
		System.out.println("the Average length of shopping " +(shop/5000) +" customers");
		System.out.println("the Average length of ATM " +(ATM/5000)+" customers");
		System.out.println("the Average length of checkout " +(checkout/5000)+" customers");
		if(atmuser == 0) {		//if the ATM user is 0, avoid division by 0, set to 1
			atmuser =1;
		}
		System.out.println("the Average time customers spend waiting in the ATM " + ATMt/atmuser+" seconds");
		System.out.println("the Average time customers spend waiting in the checkout "+checkt/customernum+" seconds");
		System.out.println("the Average time customers spend waiting in the shopping "+shopt/customernum+" seconds");
		
		
	}
	
	public void process(int money) {
		customernum++;				//count the number of the customers
		System.out.println("check the money of the customer (>20 or <20)");
		int temp1 = shoppingtime(money);		//create random shopping time (around fish*120seconds)
		int temp2 = ATMtime();					//create random ATM time (around 30 seconds per customer)
		if(money>=20) {							//if money is larger than 20, add to the shopping queue
			shopque.offer(money);
			shopt = shopt + temp1;				
			if(time>shoppoll.get(shoppoll.size()-1)) { //if there is no customer in the shopping queue
				shopstat = time + temp1;
				shoppoll.add(shopstat);			  //set the shopping exit time to (time now + fish *120 seconds)
				System.out.println("go to the shopping, exit time "+ shopstat +" money "+ money);
			}
			else {									//if there are customers in the shopping queue
				shopstat = shoppoll.get(shoppoll.size()-1)+temp1;
				shoppoll.add(shopstat);			  //set the shopping exit time to (the end of the queue exit time + fish *120 seconds)
				System.out.println("go to the shopping, exit time "+ shopstat +" money "+ money);
			}
			shopstat =0;
		}
		else {
			ATMque.offer(money);
			ATMt = ATMt + temp2;
			if(time>ATMpoll.get(ATMpoll.size()-1)) {	//if there is no customer in the ATM queue
				ATMstat = time + temp2;
				ATMpoll.add(ATMstat);				//set the ATM exit time to (time now + around 30 seconds)
				System.out.println("go to the ATM, exit time "+ ATMstat +" money "+ money);
			}
			else {								//if there are customers in the ATM queue
				ATMstat = ATMpoll.get(ATMpoll.size()-1)+temp2;
				ATMpoll.add(ATMstat);			//set the ATM exit time to (the end of the ATM queue exit time + 30 seconds)
				System.out.println("go to the ATM, exit time "+ ATMstat +" money "+ money);
			}
			ATMstat = 0;
		}
		newCustomer();	
	}
	
	public void check(int money) {
		int temp = Checkouttime(money);			//random checkout time (fish * 30 seconds)
		if(time > checkpoll.get(checkpoll.size()-1)) {		//if there is no customer in the checkout queue
			checkstat = time + temp;
			checkpoll.add(checkstat);					//set the checkout exit time to (time now + fish * 30 seconds)
			System.out.println("go to the check, exit time "+ checkstat +" money "+ money);
		}
		else {
			checkstat = checkpoll.get(checkpoll.size()-1)+temp;		//if there are customers in the checkout queue
			checkpoll.add(checkstat);								//set the checkout exit time to ( the end of the queue exit time + fish * 30 seconds)
			System.out.println("go to the check, exit time "+ checkstat +" money "+ money);
		}
		checkt = checkt+temp;			
		checkque.offer(money);
		checkstat =0;	
	}
	
	public void newCustomer() {
		int newcust = interarrivaltime();   // random interval arrival time 
		interarrivalt += newcust;			
		int money = moneyMaker();		//random money (0 - 40)
		while(time<totaltime) {			//time looping until the end (f.e. 5 hours)
			shop = shop + shopque.size();
			ATM = ATM + ATMque.size();
			checkout = checkout + checkque.size();
			time++;
			if(time == interarrivalt) {		//new customer arrived
				process(money);
			}
			if(ATMpoll.contains(time)) {	//exit time for the ATM queue
				int temp =0;
				temp = ATMque.poll()+ withdraw();		// one customer exit the ATM queue and money increase 10-40 
				atmuser++;
				
				System.out.println("	one ATM is polled, time " +time);
				System.out.println("after withdraw, new money "+ temp);
					process(temp);							//the customer after withdraw the money,(go to the go method) treat him like a new arrived customer, check the money of him (larger than 20 or not)
				}
			if(shoppoll.contains(time)) {			//exit time for the shopping queue
				System.out.println("	one shopping is polled, time "+ time);
				check(shopque.poll());				
			}
			if(checkpoll.contains(time)) {			//exit time for the checkout queue
				System.out.println("	one check is polled, time "+time);
				checkque.poll();
			}	
		}	
	}
	
	public Integer moneyMaker() {			//random money (0 - 40)
		int temp = (int)(Math.random()*40)+1;
		return temp;
	}
	
	public Integer interarrivaltime() {			// random interval arrival time 
		int temp = (int)(expo(120));
		return temp;
	}
	
	public void totaltime() {				// input the total time 
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter the total time (hours) : ");
		double temp = reader.nextFloat();
		totaltime = (int)(temp*3600);
	}
	
	public Integer shoppingtime(int money) {		//random shopping time (fish * 120 seconds)
		int fish = (int)(money/10);
		int temp = (int)(fish*(expo(120)));
		return temp;
	}
	
	public Integer ATMtime() {					//random ATM time (around 30 seconds)
		int temp = (int)(expo(30));
		return temp;
	}
			
	public Integer Checkouttime(int money) {		//random checkout time (fish * 30 seconds)
		int fish = (int)(money/10);
		int temp = (int)(fish*(expo(30)))+1;
		checkt = checkt+temp;
		return temp;
	}
	
	public Integer withdraw() {				//random withdraw (10 - 40)
		int temp =10 + (int)(Math.random()*30)+1;
		return temp;
	}
	static double expo( double mean){
	      double x = Math.random();
	      x = -mean * Math.log(x);
	      return x;
	}
	
	
	
	public static void main(String[] args) {
		customer x = new customer();	
		System.out.println("I set the average time spent in ATM around 30 seconds per customer");
	}
}
