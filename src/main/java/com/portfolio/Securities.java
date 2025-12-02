package com.portfolio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    /** The username of the database connection. */
  private final String databaseUsername = "root";

    /** The password of the database connection. */
  private final String databasePassword = "Narmatha@1998";

  /** The database connection. */
  private Connection connection = null;

  /** Instantiates the database connection for the application. */
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
    Scanner scanner =  new Scanner(System.in);
    boolean isNotExit = true;
    while(isNotExit) {
      System.out.println("\n\n\nMenu options :");
      System.out.println("1 -> Create account");
      System.out.println("2 -> Log in");
      System.out.println("3 -> Exit the application");
      System.out.print("Enter your choice : ");
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

    try {
        this.connection.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        System.out.println("ERROR: Could not close the database connection.");
    }
    System.out.println("\nThank you for using the application!");
  }

    /**
     * Creates the investor account.
     */
  private void userCreateAccount() {
      Scanner scanner = new Scanner(System.in);

    System.out.println("Please provide the following details.");

    System.out.print("First Name : ");
    String firstName = scanner.next();
    System.out.print("Last Name : ");
    String lastName = scanner.next();
    System.out.print("Username : ");
    String username = scanner.next();
    System.out.print("Password : ");
    String password = scanner.next();
    System.out.print("SSN :");
    int ssn = scanner.nextInt();
    System.out.print("Date of Birth (YYYY-MM-DD):");
    String dateOfBirth = scanner.next();
    System.out.print("Phone number : ");
    String phoneNumber = scanner.next();
    System.out.print("Address Street Number :");
    int streetNumber = scanner.nextInt();
    System.out.print("Address Street Name :");
    String streetName = scanner.next();
    System.out.print("Address City :");
    String city = scanner.next();
    System.out.print("Address State :");
    String state = scanner.next();
    System.out.print("Address ZipCode :");
    int zipcode = scanner.nextInt();
    System.out.print("Account category (Classic/Preferred/Standard) :");
    String category = scanner.next();

    try {

        String sql = "INSERT INTO investor (first_name, last_name, username, password, category_type, ssn, date_of_birth, phone_number, " +
                "address_street_number, address_street_name, address_city, address_state, address_zipcode) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);

        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setString(3, username);
        preparedStatement.setString(4, password);
        preparedStatement.setString(5, category);
        preparedStatement.setInt(6, ssn);
        preparedStatement.setDate(7, Date.valueOf(dateOfBirth));
        preparedStatement.setString(8, phoneNumber);
        preparedStatement.setInt(9, streetNumber);
        preparedStatement.setString(10, streetName);
        preparedStatement.setString(11, city);
        preparedStatement.setString(12, state);
        preparedStatement.setInt(13, zipcode);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Investor registered successful!");
        } else {
            System.out.println("Registration failed. Try again.");
        }

        preparedStatement.close();

    } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR: Could not register user into the database. Try again.");
    }
  }

    /**
     * Handles login of the investors and the actions they can perform.
     */
  private void userLoginAndActions() {
    System.out.println("Please enter the credentials :");
    Scanner scanner =  new Scanner(System.in);
    System.out.print("Username : ");
    String username = scanner.next();
      System.out.print("Password : ");
    String password = scanner.next();

    Investor investor = getInvestor(username, password);

    if(investor == null || investor.getId() == null) {
      System.out.println("Invalid credentials. Please create an account.");
    } else {
      System.out.println("\nWelcome " + investor.getFirstName() + " " + investor.getLastName());

      boolean isNotLoggedIn = true;
      while(isNotLoggedIn) {
        System.out.println("\n\nDashboard options:");
        System.out.println("1 -> View Nominee");
        System.out.println("2 -> Add/Edit Nominee");
        System.out.println("3 -> Delete Nominee");
        System.out.println("4 -> View portfolios");
        System.out.println("5 -> Create/Modify portfolio");
        System.out.println("6 -> Request for a portfolio manager");
        System.out.println("7 -> Search for securities");
        System.out.println("8 -> Show last 10 transactions");
        System.out.println("9 -> Make a transaction");
        System.out.println("10 -> Logout");
        System.out.print("Enter your choice : ");
        int choice = scanner.nextInt();
        switch(choice) {
          case 1 :
            viewNominee(investor.getId());
            break;
          case 2:
            addOrEditNominee(investor.getId());
            break;
          case 3:
            deleteNominee(investor.getId());
            break;
          case 4:
            viewPortfolios();
            break;
          case 5:
            createPortfolio();
            break;
          case 6:
            requestPortfolioManager();
            break;
          case 7:
            searchSecurities();
            break;
          case 8:
            showLatestTransactions();
            break;
          case 9:
            makeTransaction();
            break;
          case 10:
            isNotLoggedIn = false;
            break;
          default: System.out.println("Invalid option.");
        }
      }

      System.out.println("Logged out successfully!");
    }
  }

    /**
     * Gets the investor details from login.
     *
     * @param username the username
     * @param password the password
     * @return the investor details.
     */
  private Investor getInvestor(String username, String password) {
      Investor investor = null;
      String sql = "{CALL login(?, ?)}";
      try {
          CallableStatement callableStatement = this.connection.prepareCall(sql);

          callableStatement.setString(1, username);
          callableStatement.setString(2, password);

          boolean hasResultSet = callableStatement.execute();

          if (hasResultSet) {
              ResultSet resultSet = callableStatement.getResultSet();
              while (resultSet.next()) {
                  investor = new Investor(resultSet.getInt("investor_id"),
                          resultSet.getString("first_name"), resultSet.getString("last_name"),
                          resultSet.getString("username"), resultSet.getString("password"),
                          resultSet.getString("category_type"), resultSet.getInt("ssn"),
                          resultSet.getDate("date_of_birth"), resultSet.getInt("phone_number"),
                          resultSet.getInt("address_street_number"), resultSet.getString("address_street_name"),
                          resultSet.getString("address_city"), resultSet.getString("address_state"),
                          resultSet.getInt("address_zipcode"));
              }
              resultSet.close();
          }
          callableStatement.close();

      } catch(Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not fetch the investor details. Try again.");
      }
      return investor;
  }

    /**
     * Fetches the nominee details for an investor id.
     */
  private void viewNominee(Integer investorId) {
      String sql = "{CALL get_nominee(?)}";
      try {
          CallableStatement callableStatement = this.connection.prepareCall(sql);

          callableStatement.setInt(1, investorId);

          boolean hasResultSet = callableStatement.execute();

          if (hasResultSet) {
              ResultSet resultSet = callableStatement.getResultSet();
              if(resultSet.next()) {
                  System.out.println("Nominee First Name : " + resultSet.getString("first_name"));
                  System.out.println("Nominee Last Name : " + resultSet.getString("last_name"));
                  System.out.println("Nominee SSN : " + resultSet.getInt("ssn"));
              } else {
                  System.out.println("The investor has no nominee.");
              }
              resultSet.close();
          }
          callableStatement.close();

      } catch(Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not fetch the nominee details. Try again.");
      }
  }

    /**
     * Adds or updates nominee for an investor id.
     *
     * @param investorId the investor id.
     */
  private void addOrEditNominee(Integer investorId) {

      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter the nominee details : ");
      System.out.print("First Name : ");
      String firstName = scanner.next();
      System.out.print("Last Name : ");
      String lastName = scanner.next();
      System.out.print("SSN : ");
      int ssn = scanner.nextInt();
      System.out.print("Date of Birth (YYYY-MM-DD) : ");
      String dateOfBirth = scanner.next();
      System.out.print("Phone number : ");
      String phoneNumber = scanner.next();
      System.out.print("Address Street Number : ");
      int streetNumber = scanner.nextInt();
      System.out.print("Address Street Name : ");
      String streetName = scanner.next();
      System.out.print("Address City : ");
      String city = scanner.next();
      System.out.print("Address State : ");
      String state = scanner.next();
      System.out.print("Address ZipCode : ");
      int zipcode = scanner.nextInt();

      try {

          String sql = "{CALL modify_nominee(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

          CallableStatement callableStatement = this.connection.prepareCall(sql);

          callableStatement.setInt(1, investorId);
          callableStatement.setInt(2, ssn);
          callableStatement.setString(3, firstName);
          callableStatement.setString(4, lastName);
          callableStatement.setDate(5, Date.valueOf(dateOfBirth));
          callableStatement.setString(6, phoneNumber);
          callableStatement.setInt(7, streetNumber);
          callableStatement.setString(8, streetName);
          callableStatement.setString(9, city);
          callableStatement.setString(10, state);
          callableStatement.setInt(11, zipcode);

          callableStatement.execute();

          System.out.println("Nominee added successful!");

          callableStatement.close();

      } catch (Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not add nominee into the database. Try again.");
      }
  }

  private void deleteNominee(Integer investorId) {
      String sql = "{CALL delete_nominee(?)}";
      try {
          CallableStatement callableStatement = this.connection.prepareCall(sql);

          callableStatement.setInt(1, investorId);

          callableStatement.execute();

          System.out.println("The nominee has been deleted successfully!");

          callableStatement.close();

      } catch(Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not fetch the nominee details. Try again.");
      }
  }

  private void viewPortfolios() {

  }

  private void createPortfolio() {

  }

  private void requestPortfolioManager() {

  }

  private void searchSecurities() {
      Scanner scanner = new Scanner(System.in);
      System.out.print("What type of security do you want to search for? (stock/bond/mutual fund/etf) : ");
      String type = scanner.next();
      System.out.print("What risk level securities do you want to look for? (High/Medium/Low) : ");
      String riskLevel = scanner.next();
      String sql = "{CALL search_securities(?, ?)}";
      try {
          CallableStatement callableStatement = this.connection.prepareCall(sql);

          callableStatement.setString(1, riskLevel);
          callableStatement.setString(2, type);

          boolean hasResultSet = callableStatement.execute();

          if (hasResultSet) {
              ResultSet resultSet = callableStatement.getResultSet();
              switch (type) {
                  case "stock":
                      System.out.println("Stocks of risk level " + riskLevel + ":");
                      while (resultSet.next()) {
                          System.out.println(
                                  "ID: " + resultSet.getInt("security_id") +
                                          ", Name: " + resultSet.getString("security_name") +
                                          ", Company: " + resultSet.getString("issuing_company") +
                                          ", Price: " + resultSet.getDouble("market_price") +
                                          ", Sector: " + resultSet.getString("sector") +
                                          ", Currency: " + resultSet.getString("currency") +
                                          ", Exchange: " + resultSet.getString("exchange_name") +
                                          ", Dividend Yield: " + resultSet.getDouble("divident_yield") +
                                          ", Dividend Frequency: " + resultSet.getInt("divident_frequency") +
                                          ", Shares Outstanding: " + resultSet.getDouble("shares_outstanding")
                          );
                      }
                      break;

                  case "bond":
                      System.out.println("Bonds of risk level " + riskLevel + ":");
                      while (resultSet.next()) {
                          System.out.println(
                                  "ID: " + resultSet.getInt("security_id") +
                                          ", Name: " + resultSet.getString("security_name") +
                                          ", Company: " + resultSet.getString("issuing_company") +
                                          ", Price: " + resultSet.getDouble("market_price") +
                                          ", Sector: " + resultSet.getString("sector") +
                                          ", Currency: " + resultSet.getString("currency") +
                                          ", Face Value: " + resultSet.getDouble("face_value") +
                                          ", Coupon Rate: " + resultSet.getDouble("coupon_rate") +
                                          ", Coupon Frequency: " + resultSet.getInt("coupon_frequency") +
                                          ", Maturity Date: " + resultSet.getDate("maturity_date") +
                                          ", Credit Rating: " + resultSet.getString("credit_rating")
                          );
                      }
                      break;

                  case "mutual fund":
                      System.out.println("Mutual Funds of risk level " + riskLevel + ":");
                      while (resultSet.next()) {
                          System.out.println(
                                  "ID: " + resultSet.getInt("security_id") +
                                          ", Name: " + resultSet.getString("security_name") +
                                          ", Company: " + resultSet.getString("issuing_company") +
                                          ", Price: " + resultSet.getDouble("market_price") +
                                          ", Sector: " + resultSet.getString("sector") +
                                          ", Currency: " + resultSet.getString("currency") +
                                          ", Benchmark: " + resultSet.getString("benchmark_name") +
                                          ", NAV: " + resultSet.getDouble("nav") +
                                          ", Expense Ratio: " + resultSet.getDouble("expense_ratio") +
                                          ", Distribution Yield: " + resultSet.getDouble("distribution_yield") +
                                          ", Distribution Frequency: " + resultSet.getInt("distribution_frequency") +
                                          ", Fund Type: " + resultSet.getString("fund_type")
                          );
                      }
                      break;

                  case "etf":
                      System.out.println("ETFs of risk level " + riskLevel + ":");
                      while (resultSet.next()) {
                          System.out.println(
                                  "ID: " + resultSet.getInt("security_id") +
                                          ", Name: " + resultSet.getString("security_name") +
                                          ", Company: " + resultSet.getString("issuing_company") +
                                          ", Price: " + resultSet.getDouble("market_price") +
                                          ", Sector: " + resultSet.getString("sector") +
                                          ", Currency: " + resultSet.getString("currency") +
                                          ", Benchmark: " + resultSet.getString("benchmark_name") +
                                          ", NAV: " + resultSet.getDouble("nav") +
                                          ", Expense Ratio: " + resultSet.getDouble("expense_ratio") +
                                          ", Distribution Yield: " + resultSet.getDouble("distribution_yield") +
                                          ", Distribution Frequency: " + resultSet.getInt("distribution_frequency") +
                                          ", Liquidity: " + resultSet.getString("liquidity")
                          );
                      }
                      break;

                  default:
                      System.out.println("Unknown security type: " + type);
                      break;
              }
              resultSet.close();
          }
          callableStatement.close();

      } catch(Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not fetch the nominee details. Try again.");
      }
  }

  private void showLatestTransactions() {

  }

  private void makeTransaction() {

  }
}
