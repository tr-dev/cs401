import java.util.*;

public class Store {
  private double[] prices;
  private String[] menuItems;
  private String[] subMenuItems;
  private double[] subMenuPrices;
  private Scanner inputStream;
  private int[] quantities;
  private int[] subMenuQuantities;
  
  
  public Store(Scanner stdin) {
    prices = new double[6];
    menuItems = new String[6];

    subMenuItems = new String[3];
    subMenuPrices = new double[3];
    quantities = new int[6];
    subMenuQuantities = new int[3];  

    inputStream = stdin;

  }
  public void showMenu() {
    System.out.println("The Java Shoppe Menu:");
    for(int item = 0; item < menuItems.length; item++){
      System.out.println("\t" + (item + 1) + ".) " + menuItems[item]) ;
    }
    System.out.println("\t7.) Checkout / Print Receipt");
    System.out.print("Which item is being purchased? ");
  }

  public void run(){
    
    buildItemsArray();
    buildPricesArray();
    
    
    int menuSelection = 0;
    int submenuSelection = 0;
    int subMenuQuantity = 0;
    String input = "";

    do {
      showMenu();
      input = inputStream.nextLine();
      if( isValidInput(input, false) ){
        menuSelection = Integer.parseInt(input);
        switch (menuSelection) {
          case 1:
            submenuSelection = displayAndParseSubmenu();
            subMenuQuantity = displayAndParseQuantity();
            addSubMenuItem(submenuSelection, subMenuQuantity );
            break;
          case 2:
          case 3:
          case 4:
          case 5:
          case 6:
            addMenuItem(menuSelection);
            break;
        }
      } else {
        System.out.println("Invalid input. Please enter a value from 1 to 7");
      }
    } while (menuSelection != 7);

    printReceipt();
  }

  private void printReceipt() {
    printItems();
  }
  
  private void printItems() {
    if(quantities[0] > 0) {
      for(int item = 0; item < subMenuQuantities.length; item ++){
        if(subMenuQuantities[item] > 0) {
          System.out.print(subMenuQuantities[item] +"\t" + subMenuItems[item] + " " +  menuItems[0] + "\t\t\t");
          System.out.printf("$% .2f", subMenuPrices[item]);
          System.out.print("\n");
        }
      }
    }
    for(int item = 1; item < quantities.length; item++){
      if(quantities[item] > 0) {
        System.out.print(quantities[item] +"\t" + menuItems[item] + "\t\t\t");
        System.out.printf("$%,.2f", prices[item]);
        System.out.print("\n");
      }
    }
  }
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
  private void addMenuItem(int menuSelection){
      quantities[menuSelection - 1]++;
  }
  public void addSubMenuItem(int menuSelection, int quantity) {
    quantities[0]++;
    subMenuQuantities[menuSelection - 1 ] += quantity;
  }
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
    
    subMenuPrices[0] = 1.50;
    subMenuPrices[1] = 1.75;
    subMenuPrices[2] = 2.50;

    prices[0] = -1.00;
    prices[1] = 1.75;
    prices[2] = 2.00;
    prices[3] = 2.50;
    prices[4] = 2.00;
    prices[5] = 4.00;
  }

  private void buildItemsArray(){

    subMenuItems[0] = "Small";
    subMenuItems[1] = "Medium";
    subMenuItems[2] = "Mondo";

    menuItems[0] = "Brewed Coffee";
    menuItems[1] = "Chocolate Chip Muffin";
    menuItems[2] = "Pot of Tea";
    menuItems[3] = "Hot chocolate";
    menuItems[4] = "Water";
    menuItems[5] = "Organic Water";
  }
}