# EntitySimilarityChecker

EntitySimilarityChecker is a simple entity comparison tool that currently works on a single table in a database. 

This tool will compare all entities based on weighted Levenshtein distance on multiple fields to determine similar entities. 

Currently the database table containig the entities required a separate column 'merge_group'. This column will be used to group similiar entities.

## Getting Started

Current setup is a maven project that can be imported in any IDE that supports maven projects.
### Prerequisites

MYSQL Database
JAVA >= 1.8 

### Installing (using eclipse as IDE)

Download or clone repository.

Import in Eclipse using 'import maven project'.

Setup database with at least one table that countains the entities that should be compared.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details


