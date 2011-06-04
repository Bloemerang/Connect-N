#!/bin/sh

clean()
{
    echo "rm -r classes/connect_n"
    rm -r classes/connect_n
}

build()
{
    echo "scalac -d classes src/**/*"
    scalac -d classes src/**/*
}

run()
{
    scala -cp classes connect_n.Main
}

for cmd in $@
do
  case $cmd in
      clean)
	  clean
	  ;;
      build)
	  build
	  ;;
      run)
          run
          ;;
      *)
          echo "No clue what to do here"
          ;;
  esac
done	  