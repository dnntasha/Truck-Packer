# Truck Packer

Packs all goods across a fleet of delivery trucks that each have their own volume, item limit with a fixed delivery cost. The program choose which trucks to use so that all goods are transported while keeping the total delivery cost low.

# Test reports (GitLab Pages)

Convergence and mutation testing reports from the latest pipeline on main:

- **JaCoCo** - statement and branch coverage
- **PIT** - mutation testing score


### Raw reports from the latest pipeline (CI artifacts)

- [JaCoCo HTML](https://git.cs.sun.ac.za/Computer-Science/rw344/2026/27887707-RW344-Tutorials/-/jobs/artifacts/main/file/tut-project/target/site/jacoco/index.html?job=test)
- [JaCoCo XML](https://git.cs.sun.ac.za/Computer-Science/rw344/2026/27887707-RW344-Tutorials/-/jobs/artifacts/main/file/tut-project/target/site/jacoco/jacoco.xml?job=test)
- [PIT HTML](https://git.cs.sun.ac.za/Computer-Science/rw344/2026/27887707-RW344-Tutorials/-/jobs/artifacts/main/file/tut-project/target/pit-reports/index.html?job=test)
- [PIT XML](https://git.cs.sun.ac.za/Computer-Science/rw344/2026/27887707-RW344-Tutorials/-/jobs/artifacts/main/file/tut-project/target/pit-reports/mutations.xml?job=test)


# Versions
- **Java 21** 
- **Maven 3.9+**

# build 
Run inside `tut-project/`

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
The Program exits with code `0` on success and `1` on any error. Errors are printed to the terminal and no output file is written when:
- no input file is give,
- the input file is missing, malformed, or has an unsupported extention.
- The available trucks **cannot** carry all the goods,
- The output file cannot be written


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
Truck T1 (volume=120, Items=6):
- TV x3 (volume = 90, values =1500)
- Radio x3 (volume = 30, values =300)

Truck T2 (volume=20, Items=2):
- Radio x2 (volume = 20, values =200)

Total cost: 250
Total items: 8
Total volume used: 140
Total value: 2000
```

## Packing approach 

Trucks are considered from cheapest to most expensive, and goods from largest
to smallest volume (first-fit decreasing). Each unit goes into the cheapest
truck that still has enough volume and item space. If any unit fits in no
truck, no valid packing exists and an error is reported.

## Testing

Run from inside `tut-project/`:

```bash
mvn test                                                      # tests + JaCoCo coverage report
mvn clean test org.pitest:pitest-maven:mutationCoverage       # tests + coverage + PIT mutation report
```

Reports are written to:

- `target/site/jacoco/index.html` (HTML), `jacoco.xml`, `jacoco.csv`
- `target/pit-reports/index.html` (HTML), `mutations.xml`

| Test class | What it covers |
|---|---|
| `PackerTest` | Single truck, multiple trucks, cheapest truck preferred, items too large, per-item volume/value |
| `InputLoaderTest` | JSON and YAML (`.yaml`/`.yml`) parsing, malformed files, unsupported extensions |
| `OutputWriterTest` | Exact output file format; unwritable output path |
| `MainTest` | Argument handling, every error path, default and explicit output paths |

## CI pipeline
 `.gitlab-ci.yml` runs on every push:

1. **test** – runs all tests, JaCoCo and PIT in one job; reports kept as artifacts.
2. **pages** – on `main` only, publishes both HTML reports to GitLab Pages.

All artifacts expire after **1 day**.

## Project layout

```
.gitlab-ci.yml                  CI pipeline (tests, coverage, mutation, Pages)
.editorconfig                   shared formatting rules
README.md
tut-project/
  pom.xml                       build, JaCoCo and PIT configuration
  input/exampleinfo.json        sample input
  pages/index.html              landing page for the published reports
  src/main/java/rw344/truckpacker/
    Main.java                   command-line entry point
    InputLoader.java            reads JSON/YAML input
    TruckConfig.java            trucks + goods from the input
    Truck.java                  one truck
    Item.java                   one good
    Packer.java                 packing algorithm
    OutputWriter.java           writes the result file
  src/test/java/rw344/truckpacker/
    PackerTest.java  InputLoaderTest.java  OutputWriterTest.java  MainTest.java
  src/test/resources/           input files used by the tests
tut0/
```
