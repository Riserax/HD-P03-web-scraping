# HD-P03-web-scraping

1. Execute **mvn clean install** (in main directory).
2. Run MySQL Server.
3. In Workbench: create user **hbstudent** (the same password) with all privileges, connect to local database **localhost:3306** with user *hbstudent* and create empty **booksdb** database. 
4. Click **Run 'Application'** in IntelliJ
5. Type **localhost:8081/getExtractedTransformedAndLoadedItems/5** in web browser.
6. See data results in schema's tables.
