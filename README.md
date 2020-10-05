# ATM Machine

## Project Description
> It is a __Spring Boot App__;  
> The app simulates an __ATM Machine__;  
### Basic functionalities 
> - `deposit` money on a bank account;
> - `withdraw` money from a bank account;
> - `query` a bank account's balance;  
### Technologies used
> - `thymeleaf` template engine used to render *HTML* files at `Runtime`
> - `SpringBoot` includes `Spring MVC` used to make the project a `web application`
> - `java 11` provides multiple functionalities when working with the stream API
> - `HTML/CSS/JS` used to build the web interface

## Running the APP
> There are a few ways in which you can run a `SpringBoot App`
> 1. Using `java -jar atmapp-1.0-ATM Machine.jar` command  
>>  - Preinstalled software requirements: `JRE`
>>  - The project comes with a packed jar file you can use the above command to run the project; 
> 2. Using `mvn spring-boot:run` command  
>>  - Preinstalled software requirements: `JRE` and `maven`

## Endpoints
### ATM  
> `HTTP GET request` __"/customer/atm/index"__
> - This endpoint loads an HTML page and provides a list of all the cards;
> - On each card you can click a button to __introduce__ it in the *ATM Machine*
>  
> `HTTP GET request` __"/customer/atm/introduce-card/{cardNumber}"__
> - This endpoint loads am HTML page that contains a form which asks for the `pin code` of the introduced card;
> - The submit button calls __"/customer/atm/authenticate-card"__ endpoint;
>  
> `HTTP POST request` __"/customer/atm/authenticate-card"__
> - This handler receives a form in the request body;
> - Validates the user input;
> - On wrong user input redirects to the __"/customer/atm/introduce-card/{cardNumber}"__
> - On correct user input loads an HTML file and attaches to it the __Bank Account__ associated with the __introduced card__
>  
> `HTTP GET request` __"/customer/atm/deposit"__
> - This endpoint is available only after introducing a `Card` in the `ATM` and providing the correct pin code;
> - It loads an HTML and attaches to it a form to get from the user the amount desired to be deposited on the bank account;
>  
> `HTTP GET request` __"/customer/atm/withdraw"__
> - This endpoint is available only after introducing a `Card` in the `ATM` and providing the correct pin code;
> - It loads an HTML and attaches to it a form to get from the user the amount desired to be withdrawn from the bank account;
> - On submit validates user input

### Bank Account
> `HTTP GET request` __"/admin/bank-account/index"__
> - This endpoint loads an HTML page and provides a list of all the Bank Accounts;
> - On each bank account you can click a button to __Add a card__, and  a button to see the bank account's __Transactions__
> - On this page you can also click on the __Create__ button which calls the __"/admin/bank-account/create"__ endpoint;
>      
> `HTTP GET request` __"/admin/bank-account/create"__
> - This endpoint loads an HTML file and attaches to it a form to get the required data in order to create a new Bank Account;
>    
> `HTTP POST request` __"/admin/bank-account/store"__  
> - Creates a new bank account with the given information
>  
> `HTTP GET request` __"/admin/bank-account/create-card"__
> - This endpoint loads an HTML file and attaches to it a form to get the required data in order to create a new card;
>  
> `HTTP POST request` __"/admin/bank-account/link-card"__
> - This endpoint receives the form discussed above
> - Validates data 
> - Creates a new card associated with the bank account
>  
> `HTTP GET request` __"/admin/bank-account/transactions-report"__
> - This endpoint loads an HTML file and attaches to it a list of all the `transactions` associated with a bank account

### Card
> `HTTP GET request` __"/admin/card/index"__
> - This endpoint loads an HTML page and provides a list of all the cards;
>
> `HTTP GET request` __"/admin/card/create"__
> - This endpoint loads an HTML file and attaches to it a form to get the required data in order to create a new card;
>    
> `HTTP POST request` __"/admin/card/store"__ 
> - Validates user input 
> - Creates a new card with the given information  
### Log
> `HTTP GET request` __"/admin/card/index"__
> - This endpoint loads an HTML page and provides a list of all the logs;
> - A Log it's added when something goes wrong, such as not finding a card/bank account, trying to enter a wrong pin code for more than 3 times etc. 

### Entity diagram
> - `ATM Entity Diagram.png` describes the relationships between a Card a Bank Account and Transaction Object
