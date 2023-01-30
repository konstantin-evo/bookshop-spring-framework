<a name="readme-top"></a>

<!-- PROJECT SHIELDS -->
![made-with](https://badgen.net/badge/Java/11/green)
![framework](https://badgen.net/badge/Framework/Spring%20Boot%2C%20Data%2C%20Security/green)
![build-tool](https://badgen.net/badge/Build/Maven/green)
![test](https://badgen.net/badge/Test/JUnit%2C%20Mockito/green)
![database](https://badgen.net/badge/Database/PostgreSql%20+%20Hibernate/green)
![front-end](https://badgen.net/badge/Front-end/JS%20+%20Jquery%2C%20HTML%2C%20CSS/green)
![ci-cd](https://badgen.net/badge/CI%2FCD/GitLab%2C%20K8s%2C%20Helm/green)

<!-- PROJECT LOGO -->
<div align="center">
  <a href="#">
    <img src="img/logo_md.png" alt="Logo" width="270" height="300">
  </a>
</div>

<!-- CONTACTS -->
<div>
    <p align="center">
        <br />
        <a href="#demo">View Demo</a>
        ·
        <a href=mailto:"konstantin.priluchnyi@gmail.com?subject=report%20Bug">Report Bug</a>
        ·
        <a href=mailto:"konstantin.priluchnyi@gmail.com?subject=Request%20Feature">Request Feature</a>
    </p>
    <p align="center">
        <a href=mailto:"konstantin.priluchnyi@gmail.com">
            <img src="https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white">
        </a>
        <a href="https://t.me/konstantin_evo">
            <img src="https://img.shields.io/badge/Telegram-2CA5E0?style=for-the-badge&logo=telegram&logoColor=white">
        </a>
        <a href="https://www.linkedin.com/in/konstantin-evo/">
            <img
                src="https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white">
        </a>
    </p>
</div>


<!-- TABLE OF CONTENTS -->

## Table of Contents

 <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#using-docker-compose-file">Using docker-compose file</a></li>
        <li><a href="#run-the-project-locally-with-postresql">Locally with PostreSQL</a></li>
      </ul>
    </li>
    <li>
      <a href="#usage">Usage</a>
      <ul>
        <li><a href="#structure-of-the-project">Structure of the project</a></li>
        <li><a href="#feature">Feature</a></li>
          <ul>
            <li><a href="#aop">AOP</a></li>
            <li><a href="#scheduler">Scheduler</a></li>
            <li><a href="#security">Security and OAuth 2.0.</a></li>
          </ul>
      </ul>
    </li>
    <li>
      <a href="#demo">Demo</a>
      <ul>
        <li><a href="#general-overview">General overview</a></li>
        <li><a href="#searching-(rest-api)">Searching (REST API)</a></li>
        <li><a href="#grading-books-and-writing-reviews">Grading books and writing reviews</a></li>
        <li><a href="#user-interaction-with-books">User interaction with books</a></li>
        <li><a href="#authentication">Authentication</a></li>
        <li><a href="#administration">Admin</a></li>
          <ul>
            <li><a href="#book">Book</a></li>
            <li><a href="#author">Author</a></li>
            <li><a href="#user-review">User review</a></li>
            <li><a href="#bookshelf">Bookshelf</a></li>
          </ul>
      </ul>
    </li>
</ol>

<!-- ABOUT THE PROJECT -->

## About The Project

Bookshop is a web-based application written with Java using Spring framework.

Application consist of tree services:

1. Main Springboot application
2. Database PostgreSQL
3. Springboot Admin application for health check (API, connection to DB, Spring Bean, Exception and so on)

The project supports the following functionality:

* Viewing information about books and authors
* Search for books by parameters: title, author, tag, genre, etc.
* Grading books and writing reviews
* Buy, add to favorites and archiving books
* Register New user registration and edit profile information

Login to the app is possible by using:

* Oauth 2.0. using Gmail service
* Plain password
* One-time code send to Email

Functionality of the admin role:

* Add, edit general information, change cover and safely delete book
* Edit author information
* Moderate user's reviews
* Editing user's bookshelves

![main_page.png](img%2Fmain_page.png)

<!-- GETTING STARTED -->

## Getting Started

### Using Docker Compose File

1. Clone the repo

   ```sh
   git clone https://github.com/konstantin-evo/bookshop-spring-framework.git
   ```

2. Change working directory to the Project dir

3. Start and run Bookshop app via `docker-compose up` command

**Important**: to upload the book cover from the server make sure that the application has access to the following directories from environment
variables `UPLOAD.PATH`, `DOWNLOAD.PATH`

```yaml
version: '3.5'

services:

  app:
    image: 'konstantinevo/bookshop:1.1.0'
    build:
      context: .
    container_name: bookshop-app
    depends_on:
      - db-api
      - admin-api
    ports:
      - '8080:8080'
    networks:
      - proxynet
    environment:
      - SPRING.BOOT.ADMIN.CLIENT.URL=http://admin-api:8081
      - SPRING.BOOT.ADMIN.CLIENT.INSTANCE.SERVICE-URL=http://app:8080/
      - SPRING.BOOT.ADMIN.CLIENT.INSTANCE.HEALTH-URL=http://app:8080/actuator/health
      - SPRING.BOOT.ADMIN.CLIENT.INSTANCE.MANAGEMENT-URL=http://app:8080/actuator
      - SPRING.DATASOURCE.URL=jdbc:postgresql://db-api:5432/bookshop
      - SPRING.DATASOURCE.USERNAME=postgres
      - SPRING.DATASOURCE.PASSWORD=postgres
      - UPLOAD.PATH=/apache-tomcat-9.0.55/external_uploads/book-covers
      - DOWNLOAD.PATH=/apache-tomcat-9.0.55/external_uploads/book-files

  db-api:
    image: 'postgres:13.1-alpine'
    container_name: db-api
    networks:
      - proxynet
    ports:
      - '5432:5432'
    expose:
      - 5432
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
      - POSTGRES_DB=bookshop
    healthcheck:
      test: [ 'CMD-SHELL', 'pg_isready -U postgres' ]
      interval: 10s
      timeout: 5s
      retries: 5

  admin-api:
    image: 'konstantinevo/bookshop-admin:1.0.0'
    container_name: 'bookshop-admin'
    hostname: 'admin'
    ports:
      - '8081:8081'
    networks:
      - proxynet

networks:
  proxynet:
    name: bookshop_network
```

### Run the Project locally with PostreSQL

In order to the project to be compiled and packaged correctly you must have a PostreSQL database version 13.1 (working with other versions will
probably cause errors).

Default environment variables for working with the database:

* spring.datasource.url=jdbc:postgresql://localhost:5432/bookshop
* spring.datasource.username=postgres
* spring.datasource.password=postgres

If your variables are different you can overwrite them in step 3.

1. Clone the repo

   ```sh
   git clone https://github.com/konstantin-evo/bookshop-spring-framework.git
   ```

2. Change working directory to the Project dir
3. Compile and package code

   ```sh
   mvn clean package
   ```

If you need to overwrite the variables associated with the database, add the following keys to the command above.

   ```sh
   -Dspring.datasource.url={{DATABASE_URL}} -Dspring.datasource.username={{DB_USERNAME}} -Dspring.datasource.password={{DB_PASSORD}}
   ```

**Important**: to upload the book cover from the server make sure that the application has access to the following directories from environment
variables `upload.path`, `download.path`. To overwrite it add the following keys to the `mvn clean package` command.

   ```sh
   -Dupload.path={{UPLOAD_PATH}} -Ddownload.path={{DOWNLOAD_PATH}}
   ```

4. Run the project

   ```sh
   mvn spring-boot:run
   ```

<p align="right">(<a href="#table-of-contents">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

## Usage

### Structure of the project

The project structure is divided into two parts

1. the business logic part (app directory)
2. web related part (web directory)

```
.
└── bookshop
├── app
│   ├── aspect
│   ├── config
│   ├── model
│   │   ├── dao
│   │   ├── entity
│   │   │   └── enumuration
│   └── services
└── web
├── controllers
├── dto
│   └── enumuration
├── exception
└── services
```

Resources to help you learn more:

| Resource            | Path                                            |
|---------------------|-------------------------------------------------|
| Swagger             | `src/main/resources/api/swagger.yaml`           |
| DB script           | `src/main/resources/db/script`                  |
| Message properties: | `src/main/resources/lang`                       |
| HTML                | `src/main/resources/spring-frontend`            |
| CSS                 | `src/main/resources/spring-frontend/assets/css` |
| JS                  | `src/main/resources/spring-frontend/assets/js`  |

Full structure of the project:

```
.
├── helm
│   └── bookshop
│       └── templates
├── img
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── bookshop
│   │   │               ├── app
│   │   │               │   ├── aspect
│   │   │               │   ├── config
│   │   │               │   │   └── security
│   │   │               │   │       ├── jwt
│   │   │               │   │       └── oauth
│   │   │               │   ├── model
│   │   │               │   │   ├── dao
│   │   │               │   │   ├── entity
│   │   │               │   │   │   └── enumuration
│   │   │               │   │   └── google
│   │   │               │   │       └── api
│   │   │               │   │           └── books
│   │   │               │   └── services
│   │   │               └── web
│   │   │                   ├── controllers
│   │   │                   │   ├── advice
│   │   │                   │   ├── book
│   │   │                   │   ├── rest
│   │   │                   │   └── user
│   │   │                   ├── dto
│   │   │                   │   └── enumuration
│   │   │                   ├── exception
│   │   │                   └── services
│   │   └── resources
│   │       ├── db
│   │       │   ├── changelog
│   │       │   └── script
│   │       ├── lang
│   │       └── spring-frontend
│   │           ├── admin
│   │           ├── assets
│   │           │   ├── css
│   │           │   ├── fonts
│   │           │   ├── img
│   │           │   ├── js
│   │           │   └── plg
│   │           │       ├── Slider
│   │           │       ├── form
│   │           │       ├── jCalendar
│   │           │       └── jQuery
│   │           ├── authors
│   │           ├── books
│   │           ├── documents
│   │           ├── fragments
│   │           ├── genres
│   │           ├── search
│   │           ├── stylus
│   │           └── tags
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── bookshop
│       │               ├── app
│       │               │   ├── model
│       │               │   │   └── dao
│       │               │   └── services
│       │               └── web
│       │                   ├── controllers
│       │                   ├── selenium
│       │                   └── services
│       └── resources
│           └── db
│               ├── changelog
│               └── script
```

<p align="right">(<a href="#table-of-contents">back to top</a>)</p>

### Feature

#### AOP

Spring AOP provides the way to dynamically add the cross-cutting concern before, after, or around the actual logic using simple pluggable
configurations. It makes it easy to maintain code in the present and future as well.

The Bookshop application implements the OAP approach as follows:

1. Logging (Auth, Exception, Debug)
2. ChangeProfile confirmation
3. Transaction

Transactions are a good example of using the OAP approach as it's always associated with the execution of other methods (doesn't exist in isolation).

Let's look at the implementation:

```java
public class TransactionAspect {

    private final TransactionRepository transactionRepo;
    private final BookRepository bookRepo;

    private static final String TOP_UP_BALANCE = TransactionInfo.TOPUP.getValue();
    private static final String ORDER_BOOK = TransactionInfo.BUY_BOOK.getValue();

    @Pointcut(value = "execution(public * com.example.bookshop.app.services.UserProfileService.topUpAccount(..))"
            + "&& args(sum, user, ..)", argNames = "sum, user")
    public void topUpAccount(Integer sum, User user) {
    }

    @Pointcut(value = "execution(public * com.example.bookshop.app.services.BookToUserService.orderBooks(..))"
            + "&& args(cookie, user, httpServletResponse, ..)", argNames = "cookie, user, httpServletResponse")
    public void orderBook(String cookie, User user, HttpServletResponse httpServletResponse) {
    }

    @Transactional
    @After(value = "topUpAccount(sum, user)", argNames = "sum, user")
    public void topUpAccountSuccess(Integer sum, User user) {
        Transaction transaction = new Transaction(sum, TOP_UP_BALANCE, user);
        transactionRepo.save(transaction);
        log.info(TOP_UP_BALANCE + ". User ID: " + user.getId() + ", sum: " + sum);
    }

    @Transactional
    @After(value = "orderBook(cookie, user, httpServletResponse)", argNames = "cookie, user, httpServletResponse")
    public void orderBookSuccess(String cookie, User user, HttpServletResponse httpServletResponse) {

        List<String> bookSlugs = CookieUtil.getValues(cookie);
        List<Book> books = bookRepo.findBooksBySlugIn(bookSlugs);

        books.forEach(book -> {
            Transaction transaction = new Transaction(book.getPrice(), ORDER_BOOK, user, book.getId());
            transactionRepo.save(transaction);
            log.info(ORDER_BOOK + ". User ID: " + user.getId()
                    + " Book ID: " + book.getId() + ", sum: " + book.getPrice());
        });
    }
}
```

You can view all implementations in the following directory `src/main/java/com/example/bookshop/app/aspect`

```
└── bookshop
├── app
│   ├── aspect
│   │   ├── AuthLogAspect.java
│   │   ├── ChangeProfileAspect.java
│   │   ├── CommonLogAspect.java
│   │   └── TransactionAspect.java
```

<p align="right">(<a href="#table-of-contents">back to top</a>)</p>

#### Scheduler

The popularity of a book is a non-negative number calculated using a formula based on the number of books bought, kept, viewed, etc.

From a performance point of view, it is not efficient to update this parameter every time the user changes the status of the book (purchase, view,
etc.).

For this purpose it is reasonable to use Scheduler and not to update all books at once, but to divide them into parts - this way the load on server
will be more balanced.

Let's look at implementation `src/main/java/com/example/bookshop/app/services/BookPopularityScheduler.java`:

```java
public class BookPopularityScheduler {

    private final BookRepository bookRepo;
    private final BookToUserRepository bookToUserRepo;

    @Value("${book.coefficient.paid}")
    private double bookCoefficientPaid;

    @Value("${book.coefficient.viewed}")
    private double bookCoefficientViewed;

    @Value("${book.coefficient.cart}")
    private double bookCoefficientCart;

    @Value("${book.coefficient.kept}")
    private double bookCoefficientKept;

    @Value("${book.time.popular.month}")
    private int timeOfRelevance;

    @Value("${book.size.popular.update}")
    private int numberOfBooksToUpdate;

    private int currentUpdatingPage;

    /*
    The book's popularity rating is calculated every hour
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void calculateBooksPopularity() {
        Pageable nextPage = PageRequest.of(currentUpdatingPage, numberOfBooksToUpdate);
        Page<Book> books = bookRepo.findActualBooks(nextPage);
        int totalPages = books.getTotalPages();

        if (totalPages > currentUpdatingPage) {
            books.forEach(this::updateBookRating);
            this.currentUpdatingPage++;
            log.info("The Rating of the books has been updated. Total books on the update list: {}, The books have been updated so far: {}, Time: {}",
                    totalPages * numberOfBooksToUpdate, currentUpdatingPage * numberOfBooksToUpdate, LocalDateTime.now());
        } else {
            this.currentUpdatingPage = 0;
            log.info("All the books have been updated. The popularity update process has started from the beginning of the list." +
                    "Time: {}", LocalDateTime.now());
        }
    }

    private void updateBookRating(Book book) {

        Timestamp relevanceTime = Timestamp.valueOf(LocalDateTime.now().minusMonths(timeOfRelevance));

        double cart = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.CART);
        double kept = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.KEPT);
        double paid = bookToUserRepo.countByBookAndTypeCode(book, BookToUserEnum.PAID);
        double viewed = bookToUserRepo.countByBookAndTypeCodeAndTimeAfter(book, BookToUserEnum.VIEWED, relevanceTime);

        double rating = bookCoefficientPaid * paid + bookCoefficientCart * cart + bookCoefficientKept * kept + bookCoefficientViewed * viewed;
        bookRepo.updatePopularity(rating, book.getId());
    }
}
```

<p align="right">(<a href="#table-of-contents">back to top</a>)</p>

#### Security

User authentication and authorisation is based on the use of JWT and described in the following directories.

```
.
└── bookshop
├── app
│   ├── config
│   │   └── security
│   │       ├── BookshopUserDetails.java
│   │       ├── BookshopUserDetailsService.java
│   │       ├── SecurityConfig.java
│   │       ├── jwt
│   │       │   ├── JWTLogoutHandler.java
│   │       │   ├── JWTRequestFilter.java
│   │       │   ├── JWTUtil.java
│   │       │   └── package-info.java
│   │               └── VolumeInfo.java
│   └── services
│       └── ...
│       └── UserRegisterService.java
```

When a request goes through the filter chain, JWTRequestFilter is executed only once for a given request to check User Authentication.

```java
public class JWTRequestFilter extends OncePerRequestFilter {

    private static final String JWT_COOKIE_NAME = "token";

    private final BookshopUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = CookieUtil.getValue(request, JWT_COOKIE_NAME);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtUtil.extractUsername(token);
            BookshopUserDetails userDetails = (BookshopUserDetails) userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(token, userDetails)) {
                setAuthentication(request, userDetails);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, BookshopUserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
```

Using OAuth 2.0, Bookshop app has access to the user's data without the disclosure of the user's credentials.

Bookshop uses Google as Authorization server.

The configuration is partly in the `application.properties` file, partly in the Services layer.

```
# GOOGLE API OAUTH
spring.security.oauth2.client.registration.google.client-id=325849759234-4ca54jlsu5fhjrpgo47ft4dsb22pcvjr.apps.googleusercontent.com
spring.security.oauth2.client.registration.google.client-secret=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
spring.security.oauth2.client.registration.google.scope=email, profile
```

```java
public class OauthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRegisterService userRegisterService;
    private final BookshopUserDetailsService userDetailsService;

    @Autowired
    public OauthSuccessHandler(@Lazy UserRegisterService userRegisterService,
                               BookshopUserDetailsService userDetailsService) {
        this.userRegisterService = userRegisterService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        BookshopUserDetails userDetails;
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

        try {
            userDetails = (BookshopUserDetails) userDetailsService.loadUserByUsername(oauthUser.getEmail());
        } catch (UsernameNotFoundException e) {
            userDetails = userRegisterService.registerNewUser(oauthUser);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        response.sendRedirect("/home");
    }
}
```

<p align="right">(<a href="#table-of-contents">back to top</a>)</p>

#### One-Time Passcode Verification via email and phone

<div>
   The user can sign in using a one-time code contained in the SMS or email message
   <a href="#authentication">(demo)</a>.
</div>

**Note**: SMS integration temporarily disabled due to instability of the Twilio free version service.

OTP Verification implemented via configuration and application.properties

```java
public class EmailConfig {

    @Value("${mail.username}")
    private String email;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.protocol}")
    private String protocol;

    private static final String IS_ENABLED = "true";

    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smpt.auth", IS_ENABLED);
        props.put("mail.smtp.starttls.enable", IS_ENABLED);
        props.put("mail.smtp.ssl.enable", IS_ENABLED);
        props.put("mail.debug", IS_ENABLED);

        return mailSender;
    }
}
```

```
# SMS INTEGRATION
twilio.account_sid=AC0043c73a8bf7fc0af0faccf6571f987d
twilio.auth_token=6248bce7744474c72a6233b8ed559e75
twilio.twilio_number=+18646686892
twilio.expire_time_sec=60
twilio.text="Your secret code is:
twilio.magic_code=123 123

# EMAIL INTEGRATION
mail.host=smtp.mail.ru
mail.port=465
mail.username=bookstore.kpriluch@mail.ru
mail.password=7Wcxg4if7vYWtP8ksk8x
mail.from=bookstore.kpriluch@mail.ru
mail.protocol=smtps
mail.subject=Bookstore email verification
mail.text=Verification code is:
```

For both type using OnetimeCodeService.

```java
public class OneTimeCodeService {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.twilio_number}")
    private String twilioNumber;

    @Value("${twilio.text}")
    private String twilioText;

    @Value("${mail.username}")
    private String bookstoreEmail;

    @Value("${mail.subject}")
    private String subjectEmail;

    @Value("${mail.text}")
    private String textEmail;

    /**
     * The SMS_CODE value is temporarily hardcoded due to the fact that the TWILIO service in free mode does not work stably,
     * and it is impossible to guarantee the operation of the operation of the service during launch
     */
    @Value("${twilio.magic_code}")
    private String smsCode;

    private final OneTimeCodeRepository oneTimeCodeRepository;
    private final SimpleMailSender mailSender;

    /**
     * The method sends SMS to a phone number
     * temporarily disabled due to support issues with Twilio service
     *
     * @param contact - telephone number
     * @return the generated value of the code
     */
    @SuppressWarnings("unused")
    public String sendSecretCodeSms(String contact) throws NoSuchAlgorithmException {
        Twilio.init(accountSid, authToken);
        String formattedContact = contact.replaceAll("[()-]]", "");
        String generatedCode = generateCode();

        Message.creator(new PhoneNumber(formattedContact),
                        new PhoneNumber(twilioNumber), twilioText + generatedCode)
                .create();

        return generatedCode;
    }

    public String sendSecretCodeEmail(String contact) throws NoSuchAlgorithmException {
        String generatedCode = generateCode();
        mailSender.send(subjectEmail, textEmail + generatedCode, bookstoreEmail, contact);
        return generatedCode;
    }

    public void saveCode(OneTimeCode oneTimeCode) {
        if (oneTimeCodeRepository.findByCode(oneTimeCode.getCode()) == null) {
            oneTimeCodeRepository.save(oneTimeCode);
        }
    }

    public boolean verifyCode(String code) {
        OneTimeCode oneTimeCode = oneTimeCodeRepository.findByCode(code);
        return (code.equals(smsCode)) || (oneTimeCode != null && !oneTimeCode.isExpired());
    }

    /**
     * The method generates a random numeric value in the format "XXX XXX"
     * For example, "158 927"
     */
    private String generateCode() throws NoSuchAlgorithmException {
        Random rand = SecureRandom.getInstanceStrong(); // SecureRandom is preferred to Random
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 6) {
            sb.append(rand.nextInt(9));
        }
        sb.insert(3, " ");
        return sb.toString();
    }

}
```

## Demo

### General overview

![demo_general_view.gif](img%2Fdemo_general_view.gif)
<p align="right">(<a href="#table-of-contents">back to top</a>)</p>

### Searching (REST API)

![demo_search_by_api.gif](img%2Fdemo_search_by_api.gif)
<p align="right">(<a href="#table-of-contents">back to top</a>)</p>

### Grading books and writing reviews

![demo_review.gif](img%2Fdemo_review.gif)
<p align="right">(<a href="#table-of-contents">back to top</a>)</p>

### User interaction with books

![demo_book_to_user_interaction.gif](img%2Fdemo_book_to_user_interaction.gif)
<p align="right">(<a href="#table-of-contents">back to top</a>)</p>

### Authentication

![demo_login.gif](img%2Fdemo_login.gif)
<p align="right">(<a href="#table-of-contents">back to top</a>)</p>

### Administration

#### Book

Create Book

![demo_admin_create_book.gif](img%2Fdemo_admin_create_book.gif)

Edit Book Cover

![demo_admin_cover_book.gif](img%2Fdemo_admin_cover_book.gif)

Edit book general info

![demo_admin_edit_book.gif](img%2Fdemo_admin_edit_book.gif)

#### Author

Edit information about author

![demo_admin_edit_author.gif](img%2Fdemo_admin_edit_author.gif)

#### User review

Hide review

![demo_admin_review_hide.gif](img%2Fdemo_admin_review_hide.gif)

Block user (not possible to write new review)

![demo_admin_block_user.gif](img%2Fdemo_admin_block_user.gif)

#### Bookshelf

Add book to user

![demo_admin_bookshelf.gif](img%2Fdemo_admin_bookshelf.gif)

Change viewed book to paid

![demo_admin_edit_bookstatus.gif](img%2Fdemo_admin_edit_bookstatus.gif)

