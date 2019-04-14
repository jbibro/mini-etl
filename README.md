# mini-etl 

With Mini ETL you can convert input csv file based on transformations defined in configuration file.

## Build
```
./gradlew b
```

## Usage
> Example: src/com/github/jbibro/minietl/example/Example.kt

To use mini-etl you have to create configuration yaml file.

### Configuration file

Configuration file is composed of two parts:
1. Source Columns - represents columns from csv file that are used for transformation
* name - column name from csv header
* pattern - regex pattern used for validation
2. Target Columns - represents output columns. Each column can be built from source columns or can be a constant value
* name - new column name
* type - target type (possible values are: STRING, STRING_PROPER_CASED, INT, BIG_DECIMAL, DATE)
* value - source columns mapping

Example:
```yaml
sourceColumns:
  - name: Order Number
    pattern: "\\d+"
  - name: Year
    pattern: "\\d{4}"
  - name: Month
    pattern: "\\d{1,2}"
  - name: Day
    pattern: "\\d{1,2}"
  - name: Product Number
    pattern: "[-A-Z0-9]+"
  - name: Product Name
    pattern: "[A-Za-z\\s]+"
  - name: Count
    pattern: "[0-9.,]+"

targetColumns:
  - name: OrderID
    type: INT
    value: "#{Order Number}"
  - name: OrderDate
    type: DATE
    value: "#{Year}-#{Month}-#{Day}"
  - name: ProductId
    type: STRING
    value: "#{Product Name}"
  - name: ProductName
    type: STRING_PROPER_CASED
    value: "#{Product Name}"
  - name: Quantity
    type: BIG_DECIMAL
    value: "#{Count}"
  - name: Unit
    type: STRING
    value: "kg"
```

To transform csv file use following code:
```java
    MiniEtl
        .using(File("example/configuration.yaml"))
        .transform(File("example/orders.csv"))
```
