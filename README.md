# adv__server :space_invader:
#### A simple multiplayer game created just for fun ([game client](https://github.com/ZERDICORP/adv__client)).
## How can I raise my server? :eyes:

#### Clone repository
```
$ git clone https://github.com/ZERDICORP/adv__server.git
```

#### Make sure you have java & jar installed
```
$ java --version
openjdk 17.0.3 2022-04-19
OpenJDK Runtime Environment (build 17.0.3+3)
OpenJDK 64-Bit Server VM (build 17.0.3+3, mixed mode)
$ jar --version
jar 17.0.3
```

#### Run the following command
```
$ cd adv__server/src/ && ./build && cd ../build/
```

#### Now you can start the server
> By default, port 8000 is listening  
> You can change it in the **resources/server.cfg** file
```
$ ./run
Server started listening on port 8000..
```

> P.S. don't forget to forward ports on your router (UDP connection) if you want your server to be available on the internet.  
> And you may have to open ports if you use Windows.
