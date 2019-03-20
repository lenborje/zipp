# Zipp
The purpose of this project is to illustrate how to combine Java Stream parallelism with the jar FileSystem.

To let the code do something useful, too, it implements a simple "zip" utility that can pack files in parallel.


## Build
Zipp builds with Java 8 or later.

`./gradlew assemble`

## Installation

Unpack the generated zip/tar (in `build/distributions`) wherever you want it.

## Usage
When called as an application, it will create zip archives from the files/directories given.

Add the unpacked installation (i.e. `<installDir>/zipp/bin`) to your PATH, then call:

`zipp [-p | --parallel] [-r | --recursive] zip-archive files...`

## Example

In this example the `log` directory contains 74 text files, all roughly 60MB. The computer is a MacBook Pro with 4 (8 with ht) cores.

Let's create a zip archive using the standard `zip` utility:

```
$ time zip -r zip.zip log
  adding: log/ (stored 0%)
  adding: log/out-38.log (deflated 82%)
  adding: log/out-10.log (deflated 82%)
  ...
  adding: log/out-21.log (deflated 82%)

real	2m55.252s
user	2m48.764s
sys		0m3.240s
```

Second, use `zipp` but let it execute serially:

```
$ time zipp -r zipp.zip log
Working on ZIP FileSystem jar:file:/Users/lennartb/tmp/zipp/zipp.zip, using the options '[RECURSIVE]'
   adding : log/out-38.log (deflated 82%)
   adding : log/out-10.log (deflated 82%)
   ...
   adding : log/out-21.log (deflated 82%)
All files/dirs entered, now closing...
A zip archive of the given files/dirs has been created.

real	3m0.118s
user	3m7.665s
sys		0m6.044s

```

Slightly worse time. Finally, let's do it in parallel:

```
$ time zipp -r -p zipp2.zip log
Working on ZIP FileSystem jar:file:/Users/lennartb/tmp/zipp/zipp2.zip, using the options '[RECURSIVE, PARALLEL]'
   adding : log/out-38.log (deflated 82%)
   adding : log/out-13.log (deflated 82%)
	...
   adding : log/out-43.log (deflated 82%)
All files/dirs entered, now closing...
A zip archive of the given files/dirs has been created.

real	0m40.411s
user	4m22.777s
sys		0m7.701s

```

A nice speed-up!

Further tests have shown that it scales up rather nicely, provided you have a fast enough disk.