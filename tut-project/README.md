# Truck Packer

Select the most valuable combination of goods that fits into a delivery truck, subject tp a volume limit and a cap om the number of total items for the sake of safetly concerns.

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

- `input.json` — required, path to the input file (see format below).
- `output.txt` — optional, defaults to `output.txt` in the current directory.

Example, using the sample file included in this repo:
java -jar target/truck-packer-1.0.jar input/exampleinfo.json output.txt

If the combination of goods fit within the volume and item-count limits, the program prints an erro to the terminal and doesn't write an output file.


## Test

mvn test

Runs the JUnit 5 test suite (`src/test/java`), which cover:
1. Well formed input where a valid packing exists (and that the packing is
   actually optimal, not just non-empty).
2. Well-formed input where no valid packing can be found.
3. Ill-formed (malformed) input that will call an error.

## Input format

JSON file with the following structure:

```json
{
  "truckVolume": 150,
  "maxItems": 10,
  "goods": [
    {"name": "TV", "volume": 30, "value": 500, "quantity": 3},
    {"name": "Radio", "volume": 10, "value": 100, "quantity": 5}
  ]
}
```

| Field | Type | Meaning |

| `truckVolume` | integer | Total volume capacity of the truck |
| `maxItems` | integer | Maximum total number of items allowed on the truck |
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
  input/exampleinfo.json       sample input file
  src/main/java/rw344/truckpacker/    application source
  src/test/java/rw344/truckpacker/    test suite
  src/test/resources/                  fixture input files used by the tests
  .gitlab-ci.yml                CI pipeline definition
```
