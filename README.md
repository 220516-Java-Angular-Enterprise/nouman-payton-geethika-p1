# Java Enterprise Foundations Project Requirements

## Project Description

For the foundations module of your training you are tasked with building an API that will support a new internal expense reimbursement system. This system will manage the process of reimbursing employees for expenses incurred while on company time. All registered employees in the company can login and submit requests for reimbursement and view their past tickets and pending requests. Finance managers can log in and view all reimbursement requests and past history for all employees in the company. Finance managers are authorized to approve and deny requests for expense reimbursement.

### Project Design Specifications and Documents

##### Relational Data Model
![Relational Model](https://github.com/220207-java-enterprise/assignments/blob/main/foundations-project/imgs/ERS%20Relational%20Model.png)

##### Reimbursement Types
Reimbursements are to be one of the following types:
- LODGING
- TRAVEL
- FOOD
- OTHER

##### System Use Case Diagrams
![System Use Case Diagrams](https://raw.githubusercontent.com/220207-java-enterprise/assignments/main/foundations-project/imgs/ERS%20Use%20Case%20Diagram.png)

##### Reimbursment Status State Flow
![Reimbursment Status State Flow](https://raw.githubusercontent.com/220207-java-enterprise/assignments/main/foundations-project/imgs/ERS%20State%20Flow%20Diagram.png)

### Technologies

**Persistence Tier**
- PostGreSQL (running on Docker)

**Application Tier**
- Java 8
- Apache Maven
- JDBC
- Java EE Servlets
- JSON Web Tokens
- JUnit
- Mockito

### Functional Requirements

~- An new employee or new finance manager can request registration with the system~
- ~An admin user can approve or deny new registration requests~
- The system will register the user's information for payment processing PUT -G
- ~A registered employee can authenticate with the system by providing valid credentials~
- An authenticated employee can view and manage their pending reimbursement requests GET -payton
- An authenticated employee can view their reimbursement request history (sortable and filterable) GET -payton
- ~An authenticated employee can submit a new reimbursement request~
- An authenticated finance manager can view a list of all pending reimbursement requests GET - payton
- An authenticated finance manager can view a history of requests that they have approved/denied GET - payton
- An authenticated finance manager can approve/deny reimbursement requests - PUT - G
- The system will send a payment request when a reimbursement request is approved - PUT -G
- ~An admin user can deactivate user accounts, making them unable to log into the system~
- ~An admin user can reset a registered user's password~

### Non-Functional Requirements

- Basic validation is enforced to ensure that invalid/improper data is not persisted
- User passwords will be encrypted by the system before persisting them to the data source
- Users are unable to recall reimbursement requests once they have been processed (only pending allowed)
- Sensitive endpoints are protected from unauthenticated and unauthorized requesters using JWTs
- Errors and exceptions are handled properly and their details are obfuscated from the user
- The system conforms to RESTful architecture constraints
- The system's is tested with at least 80% line coverage in all service and utility classes
- The system's data schema and component design is documented and diagrammed
- The system keeps detailed logs on info, error, and fatal events that occur

### Suggested Bonus Features
- Authenticated employees are able to upload an receipt image along with their reimbursement request
- The system notifies the user of changes to their account registration status by email
- The system notifies the user of changes to their reimbursement request status by email
- Document your API using a tool like OpenAPI/Swagger
- Run your application within a Docker container
- Automate builds using GitHub Actions
