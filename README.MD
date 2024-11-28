# Capital Gains Calculator

A Java application that calculates taxes on stock trading operations following specific business rules for capital gains.

## Solution Architecture

### Overview
The application follows SOLID principles and uses a combination of Strategy and Chain of Responsibility patterns to handle different types of operations (buy/sell) and their tax calculations.

### Core Components

#### 1. Operation Handlers
- `BuyOperationHandlerImpl` and `SellOperationHandlerImpl` provide specific implementations
- Reason: Separates concerns and makes it easy to add new operation types

#### 2. Calculators
- `MediumPriceCalculator` for weighted average price calculations
- `TaxCalculator` for tax computations
- Reason: Isolates complex calculations and business rules, improving maintainability

#### 3. Context Management
- `CapitalGainsContext` maintains state between operations
- Tracks stocks quantity, medium price, and accumulated losses
- Reason: Encapsulates state management, preventing global variables

#### 4. Main Handler
- `CapitalGainsTaxCalculatorHandler` orchestrates the operation processing
- Uses dependency injection for flexibility and testability
- Reason: Provides a clean high-level interface while managing complexity internally

### Design Decisions

1. **Immutable Operations**
    - Operations are immutable once created
    - Reason: Prevents state-related bugs and ensures thread safety

2. **Dependency Injection**
    - Used throughout the application
    - Reason: Improves testability and follows SOLID principles

3. **Chain of Responsibility**
    - Allows sequential processing of operations
    - Reason: Makes it easy to add new operation types without modifying existing code

4. **Strategy Pattern**
    - Different strategies for different operation types
    - Reason: Encapsulates varying algorithms and makes them interchangeable

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6.3 or higher

### First Time Setup
When building the project for the first time, Maven will automatically:
- Download all required dependencies
- Install necessary plugins
- Create local repository cache

No manual installation of dependencies or plugins is required.

### Commands

#### First Build
For the first build, simply run:
```bash
mvn clean compile
```

#### Compile the Program
```bash
mvn clean compile
```
#### Run Tests

```bash
mvn test
```

#### Build Executable

```bash
mvn clean package
```

#### Run the Program

Use the generated script in the target folder:

```bash
./target/appassembler/bin/capitalgains
```

### Input Format
The program accepts JSON input in the following format:

```json
[{"operation":"buy", "unit-cost":10.00, "quantity":100}, {"operation":"sell", "unit-cost":15.00, "quantity":50}]
```

You can provide input in two ways:

1. **Direct Input** : Type or paste the JSON directly when running the program:

   ```bash
   ./target/appassembler/bin/capitalgains
   [{"operation":"buy", "unit-cost":10.00, "quantity":100}, {"operation":"sell", "unit-cost":15.00, "quantity":50}]
   ```

2. **Input Redirection** : Use a file containing the JSON input:

   ```bash
   ./target/appassembler/bin/capitalgains < input.txt
   ```
   Example input.txt:

   ```json 
   [{"operation":"buy", "unit-cost":10.00, "quantity":100},{"operation":"sell", "unit-cost":15.00, "quantity":50}]
   
   ```
   last line in the file should be empty.

## Dependencies and Plugins

### Main Dependencies

1. **Jackson (com.fasterxml.jackson.core)**
   ```xml
   <dependency>
       <groupId>com.fasterxml.jackson.core</groupId>
       <artifactId>jackson-databind</artifactId>
       <version>2.15.2</version>
   </dependency>
   ```
   * Purpose: JSON processing

2. **JUnit Jupiter (org.junit.jupiter)**
   ```xml
   <dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
    </dependency>
   ```
    * Purpose: Testing framework

3. **Mockito (org.mockito)**
   ```xml
   <dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.3.1</version>
    <scope>test</scope>
    </dependency>
   ```
   
   * Purpose: Mocking framework for tests

### Plugins

1. **maven-assembly-plugin**
   ```xml
   <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>3.6.0</version>
    <configuration>
        <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
        <archive>
            <manifest>
                <mainClass>org.capitalgains.Main</mainClass>
            </manifest>
        </archive>
    </configuration>
    </plugin>
   ```

    * Purpose: Creates executable JAR with dependencies

2. **maven-surefire-plugin**
   ```xml
   <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <includes>
            <include>**/*Test.java</include>
            <include>**/*E2ETest.java</include>
        </includes>
    </configuration>
    </plugin>
   ```

    * Purpose: Test execution

3. **maven-compiler-plugin**
   ```xml
     <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>11</source>
        <target>11</target>
    </configuration>
    </plugin>
   ```
    * Purpose: Java compilation

4. **appassembler-maven-plugin (org.codehaus.mojo)**
   ```xml
   <plugin>
       <groupId>org.codehaus.mojo</groupId>
       <artifactId>appassembler-maven-plugin</artifactId>
       <version>2.1.0</version>
       <configuration>
           <programs>
               <program>
                   <mainClass>org.capitalgains.Main</mainClass>
                   <id>capitalgains</id>
               </program>
           </programs>
           <repositoryLayout>flat</repositoryLayout>
           <repositoryName>lib</repositoryName>
           <generateRepository>true</generateRepository>
           <showConsoleWindow>true</showConsoleWindow>
       </configuration>
       <executions>
           <execution>
               <phase>package</phase>
               <goals>
                   <goal>assemble</goal>
               </goals>
           </execution>
       </executions>
   </plugin>
   ```
   * Purpose: Creates platform-specific scripts and a repository structure for running Java applications