# Spring Telegram Channel Manager
**Telegram bot** built with Spring Framework for managing Telegram channels, groups, and subscriptions. The bot provides various features such as user profile management, linking groups, sending posts, and handling subscription logic.

---

## 🧩 Problem Solved

Imagine you have 15 Telegram channels with very similar content.
To be able to post on time, your business would need ~7 people, each acting as a manager for ~2 channels.

However, this bot solves that problem: it allows you to link all groups to one person who can handle posting alone.
You only need to write a post once, and it will automatically be sent to all channels linked to that person.
***No more need for additional staff!***

---

## 🚀 Features

- **User Profile Management:**
    - View and manage user profile.
    - Check subscription status and validate dates.
- **Group Management:**
    - Link or unlink groups to a user account.
- **Subscription Handling:**
    - Notify users of expired subscriptions.
    - Purchase subscription functionality.
- **Log Management:**
    - Logs user interactions and errors to a dedicated channel.
- **Dynamic Keyboard:**
    - Custom inline keyboard menus for navigation.

---

## 🧰 Tech stack used in this project

- **Java 21**
- **Spring Boot** (Core, DI, Annotations)
- **Hibernate** (ORM)
- **TelegramBots API** (Telegram Bot library)
- **PostgreSQL** (Database)
- **Lombok** (for reducing boilerplate code)
- **Maven** (Build tool)
- **Slf4j** (Logging)
---

## 📂 Project Structure
```
src/main/java
├── com.moldavets.SpringTelegramChannelManager
│   ├── bot                // Telegram Bot API logic
│   ├── config             // Configuration bot
│   ├── dao                // Database interaction
│   ├── entity             // ORM Entity classes
│   ├── service
│   │   ├── action         // Actions
│   │   ├── command        // Commands
│   │   ├── message        // Message handling logic
│   ├── utils              // Utilities (logging, message processing)
│   └── SpringTelegramChannelManagerApplication.java // main
```
---

## ⚙️ Installation and Setup

**1. Clone the Repository**
```
git clone https://github.com/BohdanMoldavets/SpringTelegramChannelManager.git
cd SpringTelegramChannelManager
```

**2. Database Configuration**

+ Create a PostgreSQL database and run the following script:

```
\src\main\resources\data.sql
```

+ Update the database connection settings in the application.properties file:
```
\src\main\resources\application.properties

spring.application.name=SpringTelegramChannelManager

bot.name= #Your bot name
bot.key= #Your bot token
bot.log.chat.id= #Bot log telegram chat

spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:postgresql://localhost:5432/managment-telegram-channel-bot
spring.datasource.username= #Db user
spring.datasource.password= #Db password
spring.jpa.show-sql=true
```

**3. Install Dependencies**

```
cd SpringTelegramChannelManager
.\mvnw clean install
```

**4. Start the Application**

```
.\mvnw spring-boot:run
```

## 🛑 FAQ
+ **Unable to start the application**:
  Please make sure that all data in the application.properties file is filled in correctly. Also make sure that the bot name and its token match the one you created in **[BotFather](https://telegram.me/BotFather)**

# Contact

+ Email: steamdlmb@gmail.com
+ [Telegram](https://telegram.me/moldavets)
+ [Linkedin](https://www.linkedin.com/in/bohdan-moldavets/)


