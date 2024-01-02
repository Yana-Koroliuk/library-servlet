# library-servlet

## Relevance

The library management web application is an essential tool designed to streamline the organization and administration of a library's book collection. This application is highly relevant and beneficial across multiple environments:
* **Public libraries:** Enhances community access to literary resources, facilitating easy book borrowing, returns, and catalog searches.
* **Educational institutions:** Serves schools, colleges, and universities by managing textbooks and educational materials, thus supporting the academic curriculum.
* **Personal libraries:** Ideal for individuals who want to maintain an organized record of their private book collections, tracking loans to friends and family.

This web application offers an intuitive, user-friendly interface that enhances the accessibility and management of library resources. It simplifies administrative tasks such as cataloging, user account management, and transaction processing, thereby contributing to an improved organizational system, user experience, and library service delivery.


## Concept

The library management web application is a versatile system designed to manage various aspects of a library.  It equips users with robust functionalities, including the creation, modification, and removal of book records, user profiles, categories, and borrowing orders. Patrons of the library can conveniently search for books, reserve books, manage their borrowing history, and process returns directly through the platform.

Library administrators benefit immensely from the system's capability to manage the book inventory with precision. New books can be cataloged, existing entries can be updated with current information, and obsolete records can be purged as needed. The application allows for the integration of author information and categorization, facilitating an organized and searchable library database.

A critical component of the application is the order management system. Users can place orders for book loans, specify the duration of borrowings, and track their order history. The feature of returning books is streamlined to update the system in real-time, ensuring that the inventory is always current.

Security is paramount, thus the application incorporates user authentication and role-based authorization mechanisms. These features guarantee that only authorized users can access specific functionalities, safeguarding the system's integrity and the privacy of user data.

Multi-language support is provided for English and Ukrainian.

## Program

Library Management Web Application offers a comprehensive suite of features for efficient library operations, user management, and service enhancement. Here's a detailed breakdown of its functionality:

* **sign up** - allows a new user to register in the system like reader.
* **login** - allows the user to log in to the system. 
* **search** - searches for books based on keyword of book's title or author. It displays all information about found books.
* **sort** - provided sorted by title, author, edition, date of publish.

User have **3 roles** with different functionality: **reader**, **librarian**, **admin**.
1. **Reader:**
* Has access to their personalized dashboard which displays reader's current active books orders, including details such as order ID, book title, authors, edition, language, start and end date of the order, and order status: **READER_HOLE**, **RECEIVED**, **OVERDUE**, **CANCELED**
* Ability to cancel orders for those pending or those that are in progress.
* A search feature to find books by title or authors.
* Access to reader's borrowing history for past reference.
* An overview of any fees incurred from late returns or penalties.
2. **Librarian:**
* View and manage all the orders placed by users.
* View specific reader subscriptions.
* Manage reader subscriptions, which involves canceling them or approving the subscription status.
3. **Admin:**
* Managing user accounts:
    - adding new librarians
    - blocking/unblocking users
* Book management:
    - adding books
    - editing books
    - deleting books

Without being authorized, you can only search for a book by various criteria. You need
to log in to use all other functionality.
All code has been verified by SonarLint. All dependencies are listed in **pom.xml** file.

## Built with
#### Back-end
* Java
* Maven - Dependency Management
* PostgreSQL - Database System
#### Dev dependencies
* JUnit - Testing Framework
* Mockito - Mocking Framework
#### Front-end
* JSP
* JavaScript
* CSS

## Help

Ask questions at [Yana Koroliuk](https://t.me/Koroliuk_Yana) and post issues on GitHub.

## LicenseGNU

Don't forget about licence. This program is GPL General Public licensed.