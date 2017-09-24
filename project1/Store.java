import java.util.*;

public class Store {
  //Array of item prices
  private double[] prices;
  //Array of item  "Strings"
  private String[] menuItems;
  //Aray of coffee/submenu Price
  private double[] subMenuPrices;
  //Aray of coffee/submenu Price
  private String[] subMenuItems;

  //Input stream for interaction with user
  private Scanner inputStream;

  //Will keep track of quatities being purchased per item
  private int[] quantities;
  //Will keep track of coffee quantities
  private int[] subMenuQuantities;

  //Configurable tax rate
  private double taxRate;
  
  public Store(Scanner stdin) {
    //Initialization
    //Per project requirements, there will be 6 items to be purchased, and 3 coffee sizes
    prices    = new double[6];
    menuItems = new String[6];
    quantities = new int[6];

    subMenuItems = new String[3];
    subMenuPrices = new double[3];
    subMenuQuantities = new int[3];  

    taxRate = 0.05;

    inputStream = stdin;

  }
  /*
    showMenu - Iterates over items to show store menu
  */
  public void showMenu() {
    System.out.println("The Java Shoppe Menu:");
    for(int item = 0; item < menuItems.length; item++){
      System.out.println("\t" + (item + 1) + ".) " + menuItems[item]) ;
    }
    System.out.println("\t7.) Checkout / Print Receipt");
    System.out.print("Which item is being purchased? ");
  }

  /*
  * run - Activates a store "rung"
  */
  public void run(){
    
    //Builds out items, and Prices
    buildItemsArray();
    buildPricesArray();
    
    //Which menu option they are choosing
    int menuSelection = 0;
    int submenuSelection = 0;
    int subMenuQuantity = 0;
    String input = "";

    do {
      //Show menu
      showMenu();
      //Get input
      input = inputStream.nextLine();
      //Determine if input is valid
      if( isValidInput(input, false) ){
        menuSelection = Integer.parseInt(input);
        //Act on menu selection
        switch (menuSelection) {
          //If coffee, show submenu and parse the selection and quantity
          case 1:
            submenuSelection = displayAndParseSubmenu();
            subMenuQuantity = displayAndParseQuantity();
            addSubMenuItem(submenuSelection, subMenuQuantity );
            break;
          //Else, just add normally
          case 2:
          case 3:
          case 4:
          case 5:
          case 6:
            addMenuItem(menuSelection);
            break;
        }
      } else {
        //Show error message for invalid inputs
        System.out.println("Invalid input. Please enter a value from 1 to 7");
      }
    } while (menuSelection != 7);

    //Once the user has selected 7, print the 
    processCheckout();
    
  }
  /*
  * cashOut - Method used to take amount paid for order
  */
  private void cashOut(double total) {
    
    double tender = 0.00;
    String input  = "";
    do {
      System.out.print("Customer paid? ");
      input = inputStream.nextLine();
      try {
        tender = Double.parseDouble(input);
        if(tender < total) {
          System.out.println("Tender must be greater then the total");
        }
      } catch(Exception e) {
        tender = 0.00;
        System.out.println("Please enter a valid tender");

      }
    } while(tender < total);

    System.out.printf("Their change is $%,.2f", tender - total);
  }
  /*
  * processCheckout - Begins the checkout process by displaying subtotals and total
  */
  private void processCheckout() {
    double subTotal = 0.00;
    double total    = 0.00;
    subTotal  = calulateAndPrintItems();
    subTotal = calculateAndPrintDiscounts(subTotal);
    total = printSubTotalsCalulateTotal(subTotal);
    printLineItem("\tTotal:", total);
    cashOut(total);
  }
  
  /*
  * calulateAndPrintItems - Displays items in the order and their totals. Returns items subtotal
  */
  private double calulateAndPrintItems() {
    double subTotal = 0.00;

    if(quantities[0] > 0) {
      for(int item = 0; item < subMenuQuantities.length; item ++){
        if(subMenuQuantities[item] > 0) {
          subTotal += subMenuPrices[item];
          printLineItem(subMenuQuantities[item] +"\t" + subMenuItems[item] + " " + menuItems[0], subMenuPrices[item]);
        }
      }
    }
    for(int item = 1; item < quantities.length; item++){
      if(quantities[item] > 0) {
        subTotal += prices[item];
        printLineItem(quantities[item] +"\t" + menuItems[item], prices[item]);
      }
    }
    printLineBreak();

    return subTotal;
  }
  /*
  * calculateAndPrintDiscounts - Displays any available and subtracts them from the subtotal. Returns new subtotal
  */
  private double calculateAndPrintDiscounts(double subTotal) {
    boolean hasDiscount = false;
    if(quantities[0] > 0 && subMenuQuantities[2] > 0 && quantities[1] > 0) {
      double discount = -0.75;
      subTotal += discount;
      printLineItem("\tMondo Muffin Discount", -0.75);
      hasDiscount = true;
    }
    if(quantities[5] > 0) {
      double discount = 0.00;
      subTotal += discount;
      printLineItem("\tFree  chemistry textbook", 0.00);
      hasDiscount = true;

    }
    if(hasDiscount) {
      printLineBreak();
    }

    return subTotal;
  }
  /*
  * Prints the order subtotal, and tax amount. Returns the order total
  */
  private double printSubTotalsCalulateTotal(double subTotal){
    double taxes = subTotal * taxRate;
    double total = subTotal + taxes;
    
    printLineItem("\tSubtotal:", subTotal);
    printLineItem("\t5% Java Tax:", taxes);
    printLineBreak();
    
    return total;
  }
  /*
  * printLineItem - A way to abstract formatting. Takes a the line item string and the price
  */
  private void printLineItem(String str, double price){
    System.out.print(str + "\t");
    System.out.printf("$%,.2f", price);
    System.out.print("\n");
  }
  private void printLineBreak(){
    System.out.println("-------------------------------------");
  }

  /*
  * displayAndParseQuantity - Used to valid get and validate coffee size
  */
  private int displayAndParseQuantity(){
    boolean valid = false;
    int quantity = 0;
    do {
      System.out.print("How many? ");
      try {
        quantity = Integer.parseInt(inputStream.nextLine());
        valid = true;
      } catch(Exception e) {
        valid = false;
        System.out.println("Please enter a valid qunatity");
      }
    }while(!valid);

    return quantity;
  }
  /*
  * DispldisplayAndParseSubmenu - Displays the available coffee sizes, and gets/validates the input for coffee size selection
  */
  private int displayAndParseSubmenu() {
    String input = "";
    boolean valid = false;
    do{
      System.out.println("Coffe Sizes");
      for(int item = 0; item < subMenuItems.length; item++) {
        System.out.println("\t" + (item + 1)  + ".) " + subMenuItems[item]);
      }
      System.out.print("What size? ");
      input = inputStream.nextLine();
      valid = isValidInput(input, true);
      if(!valid) {
        System.out.println("Please enter a valid selection 1 - 3");
      }
    }while(!valid);

    return Integer.parseInt(input);
    
  } 
  /*
  * addMenuItem Increments the menu item that was selected
  */
  private void addMenuItem(int menuSelection){
      quantities[menuSelection - 1]++;
  }
  /*
  * addSubMenuItem - Sets the quanity of the number of coffess ordered
  */
  public void addSubMenuItem(int menuSelection, int quantity) {
    quantities[0]++;
    subMenuQuantities[menuSelection - 1 ] += quantity;
  }
  /* 
  * isValidInput - Make sure the value is an integer, and falls within the designated menu range
  */
  private boolean isValidInput(String input, boolean submenu){
    boolean valid = false;
    try {
      
      int value = Integer.parseInt(input);
      valid = submenu ? value > 0 && value <= 3 : value > 0 && value <= 7;
    } catch(Exception e) {
      valid = false;
    }
    return valid;
  }
  private void buildPricesArray(){
    
    //Submenu prices
    subMenuPrices[0] = 1.50;
    subMenuPrices[1] = 1.75;
    subMenuPrices[2] = 2.50;

    //Regular menu prices. Leave coffee as 0, since coffee charges will always be pulled from submenu
    prices[0] = 0.00;
    prices[1] = 1.75;
    prices[2] = 2.00;
    prices[3] = 2.50;
    prices[4] = 2.00;
    prices[5] = 4.00;
  }

  private void buildItemsArray(){
    //Submenu Items for coffee. 
    subMenuItems[0] = "Small";
    subMenuItems[1] = "Medium";
    subMenuItems[2] = "Mondo";

    //Regular menu items
    menuItems[0] = "Brewed Coffee";
    menuItems[1] = "Chocolate Chip Muffin";
    menuItems[2] = "Pot of Tea";
    menuItems[3] = "Hot chocolate";
    menuItems[4] = "Water";
    menuItems[5] = "Organic Water";
  }
}