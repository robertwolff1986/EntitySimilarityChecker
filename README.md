# EntitySimilarityChecker

EntitySimilarityChecker is a simple entity comparison tool that currently works on a single table in a database. 

This tool will compare all entities based on weighted Levenshtein distance on multiple fields to determine similar entities. 

Currently the database table containig the entities required a separate column 'merge_group'. This column will be used to group similiar entities.

Sample input:
![alt text](https://github.com/robertwolff1986/EntitySimilarityChecker/blob/master/images/source.png "input")

Sample output:
![alt text](https://github.com/robertwolff1986/EntitySimilarityChecker/blob/master/images/target.png "ouput")


## How does it work
Basically the application takes all entities and compares the configured columns to all other entities using a levenshtein distance. The result for each column can be weighted to meet individual requirements. 
For example the name is more identifying that a zip code, therefore the name should be weighted higher.

There are also a few extra rounds to find similar entities that are not that obvious at the first glance. 
1. If 2 columns are compared it will be checked if the single words in one column (splitted by ' ') are entirely contained in the other. If so, both columns will be treated as 100% similar.

Example: 

entity1:name=Robert Wolff

entity2:name=Wolff

This will be treated as a 100% match looking at the name column.

2. If 2 columns are compared, the levenshtein distance of all permutations of all words in the column (splitted by ' ') will be calculated and the optimal distance will be used to rate the similarity.

Example: 

entity1:name=Robert Wolff

entity2:name=Wolff Robert

The first entity will eventually be permutated to 'Wolff Robert' too, resulting in a 100% match.

3. Matches will be matched again recursively to match entities that woudnt have matched with the original entity.

Example: 

entity1:name=Robert

entity2:name=Robert Wolff

entity3:name=Wolff

Entity 1 is contained in entity 2, resulting in a match considerung the containing check mentioned at 1. 
Entity 2 will contain entity 3 using that same mechanism.
All 3 entities will be treated similar at the end of the process.

## Getting Started

Current setup is a maven project that can be imported in any IDE that supports maven projects.
### Prerequisites

MYSQL Database
JAVA >= 1.8 

### Installing (using eclipse as IDE)

Download or clone repository.

Import in Eclipse using 'import maven project'.

Setup database with at least one table that countains the entities that should be compared(use db.sql in resources).

Fill table with data and run application. 

If you want to add a new entity that should be compared, create a new Entity, a new CrudRepository and table with merge_group column.

## Configuration
Most of the parameters are hard coded atm.
Create CheckConfigurations at application startup like demonstrated in Mainclass:

CheckConfiguration firstCheckConfiguration = new CheckConfiguration("name", 10);
		firstCheckConfiguration.setCountSingleWordEqualsAsSimilar(true);
		firstCheckConfiguration.setSwitchWords(true);

You have to try a few combinations of weights to get the right configuration for your needs.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details


