linker
======

A project for the course Management of Big Data of the University of Twente

### Idea
`What if we can see immediately the alternatives for everything in the world?`

Nowadays advertisers have a great influence on people. 
Using advertisements in media such as the Internet, television, and radio they try to convince people to buying certain products. 
However, advertisements can make the market seem smaller than it actually is.
Not every company has the ability to advertise their product on the same scale as other companies.
That pushes people in certain directions, away from alternatives for which is advertised in a different way (e.g. another medium), in lesser extent, or not at all.

Our idea is that for every product in the world there is an alternative and that this alternative can be better than the original product.
What we would like to do is to give people the option to find those alternatives and to compare them against each other and the original product. 
As advertisement is not always done for alternatives it can be difficult to find them.
However, big data analysis can solve this problem.

### Installation
###### MapReduce
To run this program you have to build the code using build.xml and run the linker script in eclipse with the following parameters:
```
-in /path/to/arc/file -out /where/to/put/the/output/dir
```

Hadoop 0.20 must be installed and added to the classpath and also pig 0.10.0 when using pig. The jars of lib/* must be added to the include path as well.

###### Frontend
The requirements for the frontend are PHP5+ and a MySQL database. 
To run the frontend, the dataset must be loaded into a database. Then the settings of the database in `src/php/index.php` should be setup. 

### Structure
Our paper can be found in `doc` directory. The job-report can be found in `report` directory. 

