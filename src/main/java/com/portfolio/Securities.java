package com.portfolio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * This class contains the code to connect to Nexa Wealth Management Database
 * and perform various actions.
 *
 * @author Narmatha
 */
public class Securities {

  /** The name of the computer running MySQL. */
  private final String serverName = "localhost";

  /** The port of the MySQL server (default is 3306). */
  private final int portNumber = 3306;

  /** The name of the database. */
  private final String databaseName = "nexa_wealth_management";

  private final String databaseUsername = "root";

  private final String databasePassword = "Narmatha@1998";

  private Connection connection = null;

  public Securities() {
    try {
      Properties connectionProps = new Properties();
      connectionProps.put("user", this.databaseUsername);
      connectionProps.put("password", this.databasePassword);
      this.connection = DriverManager.getConnection("jdbc:mysql://"
              + this.serverName + ":" + this.portNumber + "/" +
              this.databaseName + "?characterEncoding=UTF-8&useSSL=false",
          connectionProps);
    } catch (SQLException e) {
      System.out.println("The application did not connect to the database");
      System.out.println("Please restart the application.");
      System.exit(0);
    }

    try{
      if(!this.connection.isValid(5)) {
        System.out.println("The application did not connect to the database");
        System.out.println("Please restart the application.");
        System.exit(0);
      }
    } catch (SQLException e) {
      System.out.println("The application did not connect to the database");
      System.out.println("Please restart the application.");
      System.exit(0);
    }
  }

  /**
   * The starting point of the program.
   * @param args the arguments.
   */
  public static void main(String[] args) {
    System.out.println("Welcome to Nexa Wealth Management!");
    Securities app = new Securities();
    app.run();
  }

  /**
   * The flow of the application with all inputs and outputs.
   */
  public void run() {

    boolean isNotExit = true;
    while(isNotExit) {
      System.out.println("Menu options :");
      System.out.println("1 -> Create account");
      System.out.println("2 -> Log in");
      System.out.println("3 -> Exit the application");
      System.out.println("Enter your choice : ");
      int option = scanner.nextInt();
      switch(option) {
        case 1:
          userCreateAccount();
          break;
        case 2:
          userLoginAndActions();
          break;
        case 3:
          isNotExit = false;
          break;
        default: System.out.println("Invalid option.");
      }
    }
    System.out.println("Thank you for using the application!");
  }

  private static void userCreateAccount() {

  }

  private static void userLoginAndActions() {
    System.out.println("Please enter username and password.");
    Scanner scanner =  new Scanner(System.in);
    String username = scanner.nextLine();
    String password = scanner.nextLine();
    boolean isValidCredential = true;//getAuthentication(username, password);
    if(!isValidCredential) {
      System.out.println("Invalid credentials. Please create an account.");
    } else {
      //Investor investor = new Investor();
      //System.out.println("Welcome " + investor.getFirstName() + " " + investor.getLastName());
      boolean isNotLoggedIn = true;
      while(isNotLoggedIn) {
        System.out.println("Dashboard options:");
        System.out.println("1 -> View Nominee");
        System.out.println("2 -> Add/Edit Nominee");
        System.out.println("3 -> Delete Nominee");
        System.out.println("4 -> Show last 10 transactions");
        System.out.println("5 -> Make a transaction");
        System.out.println("6 -> Logout");
        System.out.println("Enter your choice : ");
        int choice = scanner.nextInt();
        switch(choice) {
          case 1 :
            break;
          case 2:
            break;
          case 3:
            break;
          case 4:
            break;
          case 5:
            break;
          case 6:
            isNotLoggedIn = false;
            break;
        }
      }

      System.out.println("Logged out successfully!");
    }
  }


  /**
   * Fetches all the spell types.
   *
   * @param connection the database connection.
   * @return the list of spell types.
   */
  public List<String> getSpellTypeList(Connection connection) {
    List<String> spellTypeList = new ArrayList<>();
    String sql = "SELECT type_name FROM spell_type";
    PreparedStatement preparedStatement;
    try {
      preparedStatement = connection.prepareStatement(sql);
      ResultSet resultSet = preparedStatement.executeQuery();

      while(resultSet.next()) {
        spellTypeList.add(resultSet.getString("type_name"));
      }

      resultSet.close();
      preparedStatement.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println("ERROR: Could not fetch the spell types from database.");
    }
    return spellTypeList;
  }

  /**
   * Prints all spells with the particular spell type.
   * @param spellType the spell type to be queried for all spells.
   * @param connection the database connection.
   */
  public void displaySpells(String spellType, Connection connection) {
    String sql = "{ CALL spell_has_type(?) }";
    CallableStatement callableStatement;
    try {

      callableStatement = connection.prepareCall(sql);
      callableStatement.setString(1, spellType);
      ResultSet  resultSet = callableStatement.executeQuery();

      while (resultSet.next()) {
        int id = resultSet.getInt("spell_id");
        String name = resultSet.getString("spell_name");
        String alias = resultSet.getString("spell_alias");
        System.out.printf("ID: %d | Name: %s | Alias: %s%n", id, name, alias);
      }

      resultSet.close();
      callableStatement.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println("ERROR: Could not fetch the spells from database.");
    }
  }
}
