Import List
===========

Import List is an open-source extension for [Moneydance®]
(http://www.moneydance.com) monitoring a given directory for transaction files 
to import. For more information on where to download and how to install the 
extension please visit the [project page]
(http://my-flow.github.com/importlist/).

Build Prerequisites
-------------------
* Java Development Kit, version 6
* [Apache Ant™](http://ant.apache.org), version 1.7 or newer

Building the extension
----------------------
1. `git clone git@github.com:my-flow/importlist.git` creates a copy of the 
repository.
2. `ant importlist` compiles the extension (and signs it if an applicable key 
pair is found).

Signing the extension
---------------------
1. `ant genkeys` generates a passphrase-protected key pair.
2. `ant sign` signs the extension.

Running the extension
---------------------
After the build process has succeeded, the resulting extension file 
`dist/importlist.mxt` can be added to Moneydance®. However, it can also run in 
stand-alone mode, which allows for easy testing with fast feedback:
* `java -jar dist/importlist.mxt` runs the extension in stand-alone mode.

System properties:
* `java -Dlog4j.configuration=file:///$HOME/log4j.properties -jar 
dist/importlist.mxt` runs the extension using an [Apache log4j™]
(http://logging.apache.org/log4j/) configuration file.

Arguments:
* `java -jar dist/importlist.mxt -d` runs the extension in Moneydance®'s debug 
mode.
* `java -jar dist/importlist.mxt -basedirectory=$HOME/Downloads/` runs the 
extension using a predefined base directory.

Further Assistance
------------------
* [Import List API](http://my-flow.github.com/importlist/docs/api/index.html) 
* [Core API](http://www.moneydance.com/dev/apidoc/index.html).

License
-------
Copyright 2011 [Florian J. Breunig](http://www.my-flow.com). Import List is 
released under the [GNU General Public License, Version 3.0]
(http://www.gnu.org/licenses/gpl.html).
