# cheshire-cat

FIXME

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

Run with Leiningen 2.8.1 in particular.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To run a standalown clojurescript repl

    lein trampoline cljsbuild repl-rhino

To run a browser based clojurescript repl

    lein trampoline cljsbuild repl-listen

Compile clojurescript

    lein cljsbuild auto

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2019 FIXME
