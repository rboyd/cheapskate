# cheapskate

Interface with the Namecheap API.

## Usage

Create a "config.clj" file in your classpath (e.g. project-dir/etc) with your API
credentials. Example:

```clojure
{:auth {:ApiUser "your API username"
        :ApiKey "your API key"
        :UserName "your namecheap username"
        :ClientIp "your IP address"}

 :service-url "https://api.namecheap.com/xml.response"}
```

Invoke with ```(cheapskate.core/get-domains)```


## License

Copyright © 2012-2013 Robert Boyd

Distributed under the Eclipse Public License, the same as Clojure.
