# Truck Packer

Packs all goods across a fleet of delivery trucks that each have their own volume, item limit with a fixed delivery cost. This chooses which trucks to use to miniise the cost

# Versions
- **Java 21** 
- **Maven 3.9+**

# build 
mvn package

Produces a runnable, dependent jar at "target/truck-packer-1.0.jar"

## Run 

```bash
java -jar target/truck-packer-1.0.jar <input.json> [output.txt]
```

- `input-file` — required, path to the input file in **JSON or YAML** (see format below). The format is chosen automatically from the file extentions (`.json`, `.yaml`, `.yml`).
- `output.txt` — optional, defaults to `output.txt` in the current directory.

Example, using the sample file included in this repo:
```bash
java -jar target/truck-packer-1.0.jar input/exampleinfo.json output.txt
java -jar target/truck-packer-1.0.jar input/exampleinfo.yaml output.txt
```
If the combination of the available trucks can carry all items, the program will print an error to the terminal and doesn't create an output file. The program also rejects unrecognised file extentions.

## Test

mvn test

Runs the JUnit 5 test suite (`src/test/java`), which cover:
1. A single truck large enough to hold everything (only that truck is used).
2. Goods that require multiple trucks, correctly split within each
   truck's own volume and item limits.
3. Cost minimisation: a cheaper truck is chosen over a pricier
   alternative when either could carry everything alone.
4. An item too large for any available truck (reported as no valid packing).
5. Malformed JSON input (reported as a clean error).
6. Well-formed YAML input parses and packs identically to equivalent JSON,
   and malformed YAML / unsupported file extensions fail cleanly
   (`InputLoaderTest`).

## Input format

Accepts **JSON or YAML** file chosen based on a file extension with the following structure:

```json
{
  "trucks": [
    {"id": "T1", "volume": 150, "maxItems": 6, "cost": 100},
    {"id": "T2", "volume": 150, "maxItems": 10, "cost": 150}
  ],
  "goods": [
    {"name": "TV", "volume": 30, "value": 500, "quantity": 3},
    {"name": "Radio", "volume": 10, "value": 100, "quantity": 5}
  ]
}
```

Equivalent YAML:
```yaml
trucks:
  - id: T1
    volume: 150
    maxItems: 6
    cost: 100
  - id: T2
    volume: 150
    maxItems: 10
    cost: 150
goods:
  - name: TV
    volume: 30
    value: 500
    quantity: 3
  - name: Radio
    volume: 10
    value: 100
    quantity: 5
```

| Field | Type | Meaning |
| `trucks` | array | The fleet of trucks available for this delivery |
| `trucks[].id` | string | Unique identifier for this truck |
| `trucks[].volume` | integer | Volume capacity of this truck |
| `trucks[].maxItems` | integer | Maximum number of items this truck can carry |
| `trucks[].cost` | integer | Fixed cost incurred if this truck is used |
| `goods` | array | List of good types available |
| `goods[].name` | string | Name of the good |
| `goods[].volume` | integer | Volume of a single unit of this good |
| `goods[].value` | integer | Sale value of a single unit of this good |
| `goods[].quantity` | integer | Number of units of this good available in stock |

## Output format

Plain text file listing the selected goods, the quantity of each taken, and
totals, e.g.:

```
Packed items:
- TV x2 (volume=60, value=1000)
- Radio x4 (volume=40, value=400)

Total items: 6
Total volume used: 100
Total value: 1400
```

##Project layout

```
tut-project/
  pom.xml
  input/
    exampleinfo.json       sample input file (JSON)
    exampleinfo.yaml       sample input file (YAML)
  src/main/java/rw344/truckpacker/    application source
    Item.java
    Truck.java
    TruckConfig.java
    InputLoader.java
    Packer.java
    Main.java
    OutputWriter.java
  src/test/java/rw344/truckpacker/    test suite
    PackerTest.java
    InputLoaderTest.java
  src/test/resources/                  fixture input files used by the tests
    malformed.json
    malformed.yaml
    random.txt
    valid_packing.yaml
    cheapest_truck_preferred.json
    single_truck_sufficient.json
    multi_truck_required.json
    items_too_large_no_packing.json
  .gitlab-ci.yml                CI pipeline definition
  .gitignore
  README.md
```
