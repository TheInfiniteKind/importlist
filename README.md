Import List
===========

Import List is an open-source extension for [Moneydance�](http://www.moneydance.com) monitoring a given directory for transaction files to import. For more information on how to install and use the extension please visit the [project page](http://my-flow.github.com/importlist/).

Building the extension from scratch
-----------------------------------

### Requirements
*	Java Development Kit, version 6
*	Apache Ant™, tested with version 1.8.2

### Instructions
1.	`git clone git@github.com:my-flow/importlist.git` creates a copy of the repository.
2.	`ant genkeys` generates a passphrase-protected key pair.
3.	`ant importlist` compiles and signs the extension.
4.	`dist/importlist.mxt` is the resulting extension file.

### Further Assistance
Consult the [Core API](http://www.moneydance.com/dev/apidoc/index.html) and the [Import List API](http://my-flow.github.com/importlist/docs/api/index.html).
