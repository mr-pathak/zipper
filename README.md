Zipper
======


Compress Files using this simple yet power tool.

The tool compresses files and folders into a set of compressed files.
Each compressed file doesn't exceed a maximum size specified by user.

The same tool can decompress the files that it has generated earlier.

# Getting Started


## Prerequisites
Things required to run the tool:-
```
Java

```

## Install
1. Download the jar from [release][down]
2. To run the tool use following command
```
java -jar zipper-*.jar -h
```
This option displays help and usage.


## Running sample tests
### Case A: Compression
Compresses files in `input` folder and store the compressed files in `output` folder

**Example:-  Compress all files/folders in inputFolder**
> `java -jar zipper-*.jar -c /path/to/inputFolder /path/to/outputFolder 2`

```
here, -c means compress operation
which needs three arguments
	a) absolute path to inputFolder
	b) absolute path to outputFolder
	c) max size in Mb of output file to split (specified as an integer)
	
Note:
There is no restriction on naming the input or output folder.
```

### Case B: Decompression
Files to compressed are provided in `input` folder and de-compressed files in `output` folder

**Example:- Read compressed files from input folder and reconstruct the original files**
> `java -jar zipper-*.jar -d /path/to/inputFolder /path/to/outputFolder`

```
here, -d means de-compress operation
which needs two arguments
	a) inputFolder where archived files are present
	b) outputFolder where the de-compressed files will be written
```

### Case C: Multi-threading for faster execution
Each file can be handled separately by individual thread.
You can specify the option `-Dzthreads=n` to run the program with `n` threads to improve performance.
By default the program runs on single thread, i.e. `-Dzthreads=1`

**Example:- Running compress operation with 10 threads**
```
java -Dzthreads=10 -jar zipper-*.jar -c /path/to/inputFolder /path/to/outputFolder 2
```

## Building from source

1. Clone the source code
> `git  clone   https://github.com/mr-pathak/zipper.git`

2. Run Maven Build
> `mvn clean install`

# TODO
1. Improve error logging and UX for user
2. Introduce quiet mode for faster execution (can speed upto 30% faster)
3. Current parallelism is based on 1 file per thread.
    A better approach is to have multiple threads working on single file.
   This can be improved to read and write faster on large files
4. Improve current documentation   


[down]: https://github.com/mr-pathak/zipper/releases
[src]: https://github.com/mr-pathak/zipper.git

