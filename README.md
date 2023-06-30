# di22-earnit5

**Earn It Project**

_Group 5_

## Project description

The goal of this project is to develop a new platform where students can submit their hours and companies can approve them, with an invoice automatically generated upon approval.

The platform will have three main types of users:
- Students
- Companies
- Earn It Staff

Students will be able to view jobs they're linked to and fill in their hours worked on a day-by-day basis. At the end of each week, students will confirm their hours and send them to the company for approval. Additionally, students will be able to include notes or comments to provide context or explain any discrepancies related to their hours worked. Once approved, an invoice is generated, and all invoices from the past year will be accessible to the student.

Companies will be able to view all students they've hired and approve hours worked at the end of each week. If a company notices any suspicious submissions, they can flag them, and the Earn It staff will investigate. Invoices will be generated once hours are approved and kept accessible to the company, which can decide how long they want to keep them before deletion.

Earn It Staff will link students and companies for a role and have an overview of all students and companies on the platform. They won't necessarily see the hours breakdown but will be able to view all flagged submissions and resolve them by accepting or rejecting the hours. It can be assumed that the decision will be made through conversations that happen outside the platform.


## Tools

- During the procedure of project implementation, we are using Trello board to have a visual idea of all the requirements and user stories. Moreover, it is used to split the cards there, namely the responsibilities to each of the members of the group. You can access our Trello board via the link: https://trello.com/b/BNmNTXqN/m4-project 

- During the course, scrum master of that week receives an email with a username and password for the Provider Platform-as-a-Service (PaaS). These login credentials are shared among all group members. Here you can find the URL to access the platform: http://earnit5.paas.hosted-by-previder.com/earnit/login 

- To use the sorted data, the database schema was created and added to our GitLab. You can find the database schema below: 

User(id PK, email NOT NULL, password NOT NULL, salt NOT NULL)

Student(id PK, email NOT NULL, password NOT NULL, salt NOT NULL, name NOT NULL, university, birthdate NOT NULL, study NOT NULL, skills, btw_number, UNIQUE(btw_number))

Company(id PK, email NOT NULL, password NOT NULL, salt NOT NULL, name NOT NULL, location NOT NULL, field, contact NOT NULL, kvk_number, logo, UNIQUE(kvk_number))

Admin(id PK, email NOT NULL, password NOT NULL, salt NOT NULL)

Employment(eid PK, sid NOT NULL, cid NOT NULL, job_title, job_description, salary_per_hour, FK sid REF Student(id), FK cid REF Company(id), UNIQUE(sid, cid))

Submission(submission_id PK, hours NOT NULL, eid NOT NULL, comment, worked_date, FK eid REF Employment(eid), UNIQUE(worked_date, eid))

Invoice(iid PK, week, eid NOT NULL,  total_salary, date_of_issue, FK eid REF Employment(eid), UNIQUE(eid))

Flag(eid PK, week PK, year PK, week NOT NULL, year NOT NULL, status, suggested_hours, FK eid REF Employment(eid))



## Files allocation

There are 4 folders and a CHANGELOG file in our gitlab repository:

- Design folder:

Contains all the UML Diagrams (Class daigram, Use Case Diagram) which were used to have a visual idea to create the foundation for the project, Story map, SQL Database Schema.

- Meetings folder:

Contains Assignments Folders with 3 more folders: Assignment 1, Assignment 2, Assignment 3 respectively. Each of them contains all the documents we needed to create, such as Report on UML diagrams, Security analysis, Testing Report, and some additional documents for Academic skills Workshops. 

- Src folder:

Contains the main complete project source code and testing code with other testing-related recources.


## Usage

The primary usage of our project based on the idea to link students and company with each other and ensure the interaction between them, involving the Earn It staff and, to the certain extent, Earn It platform.

As a very useful and captivating example, imagine the situation when the company should review the progress of every employee (in our case student), as well as each student should submit his or her working hours and progress. 

With our contribution and implementation of this project, the steps mentioned above will not be so time-consuming and stressful, as student and company are able to do everything in a platform digitally, with the interaction of Admin, who can either approve flagged submissions by company, or deny it.

## Support

If there is a struggle with interacting with our platform, you can immediately contact one of our group members through our student emails, contact numbers, Discord, or directly message us via Canvas.

Radu Mungu s3091554,
Khanh Nguyen s2950944,
Cuong Bui s2966174,
Kamran Babayev s2882582,
Indigo Carelsz s2990210,
Hieu Chu s2948923.

## Roadmap

The roadmap is sorted by the releases order:

For Student:

1. Show linked jobs 
2. Login 
3. Logout
4. Page restriction
5. Search companies
6. Registration
7. Submit progress
8. List students invoices
9. Export
10. Review progress of each week

For Company:

1. Login 
2. Logout 
3. List linked students
4. Registration 
5. Search students
6. View submitted submissions 
7. Flagging submissions 
8. List employees' invoices 
9. Export 

For admin:

1. Linking jobs 
2. Search function
3. Manage flagged submissions 
4. Create new jobs 


## Authors and acknowledgment

The people who made a contribution to the project are the group members:

Radu Mungu (back-end developer),
Khanh Nguyen (back-end developer),
Cuong Bui (front-end developer),
Kamran Babayev (front-end developer),
Indigo Carelsz (back-end developer),
Hieu Chu (front-end developer)

In addition, we, as the developers of Earn It Group 5, would like to express gratitude to our Mentor, Remus Niculescu (TA), who was guiding us during the whole path of the project and provided us with all updates, relating to the Sprint Reviews requirements and client's preferences. 
