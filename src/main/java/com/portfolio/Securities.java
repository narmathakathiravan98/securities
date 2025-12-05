package com.portfolio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Scanner;

/**
 * This class contains the code to connect to Nexa Wealth Management Database
 * and perform various actions.
 *
 * @author Narmatha Kathiravan
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
    System.out.print("Address Street Number : ");
    int streetNumber = scanner.nextInt();
    scanner.nextLine();
    System.out.print("Address Street Name : ");
    String streetName = scanner.nextLine();
    System.out.print("Address City : ");
    String city = scanner.nextLine();
    System.out.print("Address State (eg. MA/NY): ");
    String state = scanner.next();
    System.out.print("Address ZipCode : ");
    int zipcode = scanner.nextInt();
    System.out.print("Account category (Classic/Preferred/Standard) : ");
    String category = scanner.next();
    scanner.nextLine();
    System.out.print("Email ID : ");
    String email = scanner.nextLine();

    try {

        String sql = "{CALL register_investor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        CallableStatement callableStatement = this.connection.prepareCall(sql);

        callableStatement.setString(1, firstName);
        callableStatement.setString(2, lastName);
        callableStatement.setString(3, username);
        callableStatement.setString(4, password);
        callableStatement.setString(5, category);
        callableStatement.setInt(6, ssn);
        callableStatement.setDate(7, Date.valueOf(dateOfBirth));
        callableStatement.setString(8, phoneNumber);
        callableStatement.setInt(9, streetNumber);
        callableStatement.setString(10, streetName);
        callableStatement.setString(11, city);
        callableStatement.setString(12, state);
        callableStatement.setInt(13, zipcode);
        callableStatement.setString(14, email);

        callableStatement.executeUpdate();

        System.out.println("Investor registered successful!");

        callableStatement.close();

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
        System.out.println("5 -> Create portfolio");
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
            viewPortfolios(investor.getId());
            break;
          case 5:
            createPortfolio(investor.getId());
            break;
          case 6:
            requestPortfolioManager(investor.getId());
            break;
          case 7:
            searchSecurities();
            break;
          case 8:
            showLatestTransactions(investor.getId());
            break;
          case 9:
            makeTransaction(investor.getId());
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
                          resultSet.getDate("date_of_birth"), resultSet.getString("phone_number"),
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
      scanner.nextLine();
      System.out.print("Address Street Name : ");
      String streetName = scanner.nextLine();
      System.out.print("Address City : ");
      String city = scanner.nextLine();
      System.out.print("Address State (eg. MA/NY): ");
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

    /**
     * Deletes nominee for an investor id.
     *
     * @param investorId the investor id.
     */
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

    /**
     * Gets the portfolios of an investor id.
     *
     * @param investorId the investor id.
     */
  private void viewPortfolios(Integer investorId) {
      String sql = "{CALL view_portfolios(?)}";
      try {
          CallableStatement callableStatement = this.connection.prepareCall(sql);

          callableStatement.setInt(1, investorId);

          callableStatement.execute();

          ResultSet resultSet = callableStatement.getResultSet();
          boolean isResultPresent = false;

          while(resultSet.next()) {
              isResultPresent = true;
              System.out.println(
                      "ID: " + resultSet.getInt("portfolio_id") +
                              ", Portfolio Name: " + resultSet.getString("portfolio_name") +
                              ", Market Price in USD: " + resultSet.getDouble("portfolio_market_price")
              );
          }

          resultSet.close();

          if (!isResultPresent) {
              System.out.println("The investor has no portfolios.");
          }

          callableStatement.close();

      } catch(Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not fetch the portfolio details. Try again.");
      }
  }

    /**
     * Creates a portfolio for an investor id.
     *
     * @param investorId the investor id.
     */
  private void createPortfolio(Integer investorId) {
      getCurrencies();

      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter the portfolio details : ");
      System.out.print("Portfolio Name : ");
      String portfolioName = scanner.nextLine();
      System.out.print("Status (Aggressive/Conservative/Balanced/Income-oriented/Underperforming/Archived) : ");
      String status = scanner.next();
      System.out.print("Goal (Long-Term/Short-Term/Tax-Efficient/Retirement/Emergency Fund/Other) : ");
      String goal = scanner.next();
      System.out.print("Currency : ");
      String currency = scanner.next();
      String sql = "{CALL create_portfolio(?, ?, ?, ?, ?)}";

      try {
          CallableStatement callableStatement = this.connection.prepareCall(sql);

          callableStatement.setInt(1, investorId);
          callableStatement.setString(2, portfolioName);
          callableStatement.setString(3, status);
          callableStatement.setString(4, goal);
          callableStatement.setString(5, currency);

          callableStatement.execute();

          System.out.println("Portfolio created successful!");

          callableStatement.close();
      } catch (Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not add the portfolio. Try again.");
      }
  }

    /**
     * Adds a portfolio manager to a portfolio of the investor.
     *
     * @param investorId the investor id.
     */
  private void requestPortfolioManager(Integer investorId) {
      getPortfolioManagers();

      Scanner scanner = new Scanner(System.in);
      System.out.println("\nEnter the portfolio and manager details :");
      System.out.print("Portfolio manager employee id : ");
      int managerId = scanner.nextInt();
      System.out.print("Portfolio id : ");
      int portfolioId = scanner.nextInt();

      String sql = "{CALL choose_portfolio_manager(?, ?, ?)}";
      try {

          CallableStatement callableStatement = this.connection.prepareCall(sql);
          callableStatement.setInt(1, portfolioId);
          callableStatement.setInt(2, managerId);
          callableStatement.setInt(3, investorId);

          callableStatement.execute();

          System.out.println("Portfolio Manager added successful!");

          callableStatement.close();

      } catch (Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not add the portfolio manager to the portfolio. Try again.");
      }

  }

    /**
     * Searches for securities based on type and risk level.
     */
  private void searchSecurities() {
      Scanner scanner = new Scanner(System.in);
      System.out.print("What type of security do you want to search for? (stock/bond/mutual fund/etf) : ");
      String type = scanner.nextLine();
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
          System.out.println("ERROR: Could not fetch the securities data. Try again.");
      }
  }

    /**
     * Fetches the last 10 transactions of an investor.
     *
     * @param investorId the investor id.
     */
  private void showLatestTransactions(Integer investorId) {
      String sql = "CALL show_last_ten_transactions(?)";
      try {
          CallableStatement callableStatement = this.connection.prepareCall(sql);
          callableStatement.setInt(1, investorId);
          callableStatement.execute();
          
          ResultSet resultSet = callableStatement.getResultSet();
          boolean isResultPresent = false;

          while(resultSet.next()) {
              isResultPresent = true;
              System.out.println("ID: " + resultSet.getInt("transaction_id") +
                      "Date: " + resultSet.getDate("transaction_date") +
                      "Transaction Type: " + resultSet.getString("transaction_type") +
                      "Quantity: " + resultSet.getInt("quantity") +
                      "Security Name: " + resultSet.getString("security_name") +
                      "Portfolio Name: " + resultSet.getString("portfolio_name")
              );
          }

          resultSet.close();
          if(!isResultPresent) {
              System.out.println("No transaction present.");
          }

          callableStatement.close();
      } catch(Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not fetch the transactions. Try again.");
      }
  }

    /**
     * Saves the transaction of an investor.
     *
     * @param investorId the investor id.
     */
  private void makeTransaction(Integer investorId) {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter the transaction details : ");
      System.out.print("Transaction type (Buy/Sell) : ");
      String type = scanner.next();
      System.out.print("Security Id : ");
      int securityId = scanner.nextInt();
      System.out.print("Portfolio Id : ");
      int portfolioId = scanner.nextInt();
      System.out.print("Quantity : ");
      int quantity = scanner.nextInt();
      Date currentDate = Date.valueOf(LocalDate.now());

      String sql = "{CALL make_a_transaction(?, ?, ?, ?, ?, ?)}";

      try {
          CallableStatement callableStatement = this.connection.prepareCall(sql);
          callableStatement.setString(1, type);
          callableStatement.setDate(2, currentDate);
          callableStatement.setInt(3, securityId);
          callableStatement.setInt(4, portfolioId);
          callableStatement.setInt(5, quantity);
          callableStatement.setInt(6, investorId);

          callableStatement.execute();

          System.out.println("Transaction successful!");

          callableStatement.close();

      } catch (Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not add the transaction. Try again.");
      }
  }

    /**
     * Fetches all the currencies used in the company.
     */
  private void getCurrencies() {
      try {

          String sql = "{CALL get_currencies()}";

          CallableStatement callableStatement = this.connection.prepareCall(sql);

          callableStatement.execute();
          ResultSet resultSet = callableStatement.getResultSet();

          System.out.println("List of available Currencies : ");
          while(resultSet.next()) {
              System.out.println("Currency: " + resultSet.getString("currency") +
                      ", Conversion rate to USD: " + resultSet.getDouble("rate_to_usd"));
          }

          resultSet.close();
          callableStatement.close();

      } catch (Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not fetch the currencies. Try again.");
      }
  }

    /**
     * Fetches all the portfolio managers in the company.
     */
  private void getPortfolioManagers() {
      try {

          String sql = "{CALL get_portfolio_managers()}";

          CallableStatement callableStatement = this.connection.prepareCall(sql);

          callableStatement.execute();
          ResultSet resultSet = callableStatement.getResultSet();
          System.out.println("List of available Portfolio Managers : ");
          while(resultSet.next()) {
              System.out.println("ID: " + resultSet.getInt("employee_id") +
                      ", First Name: " + resultSet.getString("first_name") +
                      ", Last Name: " + resultSet.getString("last_name") +
                      ", Phone Number: " + resultSet.getString("phone_number") +
                      ", Email Id: " + resultSet.getString("email_id")
              );
          }

          resultSet.close();
          callableStatement.close();

      } catch (Exception e) {
          System.out.println(e.getMessage());
          System.out.println("ERROR: Could not fetch the currencies. Try again.");
      }
  }
}
